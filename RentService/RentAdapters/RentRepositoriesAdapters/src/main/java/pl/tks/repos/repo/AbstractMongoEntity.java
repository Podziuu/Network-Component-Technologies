package pl.tks.repos.repo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationOptions;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import pl.tks.repos.config.MongoProperties;
import pl.tks.repos.entities.user.AdminEnt;
import pl.tks.repos.entities.user.ClientEnt;
import pl.tks.repos.entities.user.ManagerEnt;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMongoEntity implements AutoCloseable {
    private ConnectionString connectionString;
    private MongoCredential credential;
    protected MongoClient mongoClient;
    protected MongoDatabase database;

    private CodecRegistry codecRegistry = CodecRegistries.fromProviders(
            PojoCodecProvider.builder()
                    .automatic(true)
                    .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                    .register(ClientEnt.class, AdminEnt.class, ManagerEnt.class)
                    .build()
    );

    public AbstractMongoEntity(MongoProperties properties) {
        this.connectionString = new ConnectionString(properties.getUri());
        if (properties.getUsername() != null && properties.getPassword() != null && properties.getDatabase() != null) {
            this.credential = MongoCredential.createCredential(properties.getUsername(), properties.getAuthDB(), properties.getPassword().toCharArray());
        }
        initDbConnection(properties.getDatabase());
    }

    protected void initDbConnection(String dbName) {
        MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        codecRegistry
                ));

        if (credential != null) {
            settingsBuilder.credential(credential);
        }

        MongoClientSettings settings = settingsBuilder.build();
        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(dbName);
        if (!database.listCollectionNames().into(new ArrayList<>()).contains("users")) {
            createUserCollection();
        }
        if (!database.listCollectionNames().into(new ArrayList<>()).contains("items")) {
            createItemCollection();
        }
        if (!database.listCollectionNames().into(new ArrayList<>()).contains("rents")) {
            createRentsCollection();
        }
    }

    private void createUserCollection() {
        ValidationOptions validationOptions = new ValidationOptions()
                .validator(
                        Document.parse("""
                                {
                                $jsonSchema: {
                                    bsonType: "object",
                                    required: ["login", "password", "firstName", "lastName", "active", "role"],
                                    properties: {
                                        login: {
                                            bsonType: "string",
                                            description: "must be a string and is required"
                                        },
                                        password: {
                                            bsonType: "string",
                                            description: "must be a string and is required"
                                        },
                                        firstName: {
                                            bsonType: "string",
                                            description: "must be a string and is required"
                                        },
                                        lastName: {
                                            bsonType: "string",
                                            description: "must be a string and is required"
                                        },
                                        active: {
                                            bsonType: "bool",
                                            description: "must be a boolean and is required"
                                        },
                                        role: {
                                            bsonType: "string",
                                            description: "must be a string and is required"
                                        }
                                    }
                                }
                                }
                                """)
                ).validationAction(ValidationAction.ERROR);
        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                .validationOptions(validationOptions);
        database.createCollection("users", createCollectionOptions);

        database.getCollection("users").createIndex(
                new Document("login", 1),
                new IndexOptions().unique(true)
        );
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
}
