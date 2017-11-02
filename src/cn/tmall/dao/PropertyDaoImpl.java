package cn.tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.tmall.bean.Category;
import cn.tmall.bean.Property;
import cn.tmall.util.DBUtil;

public class PropertyDaoImpl implements PropertyDao {

	@Override
	public int getTotal(int cid) {
		int total = 0;
		String sql = "select count(*) from property where cid=" + cid;
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
	public void add(Property bean) {
		String sql = "insert into property values(null, ?, ?)";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);) {
			pstmt.setInt(1, bean.getCategory().getId());
			pstmt.setString(2, bean.getName());
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
	public void update(Property bean) {
		String sql = "update property set cid=?,name=? where id=?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, bean.getCategory().getId());
			pstmt.setString(2, bean.getName());
			pstmt.setInt(3, bean.getId());
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int id) {
		String sql = "delete from property where id=?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, id);
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Property get(int id) {
		String sql = "select * from property where id=?";
		Property bean = null;
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new Property();
				bean.setId(id);
				Category category = new CategoryDaoImpl().get(rs.getInt("cid"));
				bean.setCategory(category);
				bean.setName(rs.getString("name"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	@Override
	public Property get(String name, int cid) {
		String sql = "select * from property where name=? and cid=?";
		Property bean = null;
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, name);
			pstmt.setInt(2, cid);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new Property();
				bean.setId(rs.getInt(1));
				Category category = new CategoryDaoImpl().get(cid);
				bean.setCategory(category);
				bean.setName(name);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	@Override
	public List<Property> list(int cid) {
		return list(cid, 0, Short.MAX_VALUE);
	}

	@Override
	public List<Property> list(int cid, int start, int count) {
		String sql = "select * from property where cid=? limit ?,?";
		List<Property> list = new ArrayList<Property>();
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, cid);
			pstmt.setInt(2, start);
			pstmt.setInt(3, count);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Property bean = new Property();
				bean.setId(rs.getInt(1));
				Category category = new CategoryDaoImpl().get(cid);
				bean.setCategory(category);
				bean.setName(rs.getString("name"));
				list.add(bean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
