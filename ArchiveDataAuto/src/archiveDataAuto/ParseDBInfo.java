package archiveDataAuto;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.Iterator; 
import java.util.List; 
import java.io.InputStream;

public class ParseDBInfo{
    private List<DBInfo> dbinfos = new ArrayList<DBInfo>();
	private List<DBTransform> dbtrans = new ArrayList<DBTransform>();
	private List<DBInstruct> dbinst = new ArrayList<DBInstruct>();
	
	public Document parseXmlDocument(String xmlFilePath) { 
        SAXReader reader = new SAXReader(); //产生一个解析器对象
        Document document = null; 
        try { 
        	//获取指定xml文档的Document对象,xml文件必须在classpath中可以找到
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(xmlFilePath); 
            document = reader.read(in); 
        } catch (DocumentException e) { 
            System.out.println(e.getMessage()); 
            System.out.println("读取classpath下xmlFileName文件发生异常，请检查CLASSPATH和文件名是否存在！"); 
            e.printStackTrace(); 
        } 
        return document; 
    } 
	
	
	public void ParseXMLData(String xmlFileName) {            
        Document document = parseXmlDocument(xmlFileName); //将xml文档转换为Document的对象        
        Element root = document.getRootElement(); //获取文档的根元素 <database_info>
        Iterator<?> iter_dbinfo = root.elementIterator();
        while (iter_dbinfo.hasNext()) { 
            Element dbi = (Element) iter_dbinfo.next(); // 获取<database_info>的子元素
            String dbinfo_name = dbi.getName();// 获取子元素的名称
            if("database".equals(dbinfo_name)){ //子元素为数据库配置信息
            	DBInfo dbinfo = new DBInfo();
                //获取当前元素的属性的值并分别赋给变量 
                dbinfo.setDBType(dbi.element("dbtype").getText()); 
                dbinfo.setDBIdentity(dbi.element("dbidentity").getText());
                dbinfo.setDBHost(dbi.element("dbhost").getText()); 
                dbinfo.setDBName(dbi.element("dbname").getText()); 
                dbinfo.setDBUser(dbi.element("dbuser").getText()); 
                dbinfo.setDBPassword(dbi.element("dbpassword").getText());
                if(!dbinfo.isEmpty()){
                   dbinfos.add(dbinfo); 
                }
            }else if("dbtransform".equals(dbinfo_name)){
            	DBTransform dbTran = new DBTransform();
            	dbTran.setTablename(dbi.element("tablename").getText());
            	dbTran.setColname(dbi.element("colname").getText());
            	dbTran.setSource_data(dbi.element("sourcedata").getText());
            	dbTran.setTrans_data(dbi.element("transdata").getText());
            	if(!dbTran.isEmpty()){
            	   dbtrans.add(dbTran);
            	}
            }else if("dbinstruct".equals(dbinfo_name)){
            	DBInstruct dbInst = new DBInstruct();
            	dbInst.setTablename(dbi.element("tablename").getText());
            	dbInst.setColname(dbi.element("colname").getText());
            	dbInst.setCondition(dbi.element("condition").getText());
            	dbInst.setValue(dbi.element("value").getText());
            	if(!dbInst.isEmpty()){  //判断是不是为空 标签内容为空也算没有配置信息 不加到LIST中
            		dbinst.add(dbInst);
            	}
            }           
        }
	}
	
	public List<DBInfo> getDBInfos(){
		return dbinfos;
	}
	
	public List<DBTransform> getDBTransform(){
		return dbtrans;
	}
	
	public List<DBInstruct> getDBInstruct(){
		return dbinst;
	}
}