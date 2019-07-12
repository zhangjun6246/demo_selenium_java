package com.globalegrow.rosewholesalew;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.globalegrow.base.StartPhoneBrowser;
import com.globalegrow.code.Locator;
import com.globalegrow.code.Page;
import com.globalegrow.custom.ControlUsingMethod;
import com.globalegrow.custom.InterfaceMethod;
import com.globalegrow.util.Log;
import com.globalegrow.util.MailReceiver;
import com.globalegrow.util.Op;
import com.globalegrow.util.Pub;
import com.globalegrow.util.testInfo;

public class RW_M_login  extends StartPhoneBrowser {
	private String className = GetClassName();
	private testInfo info = null;
	public Date sendEmailDate = new Date();
	private String testCaseProjectName = "rosewholesale";
	private String  loginEmail="autotest11@globalegrow.com";
	private String baseURL = "";
	public String currentWindow = null;//当前句柄
	private String  testExpectedStr;
	private String loginName;
	private String password;
	private String requestId;
    private String registercode;
	String myPublicUrl2;
	String loginUrl="";//登录的url
	String getkeys;//获得验证码

	WebDriverWait wait;
	
	private RW_M_login() {
		moduleName = className.substring(className.lastIndexOf(".") + 1);
		info = new testInfo(moduleName);
	}
	
	
	@Parameters({ "keys","testUrl","devicesName" })
	@BeforeClass
	public void beforeClass(String keys,String testUrl,String devicesName) {
		String methodName = GetMethodName();
		String methodNameFull = moduleName + "." + methodName;
		Log.logInfo("**************Ready to test (" + moduleName + ")!!!**************\n");
		Log.logInfo("(" + methodNameFull + ")...beforeClass start...");
		try {
			start(devicesName); // 初始化driver
			driver = super.getDriver();
			driver.manage().timeouts().pageLoadTimeout(pageLoadTimeoutMax, TimeUnit.SECONDS);
		
			urlInit();
			getkeys=keys;
			baseURL = testUrl;
			//driver.get(baseURL);
			op.loopGet(baseURL, 60, 2, 60);
			wait = new WebDriverWait(driver, 30);//显示等待页面元素出现
		} catch (Exception e) {
			e.printStackTrace();
			/*Assert.fail();*/
		} finally {
			Log.logInfo("(" + methodNameFull + ")...beforeClass stoop...\n\n");
		}
	}



	@AfterClass
	public void afterClass() {
		String methodName = GetMethodName();
		String methodNameFull = moduleName + "." + methodName;
		Log.logInfo("(" + methodNameFull + ")...afterClass start...");
     
		try {
			 driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Log.logInfo("(" + methodNameFull + ")...afterClass stoop...\n\n");
		}
	}

	
	/**
	 * 
	* @测试点: 错误邮箱、登录失败 
	* @验证点:1.进入到登录地址页面
		2.输入不满足格式的邮箱，登录，提示Please enter a valid email address
		3.页面继续停留在当前页
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月18日 
	  @修改说明
	 */
	@Test
	public void signin_wrongemail() {
		correFail = false;
	    String getErrInfoEmail="";
		try {
			 testExpectedStr="Please enter a valid email address";
			 loginName="zhang.com";
			 password="123456";
			 requestId = Pub.getRandomString(6);
		     registercode= InterfaceMethod.IF_Verification(driver,myPublicUrl2,requestId,getkeys);
			op.loopSendKeys("loginPage_loginEmail",loginName , 3,30);
			op.loopSendKeys("loginPage_loginPwd", password, 3,30);			
			IsExistCode(registercode);
			
			op.loopClickElement("loginPage_signInBtn", 10, 30, 100);
			getErrInfoEmail = op.loopGetElementText("loginPage_errorEmail", 3,50);		
			Assert.assertEquals(testExpectedStr, getErrInfoEmail);
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("(" + info.testCheckpoint + ").");
			Log.logWarn("实际获得的内容为："+getErrInfoEmail);
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}
	
	/**
	 * 
	* @测试点: 密码错误、登录失败 
	* @验证点: 提示“Your email/password is incorrect. Please try again.”页面继续停留在当前页
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月18日 
	  @修改说明
	 */
	@Test
	public void signin_wrongpassword() {
		correFail = false;
		String getErrInfoEmail = "";
		try {
			op.navigateRefresh(40, 2, 60);
			requestId = Pub.getRandomString(6);
			testExpectedStr = "YOUR EMAIL/PASSWORD IS INCORRECT. PLEASE TRY AGAIN";
			loginName = "autotest11@globalegrow.com";
			password = "123456789";
			registercode= InterfaceMethod.IF_Verification(driver,myPublicUrl2,requestId,getkeys);
			
			op.loopSendKeys("loginPage_loginEmail", loginName, 3, 30);
			op.loopSendKeys("loginPage_loginPwd", password, 3, 30);
			IsExistCode(registercode);
			op.loopClickElement("loginPage_signInBtn", 10, 30, 100);

			getErrInfoEmail = op.loopGetElementText("loginPage_pwdErrorMsg",6,20);

			Log.logInfo("test:"+getErrInfoEmail);
			Assert.assertEquals(testExpectedStr, getErrInfoEmail);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Pub.printTestCaseExceptionInfo(info);
			Assert.fail();
		}

	}
	
	/**
	 * 
	* @测试点: 密码<6,登录失败
	* @验证点: 1.登录，提示“Enter at least 6 characters”页面继续停留在当前页
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月18日 
	  @修改说明
	 */
	@Test
	public void sigin_passwordLength() {
		correFail = false;

		String getErrInfoEmail = "";
		try {
			op.navigateRefresh(40, 2, 60);
			requestId = Pub.getRandomString(6);
			testExpectedStr = "Please enter at least 6 characters";
			loginName = "autotest11@globalegrow.com";
			password = "1234";
			registercode= InterfaceMethod.IF_Verification(driver,myPublicUrl2,requestId,getkeys);
			
			op.loopSendKeys("loginPage_loginEmail", loginName, 3, 30);
			op.loopSendKeys("loginPage_loginPwd", password, 3, 30);
			IsExistCode(registercode);
			
			op.loopClickElement("loginPage_signInBtn", 10, 30, 100);	
			getErrInfoEmail = op.loopGetElementText("loginPage_pwdLessSixErrorMsg", 3, 50);
			Assert.assertEquals(testExpectedStr, getErrInfoEmail);
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("实际获得文本为："+getErrInfoEmail);
			Assert.fail();
		}
	}
	
	/**
	 * 
	* @测试点: 登录时，查看忘记密码 
	* @验证点: 1.邮箱收到网站发送的重置密码链接2.并且能够正常打开 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月19日 
	  @修改说明
	 */
	@Test
	public void sigin_forgetpassword() {
		correFail = false;

		try {
			op.loopClickElement("forgotPwd", 15, 30, 60);
			op.loopSendKeys("forgotPasswordInput", loginEmail, 20, 60);
			op.loopClickElement("forgotPasswordBtn", 15, 30, 60);
			
			String getCheckEmail=op.loopGetElementText("checkEmailAddress", 15, 60);
			if (getCheckEmail.equals("HOME")){
				Log.logInfo("正确提示了用户，且已发送邮件！！！");
			}else{
				Log.logError("未提示用户已发送邮件，忘记密码发送邮件异常！！！");
			}			
			/**
			 * 获取忘记密码的邮件
			 */		
		  	String passwrod = "nuH%qE2zL7";//密码
		  	String dress = "Rosewholesale.com";//发送邮件地址
		  	String subject = "Reset your password of Rosewholesale.com";//发送邮件主题
		  	MailReceiver test = new MailReceiver();//创建收取邮件对象
		  	String mailContext = null;
		  	Date sendEmailDate_2 = null;
		  	sendEmailDate_2 = new Date(sendEmailDate.getTime() - 120000);//设置发送邮件的前两分钟  	
		  	boolean flag = false;
			//循环判断是否收到邮件,120秒为上线
			int timeout = 0;
			while (true) {
				if(timeout>120){
					Log.logError("获取邮件超时！！！");
					break;
				}
			  	try {
					mailContext = test.resmg(loginEmail,passwrod,dress,subject,sendEmailDate_2);
					if(mailContext.equals("0")){
						Page.pause(3);
						timeout++;
						continue;
					}else {
						flag = true;
						Log.logInfo("收到邮件，已获取到正文信息！！！");
						break;
					}
				} catch (Exception e) {
					Log.logError("获取邮件出错"+e.getMessage());
				}		
			}
			if(flag){
				Log.logInfo("忘记密码,输入邮箱autotest11@globalegrow.com，发送电子邮件，用户可正常收到邮件，验证成功！！！");
			}else {
				Log.logError("忘记密码,输入邮箱autotest11@globalegrow.com，发送电子邮件，用户无法收到邮件，验证失败！！！");
			}	
		
	} catch (Exception e) {
		e.printStackTrace();
		Log.logWarn(info.testCorrelation);
		Assert.fail();
	}
}
	

	/**
	 * 
	* @测试点: 登录成功 
	* @验证点: TODO(这里用一句话描述这个方法的作用) 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月19日 
	  @修改说明
	 */
	@Test
	public void sigin_successful() {
		correFail = false;
		 
		String getHomeText="";
		try {
			Log.logInfo("loginUrl:"+loginUrl);
			op.loopGet(loginUrl, 40, 3, 70);
			
			loginName = "autotest11@globalegrow.com";
			password = "123456";
			requestId = Pub.getRandomString(6);
		    registercode= InterfaceMethod.IF_Verification(driver,myPublicUrl2,requestId,getkeys);
			
			op.loopSendKeys("loginPage_loginEmail", loginName, 3, 30);
			op.loopSendKeys("loginPage_loginPwd", password, 3, 30);
			IsExistCode(registercode);
			op.loopClickElement("loginPage_signInBtn", 10, 30, 100);	
			//因为新用户会跳出很多活动页面，所以我们直接跳转到登录成功页
			op.loopGet("https://userm.rosewholesale.com/user/index/userindex", 5, 5, 10);
			Assert.assertTrue(op.isElementPresent("loginHomePage", 10));
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("实际获得文本为："+getHomeText);
			Assert.fail();
		}
	}
	
	
	/**
	 * 
	* @测试点: facebook登录 
	* @验证点: 登录成功给予提示
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年3月6日 
	  @修改说明
	 */
	@Test
	public void sigin_facebooksuccessful(){
		correFail = false;
		try {
			
			op.loopGet(loginUrl, 60, 3, 40);
			try {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));// 加入到购物车
				Log.logInfo("用户已退出");
			} catch (Exception e) {
				ControlUsingMethod.SetScrollBar(600);
				Page.pause(3);
				op.loopClickElement("LoGout", 3, 20, 40);
				Log.logInfo("用户已退出");
			}
			currentWindow = driver.getWindowHandle();// 保存当前窗口句柄
			op.loopGet(loginUrl, 60, 3, 60);
			Log.logInfo("我是分割线-------------------");
			if (op.isElementPresent("loginFaceBook")) {// 判断facebook登录按钮是否存在
				op.loopClickElement("loginFaceBook", 3, 20, 60);
				Log.logInfo("facebook登录按钮已找到!!");
			} else {
				Log.logError("找不到facebook的元素!!");
			}
			Page.pause(5);

			if (Page.switchToWindow(driver, "Facebook")) { // 切换窗口
				op.loopSendKeysClean("loginEamil", "autotest03@globalegrow.com", 5);
				op.loopSendKeysClean("loginPassword", "autotest123456!",  5);
				op.loopClickElement("loginBtn", 5, 20, 20);
				if (driver.getWindowHandles().size() > 2) {
					if (op.isElementPresent("facebookloginconntinue", 10)) {
						op.loopClickElement("facebookloginconntinue", 3, 20, 20);
					}
				} else {
					Log.logInfo("没有窗体元素，内容不执行");
				}
			} else {
				Log.logInfo("没有找到Facebook登录窗口！！！");
			}
			driver.switchTo().window(currentWindow);// 切换回默认窗口

			if (op.isElementPresent("loginHomePage", 10)) {
				Log.logInfo("Facebook登录成功！！！");
			} else {
				Log.logError("Facebook登录失败，未找到 登录成功的提示！！！");
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}
	
	/**
	 * 判断是否有验证码输入框，如果有就输入，否则不输入
	 */
	public void IsExistCode(String registercode){
		try {
			if (op.isElementPresent("loginCode")){
				op.loopSendKeys("loginCode", registercode, 3, 20);	
			}else{
				Log.logInfo("不存在验证码输入框，不执行验证码，跳过");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	* @测试点: urlInit 
	* @验证点: 地址信息 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月9日 
	  @修改说明
	 */
	public void urlInit(){
		if(baseURL.contains(".a.")){	//测试环境
			loginUrl="http://loginm.wap-rosewholesale.com.a.s1cg.egomsl.com/user/login/sign";
			myPublicUrl2="http://m.wap-rosewholesale.com.a.s1cg.egomsl.com";//获取验证码的接口
		}else if(baseURL.contains(".trunk.")) {
			loginUrl="http://loginm.wap-rosewholesale.com.trunk.s1cg.egomsl.com/user/login/sign";
			myPublicUrl2="http://m.wap-rosewholesale.com.trunk.s1cg.egomsl.com";//获取验证码的接口
		}else{//正式环境
			loginUrl="https://loginm.rosewholesale.com/user/login/sign";
			myPublicUrl2="https://m.rosewholesale.com";//获取验证码的接口
		}
	}
}