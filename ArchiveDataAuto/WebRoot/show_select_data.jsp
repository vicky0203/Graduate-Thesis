<%@ page language="java" import="java.util.*" import="archiveDataAuto.*" pageEncoding="UTF-8"%>
<jsp:useBean id="db_source" scope="session" class="archiveDataAuto.DBControl"/> 
<jsp:useBean id="db_target" scope="session" class="archiveDataAuto.DBControl"/>
<jsp:useBean id="parseDBInfo" scope="session" class="archiveDataAuto.ParseDBInfo"/>

<%@ include file = "banner.jsp" %>

<% request.setCharacterEncoding("utf-8");  //解决乱码
   response.setHeader("iso-8859-1","utf-8");//解决乱码
   response.setCharacterEncoding("utf-8");//解决乱码 
   //获取查询信息
   String tablename = request.getParameter("tablename");
   List<String> columnsName = db_target.getColumnNames(tablename);
   String cols[] = new String[columnsName.size()];
   String cons[] = new String[columnsName.size()];
   int i=0; 
   for (String s : columnsName){
      cols[i] = request.getParameter(s); //cols内的顺序与得到的colsName顺序一致
      cons[i] = request.getParameter(s+"_con");
      i++;
   }
   
   //获得查询结果
   List<tableContants> records =  db_target.getSelectRecord(tablename, cols, cons);%>
    <table width=90% frame=box rules=all align="center" 
          style="font-size:15;font-family:微软雅黑;vertical-align:middle;">
           <tr bgcolor=#A7C0EE style="font-weight:bold;"><td colspan="<%=columnsName.size()%>"><%=tablename %></td></tr>
           <tr bgcolor=#A7C0EE style="font-weight:bold;">
               <% for (String s : columnsName){ %>
                   <td><%=s%></td>
               <%} %> </tr>
      <% //显示查询结果
         for (tableContants t : records){ %>
            <tr><%for (int n=1;n<=columnsName.size();n++){%>
                   <td><%=t.getContantbyIndex(n)%></td>
                <%}%></tr>  
       <%}%>
         <tr><td align="center"colspan="<%=columnsName.size()%>">
             <input type="button" name="cancel" value="返回" onclick="javascript:location.href='show_archive.jsp';"/>
         </td></tr>
   </table> 