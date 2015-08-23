package archiveDataAuto;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class userFilter implements Filter  {
	protected FilterConfig filterConfig;
	public void destroy() {
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			             FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) req;
		HttpServletResponse hres = (HttpServletResponse) res;	
		HttpSession session = hreq.getSession(true);
		//request.getSession(true)：若存在会话则返回该会话，否则新建一个会话。
		//request.getSession(false)：若存在会话则返回该会话，否则返回NULL
		String isLogin = "";
		try {
			if (null != session.getAttribute("isLogin")) {
				isLogin = session.getAttribute("isLogin").toString();
			    if ("true".equals(isLogin)) {
				    System.out.println("验证通过");
				    chain.doFilter(req, res);
			    }else {	
				   hres.sendRedirect("index.jsp");
			    }
	        }else {
	        	hres.sendRedirect("index.jsp");
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void init(FilterConfig arg0) throws ServletException {
		this.filterConfig = arg0;
	}

}
