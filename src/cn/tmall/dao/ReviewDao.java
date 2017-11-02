package cn.tmall.dao;

import java.util.List;

import cn.tmall.bean.Review;

public interface ReviewDao {
	public int getTotal();
	public int getTotal(int pid);
	public void add(Review bean);
	public void update(Review bean);
	public void delete(int id);
	public Review get(int id);
	public List<Review> list(int pid);
	public List<Review> list(int pid, int start, int count);
	public boolean isExist(String content, int pid);
}
