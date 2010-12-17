package com.live.webmail.action.user;

import com.live.webmail.dao.UserDao;
import com.live.webmail.model.User;
import com.live.webmail.service.UserService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.jbpm.api.ProcessEngine;
import org.jbpm.pvm.internal.processengine.SpringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kevin
 * @version Revision: 1.00 Date: 10-12-3上午9:38
 * @Email liuyuhui007@gmail.com
 * 用户管理
 */
@Controller
@Scope("prototype")
@ParentPackage(value = "struts-default")
@Results(
        @Result(name = "success", location = "/WEB-INF/jsp/user/list_user.jsp")
)
public class UserAction extends ActionSupport {
    public List<User> userList = new ArrayList<User>();
    @Autowired
    private UserService userService;
    @Autowired
    private ProcessEngine processEngine;
    //用户列表
    public String list() {
        userList = userService.queryAll();
        processEngine.getExecutionService();
        return SUCCESS;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

}
