package archiveDataAuto;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

public class archiveProgressBarServlet extends HttpServlet {
	private int everyPageNum = 1;
	private int percentStr = 0;
	private static DBControl db_source = new DBControl();
	private static DBControl db_target = new DBControl();
	private static List<DBTransform> dbtrans = null;
	private static List<DBInstruct> dbinst = null;
	private Logger dataArchiveLogger = Logger.getLogger("dataArchiveLogger");//生成日志
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String task = request.getParameter("task");
		String res = "";
		if (task.equals("first")) {
			res = "1";			
			//此处可以对非第1次的异步访问公共的内容，做一些初始化工作    这个应该是fist task的主要任务
			request.getSession().setAttribute("percent", 0);
		} else {//数据归档功能部分						
			//先解析数据库配置 获得两个DB实例及归档配置信息
			ParseDBInfo prasedb = new ParseDBInfo();
	    	prasedb.ParseXMLData("database_info.xml");
	    	List<DBInfo> dbinfos = prasedb.getDBInfos();
	    	dbtrans = prasedb.getDBTransform();
	    	dbinst = prasedb.getDBInstruct();
	    	
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
				        
	        //获取当前归档表的相关信息
			//获得表名
			String tableName =  request.getParameter("tableName");//System.out.println("tableName:"+tableName);
			//获得当前归档表的编号 从0开始
			String temp = request.getParameter("tableNo");
			int tableNo = 0;
			if(!"0".equals(temp)||temp!=null){
				tableNo =  Integer.parseInt(temp);
			}
            //获得总归档表个数
			int totalTableNumber =  Integer.parseInt(request.getParameter("totalTableNumber"));
			//System.out.println(tableName+"tableNo"+tableNo+"totalTableNumber"+totalTableNumber);
			//获得当前要归档的总记录数
			int recordNumber =  Integer.parseInt(request.getParameter("recordNumber")); //System.out.println(recordNumber);
			//获得归档类型
			String archiveType = request.getParameter("archiveType"); //System.out.println("archiveType: "+archiveType);
			
			//前期准备结束 开始归档
			try {
				//根据表名获得 columnNumber,recordNum,everyPageNum由servlet定义了,firstIndex=已完成的条数percentage+1
				int colNum = db_source.getColumnNumber(tableName);//获得字段个数  columnNumber
				List<column> cols = db_source.getColumnsInfo(tableName);//获得字段信息 
								
				//开始进行数据归档
				if(percentStr<recordNumber && tableNo<totalTableNumber){
				    String lastPercent = request.getSession().getAttribute("percent").toString(); //上一次已完成归档的记录数
				    int firstIndex = 1;
				    if(!"0".equals(lastPercent)){
					   firstIndex =  Integer.parseInt(lastPercent)+1;
				    }
				    //System.out.println("lastPercent:"+lastPercent+"firstIndex:"+firstIndex);
				    //开始取记录数据   
				    List<tableContants> contants = db_source.getColumnContants(tableName,archiveType,
                            colNum,firstIndex,everyPageNum,recordNumber,dbinst);
				    //测试用 查看原始数据
				    Iterator<tableContants> ji = contants.iterator();
	                while (ji.hasNext()) {
	               	    tableContants t = ji.next();
	               	    System.out.println(t.toString());          	   
	      	        } 
				    
				    //根据归档模式要求处理数据
		            if ("Condition".equals(archiveType)){
		                Iterator<tableContants> j = contants.iterator();
		                while (j.hasNext()) {
		               	    tableContants t = j.next();
		               	    //System.out.println(t.toString());
		               	    t.transContants(dbtrans,tableName);
		               	    System.out.println(t.toString());              	   
		      	        }            	
		            }
	                //向目标数据库插入数据
		            db_target.dateArchive(tableName,colNum,cols,contants);
	                //删除原始数据库数据
		            
		            
		            //计算完成归档完成进度百分比
                    percentStr = firstIndex - 1 + everyPageNum;
	                if (percentStr>recordNumber){
	            	     percentStr = recordNumber;
	            	     System.out.println("100%");
	                }
	                //System.out.println("percentStr:"+percentStr);
				    res = String.valueOf(percentStr);
				
				    if(percentStr==recordNumber){
					     percentStr = 0;
					     request.getSession().setAttribute("percent", 0);
/*					     //每归档完一个表  删除数据  生成日志
					     List<tableContants> cFordelete = db_source.getAllContants(tableName,archiveType,
					    		 colNum,dbinst);
					     for(tableContants cd : cFordelete){
					        	System.out.println("uuuu--"+cd.toString());
					     }
					     db_source.deletArchivedData(tableName, archiveType, cFordelete);*/
					     
					     //生成INFO级别日志 归档成功 归档信息
					     String info = "Archive Success\n     -- Archive Type: "+archiveType+", " +
			     		               "Table Name: "+tableName+", Table No."+temp+", " +
			     		               "Archive Records Number: "+recordNumber;
					     if( "Condition".equals(archiveType) && !dbinst.isEmpty()) {//获得tableName相同的归档条件 
					         String ins = "";     //"LOT<='27' and SSN<='111102098' "
					         Iterator<DBInstruct> iter_dbi = dbinst.iterator();
					         int c = 0;
					         while(iter_dbi.hasNext()){
					        	  DBInstruct dbi = iter_dbi.next();
					        	  if(dbi.getTablename().equals(tableName)){
					        	       c++;
					       	           if(c>=2){
					       	        	   ins += " and ";
					       	           }
					       	           ins += dbi.getColname()+dbi.getCondition()+"'"+dbi.getValue()+"'";
					        	  }
					         } 
					         info += "\n     -- Archive Condition: "+ins;
					     }   	
					     dataArchiveLogger.info(info); 
					    					     
					     //判断归档是否结束
					     if(tableNo == (totalTableNumber - 1)){
						     request.getSession().removeAttribute("percent");
						     System.out.println("归档全部结束！！！");
						     dataArchiveLogger.info("Data Archive Finished!"); 
					     }
					     //System.out.println("pppppp");
				    }else{
					     request.getSession().setAttribute("percent", percentStr);
					     //System.out.println("lllllll");
					     //System.out.println("p%:"+percentStr);
				    }
			    }												
			} catch (Exception e) {
				e.printStackTrace();
				//输出错误日志
				//dataArchiveLogger.warn("message from the " + dataArchiveLogger.getName()); 
			    //dataArchiveLogger.error("message from the " + dataArchiveLogger.getName()); 
			    dataArchiveLogger.fatal("Archiving was Terminated by Error: " + e.toString());
			}   			
		}
		
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		out.println(res);
		out.close(); 
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}