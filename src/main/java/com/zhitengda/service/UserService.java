package com.zhitengda.service;

import com.zhitengda.entity.WxUser;

/**
 * userService接口
 * @author langao_q
 * @since 2020-07-20 10:19
 */
public interface UserService {

    /**
     * 查询指定用户信息
     * @param openid
     * @return
     */
    WxUser queryByOpneId(String openid);

    /**
     * 保存用户信息
     * @param user
     * @return
     */
    WxUser saveUser(WxUser user);
}
