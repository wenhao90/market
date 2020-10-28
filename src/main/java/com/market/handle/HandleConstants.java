package com.market.handle;

public interface HandleConstants {

    /**
     * 中证800指数
     */
    String INDEX_800 = "000906.XSHG";

    /**
     * 行业：聚宽1级
     */
    String JQ_L1 = "jq_l1";

    /**
     * 初始价格单位：1天
     */
    String INIT_PRICE_UNIT = "1d";

    /**
     * 初始开始时间（需修改）
     */
    String INIT_PRICE_START_TIME = "2020-09-30";

    /**
     * 初始价格结束时间（需修改）
     */
    String INIT_PRICE_END_TIME = "2020-10-09";


    String JQ_URL = "https://dataapi.joinquant.com/apis";

    String GET_TOKEN = "get_token";

    String GET_INDEX_STOCKS = "get_index_stocks";

    String GET_SECURITY_INFO = "get_security_info";

    /**
     * 获取行业列表
     */
    String GET_INDUSTRIES = "get_industries";

    /**
     * 获取股票所属行业
     */
    String GET_INDUSTRY = "get_industry";

    /**
     * 获取行业成分股
     */
    String GET_INDUSTRY_STOCKS = "get_industry_stocks";

    String GET_PRICE_PERIOD = "get_price_period";

    /**
     * 是否可以融资
     */
    String GET_MARGINCASH_STOCKS = "get_margincash_stocks";

    /**
     * 是否可以融券
     */
    String GET_MARGINSEC_STOCKS = "get_marginsec_stocks";

    /**
     * 融券融券信息
     */
    String GET_MTSS = "get_mtss";

    /**
     * 解禁
     */
    String GET_LOCKED_SHARES = "get_locked_shares";

}
