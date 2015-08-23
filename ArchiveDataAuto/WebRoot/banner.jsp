<%@ page language="java" pageEncoding="UTF-8"%>
<html>
  <head><title>数据自动归档系统</title></head>  
  <body>
      <div align="center"><img height="10%" width=90% src="24434.jpg"></div>
      <table cellspacing=0 frame=below rules=rows border="3" bordercolor:="#5151A2"  
             width=90%  align="center" style="font-size:15;font-family:微软雅黑;vertical-align:middle;">
             <tr><td><a href = "index.jsp">首页</a></td>
                 <td align="center"><a href = "show_tables.jsp">数据归档</a></td>
                 <td align="center"><a href = "show_archive.jsp">归档数据查询</a></td>
                 <% 
                    String user = request.getSession().getAttribute("username").toString();
                 %>
                 <td align="right">用户：<%=user%></a></td><td align="right"><a href= "index.jsp" >注销</a></td>
             </tr> 
      </table>
      <!-- <hr width=90% size="2" color=#5151A2 style="FILTER: alpha(opacity=100,finishopacity=0,style=3)"> -->
      <br>
