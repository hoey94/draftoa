package com.starsoft.core.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.FavouritesLinkDomain;
@Service("favouritesLinkDomain")
@Transactional
public class FavouritesLinkDomainImpl extends BaseDomainImpl implements FavouritesLinkDomain{
	public FavouritesLinkDomainImpl(){
		this.setClassName("com.starsoft.core.entity.FavouritesLink");
	}
	
}
