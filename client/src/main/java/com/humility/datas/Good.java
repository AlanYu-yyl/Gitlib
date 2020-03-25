package com.humility.datas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Good implements Serializable {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer goodNum;
    private String description;
    private Integer ownerId;
    private Integer transaction_id;
}
