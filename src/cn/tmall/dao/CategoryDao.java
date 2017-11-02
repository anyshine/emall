package cn.tmall.dao;

import java.util.List;

import cn.tmall.bean.Category;

public interface CategoryDao {
	public int getTotal();
	public void add(Category category);
	public void update(Category category);
	public void delete(int id);
	public Category get(int id);
	public List<Category> list();
	public List<Category> list(int start, int count);
}
