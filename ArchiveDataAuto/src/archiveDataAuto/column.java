package archiveDataAuto;

public class column{
	private String colName;//������
	private String typeName;//����������
	private int  colSize;//�еľ���
	private int decimalDigits;//�е�С��λ��
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