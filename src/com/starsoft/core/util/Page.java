package com.starsoft.core.util;

/**
 * 
 * @author zhaosx
 */
public class Page {

    public static final int DEFAULT_PAGE_SIZE = 20;
    /**
     * 页面
     */
    private int pageIndex;
    /***
     * 每页大小
     */
    private int pageSize;
    /***
     * 数据总条数
     */
    private int totalCount;
    /***
     * 总页数
     */
    private int pageCount;
    
    private boolean hasPrevious;
    
    private boolean hasNext;
    /***
     * 初始化分页元素
     * @param pageIndex 第多少页
     * @param pageSize 每页大小
     */
    public Page(int pageIndex, int pageSize) {
        if(pageIndex<1)
            pageIndex = 1;
        if(pageSize<1)
            pageSize = 1;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }
    /***
     * 默认每页大小是10条
     * @param pageIndex 第多少页
     */
    public Page(int pageIndex) {
        this(pageIndex, DEFAULT_PAGE_SIZE);
    }
    //当前页
    public int getPageIndex() { return pageIndex; }
    // 每页大小
    public int getPageSize() { return pageSize; }
    //总页数
    public int getPageCount() { return pageCount; }
    //总数据条数
    public int getTotalCount() { return totalCount; }

    public int getFirstResult() { return (pageIndex-1)*pageSize; }
    //是否显示上一页
    public boolean getHasPrevious() { return pageIndex>1; }
    // 是否显示下一页
    public boolean getHasNext() { return pageIndex<pageCount; }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        pageCount = totalCount / pageSize + (totalCount%pageSize==0 ? 0 : 1);
        if(totalCount==0) {
        	this.pageIndex=1;
        }else {
            if(pageIndex>pageCount)  pageIndex=pageCount;
        }
    }
    public void setPageSize(int pageSize){
    	 this.pageSize = pageSize;
    }
    public boolean isEmpty() {
        return totalCount==0;
    }

	public void setHasPrevious(boolean hasPrevious) {
		this.hasPrevious = hasPrevious;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

}
