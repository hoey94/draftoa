package com.starsoft.core.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.starsoft.core.domain.AddressBookDomain;
import com.starsoft.core.domain.AddressBookTypeDomain;
import com.starsoft.core.entity.AddressBook;
import com.starsoft.core.entity.AddressBookType;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.StringUtil;
import com.starsoft.core.util.WEBCONSTANTS;
import com.starsoft.core.vo.FileUpload;

public class AddressBookController extends BaseAjaxController implements BaseInterface {
	private AddressBookTypeDomain addressBookTypeDomain;
	private AddressBookDomain addressBookDomain;
	/***
	 * 公共的列表方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model=HttpUtil.convertModel(request);
		Page page=HttpUtil.convertPage(request);
		DetachedCriteria criteria = this.convertCriteria(request);
		String addressBookTypeId = HttpUtil.getString(request, "addressBookTypeId","");
		if(!addressBookTypeId.equals("")){
			criteria.add(Restrictions.eq("addressBookTypeId", addressBookTypeId));
			model.put("addressBookTypeId", addressBookTypeId);
			AddressBookType addressBookType=(AddressBookType)addressBookTypeDomain.query(addressBookTypeId);
			if(addressBookType!=null){
				model.put("addressBookTypeName", addressBookType.getTname());
			}
		}
		List list= baseDomain.queryByCriteria(criteria, page);
		model.put("list", list);
		model.put("page", page);
		return new ModelAndView(this.getListPage(),model);
	}
	/***
	 * 公共的新建方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		String addressBookTypeId = HttpUtil.getString(request, "addressBookTypeId","");
		if(!addressBookTypeId.equals("")){
			model.put("addressBookTypeId", addressBookTypeId);
		}
		BaseObject loginUser=HttpUtil.getLoginUser(request);
		List addressBookTypelist=addressBookTypeDomain.queryByParentId(loginUser.getId(), true, true);
		model.put("addressBookTypelist", addressBookTypelist);
		return new ModelAndView(this.getAddPage(),model);
	}
	/***
	 * 公共的编辑方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView read(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView model=this.edit(request, response);
		return new ModelAndView(this.getReadPage(),model.getModel());
	}
	/***
	 * 公共的编辑方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id=HttpUtil.getString(request, "id", "");
		int page=HttpUtil.getInt(request, "page", 0);
		Map<String,Object> model = new HashMap<String,Object>();
		BaseObject obj=(BaseObject) baseDomain.query(id);
		BaseObject loginUser=HttpUtil.getLoginUser(request);
		List addressBookTypelist=addressBookTypeDomain.queryByParentId(loginUser.getId(), true, true);
		model.put("addressBookTypelist", addressBookTypelist);
		if(obj!=null){
			model.put("obj", obj);
			model.put("page", page);
		}else{
			model.put("msg", "编辑的信息不存在!可能已经被删除!");
			return new ModelAndView(CloseMessagePage,model);
		}
		return new ModelAndView(this.getEditPage(),model);
	}
	/***
	 * 保存
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void save(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String gotourl="";
		String method=request.getMethod().toLowerCase();
		if("post".equals(method)){
			try{
				AddressBook entity=(AddressBook)baseDomain.getBaseObject();
				this.bind(request, entity);
				this.saveBaseInfoToObject(request, entity);
				gotourl=request.getRequestURI()+"?addressBookTypeId="+entity.getAddressBookTypeId();
				this.baseDomain.save(entity);
				this.outSuccessString(request,response, gotourl);
			}catch(Exception e){
				e.getStackTrace();
				this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
			}
		}else{
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
		}
	}
	/***
	 * 更新
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void update(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String gotourl=HttpUtil.getString(request, "gotourl", "?action=list");
		String id=HttpUtil.getString(request, "id", "");
		int page=HttpUtil.getInt(request, "page", 0);
		String method=request.getMethod().toLowerCase();
		if("post".equals(method)){
			try{
				BaseObject entity=(BaseObject) baseDomain.query(id);
				if(entity!=null){
					this.bind(request, entity);
				}
				this.baseDomain.update(entity);
				gotourl=request.getRequestURI()+"?page="+page;
				this.outSuccessString(request,response, gotourl);
			}catch(Exception e){
				e.getStackTrace();
				this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
			}
		}else{
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
		}
	}
	/***
	 * 导入用户数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public ModelAndView importdata(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			Map<String,Object> model = new HashMap<String,Object>();
			FileUpload entity=new FileUpload();
			this.bind(request, entity);
			MultipartFile file=entity.getFile();
			String parameters=HttpUtil.getString(request, "parameters", "");
			Users loginUser=HttpUtil.getLoginUser(request);
			String createId=rootValue;
			if(loginUser!=null){
				createId=loginUser.getId();
			}
			String addressBookTypeId=createId;
			if(parameters.indexOf("@")>0){
				addressBookTypeId=parameters.substring(parameters.indexOf("@")+1);
			}
			String msg="通讯录正在后台导入，请稍后刷新查看";
			if(file!=null&&!file.isEmpty()){
				try {
					Workbook book = Workbook.getWorkbook(file.getInputStream());
					int sheetsize=book.getNumberOfSheets();
					if(sheetsize>0){
						for(int t=0;t<sheetsize;t++){
							Sheet sh = book.getSheet(t);
							int rows = sh.getRows();
							List savelist=new ArrayList();
							for (int i = 1; i < rows; i++) {
								String tname = sh.getCell(0, i).getContents().trim();
								String mobilNumber = sh.getCell(1, i).getContents().trim();
								String email = sh.getCell(2, i).getContents().trim();
								String qqNumber = sh.getCell(4, i).getContents().trim();
								String homeAddress = sh.getCell(4, i).getContents().trim();
								String workAddress = sh.getCell(5, i).getContents().trim();
								if(mobilNumber.equals("")||StringUtil.isMobileNumber(mobilNumber)){
									AddressBook addressBook=new AddressBook();
									addressBook.setTname(tname);
									addressBook.setMobilNumber(mobilNumber);
									addressBook.setEmail(email);
									addressBook.setQqNumber(qqNumber);
									addressBook.setHomeAddress(homeAddress);
									addressBook.setWorkAddress(workAddress);
									addressBook.setAddressBookTypeId(addressBookTypeId);
									addressBook.setValid(true);
									if(tname.equals("")&&mobilNumber.equals("")){
										continue;
									}
									addressBook.setCreateId(createId);
									savelist.add(addressBook);
								}
								if(i%100==0){
									this.baseDomain.save(savelist);
									savelist=new ArrayList();
								}
							}
							this.baseDomain.save(savelist);
						}
					}
				} catch (BiffException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				msg="请上传excel文件！";
			}
			model.put("msg", msg);
			return new ModelAndView(this.SuccessPageForShowModalDialog,model);
	}
	public void setAddressBookTypeDomain(AddressBookTypeDomain addressBookTypeDomain) {
		this.addressBookTypeDomain = addressBookTypeDomain;
	}
	public void setAddressBookDomain(AddressBookDomain addressBookDomain) {
		this.addressBookDomain = addressBookDomain;
	}
}
