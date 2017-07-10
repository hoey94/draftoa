package com.starsoft.core.domainImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domain.SystemPropertyDomain;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.SystemProperty;
import com.starsoft.core.util.WEBCONSTANTS;
@Service("systemPropertyDomain")
@Transactional
public class SystemPropertyDomainImpl extends BaseDomainImpl implements SystemPropertyDomain{
	Map<String,BaseObject> cache=new HashMap<String,BaseObject>();
	public SystemPropertyDomainImpl(){
		this.setClassName("com.starsoft.core.entity.SystemProperty");
	}
	/***
	 * 初始化系统属性配置信息
	 */
	@PostConstruct
	public void initSystemPropertyMap(){
		List<SystemProperty> list=(List<SystemProperty>) this.queryAll();
		for(SystemProperty systemProperty:list){
			if(null!=systemProperty.getTname()){
				cache.put(systemProperty.getId(), systemProperty);
				WEBCONSTANTS.setSystemProperty(systemProperty.getId(), systemProperty.getTname());
			}
		}
	}
	/***
	 * 保存对象
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(BaseObject entity){
		this.getHibernateTemplate().save(entity);
		cache.put(entity.getId(),entity);
		WEBCONSTANTS.setSystemProperty(entity.getId(), entity.getTname());
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public Object query(String id) {
		if(cache.containsKey(id)){
			return cache.get(id);
		}else{
			SystemProperty systemProperty=this.getHibernateTemplate().get(SystemProperty.class, id);
			if(systemProperty!=null){
				cache.put(id, systemProperty);
				WEBCONSTANTS.setSystemProperty(systemProperty.getId(), systemProperty.getTname());
				return systemProperty;
			}else{
				return null;
			}
		}
	}
	/**
	 *更新对象
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(BaseObject entity){
		this.getHibernateTemplate().update(entity);
		cache.put(entity.getId(),entity);
		WEBCONSTANTS.setSystemProperty(entity.getId(), entity.getTname());
	}
	/**
	 * 删除对象
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(BaseObject entity){
		this.getHibernateTemplate().delete(entity);
		cache.remove(entity.getId());
	}
}
