<%@ page language="java" import="java.util.*" import="archiveDataAuto.*" pageEncoding="UTF-8"%>
<jsp:useBean id="parseDBInfo" scope="session" class="archiveDataAuto.ParseDBInfo"/>
<jsp:useBean id="db_source" scope="session" class="archiveDataAuto.DBControl"/>
<jsp:useBean id="db_target" scope="session" class="archiveDataAuto.DBControl"/>

<%@ include file = "banner.jsp" %>
   <div style="margin-left:5%"><font size="3" face="微软雅黑">请选择需要归档的数据表单</font></div> 
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
      List<String> tables = db_source.getTableNames();
      Iterator<String> i = tables.iterator(); %> 
 <form id="chooseData" method="POST" action="archive_progress.jsp">
      <table width=90% frame=box rules=all align="center" bgcolor=#A7C0EE
          style="font-size:15;font-family:微软雅黑;vertical-align:middle;">
          <tr bgcolor=#6495ED style="font-weight:bold;"><td width=2%> </td>
              <td width=35%>表名</td>  
              <td width=21%>现有记录数</td> 
              <td width=21%>已归档记录数</td>
              <td width=21%>待归档记录数</td>          
          </tr>
    <%  while (i.hasNext()) {
	         String str = i.next();%>
          <tr><td><input type="checkbox" name="choosebox" id="<%=str%>" value="<%=str%>"/></td>
              <td><%=str%></td> 
              <td><%=db_source.getNumberOfAllRecord(str)%></td>
              <% boolean e = db_target.isTableExisted(str);%> 
              <td><% if (e){%> <%=db_target.getNumberOfAllRecord(str)%>  
                  <% }else {%> 该表未归档 <% } %> </td> 
              <td><% if (e){%> <%=db_source.getNumberOfAllRecord(str+"_TRIG_TL")%>
                  <% }else {%> 该表未归档 <% } %> </td>             
          </tr>  
    <%   //初次使用系统要在归档前先在源数据库建立备份表、触发器 在目的数据库建立归档表
				if(db_target.isTableExisted(str)){
		    	   System.out.println("目标数据库中"+str+"表已存在");
		    	}else{      		
		    	    int colNum = db_source.getColumnNumber(str);//获得字段个数  columnNumber
				    List<column> cols = db_source.getColumnsInfo(str);//获得字段信息 				    
		    		System.out.println("111");
		    		if(!db_source.isTableExisted(str+"_TRIG_TL")){
		    			//源数据库中创建触发器用表		
		        	    String table_for_trigger = db_source.getTableSQL(str,"sourceForTrigger");
		                System.out.println(table_for_trigger);
		                db_source.creatTablebySQL(table_for_trigger);
		    		}
		            // 源数据库中创建触发器 
		            db_source.creatTrigger(str,colNum,cols);
		            //目标数据库中创建归档表   
		            String table_for_target = db_source.getTableSQL(str,"target");
		            System.out.println(table_for_target);
		            db_target.creatTablebySQL(table_for_target);
		    	}       
    }%>      
          <tr style="font-weight:bold;">
              <td><input type="checkbox" name="choosebox" id="archiveType" value="Condition"/></td>
              <td colspan="5">根据配置信息归档</td></tr>                                                                                           
          <tr bgcolor=#FFFFFF ><td style="border-bottom:0px"align="center" colspan="5">
              <input type="button" name="data_arch" value="全选" onclick="check_all()"/>
              <input type="reset" name="reset" value="重置"/>
	          <input type="submit" name="data_arch" value="数 据 归 档"/></td></tr>
      </table> 
</form> 
        	
  </body>
</html>

<script>   
   function  check_all(){   
      arr = document.getElementsByName("choosebox");   
      for(var arrNum=0;arrNum<arr.length;arrNum++){   
              arr[arrNum].checked=true;   
        }   
   }   
</script>
