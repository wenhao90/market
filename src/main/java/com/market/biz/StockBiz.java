package com.market.biz;

import com.market.entity.Stock;
import com.market.handle.HandleConstants;
import com.market.handle.JQHandle;
import com.market.handle.StockHandle;
import com.market.mongo.MongoDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StockBiz {

    private static final Logger logger = LoggerFactory.getLogger(StockBiz.class);

    @Autowired
    private MongoDao mongoDao;

    @Autowired
    private StockHandle stockHandle;

    @Autowired
    private JQHandle jqHandle;

    public void init() {
        //        String token = jqHandle.auth();
        //        logger.info("token:{}", token);
        String token = "5b6a9ba1b0f77fba22667f2f07cf09bea22d7db9";


//        if (shouldInit()) {
//            initIndexStock(token, "000906.XSHG");
//        }
//        initStockInfo(token);
//        initIndustry(token);
        initIndustryForEmpty(token);
    }

    private void initStockInfo(String token) {
        List<Stock> stockList = (List<Stock>) mongoDao.findMany(new Query(), Stock.class);

        for (Stock stock : stockList) {
            String stockInfoStr = stockHandle.getStockInfo(stock.getCode(), token);

            Query query = new Query(Criteria.where("code").is(stock.getCode()));
            Update update = new Update();
            update.set("shortName", stockInfoStr.split(",")[1]);
            mongoDao.update(query, update, Stock.class);
        }
    }

    private void initIndustry(String token) {
        String industriesStr = stockHandle.getIndustries(HandleConstants.JQ_L1, token);

        String[] industriesArray = industriesStr.split("\n");
        for (int i = 1; i < industriesArray.length; i++) {
            String[] infoArray = industriesArray[i].split(",");

            String code = infoArray[0];
            String name = infoArray[1];

            String stocksStr = stockHandle.getIndustryStocks(code, token);
            logger.info("stocksStr:{}", stocksStr);
            String[] stocks = stocksStr.split("\n");

            Update update = new Update();
            update.set("jqIndustryCode", code);
            update.set("jqIndustry", name);
            for (int j = 0; j < stocks.length; j++) {
                Query query = new Query(Criteria.where("code").is(stocks[j]));
//                mongoDao.update(query, update, Stock.class);
            }
        }
    }

    private void initIndustryForEmpty(String token) {
        Query query = new Query(Criteria.where("jqIndustryCode").is(null));
        List<Stock> stockList = (List<Stock>) mongoDao.findMany(query, Stock.class);

        for (Stock stock : stockList) {
            String industriesStr = stockHandle.getIndustry(stock.getCode(), token);

            String[] infoArray = industriesStr.split("\n")[1].split(",");
            String code = infoArray[1];
            String name = infoArray[2];

            Query query_1 = new Query(Criteria.where("code").is(stock.getCode()));
            Update update = new Update();
            update.set("jqIndustryCode", code);
            update.set("jqIndustry", name);
            mongoDao.update(query_1, update, Stock.class);
        }

    }


    /**
     * 中证流通 000902.XSHG
     * 中证 800 000906.XSHG
     *
     * @param token
     */
    private void initIndexStock(String token, String... indexs) {
        for (String index : indexs) {
            String[] indexStocks = stockHandle.getIndexStocks(index, token);

            List<Stock> stockList = new ArrayList<>();
            for (int i = 0; i < indexStocks.length; i++) {
                Stock stock = Stock.builder().code(indexStocks[i]).build();
                stockList.add(stock);
            }
            mongoDao.insertAll(stockList);
        }
    }

    private boolean shouldInit() {
        long count = mongoDao.count(Stock.class);
        return count <= 0;
    }


}
