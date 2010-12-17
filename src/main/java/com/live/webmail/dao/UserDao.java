package com.live.webmail.dao;

import com.live.webmail.model.User;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 10-11-29
 * Time: 下午1:31
 * Email:liuyuhui007@gmail.com
 */
public interface UserDao {
    /**
     * 查询所有的用户
     * @return
     */
    public List<User> queryAll();
}
