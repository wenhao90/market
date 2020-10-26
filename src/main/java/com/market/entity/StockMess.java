package com.market.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("stock_mess")
public class StockMess {

    @Indexed
    private String code;

    @Indexed
    private String date;

    @Indexed
    private long timestamp;

    /**
     * 融资余额
     */
    private long fin_value;

    /**
     * 融资买入额
     */
    private long fin_buy_value;

    /**
     * 融券余量
     */
    private long sec_value;

    /**
     * 融资融券余额
     */
    private long fin_sec_value;


}
