package pl.tks.repos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@ConfigurationProperties(prefix = "mongo")
public class MongoProperties {
    private String uri;
    private String database;
    private String username;
    private String password;
    private String authDB;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getAuthDB() {
        return authDB;
    }

    public void setAuthDB(String authDB) {
        this.authDB = authDB;
    }
}
