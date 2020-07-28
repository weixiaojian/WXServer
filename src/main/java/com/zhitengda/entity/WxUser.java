package com.zhitengda.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author langao_q
 * @create 2020-07-20 10:54
 */
@Data
@TableName("TAB_WX_USER")
public class WxUser {

    private String guid;
    private String account;
    private String password;
    private String zhName;
    private String phone;
    private Long blStatus;
    private Date createDate;
    private String type;
    private String siteName;
    private String openId;

}
