package com.starsoft.cms.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.cms.domain.AcquisitionRuleDomain;
@Service("acquisitionRuleDomain")
@Transactional
public class AcquisitionRuleDomainImpl extends BaseDomainImpl implements AcquisitionRuleDomain{
	public AcquisitionRuleDomainImpl(){
		this.setClassName("com.starsoft.cms.entity.AcquisitionRule");
	}
	
}
