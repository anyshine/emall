package cn.tmall.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.tmall.bean.Category;
import cn.tmall.bean.Order;
import cn.tmall.bean.OrderItem;
import cn.tmall.bean.Page;
import cn.tmall.bean.Product;
import cn.tmall.bean.ProductImage;
import cn.tmall.bean.PropertyValue;
import cn.tmall.bean.Review;
import cn.tmall.bean.User;
import cn.tmall.comparator.ProductAllComparator;
import cn.tmall.comparator.ProductDateComparator;
import cn.tmall.comparator.ProductPriceComparator;
import cn.tmall.comparator.ProductReviewComparator;
import cn.tmall.comparator.ProductSaleCountComparator;
import cn.tmall.dao.CategoryDao;
import cn.tmall.dao.CategoryDaoImpl;
import cn.tmall.dao.OrderDao;
import cn.tmall.dao.OrderDaoImpl;
import cn.tmall.dao.OrderItemDao;
import cn.tmall.dao.OrderItemDaoImpl;
import cn.tmall.dao.ProductDao;
import cn.tmall.dao.ProductDaoImpl;
import cn.tmall.dao.ProductImageDao;
import cn.tmall.dao.ProductImageDaoImpl;
import cn.tmall.dao.PropertyDao;
import cn.tmall.dao.PropertyDaoImpl;
import cn.tmall.dao.PropertyValueDao;
import cn.tmall.dao.PropertyValueDaoImpl;
import cn.tmall.dao.ReviewDao;
import cn.tmall.dao.ReviewDaoImpl;
import cn.tmall.dao.UserDao;
import cn.tmall.dao.UserDaoImpl;

@SuppressWarnings("serial")
public class ForeServlet extends HttpServlet {
	
	protected CategoryDao categoryDao = new CategoryDaoImpl();
	protected UserDao userDao = new UserDaoImpl();
	protected OrderDao orderDao = new OrderDaoImpl();
	protected OrderItemDao orderItemDao = new OrderItemDaoImpl();
	protected ProductDao productDao = new ProductDaoImpl();
	protected ProductImageDao productImageDao = new ProductImageDaoImpl();
	protected PropertyDao propertyDao = new PropertyDaoImpl();
	protected PropertyValueDao propertyValueDao = new PropertyValueDaoImpl();
	protected ReviewDao reviewDao = new ReviewDaoImpl();
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int start = 0;
		int count = 5;
		try {
			start = Integer.parseInt(request.getParameter("page.start"));
		} catch (Exception e) {
		}
		try {
			count = Integer.parseInt(request.getParameter("page.count"));
		} catch (Exception e) {
		}
		Page page = new Page(start, count);

		String methodName = (String) request.getAttribute("method");
		Method method = null;
		try {
			method = this.getClass().getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class,
					Page.class);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("方法[" + methodName + "]不存在！", e);
		}
		String result = null;
		try {
			result = (String) method.invoke(this, request, response, page);
			if (result != null && !result.trim().isEmpty()) {
				if (result.startsWith("@")) {
					response.sendRedirect(result.substring(1));
				} else if (result.startsWith("%")) {
					response.getWriter().write(result.substring(1));
				} else {
					request.getRequestDispatcher(result).forward(request, response);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String home(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		
		List<Category> cs = categoryDao.list();
		productDao.fill(cs);
		productDao.fillByRow(cs);
		request.setAttribute("cs", cs);
		return "home.jsp";
	}
	
	public String register(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		
		boolean exist = userDao.isExist(name);
		if (exist) {
			request.setAttribute("msg", "用户名已注册，不能使用！");
			return "register.jsp";
		}
		
		User user = new User();
		user.setName(name);
		user.setPassword(password);
		userDao.add(user);
		return "registerSuccess.jsp";
	}
	
	public String login(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		
		User user = userDao.get(name);
		if (user == null) {
			request.setAttribute("msg", "用户名不存在，请重新输入！");
			return "login.jsp";
		}
		if (!password.equals(user.getPassword())) {
			request.setAttribute("msg", "密码错误！");
			return "login.jsp";
		}
		request.getSession().setAttribute("user", user);
		return "@forehome";
	}
	
	public String logout(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		return "@forehome";
	}
	
	public String product(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int pid = Integer.parseInt(request.getParameter("pid"));
		Product p = productDao.get(pid);
		List<ProductImage> singleImages = productImageDao.list(p, ProductImageDaoImpl.TYPE_SINGLE);
		List<ProductImage> detailImages = productImageDao.list(p, ProductImageDaoImpl.TYPE_DETAIL);
		p.setProductSingleImages(singleImages);
		p.setProductDetailImages(detailImages);
		productDao.setSaleAndReviewNumber(p);
		
		List<PropertyValue> pvs = propertyValueDao.list(pid);
		List<Review> reviews = reviewDao.list(pid);
		
		request.setAttribute("p", p);
		request.setAttribute("pvs", pvs);
		request.setAttribute("reviews", reviews);
		
		return "product.jsp";
	}
	
	public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return "%fail";
		}
		return "%success";
	}
	
	public String loginAjax(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		//ajax中文提交后乱码，需反编译
		name = new String(name.getBytes("iso-8859-1"), "utf-8");
		String password = request.getParameter("password");
		
		User user = userDao.get(name, password);
		if (user == null) {
			return "%fail";
		}
		request.getSession().setAttribute("user", user);
		return "%success";
	}
	
	public String addCart(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int pid = Integer.parseInt(request.getParameter("pid"));
		Product product = productDao.get(pid);
		int number = Integer.parseInt(request.getParameter("num"));
		User user = (User) request.getSession().getAttribute("user");
		
		boolean exist = false;
		List<OrderItem> ois = orderItemDao.listByUser(user.getId());
		for (OrderItem oi : ois) {
			if (oi.getProduct().getId() == pid) {
				oi.setNumber(oi.getNumber() + number);
				orderItemDao.update(oi);
				exist = true;
				break;
			}
		}
		if (!exist) {
			OrderItem oi = new OrderItem();
			oi.setUser(user);
			oi.setProduct(product);
			oi.setNumber(number);
			orderItemDao.add(oi);
		}
		return "%success";
	}
	
	public String category(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDao.get(cid);
		productDao.fill(c);
		productDao.setSaleAndReviewNumber(c.getProducts());
		
		String sort = request.getParameter("sort");
		if (sort != null) {
			switch (sort) {
			case "review":
				Collections.sort(c.getProducts(), new ProductReviewComparator());
				break;
			case "date":
				Collections.sort(c.getProducts(), new ProductDateComparator());
				break;
			case "saleCount":
				Collections.sort(c.getProducts(), new ProductSaleCountComparator());
				break;
			case "price":
				Collections.sort(c.getProducts(), new ProductPriceComparator());
				break;
			case "all":
				Collections.sort(c.getProducts(), new ProductAllComparator());
				break;
			default:
				break;
			}
		}
		
		request.setAttribute("c", c);
		return "category.jsp";
	}
	
	public String search(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		String keyword = request.getParameter("keyword");
		List<Product> ps = productDao.search(keyword, 0, 20);
		productDao.setSaleAndReviewNumber(ps);
		request.setAttribute("ps", ps);
		return "searchResult.jsp";
	}
	
	public String buyone(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int pid = Integer.parseInt(request.getParameter("pid"));
		Product product = productDao.get(pid);
		int number = Integer.parseInt(request.getParameter("num"));
		User user = (User) request.getSession().getAttribute("user");
		int oiid = 0;
		
		boolean exist = false;
		List<OrderItem> ois = orderItemDao.listByUser(user.getId());
		for (OrderItem oi : ois) {
			if (oi.getProduct().getId() == pid) {
				oi.setNumber(oi.getNumber() + number);
				orderItemDao.update(oi);
				exist = true;
				oiid = oi.getId();
				break;
			}
		}
		if (!exist) {
			OrderItem oi = new OrderItem();
			oi.setUser(user);
			oi.setProduct(product);
			oi.setNumber(number);
			orderItemDao.add(oi);
			oiid = oi.getId();
		}
		return "@forebuy?oiid=" + oiid;
	}
	
	//结算
	public String buy(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		String[] oiids = request.getParameterValues("oiid");
		List<OrderItem> ois = new ArrayList<OrderItem>();
		float total = 0f;
		for (String oiid_str : oiids) {
			int oiid = Integer.parseInt(oiid_str);
			OrderItem oi = orderItemDao.get(oiid);
			total += oi.getNumber() * oi.getProduct().getPromotePrice();
			ois.add(oi);
		}
		request.setAttribute("total", total);
		request.getSession().setAttribute("ois", ois);
		return "buy.jsp";
	}
	
	public String cart(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		List<OrderItem> ois = orderItemDao.listByUser(user.getId());
		request.setAttribute("ois", ois);
		return "cart.jsp";
	}
	
	public String changeOrderItem(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return "%fail";
		}
		
		int oiid = Integer.parseInt(request.getParameter("oiid"));
		int number = Integer.parseInt(request.getParameter("num"));
		OrderItem oi = orderItemDao.get(oiid);
		oi.setNumber(number);
		orderItemDao.update(oi);
		return "%success";
	}
	
	public String deleteOrderItem(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return "%fail";
		}
		
		int oiid = Integer.parseInt(request.getParameter("oiid"));
		orderItemDao.delete(oiid);
		return "%success";
	}


	public String createOrder(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		List<OrderItem> ois = (List<OrderItem>) request.getSession().getAttribute("ois");
		if (ois.isEmpty()) {
			return "@login.jsp";
		}
		
		String address = request.getParameter("address");
		String post = request.getParameter("post");
		String receiver = request.getParameter("receiver");
		String mobile = request.getParameter("mobile");
		String userRemark = request.getParameter("userMessage");
		
		Order order = new Order();
//		String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) +RandomUtils.nextInt(10000);
		order.setOrderCode(UUID.randomUUID().toString().replace("-", "").toUpperCase());
		order.setAddress(address);
		order.setPost(post);
		order.setReceiver(receiver);
		order.setMobile(mobile);
		order.setUserRemark(userRemark);
		order.setUser(user);
		order.setCreateDate(new Date());
		order.setStatus(OrderDaoImpl.waitPay);
		
		orderDao.add(order);
		
		float total = 0f;
		for (OrderItem oi : ois) {
			oi.setOrder(order);
			total += oi.getProduct().getPromotePrice() * oi.getNumber();
			orderItemDao.update(oi);
		}
		
		return "@forealipay?oid="+order.getId() +"&total="+total;
	}
	
	public String alipay(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		return "alipay.jsp";
	}
	
	public String payed(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order order = orderDao.get(oid);
		order.setStatus(OrderDaoImpl.waitDelivery);
		order.setPayDate(new Date());
		orderDao.update(order);
		
		request.setAttribute("o", order);
		return "payed.jsp";
	}

	public String bought(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		List<Order> os = orderDao.list(user.getId(), OrderDaoImpl.delete);
		orderItemDao.fill(os);
		request.setAttribute("os", os);
		return "bought.jsp";
	}
	
	//跳转到确认收货页面
	public String confirmPay(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order order = orderDao.get(oid);
		orderItemDao.fill(order);
		request.setAttribute("o", order);
		return "confirmPay.jsp";
	}
	
	//确认收货
	public String orderConfirmed(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order order = orderDao.get(oid);
		order.setStatus(OrderDaoImpl.waitReview);
		order.setConfirmDate(new Date());
		orderDao.update(order);
		return "orderConfirmed.jsp";
	}
	
	public String deleteOrder(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int oid = Integer.parseInt(request.getParameter("oid"));
	    Order o = orderDao.get(oid);
	    o.setStatus(OrderDaoImpl.delete);
	    orderDao.update(o);
	    return "%success";
	}
	
	public String review(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int oid = Integer.parseInt(request.getParameter("oid"));
	    Order o = orderDao.get(oid);
	    orderItemDao.fill(o);
	    Product p = o.getOrderItems().get(0).getProduct();
	    List<Review> reviews = reviewDao.list(p.getId());
	    productDao.setSaleAndReviewNumber(p);
	    
	    request.setAttribute("p", p);
	    request.setAttribute("o", o);
	    request.setAttribute("reviews", reviews);
		return "review.jsp";
	}
	
	
	public String doreview(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int oid = Integer.parseInt(request.getParameter("oid"));
		int pid = Integer.parseInt(request.getParameter("pid"));
		String content = request.getParameter("content");
		User user = (User) request.getSession().getAttribute("user");
		
		Order o = orderDao.get(oid);
		o.setStatus(OrderDaoImpl.finish);
		orderDao.update(o);
		
		Product p = productDao.get(pid);
		Review review = new Review();
		review.setContent(content);
		review.setCreateDate(new Date());
		review.setProduct(p);
		review.setUser(user);
		reviewDao.add(review);
		
		return "@forereview?oid=" + oid + "&showonly=true";
	}
}
