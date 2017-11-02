package cn.tmall.dao;

import java.util.List;

import cn.tmall.bean.Property;

public interface PropertyDao {

	public int getTotal(int cid);

	public void add(Property bean);

	public void update(Property bean);

	public void delete(int id);

	public Property get(int id);

	public Property get(String name, int cid);

	public List<Property> list(int cid);

	public List<Property> list(int cid, int start, int count);

}