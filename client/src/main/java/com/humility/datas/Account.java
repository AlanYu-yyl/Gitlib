package com.humility.datas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Humility <Yiling Yu>
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {
  private String username;
  private Integer password;
}
