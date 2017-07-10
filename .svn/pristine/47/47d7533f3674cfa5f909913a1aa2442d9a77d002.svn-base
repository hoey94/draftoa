package com.starsoft.oa.domainImpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.oa.domain.ChengbanRecordDomain;
import com.starsoft.oa.domain.LianshenpiDomain;
import com.starsoft.oa.domain.MotionDomain;
import com.starsoft.oa.entity.ChengbanRecord;
import com.starsoft.oa.entity.Lianshenpi;
import com.starsoft.oa.entity.Motion;


/**
 * 
 * @Description 承办部门领导回复
 * @author 赵一好
 * @date 2016-11-15 上午8:49:49
 *
 */
@Service("chengbanRecordDomain")
public class ChengbanRecordDomainImpl extends BaseDomainImpl implements ChengbanRecordDomain{

	@Autowired
	private MotionDomain motionDomain;
	
	@Autowired
	private LianshenpiDomain lianshenpiDomain;
	
	public ChengbanRecordDomainImpl(){
		this.setClassName("com.starsoft.oa.entity.ChengbanRecord");
	}

	@Transactional
	public void saveCbRecAndUpdateMot(String motionId, String mark,
			ChengbanRecord chengbanRecord,String createId) throws Exception {
		// 修改承办部门表中的状态为已读已修改2
		chengbanRecord.setRead_index("2");
		this.update(chengbanRecord);
		if(mark.equals("1")){
			// 修改议案状态为5 表示立案中
			Motion motion = (Motion) motionDomain.query(motionId);
			if (motion != null) {
				motion.setStatus("5");
				motionDomain.update(motion);
			}

			// 向立案表中插入一条数据
			Lianshenpi lianshenpi = new Lianshenpi();
			lianshenpi.setTname("立案");
			lianshenpi.setMotionId(motionId);
			lianshenpi.setTime(new Date());
			lianshenpi.setRead_index("0");
			lianshenpi.setCreateId(createId);
			lianshenpi.setValid(true);
			lianshenpiDomain.save(lianshenpi);
		}else{
			Motion motion = (Motion) motionDomain.queryFirstByProperty("id", motionId);
			motion.setStatus("8");
			motionDomain.update(motion);
		}
		
	}
	
}
