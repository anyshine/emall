package cn.tmall.dao;

import java.util.List;

import cn.tmall.bean.Order;

public interface OrderDao {
	public int getTotal();
	public void add(Order bean);
	public void update(Order bean);
	public void delete(int id);
	public Order get(int id);
	public List<Order> list();
	public List<Order> list(int start, int count);
	public List<Order> list(int uid, String excluedeStatus);
	public List<Order> list(int uid, String excluedeStatus, int start, int count);
}
