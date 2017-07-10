package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.JobPostDomain;
@Service("jobPostDomain")
@Transactional
public class JobPostDomainImpl extends BaseDomainImpl implements JobPostDomain{
	public JobPostDomainImpl(){
		this.setClassName("com.starsoft.core.entity.JobPost");
	}
	
}
