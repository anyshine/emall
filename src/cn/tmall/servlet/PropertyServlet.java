package cn.tmall.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.tmall.bean.Category;
import cn.tmall.bean.Page;
import cn.tmall.bean.Property;

@SuppressWarnings("serial")
public class PropertyServlet extends BaseBackServlet {

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDao.get(cid);
		List<Property> ps = propertyDao.list(cid, page.getStart(), page.getCount());
		int total = propertyDao.getTotal(cid);
		page.setTotal(total);
		page.setParam("&cid=" + cid);
		
		request.setAttribute("c", c);
		request.setAttribute("page", page);
		request.setAttribute("ps", ps);
		return "admin/listProperty.jsp";
	}

	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDao.get(cid);
		
		Property p = new Property();
		p.setName(name);
		p.setCategory(c);
		propertyDao.add(p);
		return "@admin_property_list?cid=" + cid;
	}

	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Property p = propertyDao.get(id);
		request.setAttribute("p", p);
		return "admin/editProperty.jsp";
	}

	@Override
	public String update(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDao.get(cid);
		String name = request.getParameter("name");
		
		Property p = new Property();
		p.setId(id);
		p.setName(name);
		p.setCategory(c);
		propertyDao.update(p);
		return "@admin_property_list?cid=" + cid;
	}

	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		int cid = propertyDao.get(id).getCategory().getId();
		propertyDao.delete(id);
		
		return "@admin_property_list?cid=" + cid;
	}
	
}
