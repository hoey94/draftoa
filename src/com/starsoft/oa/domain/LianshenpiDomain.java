package com.starsoft.oa.domain;

import com.starsoft.core.domain.BaseDomain;
import com.starsoft.oa.entity.Lianshenpi;

/**
 * 
 * @Description 立案审批
 * @author 赵一好
 * @date 2016-11-16 上午8:58:38
 *
 */
public interface LianshenpiDomain extends BaseDomain{

	public void saveLianshenpiAndRec(Lianshenpi lianshenpi, String motionId,String mark) throws Exception;

	
	
}
