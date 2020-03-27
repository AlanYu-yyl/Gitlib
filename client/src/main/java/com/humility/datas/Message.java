package com.humility.datas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Humility <Yiling Yu>
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

    private String message;
    private Integer sender;

}

