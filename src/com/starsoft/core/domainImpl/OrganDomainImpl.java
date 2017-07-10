package com.starsoft.core.domainImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.starsoft.core.domain.OrganDomain;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.BaseTreeObject;
import com.starsoft.core.entity.Organ;
import com.starsoft.core.entity.OrganAdministrator;
import com.starsoft.core.entity.Users;
@Service("organDomain")
public class OrganDomainImpl extends BaseTreeDomainImpl implements OrganDomain{
	@Override
	public List<BaseTreeObject> queryTreeByParentId(String parentId,Integer level,Boolean valid,Boolean order) {
		List<BaseTreeObject> result=super.queryTreeByParentId(parentId, level, valid, order);	
		return result;
	}

	public OrganDomainImpl(){
		this.setClassName("com.starsoft.core.entity.Organ");
	}
	/***
	 * 保存对象
	 */
	public void save(BaseObject entity){
		super.save(entity);
	}
	/**
	 *更新对象
	 */
	@Override
	public void update(BaseObject entity){
		super.update(entity);
	}
	/**
	 * 删除对象
	 */
	@Override
	public void delete(BaseObject entity){
		super.delete(entity);
	}
	@Override
	public List<Users> queryOrganAdministrator(String organId) {
		DetachedCriteria criteria=DetachedCriteria.forClass(OrganAdministrator.class);
		criteria.add(Restrictions.eq("organId", organId));
		List<OrganAdministrator> list=this.getHibernateTemplate().findByCriteria(criteria);
		List<Users> result=new ArrayList<Users>();
		List<String> usersIds=new ArrayList<String>();
		for(OrganAdministrator organAdministrator:list){
			usersIds.add(organAdministrator.getUsersId());
		}
		if(usersIds.size()>0){
			DetachedCriteria usersCriteria=DetachedCriteria.forClass(Users.class);
			usersCriteria.add(Restrictions.in("id", usersIds));
			result=this.getHibernateTemplate().findByCriteria(usersCriteria);
		}
		return result;
	}
	/****
	 * 更新部门管理员
	 * @param organId
	 */
	public void updateOrganAdministrator(String organId,List<String> userIds){
		DetachedCriteria criteria=DetachedCriteria.forClass(OrganAdministrator.class);
		criteria.add(Restrictions.eq("organId", organId));
		List<OrganAdministrator> list=this.getHibernateTemplate().findByCriteria(criteria);
		List<String> parentOrganId=this.queryParentIdPath(organId);
		Map<String,String> users=new HashMap<String,String>();
		for(String str:userIds){
			DetachedCriteria usercriteria=DetachedCriteria.forClass(OrganAdministrator.class);
			usercriteria.add(Restrictions.in("organId", parentOrganId));
			usercriteria.add(Restrictions.eq("usersId", str));
			List<OrganAdministrator> parentlist=this.getHibernateTemplate().findByCriteria(usercriteria);
			if(parentlist.size()<1){
				users.put(str, str);
			}
		}
		//1.检出需要新建的
		List saveList=new ArrayList();
		//2.检出需要删除的
		List deleteList=new ArrayList();
		for(OrganAdministrator organAdministrator:list){
			String userId=organAdministrator.getUsersId();
			if(users.containsKey(userId)){//已经包含了
				users.remove(userId);
			}else{
				deleteList.add(organAdministrator);
			}
		}
		for(Entry<String,String> entry:users.entrySet()){
			String objValue=entry.getValue();
			OrganAdministrator newOrganAdmin=new OrganAdministrator();
			newOrganAdmin.setOrganId(organId);
			Users user=this.getHibernateTemplate().get(Users.class, objValue);
			if(user!=null){
				newOrganAdmin.setUsersId(objValue);
				newOrganAdmin.setValid(true);
				newOrganAdmin.setTname(organId);
				saveList.add(newOrganAdmin);
			}
		 }
		this.deleteAndSaveAndUpdate(deleteList, saveList,null);
	}

	@Override
	public List<String> queryParentIdPath(String organId) {
		List<String> result=new ArrayList<String>();
		Organ organ=this.getHibernateTemplate().get(Organ.class, organId);
		if(organ!=null){
			String parentId=organ.getParentId();
			result.add(parentId);
			while(parentId!=null&&!parentId.equals("11111111111111111111111111111111")){
				Organ parentOrgan=this.getHibernateTemplate().get(Organ.class, parentId);
				if(parentOrgan!=null){
					parentId=parentOrgan.getParentId();
					result.add(parentId);
				}else{
					break;
				}
			}
		}
		return result;
	}

	@Override
	public String queryTopAdministratorOrgan(String userId) {
		String result="";
		DetachedCriteria criteria=DetachedCriteria.forClass(OrganAdministrator.class);
		criteria.add(Restrictions.eq("usersId", userId));
		List<OrganAdministrator> list=this.getHibernateTemplate().findByCriteria(criteria);
		if(list.size()>0){
			OrganAdministrator organAdministrator=list.get(0);
			result=organAdministrator.getOrganId();
		}
		return result;
	}
	/****
	 * 检查用户是否具备修改一个机构的权利
	 * @param organId
	 * @param userId
	 * @return
	 */
	@Override
	public boolean canEditOrgan(String organId, String userId) {
		List suborganlist=this.queryByParentId(organId, null, true);
		if(suborganlist.size()>0) return false;
		if("admin".equals(userId)){
			return true;
		}else{
			DetachedCriteria criteria=DetachedCriteria.forClass(OrganAdministrator.class);
			criteria.add(Restrictions.eq("usersId", userId));
			criteria.add(Restrictions.eq("organId", organId));
			List<OrganAdministrator> list=this.getHibernateTemplate().findByCriteria(criteria);
			if(list.size()>0){
				return true;
			}else{
				Organ organ=this.getHibernateTemplate().get(Organ.class, organId);
				if(organ!=null){
					String parentId=organ.getParentId();
					while(parentId!=null&&!parentId.equals("11111111111111111111111111111111")){
						DetachedCriteria pcriteria=DetachedCriteria.forClass(OrganAdministrator.class);
						pcriteria.add(Restrictions.eq("usersId", userId));
						pcriteria.add(Restrictions.eq("organId", parentId));
						List<OrganAdministrator> plist=this.getHibernateTemplate().findByCriteria(pcriteria);
						if(plist.size()>0){
							return true;
						}else{
							Organ parentOrgan=this.getHibernateTemplate().get(Organ.class, parentId);
							if(parentOrgan!=null){
								parentId=parentOrgan.getParentId();
							}else{
								return false;
							}
						}
					}
				}
			}
		}
		return false;
	}
	@Override
	public List<Users> queryAllUsersByTopOrganId(String organId) {
		List<BaseTreeObject> organs=this.queryTreeByParentId(organId,4,true,true);
		List organIds=new ArrayList();
		for(BaseTreeObject organ:organs){
			String id=organ.getId();
			organIds.add(id);
			List<BaseTreeObject> sub=organ.getSubset();
			for(BaseTreeObject suborgan:sub){
				String subid=suborgan.getId();
				organIds.add(subid);
				List<BaseTreeObject> subo=suborgan.getSubset();
				for(BaseTreeObject suborgans:subo){
					String subido=suborgans.getId();
					organIds.add(subido);
				}
			}
		}
		DetachedCriteria criteria=DetachedCriteria.forClass(Users.class);
		criteria.add(Restrictions.in("organId", organIds));
		return (List<Users>)this.queryByCriteria(criteria);
	}

	@Override
	public List<Users> queryAllUsersByTopOrganIds(List<String> organIds) {
		DetachedCriteria criteria=DetachedCriteria.forClass(Users.class);
		criteria.add(Restrictions.in("organId", organIds));
		return (List<Users>)this.queryByCriteria(criteria);
	}
}
