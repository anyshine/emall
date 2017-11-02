package cn.tmall.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.tmall.bean.Order;
import cn.tmall.bean.Page;
import cn.tmall.dao.OrderDaoImpl;

@SuppressWarnings("serial")
public class OrderServlet extends BaseBackServlet {

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		List<Order> os = orderDao.list(page.getStart(), page.getCount());
		orderItemDao.fill(os);
		int total = orderDao.getTotal();
		page.setTotal(total);
		
		request.setAttribute("page", page);
		request.setAttribute("os", os);
		return "admin/listOrder.jsp";
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

	public String delivery(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int oid = Integer.parseInt(request.getParameter("id"));
		Order o = orderDao.get(oid);
		Date deliveryDate = new Date();
		
		if (o.getStatus().equals(OrderDaoImpl.waitDelivery)) {
			o.setStatus(OrderDaoImpl.waitConfirm);
			o.setDeliveryDate(deliveryDate);
		}
		orderDao.update(o);
		
		return "@admin_order_list";
	}

}
