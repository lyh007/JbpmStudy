package com.live.webmail.service.impl;

import com.live.webmail.dao.UserDao;
import com.live.webmail.model.User;
import com.live.webmail.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kevin
 * @version Revision: 1.00 Date: 10-12-3上午10:06
 * @Email liuyuhui007@gmail.com
 */
@Service("userService")
//@Repository("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    public UserDao userDao;

    public List<User> queryAll() {
        return userDao.queryAll();
    }
}
