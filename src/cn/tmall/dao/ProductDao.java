package cn.tmall.dao;

import java.util.List;

import cn.tmall.bean.Category;
import cn.tmall.bean.Product;

public interface ProductDao {

	public int getTotal(int cid);

	public void add(Product bean);

	public void update(Product bean);

	public void delete(int id);

	public Product get(int id);

	public List<Product> list(int cid);

	public List<Product> list(int cid, int start, int count);

	public List<Product> list();

	public List<Product> list(int start, int count);

	public void fill(List<Category> list);

	public void fill(Category category);

	public void fillByRow(List<Category> list);

	public void setFirstProductImage(Product p);

	public void setSaleAndReviewNumber(Product p);

	public void setSaleAndReviewNumber(List<Product> list);

	public List<Product> search(String keyword, int start, int count);

}