package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.SignsDomain;
@Service("signsDomain")
@Transactional
public class SignsDomainImpl extends BaseDomainImpl implements SignsDomain{
	public SignsDomainImpl(){
		this.setClassName("com.starsoft.core.entity.Signs");
	}
	
}
