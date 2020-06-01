package com.humility.datas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Good implements Serializable {
    private Integer gid;
    private String gname;
    private Integer owner;
    private BigDecimal price;
    private ImageIcon image;
    private String description;
    private Boolean is_selled;
}
