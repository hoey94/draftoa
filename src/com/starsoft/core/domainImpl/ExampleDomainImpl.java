package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.ExampleDomain;
@Service("exampleDomain")
@Transactional
public class ExampleDomainImpl extends BaseDomainImpl implements ExampleDomain{
	public ExampleDomainImpl(){
		this.setClassName("com.starsoft.core.entity.Example");
	}
	
}
