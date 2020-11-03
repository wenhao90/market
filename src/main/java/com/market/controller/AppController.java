package com.market.controller;

import com.market.biz.StockInitBiz;
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
public class AppController {

    @Autowired
    private TestService service;


    @RequestMapping(value = "/mixed", method = RequestMethod.GET)
    public List<TestResponse> testMixed(TestRequest request) {
        return service.listStockPrice(request.getCode());
    }
}
