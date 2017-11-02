package cn.tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.tmall.bean.Product;
import cn.tmall.bean.Review;
import cn.tmall.bean.User;
import cn.tmall.util.DBUtil;
import cn.tmall.util.DateUtil;

public class ReviewDaoImpl implements ReviewDao {

	@Override
	public int getTotal() {
		int total = 0;
		String sql = "select count(*) from review";
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
	public int getTotal(int pid) {
		int total = 0;
		String sql = "select count(*) from review where pid=" + pid;
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
	public void add(Review bean) {
		String sql = "insert into review values(null,?,?,?,?)";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);) {
			pstmt.setString(1, bean.getContent());
			pstmt.setInt(2, bean.getUser().getId());
			pstmt.setInt(3, bean.getProduct().getId());
			pstmt.setTimestamp(4, DateUtil.d2t(bean.getCreateDate()));
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
	public void update(Review bean) {
		String sql = "update review set content=?, cid=?, pid=? createDate=? where id=?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, bean.getContent());
			pstmt.setInt(2, bean.getUser().getId());
			pstmt.setInt(3, bean.getProduct().getId());
			pstmt.setTimestamp(4, DateUtil.d2t(bean.getCreateDate()));
			pstmt.setInt(5, bean.getId());
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int id) {
		String sql = "delete from review where id=?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, id);
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Review get(int id) {
		String sql = "select * from review where id=?";
		Review bean = null;
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new Review();
				bean.setId(id);
				bean.setContent(rs.getString("content"));
				User user = new UserDaoImpl().get(rs.getInt("uid"));
				Product product = new ProductDaoImpl().get(rs.getInt("pid"));
				bean.setUser(user);
				bean.setProduct(product);
				bean.setCreateDate(rs.getTimestamp("createDate"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	@Override
	public List<Review> list(int pid) {
		return list(pid, 0, Short.MAX_VALUE);
	}

	@Override
	public List<Review> list(int pid, int start, int count) {
		String sql = "select * from review where pid=? limit ?,?";
		List<Review> list = new ArrayList<Review>();
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, pid);
			pstmt.setInt(2, start);
			pstmt.setInt(3, count);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Review bean = new Review();
				bean.setId(rs.getInt(1));
				bean.setContent(rs.getString("content"));
				User user = new UserDaoImpl().get(rs.getInt("uid"));
				Product product = new ProductDaoImpl().get(pid);
				bean.setUser(user);
				bean.setProduct(product);
				bean.setCreateDate(rs.getTimestamp("createDate"));
				list.add(bean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean isExist(String content, int pid) {
		String sql = "select * from review where pid=? and content=?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, pid);
			pstmt.setString(2, content);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
