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
import com.starsoft.oa.domain.QianpiRecordDomain;
import com.starsoft.oa.entity.Chengban;
import com.starsoft.oa.entity.Motion;
import com.starsoft.oa.entity.Qianpi;
import com.starsoft.oa.entity.QianpiRecord;

/**
 * 
 * @Description 签批记录
 * @author 赵一好
 * @date 2016-11-11 上午11:25:02
 * 
 */
@Service("qianpiRecordDomain")
public class QianpiRecordDomainImpl extends BaseDomainImpl implements
		QianpiRecordDomain {
	@Autowired
	private MotionDomain motionDomain;
	@Autowired
	private QianpiDomain qianpiDomain;

	@Autowired
	private ChengbanDomain chengbanDomain;

	public QianpiRecordDomainImpl() {
		this.setClassName("com.starsoft.oa.entity.QianpiRecord");
	}

	@Transactional
	public void saveQianpiRecord(QianpiRecord qianpiRecord, String mark,
			String motionId,String createId) throws Exception {

		this.save(qianpiRecord);
		
		// 判断mark的值，如果是1，表示签批人同意了需要修改议案的状态为通过签批，如果是0，表示签批人未通过，不需要任何改动
		if ("1".equals(mark)) {
			Motion motion = (Motion) motionDomain.query(qianpiRecord
					.getMotionId());
			// 3表示通过签批
			motion.setStatus((Integer.parseInt(motion.getStatus()) + 1) + "");
			motionDomain.update(motion);
			
			// 向承办表中插入一条信息
			Chengban chengban = new Chengban();
			chengban.setRead_index("0");
			chengban.setTime(new Date());
			chengban.setTname("承办");
			chengban.setMotionId(motionId);
			chengban.setCreateId(createId);
			chengban.setValid(true);
			chengbanDomain.save(chengban);
		} else {
			Motion motion = (Motion) motionDomain.query(qianpiRecord
					.getMotionId());
			// 1表示通过签批
			motion.setStatus((Integer.parseInt(motion.getStatus()) - 1) + "");
			motionDomain.update(motion);
		}
		
		DetachedCriteria criteria = qianpiDomain.getCriteria(null);
		criteria.add(Restrictions.eq("motionid", motionId));
		List queryByCriteria = qianpiDomain.queryByCriteria(criteria);
		Qianpi qianpi = (Qianpi) queryByCriteria.get(0);
		qianpi.setMark("2");
		qianpiDomain.update(qianpi);


	}
}
