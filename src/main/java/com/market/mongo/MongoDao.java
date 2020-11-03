package com.market.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
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

    public long count(Query query, Class<?> object) {
        long count = mongoTemplate.count(query, object);
        return count;
    }

    public <T> T insert(T object) {
        return mongoTemplate.insert(object);
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

    public AggregationResults<?> aggregate(Aggregation aggregation, Class<?> input, Class<?> output) {
        return mongoTemplate.aggregate(aggregation, input, output);
    }
}
