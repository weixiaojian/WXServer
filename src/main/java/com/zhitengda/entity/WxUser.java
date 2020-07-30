package com.zhitengda.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 微信表实体
 * @author langao_q
 * @since 2020-07-20 10:54
 */
@Data
@TableName("TAB_WX_USER")
public class WxUser {

    @TableId
    private String userId;
    private String name;
    private String phone;
    private Date createDate;
    private Date updateDate;
    private String siteName;
    private String openId;

}
