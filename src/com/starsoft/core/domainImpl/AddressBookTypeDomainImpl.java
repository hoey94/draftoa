package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.AddressBookTypeDomain;
@Service("addressBookTypeDomain")
@Transactional
public class AddressBookTypeDomainImpl extends BaseTreeDomainImpl implements AddressBookTypeDomain{
	public AddressBookTypeDomainImpl(){
		this.setClassName("com.starsoft.core.entity.AddressBookType");
	}
	
}
