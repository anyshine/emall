package cn.tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.tmall.bean.Product;
import cn.tmall.bean.Property;
import cn.tmall.bean.PropertyValue;
import cn.tmall.util.DBUtil;

public class PropertyValueDaoImpl implements PropertyValueDao {

	@Override
	public int getTotal() {
		int total = 0;
		String sql = "select count(*) from propertyvalue";
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
	public void add(PropertyValue bean) {
		String sql = "insert into propertyvalue values(null,?,?,?)";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);) {
			pstmt.setInt(1, bean.getProduct().getId());
			pstmt.setInt(2, bean.getProperty().getId());
			pstmt.setString(3, bean.getValue());
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
	public void update(PropertyValue bean) {
		String sql = "update propertyvalue set pid=?, ptid=?, value=? where id=?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, bean.getProduct().getId());
			pstmt.setInt(2, bean.getProperty().getId());
			pstmt.setString(3, bean.getValue());
			pstmt.setInt(4, bean.getId());
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int id) {
		String sql = "delete from propertyvalue where id=?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, id);
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public PropertyValue get(int id) {
		String sql = "select * from propertyvalue where id=?";
		PropertyValue bean = null;
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new PropertyValue();
				bean.setId(id);
				Product product = new ProductDaoImpl().get(rs.getInt("pid"));
				Property property = new PropertyDaoImpl().get(rs.getInt("ptid"));
				bean.setProduct(product);
				bean.setProperty(property);
				bean.setValue(rs.getString("value"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	@Override
	public PropertyValue get(int ptid, int pid) {
		String sql = "select * from propertyvalue where pid=? and ptid=?";
		PropertyValue bean = null;
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, pid);
			pstmt.setInt(2, ptid);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new PropertyValue();
				bean.setId(rs.getInt(1));
				Product product = new ProductDaoImpl().get(pid);
				Property property = new PropertyDaoImpl().get(ptid);
				bean.setProduct(product);
				bean.setProperty(property);
				bean.setValue(rs.getString("value"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	@Override
	public List<PropertyValue> list() {
		return list(0, Short.MAX_VALUE);
	}

	@Override
	public List<PropertyValue> list(int pid) {
		String sql = "select * from propertyvalue where pid=? order by id desc";
		List<PropertyValue> list = new ArrayList<PropertyValue>();
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, pid);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				PropertyValue bean = new PropertyValue();
				bean.setId(rs.getInt(1));
				Product product = new ProductDaoImpl().get(pid);
				Property property = new PropertyDaoImpl().get(rs.getInt("ptid"));
				bean.setProduct(product);
				bean.setProperty(property);
				bean.setValue(rs.getString("value"));
				list.add(bean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<PropertyValue> list(int start, int count) {
		String sql = "select * from propertyvalue limit ?,?";
		List<PropertyValue> list = new ArrayList<PropertyValue>();
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, start);
			pstmt.setInt(2, count);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				PropertyValue bean = new PropertyValue();
				bean.setId(rs.getInt(1));
				Product product = new ProductDaoImpl().get(rs.getInt("pid"));
				Property property = new PropertyDaoImpl().get(rs.getInt("ptid"));
				bean.setProduct(product);
				bean.setProperty(property);
				bean.setValue(rs.getString("value"));
				list.add(bean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void init(Product product) {
		List<Property> properties = new PropertyDaoImpl().list(product.getCategory().getId());
		for (Property property : properties) {
			PropertyValue pv = get(property.getId(), product.getId());
			if (pv == null) {
				pv = new PropertyValue();
				pv.setProduct(product);
				pv.setProperty(property);
				add(pv);
			}
		}
	}

}
