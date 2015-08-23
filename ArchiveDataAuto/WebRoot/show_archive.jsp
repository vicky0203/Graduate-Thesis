<%@ page language="java" import="java.util.*" import="archiveDataAuto.*" pageEncoding="UTF-8"%>
<jsp:useBean id="parseDBInfo" scope="session" class="archiveDataAuto.ParseDBInfo"/>
<jsp:useBean id="db_source" scope="session" class="archiveDataAuto.DBControl"/>
<jsp:useBean id="db_target" scope="session" class="archiveDataAuto.DBControl"/>

<%@ include file = "banner.jsp" %>
   <div style="margin-left:5%"><font size="3" face="微软雅黑">归档数据查询</font></div> 
   <br>
   <% //解析配置文件 获取源数据库和目标数据库信息
      parseDBInfo.ParseXMLData("database_info.xml");
      List<DBInfo> dbinfos = parseDBInfo.getDBInfos();
      List<DBTransform> dbtrans = parseDBInfo.getDBTransform();
      List<DBInstruct> dbinst = parseDBInfo.getDBInstruct();   
         	
    	String DBType_oracle = "oracle";
        String DBType_mysql = "mysql";
  	    String IDsource = "source";
  	    String IDtarget = "target";
        for(DBInfo dbinfo : dbinfos){
        	//System.out.println(dbinfo.toString());
            String url = null;
        	if(dbinfo.getDBType().equals(DBType_oracle)){//数据库类型为oracle
                 url = "jdbc:oracle:thin:@//"+dbinfo.getDBHost()+"/"+dbinfo.getDBName();
        	}else if(dbinfo.getDBType().equals(DBType_mysql)){
        		 //"jdbc:mysql://localhost:3306/relicdb?useUnicode=true&characterEncoding=utf-8";
        		 url = "jdbc:mysql://"+dbinfo.getDBHost()+"/"+dbinfo.getDBName()+"?useUnicode=true&characterEncoding=utf-8";
        	}
        	//System.out.println(dbinfo.getDBType()+"数据库URL: "+url);
        	if(dbinfo.getDBIdentity().equals(IDsource)){
        		 db_source.setDBInfo(url,dbinfo.getDBUser(),dbinfo.getDBPassword());
        	}else if(dbinfo.getDBIdentity().equals(IDtarget)){
        		 db_target.setDBInfo(url,dbinfo.getDBUser(),dbinfo.getDBPassword());
        	}
      }
              
      //显示源数据库中表名
      List<String> tables = db_target.getTableNames();
      Iterator<String> i = tables.iterator(); %> 
      <table width=40% frame=box rules=all align="center" bgcolor=#A7C0EE
          style="font-weight:bold;font-size:15;font-family:微软雅黑;vertical-align:middle;">
          <%  while (i.hasNext()) {
                  String str = i.next();%>
                  <tr><td><a href = "select_archive_data.jsp?tablename=<%=str%>"><%=str%></a></td></tr>
          <%}%>
      </table>    
  </body>
</html>