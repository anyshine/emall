package cn.tmall.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.tmall.bean.Category;
import cn.tmall.bean.Page;
import cn.tmall.util.ImageUtil;

@SuppressWarnings("serial")
public class CategoryServlet extends BaseBackServlet {
	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		
		Map<String, String> params = new HashMap<String, String>();
		InputStream in = parseUpload(request, params);

		Category category = new Category();
		String categoryName = params.get("name");
		category.setName(categoryName);
		categoryDao.add(category);
		
//		File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
		File imageFolder = new File("D:/Image/tmall_img/category");
		File image = new File(imageFolder, category.getId() + ".jpg");
		image.getParentFile().mkdirs();
		
		if (in != null && in.available() != 0) {
			try (FileOutputStream fos = new FileOutputStream(image);) {
				byte[] buf = new byte[1024 * 1024];
				int len = 0;
				while ((len = in.read(buf)) != -1) {
					fos.write(buf, 0, len);
				}
				fos.flush();
				
				//通过如下代码，把文件保存为jpg格式
				BufferedImage img = ImageUtil.change2jpg(image);
				ImageIO.write(img, "jpg", image);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "@admin_category_list";
	}

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		List<Category> list = categoryDao.list(page.getStart(), page.getCount());
		int total = categoryDao.getTotal();
		page.setTotal(total);

		request.setAttribute("page", page);
		request.setAttribute("thecs", list);

		return "admin/listCategory.jsp";
	}

	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		categoryDao.delete(id);
		return "@admin_category_list";
	}

	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Category category = categoryDao.get(id);
		request.setAttribute("c", category);
		return "admin/editCategory.jsp";
	}

	@Override
	public String update(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		Map<String, String> params = new HashMap<String, String>();
		InputStream in = parseUpload(request, params);

		Category category = new Category();
		int id = Integer.parseInt(params.get("id"));
		String categoryName = params.get("name");
		category.setId(id);
		category.setName(categoryName);
		categoryDao.update(category);
		
		File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
		File image = new File(imageFolder, category.getId() + ".jpg");
		image.getParentFile().mkdirs();
		
		if (in != null && in.available() != 0) {
			try (FileOutputStream fos = new FileOutputStream(image);) {
				byte[] buf = new byte[1024 * 1024];
				int len = 0;
				while ((len = in.read(buf)) != -1) {
					fos.write(buf, 0, len);
				}
				fos.flush();
				
				//通过如下代码，把文件保存为jpg格式
				BufferedImage img = ImageUtil.change2jpg(image);
				ImageIO.write(img, "jpg", image);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "@admin_category_list";
	}

}
