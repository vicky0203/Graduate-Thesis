package archiveDataAuto;

public class DBInfo {
	private String dbtype;
	private String dbhost;
	private String dbname;
	private String dbuser;
	private String dbpassword;
	private String dbidentity;

	public String getDBType() {
		return dbtype;
	}
	public void setDBType(String dbt) {
		this.dbtype = dbt;
	}
	
	public String getDBHost() {
		return dbhost;
	}
	public void setDBHost(String dbh) {
		this.dbhost = dbh;
	}
	
	public String getDBName() {
		return dbname;
	}
	public void setDBName(String dbn) {
		this.dbname = dbn;
	}
	
	public String getDBUser() {
		return dbuser;
	}
	public void setDBUser(String dbu) {
		this.dbuser = dbu;
	}
	
	public String getDBPassword() {
		return dbpassword;
	}
	public void setDBPassword(String dbp) {
		this.dbpassword = dbp;
	}
	
	public String getDBIdentity() {
		return dbidentity;
	}
	public void setDBIdentity(String dbi) {
		this.dbidentity = dbi;
	}
	
	public boolean isEmpty(){
		boolean result = false;
		if(this.dbtype.isEmpty() && this.dbhost.isEmpty() && this.dbname.isEmpty() && this.dbuser.isEmpty() && 
		           this.dbpassword.isEmpty() && this.dbidentity.isEmpty()){
			result = true;
		}
		return result;
	}
	
	@Override
	public String toString(){
		return this.dbtype+":"+this.dbhost+":"+this.dbname+":"+this.dbuser+":"+
	           this.dbpassword+":"+this.dbidentity;
	}
}