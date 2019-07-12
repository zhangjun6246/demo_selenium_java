package com.globalegrow.rosewholesalea;


import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.beust.jcommander.Parameter;

import com.globalegrow.base.StartApp;

import com.globalegrow.code.Page;
import com.globalegrow.custom.ControlUsingMethod;
import com.globalegrow.custom.InterfaceMethod;
import com.globalegrow.util.Log;
import com.globalegrow.util.MailReceiver;
import com.globalegrow.util.Pub;
import com.globalegrow.util.testInfo;

public class RW_A_register extends StartApp {
	private String className = GetClassName();
	private String testCaseProjectName = "rosewholesale";
	private testInfo info = null;
	public String baseURL = "";
	private String domainName="";
	private String RegisterEmail="autotest07@globalegrow.com";
	private   long setexplicitWaitTimeout=20;
	private String nickName="jun";
	private String baseUrl;
	private String packageName;//包名
	private String  appactivity;//app的activity
	InterfaceMethod	interfaceMethod;
	boolean status=false;//是否弹出网络不好的框
	
	public RW_A_register(String testurl) {
		moduleName = className.substring(className.lastIndexOf(".") + 1);
		projectName = className.substring(className.indexOf(testCaseProjectName), className.lastIndexOf("."));
	}
	
	@Parameters({ "testUrl","appPackage","appActivity" })
	@BeforeClass
	public void beforeClass(String testurl,String appPackage,String appActivity) {
		String methodName = GetMethodName();
		//stratTime=new Date();
		String methodNameFull = moduleName + "." + methodName;
		Log.logInfo("**************Ready to test (" + moduleName + ")!!!**************\n");
		Log.logInfo("(" + methodNameFull + ")...beforeClass start...");

		try {
			packageName=appPackage;
			appactivity=appActivity;
			info = new testInfo(moduleName);
			start();
			baseUrl=testurl;
			chooseBranch();
			opApp.setScreenShotPath(screenShotPath);	
			interfaceMethod=new InterfaceMethod();
			InterfaceMethod.IF_DelUserAccount2(domainName, RegisterEmail);
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
			InterfaceMethod.IF_DelUserAccount2(domainName, RegisterEmail);
		/*	driver.quit();*/
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
	* @测试点: 重复注册失败 
	* @验证点: 邮箱己注册过，注册失败 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月10日 
	  @修改说明
	 */
	@Test
	public void register_repetition(){
		correFail = false;
		RegisterEmail="zhang@qq.com";
		try {
			InterfaceMethod.IF_DelUserAccount2(domainName, RegisterEmail);
			if(status==true){
				Log.logError("网络不好，打不开这个APP,不执行脚本");
			}
			registeredPublicMethods(RegisterEmail,"123456","123456");
		} catch (Exception e) {
			e.printStackTrace();
			driver.quit();
		}
	}
	
	/**
	 * 
	* @测试点:  密码和确认密码不一致，导致注册失败
	* @验证点: 1.提示Enter the same password as above
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月22日 
	  @修改说明
	 */
	@Test
	public void register_inconformityfail(){
		correFail = false;
		try {
			RegisterEmail="autotest07@globalegrow.com";
			registeredPublicMethods(RegisterEmail,"123456","123456789");
		} catch (Exception e) {
			e.printStackTrace();
		
		}
	}
	
	/**
	 * 
	* @测试点: 密码<6,注册失败 
	* @验证点: 密码小于6位，导致注册失败
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月22日 
	  @修改说明
	 */
	@Test
	public void register_passwordLength(){
		correFail = false;
		try {
			registeredPublicMethods(RegisterEmail,"123","123");
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	/**
	 * 
	* @测试点: 不勾选协议，注册失败 
	* @验证点: 不勾选条款，导致注册失败
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月22日 
	  @修改说明
	 */
	@Test
	public void register_dontcheck(){
		correFail = false;
		try {
			IsBeChecked(false);
			registeredPublicMethods(RegisterEmail,"123456","123456");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	* @测试点: 注册成功接口查询 
	* @验证点: 使用邮箱注册成功 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月22日 
	  @修改说明
	 */
	@Test
	public void register_succeed_a(){
		correFail = false;
		try {
			if(opApp.elementExists(5, "homeaccount")){
				opApp.ClickElement("homeaccount", setexplicitWaitTimeout);
				opApp.ClickElement("signUP", setexplicitWaitTimeout);
			}
			opApp.SendKeysClean("signEami", RegisterEmail, setexplicitWaitTimeout);
			opApp.SendKeysClean("signPassword", "123456", setexplicitWaitTimeout);
			opApp.SendKeysClean("signConfirmPassword", "123456", setexplicitWaitTimeout);
			opApp.SendKeysClean("signNickName",nickName, setexplicitWaitTimeout);
			IsBeChecked(false);
			opApp.ClickElement("signJonNow", setexplicitWaitTimeout);
			

			if(opApp.elementExists(40, "homeaccount")){
				int testExpectedInt = 1;
				int res =InterfaceMethod.IF_QueryUserRegisterStatus(domainName, RegisterEmail);
				Page.pause(2);
				Assert.assertEquals(testExpectedInt, res); // checkpoint
				Log.logInfo("用户注册成功，并跳转到了主页，并通过接口查询接口的方式查询到内容，验证通过");
				
			}else{
				Log.logError("用户注册成功，没并跳转到了主页，验证失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	* @测试点: 注册成功，收到欢迎邮件 
	* @验证点:使用邮箱注册成功后，邮箱收到欢迎邮件
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月22日 
	  @修改说明
	 */
	@Test
	public void register_success_b(){
		correFail = false;
		try {
			MailReceiver MR = new MailReceiver();
		    String subject="[Rosewholesale] Congratulations! You have successfully registered - you’re ready to start saving big!";
		                   
		    String mail_content = null;
			long time_start_check = System.currentTimeMillis();
			long check_receive_email = 120;
	        boolean rec_email_falg = false;
	        String sender = "rosewholesale.com"; //发送邮件
			while (true) {
				Thread.sleep(1000);
				try {
					mail_content = MR.resmg(RegisterEmail, "nuH%qE2zL7", sender, subject);					
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
				if (System.currentTimeMillis() - time_start_check > check_receive_email * 1000) {// 当前时间-当前时间>120s// 1000就是1s																	
					break;
				}
				if (Pub.hasLength(mail_content)) {
					Log.logInfo("进来了");
					rec_email_falg = true;
					break;
				}
			}
			if (rec_email_falg) { // checkpoint
				Log.logWarn("在" + check_receive_email + "秒内,用户邮箱收到注册成功的提示邮件,验证通过.");
				
			}else{
			Log.logInfo("在" + check_receive_email + "秒内,用户邮箱没有收到注册成功的提示邮件,验证不通过.");
			Assert.fail();
			}
		/*	try {

				MailReceiver MR = new MailReceiver();
				String subject="[Rosewholesale] Congratulations! You have successfully registered - you’re ready to start saving big!";
				long check_receive_email = 40;
				String mail_content = null;
				long time_start_check = System.currentTimeMillis();
				boolean rec_email_falg = false;
				 String sender = "rosewholesale.com"; //发送邮件
				while (true) {
					Thread.sleep(1000);
					try {
						mail_content = MR.resmg(RegisterEmail, "autotest123456!", sender, subject);
						
						//mail_content = MR.resmg(RegisterEmail, "nuH%qE2zL7", sender, subject);
					} catch (Exception e) {
						e.printStackTrace();
						break;
					}
					if (System.currentTimeMillis() - time_start_check > check_receive_email * 1000) {
						break;
					}
					if (Pub.hasLength(mail_content)) {
						rec_email_falg = true;
						break;
					}
				}
				if (!rec_email_falg) { // checkpoint
					Log.logWarn("在" + check_receive_email + "秒内,用户邮箱未收到注册成功的提示邮件.");
					Assert.fail();
				}
				Log.logInfo("在" + check_receive_email + "秒内,用户邮箱收到注册成功的提示邮件.");*/

		

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	* @测试点: 注册页面错误提示 
	* @验证点: 输入一些错误的内容，不满足注册的条件，不跳转到注册成功页面 
	* @param emaile  输入的邮件地址
	* @param Password 输入的密码
	* @param signConfirmPassword  确认密码
	* @使用环境：测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月22日 
	  @修改说明
	 */
	public void registeredPublicMethods(String emaile,String Password,String signConfirmPassword){
		if(opApp.elementExists(5, "homeaccount")){
			opApp.ClickElement("homeaccount", setexplicitWaitTimeout);
			opApp.ClickElement("signUP", setexplicitWaitTimeout);
		}		
		opApp.SendKeysClean("signEami", emaile, setexplicitWaitTimeout);
		opApp.SendKeysClean("signPassword", Password, setexplicitWaitTimeout);
		opApp.SendKeysClean("signConfirmPassword", signConfirmPassword, setexplicitWaitTimeout);
		opApp.SendKeysClean("signNickName",nickName, setexplicitWaitTimeout);
		opApp.ClickElement("signJonNow", setexplicitWaitTimeout);
		
		if(opApp.elementExists(5,"signJonNow")){
			Log.logInfo("页面未跳转到其他页面，验证成功");
		}else{
			Log.logError("页面跳转到了其他页面，验证失败");
		}
	}
	
	/**
	 * 
	* @验证点: 是否已点击了复选框.
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月23日 
	  @修改说明
	 */
	public void IsBeChecked(boolean flag){
		WebElement  agreeClick=opApp.waitForElement("signAgree");
		if(agreeClick.isSelected()==flag){//已经选择了控件，在点击，就成了取消按钮了agreeClick.isSelected()==false，代表已经选择
			agreeClick.click();
		}
	}

	
	public void chooseBranch(){
 		if(baseUrl.contains("trunk")){
			domainName="http://www.rosewholesale.com.trunk.s1.egomsl.com";
		}else{
			domainName="https://www.rosewholesale.com";
		}
	}
}
