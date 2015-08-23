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
	private Logger dataArchiveLogger = Logger.getLogger("dataArchiveLogger");//������־
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String task = request.getParameter("task");
		String res = "";
		if (task.equals("first")) {
			res = "1";			
			//�˴����ԶԷǵ�1�ε��첽���ʹ��������ݣ���һЩ��ʼ������    ���Ӧ����fist task����Ҫ����
			request.getSession().setAttribute("percent", 0);
		} else {//���ݹ鵵���ܲ���						
			//�Ƚ������ݿ����� �������DBʵ�����鵵������Ϣ
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
	        	if(dbinfo.getDBType().equals(DBType_oracle)){//���ݿ�����Ϊoracle
	                 url = "jdbc:oracle:thin:@//"+dbinfo.getDBHost()+"/"+dbinfo.getDBName();
	        	}else if(dbinfo.getDBType().equals(DBType_mysql)){
	        		 //"jdbc:mysql://localhost:3306/relicdb?useUnicode=true&characterEncoding=utf-8";
	        		 url = "jdbc:mysql://"+dbinfo.getDBHost()+"/"+dbinfo.getDBName()+"?useUnicode=true&characterEncoding=utf-8";
	        	}
	        	//System.out.println(dbinfo.getDBType()+"���ݿ�URL: "+url);
	        	if(dbinfo.getDBIdentity().equals(IDsource)){
	        		 db_source.setDBInfo(url,dbinfo.getDBUser(),dbinfo.getDBPassword());
	        	}else if(dbinfo.getDBIdentity().equals(IDtarget)){
	        		 db_target.setDBInfo(url,dbinfo.getDBUser(),dbinfo.getDBPassword());
	        	}
	        }
				        
	        //��ȡ��ǰ�鵵��������Ϣ
			//��ñ���
			String tableName =  request.getParameter("tableName");//System.out.println("tableName:"+tableName);
			//��õ�ǰ�鵵��ı�� ��0��ʼ
			String temp = request.getParameter("tableNo");
			int tableNo = 0;
			if(!"0".equals(temp)||temp!=null){
				tableNo =  Integer.parseInt(temp);
			}
            //����ܹ鵵�����
			int totalTableNumber =  Integer.parseInt(request.getParameter("totalTableNumber"));
			//System.out.println(tableName+"tableNo"+tableNo+"totalTableNumber"+totalTableNumber);
			//��õ�ǰҪ�鵵���ܼ�¼��
			int recordNumber =  Integer.parseInt(request.getParameter("recordNumber")); //System.out.println(recordNumber);
			//��ù鵵����
			String archiveType = request.getParameter("archiveType"); //System.out.println("archiveType: "+archiveType);
			
			//ǰ��׼������ ��ʼ�鵵
			try {
				//���ݱ������ columnNumber,recordNum,everyPageNum��servlet������,firstIndex=����ɵ�����percentage+1
				int colNum = db_source.getColumnNumber(tableName);//����ֶθ���  columnNumber
				List<column> cols = db_source.getColumnsInfo(tableName);//����ֶ���Ϣ 
								
				//��ʼ�������ݹ鵵
				if(percentStr<recordNumber && tableNo<totalTableNumber){
				    String lastPercent = request.getSession().getAttribute("percent").toString(); //��һ������ɹ鵵�ļ�¼��
				    int firstIndex = 1;
				    if(!"0".equals(lastPercent)){
					   firstIndex =  Integer.parseInt(lastPercent)+1;
				    }
				    //System.out.println("lastPercent:"+lastPercent+"firstIndex:"+firstIndex);
				    //��ʼȡ��¼����   
				    List<tableContants> contants = db_source.getColumnContants(tableName,archiveType,
                            colNum,firstIndex,everyPageNum,recordNumber,dbinst);
				    //������ �鿴ԭʼ����
				    Iterator<tableContants> ji = contants.iterator();
	                while (ji.hasNext()) {
	               	    tableContants t = ji.next();
	               	    System.out.println(t.toString());          	   
	      	        } 
				    
				    //���ݹ鵵ģʽҪ��������
		            if ("Condition".equals(archiveType)){
		                Iterator<tableContants> j = contants.iterator();
		                while (j.hasNext()) {
		               	    tableContants t = j.next();
		               	    //System.out.println(t.toString());
		               	    t.transContants(dbtrans,tableName);
		               	    System.out.println(t.toString());              	   
		      	        }            	
		            }
	                //��Ŀ�����ݿ��������
		            db_target.dateArchive(tableName,colNum,cols,contants);
	                //ɾ��ԭʼ���ݿ�����
		            
		            
		            //������ɹ鵵��ɽ��Ȱٷֱ�
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
/*					     //ÿ�鵵��һ����  ɾ������  ������־
					     List<tableContants> cFordelete = db_source.getAllContants(tableName,archiveType,
					    		 colNum,dbinst);
					     for(tableContants cd : cFordelete){
					        	System.out.println("uuuu--"+cd.toString());
					     }
					     db_source.deletArchivedData(tableName, archiveType, cFordelete);*/
					     
					     //����INFO������־ �鵵�ɹ� �鵵��Ϣ
					     String info = "Archive Success\n     -- Archive Type: "+archiveType+", " +
			     		               "Table Name: "+tableName+", Table No."+temp+", " +
			     		               "Archive Records Number: "+recordNumber;
					     if( "Condition".equals(archiveType) && !dbinst.isEmpty()) {//���tableName��ͬ�Ĺ鵵���� 
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
					    					     
					     //�жϹ鵵�Ƿ����
					     if(tableNo == (totalTableNumber - 1)){
						     request.getSession().removeAttribute("percent");
						     System.out.println("�鵵ȫ������������");
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
				//���������־
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