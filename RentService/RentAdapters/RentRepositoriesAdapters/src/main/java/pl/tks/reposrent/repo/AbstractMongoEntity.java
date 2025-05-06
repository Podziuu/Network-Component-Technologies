package pl.tks.reposrent.repo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationOptions;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import pl.tks.reposrent.config.MongoProperties;

import java.util.ArrayList;

public abstract class AbstractMongoEntity implements AutoCloseable {
    private final ConnectionString connectionString;
    private final MongoCredential credential;
    protected MongoClient mongoClient;
    protected MongoDatabase database;

    public AbstractMongoEntity(MongoProperties properties) {
        if (properties.getUri() == null || properties.getUri().isEmpty()) {
            throw new IllegalArgumentException("Mongo URI cannot be null or empty");
        }
        if (properties.getDatabase() == null || properties.getDatabase().isEmpty()) {
            throw new IllegalArgumentException("Database name cannot be null or empty");
        }

        this.connectionString = new ConnectionString(properties.getUri());
        if (properties.getUsername() != null && properties.getPassword() != null) {
            this.credential = MongoCredential.createCredential(
                    properties.getUsername(),
                    properties.getAuthDB(),
                    properties.getPassword().toCharArray()
            );
        } else {
            this.credential = null;
        }
        initDbConnection(properties.getDatabase());
    }

    protected void initDbConnection(String dbName) {
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry
        );

        MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(codecRegistry);

        if (credential != null) {
            settingsBuilder.credential(credential);
        }

        MongoClientSettings settings = settingsBuilder.build();
        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(dbName);

        if (!database.listCollectionNames().into(new ArrayList<>()).contains("clients")) {
            createClientCollection();
        }
        if (!database.listCollectionNames().into(new ArrayList<>()).contains("items")) {
            createItemCollection();
        }
        if (!database.listCollectionNames().into(new ArrayList<>()).contains("rents")) {
            createRentsCollection();
        }
    }

    private void createClientCollection() {
        ValidationOptions validationOptions = new ValidationOptions().validator(
                Document.parse("""
                    {
                    "$jsonSchema": {
                        "bsonType": "object",
                        "required": ["firstName", "lastName", "maxArticles", "discount"],
                        "properties": {
                            "firstName": {
                                "bsonType": "string",
                                "description": "must be a string and is required"
                            },
                            "lastName": {
                                "bsonType": "string",
                                "description": "must be a string and is required"
                            },
                            "maxArticles": {
                                "bsonType": "int",
                                "description": "must be an int and is required"
                            },
                            "discount": {
                                "bsonType": "int",
                                "description": "must be an int and is required"
                            }
                        }
                    }
                    }
                    """)
        ).validationAction(ValidationAction.ERROR);

        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                .validationOptions(validationOptions);
        database.createCollection("clients", createCollectionOptions);
    }

    private void createItemCollection() {
        ValidationOptions validationOptions = new ValidationOptions().validator(
                Document.parse("""
            {
                "$jsonSchema": {
                    "bsonType": "object",
                    "required": ["basePrice", "itemName", "itemType", "available"],
                    "properties": {
                        "basePrice": {
                            "bsonType": "int",
                            "description": "must be an int and is required"
                        },
                        "itemName": {
                            "bsonType": "string",
                            "description": "must be a string and is required"
                        },
                        "itemType": {
                            "bsonType": "string",
                            "description": "must be a string and is required"
                        },
                        "available": {
                            "bsonType": "bool",
                            "description": "must be a bool and is required"
                        },
                        "minutes": {
                            "bsonType": "int",
                            "description": "must be an int"
                        },
                        "casette": {
                            "bsonType": "bool",
                            "description": "must be a bool"
                        },
                        "genre": {
                            "bsonType": "string",
                            "description": "must be a string"
                        },
                        "vinyl": {
                            "bsonType": "bool",
                            "description": "must be a bool"
                        },
                        "pageNumber": {
                            "bsonType": "int",
                            "description": "must be an int"
                        }
                    }
                }
            }
            """)
        ).validationAction(ValidationAction.ERROR);

        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                .validationOptions(validationOptions);

        database.createCollection("items", createCollectionOptions);
    }


    private void createRentsCollection() {
        ValidationOptions validationOptions = new ValidationOptions().validator(
                Document.parse("""
                        {
                        $jsonSchema: {
                            bsonType: "object",
                            required: ["client", "item", "beginTime", "rentCost"],
                            properties: {
                                "client": {
                                    bsonType: "object",
                                    description: "must be a objectId and is required"
                                },
                                "item": {
                                    bsonType: "object",
                                    description: "must be a objectId and is required"
                                },
                                "beginTime": {
                                    bsonType: "date",
                                    description: "must be a date and is required"
                                },
                                "endTime": {
                                    bsonType: "date",
                                    description: "must be a date"
                                },
                                "rentCost": {
                                    bsonType: "int",
                                    description: "must be a int and is required"
                                },
                                "archive": {
                                    bsonType: "bool",
                                    description: "must be a boolean"
                                }
                            }
                        }
                        }
                        """)
        ).validationAction(ValidationAction.ERROR);
        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                .validationOptions(validationOptions);
        database.createCollection("rents", createCollectionOptions);
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
