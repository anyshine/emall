package cn.tmall.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.tmall.bean.Category;
import cn.tmall.bean.OrderItem;
import cn.tmall.bean.User;
import cn.tmall.dao.CategoryDaoImpl;
import cn.tmall.dao.OrderItemDaoImpl;

public class ForeAuthFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		String path = request.getServletPath();
		String[] noNeedAuthPage = new String[]{
                "home",
                "checkLogin",
                "register",
                "loginAjax",
                "login",
                "product",
                "category",
                "search"};
		
		if (path.startsWith("/fore") && !path.startsWith("/foreServlet")) {
			String method = path.substring(5);
			if (!Arrays.asList(noNeedAuthPage).contains(method)) {
				User user = (User) request.getSession().getAttribute("user");
				if (user == null) {
					response.sendRedirect("login.jsp");
					return;
				}
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

}
