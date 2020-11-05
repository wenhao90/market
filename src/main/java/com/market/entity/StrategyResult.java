package com.market.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("strategy_result")
public class StrategyResult {

    @Indexed
    private String code;

    private String shortName;

    private String jqIndustry;

    private String jqIndustryCode;

    private double highest;

    private double lowest;

    private double price;

    private String strategy;

    private double range;

    private String date;

    private long timestamp;
}
