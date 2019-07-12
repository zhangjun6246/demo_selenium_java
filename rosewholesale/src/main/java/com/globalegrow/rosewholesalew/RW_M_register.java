package com.globalegrow.rosewholesalew;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.WebElement;
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


public class RW_M_register extends StartPhoneBrowser {
	private String className = GetClassName();
	private testInfo info = null;
	public Date sendEmailDate = new Date();
	
	
	private String testCaseProjectName = "rosewholesale";
	private String  new_register_email="autotest05@globalegrow.com";

	private String baseURL = "";
	private String domainName = "";
	
	private String requestId;
	private String testExpectedStr;
	private String registerName;
	private String registerPassword;
	private String  registerConfirmPassword;
	private String registercode;
	String  getUrlterms="";//跳转到注册详情地址
	String getkeys;//获得密匙
	String myPublicUrl2;//获取验证码的域名
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//@Parameters({ })
	private RW_M_register() {
		moduleName = className.substring(className.lastIndexOf(".") + 1);
		info = new testInfo(moduleName);

	}
	@Parameters({ "keys","testUrl","devicesName"})
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
			//op.setScreenShotPath(screenShotPath);
			urlInit();
			baseURL = testUrl;
			getkeys=keys;
			driver.get(baseURL);
			enter_user();
		} catch (Exception e) {
			e.printStackTrace();
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
			InterfaceMethod.IF_DelUserAccount2(domainName, new_register_email);
			driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Log.logInfo("(" + methodNameFull + ")...afterClass stoop...\n\n");
		}
	}

	
	/**
	 * 从注册页面中点击new user，进入到注册页面,
	 * 每个页面都先进入到这个入口
	 */
	public void enter_user(){
		try {
			//Page.pause(4);
			op.loopClickElement("loginPage_NewUser", 20, 10, 10);
			if (op.isElementPresent("RegisterPage_RegBtn",40)) {
				Log.logInfo("已经成功进入到注册页面");
			} else {
				Log.logError("进入到注册页失败");
			}

		} catch (Exception e) {
			Log.logInfo("点击注册按钮异常");
		}
	}
	
	/**
	 * 判断是否有验证码输入框，如果有就输入，否则不输入
	 */
	public void IsExistCode(String registercode){
		try {
			if (op.isElementPresent("RegisterPage_Code")){
				op.loopSendKeys("RegisterPage_Code", registercode, 3, 20);	
			}else{
				Log.logInfo("不存在验证码输入框，不执行验证码，跳过");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	/**
	 * 
	* @测试点: 重复注册失败
	* @验证点: 1.注册提示“The email address is already in use”
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月16日 
	  @修改说明
	 */
	@Test
	public void register_repetition() {
		driver.navigate().refresh();
		
		String getErrInfopassword ="";
		try {	
			
			requestId = Pub.getRandomString(5);
			testExpectedStr="User email is already in use";
			registercode=InterfaceMethod.IF_Verification(driver, myPublicUrl2, requestId, getkeys);
			op.loopSendKeys("RegisterPage_Email","zhang@qq.com" , 3, 20);
			op.loopSendKeys("RegisterPage_Password", "123456", 3, 10);
			op.loopSendKeys("RegisterPage_RegConfirmPassword", "123456", 3, 10);
			// 验证码
			IsExistCode(registercode);
			op.loopClickElement("RegisterPage_RegBtn", 3, 20, 20);
			getErrInfopassword= op.loopGetElementText("RegisterPage_RegErrInfoEmailAlreadyInUse", 3, 20);
			Assert.assertEquals(getErrInfopassword, testExpectedStr);
			Pub.printTestCaseInfo(info);
		} catch (Exception e) {
			e.printStackTrace();
			Log.logError("实际获取的内容为=" + getErrInfopassword);
			Assert.fail();
		}
		

	}
	
	/**
	 * 
	* @测试点: 无效邮箱注册失败 
	* @验证点: 1.提示请输入有效邮箱“Please enter a valid email address”
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月16日 
	  @修改说明
	 */
	
	@Test
	public void register_wrongemail() {
		driver.navigate().refresh();
		String getEmailErrorText = "";
		try {
			requestId = Pub.getRandomString(6);
			testExpectedStr="Please enter a valid email address";
			registerName="zhang@qqcom";
			registerPassword="123456";
			registerConfirmPassword="123456";
			registercode=InterfaceMethod.IF_Verification(driver, myPublicUrl2, requestId, getkeys);
			
			op.loopSendKeys("RegisterPage_Email",registerName , 3, 20);
			op.loopSendKeys("RegisterPage_Password", registerPassword, 3, 10);
			op.loopSendKeys("RegisterPage_RegConfirmPassword", registerConfirmPassword, 3, 10);
			// 验证码
			IsExistCode(registercode);
			
			op.loopClickElement("RegisterPage_RegBtn", 3, 5, 20);
			getEmailErrorText = op.loopGetElementText("RegisterPage_RegErrInfoEmailAlreadyInUse",10, 30);
			Assert.assertEquals(testExpectedStr, getEmailErrorText);
			Pub.printTestCaseInfo(info);
		} catch (Exception e) {
			e.printStackTrace();
			Log.logError("实际获取的内容为=" + getEmailErrorText);
			Assert.fail();
		}
	}
	
	/**
	 * 
	* @测试点:密码和确认密码不一致，导致注册失败
	* @验证点: 1.提示Enter the same password as above
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月16日 
	  @修改说明
	 */
	@Test
	public void register_inconformityfail() {
		String getEmailErrorText="";
		try {
			//driver.navigate().refresh();
			op.navigateRefresh(40, 3, 60);
			requestId = Pub.getRandomString(6);
			testExpectedStr="Enter the same password as above";
			registerName="autotest05@globalegrow.com";
			registerPassword="123456";
			registerConfirmPassword="123456789";
			registercode=InterfaceMethod.IF_Verification(driver, myPublicUrl2, requestId, getkeys);
			op.loopSendKeys("RegisterPage_Email",registerName , 3, 20);
			op.loopSendKeys("RegisterPage_Password", registerPassword, 3, 10);
			op.loopSendKeys("RegisterPage_RegConfirmPassword", registerConfirmPassword, 3, 10);
			//验证码
			IsExistCode(registercode);
			op.loopClickElement("RegisterPage_RegBtn", 3, 5, 20);
			getEmailErrorText= op.loopGetElementText("RegisterPage_RegErrInfoPasswordNot",10, 30);
			Assert.assertEquals(testExpectedStr, getEmailErrorText);
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("实际获得文本=" + getEmailErrorText);
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}

	}
	
	/**
	 * 
	* @测试点: 密码<6,注册失败 
	* @验证点: 提示Enter at least 6 characters
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月17日 
	  @修改说明
	 */
	@Test
	public void register_passwordLength() {
		driver.navigate().refresh();
		String getEmailErrorText="";
		try {
			op.navigateRefresh(40, 3, 60);
			InterfaceMethod.IF_DelUserAccount2(domainName, new_register_email);
		    requestId = Pub.getRandomString(6);
			testExpectedStr="Please enter at least 8 characters";
			registerName="autotest05@globalegrow.com";
			registerPassword="123";
			registerConfirmPassword="1234";
			registercode=InterfaceMethod.IF_Verification(driver, myPublicUrl2, requestId, getkeys);
			op.loopSendKeys("RegisterPage_Email",registerName , 3, 20);
			op.loopSendKeys("RegisterPage_Password", registerPassword, 3, 10);
			op.loopSendKeys("RegisterPage_RegConfirmPassword", registerConfirmPassword, 3, 10);
			// 验证码
			IsExistCode(registercode);
			op.loopClickElement("RegisterPage_RegBtn", 3, 5, 20);
			getEmailErrorText = op.loopGetElementText("RegisterPage_RegErrInfoLessthan6", 10,60);
			Assert.assertEquals(testExpectedStr, getEmailErrorText);
		
		} catch (Exception e) {
			e.printStackTrace();

			Log.logWarn("实际获得文本=" + getEmailErrorText);
			Assert.fail();
		}

	}

	
	/**
	 * 
	* @测试点: 不勾选协议，注册失败 
	* @验证点:点击注册按钮后，提示必须同意条款
	提示“To complete the registration, you must agree to rosewholesale's website Terms and Conditions.”		 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月17日 
	  @修改说明
	 */
	@Test
	public void register_dontcheck() {
		driver.navigate().refresh();
		String getRegisterErrorText=""; 
		try {
			requestId = Pub.getRandomString(6);
			testExpectedStr="To complete the registration, you must agree to rosewholesale's website Terms and Conditions.";
			registerName="autotest05@globalegrow.com";
			registerPassword="123456";
			registerConfirmPassword="123456";
			registercode=InterfaceMethod.IF_Verification(driver, myPublicUrl2, requestId, getkeys);
			op.loopSendKeys("RegisterPage_Email",registerName , 3, 20);
			op.loopSendKeys("RegisterPage_Password", registerPassword, 3, 10);
			op.loopSendKeys("RegisterPage_RegConfirmPassword", registerConfirmPassword, 3, 10);
			// 验证码
			IsExistCode(registercode);
			
			op.loopClickElement("RegisterPage_Uncheck", 3, 5, 20);//去掉协议
			op.loopClickElement("RegisterPage_RegBtn", 3, 5, 20); //注册
			getRegisterErrorText= op.loopGetElementText("RegisterPage_RegErrInfoUncheck",	10, 30);
			Assert.assertEquals(testExpectedStr, getRegisterErrorText);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	/**
	 * 
	 * @测试点: 注册时查看条款
	 * @验证点:注册页面的条款处链接正确,跳转到注册条款页：http://www.rosewholesale.com/m-article-id-36.
	 * html @使用环境： 测试 @备注：
	 * @author zhangjun
	 * @date 2016年11月23日
	 */
	@Test
	public void register_terms() {

		driver.navigate().refresh();
		String getTermsText="";
		try {			
			String currentWindow = driver.getWindowHandle();
			System.out.println("currentWindow:"+currentWindow);	
			String testExpectedStr="Terms and Conditions";
			op.loopClickGoPageURL("RegisterPage_Terms", 10, 20,  60, 60);
			Page.pause(4);
			for (String winHandle:driver.getWindowHandles()){
			    Log.logInfo("获得所有的句柄"+winHandle);	
			    if(winHandle.equals(currentWindow)){
			    	continue;			
			    }else{
			    	Log.logInfo("取得的句柄"+winHandle);
			    	driver.switchTo().window(winHandle);
			 
			    	getTermsText=op.loopGetElementText("RegisterPage_TermsText", 10, 60);
					Log.logInfo("取得的文本:" + getTermsText);
					Assert.assertEquals(getTermsText,testExpectedStr);
			    }			    
			}
			driver.close();
			driver.switchTo().window(currentWindow);//切回原来的窗口
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("实际获得的文本为：" + getTermsText);
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}

	}
	/**
	 * 
	 * @测试点:验证码不正确，导致注册失败
	 * @验证点: 提示：The code appears to be wrong. Please try again
	 * @使用环境： 测试环境
	 * @备注：register_verificationerror和注册成功a,b,c关联性强
	 *      
	 * @author zhangjun
	 * @date 2016年12月5日
	 */
	@Test
	public void register_verificationerror() {	
		String getEmailErrorText="";
		try {
			op.navigateRefresh(40, 3, 60);
		    String keys="123456";
		    testExpectedStr="THE CODE APPEARS TO BE WRONG. PLEASE TRY AGAIN.";
		    registerName="autotest05@globalegrow.com";
			registerPassword="zhang123456";
			registerConfirmPassword="zhang123456";
			op.loopSendKeys("RegisterPage_Email",registerName , 3, 20);
			op.loopSendKeys("RegisterPage_Password", registerPassword, 3, 10);
			op.loopSendKeys("RegisterPage_RegConfirmPassword", registerConfirmPassword, 3, 10);						
			
			//验证码
			op.loopSendKeys("RegisterPage_Code", keys, 3, 20);		
			op.loopClickElement("RegisterPage_RegBtn", 3, 5, 20); //注册
			getEmailErrorText = op.loopGetElementText("RegisterPage_ErrorCode",3, 40);
			Log.logInfo("实际获得的文本内容为:"+getEmailErrorText);
			Assert.assertEquals(testExpectedStr, getEmailErrorText);
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("getEmailErrorText:" + getEmailErrorText);
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}
	
	
	
	
	/**
	 * 
	* @测试点: 注册成功，收到积分和促销码
	* @验证点: 1.赠送10个积分，赠送1个促销码
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月18日 
	  @修改说明
	 */
	
	@Test
	public void register_succeed_a() {
		correFail = false;
		String	getIntegralStr="";
		driver.navigate().refresh();
		
		try {
			InterfaceMethod.IF_DelUserAccount2(domainName, new_register_email);
			String requestId = Pub.getRandomString(6);
			Page.pause(5);
			 testExpectedStr = "";			
			registerName="autotest05@globalegrow.com";
			registerPassword="zhang123456";
			registerConfirmPassword="zhang123456";
			registercode=InterfaceMethod.IF_Verification(driver, myPublicUrl2, requestId, getkeys);
			if(op.isElementPresent("RegisterPage_Email")){
				op.loopSendKeys("RegisterPage_Email",registerName , 3, 20);
				op.loopSendKeys("RegisterPage_Password", registerPassword, 3, 10);
				op.loopSendKeys("RegisterPage_RegConfirmPassword", registerConfirmPassword, 3, 10);
				IsExistCode(registercode);
				op.loopClickElement("RegisterPage_RegBtn", 3, 5, 20); //注册
			}
				//因为新用户会跳出很多活动页面，所以我们直接跳转到登录成功页
				op.loopGet("https://userm.rosewholesale.com/user/index/userindex", 5, 5, 10);
				if(op.isElementPresent("registerSucces2", 10)||op.isElementPresent("registerSucces", 10)){
					Log.logInfo("注册成功");	
				}else{
					Log.logError("注册失败");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				Pub.printTestCaseExceptionInfo(info);
				Log.logWarn("实际获取的文本内容为：" + getIntegralStr);
				Log.logWarn(info.testCorrelation);
				Assert.fail();
		
		}
	}

	/**
	 * 
	* @测试点: 注册成功，接口查询能返回内容
	* @验证点: 返回1就说明接口查询成功
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月18日 
	  @修改说明
	 */
	@Test
	public void register_success_b() {
		Pub.checkStatusPhoneBrowser();
		try {
			int testExpectedInt = 1;
			Log.logInfo("new_register_email:"+new_register_email);
			int res =InterfaceMethod.IF_QueryUserRegisterStatus(domainName, new_register_email);
			Page.pause(5);
			Pub.assertInfo("assertEquals", "", "");
			Assert.assertEquals(testExpectedInt, res); // checkpoint
			Pub.printTestCaseInfo(info);
		} catch (Exception e) {
			e.printStackTrace();
			Pub.printTestCaseExceptionInfo(info);
			Log.logWarn("接口返回错误");
			Log.logWarn(info.testCorrelation);
			Assert.fail();
	
		}
	}
	/**
	 * 
	 * @测试点:注册成功，收到欢迎邮件
	 * @验证点: 使用邮箱注册成功后，邮箱收到欢迎邮件 @使用环境： 测试
	 * @备注： 
	 * @author zhangjun
	 * @date 2016年11月29日
	 */
	@Test
	public void register_succeed_c() {		
		Pub.checkStatusPhoneBrowser();
	      try{
		   
			String passwrod = "nuH%qE2zL7";//密码
		  	String dress = "Rosewholesale.com";//发送邮件地址
		  	String subject = "[Rosewholesale] Congratulations! You have successfully registered - you’re ready to start saving big!";//发送邮件主题
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
					mailContext = test.resmg(new_register_email,passwrod,dress,subject,sendEmailDate_2);
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
				Log.logInfo("注册新用户,邮箱autotest05@globalegrow.com，用户可正常收到邮件，验证成功！！！");
			}else {
				Log.logError("注册新用户,邮箱autotest05@globalegrow.com，用户无法收到邮件，验证失败！！！");
			}	
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("(" + info.testCheckpoint + ").");
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}
		
	
	public void urlInit(){
		if(baseURL.contains(".a.")){	//测试环境	
			domainName="http://rosewholesale.com.d.s1.egomsl.com";
			myPublicUrl2="http://m.wap-rosewholesale.com.a.s1cg.egomsl.com";//获取验证码的接口
		}else if(baseURL.contains(".trunk.")){
			domainName="http://rosewholesale.com.trunk.s1.egomsl.com/";
			myPublicUrl2="http://m.wap-rosewholesale.com.trunk.s1cg.egomsl.com";//获取验证码的接口
		}else{//正式环境
			domainName="https://rosewholesale.com";
			myPublicUrl2="https://m.rosewholesale.com";//获取验证码的接口
		}
			
	}
	
}
