package archiveDataAuto;

public class primaryKey{
	private String pkName;
	private int pkNo;
	
    public primaryKey(String keyname, int keyno){
    	this.pkName = keyname;
        this.pkNo = keyno;
    }
    
    public String getPKName(){
    	return pkName;
    }
    
    public int getPKNo(){
    	return pkNo;
    }
}