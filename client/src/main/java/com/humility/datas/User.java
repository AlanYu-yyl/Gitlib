package com.humility.datas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private Integer user_id;
    private String  username;
    private Integer password;
    private String  realname;
    private String  phoneNumber;
    private String  emailAddress;
    private String  qqAccount;
}
