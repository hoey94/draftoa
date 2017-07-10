package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.GroupDomain;
@Service("groupDomain")
@Transactional
public class GroupDomainImpl extends BaseDomainImpl implements GroupDomain{
	public GroupDomainImpl(){
		this.setClassName("com.starsoft.core.entity.Group");
	}
	
}
