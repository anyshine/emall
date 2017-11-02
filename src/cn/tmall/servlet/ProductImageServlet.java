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

import cn.tmall.bean.Page;
import cn.tmall.bean.Product;
import cn.tmall.bean.ProductImage;
import cn.tmall.util.ImageUtil;

@SuppressWarnings("serial")
public class ProductImageServlet extends BaseBackServlet {

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		int pid = Integer.parseInt(request.getParameter("pid"));
		Product p = productDao.get(pid);
		List<ProductImage> singleImages = productImageDao.list(p, "type_single");
		List<ProductImage> detailImages = productImageDao.list(p, "type_detail");
		
		request.setAttribute("p", p);
		request.setAttribute("singleImages", singleImages);
		request.setAttribute("detailImages", detailImages);
		return "admin/listProductImage.jsp";
	}

	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page)
			throws ServletException, IOException {
		Map<String, String> params = new HashMap<String, String>();
		InputStream is = parseUpload(request, params);
		int pid = Integer.parseInt(params.get("pid"));
		Product p = productDao.get(pid);
		String type = params.get("type");
		
		ProductImage pi = new ProductImage();
		pi.setProduct(p);
		pi.setType(type);
		
		productImageDao.add(pi);
		
		File imageFolder = null;
		File imageFolder_small = null;
		File imageFolder_middle = null;
		if (type.equals("type_single")) {
//			imageFolder = new File(request.getServletContext().getRealPath("img/productSingle"));
			imageFolder = new File("D:/Image/tmall_img/productSingle");
			imageFolder_small = new File("D:/Image/tmall_img/productSingle_small");
			imageFolder_middle = new File("D:/Image/tmall_img/productSingle_middle");
		} else if (type.equals("type_detail")) {
//			imageFolder = new File(request.getServletContext().getRealPath("img/productDetail"));
			imageFolder = new File("D:/Image/tmall_img/productDetail");
		}
		imageFolder.mkdirs();
		String filename = pi.getId() + ".jpg";
		File image = new File(imageFolder, filename);
		
		if (is != null && is.available() != 0) {
			FileOutputStream fos = new FileOutputStream(image);
			byte[] buf = new byte[1024 * 1024];
			int len = 0;
			while ((len = is.read(buf)) != -1) {
				fos.write(buf, 0, len);
			}
			fos.close();
			
			BufferedImage img = ImageUtil.change2jpg(image);
            ImageIO.write(img, "jpg", image);
            
            if (type.equals("type_single")) {
            	File image_small = new File(imageFolder_small, filename);
            	File image_middle = new File(imageFolder_middle, filename);
            	ImageUtil.resizeImage(image, 56, 56, image_small);
                ImageUtil.resizeImage(image, 217, 190, image_middle);
            }
		}
		
		return "@admin_productImage_list?pid=" + pid;
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
		int id = Integer.parseInt(request.getParameter("id"));
		ProductImage pi = productImageDao.get(id);
		int pid = pi.getProduct().getId();
		String type = pi.getType();
		productImageDao.delete(id);
		
		String filename = id + ".jpg";
		File imageFolder = null;
		File imageFolder_small = null;
		File imageFolder_middle = null;
		if (type.equals("type_single")) {
			imageFolder = new File("D:/Image/tmall_img/productSingle");
			imageFolder_small = new File("D:/Image/tmall_img/productSingle_small");
			imageFolder_middle = new File("D:/Image/tmall_img/productSingle_middle");
			new File(imageFolder, filename).delete();
			new File(imageFolder_small, filename).delete();
			new File(imageFolder_middle, filename).delete();
		} else {
			imageFolder = new File("D:/Image/tmall_img/productDetail");
			new File(imageFolder, filename).delete();
		}
		
		return "@admin_productImage_list?pid=" + pid;
	}
	
}
