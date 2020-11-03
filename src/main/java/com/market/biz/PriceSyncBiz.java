package com.market.biz;

import com.market.entity.*;
import com.market.handle.JQHandle;
import com.market.mongo.MongoDao;
import com.market.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PriceSyncBiz {

    public static final String REGEX_LINE_BREAK = "\n";
    public static final String REGEX_COMMA = ",";
    public static final int ONE_DAY_MILS = 86400000;

    private static final Logger logger = LoggerFactory.getLogger(PriceSyncBiz.class);

    @Autowired
    private StockInitBiz initBiz;

    @Autowired
    private JQHandle jqHandle;

    @Autowired
    private MongoDao mongoDao;

    public void sync() {
        String token = jqHandle.auth();

//        Map<String, String> dateSection = getDateSection();
//        if (Objects.isNull(dateSection)) {
//            return;
//        }

//        Map<String, Integer> syncResult = syncPrice(token, dateSection.get("start"), dateSection.get("end"));
//
//        initBiz.initEndsPrice();
//
//        syncMA(syncResult);
//        syncIndustryPrice(token, dateSection.get("start"), dateSection.get("end"));

        syncWidth("2020-10-29", "2020-11-02");
    }

    private void syncWidth(String start, String end) {
        LocalDateTime startDate = LocalDate.parse(start, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT)).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(end, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT)).atStartOfDay();

        while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
            logger.info("startDate:{}", startDate);
            String date = startDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT));
            initBiz.handleDailyWidth(date);

            startDate = startDate.plusDays(1);
        }
    }


    private Map<String, Integer> syncPrice(String token, String start, String end) {
        return initBiz.initStockPrice(token, start, end);
    }

    private void syncMA(Map<String, Integer> results) {
        results.keySet().forEach(code -> {
            int handleNum = results.get(code);
            int limit = 120 + handleNum;

            logger.info("limit is {}", limit);
            Query query = new Query(Criteria.where("code").is(code))
                    .with(Sort.by(Sort.Order.desc("timestamp"))).limit(limit);
            List<StockPrice> priceList = (List<StockPrice>) mongoDao.findMany(query, StockPrice.class);

            initBiz.handlePriceMA(priceList, handleNum);
        });
    }

    public void syncIndustryPrice(String token, String start, String end) {
        initBiz.initIndustryPrice(token, start, end);
    }

    private Map getDateSection() {
        LocalDateTime time = LocalDateTime.now();
        if (time.getHour() < 15) {
            logger.info("sync from 15 o'clock");
            return null;
        }

        Query query = new Query(Criteria.where("code").is("000001.XSHE"))
                .with(Sort.by(Sort.Order.desc("timestamp"))).limit(1);
        List<StockPrice> priceListNew = (List<StockPrice>) mongoDao.findMany(query, StockPrice.class);

        long timestamp_new = priceListNew.get(0).getTimestamp();

        long now = System.currentTimeMillis();
        if (now - timestamp_new < ONE_DAY_MILS) {
            logger.info("do not need sync");
            return null;
        }

        LocalDateTime newDate = LocalDate.parse(priceListNew.get(0).getDate(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT)).atStartOfDay();
        newDate = newDate.plusDays(1);

        String newDateStr = newDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT));
        String nowDateStr = time.format(DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT));
        logger.info("sync date form {} to {}", newDateStr, nowDateStr);

        Map result = new HashMap();
        result.put("start", newDateStr);
        result.put("end", nowDateStr);

        return result;
    }

}

