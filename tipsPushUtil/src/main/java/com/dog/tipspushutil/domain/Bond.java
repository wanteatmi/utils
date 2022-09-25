package com.dog.tipspushutil.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Bond {

    //@JsonProperty("SECUCODE")
    @JsonAlias("SECUCODE")
    private String bondCode;

    @JsonAlias("TRADE_MARKET")
    private String tradeMarket;

    @JsonAlias("SECURITY_NAME_ABBR")
    private String bondName;

    @JsonAlias("CONVERT_STOCK_CODE")
    private String stockCode;

    //评级 AA-
    @JsonAlias("RATING")
    private String rating;

    @JsonAlias("VALUE_DATE")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date issueDate;

    @JsonAlias("REMARK")
    private String remark;

    @JsonAlias("ISSUE_OBJECT")
    private String issueInfo;

}
