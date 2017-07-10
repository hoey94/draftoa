package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.FansDomain;
@Service("fansDomain")
@Transactional
public class FansDomainImpl extends BaseDomainImpl implements FansDomain{
	public FansDomainImpl(){
		this.setClassName("com.starsoft.core.entity.Fans");
	}
	
}
