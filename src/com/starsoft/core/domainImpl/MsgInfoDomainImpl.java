package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.MsgInfoDomain;
@Service("msgInfoDomain")
@Transactional
public class MsgInfoDomainImpl extends BaseDomainImpl implements MsgInfoDomain{
	public MsgInfoDomainImpl(){
		this.setClassName("com.starsoft.core.entity.MsgInfo");
	}
	
}
