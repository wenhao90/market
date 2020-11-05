package com.market.strategy;

import com.market.entity.Stock;
import com.market.entity.StockPrice;
import com.market.entity.StrategyResult;
import com.market.mongo.MongoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class LowValueStrategy {

    public static final double STRATEGY_LOW_VALUE_RANGE = 30;
    @Autowired
    private MongoDao mongoDao;

    public void run() {
        List<Stock> stocks = (List<Stock>) mongoDao.findAll(Stock.class);
        stocks.stream().forEach(stock -> {
            Query query = new Query(Criteria.where("code").is(stock.getCode()))
                    .with(Sort.by(Sort.Order.desc("timestamp"))).limit(1);
            List<StockPrice> priceList = (List<StockPrice>) mongoDao.findMany(query, StockPrice.class);

            StockPrice price = priceList.get(0);
            double close = price.getClose();

            double highest = stock.getHighest();
            double lowest = stock.getLowest();

            // 获取当前价在整个股价区间的比值
            double range = new BigDecimal(close).divide(new BigDecimal(highest - lowest), 4, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;

            if (range <= STRATEGY_LOW_VALUE_RANGE) {
                StrategyResult result = StrategyResult.builder()
                        .code(stock.getCode())
                        .shortName(stock.getShortName())
                        .jqIndustry(stock.getJqIndustry())
                        .jqIndustryCode(stock.getJqIndustryCode())
                        .highest(stock.getHighest())
                        .lowest(stock.getLowest())
                        .price(close)
                        .strategy("低值")
                        .range(range)
                        .date(price.getDate())
                        .timestamp(price.getTimestamp())
                        .build();

                mongoDao.insert(result);
            }
        });


    }
}
