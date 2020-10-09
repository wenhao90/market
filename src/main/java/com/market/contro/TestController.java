package com.market.contro;

import com.market.biz.StockBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController  {

    @Autowired
    private StockBiz handle;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test(){
        handle.init();
    }
}
