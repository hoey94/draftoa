package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.WaitToReadDomain;
@Service("waitToReadDomain")
@Transactional
public class WaitToReadDomainImpl extends BaseDomainImpl implements WaitToReadDomain{
	public WaitToReadDomainImpl(){
		this.setClassName("com.starsoft.core.entity.WaitToRead");
	}
	
}
