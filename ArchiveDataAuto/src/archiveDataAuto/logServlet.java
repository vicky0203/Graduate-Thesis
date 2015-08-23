package archiveDataAuto;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.*;

public class logServlet extends HttpServlet {
	public void init() throws ServletException {
		String path = getServletContext().getRealPath("/");
		String propfile = getInitParameter("propfile");
		PropertyConfigurator.configure(path+propfile); 
        //System.out.println(path+propfile);  
	}
}