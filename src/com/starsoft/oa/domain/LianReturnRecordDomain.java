package com.starsoft.oa.domain;

import com.starsoft.core.domain.BaseDomain;
import com.starsoft.oa.entity.LianReturnRecord;

/**
 * 
 * @Description 立案回复记录表
 * @author 赵一好
 * @date 2016-11-18 上午11:57:36
 * 
 */
public interface LianReturnRecordDomain extends BaseDomain {

	public void saveLaReturnRecAndUpdateMot(String motionId, String mark,
			LianReturnRecord lianReturnRecord) throws Exception;

}
