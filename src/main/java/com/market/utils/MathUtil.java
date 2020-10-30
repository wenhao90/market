package com.market.utils;


import com.market.entity.StockPrice;

import java.math.BigDecimal;
import java.util.List;

public class MathUtil {

    public static double getMA(List<StockPrice> list) {
        double ma = list.stream().mapToDouble(StockPrice::getClose).average().getAsDouble();
        return new BigDecimal(ma).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static Double getEMA(List<StockPrice> list) {
        int size = list.size();

        if (size == 1) {
            return list.get(0).getClose();
        }

        Double index = 2.0 / (size + 1.0);

        Double ema = list.get(0).getClose();
        for (int i = 1; i < size; i++) {
            ema = list.get(i).getClose() * index + ema * (1 - index);
        }
        return new BigDecimal(ema).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
