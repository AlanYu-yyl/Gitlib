package com.humility.datas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {
    private Integer tid;
    private Integer gid;
    private Integer buyer_id;
    private Integer seller_id;
    private String comment;
    private BigDecimal tprice;
    private Long timeMillis;
}
