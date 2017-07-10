package com.starsoft.oa.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import com.starsoft.oa.domain.LianReturnDomain;

/**
 * 
 * @Description 立案回复
 * @author 赵一好
 * @date 2016-11-18 上午11:02:40
 *
 */
@Service("lianReturnDomain")
public class LianReturnDomainImpl extends BaseDomainImpl implements LianReturnDomain {

	public LianReturnDomainImpl(){
		this.setClassName("com.starsoft.oa.entity.LianReturn");
	}
	
}
