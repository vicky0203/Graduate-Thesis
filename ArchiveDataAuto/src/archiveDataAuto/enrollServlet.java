package archiveDataAuto;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class enrollServlet extends HttpServlet {
	private DBControl db_source = new DBControl();
	
	protected void doPost( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {     
		String username = request.getParameter("userid");
		String password = request.getParameter("pwd");
		if ( username.isEmpty() || password.isEmpty() || (username.isEmpty() && password.isEmpty()) ){			
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}	
		if ( !username.isEmpty() && !password.isEmpty() ){
			username = new String (username.getBytes("iso-8859-1"),"utf-8");
			password = new String (password.getBytes("iso-8859-1"),"utf-8");
		}
		
		//先解析数据库配置 获得DB实例
		ParseDBInfo prasedb = new ParseDBInfo();
    	prasedb.ParseXMLData("database_info.xml");
    	List<DBInfo> dbinfos = prasedb.getDBInfos();
    	
    	String DBType_oracle = "oracle";
        String DBType_mysql = "mysql";
  	    String IDsource = "source";
        for(DBInfo dbinfo : dbinfos){
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
        	}
        }
		
        try {
        	db_source.adduser(username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        response.setContentType("text/html;charest=utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>注册成功</title></head>" +
        		    "<body><script charset='utf-8'>alert('成功注册，请登陆！');location.href='index.jsp';</script>" +
        		    "</body></html>");
        out.close();
	} 
}