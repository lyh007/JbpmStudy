package com.live.webmail.action.user;


import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.config.NullResult;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 10-11-26
 * Time: 下午3:40
 */
@Results({
        @Result(name = "success", location = "/success.jsp", params = {}),
        @Result(name = "error", location = "/error.html", params = {})
})
public class LoginAction extends ActionSupport{
    public String login() {
        return "success";
    }
}
