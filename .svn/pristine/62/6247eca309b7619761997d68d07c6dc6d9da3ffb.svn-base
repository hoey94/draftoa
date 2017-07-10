package com.starsoft.oa.domainImpl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import com.starsoft.oa.domain.ChengbanDomain;
import com.starsoft.oa.domain.MotionDomain;
import com.starsoft.oa.domain.QianpiDomain;
import com.starsoft.oa.entity.Chengban;
import com.starsoft.oa.entity.Motion;
import com.starsoft.oa.entity.Qianpi;
import com.starsoft.oa.entity.QianpiRecord;

/**
 * 
 * @Description 签批
 * @author 赵一好
 * @date 2016-11-11 上午11:19:20
 * 
 */
@Service("qianpiDomain")
public class QianpiDomainImpl extends BaseDomainImpl implements QianpiDomain {
	@Autowired
	private MotionDomain motionDomain;
	
	
	public QianpiDomainImpl() {
		this.setClassName("com.starsoft.oa.entity.Qianpi");
	}

	@Transactional
	public void saveQpAndUpdateMot(Qianpi qianpi, String motionId)
			throws Exception {
		
		this.save(qianpi);
		
		// 修改议案的状态
		Motion motion = (Motion) motionDomain.query(motionId);
		// 2表示签批中
		motion.setStatus((Integer.parseInt(motion.getStatus())+1)+"");
		motionDomain.update(motion);
		
	}
	
	
}
