package archiveDataAuto;

import java.sql.*;
import java.util.*;

public class tableContants{//每一个记录抽象为一个类  这个类的每一个实例就是一个记录
   private List<String> columnsContant = new ArrayList<String>();
   private List<String> columnsName = new ArrayList<String>();
   //private int columnNum = 0;
    
   public tableContants(ResultSet columns, int columnNumber,List<String> columnName) {//构造函数 每个记录的所有字段都在这个集合里
	   //注意现在的columns指的是当前指向的结果集中的一个结果  所以再使用rs.next()会出错 没有rs.next()
	    //this.columnNum = columnNumber;   	
	    this.columnsName = columnName;
    	int i=1;
    	try {
			while (i<=columnNumber) {
				String str = columns.getString(i);
				//System.out.println("构造函数"+i+"  "+str);
				columnsContant.add(str);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
	    }
    }
    
    public String getContantbyIndex(int index) {//提取每一个字段的内容 便于显示
    	String temp = columnsContant.get(index - 1);
    	return temp;
    }
    
    public String getContantbyColumnName(String columnname) {//提取每一个字段的内容 便于显示
    	String temp = null;
    	Iterator<String> iter = columnsName.iterator();
    	int i=1;
	    while (iter.hasNext()) {
	    	 if(iter.next().equals(columnname)){
	    		 //System.out.println(i +" "+ columnname);
	    		 temp = getContantbyIndex(i);
	    		 break;
	    	 } 
	    	 i++;
	    }
		return temp;
    }
    
    public String getColumnName(int index){
    	return columnsName.get(index - 1);
    }
    
    public void transContants(List<DBTransform> dbtrans, String tl){
    	Iterator<DBTransform> iter_dbt = dbtrans.iterator();
    	while(iter_dbt.hasNext()){
    		DBTransform dbt = iter_dbt.next();
            String tablename = dbt.getTablename();
            String colname = dbt.getColname();
            String sourcedata = dbt.getSource_data();
            String trandata = dbt.getTrans_data();     
            
    		if(tl.equals(tablename) && getContantbyColumnName(colname).equals(sourcedata)){
    			//找到colname的index 
    			int index = columnsName.indexOf(colname);
    			//根据配置文件转换数据
    			columnsContant.set(index, trandata);
    		}
    	}   	
    }
    
    public String toString(){
    	String temp = "";
    	for(int i=1;i<=columnsName.size();i++){
    		temp += columnsContant.get(i - 1).trim()+"::";
    	}
    	return temp;
    }
}