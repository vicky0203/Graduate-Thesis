<%@ page language="java" import="java.util.*" import="archiveDataAuto.*" pageEncoding="UTF-8"%>
<jsp:useBean id="db_source" scope="session" class="archiveDataAuto.DBControl"/> 
<jsp:useBean id="db_target" scope="session" class="archiveDataAuto.DBControl"/>
<jsp:useBean id="parseDBInfo" scope="session" class="archiveDataAuto.ParseDBInfo"/>

<%@ include file = "banner.jsp" %>

<% request.setCharacterEncoding("utf-8");  //解决乱码
   response.setHeader("iso-8859-1","utf-8");//解决乱码
   response.setCharacterEncoding("utf-8");//解决乱码 
   //获取选中的表名
   String tablename = request.getParameter("tablename"); %>
   <form id="selectData" method="POST" action="show_select_data.jsp?tablename=<%=tablename%>">
      <table width=90% frame=box rules=all align="center" 
          style="font-size:15;font-family:微软雅黑;vertical-align:middle;">
           <tr bgcolor=#A7C0EE style="font-weight:bold;"><td colspan="3"><%=tablename %></td></tr>
           <tr bgcolor=#A7C0EE style="font-weight:bold;"><td>字段</td><td>请输入查询信息</td>
                                                         <td>请选择查询条件</td>
           </tr>
         <%  List<String> columnsName = db_target.getColumnNames(tablename); 
             for (String s : columnsName){%>
                <tr><td><%=s%></td><td><input type="text" name="<%=s%>" style="width:100%;"/></td>
                    <td><select name="<%=s%>_con" style="width:100%;">
                               <option value="0"> </option>
                               <option value=">">大于</option>
                               <option value=">=">大于等于</option>
                               <option value="=">等于</option>
                               <option value="&lt;">小于</option>
                               <option value="&lt;=">小于等于</option></select></td>
                </tr>
         <%  } %>  
           <tr><td colspan="3" align="center"><input type="submit" value="提交查询"/>
               <input type="button" name="cancel" value="取消" onclick="javascript:location.href='show_archive.jsp';"/></td>
           </tr>
      </table> 
  </form> 	