package cn.tmall.dao;

import java.util.List;

import org.junit.Test;

import cn.tmall.bean.Category;

public class CategoryDaoImplTest {

	CategoryDao dao = new CategoryDaoImpl();
	
	@Test
	public void testGetTotal() {
		System.out.println(dao.getTotal());
	}

	@Test
	public void testAdd() {
		Category c = new Category();
		c.setName("服装");
		dao.add(c);
	}

	@Test
	public void testUpdate() {
		Category c = dao.get(2);
		c.setName("电器");
		dao.update(c);
	}

	@Test
	public void testDelete() {
		dao.delete(2);
	}

	@Test
	public void testGet() {
		System.out.println(dao.get(2));
	}

	@Test
	public void testList() {
		List<Category> list = dao.list();
		System.out.println(list);
	}

	@Test
	public void testListIntInt() {
		List<Category> list = dao.list(0, 1);
		System.out.println(list);
	}

}
