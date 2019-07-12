package com.globalegrow.rosewholesalea;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.globalegrow.base.StartApp;
import com.globalegrow.code.Page;
import com.globalegrow.custom.ControlUsingMethod;
import com.globalegrow.custom.InterfaceMethod;
import com.globalegrow.util.Log;
import com.globalegrow.util.MailReceiver;

import com.globalegrow.util.testInfo;

public class RW_A_login extends StartApp{
	private String className = GetClassName();
	private String testCaseProjectName = "rosewholesale";
	private testInfo info = null;
	public String baseUrl;
	private String domainName;
	private String RegisterEmail="autotest13@globalegrow.com";
	private   long setexplicitWaitTimeout=20;
	private String username;
	private String password="123456";
	private String packageName;//包名
	private String  appactivity;//app的activity
	boolean status=false;//是否弹出网络不好的框
	InterfaceMethod	interfaceMethod;
	
	@Parameters({ "testUrl" })
	public RW_A_login(String testurl) {
		moduleName = className.substring(className.lastIndexOf(".") + 1);
		projectName = className.substring(className.indexOf(testCaseProjectName), className.lastIndexOf("."));
		baseUrl=testurl;
		if(baseUrl.contains("trunk")){
			domainName="http://www.rosewholesale.com.trunk.s1.egomsl.com";
		}else{
			domainName="https://www.rosewholesale.com";
		}
	}
	
	
	
	@Parameters({"appPackage","appActivity"})
	@BeforeClass
	public void beforeClass(String appPackage,String appActivity) {
		String methodName = GetMethodName();
		String methodNameFull = moduleName + "." + methodName;
		Log.logInfo("**************Ready to test (" + moduleName + ")!!!**************\n");
		Log.logInfo("(" + methodNameFull + ")...beforeClass start...");

		try {
			packageName=appPackage;
			appactivity=appActivity;
			info = new testInfo(moduleName);
			start();
			opApp.setScreenShotPath(screenShotPath);	
			interfaceMethod=new InterfaceMethod();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Log.logInfo("(" + methodNameFull + ")...beforeClass stoopApp...\n\n");
		}
	}

	

	@AfterClass
	public void afterClass() {
		String methodName = GetMethodName();
		String methodNameFull = moduleName + "." + methodName;
		Log.logInfo("(" + methodNameFull + ")...afterClass start...");

		try {
			/*driver.quit();*/
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Log.logInfo("(" + methodNameFull + ")...afterClass stoopApp...\n\n");
		}
	}


	
	/**
	 * 
	* @备注在每个测试方法前检测是否有弹框   
	* @author zhangjun 

	 */
	@BeforeMethod
	public void befroemethod(){
		ControlUsingMethod.existAbnormalBox(opApp,packageName,appactivity);
	}




	/**
	 * 
	* @测试点: 错误邮箱、登录失败 
	* @验证点: 登录失败，页面停留在当前页面
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月23日 
	  @修改说明
	 */
	@Test
	public void email_notexist(){
		correFail = false;
		if(status==true){
			Log.logError("网络不好，打不开这个APP,不执行脚本");
		}
		username="zhang";
		loginPublicMethods(username,password);

	}
	
	
	/**
	 * 
	* @测试点: 账号正确，密码为空
	* @验证点: 登录失败，页面停留在当前页面
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月23日 
	  @修改说明
	 */
	@Test
	public void password_empty(){
		correFail = false;
		username="autotest13@globalegrow.com";
		password="";
		loginPublicMethods(username,password);
	}
	
	/**
	 * 
	* @测试点: 账号密码都错误
	* @验证点: 登录失败，页面停留在当前页面
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月23日 
	  @修改说明
	 */
	@Test
	public void invalid_content(){
		correFail = false;
		username="autotest13";
		password="12487888";
		loginPublicMethods(username,password);
	}
	
	/**
	 * 
	* @测试点: 账号密码都错误
	* @验证点: 登录失败，页面停留在当前页面
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月23日 
	  @修改说明
	 */
	@Test
	public void password_error(){
		correFail = false;
		username="autotest13@globalegrow.com";
		password="12487888";
		loginPublicMethods(username,password);
	}
	
	/**
	 * 
	* @测试点: 点击忘记密码按钮，输入无效账号
	* @验证点: 页面还是停留在当前页面
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月23日 
	  @修改说明
	 */
	@Test
	public void foreget_errorEmai(){
		correFail = false;
		username="autotest13";
		if(opApp.elementExists(5, "homeaccount")){
			opApp.ClickElement("homeaccount", setexplicitWaitTimeout);
		}
		opApp.ClickElement("foregetPassword", setexplicitWaitTimeout);
		opApp.SendKeysClean("foregetSendkey", username, setexplicitWaitTimeout);
		opApp.ClickElement("forgetBtn", setexplicitWaitTimeout);
		if(opApp.elementExists(5,"foregetSendkey")){
			Log.logInfo("输入了错误的数据，页面还是停留在当前页，验证通过");
		}else{
			Log.logError("输入了错误的数据，页面没有停留在当前页，验证失败");
		}
	}
	
	
	/**
	 * 
	* @测试点: 输入正确存在的邮箱账户
	* @验证点:给予提示已正确发送了邮件
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月23日 
	  @修改说明
	 */
	@Test
	public void foreget_update(){
		username="autotest13@globalegrow.com";
		//opApp.ClickElement("foregetPassword", setexplicitWaitTimeout);
		opApp.SendKeysClean("foregetSendkey", username, setexplicitWaitTimeout);
		opApp.ClickElement("forgetBtn", setexplicitWaitTimeout);
		if(opApp.elementExists(10,"forgetTitle")){
			Log.logInfo("输入了正确数据，弹出发送邮件的消息，验证通过");
			opApp.ClickElement("forgetBtn", 10);
		}else{
			Log.logError("输入了正确的数据，没有弹出发送邮件的消息，验证失败");
		}
	}
	/**
	 * 
	* @测试点: 输入正确存在的邮箱账户
	* @验证点:给予提示已正确发送了邮件
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月23日 
	  @修改说明
	 */
	@Test
	public void foreget_receiveEmai(){
		Date sendEmailDate = new Date();
		// 获取邮件地址
		// 读取邮件内容，判断修改密码链接是否正确
		String subject = "Reset your password of Rosewholesale.com";
		String urlEmial;
		String mail_content = null;
		String sender = "Rosewholesale.com";
		MailReceiver MR = new MailReceiver();
		Date sendEmailDate_2 = null;
		String email="autotest13@globalegrow.com";
		String password="nuH%qE2zL7";
		Pattern p;
		String[] code;
		try {
			sendEmailDate_2 = new Date(sendEmailDate.getTime() - 120000);// 设置发送邮件的前两分钟
			// 循环判断是否收到邮件,60秒为上线
			int timeout = 0;
			while (true) {
				if (timeout > 240) {
					Log.logError("获取邮件超时！！！");
					break;
				}
				try {
					mail_content = MR.resmg(email, password, sender, subject,sendEmailDate_2);
					if (mail_content.equals("0")) {
						Page.pause(3);
						timeout++;
						continue;
					} else {
						if (baseUrl.contains("trunk")) {// 正式环境和测试环境的链接地址不一样
							p = Pattern.compile("http://(.*?).html"); // 正则取值,匹配http开头.html结尾的字符串
							Log.logInfo("使用测试环境的规则");
						} else {
							p = Pattern.compile("https://(.*?).html"); // 正则取值,匹配http开头.html结尾的字符串
							Log.logInfo("使用正式环境的规则");
						}

						Matcher m = p.matcher(mail_content); // 取出忘记密码邮件中链接地址
						while (m.find()) {
							
							System.out.println("获取的邮件地址为" + m.group());
							urlEmial=m.group();
							code=urlEmial.split("-code-");
						}
						break;
					}
				} catch (Exception e) {
					Log.logError("获取邮件出错" + e.getMessage());
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}
	
	
	/**
	 * 
	* @测试点: 邮件账号登陆 
	* @验证点: 输入有效邮件账号，正确密码登录 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月23日 
	  @修改说明
	 */
	@Test
	public void sigin_successful(){
		correFail = false;
		if(status==true){
			Log.logError("网络不好，打不开这个APP,不执行脚本");
		}
		if(opApp.elementExists(5, "homeaccount")){
			opApp.ClickElement("homeaccount", setexplicitWaitTimeout);
		}
		username="autotest13@globalegrow.com";	
		opApp.SendKeysClean("loginName", username, setexplicitWaitTimeout);
		opApp.SendKeysClean("logingPassword", "123456", setexplicitWaitTimeout);
		opApp.ClickElement("loginSign", setexplicitWaitTimeout);
	}
	
	/**
	 * 
	* @测试点: 新旧密码一致，修改失败 
	* @验证点:  修改密码成功，跳转到登录页面 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月23日 
	  @修改说明
	 */
	@Test
	public void update_password_fail(){
		correFail = false;
		if(status==true){
			Log.logError("网络不好，打不开这个APP,不执行脚本");
		}
		opApp.ClickElement("homeaccount", setexplicitWaitTimeout);
		opApp.ClickElement("forgetChang",setexplicitWaitTimeout);	
		opApp.SendKeysClean("forgetOld",password,setexplicitWaitTimeout);
		opApp.SendKeysClean("forgetNew","123456",setexplicitWaitTimeout);
		opApp.SendKeysClean("forgetNewConfim","123456",setexplicitWaitTimeout);
		opApp.ClickElement("forgetDone",setexplicitWaitTimeout);
		if(opApp.elementExists(10,"forgetNewConfim")){
			Log.logInfo(" 新旧密码一致，修改失败 ，验证通过");
		}else{
			Log.logError(" 新旧密码一致，修改成功 ，验证失败");
		}
	}
	/**
	 * 
	* @测试点: 新旧密码一致，修改失败 
	* @验证点:  修改密码成功，跳转到登录页面 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月23日 
	  @修改说明
	 */
	@Test
	public void update_password_success(){
		//correFail = false;
		opApp.SendKeysClean("forgetOld","123456",setexplicitWaitTimeout);
		opApp.SendKeysClean("forgetNew","12345678",setexplicitWaitTimeout);
		opApp.SendKeysClean("forgetNewConfim","12345678",setexplicitWaitTimeout);
		opApp.ClickElement("forgetDone",setexplicitWaitTimeout);
		if(opApp.elementExists(10,"loginSign")){
			Log.logInfo("修改密码成功，现在跳转到登录页面，验证成功");
		}else{
			Log.logError("修改密码失败，没有跳转到登录页面，验证失败");
		}
	}
	
	/**
	 * 
	* @测试点: 旧密码错误,使用新密码登录成功
	* @验证点:  输入错误的旧密码进行登录，点击确认
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月23日 
	  @修改说明
	 */
	@Test
	public void update_login(){
		try {
			opApp.SendKeysClean("loginName", RegisterEmail, setexplicitWaitTimeout);
			opApp.SendKeysClean("logingPassword", "12345678", setexplicitWaitTimeout);
			opApp.ClickElement("loginSign", setexplicitWaitTimeout);
			if(opApp.elementExists(5, "homeaccount")){
				Log.logInfo("登录成功，页面跳转到了主页，验证通过");
			}else{
				Log.logError("登录失败，页面没有跳转到了主页，验证失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//登录成功后把密码还原到以前的密码
			opApp.ClickElement("homeaccount", setexplicitWaitTimeout);
			opApp.ClickElement("forgetChang",setexplicitWaitTimeout);	
			opApp.SendKeysClean("forgetOld","12345678",setexplicitWaitTimeout);
			opApp.SendKeysClean("forgetNew","123456",setexplicitWaitTimeout);
			opApp.SendKeysClean("forgetNewConfim","123456",setexplicitWaitTimeout);
			opApp.ClickElement("forgetDone",setexplicitWaitTimeout);
		}
		
	}
	
	
	/**
	 * 
	* @测试点: FacekBook登录 
	* @验证点: 使用有效FB账号密码登录 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月24日 
	  @修改说明
	 */
	@Test
	public void sigin_facebooksuccessful(){
		correFail = false;
		//existAbnormalBox();
		opApp.ClickElement("facebookbtn", setexplicitWaitTimeout);

		Log.logInfo("资源文件:" + driver.getPageSource());
		
		if (opApp.elementExists(15, "facebookViewname")) {
			Log.logInfo("使用的webview控件登录");
			opApp.sendKeys("facebookViewname", "autotest03@globalegrow.com");
			opApp.sendKeys("facebookViewpassword", "autotest123456!");
			opApp.ClickElement("facebookViewbtn", 20);
			// 继续按钮
			opApp.ClickElement("facebookViewokBtn", 20);
		} else {
			Log.logInfo("使用的原生APP登录");
			if (opApp.elementExists(15, "homeaccount")) {
				Log.logInfo("携带登录态登录成功");
			} else {
				if (opApp.elementExists(5, "facebookconntiue")) {
					opApp.ClickElement("facebookconntiue", setexplicitWaitTimeout);
				} else {
					opApp.SendKeysClean("facebookLogin", "autotest03@globalegrow.com", setexplicitWaitTimeout);
					opApp.SendKeysClean("facebookPassword", "autotest123456!", setexplicitWaitTimeout);
					opApp.ClickElement("facebookBtnLogin", setexplicitWaitTimeout);
				}
			}
		}
		
		
		if (opApp.elementExists(20, "homeaccount")) {
			Log.logInfo("使用facebook已经登录成功,验证通过");
		}else{
			Log.logError("使用facebook登录失败，验证失败");
		}
		
		
	}
	
	public void loginPublicMethods(String name,String password){
		
		if(opApp.elementExists(5, "homeaccount")){
			opApp.ClickElement("homeaccount", setexplicitWaitTimeout);
		}
		
		opApp.SendKeysClean("loginName", name, setexplicitWaitTimeout);
		opApp.SendKeysClean("logingPassword", password, setexplicitWaitTimeout);
		opApp.ClickElement("loginSign", setexplicitWaitTimeout);
		if(opApp.elementExists(5, "loginSign")){
			Log.logInfo("输入的账号或密码不满足规则，登录失败，验证通过");
		}else{
			Log.logError("输入的账号或密码不满足规则，登录反而成功了，验证失败");
		}
	}
	
	
	
	

	
}
