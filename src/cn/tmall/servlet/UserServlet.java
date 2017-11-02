package cn.tmall.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.tmall.bean.Page;
import cn.tmall.bean.User;

@SuppressWarnings("serial")
public class UserServlet extends BaseBackServlet {

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		List<User> users = userDao.list(page.getStart(), page.getCount());
		int total = userDao.getTotal();
		page.setTotal(total);
		
		request.setAttribute("page", page);
		request.setAttribute("users", users);
		return "admin/listUser.jsp";
	}

	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {

		return null;
	}

	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {

		return null;
	}

	@Override
	public String update(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {

		return null;
	}

	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {

		return null;
	}

}
