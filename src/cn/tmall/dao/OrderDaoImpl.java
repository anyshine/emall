package cn.tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.tmall.bean.Order;
import cn.tmall.bean.User;
import cn.tmall.util.DBUtil;
import cn.tmall.util.DateUtil;

public class OrderDaoImpl implements OrderDao {
	public static final String waitPay = "waitPay";
	public static final String waitDelivery = "waitDelivery";
	public static final String waitConfirm = "waitConfirm";
	public static final String waitReview = "waitReview";
	public static final String finish = "finish";
	public static final String delete = "delete";

	@Override
	public int getTotal() {
		int total = 0;
		String sql = "select count(*) from orders";
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
	public void add(Order bean) {
		String sql = "insert into orders values(null,?,?,?,?,?,?,?,?,?,?,?,?)";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);) {
			pstmt.setString(1, bean.getOrderCode());
			pstmt.setString(2, bean.getAddress());
			pstmt.setString(3, bean.getPost());
			pstmt.setString(4, bean.getReceiver());
			pstmt.setString(5, bean.getMobile());
			pstmt.setString(6, bean.getUserRemark());

			pstmt.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
			pstmt.setTimestamp(8, DateUtil.d2t(bean.getPayDate()));
			pstmt.setTimestamp(9, DateUtil.d2t(bean.getDeliveryDate()));
			pstmt.setTimestamp(10, DateUtil.d2t(bean.getConfirmDate()));
			pstmt.setInt(11, bean.getUser().getId());
			pstmt.setString(12, bean.getStatus());
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
	public void update(Order bean) {
		String sql = "update orders set address= ?, post=?, receiver=?,mobile=?, "
				+ "userRemark=?, createDate = ? , payDate =? , deliveryDate =?, "
				+ "confirmDate = ? , orderCode =?, uid=?, status=? where id = ?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, bean.getAddress());
			pstmt.setString(2, bean.getPost());
			pstmt.setString(3, bean.getReceiver());
			pstmt.setString(4, bean.getMobile());
			pstmt.setString(5, bean.getUserRemark());

			pstmt.setTimestamp(6, DateUtil.d2t(bean.getCreateDate()));
			pstmt.setTimestamp(7, DateUtil.d2t(bean.getPayDate()));
			pstmt.setTimestamp(8, DateUtil.d2t(bean.getDeliveryDate()));
			pstmt.setTimestamp(9, DateUtil.d2t(bean.getConfirmDate()));
			pstmt.setString(10, bean.getOrderCode());
			pstmt.setInt(11, bean.getUser().getId());
			pstmt.setString(12, bean.getStatus());
			pstmt.setInt(13, bean.getId());
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int id) {
		String sql = "delete from orders where id=?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, id);
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Order get(int id) {
		String sql = "select * from orders where id=?";
		Order bean = null;
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new Order();
				bean.setId(id);
				
				String orderCode =rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userRemark = rs.getString("userRemark");
                String status = rs.getString("status");
                User user = new UserDaoImpl().get(rs.getInt("uid"));
                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d( rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d( rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d( rs.getTimestamp("confirmDate"));
                
                bean.setOrderCode(orderCode);
                bean.setAddress(address);
                bean.setPost(post);
                bean.setReceiver(receiver);
                bean.setMobile(mobile);
                bean.setUserRemark(userRemark);
                bean.setCreateDate(createDate);
                bean.setPayDate(payDate);
                bean.setDeliveryDate(deliveryDate);
                bean.setConfirmDate(confirmDate);
                bean.setUser(user);
                bean.setStatus(status);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	@Override
	public List<Order> list() {
		return list(0, Short.MAX_VALUE);
	}

	@Override
	public List<Order> list(int start, int count) {
		String sql = "select * from orders limit ?,?";
		List<Order> list = new ArrayList<Order>();
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, start);
			pstmt.setInt(2, count);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Order bean = new Order();
				
				String orderCode =rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userRemark = rs.getString("userRemark");
                String status = rs.getString("status");
                User user = new UserDaoImpl().get(rs.getInt("uid"));
                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d( rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d( rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d( rs.getTimestamp("confirmDate"));
                
                bean.setId(rs.getInt(1));
                bean.setOrderCode(orderCode);
                bean.setAddress(address);
                bean.setPost(post);
                bean.setReceiver(receiver);
                bean.setMobile(mobile);
                bean.setUserRemark(userRemark);
                bean.setCreateDate(createDate);
                bean.setPayDate(payDate);
                bean.setDeliveryDate(deliveryDate);
                bean.setConfirmDate(confirmDate);
                bean.setUser(user);
                bean.setStatus(status);
				
				list.add(bean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Order> list(int uid, String excluedeStatus) {
		return list(uid, excluedeStatus, 0, Short.MAX_VALUE);
	}

	@Override
	public List<Order> list(int uid, String excluedeStatus, int start, int count) {
		String sql = "select * from orders where uid=? and status!=? limit ?,?";
		List<Order> list = new ArrayList<Order>();
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, uid);
			pstmt.setString(2, excluedeStatus);
			pstmt.setInt(3, start);
			pstmt.setInt(4, count);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Order bean = new Order();
				
				String orderCode =rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userRemark = rs.getString("userRemark");
                String status = rs.getString("status");
                User user = new UserDaoImpl().get(rs.getInt("uid"));
                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d( rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d( rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d( rs.getTimestamp("confirmDate"));
                
                bean.setId(rs.getInt(1));
                bean.setOrderCode(orderCode);
                bean.setAddress(address);
                bean.setPost(post);
                bean.setReceiver(receiver);
                bean.setMobile(mobile);
                bean.setUserRemark(userRemark);
                bean.setCreateDate(createDate);
                bean.setPayDate(payDate);
                bean.setDeliveryDate(deliveryDate);
                bean.setConfirmDate(confirmDate);
                bean.setUser(user);
                bean.setStatus(status);
				
				list.add(bean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
