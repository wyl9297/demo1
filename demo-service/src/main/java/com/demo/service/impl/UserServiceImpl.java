package com.demo.service.impl;

import com.demo.model.RegUser;
import com.demo.persistence.dao.RegUserMapper;
import com.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/4/2.
 */
@Service("UserService")
public class UserServiceImpl implements UserService {

    @Autowired
    private RegUserMapper regUserMapper;

    public RegUser checkLogin(RegUser regUser) {
        return regUserMapper.checkLogin(regUser);
    }
}
