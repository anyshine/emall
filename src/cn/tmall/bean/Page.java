package cn.tmall.bean;

public class Page {
	private int start;
	private int count;
	private int total;
	private String param;
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public Page(int start, int count) {
		super();
		this.start = start;
		this.count = count;
	}
	public boolean isHasPrevious() {
		return start != 0;
	}
	public boolean isHasNext() {
		return start != getLast();
	}
	public int getTotalPage() {
		int totalPage;
		if (total == 0) {
			return 1;
		}
		if (total % count == 0) {
			totalPage = total / count;
		} else {
			totalPage = total / count + 1;
		}
		return totalPage;
	}
	public int getLast() {
		int last;
		if (total == 0) {
			return 0;
		}
		if (total % count == 0) {
			last = total - count;
		} else {
			last = total - total % count;
		}
		return last;
	}
}
