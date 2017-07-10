package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domain.SSODomain;
@Service("ssoDomain")
@Transactional
public class SSODomainImpl extends BaseDomainImpl implements SSODomain{
	public SSODomainImpl(){
		this.setClassName("com.starsoft.core.entity.SSO");
	}

}
