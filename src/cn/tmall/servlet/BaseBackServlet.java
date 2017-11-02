package cn.tmall.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.tmall.bean.Page;
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

public abstract class BaseBackServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected CategoryDao categoryDao = new CategoryDaoImpl();
	protected UserDao userDao = new UserDaoImpl();
	protected OrderDao orderDao = new OrderDaoImpl();
	protected OrderItemDao orderItemDao = new OrderItemDaoImpl();
	protected ProductDao productDao = new ProductDaoImpl();
	protected ProductImageDao productImageDao = new ProductImageDaoImpl();
	protected PropertyDao propertyDao = new PropertyDaoImpl();
	protected PropertyValueDao propertyValueDao = new PropertyValueDaoImpl();
	protected ReviewDao reviewDao = new ReviewDaoImpl();

	public abstract String add(HttpServletRequest request,
			HttpServletResponse response, Page page) throws ServletException, IOException;

	public abstract String delete(HttpServletRequest request,
			HttpServletResponse response, Page page) throws ServletException, IOException;

	public abstract String edit(HttpServletRequest request,
			HttpServletResponse response, Page page) throws ServletException, IOException;

	public abstract String update(HttpServletRequest request,
			HttpServletResponse response, Page page) throws ServletException, IOException;

	public abstract String list(HttpServletRequest request,
			HttpServletResponse response, Page page) throws ServletException, IOException;

	@Override
	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		int start = 0;
		int count = 5;
		try {
			start = Integer.parseInt(req.getParameter("page.start"));
		} catch (Exception e) {
		}
		try {
			start = Integer.parseInt(req.getParameter("page.count"));
		} catch (Exception e) {
		}
		Page page = new Page(start, count);

		String methodName = (String) req.getAttribute("method");
		Method method = null;
		try {
			method = this.getClass().getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class, Page.class);
		} catch (Exception e) {
			throw new RuntimeException("方法[" + methodName + "]不存在！", e);
		}
		String result;
		try {
			result = (String) method.invoke(this, req, res, page);
			if (result != null && !result.trim().isEmpty()) {
				if (result.startsWith("@")) {
					res.sendRedirect(result.substring(1));
				} else if (result.startsWith("%")) {
					res.getWriter().write(result.substring(1));
				} else {
					req.getRequestDispatcher(result).forward(req, res);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	public InputStream parseUpload(HttpServletRequest request, Map<String, String> params) {
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setFileSizeMax(1024 * 1024 * 10);
		
		InputStream in = null;
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> fileItems = sfu.parseRequest(request);
			for (FileItem fileItem : fileItems) {
				if (!fileItem.isFormField()) {
					in = fileItem.getInputStream();
				} else {
					String paramName = fileItem.getFieldName();
					String paramValue = fileItem.getString("utf-8");//request.setCharacterEncoding无效？
					params.put(paramName, paramValue);
				}
			}
		} catch (FileUploadException | IOException e) {
			e.printStackTrace();
		}
		return in;
	}

}
