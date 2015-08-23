<%@ page language="java" pageEncoding="UTF-8"%>
<html>
  <head>
    <title>数据自动归档系统</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <STYLE> 
      body 
      {background-image:url(926508.jpg); background-position:center; background-repeat:no-repeat}
    </STYLE>
   </head>  
   <body>
      <% request.getSession().removeAttribute("username");%>
      <br><br><br>
      <h2 style="font-weight: normal;" align="center"><font face="微软雅黑">数据自动归档系统</font></h2>
      <h4 style="font-weight: normal;"></h4><div align="center"><font face="微软雅黑"><br><br><font size="6">请登录</font></font></div><h4 style="font-weight: normal;"></h4>
      
      <br><br><br>
      <form name="loginForm" method="POST" action="loginAction">  
         <table align="center">
           <tr><td><font size="5" face="微软雅黑">用户名:</font></td>
               <td><input type="text" name="userid" size="25"></td></tr>
           <tr><td><font size="5" face="微软雅黑">密码:</font></td>
               <td><input type="password" name="pwd" size="25"></td></tr>
           <tr><td><font size="4" face="微软雅黑"><strong><a href="enroll.jsp">新用户注册</a></strong></font></td>
               <td align="center"><input type="submit" name="login" value="登陆">
                                  <input type="reset" name="reset" value="重置"></td></tr>               
         </table>
      </form>  
  </body> 
  
</html>
