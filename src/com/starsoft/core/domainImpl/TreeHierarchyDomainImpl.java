package com.starsoft.core.domainImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.starsoft.core.domain.TreeHierarchyDomain;
import com.starsoft.core.entity.BaseTreeObject;
import com.starsoft.core.entity.TreeHierarchy;

public class TreeHierarchyDomainImpl extends BaseTreeDomainImpl implements TreeHierarchyDomain {

	@Override
	public void save(BaseTreeObject treeObject, String appes) {
		List<TreeHierarchy> list=this.queryParentListByChildId(treeObject.getParentId(), treeObject.getCreateId(), appes);
		List saveList=new ArrayList();
		int sortCode=0;
		for(TreeHierarchy treeHierarchy:list){
			TreeHierarchy obj=new TreeHierarchy();
			obj.setTname(treeObject.getTname());
			obj.setAppes(appes);
			obj.setChildId(treeObject.getId());
			obj.setCreateId(treeObject.getCreateId());
			obj.setParentId(treeHierarchy.getParentId());
			sortCode++;
			saveList.add(obj);
		}
		TreeHierarchy obj=new TreeHierarchy();
		obj.setTname(treeObject.getTname());
		obj.setAppes(appes);
		obj.setChildId(treeObject.getId());
		obj.setCreateId(treeObject.getCreateId());
		obj.setParentId(treeObject.getParentId());
		sortCode++;
		saveList.add(obj);
		this.save(saveList);
	}
	/***
	 * 仅仅是级别调整的时候才用
	 */
	@Override
	public void update(BaseTreeObject treeObject, String appes) {
		this.delete(treeObject, appes);
		this.save(treeObject, appes);
	}

	@Override
	public void delete(BaseTreeObject treeObject, String appes) {
		// TODO Auto-generated method stub
		List deleteList=this.queryChildListByParentId(treeObject.getId(), treeObject.getCreateId(), appes);
		this.deleteAndSaveAndUpdate(deleteList, null, null);
	}


	@Override
	public List queryParentListByChildId(String id, String appes) {
		 return this.queryByCriteria(this.getCriteria(true,false).add(Restrictions.eq("appes", appes)).add(Restrictions.eq("childId", id)));
	}
	@Override
	public List queryChildListByParentId(String id, String appes) {
		return this.queryByCriteria(this.getCriteria(true,false).add(Restrictions.eq("appes", appes)).add(Restrictions.eq("parentId", id)));
	}
	@Override
	public List queryParentListByChildId(String id, String createId,
			String appes) {
		return this.queryByCriteria(this.getCriteria(true,false).add(Restrictions.eq("appes", appes)).add(Restrictions.eq("childId", id)).add(Restrictions.eq("createId", createId)));
	}
	@Override
	public List queryChildListByParentId(String id, String createId,
			String appes) {
		return this.queryByCriteria(this.getCriteria(true,false).add(Restrictions.eq("appes", appes)).add(Restrictions.eq("parentId", id)).add(Restrictions.eq("createId", createId)));
	}

	
}
