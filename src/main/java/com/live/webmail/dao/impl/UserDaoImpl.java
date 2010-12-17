package com.live.webmail.dao.impl;

import com.live.webmail.dao.UserDao;
import com.live.webmail.model.User;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 10-11-29
 * Time: 下午1:35
 * Email:liuyuhui007@gmail.com
 */
@Repository("userDao")
public class UserDaoImpl implements UserDao {
//    @Resource(name = "sessionFactory")
//    private SqlSessionFactory sqlSessionFactory;

    public List<User> queryAll() {
//        return (List<User>) sqlSessionFactory.openSession().selectList("com.live.webmail.model.User.selectUser");
        return null;
    }
}
