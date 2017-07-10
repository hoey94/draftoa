package com.starsoft.core.domainImpl;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.AppesActionPrivilegeDomain;
import com.starsoft.core.entity.Appes;
import com.starsoft.core.entity.AppesAction;
import com.starsoft.core.entity.AppesActionPrivilege;
@Service("appesActionPrivilegeDomain")
@Transactional
public class AppesActionPrivilegeDomainImpl extends BaseDomainImpl implements AppesActionPrivilegeDomain{
	public AppesActionPrivilegeDomainImpl(){
		this.setClassName("com.starsoft.core.entity.AppesActionPrivilege");
	}
	/***
	 * 为角色增加所有权限
	 * @param appes
	 * @param role
	 * @param defaultAction
	 */
	@Override
	public void addAllPrivilegeForRole(Appes appes, String role,
			String defaultAction) {
		DetachedCriteria criteria=DetachedCriteria.forClass(AppesActionPrivilege.class);
		criteria.add(Restrictions.eq("appes", appes.getId()));
		criteria.add(Restrictions.eq("roleId", role));
		List list=this.queryByCriteria(criteria);
		if(list.size()>0){
			int listsize=list.size();
			boolean hasCon=false;
			for(int t=0;t<listsize;t++){
				AppesActionPrivilege appesActionPrivilege=(AppesActionPrivilege)list.get(t);
				if(appesActionPrivilege.getTname().equals(defaultAction)) hasCon=true;
			}
			if(!hasCon){
				AppesActionPrivilege appesActionPrivilege=new AppesActionPrivilege();
				appesActionPrivilege.setAppes(appes.getId());
				appesActionPrivilege.setRoleId(role);
				appesActionPrivilege.setTname(defaultAction);
				appesActionPrivilege.setValid(true);
				appesActionPrivilege.setCreateId(appes.getCreateId());
				this.save(appesActionPrivilege);
			}
		}else{
			List<AppesAction> actionList=appes.getActionList();
			List saveList=new ArrayList();
			for(AppesAction action:actionList){
				AppesActionPrivilege appesActionPrivilege=new AppesActionPrivilege();
				appesActionPrivilege.setAppes(appes.getId());
				appesActionPrivilege.setRoleId(role);
				appesActionPrivilege.setTname(action.getTname());
				appesActionPrivilege.setValid(true);
				appesActionPrivilege.setCreateId(action.getCreateId());
				saveList.add(appesActionPrivilege);
			}
			this.save(saveList);
		}
	}
	/***
	 * 为角色去掉指定的操作权限
	 * @param appes
	 * @param role
	 * @param defaultAction
	 */
	@Override
	public void deletePrivilegeForRole(Appes appes,String role,String defaultAction) {
		DetachedCriteria criteria=DetachedCriteria.forClass(AppesActionPrivilege.class);
		criteria.add(Restrictions.eq("appes", appes.getId()));
		criteria.add(Restrictions.eq("roleId", role));
		if(!defaultAction.equals("list")){
			criteria.add(Restrictions.eq("tname", defaultAction));
		}
		List list=this.queryByCriteria(criteria);
		this.deleteAndSaveAndUpdate(list, null, null);
	}
	@Override
	public void addAllPrivilegeForRole(String appes, String role,
			String defaultAction) {
		DetachedCriteria criteria=DetachedCriteria.forClass(AppesActionPrivilege.class);
		criteria.add(Restrictions.eq("appes", appes));
		criteria.add(Restrictions.eq("roleId", role));
		criteria.add(Restrictions.eq("tname", defaultAction));
		List list=this.queryByCriteria(criteria);
		if(list.size()<1){
			AppesActionPrivilege appesActionPrivilege=new AppesActionPrivilege();
			appesActionPrivilege.setAppes(appes);
			appesActionPrivilege.setRoleId(role);
			appesActionPrivilege.setTname(defaultAction);
			appesActionPrivilege.setValid(true);
			appesActionPrivilege.setCreateId("");
			this.save(appesActionPrivilege);
		}
		
	}
	@Override
	public void deletePrivilegeForRole(String appes, String role,
			String defaultAction) {
		DetachedCriteria criteria=DetachedCriteria.forClass(AppesActionPrivilege.class);
		criteria.add(Restrictions.eq("appes", appes));
		criteria.add(Restrictions.eq("roleId", role));
		criteria.add(Restrictions.eq("tname", defaultAction));
		List list=this.queryByCriteria(criteria);
		this.deleteAndSaveAndUpdate(list, null, null);
	}
	@Override
	public boolean hasPrivilege(String appes, String action,
			List<String> roleIds) {
		DetachedCriteria criteria=DetachedCriteria.forClass(AppesActionPrivilege.class);
		criteria.add(Restrictions.eq("appes", appes));
		criteria.add(Restrictions.eq("tname", action));
		criteria.add(Restrictions.in("roleId", roleIds));
		List list=this.queryByCriteria(criteria);
		if(list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
}
