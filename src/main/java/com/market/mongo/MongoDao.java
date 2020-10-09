package com.market.mongo;

import com.mongodb.client.result.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class MongoDao {

    private static final Logger logger = LoggerFactory.getLogger(MongoDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public long count(Class<?> object) {
        Query query = new Query();
        long count = mongoTemplate.count(query, object);
        return count;
    }

    public Collection<?> insertAll(List<?> objects) {
        return mongoTemplate.insertAll(objects);
    }

    public Object findOne(Query query, Class<?> object) {
        Object result = mongoTemplate.findOne(query, object);
        return result;
    }

    public List<?> findMany(Query query, Class<?> object) {
        List<?> resultList = mongoTemplate.find(query, object);
        return resultList;
    }

    public List<?> findAll(Class<?> object) {
        List<?> resultList = mongoTemplate.findAll(object);
        return resultList;
    }

    public UpdateResult update(Query query, Update update, Class<?> object) {
        UpdateResult resultList = mongoTemplate.updateFirst(query, update, object);
        return resultList;
    }
}
