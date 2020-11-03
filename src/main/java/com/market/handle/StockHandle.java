package com.market.handle;

import com.market.request.*;
import com.market.utils.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockHandle {

    @Autowired
    private HttpClientUtil clientUtil;

    public String getCount (CountRequest request) {
        String count = (String) clientUtil.post(HandleConstants.JQ_URL, request, String.class);
        return count;
    }

    public String[] getIndexStocks(IndexStocksRequest request) {
        String indexStockStr = (String) clientUtil.post(HandleConstants.JQ_URL, request, String.class);
        return indexStockStr.split("\n");
    }

    public String getStockInfo(StockInfoRequest request) {
        String stockInfoStr = (String) clientUtil.post(HandleConstants.JQ_URL, request, String.class);
        return stockInfoStr.split("\n")[1];
    }

    public String getIndustries(StockInfoRequest request) {
        String industriesStr = (String) clientUtil.post(HandleConstants.JQ_URL, request, String.class);
        return industriesStr;
    }

    public String getIndustry(StockInfoRequest request) {
        String industriesStr = (String) clientUtil.post(HandleConstants.JQ_URL, request, String.class);
        return industriesStr;
    }

    public String getIndustryStocks(StockInfoRequest request) {
        String industriesStr = (String) clientUtil.post(HandleConstants.JQ_URL, request, String.class);
        return industriesStr;
    }

    public String getStockPrice(StockPriceRequest request) {
        String priceStr = (String) clientUtil.post(HandleConstants.JQ_URL, request, String.class);
        return priceStr;
    }

    public String getStockMarginsec(StockMarginsecRequest request) {
        String priceStr = (String) clientUtil.post(HandleConstants.JQ_URL, request, String.class);
        return priceStr;
    }

    public String getStockMtss(StockMessRequest request) {
        String priceStr = (String) clientUtil.post(HandleConstants.JQ_URL, request, String.class);
        return priceStr;
    }

    public String getLockedShares(StockLockedRequest request) {
        String lockedStr = (String) clientUtil.post(HandleConstants.JQ_URL, request, String.class);
        return lockedStr;
    }

}
