package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.HelpGuideDomain;
@Service("helpGuideDomain")
@Transactional
public class HelpGuideDomainImpl extends BaseDomainImpl implements HelpGuideDomain{
	public HelpGuideDomainImpl(){
		this.setClassName("com.starsoft.core.entity.HelpGuide");
	}
	
}
