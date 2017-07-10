package com.starsoft.core.domainImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.starsoft.core.domain.AddressBookDomain;
import com.starsoft.core.entity.AddressBook;
import com.starsoft.core.util.StringUtil;
@Service("addressBookDomain")
@Transactional
public class AddressBookDomainImpl extends BaseDomainImpl implements AddressBookDomain{
	public AddressBookDomainImpl(){
		this.setClassName("com.starsoft.core.entity.AddressBook");
	}
}
