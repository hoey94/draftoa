package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.LogInRecordDomain;
@Service("logInRecordDomain")
@Transactional
public class LogInRecordDomainImpl extends BaseDomainImpl implements LogInRecordDomain{
	public LogInRecordDomainImpl(){
		this.setClassName("com.starsoft.core.entity.LogInRecord");
	}
	
}
