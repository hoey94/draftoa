package com.starsoft.core.domainImpl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domain.MenuPrivilegeDomain;
import com.starsoft.core.entity.Menu;
import com.starsoft.core.entity.MenuPrivilege;
@Service("menuPrivilegeDomain")
@Transactional
public class MenuPrivilegeDomainImpl extends BaseDomainImpl implements MenuPrivilegeDomain {
	public MenuPrivilegeDomainImpl(){
		this.setClassName("com.starsoft.core.entity.MenuPrivilege");
	}
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void updatePrivilege(String menuid, List<String> roleids) {
		List selectList=this.queryByProperty("menu.id", menuid);
		for(int t=0;t<selectList.size();t++){
			MenuPrivilege privilege=(MenuPrivilege)selectList.get(t);
			this.delete(privilege);
		}
		if(roleids.size()>0){
			Menu menu=(Menu) this.getHibernateTemplate().get(Menu.class, menuid);
			for(int t=0;t<roleids.size();t++){
				String roleId=(String) roleids.get(t);
				MenuPrivilege privilege=(MenuPrivilege) this.getBaseObject();
				privilege.setMenuId(menuid);
				privilege.setRoleId(roleId);
				privilege.setTname(menu.getTname());
				privilege.setValid(true);
				this.getHibernateTemplate().save(privilege);
			}
		}
	}
	@Override
	public boolean queryPrivilegeByMenuIdAndRoleIds(String menuid,
			List<String> roleids) {
		if(roleids.size()<1){ return false;}else{
			List list=this.queryByCriteria(this.getCriteria(true).add(Restrictions.eq("menuId", menuid)).add(Restrictions.in("roleId", roleids)));
			if(list.size()>0){
				return true;
			}else{
				return false;
			}
		}
	}
}
