package com.yepit.mapp.framework.dto.common;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * * 分页对象.<br>
 * 提供给外部系统的分页Bean<br>
 *
 * @param <T> 结果集的数据类型
 *            Created by qianlong on 2017/8/20.
 */
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 请求查询的页码
     */
    private int pageNum = 1;

    /**
     * 每页显示条数
     */
    private int pageSize;

    /**
     * 结果集
     */
    private List<T> rows;

    /**
     * 总条数
     */
    private long count = 0;

    /**
     * 总页数
     */
    private long pageCount;

    private long startRowIndex;
    private long endRowIndex;

    public PageResult(List<T> rows) {
        if (rows == null) {
            this.count = 0;
            this.pageSize = 0;
            this.pageCount = 0;
            this.rows = rows;
        } else {
            PageInfo pageInfo = new PageInfo(rows);
            this.count = pageInfo.getTotal();
            this.pageSize = pageInfo.getPageSize();
            this.pageCount = pageInfo.getPages();
            this.rows = rows;
        }
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    /**
     * 获取开始行
     *
     * @return
     * @author qianlong
     */
    public long getStartRowIndex() {
        startRowIndex = (this.getPageNum() - 1) * this.getPageSize();
        return startRowIndex;
    }

    /**
     * 获取结束行
     *
     * @return
     * @author qianlong
     */
    public long getEndRowIndex() {
        endRowIndex = this.getPageNum() * this.getPageSize();
        return endRowIndex;
    }

    /**
     * 获取最大页数
     *
     * @return
     * @author qianlong
     */
    public long getPageCount() {
        if (this.getCount() == 0 || this.getPageSize() == 0) {
            return 0;
        }
        long quotient = this.getCount() / this.getPageSize();
        long remainder = this.getCount() % this.getPageSize();
        pageCount = quotient;
        if (remainder > 0) {
            pageCount += 1;
        }
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

}
