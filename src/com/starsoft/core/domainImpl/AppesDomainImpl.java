package com.starsoft.core.domainImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domain.AppesDomain;
import com.starsoft.core.entity.Appes;
import com.starsoft.core.entity.AppesAction;
import com.starsoft.core.entity.AppesAttribute;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.DateUtil;
@Service("appesDomain")
@Transactional
public class AppesDomainImpl extends BaseDomainImpl implements AppesDomain{
	public AppesDomainImpl(){
		this.setClassName("com.starsoft.core.entity.Appes");
	}
	Map<String,Appes> cache=new HashMap<String,Appes>();
	/***
	 * 初始化系统属性配置信息
	 */
	@PostConstruct
	public void initAppesMap(){
//		if((new Date()).after(DateUtil.parseDate("2016-04-01"))){
//			System.exit(0);
//		}
		List<Appes> list=(List<Appes>) this.queryAll();
		for(Appes appes:list){
			//查询应用的操作信息
			List<AppesAction> actionList=new ArrayList<AppesAction>();
			DetachedCriteria actionCriteria=DetachedCriteria.forClass(AppesAction.class);
			actionCriteria.add(Restrictions.eq("appes",appes.getId()));
			actionCriteria.add(Restrictions.eq("valid",true));
			actionList=this.getHibernateTemplate().findByCriteria(actionCriteria);
			appes.setActionList(actionList);
			//查询应用的属性信息
			List<AppesAttribute> attributeList=new ArrayList<AppesAttribute>();
			DetachedCriteria attributeCriteria=DetachedCriteria.forClass(AppesAttribute.class);
			attributeCriteria.add(Restrictions.eq("appes",appes.getId()));
			attributeCriteria.add(Restrictions.eq("valid",true));
			attributeList=this.getHibernateTemplate().findByCriteria(attributeCriteria);
			Map<String,AppesAttribute> attributeMap=appes.getAttributeMap();
			appes.setAttributeList(attributeList);
			for(AppesAttribute appesAttribute:attributeList){
				attributeMap.put(appesAttribute.getTname(), appesAttribute);
			}
			appes.setAttributeMap(attributeMap);
			cache.put(appes.getId(), appes);
		}
	}
	/***
	 * 保存对象
	 */
	@Override
	public void save(BaseObject entity){
		super.save(entity);
		cache.remove(entity.getId());
		this.query(entity.getId());
	}
	/**
	 *更新对象
	 */
	@Override
	public void update(BaseObject entity){
		super.update(entity);
		cache.remove(entity.getId());
		this.query(entity.getId());
	}
	/**
	 * 删除对象
	 */
	@Override
	public void delete(BaseObject entity){
		super.delete(entity);
		cache.remove(entity.getId());
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public Object query(String id) {
		if(cache.containsKey(id)){
			return cache.get(id);
		}else{
			Appes appes=this.getHibernateTemplate().get(Appes.class, id);
			if(appes!=null){
				//查询应用的操作信息
				List<AppesAction> actionList=new ArrayList<AppesAction>();
				DetachedCriteria actionCriteria=DetachedCriteria.forClass(AppesAction.class);
				actionCriteria.add(Restrictions.eq("appes",id));
				actionCriteria.add(Restrictions.eq("valid",true));
				actionList=this.getHibernateTemplate().findByCriteria(actionCriteria);
				appes.setActionList(actionList);
				//查询应用的属性信息
				List<AppesAttribute> attributeList=new ArrayList<AppesAttribute>();
				DetachedCriteria attributeCriteria=DetachedCriteria.forClass(AppesAttribute.class);
				attributeCriteria.add(Restrictions.eq("appes",id));
				attributeCriteria.add(Restrictions.eq("valid",true));
				attributeList=this.getHibernateTemplate().findByCriteria(attributeCriteria);
				Map<String,AppesAttribute> attributeMap=appes.getAttributeMap();
				appes.setAttributeList(attributeList);
				for(AppesAttribute appesAttribute:attributeList){
					attributeMap.put(appesAttribute.getTname(), appesAttribute);
				}
				appes.setAttributeMap(attributeMap);
				//查询应用的属性信息
				cache.put(id, appes);
				return appes;
			}else{
				return null;
			}
		}
	}
	@Override
	public Appes getAppes(String id) {
		if(cache.containsKey(id)){
			return cache.get(id);
		}else{
			Appes appes=(Appes) this.query(id);
			return appes;
		}
	}
	/****
	 * 更新一个缓存信息
	 */
	@Override
	public void updateCache(String id) {
		cache.remove(id);
		this.query(id);
	}
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public Object query(Class cls, String id) {
		return this.getHibernateTemplate().get(cls, id);
	}
	
}
