package archiveDataAuto;

public class foreignKey{
	private String fkName;//���������
	private int fkNo;//������
	private String pkTablenName;//�����������������
	private String pkColumnName;//�����������������
	private int updateRule;//��������ʱ��������ı仯
	private int deleteRule;//ɾ������ʱ��������ı仯
	
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