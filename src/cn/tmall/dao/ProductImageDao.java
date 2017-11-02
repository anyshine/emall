package cn.tmall.dao;

import java.util.List;

import cn.tmall.bean.Product;
import cn.tmall.bean.ProductImage;

public interface ProductImageDao {
	public int getTotal();
	public void add(ProductImage bean);
	public void update(ProductImage bean);
	public void delete(int id);
	public ProductImage get(int id);
	public List<ProductImage> list(Product product, String type);
	public List<ProductImage> list(Product product, String type, int start, int count);
}
