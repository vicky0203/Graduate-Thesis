package archiveDataAuto;

public class DBTransform {
	private String table_name;
	private String col_name;
	private String source_data;
	private String trans_data;

	public String getTablename() {
		return table_name;
	}
	public void setTablename(String dbt) {
		this.table_name = dbt;
	}
	
	public String getColname() {
		return col_name;
	}
	public void setColname(String dbc) {
		this.col_name = dbc;
	}
	
	public String getSource_data() {
		return source_data;
	}
	public void setSource_data(String dbs) {
		this.source_data = dbs;
	}
	
	public String getTrans_data() {
		return trans_data;
	}
	public void setTrans_data(String dbt) {
		this.trans_data = dbt;
	}
	
	public boolean isEmpty(){
		boolean result = false;
		if(this.table_name.isEmpty() && this.col_name.isEmpty() && this.source_data.isEmpty() && this.trans_data.isEmpty()){
			result = true;
		}
		return result;
	}
	
	@Override
	public String toString(){
		return this.table_name+":"+this.col_name+":"+this.source_data+":"+this.trans_data;
	}
}