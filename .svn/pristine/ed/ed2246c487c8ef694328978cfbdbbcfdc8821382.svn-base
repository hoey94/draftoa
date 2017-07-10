package com.starsoft.oa.domain;

import java.util.List;
import java.util.Map;

import com.starsoft.core.domain.BaseDomain;
import com.starsoft.core.util.Page;
import com.starsoft.oa.entity.Motion;

/**
 * 
 * @Description 议案
 * @author 赵一好
 * @date 2016-11-8 下午5:35:09
 * 
 */
public interface MotionDomain extends BaseDomain {

	public List<Motion> queryMotions() throws Exception;
	
	// 查询记录数
	public int queryCountForStatus(String status,String createId) throws Exception;
	
	// 条件查询所有信息
	public List<Motion> queryMotionsByDync(Map<String, String> map,Page page) throws Exception;
	
	// 根据状态查找出对应的motion
	public List<Motion> queryMotionsByStatus(String status,Page page) throws Exception;
	
	// 插入motion和记录
	public void saveMotAndRec(Motion motion,String createId,String fyr) throws Exception;
	
}
