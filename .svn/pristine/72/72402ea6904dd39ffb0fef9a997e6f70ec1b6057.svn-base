package com.starsoft.cms.domainImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import com.starsoft.core.exception.OperateException;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.cms.domain.ImagePlayRelationImagesDomain;
@Service("imagePlayRelationImagesDomain")
@Transactional
public class ImagePlayRelationImagesDomainImpl extends BaseDomainImpl implements ImagePlayRelationImagesDomain{
	/****
	 * 获取最大的排序号码+1
	 */
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public Integer getMaxSortCode() {
		StringBuffer hql = new StringBuffer();
		hql.append("select max(tmp.sortCode) from "+ getBaseObject().getClass().getName()+" as tmp ");
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
	public ImagePlayRelationImagesDomainImpl(){
		this.setClassName("com.starsoft.cms.entity.ImagePlayRelationImages");
	}
	
}
