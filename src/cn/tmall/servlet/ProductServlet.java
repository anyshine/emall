package cn.tmall.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.tmall.bean.Category;
import cn.tmall.bean.Page;
import cn.tmall.bean.Product;
import cn.tmall.bean.PropertyValue;

@SuppressWarnings("serial")
public class ProductServlet extends BaseBackServlet {

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDao.get(cid);
		List<Product> ps = productDao.list(cid, page.getStart(), page.getCount());
		int total = productDao.getTotal(cid);
		page.setTotal(total);
		page.setParam("&cid=" + cid);
		
		request.setAttribute("c", c);
		request.setAttribute("page", page);
		request.setAttribute("ps", ps);
		return "admin/listProduct.jsp";
	}

	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDao.get(cid);
		String name = request.getParameter("name");
		String subTitle = request.getParameter("subTitle");
		Float originalPrice = Float.parseFloat(request.getParameter("originalPrice"));
		Float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
		int stock = Integer.parseInt(request.getParameter("stock"));
		Date createDate = new Date();
		
		Product p = new Product();
		p.setCategory(c);
		p.setName(name);
		p.setSubTitle(subTitle);
		p.setOriginalPrice(originalPrice);
		p.setPromotePrice(promotePrice);
		p.setStock(stock);
		p.setCreateDate(createDate);
		
		productDao.add(p);
		return "@admin_product_list?cid=" + cid;
	}

	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Product p = productDao.get(id);
		request.setAttribute("p", p);
		return "admin/editProduct.jsp";
	}

	@Override
	public String update(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		int cid = Integer.parseInt(request.getParameter("cid"));
		String name = request.getParameter("name");
		String subTitle = request.getParameter("subTitle");
		Float originalPrice = Float.parseFloat(request.getParameter("originalPrice"));
		Float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
		int stock = Integer.parseInt(request.getParameter("stock"));
		
		Product p = productDao.get(id);
		p.setName(name);
		p.setSubTitle(subTitle);
		p.setOriginalPrice(originalPrice);
		p.setPromotePrice(promotePrice);
		p.setStock(stock);
		
		productDao.update(p);
		return "@admin_product_list?cid=" + cid;
	}

	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		int cid = productDao.get(id).getCategory().getId();
		productDao.delete(id);
		
		return "@admin_product_list?cid=" + cid;
	}
	
	public String editPropertyValue(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int pid = Integer.parseInt(request.getParameter("id"));
		Product p = productDao.get(pid);
		propertyValueDao.init(p);
		List<PropertyValue> pvs = propertyValueDao.list(pid);
		
		request.setAttribute("p", p);
		request.setAttribute("pvs", pvs);
		return "admin/editPropertyValue.jsp";
	}
	public String updatePropertyValue(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String value = request.getParameter("value");
		
		PropertyValue pv = propertyValueDao.get(id);
		pv.setValue(value);
		propertyValueDao.update(pv);
		
		return "%success";
	}
}
