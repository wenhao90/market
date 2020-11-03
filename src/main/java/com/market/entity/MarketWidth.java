package com.market.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document("market_width")
public class MarketWidth {

    private String date;

    private long timestamp;

    private List<Width> widths = new ArrayList<>();

    public void addWidth(Width width) {
        this.widths.add(width);
    }

    @Data
    public class Width {

        private String name;

        private String code;

        private int range;
    }

}


