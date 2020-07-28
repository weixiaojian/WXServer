package com.zhitengda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhitengda.entity.WxUser;
import com.zhitengda.mapper.UserMapper;
import com.zhitengda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author langao_q
 * @create 2020-07-20 10:19
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public WxUser queryByOpneId(String openid) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("OPEN_ID", openid);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public WxUser saveUser(WxUser user) {
        Integer total = userMapper.insert(user);
        return total>0?user:null;
    }


}
