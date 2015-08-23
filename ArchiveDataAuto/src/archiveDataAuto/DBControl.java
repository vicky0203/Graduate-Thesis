package archiveDataAuto;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class DBControl {		
	private String dbUrl = null;
	private String dbUser = null; 
	private String dbPwd = null;
	
	public int test_user_mark = 0; //1表示登陆成功 0表示不成功
	
	public DBControl(){
	    try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
			
	public void setDBInfo(String url,String user, String pwd){ //弄清where语句形式再使用这个函数
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

	public void adduser(String userid, String userpwd) throws Exception{ //新用户注册
	    Connection con = null;
	    PreparedStatement prepStmt = null;
	    ResultSet rs = null;
	    String statement;
	    con = getConnection(); System.out.println("连接成功");
        statement = " insert into userinfo (userid,pwd) values(?,?) ";	      
       	prepStmt = con.prepareStatement(statement);   	      
       	prepStmt.setString(1, userid);
       	prepStmt.setString(2, userpwd);
       	prepStmt.executeUpdate();
     
	    closeResultSet(rs); System.out.println("关闭结果集");
	    closePrepStmt(prepStmt); System.out.println("关闭准备语句");
	    closeConnection(con);  System.out.println("关闭连接");
    } 
	
	public void checkUser(String userid, String userpwd) throws Exception{ // 检查用户名和密码
	    Connection con = null;
	    PreparedStatement prepStmt = null;
	    ResultSet rs = null;
	    con=getConnection(); 
	    String selectStatement = "select * from userinfo where userid = "+ userid;
	    prepStmt = con.prepareStatement(selectStatement);
	    rs = prepStmt.executeQuery();
          
        while(rs.next()){//就是1条结果也必须用rs.next(); 
	       if(rs.getString(2).equals(userpwd)){//密码错误    
	            	 test_user_mark = 1;
	       }
           //注意 如果没有找到与testid相符的结果 即 用户名不存在 则test_user_mark仍为0
       }
	   closeResultSet(rs);
	   closePrepStmt(prepStmt);
	   closeConnection(con);//System.out.println("checkUser关闭连接");
   }   
	
	public List<String> getTableNames() throws Exception{//获得数据库所有表的名字
		Connection con = getConnection(); 
	    DatabaseMetaData meta = con.getMetaData();
        //    System.out.println("表所属用户名：" + rs.getString(2));
	    ResultSet rs_all = meta.getTables(null, dbUser.toUpperCase(), "%", new String[] { "TABLE" });
	    List<String> tables = new ArrayList<String>();   
	    while (rs_all.next()) {
	    	if ( !rs_all.getString(3).contains("$") && !rs_all.getString(3).contains("SQLPLUS_")
	    		 && !rs_all.getString(3).contains("LOGMNR") && !rs_all.getString(3).equals("HELP") 
	    		 && !rs_all.getString(3).equals("USERINFO") && !rs_all.getString(3).contains("_TRIG_TL")){
	    	    tables.add(rs_all.getString(3));
	    		//System.out.println("表名：" + rs_all.getString(3));
	    	}
	    } 	    
	    closeResultSet(rs_all);	    
		closeConnection(con);//System.out.println("getTableNames关闭连接");
		return tables;
	}
	
	public List<String> getColumnNames(String tableName) throws Exception{ //获得指定表的所有字段名字
		Connection con = getConnection(); 
	    DatabaseMetaData meta = con.getMetaData();
        //    System.out.println("表所属用户名：" + rs.getString(2));
	    ResultSet rs = meta.getColumns(null, dbUser.toUpperCase(), tableName, "%");
	    List<String> columns = new ArrayList<String>();   
	    while (rs.next()) {
	    	    columns.add(rs.getString(4));
	    		//System.out.println("列名：" + rs.getString(4));	    	    
	    } 	    
	    closeResultSet(rs);	    
		closeConnection(con);//System.out.println("getColumnNames关闭连接");
		return columns;
	}
	
	public List<column> getColumnsInfo(String tableName) throws Exception{//获得指定表字段信息
		Connection con = getConnection(); 
	    DatabaseMetaData meta = con.getMetaData();
	    ResultSet rs = meta.getColumns(null, dbUser.toUpperCase(), tableName, "%");
	    List<column> columns = new ArrayList<column>();
	    while (rs.next()) {
	    	//COLUMN_NAME就是字段的名字，TYPE_NAME就是数据类型，比如"int","int unsigned"等等，
	    	//COLUMN_SIZE返回整数，就是字段的长度，比如定义的int(8)的字段，返回就是8，
	    	//NULLABLE，返回1就表示可以是Null,而0就表示Not Null。*/
	    	String columnName = rs.getString("COLUMN_NAME"); 
	    	String columnType = rs.getString("TYPE_NAME"); 
	    	int datasize = rs.getInt("COLUMN_SIZE"); 
	    	int digits = rs.getInt("DECIMAL_DIGITS"); //列的小数位数
	    	int nullable = rs.getInt("NULLABLE");
    		//System.out.println("列名：" + rs.getString(4));
	    	columns.add(new column(columnName,columnType,datasize,digits,nullable));
        } 
	    closeResultSet(rs);	    
		closeConnection(con);//System.out.println("getColumns关闭连接");
		return columns;	    
	}
	
	public int getColumnNumber(String tableName) throws Exception{//获得指定表字段个数
		List<String> columns = getColumnNames(tableName);
		return columns.size();
	}
	
	public List<primaryKey> getPrimaryKeys(String tableName) throws Exception{//获得指定表主键信息
		Connection con = getConnection(); 
	    DatabaseMetaData meta = con.getMetaData();
	    String catalog = con.getCatalog();//catalog 其实也就是数据库名
	    ResultSet primaryKeyResultSet = meta.getPrimaryKeys(catalog,null,tableName);
	    
	    List<primaryKey> tables = new ArrayList<primaryKey>();
	    while(primaryKeyResultSet.next()){
	        String primaryKeyColumnName = primaryKeyResultSet.getString("COLUMN_NAME");//主键名
	        int primaryKeyColumnNumber = primaryKeyResultSet.getInt("KEY_SEQ");//主键序号
	        //System.out.println("主键名："+primaryKeyColumnName+", 位置："+primaryKeyColumnNumber);
	        tables.add(new primaryKey(primaryKeyColumnName,primaryKeyColumnNumber));
	    }
	    closeResultSet(primaryKeyResultSet);	    
		closeConnection(con);//System.out.println("getPrimaryKeys关闭连接");
		return tables;
	}
	
	public List<foreignKey> getForeignKeys(String tableName) throws Exception{//获得指定表外键信息
		Connection con = getConnection(); 
	    DatabaseMetaData meta = con.getMetaData();
	    String catalog = con.getCatalog();//catalog 其实也就是数据库名
	    ResultSet foreignKeyResultSet = meta.getImportedKeys(catalog,null,tableName);
	    List<foreignKey> tables = new ArrayList<foreignKey>();
	    while(foreignKeyResultSet.next()){
	        String fkColumnName = foreignKeyResultSet.getString("FKCOLUMN_NAME");//外键列名称
	        int fkColumnNumber = foreignKeyResultSet.getInt("KEY_SEQ");//外键序号
	        String pkTablenName = foreignKeyResultSet.getString("PKTABLE_NAME");//被导入的主键表名称
	        String pkColumnName = foreignKeyResultSet.getString("PKCOLUMN_NAME");//被导入的主键列名称
	        int updateRule = foreignKeyResultSet.getInt("UPDATE_RULE");//更新主键时外键发生的变化
	        int deleteRule = foreignKeyResultSet.getInt("DELETE_RULE");//删除主键时外键发生的变化	        
	        tables.add(new foreignKey(fkColumnName,fkColumnNumber,pkTablenName,pkColumnName,updateRule,deleteRule));
	        /*System.out.println("外键列名："+fkColumnName+" 号："+fkColumnNumber+" 主键表名："+pkTablenName+
	        		             " 主键列名称: "+pkColumnName+" 更新主键时外键发生的变化: "+updateRule+
	        		             " 删除主键时外键发生的变化: "+deleteRule);*/
	    }
	    closeResultSet(foreignKeyResultSet);	    
		closeConnection(con);//System.out.println("getForeignKeys关闭连接");
		return tables;
	}
	
	public List<tableContants> getColumnContants(String tableName, String archiveTpye, int columnNumber,
           int firstIndex,int everyPageNum,int recordNum,List<DBInstruct> dbinst) throws Exception{//获得各字段内容 也就是每个记录 
		//根据归档类型判断从哪取数据归档
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
        //获得SELECT SQL语句         
        String selectStatement = "";
        if(("Condition".equals(archiveTpye) && dbinst.isEmpty()) || "General".equals(archiveTpye)){
        	//没有归档条件  适用于将原始数据库中的信息全部归档  或者 将源数据库中备份表中的数据全部归档
        	//System.out.println("3333");
        	selectStatement = "select * from (select "+table+".*, " +
                              "rownum as rn from "+table+" where rownum<="+secondIndex+") " +
                              "where rn>="+firstIndex;
        }else if( "Condition".equals(archiveTpye) && !dbinst.isEmpty()) {//获得tableName相同的归档条件 
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
        	//查询一定要排序 是否按主键待测 否则每次查找出的分块不一样 会有重复数据
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
        
        //开始取记录
        Connection con = getConnection();
        PreparedStatement prepStmt = con.prepareStatement(selectStatement);
        ResultSet rs = prepStmt.executeQuery();//RS结果集得出好多记录 每一个记录就是一个tableContants
        List<tableContants> contants = new ArrayList<tableContants>();
        while (rs.next()) {
             tableContants tc = new tableContants(rs,columnNumber,columnsName); //注意现在的RS指的是当前指向的结果集中的一个结果 
                                                                    //所以再使用rs.next()会出错 没有rs.next()
             contants.add(tc);
        }
        closeResultSet(rs);
        closePrepStmt(prepStmt);
        closeConnection(con);  //System.out.println("getColumnContants关闭连接");
        return contants;  //插入数据要另一个DBControl实例  所以这里先取出tablecontans 然后main里再循环插入数据
    }	
	
	public List<tableContants> getAllContants(String tableName, String archiveTpye, int columnNumber,
	            List<DBInstruct> dbinst) throws Exception{//获得各字段内容 也就是每个记录 
			//根据归档类型判断从哪取数据归档
			String table = tableName;
			if("General".equals(archiveTpye)){
				table += "_TRIG_TL";
	        }
			List<String> columnsName = getColumnNames(tableName);
	        //获得SELECT SQL语句         
	        String selectStatement = "";
	        if(("Condition".equals(archiveTpye) && dbinst.isEmpty()) || "General".equals(archiveTpye)){
	        	//没有归档条件  适用于将原始数据库中的信息全部归档  或者 将源数据库中备份表中的数据全部归档
	        	selectStatement = "select * from "+table;
	        }else if( "Condition".equals(archiveTpye) && !dbinst.isEmpty()) {//获得tableName相同的归档条件 
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
	        	//查询一定要排序 是否按主键待测 否则每次查找出的分块不一样 会有重复数据
	            for(DBInstruct d : dbinst){
	            	if(d.getTablename().equals(table)){
	            		selectStatement += " order by "+ d.getColname();
	            		break;
	            	}
	            }
	        }
	        //System.out.println("selectStatement: "+selectStatement);
	        
	        //开始取记录
	        Connection con = getConnection();
	        PreparedStatement prepStmt = con.prepareStatement(selectStatement);
	        ResultSet rs = prepStmt.executeQuery();//RS结果集得出好多记录 每一个记录就是一个tableContants
	        List<tableContants> contants = new ArrayList<tableContants>();
	        while (rs.next()) {
	             tableContants tc = new tableContants(rs,columnNumber,columnsName); //注意现在的RS指的是当前指向的结果集中的一个结果 
	                                                                    //所以再使用rs.next()会出错 没有rs.next()
	             contants.add(tc);
	             
	        }
	        closeResultSet(rs);
	        closePrepStmt(prepStmt);
	        closeConnection(con); 
	        return contants; 
	}		
	
	public int getRecordsNumber(String tablename, String archiveTpye, List<DBInstruct> dbinst) throws Exception{//获得指定表记录数
		int totalCount = 0;
		Connection con = getConnection(); 
		//获得SELECT SQL语句         
        String selectStatement = "select count(*) from "+tablename;
        if("General".equals(archiveTpye)){
        	selectStatement += "_TRIG_TL";
        }
        if("Condition".equals(archiveTpye) && !dbinst.isEmpty()){//直接从原数据表归档时 根据XML获得tableName相同的归档条件
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
	    ResultSet rs = prepStmt.executeQuery();//RS结果集得出好多记录 每一个记录就是一个tableContants
	    if (rs.next()){
	    	totalCount=rs.getInt(1);
	    }
	    closeResultSet(rs);
	    closePrepStmt(prepStmt);
		closeConnection(con);  //System.out.println("getRecordsNumber关闭连接");
        return totalCount;
	}
	
 	public int getNumberOfAllRecord(String tableName) throws Exception{//获得指定表记录数
		int totalCount = 0;
		Connection con = getConnection(); 
		String sql = "select count(*) from "+tableName;
		PreparedStatement prepStmt = con.prepareStatement(sql);
	    ResultSet rs = prepStmt.executeQuery();//RS结果集得出好多记录 每一个记录就是一个tableContants
	    if (rs.next()){
	    	totalCount=rs.getInt(1);
	    }
	    closeResultSet(rs);
	    closePrepStmt(prepStmt);
		closeConnection(con);  //System.out.println("getNumberOfAllRecord关闭连接");
        return totalCount;
	} 
	
/*	public String getTableSQL(String tableName) throws Exception{//获得指定表的创建语句   创建带有外键 貌似外键用不到 
		List<primaryKey> primaryKeys = getPrimaryKeys(tableName);
		List<foreignKey> foreignKeys = getForeignKeys(tableName);
		List<column> columns = getColumnsInfo(tableName);
		
		String sql = "CREATE TABLE "+tableName+" ("; 	
		//字段信息
		Iterator<column> col_itr = columns.iterator();
		while (col_itr.hasNext()) {
			column col = col_itr.next();
			sql += col.getColName()+" "+ col.getTypeName();  //有的特殊的数据类型不用 size 例如DATA
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
		//System.out.println(tableName+"截止到字段:"+sql);
		
		//主外键信息
		String action[] = {"cascade", "restrict", "set null", "no action", "set default"};
		if(!primaryKeys.isEmpty()){//创建只有主键的表
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
            //System.out.println(tableName+"到主键语句:"+sql);
			if (!foreignKeys.isEmpty()){ //创建既有主键又有外键的表
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
	            //System.out.println(tableName+"到外键语句:"+sql);
			}
		}else if (primaryKeys.isEmpty() && !foreignKeys.isEmpty()){ //创建只有外键的表 一般不会有
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
            System.out.println(tableName+"只有外键表:"+sql);
		}		
		//最后 
		sql += ")";
		return sql;		
	}*/
	
	public String getTableSQL(String tableName,String tableType) throws Exception{//获得指定表的创建语句
	    List<primaryKey> primaryKeys = getPrimaryKeys(tableName);
	    List<column> columns = getColumnsInfo(tableName);
	    
	    String NewTableName = tableName;
	    if("sourceForTrigger".equals(tableType)){//为了触发器建立的表
	    	NewTableName += "_TRIG_TL";
	    }
	    String sql = "CREATE TABLE "+NewTableName+" ("; 	
	    //字段信息
	    Iterator<column> col_itr = columns.iterator();
	    while (col_itr.hasNext()) {
		     column col = col_itr.next();
		     sql += col.getColName()+" "+ col.getTypeName();  //有的特殊的数据类型不用 size 例如DATA
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
	    //System.out.println(tableName+"截止到字段:"+sql);
	
	    //主键信息
	    String action[] = {"cascade", "restrict", "set null", "no action", "set default"};
	    if(!primaryKeys.isEmpty()){//创建只有主键的表 这里外键作用不大 备份表有可能不是同步的？？ 只要原始数据控制 备份数据不会出错
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
           //System.out.println(tableName+"到主键语句:"+sql);
	  }
	  //最后 
	  sql += ")";
	  return sql;		
   }
	
	public void creatTablebySQL(String sql) throws Exception{//根据创建的SQL语句在目的数据库中创建相应表
		Connection con = getConnection(); 
		PreparedStatement prepStmt = con.prepareStatement(sql);
	    prepStmt.executeUpdate();
	    closePrepStmt(prepStmt);
		closeConnection(con);  System.out.println("creatTablebySQL关闭连接");
	}
	
	public void dateArchive(String tablename, int colNum,List<column> cols,List<tableContants> contants) throws Exception{ //项目数据库插入数据
         Iterator<column> cols_iter = cols.iterator();
        
         //创建插入SQL语句  同时找到字段类型为DATE的字段 保存其位置
         //"insert into relicInfo (relicID,relicName, pictureID,outTime, outSite,dynasty, info) values(?,?,?,?,?,?,?)
         int[] col_date = new int[colNum+1]; //保存字段类型为DATE的字段位置  字段为DATE 相应位置数组值==1
         int count = 1;
         String sql = "insert into "+tablename+" (";
         
         while (cols_iter.hasNext()) {
        	 //保存字段类型为DATE的字段位置 从1开始
        	 column col_temp = cols_iter.next();
        	 if("DATE".equals(col_temp.getTypeName())){        		  
	              col_date[count] = 1;
             }
        	 count++;
        	 
        	 //创建SQL语句
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
         //System.out.println("插入数据SQL语句："+sql);

         //执行数据归档 
         Connection con = getConnection();
         Iterator<tableContants> iter_tc = contants.iterator();
         while (iter_tc.hasNext()) { 	    	     
              tableContants tc = iter_tc.next();//提取一条记录    	     
              //执行SQL数据				
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
        closeConnection(con);  //System.out.println("dateArchive关闭连接");     		    
    }
	
	public boolean isTableExisted(String tablename){//测试表是否已经存在
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
	
	public  String[] getArchieveSequence(String chooseboxstr[]){//根据选中的表名 及表的主外键关系获得表归档的顺序
		int sqlen = chooseboxstr.length;
		List<String> resultPK = new ArrayList<String>();
		List<String> resultFK = new ArrayList<String>();
		for (int i = 0; i<sqlen;i++){
			String tablename = chooseboxstr[i];
			try {
				if (getForeignKeys(tablename).size() == 0){//不含外键的表
					resultPK.add(tablename);
				}else{
					resultFK.add(tablename);//含有外键的表
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		resultPK.addAll(resultFK);//将带有主键 外键的表名合并
		String[] result = new String[resultPK.size()];
		result = resultPK.toArray(result);
		return result;
	}
		
	public void creatTrigger (String tablename, int colNum, List<column> cols) throws Exception{
		Connection con = getConnection();
		String triggerName = tablename+"_TRIG";
		String trigTableName = tablename+"_TRIG_TL";
		//判断是否触发器已存在
        String test = "select * from user_triggers where trigger_name='"+triggerName+"'";
		PreparedStatement prepStmt1 = con.prepareStatement(test);
		int ifexist = prepStmt1.executeUpdate();
		closePrepStmt(prepStmt1);
		if(ifexist==0){//不存在再创建
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
			  //根据COLNAME组建SQL语句
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
		      //创建触发器
		      System.out.println(trigger_sql);
		      Statement stmt = con.createStatement();
		      stmt.executeUpdate(trigger_sql);	
		      stmt.close();
		}		
		closeConnection(con);  
		System.out.println("creatTrigger关闭连接");
	}
	
	public void deletArchivedData(String tablename, String archiveType, List<tableContants> contants) throws Exception{ //项目数据库插入数据
		//创建删除SQL语句  
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
        //执行归档数据删除
        Connection con = getConnection();
        con.setAutoCommit(false);
        PreparedStatement ps = con.prepareStatement(delete_sql);
        for(tableContants tc : contants){
        	ps.setString(1,tc.getContantbyColumnName(pk1).trim());  
        	//这里要是加上TRIM()  tc.getContantbyColumnName(pk1).trim() 去掉空白 删除时无法与数据库匹配 因为源数据库触发器插入就有空白 待研究
        	ps.addBatch(); 
        }
        ps.executeBatch();
        con.commit();  
        closePrepStmt(ps);
        closeConnection(con);      
	}     
	
	public List<tableContants> getSelectRecord(String tableName, String cols[],String cons[]) throws Exception{
		//获取查询语句         
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
		
		//开始取记录
        Connection con = getConnection();
        PreparedStatement prepStmt = con.prepareStatement(select);
        ResultSet rs = prepStmt.executeQuery();//RS结果集得出好多记录 每一个记录就是一个tableContants
        List<tableContants> contants = new ArrayList<tableContants>();
        while (rs.next()) {
             tableContants tc = new tableContants(rs,columnsName.size(),columnsName); //注意现在的RS指的是当前指向的结果集中的一个结果 
                                                                    //所以再使用rs.next()会出错 没有rs.next()
             contants.add(tc);             
        }
        closeResultSet(rs);
        closePrepStmt(prepStmt);
        closeConnection(con); 
        return contants; 
	}
}