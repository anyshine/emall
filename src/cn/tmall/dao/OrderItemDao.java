package cn.tmall.dao;

import java.util.List;

import cn.tmall.bean.Order;
import cn.tmall.bean.OrderItem;

public interface OrderItemDao {

	public int getTotal();

	public void add(OrderItem bean);

	public void update(OrderItem bean);

	public void delete(int id);

	public OrderItem get(int id);

	public List<OrderItem> listByUser(int uid);

	public List<OrderItem> listByUser(int uid, int start, int count);

	public List<OrderItem> listByOrder(int oid);

	public List<OrderItem> listByOrder(int oid, int start, int count);

	public List<OrderItem> listByProduct(int pid);

	public List<OrderItem> listByProduct(int pid, int start, int count);

	public void fill(List<Order> list);

	public void fill(Order order);

	public int getSaleCount(int pid);

}