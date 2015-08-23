<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
  <head><title>数据自动归档系统</title></head>  
   <body>
      <div align="center"><img height="15%" width=90% src="24434.jpg"></div>
      <table width=90%  align="center" style="font-size:15;font-family:微软雅黑;vertical-align:middle;">
             <tr><td>数据自动归档系统</td>
                 <td align="right"><a href= "index.jsp" >退出</a></td>
             </tr>            
      </table>
      <hr width=90% size="3" color=#5151A2 style="FILTER:alpha(opacity=100,finishopacity=0,style=3)">
      <br>
      <div style="margin-left:5%"><font size="3" face="微软雅黑">用户名和密码须为10个字符以内的英文字母或数字</font></div>
      <br>
      <form name="enrollForm" method="POST" action="enrollAction">  
         <table align="center">
           <tr><td><font size="5" face="微软雅黑">用户名:</font></td>
               <td><input type="text" name="userid" size="25"></td></tr>
           <tr><td><font size="5" face="微软雅黑">密码:</font></td>
               <td><input type="password" name="pwd" size="25"></td></tr>
           <tr><td></td>
               <td align="center"><input type="submit" name="enroll" value="注册">
                                  <input type="reset" name="reset" value="重置"></td></tr>               
         </table>
      </form>  
    </body> 
 </html>
