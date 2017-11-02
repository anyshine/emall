package cn.tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.tmall.bean.Category;
import cn.tmall.bean.Product;
import cn.tmall.bean.ProductImage;
import cn.tmall.util.DBUtil;
import cn.tmall.util.DateUtil;

public class ProductDaoImpl implements ProductDao {

	@Override
	public int getTotal(int cid) {
		int total = 0;
		String sql = "select count(*) from product where cid=" + cid;
		try (Connection conn = DBUtil.getConnection();
				Statement stmt = conn.createStatement();) {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

	@Override
	public void add(Product bean) {
		String sql = "insert into product values(null,?,?,?,?,?,?,?)";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);) {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getSubTitle());
			pstmt.setFloat(3, bean.getOriginalPrice());
			pstmt.setFloat(4, bean.getPromotePrice());
			pstmt.setInt(5, bean.getStock());
			pstmt.setInt(6, bean.getCategory().getId());
			pstmt.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
			pstmt.execute();

			ResultSet rs = pstmt.getGeneratedKeys();
			while (rs.next()) {
				int id = rs.getInt(1);
				bean.setId(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Product bean) {
		String sql = "update product set name= ?, subTitle=?, originalPrice=?,"
				+ "promotePrice=?,stock=?, cid = ?, createDate=? where id = ?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getSubTitle());
			pstmt.setFloat(3, bean.getOriginalPrice());
			pstmt.setFloat(4, bean.getPromotePrice());
			pstmt.setInt(5, bean.getStock());
			pstmt.setInt(6, bean.getCategory().getId());
			pstmt.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
			pstmt.setInt(8, bean.getId());
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int id) {
		String sql = "delete from product where id=?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, id);
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Product get(int id) {
		String sql = "select * from product where id=?";
		Product bean = null;
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new Product();
				bean.setId(id);

				String name = rs.getString("name");
				String subTitle = rs.getString("subTitle");
				float originalPrice = rs.getFloat("originalPrice");
				float promotePrice = rs.getFloat("promotePrice");
				int stock = rs.getInt("stock");
				int cid = rs.getInt("cid");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

				bean.setName(name);
				bean.setSubTitle(subTitle);
				bean.setOriginalPrice(originalPrice);
				bean.setPromotePrice(promotePrice);
				bean.setStock(stock);
				Category category = new CategoryDaoImpl().get(cid);
				bean.setCategory(category);
				bean.setCreateDate(createDate);

				setFirstProductImage(bean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	@Override
	public List<Product> list(int cid) {
		return list(cid, 0, Short.MAX_VALUE);
	}

	@Override
	public List<Product> list(int cid, int start, int count) {
		String sql = "select * from product where cid=? limit ?,?";
		List<Product> list = new ArrayList<Product>();
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, cid);
			pstmt.setInt(2, start);
			pstmt.setInt(3, count);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Product bean = new Product();

				String name = rs.getString("name");
				String subTitle = rs.getString("subTitle");
				float originalPrice = rs.getFloat("originalPrice");
				float promotePrice = rs.getFloat("promotePrice");
				int stock = rs.getInt("stock");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

				bean.setId(rs.getInt(1));
				bean.setName(name);
				bean.setSubTitle(subTitle);
				bean.setOriginalPrice(originalPrice);
				bean.setPromotePrice(promotePrice);
				bean.setStock(stock);
				Category category = new CategoryDaoImpl().get(cid);
				bean.setCategory(category);
				bean.setCreateDate(createDate);
				setFirstProductImage(bean);
				list.add(bean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Product> list() {
		return list(0, Short.MAX_VALUE);
	}

	@Override
	public List<Product> list(int start, int count) {
		String sql = "select * from product limit ?,?";
		List<Product> list = new ArrayList<Product>();
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, start);
			pstmt.setInt(2, count);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Product bean = new Product();

				String name = rs.getString("name");
				String subTitle = rs.getString("subTitle");
				float originalPrice = rs.getFloat("originalPrice");
				float promotePrice = rs.getFloat("promotePrice");
				int stock = rs.getInt("stock");
				int cid = rs.getInt("cid");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

				bean.setId(rs.getInt(1));
				bean.setName(name);
				bean.setSubTitle(subTitle);
				bean.setOriginalPrice(originalPrice);
				bean.setPromotePrice(promotePrice);
				bean.setStock(stock);
				Category category = new CategoryDaoImpl().get(cid);
				bean.setCategory(category);
				bean.setCreateDate(createDate);
				list.add(bean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void fill(List<Category> list) {
		for (Category category : list) {
			fill(category);
		}
	}

	@Override
	public void fill(Category category) {
		List<Product> products = list(category.getId());
		category.setProducts(products);
	}

	@Override
	public void fillByRow(List<Category> list) {
		int productNumberEachRow = 8;
		for (Category category : list) {
			List<Product> products = category.getProducts();
			List<List<Product>> productsByRow = new ArrayList<List<Product>>();
			for (int i = 0; i < products.size(); i += productNumberEachRow) {
				int size = i + productNumberEachRow;
				size = size > products.size() ? products.size() : size;
				List<Product> productsOfEachRow = products.subList(i, size);
				productsByRow.add(productsOfEachRow);
			}
			category.setProductsByRow(productsByRow);
		}
	}

	@Override
	public void setFirstProductImage(Product p) {
		List<ProductImage> productImages = new ProductImageDaoImpl().list(p,
				ProductImageDaoImpl.TYPE_SINGLE);
		if (!productImages.isEmpty()) {
			p.setFirstProductImage(productImages.get(0));
		}
	}
	
	@Override
	public void setSaleAndReviewNumber(Product p) {
		int saleCount = new OrderItemDaoImpl().getSaleCount(p.getId());
		int reviewCount = new ReviewDaoImpl().getTotal(p.getId());
		p.setSaleCount(saleCount);
		p.setReviewCount(reviewCount);
	}
	
	@Override
	public void setSaleAndReviewNumber(List<Product> list) {
		for (Product product : list) {
			setSaleAndReviewNumber(product);
		}
	}
	
	@Override
	public List<Product> search(String keyword, int start, int count) {
		List<Product> list = new ArrayList<Product>();
		String sql = "select * from product where name like ? limit ?,?";
		if (keyword == null || keyword.trim().length() == 0) {
			return list;
		}
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, "%" + keyword.trim() + "%");
			pstmt.setInt(2, start);
			pstmt.setInt(3, count);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Product bean = new Product();

				String name = rs.getString("name");
				String subTitle = rs.getString("subTitle");
				float originalPrice = rs.getFloat("originalPrice");
				float promotePrice = rs.getFloat("promotePrice");
				int stock = rs.getInt("stock");
				int cid = rs.getInt("cid");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

				bean.setId(rs.getInt(1));
				bean.setName(name);
				bean.setSubTitle(subTitle);
				bean.setOriginalPrice(originalPrice);
				bean.setPromotePrice(promotePrice);
				bean.setStock(stock);
				Category category = new CategoryDaoImpl().get(cid);
				bean.setCategory(category);
				bean.setCreateDate(createDate);
				setFirstProductImage(bean);
				list.add(bean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
