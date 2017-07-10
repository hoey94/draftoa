package com.starsoft.cms.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.cms.domain.SpecialDomain;
@Service("specialDomain")
@Transactional
public class SpecialDomainImpl extends BaseDomainImpl implements SpecialDomain{
	public SpecialDomainImpl(){
		this.setClassName("com.starsoft.cms.entity.Special");
	}
	
}
