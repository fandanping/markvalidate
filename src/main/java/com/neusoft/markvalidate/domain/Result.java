package com.neusoft.markvalidate.domain;

import java.util.List;

/**
 * 
 * <p>[描述信息：返回值为result实体类]</p>
 *
 * @author 范丹平
 * @mail fandp@neusoft.com
 * @version 1.0 Created on 2018-10-12 上午08:43:48
 */
public class Result {
	//排序后的文献列表
	private List<SortData> sortDataList;
	//检索式命中条数
    private int hitNum=-1;
    //案卷对比文献出现在列表中的位置
    private int location_com = -1;
}
