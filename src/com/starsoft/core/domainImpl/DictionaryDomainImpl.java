package com.starsoft.core.domainImpl;


import org.springframework.stereotype.Service;

import com.starsoft.core.domain.DictionaryDomain;
@Service("dictionaryDomain")
public class DictionaryDomainImpl extends BaseDomainImpl implements DictionaryDomain {
	public DictionaryDomainImpl(){
		this.setClassName("com.starsoft.core.entity.Dictionary");
	}

}
