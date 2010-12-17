package com.live.webmail.base;

import junit.framework.TestCase;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.Reader;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 10-11-29
 * Time: 下午1:18
 *
 * @author kevin
 * @version 1.0
 * @Email liuyuhui007@gmail.com
 */
public class BaseTestCase {
    //Spring配置文件路径
    private static String[] configLocations = {"conf/spring/applicationContext*.xml"};
    private static ApplicationContext context;

    //设置配置文件路径
    protected static void setConfigLocations(String[] locations) {
        configLocations = locations;
    }

    protected static ApplicationContext getApplicationContext() {
        if (context == null) {
            context = new ClassPathXmlApplicationContext(configLocations);
        }

        return context;
    }
}
