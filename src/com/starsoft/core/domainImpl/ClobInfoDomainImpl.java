package com.starsoft.core.domainImpl;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domain.ClobInfoDomain;
import com.starsoft.core.entity.ClobInfo;

@Service("clobInfoDomain")
@Transactional
public class ClobInfoDomainImpl extends HibernateDaoSupport implements ClobInfoDomain {
	@Override
	public void save(String Id, String content) {
		if(Id!=null&&!Id.equals("")&&content!=null&&!content.equals("")){
			ClobInfo entity=new ClobInfo(Id);
			Clob contents = Hibernate.createClob(content);
			entity.setContent(contents);
			this.getHibernateTemplate().save(entity);
		}

	}
	@Override
	public void update(String Id, String content) {
		ClobInfo entity=this.getHibernateTemplate().get(ClobInfo.class, Id);
		if(content==null){ content="";}
		if(entity!=null){
			Clob contents = Hibernate.createClob(content);
			entity.setContent(contents);
			this.getHibernateTemplate().update(entity);
		}else{
			entity=new ClobInfo(Id);
			Clob contents = Hibernate.createClob(content);
			entity.setContent(contents);
			this.getHibernateTemplate().save(entity);
		}
		
	}
	@Override
	public String query(String id) {
		ClobInfo entity=this.getHibernateTemplate().get(ClobInfo.class, id);
		String result="";
		if(entity!=null){
			Clob content=entity.getContent();
			try {
				if(content!=null&&content.length()>1){
					result=content.getSubString((long)1,(int)content.length());
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public void deletes(final String propertyName,final List<String> ids) {
		final String hql="delete from "+ClobInfo.class.getName()+" as t where t."+propertyName+" in (:ids)";
		getHibernateTemplate().execute(new HibernateCallback() { 
			 public Object doInHibernate(Session session) throws HibernateException {
				 Query query = session.createQuery(hql);
				 query.setParameterList("ids", ids);
                 return query.executeUpdate();
            } 
		 }); 
	}
}
