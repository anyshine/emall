package cn.tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.tmall.bean.Product;
import cn.tmall.bean.ProductImage;
import cn.tmall.util.DBUtil;

public class ProductImageDaoImpl implements ProductImageDao {
	public static final String TYPE_SINGLE = "type_single";
	public static final String TYPE_DETAIL = "type_detail";
	
	@Override
	public int getTotal() {
		int total = 0;
		String sql = "select count(*) from ProductImage";
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
	public void add(ProductImage bean) {
		String sql = "insert into ProductImage values(null, ?, ?)";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);) {
			pstmt.setInt(1, bean.getProduct().getId());
			pstmt.setString(2, bean.getType());
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
	public void update(ProductImage bean) {
	}

	@Override
	public void delete(int id) {
		String sql = "delete from ProductImage where id=?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, id);
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ProductImage get(int id) {
		String sql = "select * from ProductImage where id=?";
		ProductImage bean = null;
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new ProductImage();
				bean.setId(id);
				Product product = new ProductDaoImpl().get(rs.getInt("pid"));
				bean.setProduct(product);
				bean.setType(rs.getString("type"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	@Override
	public List<ProductImage> list(Product product, String type) {

		return list(product, type, 0, Short.MAX_VALUE);
	}

	@Override
	public List<ProductImage> list(Product product, String type, int start,
			int count) {
		String sql = "select * from ProductImage where pid=? and type=? limit ?,?";
		List<ProductImage> list = new ArrayList<ProductImage>();
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, product.getId());
			pstmt.setString(2, type);
			pstmt.setInt(3, start);
			pstmt.setInt(4, count);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ProductImage bean = new ProductImage();
				bean.setId(rs.getInt(1));
				bean.setProduct(product);
				bean.setType(type);
				list.add(bean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
