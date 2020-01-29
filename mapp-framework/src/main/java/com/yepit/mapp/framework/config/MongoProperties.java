package com.yepit.mapp.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * mongodb配置
 * @author qianlong
 */
@Component
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoProperties {

    private String host;

    private String port;

    private String password;

    private String database;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
