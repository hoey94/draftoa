package com.starsoft.cms.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.cms.domain.VisterHistoryDomain;
@Service("visterHistoryDomain")
@Transactional
public class VisterHistoryDomainImpl extends BaseDomainImpl implements VisterHistoryDomain{
	public VisterHistoryDomainImpl(){
		this.setClassName("com.starsoft.cms.entity.VisterHistory");
	}
	
}
