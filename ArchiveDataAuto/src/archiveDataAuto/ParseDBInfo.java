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
        SAXReader reader = new SAXReader(); //����һ������������
        Document document = null; 
        try { 
        	//��ȡָ��xml�ĵ���Document����,xml�ļ�������classpath�п����ҵ�
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(xmlFilePath); 
            document = reader.read(in); 
        } catch (DocumentException e) { 
            System.out.println(e.getMessage()); 
            System.out.println("��ȡclasspath��xmlFileName�ļ������쳣������CLASSPATH���ļ����Ƿ���ڣ�"); 
            e.printStackTrace(); 
        } 
        return document; 
    } 
	
	
	public void ParseXMLData(String xmlFileName) {            
        Document document = parseXmlDocument(xmlFileName); //��xml�ĵ�ת��ΪDocument�Ķ���        
        Element root = document.getRootElement(); //��ȡ�ĵ��ĸ�Ԫ�� <database_info>
        Iterator<?> iter_dbinfo = root.elementIterator();
        while (iter_dbinfo.hasNext()) { 
            Element dbi = (Element) iter_dbinfo.next(); // ��ȡ<database_info>����Ԫ��
            String dbinfo_name = dbi.getName();// ��ȡ��Ԫ�ص�����
            if("database".equals(dbinfo_name)){ //��Ԫ��Ϊ���ݿ�������Ϣ
            	DBInfo dbinfo = new DBInfo();
                //��ȡ��ǰԪ�ص����Ե�ֵ���ֱ𸳸����� 
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
            	if(!dbInst.isEmpty()){  //�ж��ǲ���Ϊ�� ��ǩ����Ϊ��Ҳ��û��������Ϣ ���ӵ�LIST��
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