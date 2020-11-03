package com.market.biz;

import com.market.entity.*;
import com.market.handle.HandleConstants;
import com.market.handle.JQHandle;
import com.market.handle.StockHandle;
import com.market.mongo.MongoDao;
import com.market.request.*;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class StockInitBiz {

    public static final String REGEX_LINE_BREAK = "\n";
    public static final String REGEX_COMMA = ",";

    private static final Logger logger = LoggerFactory.getLogger(StockInitBiz.class);

    @Autowired
    private StockHandle stockHandle;

    @Autowired
    private JQHandle jqHandle;

    @Autowired
    private MongoDao mongoDao;

    public void init() {
        String token = jqHandle.auth();

//        if (!shouldInit()) {
//            logger.info("StockInitBiz: there is not init data.");
//            return;
//        }

//        initStockRelevant(token);

//        initPriceRelevant(token);

//        initMtss(token);

        initWidth();


        String count = stockHandle.getCount(new CountRequest(HandleConstants.GET_QUERY_COUNT, token));
        logger.info("剩余查询条数：{}", count);
    }


    private void initPriceRelevant(String token) {
        initStockPrice(token);
        initMA();
        initEndsPrice();

        initIndustryPrice(token);
    }

    private void initStockRelevant(String token) {
        initIndexStock(token, HandleConstants.INDEX_800);
        initStockInfo(token);
        initIndustry(token);
        initIndustryForEmpty(token);
        initMargincash(token);
        initMarginsec(token);
    }

    private void initWidth() {
        Query dateQuery = new Query(Criteria.where("code").is("000001.XSHE"))
                .with(Sort.by(Sort.Order.desc("timestamp"))).limit(100);
        List<StockPrice> dataPrices = (List<StockPrice>) mongoDao.findMany(dateQuery, StockPrice.class);

        for (StockPrice dataPrice : dataPrices) {
            handleDailyWidth(dataPrice.getDate());
        }

    }

    public void handleDailyWidth(String date) {
        List<Industry> industries = (List<Industry>) mongoDao.findAll(Industry.class);

        MarketWidth marketWidth = new MarketWidth();
        marketWidth.setDate(date);

        LocalDateTime startDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT)).atStartOfDay();
        marketWidth.setTimestamp(startDate.toEpochSecond(ZoneOffset.of("+8")));

        for (Industry industry : industries) {
            String industryCode = industry.getCode();

            Query stockQuery = new Query(Criteria.where("jqIndustryCode").is(industryCode));
            List<Stock> stocks = (List<Stock>) mongoDao.findMany(stockQuery, Stock.class);
            List<String> stockCodes = stocks.stream().map(stock -> stock.getCode()).collect(Collectors.toList());

            Query priceQuery = new Query(Criteria.where("code").in(stockCodes).and("date").is(date));
            List<StockPrice> prices = (List<StockPrice>) mongoDao.findMany(priceQuery, StockPrice.class);
            if (prices.size() <= 0) {
                return;
            }

            long count = prices.stream().filter(price -> price.getClose() > price.getMa_20()).count();

            MarketWidth.Width width = new MarketWidth().new Width();
            width.setCode(industry.getCode());
            width.setName(industry.getName());
            double range = new BigDecimal(count).divide(new BigDecimal(prices.size()), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
            width.setRange((int) (range * 100));

            marketWidth.addWidth(width);
        }
        mongoDao.insert(marketWidth);
    }

    private void initIndustryPrice(String token) {
        initIndustryPrice(token, HandleConstants.INIT_PRICE_START_TIME, HandleConstants.INIT_PRICE_END_TIME);
    }

    public void initIndustryPrice(String token, String date, String endDate) {
        StockPriceRequest request = StockPriceRequest.builder().method(HandleConstants.GET_PRICE_PERIOD).token(token)
                .code(HandleConstants.INDEX_800)
                .unit(HandleConstants.INIT_PRICE_UNIT)
                .date(date)
                .end_date(endDate)
                .fq_ref_date(date)
                .build();

        String priceStr = stockHandle.getStockPrice(request);

        List<IndustryPrice> priceList = new ArrayList<>();

        String[] priceArray = priceStr.split(REGEX_LINE_BREAK);
        for (int i = 1; i < priceArray.length; i++) {
            String[] priceInfoArray = priceArray[i].split(REGEX_COMMA);
            IndustryPrice price = IndustryPrice.builder()
                    .code(HandleConstants.INDEX_800)
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
    }

    /**
     * TODO 暂不考虑
     *
     * @param token
     */
    private void initLockedShares(String token) {
        StockLockedRequest request = new StockLockedRequest(HandleConstants.GET_LOCKED_SHARES, token, "000001.XSHE", "2020-10-28", "2020-12-31");
        String loceedStr = stockHandle.getLockedShares(request);
        logger.info("asdfasdf:{}", loceedStr);

    }

    private void initMtss(String token) {
        List<Stock> stockList = (List<Stock>) mongoDao.findAll(Stock.class);

        StockMessRequest request = new StockMessRequest(HandleConstants.GET_MTSS, token, "", HandleConstants.INIT_PRICE_START_TIME, HandleConstants.INIT_PRICE_END_TIME);
        for (Stock stock : stockList) {
            request.setCode(stock.getCode());
            logger.info("handle {} data", stock.getCode());
            String mtssStr = stockHandle.getStockMtss(request);

            if (Objects.isNull(mtssStr)) {
                continue;
            }

            List<StockMess> messList = new ArrayList<>();

            String[] mtssArray = mtssStr.split(REGEX_LINE_BREAK);
            for (int i = 1; i < mtssArray.length; i++) {
                String[] dataArray = mtssArray[i].split(REGEX_COMMA);

                StockMess mess = StockMess.builder().code(dataArray[1])
                        .date(dataArray[0])
                        .timestamp(DateUtil.strToStamp(dataArray[0]))
                        .fin_value(Long.parseLong(dataArray[2]))
                        .fin_buy_value(Long.parseLong(dataArray[3]))
                        .fin_refund_value(Long.parseLong(dataArray[4]))
                        .sec_value(Long.parseLong(dataArray[5]))
                        .sec_sell_value(Long.parseLong(dataArray[6]))
                        .sec_refund_value(Long.parseLong(dataArray[7]))
                        .fin_sec_value(Long.parseLong(dataArray[8]))
                        .build();
                messList.add(mess);
            }
            mongoDao.insertAll(messList);
        }
    }

    private void initMargincash(String token) {
        StockMarginsecRequest request = new StockMarginsecRequest(HandleConstants.GET_MARGINCASH_STOCKS, token);
        String margincashString = stockHandle.getStockMarginsec(request);

        String[] margincashArray = margincashString.split(REGEX_LINE_BREAK);

        Update update = new Update();
        update.set("margincash", 1);

        for (int i = 0; i < margincashArray.length; i++) {
            Query query = new Query(Criteria.where("code").is(margincashArray[i]));
            mongoDao.update(query, update, Stock.class);
        }

    }

    private void initMarginsec(String token) {
        StockMarginsecRequest request = new StockMarginsecRequest(HandleConstants.GET_MARGINSEC_STOCKS, token);
        String margisecString = stockHandle.getStockMarginsec(request);

        String[] margisecArray = margisecString.split(REGEX_LINE_BREAK);

        Update update = new Update();
        update.set("marginsec", 1);

        for (int i = 0; i < margisecArray.length; i++) {
            Query query = new Query(Criteria.where("code").is(margisecArray[i]));
            mongoDao.update(query, update, Stock.class);
        }

    }

    public void initEndsPrice() {
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
        }
    }

    private void initMA() {
        List<Stock> stockList = (List<Stock>) mongoDao.findAll(Stock.class);
        for (Stock stock : stockList) {
            Query query = new Query(Criteria.where("code").is(stock.getCode()))
                    .with(Sort.by(Sort.Order.desc("timestamp")));
            List<StockPrice> priceList = (List<StockPrice>) mongoDao.findMany(query, StockPrice.class);

            handlePriceMA(priceList, priceList.size() - 120);
        }
    }

    public void handlePriceMA(List<StockPrice> priceList, int handleNum) {
        for (int i = 0; i < handleNum; i++) {
            StockPrice price = priceList.get(i);
            if (price.getEma_20() > 0.0) {
                continue;
            }

            Update update = new Update();

            List<StockPrice> maList_20 = (List<StockPrice>) subList(priceList, i, 20 + i);
            update.set("ma_20", MathUtil.getMA(maList_20));
            update.set("ema_20", MathUtil.getEMA(maList_20));

            List<StockPrice> maList_60 = (List<StockPrice>) subList(priceList, i, 60 + i);
            update.set("ma_60", MathUtil.getMA(maList_60));
            update.set("ema_60", MathUtil.getEMA(maList_60));

            List<StockPrice> maList_120 = (List<StockPrice>) subList(priceList, i, 120 + i);
            update.set("ma_120", MathUtil.getMA(maList_120));
            update.set("ema_120", MathUtil.getEMA(maList_120));

            Query query_1 = new Query(Criteria.where("code").is(price.getCode()).and("date").is(price.getDate()));
            mongoDao.update(query_1, update, StockPrice.class);

            logger.info("handle {} {} data", price.getCode(), price.getDate());
        }
    }

    public Map<String, Integer> initStockPrice(String token) {
        return initStockPrice(token, HandleConstants.INIT_PRICE_START_TIME, HandleConstants.INIT_PRICE_END_TIME);
    }

    public Map<String, Integer> initStockPrice(String token, String date, String endDate) {
        List<Stock> stockList = (List<Stock>) mongoDao.findAll(Stock.class);

        StockPriceRequest request = StockPriceRequest.builder().method(HandleConstants.GET_PRICE_PERIOD).token(token)
                .unit(HandleConstants.INIT_PRICE_UNIT)
                .date(date)
                .end_date(endDate)
                .fq_ref_date(date)
                .build();

        Map<String, Integer> result = new HashMap<>();
        for (Stock stock : stockList) {
            String code = stock.getCode();

            Query query = new Query(Criteria.where("code").is(code));
            long count = mongoDao.count(query, StockPrice.class);
//            if (count > 0) {
//                continue;
//            }

            request.setCode(code);
            String priceStr = stockHandle.getStockPrice(request);

            List<StockPrice> priceList = new ArrayList<>();

            String[] priceArray = priceStr.split(REGEX_LINE_BREAK);
            for (int i = 1; i < priceArray.length; i++) {
                String[] priceInfoArray = priceArray[i].split(REGEX_COMMA);
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

            result.put(code, priceList.size());
        }
        return result;
    }


    private void initStockInfo(String token) {
        List<Stock> stockList = (List<Stock>) mongoDao.findAll(Stock.class);

        StockInfoRequest request = new StockInfoRequest(HandleConstants.GET_SECURITY_INFO, token, "");
        for (Stock stock : stockList) {
            request.setCode(stock.getCode());
            String stockInfoStr = stockHandle.getStockInfo(request);

            Query query = new Query(Criteria.where("code").is(stock.getCode()));
            Update update = new Update();
            update.set("shortName", stockInfoStr.split(REGEX_COMMA)[1]);
            mongoDao.update(query, update, Stock.class);
        }
    }

    private void initIndustry(String token) {
        StockInfoRequest request = new StockInfoRequest(HandleConstants.GET_INDUSTRIES, token, HandleConstants.JQ_L1);
        String industriesStr = stockHandle.getIndustries(request);

        String[] industriesArray = industriesStr.split(REGEX_LINE_BREAK);
        List<Industry> industryList = new ArrayList<>();
        for (int i = 1; i < industriesArray.length; i++) {
            String[] infoArray = industriesArray[i].split(REGEX_COMMA);

            String code = infoArray[0];
            String name = infoArray[1];

            Industry industry = Industry.builder().code(code).name(name).build();
            industryList.add(industry);
        }

        mongoDao.insertAll(industryList);


        StockInfoRequest industryStocksRequest = new StockInfoRequest(HandleConstants.GET_INDUSTRY_STOCKS, token, "");
        for (Industry industry : industryList) {
            industryStocksRequest.setCode(industry.getCode());
            String stocksStr = stockHandle.getIndustryStocks(industryStocksRequest);
            logger.info("stocksStr:{}", stocksStr);
            String[] stocks = stocksStr.split(REGEX_LINE_BREAK);

            Update update = new Update();
            update.set("jqIndustryCode", industry.getCode());
            update.set("jqIndustry", industry.getName());
            for (int j = 0; j < stocks.length; j++) {
                Query query = new Query(Criteria.where("code").is(stocks[j]));
                mongoDao.update(query, update, Stock.class);
            }
        }
    }

    private void initIndustryForEmpty(String token) {
        Query query = new Query(Criteria.where("jqIndustryCode").is(null));
        List<Stock> stockList = (List<Stock>) mongoDao.findMany(query, Stock.class);

        StockInfoRequest request = new StockInfoRequest(HandleConstants.GET_INDUSTRY, token, "");
        for (Stock stock : stockList) {
            request.setCode(stock.getCode());
            String industriesStr = stockHandle.getIndustry(request);

            String[] infoArray = industriesStr.split(REGEX_LINE_BREAK)[1].split(REGEX_COMMA);
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
     * 中证 800 000906.XSHG
     *
     * @param token
     */
    private void initIndexStock(String token, String... indexs) {
        for (String index : indexs) {
            IndexStocksRequest request = new IndexStocksRequest(HandleConstants.GET_INDEX_STOCKS, token, index);
            String[] indexStocks = stockHandle.getIndexStocks(request);

            List<Stock> stockList = new ArrayList<>();
            for (int i = 0; i < indexStocks.length; i++) {
                Stock stock = Stock.builder().code(indexStocks[i]).build();
                stockList.add(stock);
            }
            mongoDao.insertAll(stockList);
        }
    }

    private boolean shouldInit() {
        Query query = new Query();
        long count = mongoDao.count(query, Stock.class);
        return count <= 0;
    }

    private List<?> subList(List<?> list, int start, int end) {
        if (list.size() <= end - start) {
            return list;
        }
        return list.subList(start, end);
    }

}
