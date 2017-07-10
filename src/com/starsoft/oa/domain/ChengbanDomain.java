package com.starsoft.oa.domain;

import com.starsoft.core.domain.BaseDomain;
import com.starsoft.oa.entity.Chengban;

/**
 * 
 * @Description 承办
 * @author 赵一好
 * @date 2016-11-14 下午2:20:43
 *
 */
public interface ChengbanDomain extends BaseDomain{

	public void saveChengbanAndRec(Chengban chengban, String motionId,String cbr,String createId) throws Exception;

	
}
