package com.humility.datas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Humility <Yiling Yu>
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    private Integer mid;
    private String message;
    private Integer sender_id;
    private Integer getter_id;
    private LocalDateTime mtime;
    private Boolean is_received;
}
