package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domain.WaitToDoDomain;
@Service("waitToDoDomain")
@Transactional
public class WaitToDoDomainImpl extends BaseDomainImpl implements WaitToDoDomain{
	public WaitToDoDomainImpl(){
		this.setClassName("com.starsoft.core.entity.WaitToDo");
	}
	
}
