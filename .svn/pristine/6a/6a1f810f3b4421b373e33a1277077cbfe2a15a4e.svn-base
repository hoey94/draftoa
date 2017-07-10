package com.starsoft.oa.domainImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import com.starsoft.oa.domain.LianReturnRecordDomain;
import com.starsoft.oa.domain.MotionDomain;
import com.starsoft.oa.entity.LianReturn;
import com.starsoft.oa.entity.LianReturnRecord;
import com.starsoft.oa.entity.Motion;

/**
 * 
 * @Description 立案回复记录表
 * @author 赵一好
 * @date 2016-11-18 上午11:58:19
 * 
 */
@Service("lianReturnRecordDomain")
public class LianReturnRecordDomainImpl extends BaseDomainImpl implements LianReturnRecordDomain {
	
	@Autowired
	private MotionDomain motionDomain;
	public LianReturnRecordDomainImpl() {
		this.setClassName("com.starsoft.oa.entity.LianReturnRecord");
	}

	@Transactional
	public void saveLaReturnRecAndUpdateMot(String motionId, String mark,
			LianReturnRecord lianReturnRecord)
			throws Exception {
		Motion motion = (Motion) motionDomain.query(motionId);
		this.save(lianReturnRecord);
		if(mark.equals("1")){
			// 设置议案状态为7
			motion.setStatus("7");
			motionDomain.update(motion);
		}else{
			motion.setStatus("8");
			motionDomain.update(motion);
		}
		
	}

}
