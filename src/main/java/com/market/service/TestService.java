package com.market.service;

import com.market.entity.StockPrice;
import com.market.mongo.MongoDao;
import com.market.response.TestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private MongoDao mongoDao;

    public List<TestResponse> listStockPrice(String code) {
        List<StockPrice> stockList = (List<StockPrice>) mongoDao.findAll(StockPrice.class);

        List<TestResponse> responses = new ArrayList<>();
        for (StockPrice price : stockList) {
            TestResponse response = new TestResponse();
            response.setDate(price.getDate());
            response.setMarket1(price.getClose());
            response.setSales2(price.getHigh());
            responses.add(response);
        }
        return responses;
    }
}
