package com.market.biz;

import com.market.entity.Stock;
import com.market.entity.StockPrice;
import com.market.handle.HandleConstants;
import com.market.handle.JQHandle;
import com.market.handle.StockHandle;
import com.market.mongo.MongoDao;
import com.market.request.StockPriceRequest;
import com.market.utils.DateUtil;
import com.market.utils.MathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
//                String token = jqHandle.auth();
//                logger.info("token:{}", token);
        String token = "5b6a9ba1b0f77fba22667f2f07cf0bba3cebfe4e";


//        if (shouldInit()) {
//            initIndexStock(token, "000906.XSHG");
//        }
//        initStockInfo(token);
//        initIndustry(token);
//        initIndustryForEmpty(token);
//        initStockPrice(token);

//        initMA(2);
//        initPeatPrice();

    }

    private void initPeatPrice() {
        List<Stock> stockList = (List<Stock>) mongoDao.findAll(Stock.class);

        for (Stock stock : stockList) {
            Query query = new Query(Criteria.where("code").is(stock.getCode()))
                    .with(Sort.by(Sort.Order.desc("close"))).limit(1);
            List<StockPrice> priceList_high = (List<StockPrice>) mongoDao.findMany(query, StockPrice.class);

            query = new Query(Criteria.where("code").is(stock.getCode()))
                    .with(Sort.by(Sort.Order.asc("close"))).limit(1);
            List<StockPrice> priceList_low = (List<StockPrice>) mongoDao.findMany(query, StockPrice.class);

            query = new Query(Criteria.where("code").is(stock.getCode()));
            Update update = new Update();
            update.set("highest", priceList_high.get(0).getClose());
            update.set("lowest", priceList_low.get(0).getClose());
            mongoDao.update(query, update, Stock.class);
            return;
        }
    }

    private void initMA(int number) {
        List<StockPrice> stockList = (List<StockPrice>) mongoDao.findAll(StockPrice.class);

        for (int i = 0; i < number; i++) {
            Update update = new Update();
            StockPrice price = stockList.get(i);

            List<StockPrice> maList_20 = (List<StockPrice>) subList(stockList, i, 20 + i);
            update.set("ma_20", MathUtil.getMA(maList_20));
            update.set("ema_20", MathUtil.getEMA(maList_20));

            List<StockPrice> maList_60 = (List<StockPrice>) subList(stockList, i, 60 + i);
            update.set("ma_60", MathUtil.getMA(maList_60));
            update.set("ema_60", MathUtil.getEMA(maList_60));

            List<StockPrice> maList_120 = (List<StockPrice>) subList(stockList, i, 120 + i);
            update.set("ma_120", MathUtil.getMA(maList_120));
            update.set("ema_120", MathUtil.getEMA(maList_120));

            Query query_1 = new Query(Criteria.where("code").is(price.getCode()).and("date").is(price.getDate()));
            mongoDao.update(query_1, update, StockPrice.class);
        }

    }

    private void initStockPrice(String token) {
        List<Stock> stockList = (List<Stock>) mongoDao.findAll(Stock.class);

        StockPriceRequest request = StockPriceRequest.builder().method(HandleConstants.GET_PRICE_PERIOD).token(token)
                .unit(HandleConstants.INIT_PRICE_UNIT)
                .date(HandleConstants.INIT_PRICE_START_TIME)
                .end_date(HandleConstants.INIT_PRICE_END_TIME)
                .fq_ref_date(HandleConstants.INIT_PRICE_START_TIME)
                .build();

        for (Stock stock : stockList) {
            String code = stock.getCode();
            request.setCode(code);
            String priceStr = stockHandle.getStockPrice(request);

            List<StockPrice> priceList = new ArrayList<>();

            String[] priceArray = priceStr.split("\n");
            for (int i = 1; i < priceArray.length; i++) {
                String[] priceInfoArray = priceArray[i].split(",");
                StockPrice price = StockPrice.builder()
                        .code(code)
                        .date(priceInfoArray[0])
                        .timestamp(DateUtil.strToStamp(priceInfoArray[0]))
                        .open(Double.parseDouble(priceInfoArray[1]))
                        .close(Double.parseDouble(priceInfoArray[2]))
                        .high(Double.parseDouble(priceInfoArray[3]))
                        .low(Double.parseDouble(priceInfoArray[4]))
                        .volume(Double.parseDouble(priceInfoArray[5]))
                        .money(Double.parseDouble(priceInfoArray[6]))
                        .build();
                logger.info("price:{}", price);
                priceList.add(price);
            }

            mongoDao.insertAll(priceList);
            return;
        }
    }


    private void initStockInfo(String token) {
        List<Stock> stockList = (List<Stock>) mongoDao.findAll(Stock.class);

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
     * 中证 1000 000852.XSHG
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

    private List<?> subList(List<?> list, int start, int end) {
        if (list.size() <= end - start) {
            return list;
        }
        return list.subList(start, end);
    }


}
