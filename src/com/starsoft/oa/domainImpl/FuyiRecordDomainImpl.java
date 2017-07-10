package com.starsoft.oa.domainImpl;

import java.text.DecimalFormat;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import com.starsoft.oa.domain.FuyiDomain;
import com.starsoft.oa.domain.FuyiRecordDomain;
import com.starsoft.oa.domain.MotionDomain;
import com.starsoft.oa.entity.Fuyi;
import com.starsoft.oa.entity.FuyiRecord;
import com.starsoft.oa.entity.Motion;

/**
 * 
 * @Description 附议记录
 * @author 赵一好
 * @date 2016-11-10 下午2:01:57
 * 
 */
@Service("fuyiRecordDomain")
public class FuyiRecordDomainImpl extends BaseDomainImpl implements FuyiRecordDomain {
	
	@Autowired
	private FuyiDomain fuyiDomain;
	
	@Autowired
	private MotionDomain motionDomain;
	
	public FuyiRecordDomainImpl() {
		this.setClassName("com.starsoft.oa.entity.FuyiRecord");
	}

	@Override
	public void saveFyRecAndUpdateMot(FuyiRecord fyRec,Motion motion,String createId) throws Exception {
		
		this.save(fyRec);
		
		// 修改附议表的状态为已读已处理
		DetachedCriteria criteria2 = fuyiDomain.getCriteria(null);
		criteria2.add(Restrictions.eq("motionid", motion.getId()));
		criteria2.add(Restrictions.eq("fyr", createId));
		List<Fuyi> fys = fuyiDomain.queryByCriteria(criteria2);
		if(fys.size()>0){
			Fuyi fy = fys.get(0);
			fy.setMark("2");
			fuyiDomain.update(fy);
		}
		
		/* motion.setMark("2"); */
		// 如果该提案的附议人数超过附议人大于2人就修改议案的状态为允许上报，否则什么都不干
		// 查找附议记录表中该议案通过的数量
		DetachedCriteria criteria = this.getCriteria(null);
		criteria.add(Restrictions.eq("mark", "1"));
		criteria.add(Restrictions.eq("motionId", motion.getId()));
		List lists = this.queryByCriteria(criteria);
		double count = 0;
		if (lists.size() > 0) {
			count = lists.size();
		}
		double i = 3;
		DecimalFormat dec = new DecimalFormat("0.0000");
		float parseFloat = Float.parseFloat((dec.format(count / i)));
		float b = 0.666f;
		if (parseFloat >= b) {
			// 更新motion的状态
			motion.setStatus("1");
		}
		motionDomain.update(motion);
		
	}


}
