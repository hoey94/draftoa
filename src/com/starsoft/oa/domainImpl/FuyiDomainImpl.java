package com.starsoft.oa.domainImpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.oa.domain.FuyiDomain;

/**
 * 
 * @Description 附议
 * @author 赵一好
 * @date 2016-11-9 下午4:13:12
 * 
 */
@Service("fuyiDomain")
public class FuyiDomainImpl extends BaseDomainImpl implements FuyiDomain {

	public FuyiDomainImpl() {

		this.setClassName("com.starsoft.oa.entity.Fuyi");

	}
	
	public int findFuyiCount(String motionId, String mark) throws Exception {
		int num = 0;
		if(mark != null && !mark.equals("")){
			num = jdbcTemplate.queryForInt("select count(0) from t_oa_fuyiRecord where motionid = '"+motionId+"' and mark = '" + mark+"'");
		}else{
			num =  jdbcTemplate.queryForInt("select count(0) from t_oa_fuyi where motionid = '"+motionId+"'");
		}
		return num;
		
	}

	
}
