package com.starsoft.core.domainImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.AppesAttributeDomain;
import com.starsoft.core.exception.OperateException;
@Transactional
@Service("appesAttributeDomain")
public class AppesAttributeDomainImpl extends BaseDomainImpl implements AppesAttributeDomain{
	public AppesAttributeDomainImpl(){
		this.setClassName("com.starsoft.core.entity.AppesAttribute");
	}

	@Override
	public Integer getMaxSortCodeByProperty(String propertyName,
			String propertyValue) {
		StringBuffer hql = new StringBuffer();
		hql.append("select max(tmp.sortCode) from "+ getBaseObject().getClass().getName()+" as tmp ");
		hql.append("where tmp."+propertyName+"='"+propertyValue+"'");
		List list = getHibernateTemplate().find(hql.toString());
		if(list!=null&&list.size()>0){
			if(list.get(0)!=null){
			    try{
			    	return Integer.parseInt(list.get(0).toString())+1;
			    }catch(Exception e){
			    	throw new OperateException("获取最大排序号出错!数字值:");
			    }
			}else{
				return 1;
			}
		}else{
			return 1;
		}
	}
	
}
