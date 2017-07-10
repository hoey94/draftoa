package com.starsoft.cms.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.cms.domain.LinkContentDomain;
import com.starsoft.core.domainImpl.BaseDomainImpl;
@Service("linkContentDomain")
public class LinkContentDomainImpl extends BaseDomainImpl implements LinkContentDomain{
	public LinkContentDomainImpl(){
		this.setClassName("com.starsoft.cms.entity.LinkContent");
	}
	
}
