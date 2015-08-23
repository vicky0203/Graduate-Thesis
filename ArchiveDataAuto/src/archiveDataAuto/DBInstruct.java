package archiveDataAuto;

public class DBInstruct {
	private String table_name;
	private String colname;
	private String condition;
	private String value;

    public String getTablename() {
		return table_name;
	}
	public void setTablename(String dbt) {
		this.table_name = dbt;
	}
	
	public String getColname() {
		return colname;
	}
	public void setColname(String dbc) {
		this.colname = dbc;
	}
	
	public String getCondition() {
		return condition;
	}
	public void setCondition(String dbcc) {
		this.condition = dbcc;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String dbv) {
		this.value = dbv;
	}
	
	public boolean isEmpty(){
		boolean result = false;
		if(this.table_name.isEmpty() && this.colname.isEmpty() && this.condition.isEmpty() && this.value.isEmpty()){
			result = true;
		}
		return result;
	}
	
	@Override
	public String toString(){
		return this.table_name+":"+this.colname+":"+this.condition+":"+this.value;
	}
}