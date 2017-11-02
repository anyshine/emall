package cn.tmall.filter;

import java.io.IOException;
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

public class BaseServletFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html;charset=UTF-8");
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		String path = request.getServletPath();
		if (path.startsWith("/admin_")) {
			int start = path.indexOf('_');
			int end = path.lastIndexOf('_');
			String servletName = path.substring(start + 1, end) + "Servlet";
			String method = path.substring(end + 1);
			request.setAttribute("method", method);
			request.getRequestDispatcher("/" + servletName).forward(request, response);
			return;
		}
		
		User user = (User) request.getSession().getAttribute("user");
		int cartTotalItemNumber = 0;
		if (user != null) {
			List<OrderItem> ois = new OrderItemDaoImpl().listByUser(user.getId());
			for (OrderItem orderItem : ois) {
				cartTotalItemNumber += orderItem.getNumber();
			}
		}
		request.setAttribute("cartTotalItemNumber", cartTotalItemNumber);
		
		@SuppressWarnings("unchecked")
		List<Category> cs = (List<Category>) request.getAttribute("cs");
		if (cs == null) {
			cs = new CategoryDaoImpl().list();
			request.setAttribute("cs", cs);
		}
		
		if (path.startsWith("/fore") && !path.startsWith("/foreServlet")) {
			String method = path.substring(5);
			request.setAttribute("method", method);
			request.getRequestDispatcher("/foreServlet").forward(request, response);
			return;
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

}
