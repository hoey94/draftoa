package com.starsoft.core.domainImpl;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.RoleUsers;
import com.starsoft.core.entity.Users;
import com.starsoft.core.exception.OperateException;
@Service("usersDomain")
public class UsersDomainImpl extends BaseDomainImpl implements UsersDomain {
	public UsersDomainImpl(){
		this.setClassName("com.starsoft.core.entity.Users");
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public List<String> getUserIdsByOrganId(String organId) {
		@SuppressWarnings("rawtypes")
		List<String> result=new ArrayList();
		List<Users> list=(List<Users>) this.queryByCriteria(this.getCriteria(true).add(Restrictions.eq("organId", organId)));
		for(Users obj:list){
			result.add(obj.getId());
		}
		return result;
	}

	@Override
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
