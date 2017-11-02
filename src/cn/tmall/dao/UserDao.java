package cn.tmall.dao;

import java.util.List;

import cn.tmall.bean.User;

public interface UserDao {
	public int getTotal();
	public void add(User user);
	public void update(User user);
	public void delete(int id);
	public User get(int id);
	public List<User> list();
	public List<User> list(int start, int count);
	public boolean isExist(String name);
	public User get(String name);
	public User get(String name, String password);
}
