package com.starsoft.cms.domainImpl;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.cms.domain.ArticleTemplateDomain;
@Service("articleTemplateDomain")
@Transactional
public class ArticleTemplateDomainImpl extends BaseDomainImpl implements ArticleTemplateDomain{
	public ArticleTemplateDomainImpl(){
		this.setClassName("com.starsoft.cms.entity.ArticleTemplate");
	}
	
}
