package archiveDataAuto;

public class foreignKey{
	private String fkName;//外键列名称
	private int fkNo;//外键序号
	private String pkTablenName;//被导入的主键表名称
	private String pkColumnName;//被导入的主键列名称
	private int updateRule;//更新主键时外键发生的变化
	private int deleteRule;//删除主键时外键发生的变化
	
    public foreignKey(String fkname, int fkno, String pkTablenname,
    		         String pkColumnname, int updaterule, int deleterule){
    	this.fkName = fkname;
        this.fkNo = fkno;
        this.pkTablenName = pkTablenname;
        this.pkColumnName = pkColumnname;
        this.updateRule = updaterule;
        this.deleteRule = deleterule;
    }
    
    public String getFKName(){
    	return fkName;
    }
    
    public int getFKNo(){
    	return fkNo;
    }
    
    public String getPKTablenName(){
    	return pkTablenName;
    }
    
    public String getPKColumnName(){
    	return pkColumnName;
    }
    
    public int getUpdateRule(){
    	return updateRule;
    }
    
    public int getDeleteRule(){
    	return deleteRule;
    }
}