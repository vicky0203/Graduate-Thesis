package archiveDataAuto;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class DBControl {		
	private String dbUrl = null;
	private String dbUser = null; 
	private String dbPwd = null;
	
	public int test_user_mark = 0; //1��ʾ��½�ɹ� 0��ʾ���ɹ�
	
	public DBControl(){
	    try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
			
	public void setDBInfo(String url,String user, String pwd){ //Ū��where�����ʽ��ʹ���������
		this.dbUrl = url; 
		this.dbUser = user;
		this.dbPwd = pwd;
	}
	
	public String getDBUrl(){ 
		return this.dbUrl;
	}
	
	public String getDBUser(){ 
		return this.dbUser;
	}
	
	public String getDBPwd(){ 
		return this.dbPwd;
	}

	public Connection getConnection() throws Exception{
		 return java.sql.DriverManager.getConnection(dbUrl,dbUser,dbPwd);	     
	}

	public void closeConnection(Connection con){
	    try{
	        if(con!=null) con.close();
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	}

	public void closePrepStmt(PreparedStatement prepStmt){
	    try{
	        if(prepStmt!=null) prepStmt.close();
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	}

	public void closeResultSet(ResultSet rs){
	    try{
	        if(rs!=null) rs.close();
	      }catch(Exception e){
	        e.printStackTrace();
	      }
	}

	public void adduser(String userid, String userpwd) throws Exception{ //���û�ע��
	    Connection con = null;
	    PreparedStatement prepStmt = null;
	    ResultSet rs = null;
	    String statement;
	    con = getConnection(); System.out.println("���ӳɹ�");
        statement = " insert into userinfo (userid,pwd) values(?,?) ";	      
       	prepStmt = con.prepareStatement(statement);   	      
       	prepStmt.setString(1, userid);
       	prepStmt.setString(2, userpwd);
       	prepStmt.executeUpdate();
     
	    closeResultSet(rs); System.out.println("�رս����");
	    closePrepStmt(prepStmt); System.out.println("�ر�׼�����");
	    closeConnection(con);  System.out.println("�ر�����");
    } 
	
	public void checkUser(String userid, String userpwd) throws Exception{ // ����û���������
	    Connection con = null;
	    PreparedStatement prepStmt = null;
	    ResultSet rs = null;
	    con=getConnection(); 
	    String selectStatement = "select * from userinfo where userid = "+ userid;
	    prepStmt = con.prepareStatement(selectStatement);
	    rs = prepStmt.executeQuery();
          
        while(rs.next()){//����1�����Ҳ������rs.next(); 
	       if(rs.getString(2).equals(userpwd)){//�������    
	            	 test_user_mark = 1;
	       }
           //ע�� ���û���ҵ���testid����Ľ�� �� �û��������� ��test_user_mark��Ϊ0
       }
	   closeResultSet(rs);
	   closePrepStmt(prepStmt);
	   closeConnection(con);//System.out.println("checkUser�ر�����");
   }   
	
	public List<String> getTableNames() throws Exception{//������ݿ����б������
		Connection con = getConnection(); 
	    DatabaseMetaData meta = con.getMetaData();
        //    System.out.println("�������û�����" + rs.getString(2));
	    ResultSet rs_all = meta.getTables(null, dbUser.toUpperCase(), "%", new String[] { "TABLE" });
	    List<String> tables = new ArrayList<String>();   
	    while (rs_all.next()) {
	    	if ( !rs_all.getString(3).contains("$") && !rs_all.getString(3).contains("SQLPLUS_")
	    		 && !rs_all.getString(3).contains("LOGMNR") && !rs_all.getString(3).equals("HELP") 
	    		 && !rs_all.getString(3).equals("USERINFO") && !rs_all.getString(3).contains("_TRIG_TL")){
	    	    tables.add(rs_all.getString(3));
	    		//System.out.println("������" + rs_all.getString(3));
	    	}
	    } 	    
	    closeResultSet(rs_all);	    
		closeConnection(con);//System.out.println("getTableNames�ر�����");
		return tables;
	}
	
	public List<String> getColumnNames(String tableName) throws Exception{ //���ָ����������ֶ�����
		Connection con = getConnection(); 
	    DatabaseMetaData meta = con.getMetaData();
        //    System.out.println("�������û�����" + rs.getString(2));
	    ResultSet rs = meta.getColumns(null, dbUser.toUpperCase(), tableName, "%");
	    List<String> columns = new ArrayList<String>();   
	    while (rs.next()) {
	    	    columns.add(rs.getString(4));
	    		//System.out.println("������" + rs.getString(4));	    	    
	    } 	    
	    closeResultSet(rs);	    
		closeConnection(con);//System.out.println("getColumnNames�ر�����");
		return columns;
	}
	
	public List<column> getColumnsInfo(String tableName) throws Exception{//���ָ�����ֶ���Ϣ
		Connection con = getConnection(); 
	    DatabaseMetaData meta = con.getMetaData();
	    ResultSet rs = meta.getColumns(null, dbUser.toUpperCase(), tableName, "%");
	    List<column> columns = new ArrayList<column>();
	    while (rs.next()) {
	    	//COLUMN_NAME�����ֶε����֣�TYPE_NAME�����������ͣ�����"int","int unsigned"�ȵȣ�
	    	//COLUMN_SIZE���������������ֶεĳ��ȣ����綨���int(8)���ֶΣ����ؾ���8��
	    	//NULLABLE������1�ͱ�ʾ������Null,��0�ͱ�ʾNot Null��*/
	    	String columnName = rs.getString("COLUMN_NAME"); 
	    	String columnType = rs.getString("TYPE_NAME"); 
	    	int datasize = rs.getInt("COLUMN_SIZE"); 
	    	int digits = rs.getInt("DECIMAL_DIGITS"); //�е�С��λ��
	    	int nullable = rs.getInt("NULLABLE");
    		//System.out.println("������" + rs.getString(4));
	    	columns.add(new column(columnName,columnType,datasize,digits,nullable));
        } 
	    closeResultSet(rs);	    
		closeConnection(con);//System.out.println("getColumns�ر�����");
		return columns;	    
	}
	
	public int getColumnNumber(String tableName) throws Exception{//���ָ�����ֶθ���
		List<String> columns = getColumnNames(tableName);
		return columns.size();
	}
	
	public List<primaryKey> getPrimaryKeys(String tableName) throws Exception{//���ָ����������Ϣ
		Connection con = getConnection(); 
	    DatabaseMetaData meta = con.getMetaData();
	    String catalog = con.getCatalog();//catalog ��ʵҲ�������ݿ���
	    ResultSet primaryKeyResultSet = meta.getPrimaryKeys(catalog,null,tableName);
	    
	    List<primaryKey> tables = new ArrayList<primaryKey>();
	    while(primaryKeyResultSet.next()){
	        String primaryKeyColumnName = primaryKeyResultSet.getString("COLUMN_NAME");//������
	        int primaryKeyColumnNumber = primaryKeyResultSet.getInt("KEY_SEQ");//�������
	        //System.out.println("��������"+primaryKeyColumnName+", λ�ã�"+primaryKeyColumnNumber);
	        tables.add(new primaryKey(primaryKeyColumnName,primaryKeyColumnNumber));
	    }
	    closeResultSet(primaryKeyResultSet);	    
		closeConnection(con);//System.out.println("getPrimaryKeys�ر�����");
		return tables;
	}
	
	public List<foreignKey> getForeignKeys(String tableName) throws Exception{//���ָ���������Ϣ
		Connection con = getConnection(); 
	    DatabaseMetaData meta = con.getMetaData();
	    String catalog = con.getCatalog();//catalog ��ʵҲ�������ݿ���
	    ResultSet foreignKeyResultSet = meta.getImportedKeys(catalog,null,tableName);
	    List<foreignKey> tables = new ArrayList<foreignKey>();
	    while(foreignKeyResultSet.next()){
	        String fkColumnName = foreignKeyResultSet.getString("FKCOLUMN_NAME");//���������
	        int fkColumnNumber = foreignKeyResultSet.getInt("KEY_SEQ");//������
	        String pkTablenName = foreignKeyResultSet.getString("PKTABLE_NAME");//�����������������
	        String pkColumnName = foreignKeyResultSet.getString("PKCOLUMN_NAME");//�����������������
	        int updateRule = foreignKeyResultSet.getInt("UPDATE_RULE");//��������ʱ��������ı仯
	        int deleteRule = foreignKeyResultSet.getInt("DELETE_RULE");//ɾ������ʱ��������ı仯	        
	        tables.add(new foreignKey(fkColumnName,fkColumnNumber,pkTablenName,pkColumnName,updateRule,deleteRule));
	        /*System.out.println("���������"+fkColumnName+" �ţ�"+fkColumnNumber+" ����������"+pkTablenName+
	        		             " ����������: "+pkColumnName+" ��������ʱ��������ı仯: "+updateRule+
	        		             " ɾ������ʱ��������ı仯: "+deleteRule);*/
	    }
	    closeResultSet(foreignKeyResultSet);	    
		closeConnection(con);//System.out.println("getForeignKeys�ر�����");
		return tables;
	}
	
	public List<tableContants> getColumnContants(String tableName, String archiveTpye, int columnNumber,
           int firstIndex,int everyPageNum,int recordNum,List<DBInstruct> dbinst) throws Exception{//��ø��ֶ����� Ҳ����ÿ����¼ 
		//���ݹ鵵�����жϴ���ȡ���ݹ鵵
		String table = tableName;
		if("General".equals(archiveTpye)){
			table += "_TRIG_TL";
        }
		//System.out.println("222table:   "+table);
		List<String> columnsName = getColumnNames(tableName);
        int secondIndex = firstIndex+everyPageNum-1;
        if (secondIndex > recordNum){
            secondIndex = recordNum;
        }
        //���SELECT SQL���         
        String selectStatement = "";
        if(("Condition".equals(archiveTpye) && dbinst.isEmpty()) || "General".equals(archiveTpye)){
        	//û�й鵵����  �����ڽ�ԭʼ���ݿ��е���Ϣȫ���鵵  ���� ��Դ���ݿ��б��ݱ��е�����ȫ���鵵
        	//System.out.println("3333");
        	selectStatement = "select * from (select "+table+".*, " +
                              "rownum as rn from "+table+" where rownum<="+secondIndex+") " +
                              "where rn>="+firstIndex;
        }else if( "Condition".equals(archiveTpye) && !dbinst.isEmpty()) {//���tableName��ͬ�Ĺ鵵���� 
        	//System.out.println("444");
        	String temp = "select * from "+table;     //"select * from EMPLOYEES where LOT<='27' and SSN<='111102098' "
        	Iterator<DBInstruct> iter_dbi = dbinst.iterator();
        	int c = 0;
        	while(iter_dbi.hasNext()){
        		DBInstruct dbi = iter_dbi.next();
        	    if(dbi.getTablename().equals(table)){
        	    	if (c==0){
        	    		temp += " where ";
           		    }
        	    	c++;
       	        	if(c>=2){
       	        		temp += " and ";
       	        	}
        	    	temp += dbi.getColname()+dbi.getCondition()+"'"+dbi.getValue()+"'";
        	    }
            }       	
        	//��ѯһ��Ҫ���� �Ƿ��������� ����ÿ�β��ҳ��ķֿ鲻һ�� �����ظ�����
            for(DBInstruct d : dbinst){
            	if(d.getTablename().equals(table)){
            		temp += " order by "+ d.getColname();
            		break;
            	}
            }
        	//System.out.println("Ttemp: "+temp);
        	//"select * from (select A.*, rownum as rn from ("+sql3+") A where rownum<=3) where rn>=1";
        	selectStatement = "select * from (select A.*, rownum as rn from " +
	                          "("+temp+") A where rownum<="+secondIndex+")" +
                              "where rn>="+firstIndex;
        }
        //System.out.println(selectStatement);
        
        //��ʼȡ��¼
        Connection con = getConnection();
        PreparedStatement prepStmt = con.prepareStatement(selectStatement);
        ResultSet rs = prepStmt.executeQuery();//RS������ó��ö��¼ ÿһ����¼����һ��tableContants
        List<tableContants> contants = new ArrayList<tableContants>();
        while (rs.next()) {
             tableContants tc = new tableContants(rs,columnNumber,columnsName); //ע�����ڵ�RSָ���ǵ�ǰָ��Ľ�����е�һ����� 
                                                                    //������ʹ��rs.next()����� û��rs.next()
             contants.add(tc);
        }
        closeResultSet(rs);
        closePrepStmt(prepStmt);
        closeConnection(con);  //System.out.println("getColumnContants�ر�����");
        return contants;  //��������Ҫ��һ��DBControlʵ��  ����������ȡ��tablecontans Ȼ��main����ѭ����������
    }	
	
	public List<tableContants> getAllContants(String tableName, String archiveTpye, int columnNumber,
	            List<DBInstruct> dbinst) throws Exception{//��ø��ֶ����� Ҳ����ÿ����¼ 
			//���ݹ鵵�����жϴ���ȡ���ݹ鵵
			String table = tableName;
			if("General".equals(archiveTpye)){
				table += "_TRIG_TL";
	        }
			List<String> columnsName = getColumnNames(tableName);
	        //���SELECT SQL���         
	        String selectStatement = "";
	        if(("Condition".equals(archiveTpye) && dbinst.isEmpty()) || "General".equals(archiveTpye)){
	        	//û�й鵵����  �����ڽ�ԭʼ���ݿ��е���Ϣȫ���鵵  ���� ��Դ���ݿ��б��ݱ��е�����ȫ���鵵
	        	selectStatement = "select * from "+table;
	        }else if( "Condition".equals(archiveTpye) && !dbinst.isEmpty()) {//���tableName��ͬ�Ĺ鵵���� 
	        	selectStatement = "select * from "+table;     //"select * from EMPLOYEES where LOT<='27' and SSN<='111102098' "
	        	Iterator<DBInstruct> iter_dbi = dbinst.iterator();
	        	int c = 0;
	        	while(iter_dbi.hasNext()){
	        		DBInstruct dbi = iter_dbi.next();
	        	    if(dbi.getTablename().equals(table)){
	        	    	if (c==0){
	        	    		selectStatement += " where ";
	           		    }
	        	    	c++;
	       	        	if(c>=2){
	       	        		selectStatement += " and ";
	       	        	}
	       	        	selectStatement += dbi.getColname()+dbi.getCondition()+"'"+dbi.getValue()+"'";
	        	    }
	            }       	
	        	//��ѯһ��Ҫ���� �Ƿ��������� ����ÿ�β��ҳ��ķֿ鲻һ�� �����ظ�����
	            for(DBInstruct d : dbinst){
	            	if(d.getTablename().equals(table)){
	            		selectStatement += " order by "+ d.getColname();
	            		break;
	            	}
	            }
	        }
	        //System.out.println("selectStatement: "+selectStatement);
	        
	        //��ʼȡ��¼
	        Connection con = getConnection();
	        PreparedStatement prepStmt = con.prepareStatement(selectStatement);
	        ResultSet rs = prepStmt.executeQuery();//RS������ó��ö��¼ ÿһ����¼����һ��tableContants
	        List<tableContants> contants = new ArrayList<tableContants>();
	        while (rs.next()) {
	             tableContants tc = new tableContants(rs,columnNumber,columnsName); //ע�����ڵ�RSָ���ǵ�ǰָ��Ľ�����е�һ����� 
	                                                                    //������ʹ��rs.next()����� û��rs.next()
	             contants.add(tc);
	             
	        }
	        closeResultSet(rs);
	        closePrepStmt(prepStmt);
	        closeConnection(con); 
	        return contants; 
	}		
	
	public int getRecordsNumber(String tablename, String archiveTpye, List<DBInstruct> dbinst) throws Exception{//���ָ�����¼��
		int totalCount = 0;
		Connection con = getConnection(); 
		//���SELECT SQL���         
        String selectStatement = "select count(*) from "+tablename;
        if("General".equals(archiveTpye)){
        	selectStatement += "_TRIG_TL";
        }
        if("Condition".equals(archiveTpye) && !dbinst.isEmpty()){//ֱ�Ӵ�ԭ���ݱ�鵵ʱ ����XML���tableName��ͬ�Ĺ鵵����
          // System.out.println("11111");
           Iterator<DBInstruct> iter_dbi = dbinst.iterator();
           int c = 0;
       	   while(iter_dbi.hasNext()){
       		    DBInstruct dbi = iter_dbi.next();
       	        if(dbi.getTablename().equals(tablename)){
       	            //"select count(*) from EMPLOYEES where LOT<='27' and SSN<='111102098' ";
       	        	if (c==0){
           		    	selectStatement += " where ";
           		    }
       	        	c++;
       	        	if(c>=2){
       	        		selectStatement += " and ";
       	        	}
       	        	selectStatement += dbi.getColname()+dbi.getCondition()+"'"+dbi.getValue()+"'";
       	       }
           }      	
        }
        
        //System.out.println("Count_SQL:"+selectStatement);
		PreparedStatement prepStmt = con.prepareStatement(selectStatement);
	    ResultSet rs = prepStmt.executeQuery();//RS������ó��ö��¼ ÿһ����¼����һ��tableContants
	    if (rs.next()){
	    	totalCount=rs.getInt(1);
	    }
	    closeResultSet(rs);
	    closePrepStmt(prepStmt);
		closeConnection(con);  //System.out.println("getRecordsNumber�ر�����");
        return totalCount;
	}
	
 	public int getNumberOfAllRecord(String tableName) throws Exception{//���ָ�����¼��
		int totalCount = 0;
		Connection con = getConnection(); 
		String sql = "select count(*) from "+tableName;
		PreparedStatement prepStmt = con.prepareStatement(sql);
	    ResultSet rs = prepStmt.executeQuery();//RS������ó��ö��¼ ÿһ����¼����һ��tableContants
	    if (rs.next()){
	    	totalCount=rs.getInt(1);
	    }
	    closeResultSet(rs);
	    closePrepStmt(prepStmt);
		closeConnection(con);  //System.out.println("getNumberOfAllRecord�ر�����");
        return totalCount;
	} 
	
/*	public String getTableSQL(String tableName) throws Exception{//���ָ����Ĵ������   ����������� ò������ò��� 
		List<primaryKey> primaryKeys = getPrimaryKeys(tableName);
		List<foreignKey> foreignKeys = getForeignKeys(tableName);
		List<column> columns = getColumnsInfo(tableName);
		
		String sql = "CREATE TABLE "+tableName+" ("; 	
		//�ֶ���Ϣ
		Iterator<column> col_itr = columns.iterator();
		while (col_itr.hasNext()) {
			column col = col_itr.next();
			sql += col.getColName()+" "+ col.getTypeName();  //�е�������������Ͳ��� size ����DATA
			if (col.getTypeName().contains("CHAR") || col.getTypeName()=="NUMBER" || col.getTypeName()=="RAW"){
				sql += "("+col.getColSize()+")";
			}
			if (col.getNullAble() == 0){
				sql += " not null";
			}
			if(!col_itr.hasNext()){
				break;  
			}else sql += ",";
		} 
		//System.out.println(tableName+"��ֹ���ֶ�:"+sql);
		
		//�������Ϣ
		String action[] = {"cascade", "restrict", "set null", "no action", "set default"};
		if(!primaryKeys.isEmpty()){//����ֻ�������ı�
			sql += ",PRIMARY KEY(";
			Iterator<primaryKey> pk_itr = primaryKeys.iterator();
			while (pk_itr.hasNext()) {
				primaryKey pk = pk_itr.next();
				sql += pk.getPKName();
				if(!pk_itr.hasNext()){
					sql += ")";
					break; 
				}else  sql += ",";
			}
            //System.out.println(tableName+"���������:"+sql);
			if (!foreignKeys.isEmpty()){ //��������������������ı�
				sql += ",";
				Iterator<foreignKey> fk_itr = foreignKeys.iterator();
				while (fk_itr.hasNext()) {
					foreignKey fk = fk_itr.next();
					sql += "FOREIGN KEY ("+fk.getFKName()+") REFERENCES "+fk.getPKTablenName()+"("+fk.getPKColumnName()+") ";
					//System.out.println("FK on update"+fk.getUpdateRule());
					if (fk.getUpdateRule()!= 0){
						sql += "on update "+ action[fk.getUpdateRule()]+" ";
					}					
					//System.out.println("FK on delete"+fk.getUpdateRule());
					if (fk.getDeleteRule()!= 1){
					    sql += "on delete "+ action[fk.getDeleteRule()]; 	
					}
					if(!fk_itr.hasNext()){
						break;
					}else sql += ",";
				}
	            //System.out.println(tableName+"��������:"+sql);
			}
		}else if (primaryKeys.isEmpty() && !foreignKeys.isEmpty()){ //����ֻ������ı� һ�㲻����
			sql += ",";
			Iterator<foreignKey> fk_itr = foreignKeys.iterator();
			while (fk_itr.hasNext()) {
				foreignKey fk = fk_itr.next();
				sql += "FOREIGN KEY ("+fk.getFKName()+") REFERENCES "+fk.getPKTablenName()+"("+fk.getPKColumnName()+") ";
				if (fk.getUpdateRule()!= 0){
					sql += "on update "+ action[fk.getUpdateRule()]+" ";
				}
				//System.out.println("FK on delete"+fk.getUpdateRule());
				if (fk.getDeleteRule()!= 1){
				    sql += "on delete "+ action[fk.getDeleteRule()]; 	
				}				    
				if(!fk_itr.hasNext()){
					break;
				}else sql += ",";
			}
            System.out.println(tableName+"ֻ�������:"+sql);
		}		
		//��� 
		sql += ")";
		return sql;		
	}*/
	
	public String getTableSQL(String tableName,String tableType) throws Exception{//���ָ����Ĵ������
	    List<primaryKey> primaryKeys = getPrimaryKeys(tableName);
	    List<column> columns = getColumnsInfo(tableName);
	    
	    String NewTableName = tableName;
	    if("sourceForTrigger".equals(tableType)){//Ϊ�˴����������ı�
	    	NewTableName += "_TRIG_TL";
	    }
	    String sql = "CREATE TABLE "+NewTableName+" ("; 	
	    //�ֶ���Ϣ
	    Iterator<column> col_itr = columns.iterator();
	    while (col_itr.hasNext()) {
		     column col = col_itr.next();
		     sql += col.getColName()+" "+ col.getTypeName();  //�е�������������Ͳ��� size ����DATA
		     if (col.getTypeName().contains("CHAR") || col.getTypeName()=="NUMBER" || col.getTypeName()=="RAW"){
			    sql += "("+col.getColSize()+")";
		     }
		     if (col.getNullAble() == 0){
			    sql += " not null";
		     }
		     if(!col_itr.hasNext()){
			     break;  
		     }else sql += ",";
	    } 
	    //System.out.println(tableName+"��ֹ���ֶ�:"+sql);
	
	    //������Ϣ
	    String action[] = {"cascade", "restrict", "set null", "no action", "set default"};
	    if(!primaryKeys.isEmpty()){//����ֻ�������ı� ����������ò��� ���ݱ��п��ܲ���ͬ���ģ��� ֻҪԭʼ���ݿ��� �������ݲ������
		   sql += ",PRIMARY KEY(";
		   Iterator<primaryKey> pk_itr = primaryKeys.iterator();
		   while (pk_itr.hasNext()) {
			   primaryKey pk = pk_itr.next();
			   sql += pk.getPKName();
			   if(!pk_itr.hasNext()){
				   sql += ")";
				   break; 
			   }else  sql += ",";
		   }
           //System.out.println(tableName+"���������:"+sql);
	  }
	  //��� 
	  sql += ")";
	  return sql;		
   }
	
	public void creatTablebySQL(String sql) throws Exception{//���ݴ�����SQL�����Ŀ�����ݿ��д�����Ӧ��
		Connection con = getConnection(); 
		PreparedStatement prepStmt = con.prepareStatement(sql);
	    prepStmt.executeUpdate();
	    closePrepStmt(prepStmt);
		closeConnection(con);  System.out.println("creatTablebySQL�ر�����");
	}
	
	public void dateArchive(String tablename, int colNum,List<column> cols,List<tableContants> contants) throws Exception{ //��Ŀ���ݿ��������
         Iterator<column> cols_iter = cols.iterator();
        
         //��������SQL���  ͬʱ�ҵ��ֶ�����ΪDATE���ֶ� ������λ��
         //"insert into relicInfo (relicID,relicName, pictureID,outTime, outSite,dynasty, info) values(?,?,?,?,?,?,?)
         int[] col_date = new int[colNum+1]; //�����ֶ�����ΪDATE���ֶ�λ��  �ֶ�ΪDATE ��Ӧλ������ֵ==1
         int count = 1;
         String sql = "insert into "+tablename+" (";
         
         while (cols_iter.hasNext()) {
        	 //�����ֶ�����ΪDATE���ֶ�λ�� ��1��ʼ
        	 column col_temp = cols_iter.next();
        	 if("DATE".equals(col_temp.getTypeName())){        		  
	              col_date[count] = 1;
             }
        	 count++;
        	 
        	 //����SQL���
             sql += col_temp.getColName();
             if(!cols_iter.hasNext()){
	              sql += ") values (";
	              break; 
             }else  sql += ",";
         }
         for (int i=0;i<colNum-1;i++){
             sql += "?," ;
         }
         sql += "?)";
         //System.out.println("��������SQL��䣺"+sql);

         //ִ�����ݹ鵵 
         Connection con = getConnection();
         Iterator<tableContants> iter_tc = contants.iterator();
         while (iter_tc.hasNext()) { 	    	     
              tableContants tc = iter_tc.next();//��ȡһ����¼    	     
              //ִ��SQL����				
	          PreparedStatement prepStmt = con.prepareStatement(sql);
              for(int y=1;y<=colNum;y++){
            	   if (col_date[y]==1){
            		   ///"insert into test_date values(to_date('2002-10-22 00:00:00.0','yyyy-mm-dd hh24:mi:ss'));";
            		   String date = tc.getContantbyIndex(y).trim().substring(0, tc.getContantbyIndex(y).length()-2);
            		   //String tempsql = "TO_DATE('"+ date + "','yyyy-mm-dd hh24:mi:ss')"; 
            		   //System.out.println(tempsql);
            		   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            		   Date dateTime = sdf.parse(date);
            		   Timestamp ts = new Timestamp(dateTime.getTime());
            		   TimeZone tz = TimeZone.getTimeZone("gmt"); 
            		   Calendar calendar = Calendar.getInstance(tz);
            		    
            		   prepStmt.setTimestamp(y,ts,calendar);              		   
            	   }else {
            		   prepStmt.setString(y,tc.getContantbyIndex(y).trim());
            	   }
              }
              prepStmt.executeUpdate();
              closePrepStmt(prepStmt);		        
        } 	
        closeConnection(con);  //System.out.println("dateArchive�ر�����");     		    
    }
	
	public boolean isTableExisted(String tablename){//���Ա��Ƿ��Ѿ�����
		boolean result = false;		
		try {
			List<String> tables = getTableNames();
			Iterator<String> i = tables.iterator();
	        while (i.hasNext()) {
		        if(i.next().equals(tablename)){
		    	    result = true;
		    	    break;
		        }
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return result;
	}
	
	public  String[] getArchieveSequence(String chooseboxstr[]){//����ѡ�еı��� ������������ϵ��ñ�鵵��˳��
		int sqlen = chooseboxstr.length;
		List<String> resultPK = new ArrayList<String>();
		List<String> resultFK = new ArrayList<String>();
		for (int i = 0; i<sqlen;i++){
			String tablename = chooseboxstr[i];
			try {
				if (getForeignKeys(tablename).size() == 0){//��������ı�
					resultPK.add(tablename);
				}else{
					resultFK.add(tablename);//��������ı�
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		resultPK.addAll(resultFK);//���������� ����ı����ϲ�
		String[] result = new String[resultPK.size()];
		result = resultPK.toArray(result);
		return result;
	}
		
	public void creatTrigger (String tablename, int colNum, List<column> cols) throws Exception{
		Connection con = getConnection();
		String triggerName = tablename+"_TRIG";
		String trigTableName = tablename+"_TRIG_TL";
		//�ж��Ƿ񴥷����Ѵ���
        String test = "select * from user_triggers where trigger_name='"+triggerName+"'";
		PreparedStatement prepStmt1 = con.prepareStatement(test);
		int ifexist = prepStmt1.executeUpdate();
		closePrepStmt(prepStmt1);
		if(ifexist==0){//�������ٴ���
		   String trigger_sql = "create trigger "+triggerName+"\n" +    //create or replace 
				                "  after insert or update on "+tablename+" for each row\n" +
				                "begin\n" +
				                "  case\n" +
				                "     when inserting then\n" +
				                "          insert into "+trigTableName+"(" ;
		                      /*"          insert into "+trigTableName+"(SSN, NAME, LOT) values(:new.SSN,:new.NAME,:new.LOT);\n" +
		                        "     when updating then\n" +
		                        "          update "+trigTableName+" set NAME=:new.NAME, LOT=:new.LOT where SSN=:old.SSN;\n" +
		                        "  end case;\n" +
		                         "end;";*/
			  //����COLNAME�齨SQL���
		      int count = 1;
		      for(column c : cols){
		    	  trigger_sql+= c.getColName();
		    	  if(count<colNum){
					  trigger_sql+=", ";
				  }else if(count==colNum){
					  trigger_sql+=") ";
				  }
				  count++;
		      }//"insert into "+trigTableName+"(SSN, NAME, LOT) 
		      trigger_sql+="values(";
		      count = 1;
		      for(column c : cols){
		    	  trigger_sql+= ":new."+c.getColName();
		    	  if(count<colNum){
					  trigger_sql+=", ";
				  }else if(count==colNum){
					  trigger_sql+=");\n";
				  }
				  count++;
		      }//"insert into "+trigTableName+"(SSN, NAME, LOT) values(:new.SSN,:new.NAME,:new.LOT);\n"
		      trigger_sql+="     when updating then\n" +
				           "          update "+trigTableName+" set ";
		      List<primaryKey> primaryKeys = getPrimaryKeys(tablename);
		      count = 1;
		      for(column c : cols){
		    	  boolean ptest = false;
		    	  for (primaryKey p: primaryKeys){
		    		  if(p.getPKName().equals(c.getColName())){
		    			  ptest = true;
		    			  break;
		    		  }
		    	  }
		    	  if(!ptest){
		    		  trigger_sql+= c.getColName()+"=:new."+c.getColName();
		    		  if(count<colNum){
						  trigger_sql+=", ";
					  }else if(count==colNum){
						  trigger_sql+=" ";
					  }
		    	  }
				  count++;
		      }//"          update "+trigTableName+" set NAME=:new.NAME, LOT=:new.LOT 
		      trigger_sql+= "where ";
		      count = 1;
		      for (primaryKey p: primaryKeys){
		    	  trigger_sql+= p.getPKName()+"=:old."+p.getPKName();
		    	  if(count<=primaryKeys.size()){
					  trigger_sql+=";\n";
					  break;
				  }
				  count++;
		      }		      		      
		      //"          update "+trigTableName+" set NAME=:new.NAME, LOT=:new.LOT where SSN=:old.SSN;\n" 
		      trigger_sql+= "  end case;\n" + "end;";
		      //����������
		      System.out.println(trigger_sql);
		      Statement stmt = con.createStatement();
		      stmt.executeUpdate(trigger_sql);	
		      stmt.close();
		}		
		closeConnection(con);  
		System.out.println("creatTrigger�ر�����");
	}
	
	public void deletArchivedData(String tablename, String archiveType, List<tableContants> contants) throws Exception{ //��Ŀ���ݿ��������
		//����ɾ��SQL���  
        //"delete from relicInfo where relicID = ?  
        String delete_sql = "delete from "+tablename;
        if("General".equals(archiveType)){
        	delete_sql += "_TRIG_TL";
        }
        delete_sql += " where ";
        String pk1 = "";
        List<primaryKey> primaryKeys = getPrimaryKeys(tablename);
        for(primaryKey p:primaryKeys){
        	pk1 = p.getPKName();
        	break;
        }        
        delete_sql += pk1 + " = ?";
        //System.out.println(delete_sql);
        //ִ�й鵵����ɾ��
        Connection con = getConnection();
        con.setAutoCommit(false);
        PreparedStatement ps = con.prepareStatement(delete_sql);
        for(tableContants tc : contants){
        	ps.setString(1,tc.getContantbyColumnName(pk1).trim());  
        	//����Ҫ�Ǽ���TRIM()  tc.getContantbyColumnName(pk1).trim() ȥ���հ� ɾ��ʱ�޷������ݿ�ƥ�� ��ΪԴ���ݿⴥ����������пհ� ���о�
        	ps.addBatch(); 
        }
        ps.executeBatch();
        con.commit();  
        closePrepStmt(ps);
        closeConnection(con);      
	}     
	
	public List<tableContants> getSelectRecord(String tableName, String cols[],String cons[]) throws Exception{
		//��ȡ��ѯ���         
        String select = "select * from "+tableName+" where ";
        List<String> columnsName = getColumnNames(tableName);
        int i = 0, m = 0;
        for(String n: columnsName){
        	if(!cols[i].isEmpty() && !cons[i].isEmpty() && !cons[i].equals("0") ){
        		m++;
        		if(m>=2){
	        		select += " and ";
	        	}
        		select += n+cons[i]+cols[i];
        	}	        	
        	i++;
        }
        //System.out.println(select);
		
		//��ʼȡ��¼
        Connection con = getConnection();
        PreparedStatement prepStmt = con.prepareStatement(select);
        ResultSet rs = prepStmt.executeQuery();//RS������ó��ö��¼ ÿһ����¼����һ��tableContants
        List<tableContants> contants = new ArrayList<tableContants>();
        while (rs.next()) {
             tableContants tc = new tableContants(rs,columnsName.size(),columnsName); //ע�����ڵ�RSָ���ǵ�ǰָ��Ľ�����е�һ����� 
                                                                    //������ʹ��rs.next()����� û��rs.next()
             contants.add(tc);             
        }
        closeResultSet(rs);
        closePrepStmt(prepStmt);
        closeConnection(con); 
        return contants; 
	}
}