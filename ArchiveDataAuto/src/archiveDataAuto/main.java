package archiveDataAuto; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;
  
public class main{
	//"EMPLOYEES";
	//"DEPARTMENTS";
	//"MANAGES";
	private static DBControl db_source = new DBControl();
	private static DBControl db_target = new DBControl();
	private static List<DBTransform> dbtrans = null;
	private static List<DBInstruct> dbinst = null;
	
	
    public static void main(String[] args) throws Exception{
    	//解析XML数据库配置
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
        	System.out.println(dbinfo.toString());
            String url = null;
        	if(dbinfo.getDBType().equals(DBType_oracle)){//数据库类型为oracle
                 url = "jdbc:oracle:thin:@//"+dbinfo.getDBHost()+"/"+dbinfo.getDBName();
        	}else if(dbinfo.getDBType().equals(DBType_mysql)){
        		 //"jdbc:mysql://localhost:3306/relicdb?useUnicode=true&characterEncoding=utf-8";
        		 url = "jdbc:mysql://"+dbinfo.getDBHost()+"/"+dbinfo.getDBName()+"?useUnicode=true&characterEncoding=utf-8";
        	}
        	System.out.println(dbinfo.getDBType()+"数据库URL: "+url);
        	if(dbinfo.getDBIdentity().equals(IDsource)){
        		 db_source.setDBInfo(url,dbinfo.getDBUser(),dbinfo.getDBPassword());
        	}else if(dbinfo.getDBIdentity().equals(IDtarget)){
        		 db_target.setDBInfo(url,dbinfo.getDBUser(),dbinfo.getDBPassword());
        	}
        }
        
        for(DBTransform dbt : dbtrans){
        	System.out.println(dbt.toString());
        }
        for(DBInstruct dbi : dbinst){
        	System.out.println(dbi.toString());
        }
        
        
/*        String[] sql_where = db_source.getDBInstruct().split(":");       
        for(int i = 0;i< sql_where.length;i++){
        	System.out.println(sql_where[i]);
        }*/
        /*//测试指定表列名
         List<String> tables = db_source.getTableNames();
	     Iterator<String> j0 = tables.iterator();
	     while (j0.hasNext()) {
	    	  System.out.println("Y表名:" + j0.next());	    	 
	     }
	     
	     List<String> tables2 = db_target.getTableNames();
	     Iterator<String> j01 = tables2.iterator();
	     while (j01.hasNext()) {
	    	  System.out.println("T表名:" + j01.next());	    	 
	     }
	     
         //测试指定表列名
         List<String> columns = db_source.getColumnNames("EMPLOYEES");
	     Iterator<String> j = columns.iterator();
	     while (j.hasNext()) {
	    	  System.out.println("字段名:" + j.next());	    	 
	     }*/
	     	     
	     /*//测试指定表主键
	     List<primaryKey> primaryKey = db_source.getPrimaryKeys("TEST");
	     Iterator<primaryKey> jj = primaryKey.iterator();
	     while (jj.hasNext()) {
	    	 primaryKey temp = jj.next();
	    	  System.out.println("MAIN里面测试结果  主键名："+ temp.getPKName()+", 位置："+temp.getPKNo());	    	 
	     }*/
	                                            
	    /* //测试指定表外键
	     List<foreignKey> foreignKey = db_source.getForeignKeys("MANAGES");
	     Iterator<foreignKey> jjj = foreignKey.iterator();
	     while (jjj.hasNext()) {
	    	 foreignKey temp = jjj.next();
	         System.out.println("MAIN里面测试结果   外键列名："+temp.getFKName()+" 号："+temp.getFKNo()+
	        		 " 主键表名："+temp.getPKTablenName()+
	             " 主键列名称: "+temp.getPKColumnName()+" 更新主键时外键发生的变化: "+temp.getUpdateRule()+
	             " 删除主键时外键发生的变化: "+temp.getDeleteRule());
	     }*/
        
        
         //String tableSQL = db_source.getTableSQL("TEST");
         //System.out.println(tableSQL);
         //String tableSQL1 = db_source.getTableSQL("TEST2");
         //String tableSQL2 = db_source.getTableSQL("MANAGES");
         //System.out.println(tableSQL2);
         //String tableSQL3 = db_target.getTableSQL("TARGETTEST");
         /*//System.out.println(tableSQL3);
         String sql = "CREATE TABLE MA2 (SSN CHAR(20),DID NUMBER  not null,SINCE DATE ,PRIMARY KEY(DID)," +
         		"constraint fk_der2 FOREIGN KEY (DID) REFERENCES DEPARTMENTS(DID) on delete cascade," +
         		"constraint fk_s3n2 FOREIGN KEY (SSN) REFERENCES EMPLOYEES(SSN) on delete cascade)";*/
        /*String sql = "CREATE TABLE dsf (DID NUMBER(38), SSN CHAR(20),SINCE DATE, primary key (DID),"+
        		"constraint fk_did foreign key(DID) references DEPARTMENTS(DID) on delete cascade," +
        		"constraint fk_ssn foreign key(SSN) references EMPLOYEES(SSN) on delete cascade)";*/
         //String tableSQL4 = db_source.getTableSQL("EMPLOYEES"); //CONSTRAINT FK_DID CONSTRAINT FK_SSN 
         //System.out.println(tableSQL4);
         //String tableSQL5 = db_source.getTableSQL("DEPARTMENTS");
         //System.out.println(tableSQL5);
         //db_target.creatTablebySQL(tableSQL4);
         //db_target.creatTablebySQL(tableSQL5);
         //db_target.creatTablebySQL(tableSQL2);
         
        
/*         int everyPageNum = 1;//每次取出n个数据record进行归档 
         int colNum = db_source.getColumnNumber("MANAGES");
         int recordNumber = db_source.getNumberOfAllRecord("MANAGES");
         List<column> cols = db_source.getColumnsInfo("MANAGES");//获得字段信息
         //List<String> columnNames = db_source.getColumnNames("MANAGES");
         System.out.println(recordNumber);
         //计算总归档次数
         int totaltimes;
         if (recordNumber==0){
        	 totaltimes=1;
         }else{
        	 totaltimes = (recordNumber%everyPageNum==0)?(recordNumber/everyPageNum):(recordNumber/everyPageNum+1);
         }
         
         for (int i=1; i<=totaltimes; i++){       	 
             int firstIndex=(i-1) * everyPageNum + 1;//计算当前提取的第一条记录的索引
             System.out.println("当前提取的第一条记录的索引"+firstIndex);
             List<tableContants> contants = db_source.getColumnContants("MANAGES",colNum,firstIndex,everyPageNum,recordNumber);
             Iterator<tableContants> j = contants.iterator();
             while (j.hasNext()) {
            	 tableContants t = j.next();
            	 String s = "";
            	 for (int ii =1;ii<=colNum;ii++){
            		 s += t.getContantbyIndex(ii).trim()+"::";
            	 }
   	    	     System.out.println(s);	    	 
   	         }
             
             //向目标数据库插入数据
             db_target.dateArchive("MANAGES", colNum,cols,contants);
             
             //删除原始数据库数据
             
         }*/
        
         //
        
        
         //testArchive("EMPLOYEES");
         testArchive("DEPARTMENTS");
         //testArchive("MANAGES");
        
       /* String s = "delete from EMPLOYEES_TRIG_TL where SSN = ?";
        Connection con = db_source.getConnection();
        con.setAutoCommit(false);
        PreparedStatement ps = con.prepareStatement(s);
        	ps.setString(1,"233433              ");
        	ps.addBatch(); 
        	ps.setString(1,"555567              ");
        	ps.addBatch();
        
        ps.executeBatch();
        con.commit();  
        ps.close();
        con.close(); */
        
/*        String sql = "insert into MANAGES (SSN,DID,SINCE) values (?,?,?)";
        Connection con  = db_target.getConnection();          
 		PreparedStatement prepStmt = con.prepareStatement(sql);
 		prepStmt.setString(1,"123223666");
 		prepStmt.setString(2,"50000");
 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = sdf.parse("1980-12-17 00:00:00");
        Timestamp ts = new Timestamp(dateTime.getTime());
        TimeZone tz = TimeZone.getTimeZone("gmt"); 
        Calendar calendar = Calendar.getInstance(tz);
        
 		prepStmt.setTimestamp(3,ts,calendar);
 		//prepStmt.setString(3,"TO_DATE('1980-12-17 00:00:00','yyyy-mm-dd hh24:mi:ss')");
 	    prepStmt.executeUpdate();
 	   try{
	        if(prepStmt!=null) prepStmt.close();
	    }catch(Exception e){
	        e.printStackTrace();
	    }
 	  try{
	        if(con!=null) con.close();
	    }catch(Exception e){
	        e.printStackTrace();
	    }  System.out.println("creatTablebySQL关闭连接");*/
        
/*        String sql = "insert into MANAGES (SSN,DID,SINCE) values (123223666,50000,TO_DATE('1980-12-17 00:00:00','yyyy-mm-dd hh24:mi:ss'))";
        Connection con  = db_target.getConnection();          
		PreparedStatement prepStmt = con.prepareStatement(sql);
	    prepStmt.executeUpdate();
	   try{
	        if(prepStmt!=null) prepStmt.close();
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	  try{
	        if(con!=null) con.close();
	    }catch(Exception e){
	        e.printStackTrace();
	    }  System.out.println("insert关闭连接");*/    
        
        
/*        db_source.tempselectarchivetest();
        String selectStatement = "select * from TEST where ID = '567' AND C1 = 'hiufhfs'";        
        String sql1 = "select * from EMPLOYEES where LOT<='27' order by LOT";
        String sql2 = "select * from (select A.*, rownum as rn from ("+sql1+") A where rownum<=10) where rn>=5";
        String sql3 = "select * from EMPLOYEES where LOT<='27' and SSN<='111102098' ";
        String sql4 = "select * from (select A.*, rownum as rn from ("+sql3+") A where rownum<=3) where rn>=1";
        String sql5 = "select count(*) from EMPLOYEES where LOT<='27' and SSN<='111102098' ";  
        System.out.println(db_source.getNumberOfAllRecord(sql5));
        db_source.tempselectarchivetest(sql5);*/
        //int colNum = db_source.getColumnNumber("EMPLOYEES");//获得字段个数    		
        //List<column> cols = db_source.getColumnsInfo("EMPLOYEES");//获得字段信息 
        //db_source.creatTrigger("EMPLOYEES",colNum,cols);
        
        //int colNum = db_source.getColumnNumber("DEPARTMENTS");//获得字段个数    		
        //List<column> cols = db_source.getColumnsInfo("DEPARTMENTS");//获得字段信息 
        //db_source.creatTrigger("DEPARTMENTS",colNum,cols);
        
        //int colNum = db_source.getColumnNumber("MANAGES");//获得字段个数    		
        //List<column> cols = db_source.getColumnsInfo("MANAGES");//获得字段信息 
        //db_source.creatTrigger("MANAGES",colNum,cols);
        
        
}
    
  
    public static void testArchive(String tablename) throws Exception{
    	int colNum = db_source.getColumnNumber(tablename);//获得字段个数   
    	List<column> cols = db_source.getColumnsInfo(tablename);//获得字段信息
 
/*    	if(db_target.isTableExisted(tablename)){
    	   System.out.println("目标数据库中"+tablename+"表已存在");
    	}else{      		
    		if(!db_source.isTableExisted(tablename+"_TRIG_TL")){
    			//源数据库中创建触发器用表		
        	    String table_for_trigger = db_source.getTableSQL(tablename,"sourceForTrigger");
                System.out.println(table_for_trigger);
                db_source.creatTablebySQL(table_for_trigger);
    		}
            // 源数据库中创建触发器 
            db_source.creatTrigger(tablename,colNum,cols);
            //目标数据库中创建归档表   
            String table_for_target = db_source.getTableSQL(tablename,"target");
            System.out.println(table_for_target);
            db_target.creatTablebySQL(table_for_target);
    	}*/
    	    	
        //数据归档
        int everyPageNum = 1;//每次取出n个数据record进行归档
        String archiveType = "General";
        //if(){ //这里根据选的条件 变归档条件
         	archiveType = "Condition";
        //}
        int recordNumber = db_source.getRecordsNumber(tablename,archiveType,dbinst);//根据配置信息获取要归档的总记录数
        System.out.println("recordNumber:"+recordNumber);
        
        //计算总归档次数
        int totaltimes;
        if (recordNumber==0){
       	    totaltimes=1;
        }else{
       	    totaltimes = (recordNumber%everyPageNum==0)?(recordNumber/everyPageNum):(recordNumber/everyPageNum+1);
        }
        System.out.println("totaltimes:"+totaltimes);
        
        List<tableContants> cFordelete = new ArrayList<tableContants>();//为了保存取过的记录 最后整删除
        for (int i=1; i<=totaltimes; i++){       	 
            int firstIndex=(i-1) * everyPageNum + 1;//计算当前提取的第一条记录的索引
            //System.out.println("当前提取的第一条记录的索引"+firstIndex);
            //List<tableContants> contants = db_source.getColumnContants(tablename,archiveType,
            //		                               colNum,firstIndex,everyPageNum,recordNumber,dbinst);
/*            //处理数据
            if ("Condition".equals(archiveType)){
                Iterator<tableContants> j = contants.iterator();
                while (j.hasNext()) {
               	    tableContants t = j.next();
               	    System.out.println(t.toString());
               	    //t.transContants(dbtrans,tablename);
               	    //System.out.println(t.toString());              	   
      	        }            	
            }*/
            
           /* //为了测试用
            Iterator<tableContants> j = contants.iterator();
            while (j.hasNext()) {
           	    tableContants t = j.next();
           	    System.out.println("888"+t.toString());           	   
  	        }*/
            
            //向目标数据库插入数据
            //db_target.dateArchive(tablename,colNum,cols,contants);
            
            //cFordelete.addAll(contants);
            
        }
        
        //全部归档完了 再删除原始数据库数据   要不归档一条删除一条 再取记录时rowmun就变化了 会跳行。
        //db_source.deletArchivedData(tablename, archiveType, cFordelete);
        
        //List<tableContants> cFordelete = db_source.getAllContants(tablename,archiveType,colNum,dbinst);
        //for(tableContants cd : cFordelete){
        //	  System.out.println("uuuu--"+cd.toString());
        //}
    }    
}  