package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domain.SystemLogDomain;
@Service("systemLogDomain")
@Transactional
public class SystemLogDomainImpl extends BaseDomainImpl implements SystemLogDomain{
	public SystemLogDomainImpl(){
		this.setClassName("com.starsoft.core.entity.SystemLog");
	}
	
}
