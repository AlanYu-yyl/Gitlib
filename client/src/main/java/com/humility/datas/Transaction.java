package com.humility.datas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {
    private Integer transaction_id;
    private Integer buyer_id;
    private Integer seller_id;
    private LocalDateTime time;
}
