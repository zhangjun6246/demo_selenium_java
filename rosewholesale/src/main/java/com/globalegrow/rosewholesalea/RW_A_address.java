package com.globalegrow.rosewholesalea;


import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.javascript.host.Map;
import com.globalegrow.base.StartApp;
import com.globalegrow.code.Page;
import com.globalegrow.custom.InterfaceMethod;
import com.globalegrow.util.Log;
import com.globalegrow.util.Pub;
import com.globalegrow.util.testInfo;

public class RW_A_address extends StartApp{
	private String className = GetClassName();
	private String testCaseProjectName = "rosewholesale";
	private testInfo info = null;
	public String baseUrl;
	private   long setWaitTimeout=20;

	InterfaceMethod	interfaceMethod;

	public RW_A_address() {
		moduleName = className.substring(className.lastIndexOf(".") + 1);
		projectName = className.substring(className.indexOf(testCaseProjectName), className.lastIndexOf("."));
	
	}
	
	
	@BeforeClass
	public void beforeClass() {
		String methodName = GetMethodName();
		//stratTime=new Date();
		String methodNameFull = moduleName + "." + methodName;
		Log.logInfo("**************Ready to test (" + moduleName + ")!!!**************\n");
		Log.logInfo("(" + methodNameFull + ")...beforeClass start...");

		try {
			info = new testInfo(moduleName);
			start();
			interfaceMethod=new InterfaceMethod();
			login();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Log.logInfo("(" + methodNameFull + ")...beforeClass stoopApp...\n\n");
		}
	}

	


	/**
	 * 每个测试用例执行完成时，延时1秒并打印相关信息
	 * @author yuyang 创建时间:2016-11-07 更新时间:2016-11-07
	 */
	@AfterMethod
	public void afterMethod() {
		info.lastMethodName = info.methodName;
		Log.logInfo("(" + info.methodNameFull + ")...afterMethod stoopApp...\n\n");
		Pub.sleep(1);
	}
	
	
	/**
	 * 
	* @测试点: 登录
	* @备注： void    
	* @author zhangjun 
	* @date 2017年6月15日 
	  @修改说明
	 */
	public void login(){
		correFail = false;
		opApp.ClickElement("addressAccount", setWaitTimeout);
		
		opApp.SendKeysClean("loginName", "autotest14@globalegrow.com", setWaitTimeout);
		opApp.SendKeysClean("logingPassword", "123456", setWaitTimeout);
		opApp.ClickElement("loginSign", setWaitTimeout);
		boolean account=opApp.elementExists(10,"addressAccount");
		Assert.assertTrue(account);
	}
	
	
	/**
	 * 
	* @测试点: 1.已进入新增地址页面，输入所有必填项
			2.删除firename必填项内容，点击done,页面不进行跳转
			3.填写firename必填项，删除lastName，点击done，页面不进行跳转，依次类推，验证所有的必填项 
	* @验证点: 必填项不填写，点击done，页面不进行跳转 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年6月15日 
	  @修改说明
	 */
	@Test
	public  void allOptions(){
		correFail = false;
	
		opApp.ClickElement("addressAccount", setWaitTimeout);
		opApp.ClickElement("adddressBook", setWaitTimeout);
		opApp.ClickElement("addressNewBtn", setWaitTimeout);//新增地址按钮	
		
		addAddress();//新增地址
		String[] control={"firestName","lastName","address1","city","code","phoneNumber"};
		String[] setText={"zhang","jun","shenzhen","beijing","12345678","123456789"};
		for (int j = 0; j < control.length; j++) {
		/*	HashMap<String,String> ml=new HashMap<String,String>();
			ml.put("", value)
			*/
			
			driver.findElement(locator.getBy(locator.getMap(control[j]),"id")).clear();
			opApp.ClickElement("done",setWaitTimeout);
			Assert.assertTrue(opApp.elementExists(10, "firestName"));//断言是否已经存在
			
			/*删除内容后在重新填写内容*/
			opApp.sendKeys(control[j], setText[j]);//删除内容后，在进行填写内容
		}
		//做收尾的工作点击完成，跳转到了地址页面
		opApp.ClickElement("done",setWaitTimeout);	
		Assert.assertTrue(opApp.elementExists(10, "addressNewBtn"));//新增地址按钮
	
 		
 		
	}
	
	/**
	 * 
	* @测试点: 1.已填写完除电话号码以为的内容
				2.输入电话号码内容，输入12345（&*）字符，
				3.点击Done 
	* @验证点: 1.电话号码中文本框中内容为空12345
				2.页面不进行跳转
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年6月16日 
	  @修改说明
	 */
	@Test
	public void  phoneCheck(){
		correFail = false;
		//Pub.getTestCaseInfoAndroidApp(testCasemap, GetMethodName(), info, true);
		opApp.ClickElement("addressEdit",setWaitTimeout);
		//opApp.ClickElement("editAddress", setWaitTimeout);//选择某一个地址
		opApp.SendKeysClean("phoneNumber","12345（&*）",setWaitTimeout);	
		String getPhone=opApp.GetElementText("phoneNumber", 10,10);
		//Assert.assertEquals(getPhone, "12345");
	
	}
	 
	/**
	 * 
	* @测试点: 1.已填写完除电话号码以为的内容
				2.输入电话号码内容，输入“123”字符，
				3.点击提交
	* @验证点: 页面不进行跳转
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年6月16日 
	  @修改说明
	 */
	@Test
	public void  lessPhone(){
	
		opApp.SendKeysClean("phoneNumber", "123", setWaitTimeout);
		opApp.ClickElement("done",setWaitTimeout);
		Assert.assertTrue(opApp.elementExists(10, "phoneNumber"));
	}
	
	
	/**
	 * 
	* @测试点: 进入新增 Address Book页面，点击一个地址，进行编辑，修改用户名，和地址，用户更改为：testjun，电话更改为：8888888 
	* @验证点: 保存后该被编辑的地址信息更新为编辑后的数据
				用户名已更改为：testjun
				电话已更改为：8888888
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年6月16日 
	  @修改说明
	 */
	
	@Test
	public void editAddress(){
		
		if(opApp.elementExists(5, "addressAccount")){
			opApp.ClickElement("addressAccount", setWaitTimeout);
			opApp.ClickElement("adddressBook", setWaitTimeout);
		}
		
		//编辑地址
		if (opApp.elementExists(10, "AddressList")) {
			Log.logInfo("页面存在地址按钮");
		} else {
			Log.logInfo("页面没有地址按钮,现在进行新增");
			addAddress();
			opApp.ClickElement("done", setWaitTimeout);
			if (!opApp.elementExists(10, "addressNewBtn")) {
				Log.logError("新增地址失败");
			}
		}
		//进行编辑	
		opApp.ClickElement("addressEdit",setWaitTimeout);
		String firtName="testjun";
		String phoneNumber="8888888";
		
		//修改内容
		opApp.SendKeysClean("firestName", firtName, setWaitTimeout);
		opApp.SendKeysClean("phoneNumber", phoneNumber, setWaitTimeout);
		opApp.ClickElement("done",setWaitTimeout);
		//进行编辑
		opApp.ClickElement("addressEdit",setWaitTimeout);
		String editFirtName=opApp.GetElementText("firestName", 10,10);
		String editphoneNumber=opApp.GetElementText("phoneNumber", 10,10);
		
		if(editFirtName.equals(firtName)&&phoneNumber.equals(editphoneNumber)){
			Log.logInfo("修改前的用户名和地址为:"+firtName+","+phoneNumber+"修改后的用户名和地址为："+editFirtName+","+editphoneNumber+"验证通过");
		}else{
			Log.logError("修改前的用户名和地址为:"+firtName+","+phoneNumber+"修改后的用户名和地址为："+editFirtName+","+editphoneNumber+"验证失败");
		}
		
		//为了跳转到前面页面
		opApp.ClickElement("done",setWaitTimeout);
	}
	
	/**
	 * 
	* @测试点: 进入Shipping Address Book页面，点击右上角Edit，选择一个地址，点击Delete 
	* @验证点: 地址集合总数减1
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年6月16日 
	  @修改说明
	 */
	@Test
	public void deleteAddress(){
		List<WebElement> address=opApp.GetElementList("AddressList");	
		int size=address.size();
		Log.logInfo("地址集合总数为："+size);
		opApp.ClickElement("addresEditBtn", 5, 20);
		opApp.ClickElement("deleteBtn",5, 20);
		opApp.ClickElement("affirmdelete", 5, 20);
		
		//删除后在进行查看集合
		address=opApp.GetElementList("AddressList");
		Page.pause(3);
		int size2=address.size();
		Log.logInfo("地址删除后集合总数为："+size2);
		Assert.assertEquals(size2, size-1);
	}


	/*
	 * 新增地址
	 */
	public void addAddress(){
		Page.pause(1);
		
		
		String[] control={"firestName","lastName","address1","address2","states","province","city","code","phoneNumber"};
		String[] setText={"zhang","jun","shenzhen","shanghai","France","Ain","nanshan","12345678","123456789"};
		for(int i=0;i<control.length;i++){
			if(i==4){
				opApp.ClickElement(control[4], setWaitTimeout);
				opApp.ClickElement("chooseStates",setWaitTimeout);
			}else if(i==5){
				Page.pause(1);
				opApp.ClickElement(control[5], setWaitTimeout);
				opApp.ClickElement("chooseProvince",setWaitTimeout);
			}else {
				opApp.sendKeys(control[i], setText[i]);
			}
			
		}
	}
}