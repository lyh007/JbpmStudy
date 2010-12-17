package com.live.webmail.dao.impl;

import com.live.webmail.base.BaseTestCase;
import com.live.webmail.dao.UserDao;
import com.live.webmail.model.User;
import com.live.webmail.service.UserService;
import com.opensymphony.xwork2.interceptor.annotations.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * @author kevin
 * @version Revision: 1.00 Date: 10-12-2下午3:01
 * @Email liuyuhui007@gmail.com
 */
public class UserDaoTest extends BaseTestCase {
    private UserService userService = null;

    public UserDaoTest() {
        userService = (UserService) getApplicationContext().getBean("userService");
    }

    @Test
    public void testAddUser() {
        System.out.println("hello world!");
    }

    @Test
    public void testQueryAll() {
        List<User> users = userService.queryAll();
        System.out.println("users count:" + users.size());
    }
}
