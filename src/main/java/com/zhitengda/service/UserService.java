package com.zhitengda.service;

import com.zhitengda.entity.WxUser;

/**
 * userService接口
 *
 * @author langao_q
 * @since 2020-07-20 10:19
 */
public interface UserService {

    /**
     * 保存或更新用户信息
     *
     * @param wxUser
     * @return
     */
    WxUser saveOrUpdate(WxUser wxUser);

}
