package com.market.controller;

import com.market.biz.PriceSyncBiz;
import com.market.biz.StockInitBiz;
import com.market.biz.StrategyBiz;
import com.market.request.TestRequest;
import com.market.response.TestResponse;
import com.market.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private StockInitBiz initBiz;

    @Autowired
    private PriceSyncBiz syncBiz;

    @Autowired
    private StrategyBiz strategyBiz;

    //    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public void init() {
        initBiz.init();
    }


    //    @RequestMapping(value = "/sync", method = RequestMethod.GET)
    public void sync() {
        syncBiz.sync();
    }

    @RequestMapping(value = "/strategy", method = RequestMethod.GET)
    public void strategy() {
        strategyBiz.run();
    }

}
