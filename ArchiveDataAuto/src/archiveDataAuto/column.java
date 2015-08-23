package archiveDataAuto;

public class column{
	private String colName;//列名称
	private String typeName;//列数据类型
	private int  colSize;//列的精度
	private int decimalDigits;//列的小数位数
	private int nullAble;
	
    public column(String colname, String typename, int colsize, int decimaldigits, int nullable){
    	this.colName = colname;
    	this.typeName = typename;
    	this.colSize = colsize;
    	this.decimalDigits = decimaldigits;
    	this.nullAble = nullable;
    }
    
    public String getColName(){
    	return colName;
    }
    
    public String getTypeName(){
    	return typeName;
    }
    
    public int getColSize(){
    	return colSize;
    }
    
    public int getDecimalDigits(){
    	return decimalDigits;
    }
    
    public int getNullAble(){
    	return nullAble;
    }
}