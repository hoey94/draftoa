package com.starsoft.oa.domainImpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import com.starsoft.core.util.StringUtil;
import com.starsoft.oa.domain.LianReturnDomain;
import com.starsoft.oa.domain.LianshenpiDomain;
import com.starsoft.oa.domain.MotionDomain;
import com.starsoft.oa.entity.LianReturn;
import com.starsoft.oa.entity.Lianshenpi;
import com.starsoft.oa.entity.Motion;
/**
 * 
 * @Description 立案审批
 * @author 赵一好
 * @date 2016-11-16 上午8:59:18
 *
 */
@Service("lianshenpiDomain")
public class LianshenpiDomainImpl extends BaseDomainImpl implements LianshenpiDomain{

	@Autowired
	private MotionDomain motionDomain;
	
	@Autowired
	private LianReturnDomain lianReturnDomain;
	
	public LianshenpiDomainImpl(){
		this.setClassName("com.starsoft.oa.entity.Lianshenpi");
	}

	@Transactional
	public void saveLianshenpiAndRec(Lianshenpi lianshenpi, String motionId,String mark) throws Exception {
		// 修改承办部门表中的状态为已读已修改2
		lianshenpi.setRead_index("2");
		this.update(lianshenpi);
		Motion motion = (Motion) motionDomain.query(motionId);
		if(mark.equals("1")){
			// 修改议案的状态为6
			motion.setStatus((Integer.parseInt(motion.getStatus()) + 1) + "");
			motionDomain.update(motion);
			// 向立案审批回复表中插入一条数据
			LianReturn lianReturn = new LianReturn();
			lianReturn.setMotionId(motionId);
			lianReturn.setTime(new Date());
			lianReturn.setTname("立案回复");
			lianReturn.setRead_index("0");
			lianReturnDomain.save(lianReturn);
		}else{
			motion.setStatus("8");
			motionDomain.update(motion);
		}
		
	}
	
}
