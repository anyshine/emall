package cn.tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.tmall.bean.Order;
import cn.tmall.bean.OrderItem;
import cn.tmall.bean.Product;
import cn.tmall.bean.User;
import cn.tmall.util.DBUtil;

public class OrderItemDaoImpl implements OrderItemDao {
	@Override
	public int getTotal() {
		int total = 0;
		String sql = "select count(*) from orderitem";
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
	public void add(OrderItem bean) {
		String sql = "insert into orderitem values(null, ?, ?, ?, ?)";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);) {
			pstmt.setInt(1, bean.getProduct().getId());
			if (bean.getOrder() == null) {
				pstmt.setInt(2, -1);
			} else {
				pstmt.setInt(2, bean.getOrder().getId());
			}
			pstmt.setInt(3, bean.getUser().getId());
			pstmt.setInt(4, bean.getNumber());
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
	public void update(OrderItem bean) {
		String sql = "update orderitem set pid=?, oid=?, uid=?, number=? where id=?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, bean.getProduct().getId());
			if (bean.getOrder() == null) {
				pstmt.setInt(2, -1);
			} else {
				pstmt.setInt(2, bean.getOrder().getId());
			}
			pstmt.setInt(3, bean.getUser().getId());
			pstmt.setInt(4, bean.getNumber());
			pstmt.setInt(5, bean.getId());
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int id) {
		String sql = "delete from orderitem where id=?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, id);
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public OrderItem get(int id) {
		String sql = "select * from orderitem where id=?";
		OrderItem bean = null;
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new OrderItem();
				bean.setId(id);

				Product product = new ProductDaoImpl().get(rs.getInt("pid"));
				bean.setProduct(product);

				int oid = rs.getInt("oid");
				if (oid != -1) {
					Order order = new OrderDaoImpl().get(oid);
					bean.setOrder(order);
				}

				User user = new UserDaoImpl().get(rs.getInt("uid"));
				bean.setUser(user);

				bean.setNumber(rs.getInt("number"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	@Override
	public List<OrderItem> listByUser(int uid) {
		return listByUser(uid, 0, Short.MAX_VALUE);
	}

	@Override
	public List<OrderItem> listByUser(int uid, int start, int count) {
		String sql = "select * from orderitem where uid=? and oid=-1 limit ?,?";
		List<OrderItem> list = new ArrayList<OrderItem>();
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, uid);
			pstmt.setInt(2, start);
			pstmt.setInt(3, count);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderItem bean = new OrderItem();
				bean.setId(rs.getInt(1));

				Product product = new ProductDaoImpl().get(rs.getInt("pid"));
				bean.setProduct(product);

				int oid = rs.getInt("oid");
				if (oid != -1) {
					Order order = new OrderDaoImpl().get(oid);
					bean.setOrder(order);
				}

				User user = new UserDaoImpl().get(rs.getInt("uid"));
				bean.setUser(user);

				bean.setNumber(rs.getInt("number"));
				list.add(bean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<OrderItem> listByOrder(int oid) {
		return listByOrder(oid, 0, Short.MAX_VALUE);
	}

	@Override
	public List<OrderItem> listByOrder(int oid, int start, int count) {
		String sql = "select * from orderitem where oid=? limit ?,?";
		List<OrderItem> list = new ArrayList<OrderItem>();
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, oid);
			pstmt.setInt(2, start);
			pstmt.setInt(3, count);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderItem bean = new OrderItem();
				bean.setId(rs.getInt(1));

				Product product = new ProductDaoImpl().get(rs.getInt("pid"));
				bean.setProduct(product);

				if (oid != -1) {
					Order order = new OrderDaoImpl().get(oid);
					bean.setOrder(order);
				}

				User user = new UserDaoImpl().get(rs.getInt("uid"));
				bean.setUser(user);

				bean.setNumber(rs.getInt("number"));
				list.add(bean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<OrderItem> listByProduct(int pid) {
		return listByProduct(pid, 0, Short.MAX_VALUE);
	}

	@Override
	public List<OrderItem> listByProduct(int pid, int start, int count) {
		String sql = "select * from orderitem where pid=? limit ?,?";
		List<OrderItem> list = new ArrayList<OrderItem>();
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, pid);
			pstmt.setInt(2, start);
			pstmt.setInt(3, count);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderItem bean = new OrderItem();
				bean.setId(rs.getInt(1));

				Product product = new ProductDaoImpl().get(pid);
				bean.setProduct(product);

				int oid = rs.getInt("oid");
				if (oid != -1) {
					Order order = new OrderDaoImpl().get(oid);
					bean.setOrder(order);
				}

				User user = new UserDaoImpl().get(rs.getInt("uid"));
				bean.setUser(user);

				bean.setNumber(rs.getInt("number"));
				list.add(bean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void fill(List<Order> list) {
		for (Order order : list) {
			List<OrderItem> oiList = listByOrder(order.getId());
			float total = 0;
			int totalNumber = 0;
			for (OrderItem orderItem : oiList) {
				total += orderItem.getNumber()
						* orderItem.getProduct().getPromotePrice();
				totalNumber += orderItem.getNumber();
			}
			order.setOrderItems(oiList);
			order.setTotal(total);
			order.setTotalNumber(totalNumber);
		}
	}

	@Override
	public void fill(Order order) {
		List<OrderItem> oiList = listByOrder(order.getId());
		float total = 0;
		for (OrderItem orderItem : oiList) {
			total += orderItem.getNumber()
					* orderItem.getProduct().getPromotePrice();
		}
		order.setOrderItems(oiList);
		order.setTotal(total);
	}

	@Override
	public int getSaleCount(int pid) {
		int total = 0;
		String sql = "select sum(number) from orderitem where pid=?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, pid);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

}
