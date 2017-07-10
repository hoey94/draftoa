package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.SystemPropertyPersonDomain;
@Service("systemPropertyPersonDomain")
@Transactional
public class SystemPropertyPersonDomainImpl extends BaseDomainImpl implements SystemPropertyPersonDomain{
	public SystemPropertyPersonDomainImpl(){
		this.setClassName("com.starsoft.core.entity.SystemPropertyPerson");
	}
	
}
