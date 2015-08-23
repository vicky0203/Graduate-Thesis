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
    	//����XML���ݿ�����
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
        	if(dbinfo.getDBType().equals(DBType_oracle)){//���ݿ�����Ϊoracle
                 url = "jdbc:oracle:thin:@//"+dbinfo.getDBHost()+"/"+dbinfo.getDBName();
        	}else if(dbinfo.getDBType().equals(DBType_mysql)){
        		 //"jdbc:mysql://localhost:3306/relicdb?useUnicode=true&characterEncoding=utf-8";
        		 url = "jdbc:mysql://"+dbinfo.getDBHost()+"/"+dbinfo.getDBName()+"?useUnicode=true&characterEncoding=utf-8";
        	}
        	System.out.println(dbinfo.getDBType()+"���ݿ�URL: "+url);
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
        /*//����ָ��������
         List<String> tables = db_source.getTableNames();
	     Iterator<String> j0 = tables.iterator();
	     while (j0.hasNext()) {
	    	  System.out.println("Y����:" + j0.next());	    	 
	     }
	     
	     List<String> tables2 = db_target.getTableNames();
	     Iterator<String> j01 = tables2.iterator();
	     while (j01.hasNext()) {
	    	  System.out.println("T����:" + j01.next());	    	 
	     }
	     
         //����ָ��������
         List<String> columns = db_source.getColumnNames("EMPLOYEES");
	     Iterator<String> j = columns.iterator();
	     while (j.hasNext()) {
	    	  System.out.println("�ֶ���:" + j.next());	    	 
	     }*/
	     	     
	     /*//����ָ��������
	     List<primaryKey> primaryKey = db_source.getPrimaryKeys("TEST");
	     Iterator<primaryKey> jj = primaryKey.iterator();
	     while (jj.hasNext()) {
	    	 primaryKey temp = jj.next();
	    	  System.out.println("MAIN������Խ��  ��������"+ temp.getPKName()+", λ�ã�"+temp.getPKNo());	    	 
	     }*/
	                                            
	    /* //����ָ�������
	     List<foreignKey> foreignKey = db_source.getForeignKeys("MANAGES");
	     Iterator<foreignKey> jjj = foreignKey.iterator();
	     while (jjj.hasNext()) {
	    	 foreignKey temp = jjj.next();
	         System.out.println("MAIN������Խ��   ���������"+temp.getFKName()+" �ţ�"+temp.getFKNo()+
	        		 " ����������"+temp.getPKTablenName()+
	             " ����������: "+temp.getPKColumnName()+" ��������ʱ��������ı仯: "+temp.getUpdateRule()+
	             " ɾ������ʱ��������ı仯: "+temp.getDeleteRule());
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
         
        
/*         int everyPageNum = 1;//ÿ��ȡ��n������record���й鵵 
         int colNum = db_source.getColumnNumber("MANAGES");
         int recordNumber = db_source.getNumberOfAllRecord("MANAGES");
         List<column> cols = db_source.getColumnsInfo("MANAGES");//����ֶ���Ϣ
         //List<String> columnNames = db_source.getColumnNames("MANAGES");
         System.out.println(recordNumber);
         //�����ܹ鵵����
         int totaltimes;
         if (recordNumber==0){
        	 totaltimes=1;
         }else{
        	 totaltimes = (recordNumber%everyPageNum==0)?(recordNumber/everyPageNum):(recordNumber/everyPageNum+1);
         }
         
         for (int i=1; i<=totaltimes; i++){       	 
             int firstIndex=(i-1) * everyPageNum + 1;//���㵱ǰ��ȡ�ĵ�һ����¼������
             System.out.println("��ǰ��ȡ�ĵ�һ����¼������"+firstIndex);
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
             
             //��Ŀ�����ݿ��������
             db_target.dateArchive("MANAGES", colNum,cols,contants);
             
             //ɾ��ԭʼ���ݿ�����
             
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
	    }  System.out.println("creatTablebySQL�ر�����");*/
        
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
	    }  System.out.println("insert�ر�����");*/    
        
        
/*        db_source.tempselectarchivetest();
        String selectStatement = "select * from TEST where ID = '567' AND C1 = 'hiufhfs'";        
        String sql1 = "select * from EMPLOYEES where LOT<='27' order by LOT";
        String sql2 = "select * from (select A.*, rownum as rn from ("+sql1+") A where rownum<=10) where rn>=5";
        String sql3 = "select * from EMPLOYEES where LOT<='27' and SSN<='111102098' ";
        String sql4 = "select * from (select A.*, rownum as rn from ("+sql3+") A where rownum<=3) where rn>=1";
        String sql5 = "select count(*) from EMPLOYEES where LOT<='27' and SSN<='111102098' ";  
        System.out.println(db_source.getNumberOfAllRecord(sql5));
        db_source.tempselectarchivetest(sql5);*/
        //int colNum = db_source.getColumnNumber("EMPLOYEES");//����ֶθ���    		
        //List<column> cols = db_source.getColumnsInfo("EMPLOYEES");//����ֶ���Ϣ 
        //db_source.creatTrigger("EMPLOYEES",colNum,cols);
        
        //int colNum = db_source.getColumnNumber("DEPARTMENTS");//����ֶθ���    		
        //List<column> cols = db_source.getColumnsInfo("DEPARTMENTS");//����ֶ���Ϣ 
        //db_source.creatTrigger("DEPARTMENTS",colNum,cols);
        
        //int colNum = db_source.getColumnNumber("MANAGES");//����ֶθ���    		
        //List<column> cols = db_source.getColumnsInfo("MANAGES");//����ֶ���Ϣ 
        //db_source.creatTrigger("MANAGES",colNum,cols);
        
        
}
    
  
    public static void testArchive(String tablename) throws Exception{
    	int colNum = db_source.getColumnNumber(tablename);//����ֶθ���   
    	List<column> cols = db_source.getColumnsInfo(tablename);//����ֶ���Ϣ
 
/*    	if(db_target.isTableExisted(tablename)){
    	   System.out.println("Ŀ�����ݿ���"+tablename+"���Ѵ���");
    	}else{      		
    		if(!db_source.isTableExisted(tablename+"_TRIG_TL")){
    			//Դ���ݿ��д����������ñ�		
        	    String table_for_trigger = db_source.getTableSQL(tablename,"sourceForTrigger");
                System.out.println(table_for_trigger);
                db_source.creatTablebySQL(table_for_trigger);
    		}
            // Դ���ݿ��д��������� 
            db_source.creatTrigger(tablename,colNum,cols);
            //Ŀ�����ݿ��д����鵵��   
            String table_for_target = db_source.getTableSQL(tablename,"target");
            System.out.println(table_for_target);
            db_target.creatTablebySQL(table_for_target);
    	}*/
    	    	
        //���ݹ鵵
        int everyPageNum = 1;//ÿ��ȡ��n������record���й鵵
        String archiveType = "General";
        //if(){ //�������ѡ������ ��鵵����
         	archiveType = "Condition";
        //}
        int recordNumber = db_source.getRecordsNumber(tablename,archiveType,dbinst);//����������Ϣ��ȡҪ�鵵���ܼ�¼��
        System.out.println("recordNumber:"+recordNumber);
        
        //�����ܹ鵵����
        int totaltimes;
        if (recordNumber==0){
       	    totaltimes=1;
        }else{
       	    totaltimes = (recordNumber%everyPageNum==0)?(recordNumber/everyPageNum):(recordNumber/everyPageNum+1);
        }
        System.out.println("totaltimes:"+totaltimes);
        
        List<tableContants> cFordelete = new ArrayList<tableContants>();//Ϊ�˱���ȡ���ļ�¼ �����ɾ��
        for (int i=1; i<=totaltimes; i++){       	 
            int firstIndex=(i-1) * everyPageNum + 1;//���㵱ǰ��ȡ�ĵ�һ����¼������
            //System.out.println("��ǰ��ȡ�ĵ�һ����¼������"+firstIndex);
            //List<tableContants> contants = db_source.getColumnContants(tablename,archiveType,
            //		                               colNum,firstIndex,everyPageNum,recordNumber,dbinst);
/*            //��������
            if ("Condition".equals(archiveType)){
                Iterator<tableContants> j = contants.iterator();
                while (j.hasNext()) {
               	    tableContants t = j.next();
               	    System.out.println(t.toString());
               	    //t.transContants(dbtrans,tablename);
               	    //System.out.println(t.toString());              	   
      	        }            	
            }*/
            
           /* //Ϊ�˲�����
            Iterator<tableContants> j = contants.iterator();
            while (j.hasNext()) {
           	    tableContants t = j.next();
           	    System.out.println("888"+t.toString());           	   
  	        }*/
            
            //��Ŀ�����ݿ��������
            //db_target.dateArchive(tablename,colNum,cols,contants);
            
            //cFordelete.addAll(contants);
            
        }
        
        //ȫ���鵵���� ��ɾ��ԭʼ���ݿ�����   Ҫ���鵵һ��ɾ��һ�� ��ȡ��¼ʱrowmun�ͱ仯�� �����С�
        //db_source.deletArchivedData(tablename, archiveType, cFordelete);
        
        //List<tableContants> cFordelete = db_source.getAllContants(tablename,archiveType,colNum,dbinst);
        //for(tableContants cd : cFordelete){
        //	  System.out.println("uuuu--"+cd.toString());
        //}
    }    
}  