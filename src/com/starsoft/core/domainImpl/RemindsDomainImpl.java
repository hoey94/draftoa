package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.RemindsDomain;
@Service("remindsDomain")
@Transactional
public class RemindsDomainImpl extends BaseDomainImpl implements RemindsDomain{
	public RemindsDomainImpl(){
		this.setClassName("com.starsoft.core.entity.Reminds");
	}
	
}
