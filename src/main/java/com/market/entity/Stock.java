package com.market.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("stock")
public class Stock {

    private String code;

    private String shortName;

    private String jqIndustry;

    private String jqIndustryCode;

    private String highest;

    private String lowest;

}
