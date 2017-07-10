package com.starsoft.cms.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.cms.domain.AcquisitionHistoryDomain;
@Service("acquisitionHistoryDomain")
@Transactional
public class AcquisitionHistoryDomainImpl extends BaseDomainImpl implements AcquisitionHistoryDomain{
	public AcquisitionHistoryDomainImpl(){
		this.setClassName("com.starsoft.cms.entity.AcquisitionHistory");
	}
	
}
