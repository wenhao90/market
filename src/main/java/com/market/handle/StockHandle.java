package com.market.handle;

import com.market.request.IndexStocksRequest;
import com.market.request.StockInfoRequest;
import com.market.utils.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockHandle {

    private static final Logger logger = LoggerFactory.getLogger(StockHandle.class);

    @Autowired
    private JQHandle jqHandle;

    @Autowired
    private HttpClientUtil clientUtil;

    public String[] getIndexStocks(String index, String token) {
        IndexStocksRequest request = new IndexStocksRequest(HandleConstants.GET_INDEX_STOCKS, token, index);
        String indexStockStr = (String) clientUtil.post(HandleConstants.JQ_URL, request, String.class);
        return indexStockStr.split("\n");
    }

    public String getStockInfo(String stock, String token) {
        StockInfoRequest request = new StockInfoRequest(HandleConstants.GET_SECURITY_INFO, token, stock);
        String stockInfoStr = (String) clientUtil.post(HandleConstants.JQ_URL, request, String.class);
        return stockInfoStr.split("\n")[1];
    }

    public String getIndustries(String code, String token) {
        StockInfoRequest request = new StockInfoRequest(HandleConstants.GET_INDUSTRIES, token, code);
        String industriesStr = (String) clientUtil.post(HandleConstants.JQ_URL, request, String.class);
        return industriesStr;
    }

    public String getIndustry(String code, String token) {
        StockInfoRequest request = new StockInfoRequest(HandleConstants.GET_INDUSTRY, token, code);
        String industriesStr = (String) clientUtil.post(HandleConstants.JQ_URL, request, String.class);
        return industriesStr;
    }

    public String getIndustryStocks(String code, String token) {
        StockInfoRequest request = new StockInfoRequest(HandleConstants.GET_INDUSTRY_STOCKS, token, code);
        String industriesStr = (String) clientUtil.post(HandleConstants.JQ_URL, request, String.class);
        return industriesStr;
    }

}
