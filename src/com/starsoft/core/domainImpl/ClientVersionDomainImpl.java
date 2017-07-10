package com.starsoft.core.domainImpl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import com.starsoft.core.domain.ClientVersionDomain;
import com.starsoft.core.entity.ClientVersion;
@Service("clientVersionDomain")
@Transactional
public class ClientVersionDomainImpl extends BaseDomainImpl implements ClientVersionDomain{
	public ClientVersionDomainImpl(){
		this.setClassName("com.starsoft.core.entity.ClientVersion");
	}
	/***
	 * 通过客户端类型和版本号码查询
	 */
	@Override
	public ClientVersion queryNewVersionByClientCodeAndClientType(
			String clientCode, String clientType) {
		List<ClientVersion> result=this.queryByCriteria(this.getCriteria(true).add(Restrictions.eq("clientCode", clientCode)).add(Restrictions.eq("clientType", clientType)).addOrder(Order.desc("id")), 1);
		if(result.size()>0){
			return result.get(0);
		}else{
			return null;
		}
	}
	
}
