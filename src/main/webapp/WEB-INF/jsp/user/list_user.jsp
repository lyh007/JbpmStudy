<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>Simple jsp page</title></head>
<body><h1>list user!</h1><br/>
<s:if test="userList!=null">
    <table>
        <s:iterator value="userList">
            <tr>
                <td><s:property value="id"/></td>
                <td><s:property value="name"/></td>
            </tr>
        </s:iterator>
    </table>
</s:if>
</body>
</html>