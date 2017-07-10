package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.AppesActionDomain;
@Service("appesActionDomain")
@Transactional
public class AppesActionDomainImpl extends BaseDomainImpl implements AppesActionDomain{
	public AppesActionDomainImpl(){
		this.setClassName("com.starsoft.core.entity.AppesAction");
	}
	
}
