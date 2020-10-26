package com.market.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("stock")
public class Stock {

    @Indexed
    private String code;

    private String shortName;

    private String jqIndustry;

    private String jqIndustryCode;

    private double highest;

    private double lowest;

    /**
     * 是否可以融劵
     */
    private int marginsec;

    /**
     * 是否可以融资
     */
    private int margincash;

}
