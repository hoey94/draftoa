package com.starsoft.cms.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.cms.domain.LinksDomain;
@Service("linksDomain")
@Transactional
public class LinksDomainImpl extends BaseDomainImpl implements LinksDomain{
	public LinksDomainImpl(){
		this.setClassName("com.starsoft.cms.entity.Links");
	}
	
}
