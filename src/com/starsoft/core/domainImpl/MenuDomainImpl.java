package com.starsoft.core.domainImpl;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.starsoft.core.domain.MenuDomain;
import com.starsoft.core.entity.BaseTreeObject;
import com.starsoft.core.entity.Menu;
import com.starsoft.core.util.Page;
@Service("menuDomain")
public class MenuDomainImpl extends BaseTreeDomainImpl implements MenuDomain{
	public MenuDomainImpl(){
		this.setClassName("com.starsoft.core.entity.Menu");
	}

	@Override
	public List<BaseTreeObject> querySubTreeByParentIdAndRoles(String parentId,
			List<String> roles) {
		List result=new ArrayList();
		List params=new ArrayList();
		params.add(parentId);
		String param="";
		if(roles.size()<1){
			return result;
		} else{
			for(String str:roles){
				param+="'"+str+"',";
			}
			param=param.substring(0, param.length()-1);
		}
		String hql="select distinct menu from Menu as menu,MenuPrivilege as p where menu.id=p.menuId and p.valid=true and menu.valid=true and menu.parentId=? " +
				"and p.roleId in("+param+") order by menu.sortCode asc";
		Page page=new Page(0,100);
		result=this.queryByHql(hql, params, page);
		return result;
	}

	@Override
	public List<BaseTreeObject> querySubTreesByParentIdAndRoles(String parentId,
			List<String> roles) {
		List result=new ArrayList();
		List<BaseTreeObject> list=this.querySubTreeByParentIdAndRoles(parentId, roles);
		for(int t=0;t<list.size();t++){
			Menu obj=(Menu) list.get(t);
			obj.setSubset(querySubTreesByParentIdAndRoles(obj.getId(), roles));
			result.add(obj);
		}
		return result;
	}

}
