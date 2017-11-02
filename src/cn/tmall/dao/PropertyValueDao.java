package cn.tmall.dao;

import java.util.List;

import cn.tmall.bean.Product;
import cn.tmall.bean.PropertyValue;

public interface PropertyValueDao {
	public int getTotal();
	public void add(PropertyValue bean);
	public void update(PropertyValue bean);
	public void delete(int id);
	public PropertyValue get(int id);
	public PropertyValue get(int ptid, int pid);
	public List<PropertyValue> list();
	public List<PropertyValue> list(int pid);
	public List<PropertyValue> list(int start, int count);
	public void init(Product product);
}
