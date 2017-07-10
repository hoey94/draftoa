package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.PrivilegeDomain;
@Service("privilegeDomain")
@Transactional
public class PrivilegeDomainImpl extends BaseDomainImpl implements PrivilegeDomain{
	public PrivilegeDomainImpl(){
		this.setClassName("com.starsoft.core.entity.Privilege");
	}
	
}
