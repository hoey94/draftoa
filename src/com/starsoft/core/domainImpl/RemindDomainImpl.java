package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.RemindDomain;
@Service("remindDomain")
@Transactional
public class RemindDomainImpl extends BaseDomainImpl implements RemindDomain{
	public RemindDomainImpl(){
		this.setClassName("com.starsoft.core.entity.Remind");
	}
	
}
