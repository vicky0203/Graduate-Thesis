<%@ page language="java" import="java.util.*" import="archiveDataAuto.*" pageEncoding="UTF-8"%>
<!-- 自定义标签的引入 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- 如果在同一客户的不同JSP页面中声明了相同id的javabean且范围仍为scope更改此javabean的成员变量值，
其他页面中此id的bean的成员变量值也会被改变。当客户打开服务器上的所有网页都被关闭时，对应的客户的这一次会话中的javabean被取消。 -->
<jsp:useBean id="db_source" scope="session" class="archiveDataAuto.DBControl"/> 
<jsp:useBean id="db_target" scope="session" class="archiveDataAuto.DBControl"/>
<jsp:useBean id="parseDBInfo" scope="session" class="archiveDataAuto.ParseDBInfo"/>
<script language="javascript" type="text/javascript" src="jquery-1.11.0.js" charset="UTF-8"></script>
<script language="javascript" type="text/javascript" src="progress.js" charset="UTF-8"></script>

<%@ include file = "banner.jsp" %>
<% request.setCharacterEncoding("utf-8");  //解决乱码
   response.setHeader("iso-8859-1","utf-8");//解决乱码
   response.setCharacterEncoding("utf-8");//解决乱码 
   //获取选中的表名
   String chooseboxstr[] = request.getParameterValues("choosebox");	
   if(chooseboxstr==null || (chooseboxstr!=null && chooseboxstr.length==0)) { //前者为arr[]=null 后者为arr[]={""}时
       System.out.println("复选内容没有选择");
       //待改进
       response.sendError(HttpServletResponse.SC_FORBIDDEN);
  	   return;
  	}else { //开始执行数据归档
  	 //先判断归档模式 是否是根据配置文件Condition  归档 即判断复选框最后一项是否被选中 
     String archiveType = "Condition";
     if (!archiveType.equals(chooseboxstr[chooseboxstr.length - 1])){
          archiveType = "General";
     }
     System.out.println("归档类型:"+archiveType);
     
     //获得各表归档顺序
     String arSq[] = null;
     if("Condition".equals(archiveType)){
        arSq = new String[chooseboxstr.length - 1];
        for (int j=0;j<arSq.length;j++){ 
            arSq[j]=chooseboxstr[j];
        }
     }else{
        arSq = db_source.getArchieveSequence(chooseboxstr);
     }
/*      for (String n : arSq){
        System.out.println(n);
     } */
         
     //根据表名获得表的记录数  //int[] sn = {163,433,100,210,20};
     parseDBInfo.ParseXMLData("database_info.xml");
     List<DBInstruct> dbinst = parseDBInfo.getDBInstruct(); 
     int[] recordNum = new int[arSq.length];
     for(int t=0;t<arSq.length;t++){
         if("Condition".equals(archiveType)){
              recordNum[t] = db_source.getRecordsNumber(arSq[t],archiveType,dbinst);
         }else if("General".equals(archiveType)){
              recordNum[t] = db_source.getNumberOfAllRecord(arSq[t]+"_TRIG_TL");
         }
     }
     
     for (int l : recordNum){
        System.out.println(l);
     }
      
%>  		
<body id="process" onload="process()"><!-- onload="process()" -->
  	<div style="margin-left:5%;font-weight:bold"><font size="3" face="微软雅黑">数据归档进行中，请等待</font></div> 
    <br>
  	<table frame=box rules=all cellspacing="50" align="center"
           style="font-weight:bold; font-size:15;font-family:微软雅黑;vertical-align:middle;">
     <% for(int i = 0;i<arSq.length;i++){           
          String tempName = arSq[i]; //System.out.println("选择的数据表"+tempName);%>
         <tr><td width=250 bgcolor=#A7C0EE><%=tempName%></td>
             <td width=450px bgcolor=#F6EEF4>
                   <%int ii = 1; %>
                   <div id="progressBar<%=i%>"  style="visibility:hidden;width:450px" align="left">									      
						  <c:forEach begin="1" end="50" step="1" ><span id="table<%=i%>block<%=ii++ %>" style="width:2%;margin:0px;display:-moz-inline-box;display:inline-block;">
						  </span></c:forEach>									       
				   </div>
             </td>
             <td>当前完成进度</td>
             <td width=50px id="table<%=i%>finish">  </td>
         </tr> 
      <%}%>
     </table>
     
     <table style="visibility:hidden"> 
        <tr><td><input type="text" id="archiveType" value="<%=archiveType%>"/></td></tr>
        <tr><td><input type="text" id="tableNum" value="<%=arSq.length%>"/></td></tr>
        <%for(int j=0;j<arSq.length;j++){%>
             <tr><td><input type="text" id="table<%=j%>" value="<%=arSq[j]%>"/></td></tr>
             <tr><td><input type="text" id="table<%=j%>recordNum" value="<%=recordNum[j]%>"/></td></tr>      
        <%}%>
     </table>
</body>
<%}%>
</html>