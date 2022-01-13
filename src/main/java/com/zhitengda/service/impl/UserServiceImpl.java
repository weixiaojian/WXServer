package com.zhitengda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhitengda.entity.WxUser;
import com.zhitengda.mapper.UserMapper;
import com.zhitengda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * UserService实现类
 *
 * @author langao_q
 * @since 2020-07-20 10:19
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public WxUser saveOrUpdate(WxUser wxUser) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("OPEN_ID", wxUser.getOpenId());
        WxUser user = userMapper.selectOne(wrapper);
        if (user == null) {
            wxUser.setUserId(wxUser.getOpenId());
            userMapper.insert(wxUser);
            return wxUser;
        } else {
            user.setUpdateDate(new Date());
            userMapper.updateById(user);
            return user;
        }
    }
}
