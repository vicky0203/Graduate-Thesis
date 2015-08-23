package archiveDataAuto;

import java.sql.*;
import java.util.*;

public class tableContants{//ÿһ����¼����Ϊһ����  ������ÿһ��ʵ������һ����¼
   private List<String> columnsContant = new ArrayList<String>();
   private List<String> columnsName = new ArrayList<String>();
   //private int columnNum = 0;
    
   public tableContants(ResultSet columns, int columnNumber,List<String> columnName) {//���캯�� ÿ����¼�������ֶζ������������
	   //ע�����ڵ�columnsָ���ǵ�ǰָ��Ľ�����е�һ�����  ������ʹ��rs.next()����� û��rs.next()
	    //this.columnNum = columnNumber;   	
	    this.columnsName = columnName;
    	int i=1;
    	try {
			while (i<=columnNumber) {
				String str = columns.getString(i);
				//System.out.println("���캯��"+i+"  "+str);
				columnsContant.add(str);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
	    }
    }
    
    public String getContantbyIndex(int index) {//��ȡÿһ���ֶε����� ������ʾ
    	String temp = columnsContant.get(index - 1);
    	return temp;
    }
    
    public String getContantbyColumnName(String columnname) {//��ȡÿһ���ֶε����� ������ʾ
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
    			//�ҵ�colname��index 
    			int index = columnsName.indexOf(colname);
    			//���������ļ�ת������
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