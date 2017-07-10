package com.starsoft.oa.domainImpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import com.starsoft.oa.domain.ChengbanDomain;
import com.starsoft.oa.domain.ChengbanRecordDomain;
import com.starsoft.oa.domain.MotionDomain;
import com.starsoft.oa.entity.Chengban;
import com.starsoft.oa.entity.ChengbanRecord;
import com.starsoft.oa.entity.Motion;

/**
 * 
 * @Description 承办
 * @author 赵一好
 * @date 2016-11-14 下午2:21:24
 *
 */
@Service("chengbanDomain")
public class ChengbanDomainImpl extends BaseDomainImpl implements ChengbanDomain{

	@Autowired
	private MotionDomain motionDomain;
	
	@Autowired
	private ChengbanRecordDomain chengbanRecordDomain;
	
	public ChengbanDomainImpl(){
		this.setClassName("com.starsoft.oa.entity.Chengban");
	}

	@Transactional
	public void saveChengbanAndRec(Chengban chengban, String motionId,String cbr,String createId)
			throws Exception {
				// 修改承办表为2
				chengban.setRead_index((Integer.parseInt(chengban.getRead_index()) + 1) + "");
				this.update(chengban);

				// 修改议案的状态为4,表示为办理中
				Motion motion = (Motion) motionDomain.queryFirstByProperty("id",
						motionId);
				motion.setStatus((Integer.parseInt(motion.getStatus()) + 1) + "");
				motionDomain.update(motion);

				// 向承办回复表中插入一条数据
				ChengbanRecord chengbanRecord = new ChengbanRecord();
				chengbanRecord.setTname("承办回复");
				chengbanRecord.setRead_index("0");
				chengbanRecord.setTime(new Date());
				chengbanRecord.setContent("");
				chengbanRecord.setCreateId(createId);
				chengbanRecord.setValid(true);
				chengbanRecord.setCbr(cbr);
				chengbanRecord.setMotionId(motionId);
				chengbanRecordDomain.save(chengbanRecord);
		
	}
	
}
