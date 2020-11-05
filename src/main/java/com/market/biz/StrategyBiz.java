package com.market.biz;

import com.market.strategy.LowValueStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StrategyBiz {

    @Autowired
    private LowValueStrategy lowValueStrategy;

    public void run() {
        lowValueStrategy.run();
    }
}
