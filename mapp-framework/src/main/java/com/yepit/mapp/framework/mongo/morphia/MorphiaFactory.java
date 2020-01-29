package com.yepit.mapp.framework.mongo.morphia;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.yepit.mapp.framework.config.MongoProperties;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

@Configuration
@ConditionalOnClass(Mongo.class)
public class MorphiaFactory {

    @Inject
    private Mongo mongo;

    @Inject
    MongoProperties mongoProperties;

    @Bean
    public Datastore get() {
        Morphia morphia = new Morphia();
        return morphia.createDatastore((MongoClient) mongo,mongoProperties.getDatabase());
    }

}
