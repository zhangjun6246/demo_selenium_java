package com.globalegrow.rosewholesale;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.globalegrow.base.Startbrowser;
import com.globalegrow.code.Page;
import com.globalegrow.custom.InterfaceMethod;
import com.globalegrow.util.Log;
import com.globalegrow.util.MailReceiver;
import com.globalegrow.util.Pub;
import com.globalegrow.util.testInfo;


public class RW_register extends Startbrowser {
	private String className = GetClassName(); // 必要
	private String myProjectName = GetProjectName(); // 必要
	private String baseURL =null;

	private String classNameShort;
	
	private final String exceptionNote = "执行流程异常";
	private testInfo info = null;
	private String domainName;
	public String domainNameCmd = ""; // 用于接口命令
	private String testCaseProjectName = "rosewholesale";
	public Date sendEmailDate = new Date();
	private String new_register_email = "autotest02@globalegrow.com";
	private String register_email_user_password = "autotest123456!";
	
	private String testExpectedStr; //获取的对比项
	private String getkeys; //关键字key
	private long explicitWaitTimeoutLoop = 20;
	private long explicitWaitTimeout3s = 3;
	private String requestId;
	private String comparedAddress;//对比地址
	@Parameters({ "testUrl" }) // 必要,二选一
	private RW_register(String testUrl) { // 必要
		moduleName = className.substring(className.lastIndexOf(".") + 1); // 必要
		info = new testInfo(moduleName); // 必要
		baseURL = testUrl;
	}

	@Parameters({ "browserName", "keys" })
	@BeforeClass
	public void beforeClass(String browserName, String keys) {
		String methodName = GetMethodName();
		String methodNameFull = moduleName + "." + methodName;
		Log.logInfo("**************Ready to test (" + moduleName + ")!!!**************\n");
		Log.logInfo("(" + methodNameFull + ")...beforeClass start...");
		// 加载登录
		try {
			start(browserName); // 必要,初始化driver,web,二选一
			driver = super.getDriver(); // 启动driver
			driver.manage().timeouts().pageLoadTimeout(pageLoadTimeoutMax, TimeUnit.SECONDS);
			op.loopGet(baseURL, 30, 4, 80);
			getkeys=keys;
			Log.logInfo("baseURL:" + baseURL);
			urlInit();
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			Log.logInfo("(" + methodNameFull + ")...beforeClass stoop...\n\n");
		}
	}
	
	

	/**
	 * 
	* @测试点: afterClass 
	* @验证点: 删除用户 
	* @使用环境：     测试环境
	* @备注： void    
	* @author zhangjun 
	* @date 2016年12月5日
	 */
	@AfterClass
	public void afterClass() {
		try {
			InterfaceMethod.IF_DelUserAccount2(domainName, new_register_email);
			Thread.sleep(3000);
			beforeTestRunFlag = false;
			driver.quit();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	/**
	 * 
	 * @测试点: afterMethod
	 * @验证点: TODO(这里用一句话描述这个方法的作用) @使用环境： 测试环境 @备注： 每个测试用例执行完成时，延时1秒并打印相关信息
	 * @author zhangjun
	 * @date 2016年11月30日
	 */
	@AfterMethod
	public void afterMethod() {
		String methodName = GetMethodName();
		Log.logInfo("(" + methodName + ")...afterMethod stoop...\n\n");
		Pub.sleep(1);
	}
	
	
	
	/**
	 * 
	* @测试点: 判断是否存在验证码输入框 
	* @验证点: 不存在就不输入，存在就输入 
	* @使用环境： @param registercode 获取的验证码   
	* 测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月9日 
	  @修改说明
	 */
	public void IsExistVerification(String registercode){
		if(op.isElementPresent("Register_verificationcodeInput")){
			try {
				op.loopSendKeys("Register_verificationcodeInput", registercode, 3, explicitWaitTimeoutLoop);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			Log.logInfo("验证码输入框已被关闭，不输入验证码登录");
		}
	}
	

	/**
	 * 
	 * @测试点: 重复注册失败
	 * @验证点: 邮箱己注册过，注册失败,提示The email address is already in use”
	 *  @使用环境： 设定文件 
	 *  @备注：测试环境
	 * @author zhangjun
	 * @date 2016年11月28日
	 */
	

	@Test
	public void register_repetition() {
		try {
			op.navigateRefresh(30, 3, 60);
			 requestId = Pub.getRandomString(6);
			 testExpectedStr = "The email address is already in use";
			op.loopSendKeys("Register_EmailInput", "zhang@qq.com", 3, 20);
			op.loopSendKeys("Register_passwordInput", "autotest123456!", 3, 10);
			op.loopSendKeys("Register_passwordconfirmInput", "autotest123456!", 3, 10);
			String registercode = InterfaceMethod.IF_Verification(driver, domainName, requestId, getkeys, "regist");// 验证码
			IsExistVerification(registercode);//是否存在验证码
			
			op.loopClickElement("Register_joinBtn", 3, 20, 20);
			
			String getErrInfopassword = op.loopGetElementText("regPage_ErrInfoEmailAlreadyInUse", 3, 20);
			if(baseURL.contains("/es/")){
				Log.logInfo("使用的西班牙语言");
				testExpectedStr="La dirección de correo electrónico ya está en uso";
			}
			Assert.assertEquals(getErrInfopassword, testExpectedStr);	
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @测试点: 错误邮箱注册失败
	 * @验证点: 无效邮箱(邮箱字符串中不包含@的字符串)，注册失败，提示：Please enter a valid emailaddress” 
	 * @使用环境： 测试环境
	 * @备注：无效邮箱(邮箱字符串中不包含@的字符串)，导致注册失败， @author zhangjun
	 * @date 2016年11月23日
	 */

	@Test
	public void register_wrongemail() {
		driver.navigate().refresh();		
		try {
			requestId = Pub.getRandomString(6);
			testExpectedStr ="Please enter a valid email address";
			op.loopSendKeys("Register_EmailInput","zhangglobalegrow.com", 3, 10);
			op.loopSendKeys("Register_passwordInput","autotest123456!", 3, 10);
			op.loopSendKeys("Register_passwordconfirmInput", "autotest123456!", 3, 10);
			
			//验证码
			String registercode = InterfaceMethod.IF_Verification(driver, domainName, requestId, getkeys, "regist");
			IsExistVerification(registercode);//是否存在验证码输入框
			op.loopClickElement("Register_joinBtn", 3, 5, explicitWaitTimeoutLoop);
			String getEmailErrorText = op.loopGetElementText("Register_ErrInfoEmailmistake",explicitWaitTimeout3s, explicitWaitTimeoutLoop);
			
			if(baseURL.contains("/es/")){
				Log.logInfo("使用的西班牙语言");
				testExpectedStr="Por favor, introduzca una dirección de correo electrónico válida";
			}
			Assert.assertEquals(testExpectedStr, getEmailErrorText);
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 密码不一致，注册失败
	 * @验证点: 密码和确认密码不一致，注册失败，提示 Enter the same password as above @使用环境： 测试环境
	 * @备注：是通过移动焦点，获取错误提示，对比错误，如果出现相同的预先定义错误，那么断言就成功
	 * @author zhangjun
	 * @date 2016年11月23日
	 */
	
	@Test
	public void register_inconformityfail() {
		try {
			driver.navigate().refresh();
			requestId = Pub.getRandomString(6);
			testExpectedStr = "Enter the same password as above";
			op.loopSendKeys("Register_EmailInput", "Enter the same password as above", 3, 10);
			op.loopSendKeys("Register_passwordInput", "123456", 3, 10);
			op.loopSendKeys("Register_passwordconfirmInput","123456789", 3, 10);
			//验证码
			String registercode = InterfaceMethod.IF_Verification(driver, domainName, requestId, getkeys, "regist");			
			IsExistVerification(registercode);//是否存在验证码输入框
			op.loopClickElement("Register_joinBtn", 3, 5, explicitWaitTimeoutLoop);
			String getEmailErrorText = op.loopGetElementText("Register_ErrInfoEnterSamePassword",explicitWaitTimeout3s, explicitWaitTimeoutLoop);
			if(baseURL.contains("/es/")){
				Log.logInfo("使用的西班牙语言");
				testExpectedStr="Escriba la misma contraseña que escribio anteriormente";
			}
			Assert.assertEquals(testExpectedStr, getEmailErrorText);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	/**
	 * 
	 * @测试点: 密码<6,注册失败
	 * @验证点: 密码小于6位，导致注册失败,提示Enter at least 6 characters @使用环境： @备注：
	 * @author zhangjun
	 * @date 2016年11月23日
	 */
	@Test
	public void register_passwordLength() {
		driver.navigate().refresh();
		try {
			requestId = Pub.getRandomString(6);
			 testExpectedStr = "Enter at least 8 characters";
			op.loopSendKeys("Register_EmailInput","autotest02@globalegrow.com", 3, 10);
			op.loopSendKeys("Register_passwordInput", "1234", 3, 10);
			op.loopSendKeys("Register_passwordconfirmInput", "1234", 3, 10);
			// 验证码
			String registercode = InterfaceMethod.IF_Verification(driver, domainName, requestId, getkeys, "regist");
			IsExistVerification(registercode);//是否存在验证码输入框
			op.loopClickElement("Register_joinBtn", 3, 5, explicitWaitTimeoutLoop);
			String getEmailErrorText = op.loopGetElementText("Register_ErrInfopslength", explicitWaitTimeout3s,explicitWaitTimeoutLoop);
			if(baseURL.contains("/es/")){
				Log.logInfo("使用的西班牙语言");
				testExpectedStr="introduzca al menos de 8 caracteres";
			}
			Assert.assertEquals(testExpectedStr, getEmailErrorText);
		
		} catch (Exception e) {
			e.printStackTrace();
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
		driver.navigate().refresh();
		String getEmailErrorText = "";
		try {
			String testExpectedStr = "The code appears to be wrong. Please try again.";
			String keys = "123456";
			InterfaceMethod.IF_DelUserAccount2(domainName, new_register_email);
			if (op.isElementPresent("Register_verificationcodeInput")) {
				op.loopSendKeys("Register_EmailInput", "autotest02@globalegrow.com", 3, 10);
				op.loopSendKeys("Register_passwordInput", "autotest123456!", 3, 10);
				op.loopSendKeys("Register_passwordconfirmInput","autotest123456!", 3, 10);
				// 验证码
				IsExistVerification(keys);
				op.loopClickElement("Register_joinBtn", 3, 5, explicitWaitTimeoutLoop);
				Page.pause(4);
				getEmailErrorText = op.loopGetElementText("Register_ErrInfcode", 5, 20);
				if(baseURL.contains("/es/")){
					Log.logInfo("使用的西班牙语言");
					testExpectedStr="El código parece estar mal. Por favor, inténtelo de nuevo.";
				}
				Assert.assertEquals(testExpectedStr, getEmailErrorText);
			} else {
				Log.logInfo("验证码已经被关闭，用例不执行，跳过");
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		try {
		/*	op.loopGet("http://es.rosewholesale.com/m-article-id-36.html", 20, 60, 60);
			System.out.println(driver.getTitle());*/
			
				String currentWindow = driver.getWindowHandle();
			if(baseURL.contains("/es/")){
				op.loopClickElement("Register_ViewTermses",3, 5, explicitWaitTimeoutLoop);//使用的西班牙语
			}else{
				op.loopClickElement("Register_ViewTerms", 3, 5, explicitWaitTimeoutLoop);
			}
			if (!op.switchToWindow( "Terms and Conditions")) { // checkpoint1
				if(!op.switchToWindow( "Términos y Condiciones")){
					Log.logWarn("切换窗口失败(Terms and Conditions),测试中止.");
				}
			}			
			//Log.logInfo("切换窗口成功(Terms and Conditions).");			
			String getWindowURL = driver.getCurrentUrl();	
			Page.pause(3);
			Log.logInfo("testExpectedStr:" +comparedAddress);
			Log.logInfo("getWindowURL:" + getWindowURL);
               if(comparedAddress.equals(getWindowURL)){
            	   Log.logInfo("获取的url地址与实际url一致，验证通过");
               }else{
            	   Log.logError("获取的url地址与实际url不一致，验证失败，实际获取的url"+getWindowURL+"对比的url为："+testExpectedStr); 
               }
			Page.closeOtherWindow(driver, currentWindow);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	/**
	 * 
	 * @测试点: 不勾选协议，注册失败
	 * @验证点: 点击注册按钮后，提示必须同意条款, 提示“To complete the registration, you must agreeto rosewholesale's 
	 * website Terms and Conditions. @使用环境： 设定文件
	 *  @备注：测试
	 * @author zhangjun
	 * @date 2016年11月28日
	 */
	@Test
	public void register_dontcheck() {
		driver.navigate().refresh();
		try {
			String requestId = Pub.getRandomString(6);
			String testExpectedStr = "To complete the registration, you must agree to rosewholesale\'s website Terms and Conditions.";		
			op.loopSendKeys("Register_EmailInput", "autotest02@globalegrow.com", 3, 10);
			op.loopSendKeys("Register_passwordInput", "autotest123456!", 3, 10);
			op.loopSendKeys("Register_passwordconfirmInput", "autotest123456!", 3, 10);
			//验证码
			String registercode = InterfaceMethod.IF_Verification(driver, domainName, requestId, getkeys, "regist");		
			IsExistVerification(registercode);//是否存在验证码输入框
			op.loopClickElement("Register_AgreementCheckbox", 3, 5, explicitWaitTimeoutLoop);//去掉协议
			op.loopClickElement("Register_joinBtn", 3, 5, explicitWaitTimeoutLoop); //注册
			String getEmailErrorText = op.loopGetElementText("Register_ErrInfoAgreeTerms",	explicitWaitTimeout3s, explicitWaitTimeoutLoop);
			if(baseURL.contains("/es/")){
				testExpectedStr="Para completar el registro, debe estar de acuerdo con los Términos y Condiciones del sitio web de rosewholesale's.";
			}
			Assert.assertEquals(testExpectedStr, getEmailErrorText);
		
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	/**
	 * 
	 * @测试点: 注册成功 收到积分和促销码
	 * @验证点: 使用邮箱注册成功后，收到赠送10个积分，和1个促销码 ,目前没有提供删除接口，先登录在获取 @使用环境： 测试
	 * @备注：register_verificationerror和注册成功register_succeed（a,b,c）关联性强
	 * @author zhangjun
	 * @date 2016年11月28日
	 */

	@Test
	public void register_succeed_a() {
		try {
			InterfaceMethod.IF_DelUserAccount2(domainName, new_register_email);
			if (op.isElementPresent("Register_SeemoreLink",5)||op.isElementPresent("Register_SeemoreLink_es",5)) {// 避免第一次注册失败后，已经在页面了，还不能继续执行
				Log.logInfo("页面已经登录了，第二次获取页面内容");
				registrationVerificationPoints();
			} else {
				requestId = Pub.getRandomString(6);
				number++;
				String registercode = InterfaceMethod.IF_Verification(driver, domainName, requestId, getkeys, "regist");
				if (registercode.equals("verify") && number < 6) {
					driver.navigate().refresh();
					Page.pause(3);
					register_succeed_a();
				} else {
					op.loopSendKeysClean("Register_EmailInput", "autotest02@globalegrow.com", 3);
					op.loopSendKeysClean("Register_passwordInput","autotest123456!", 3);
					op.loopSendKeysClean("Register_passwordconfirmInput", "autotest123456!", 3);
					IsExistVerification(registercode);// 是否存在验证码输入框
					op.loopClickElement("Register_joinBtn", 3, 5, explicitWaitTimeoutLoop); // 注册按钮
					
					String LoginStats = op.loopGetElementText("signlinkName", 5, 20);
					if (LoginStats.contains("Hi, autot")) {
						Log.logInfo("注册成功");
						
					}else{
						Log.logError("注册失败");
					}
					// 设置超时
				/*	try {
						driver.manage().timeouts().pageLoadTimeout(80, TimeUnit.SECONDS);
					} catch (Exception e) {
						Log.logInfo("注册页面加载超时，刷新页面");
						driver.navigate().refresh();
					} finally {
						registrationVerificationPoints();
					}*/
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	int number = 0;
	
	/**
	 * 
	* @测试点: 登陆后获取赠送的促销码和积分 
	* @验证点: 为了避免注册后，登陆到页面找不到控件，
	* @使用环境： @throws Exception    测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年3月9日 
	  @修改说明
	 */
	public void registrationVerificationPoints() throws Exception{
		String testExpectedStr2 = "";
	
		if(baseURL.contains("/es/")){
			testExpectedStr ="Finalice el registro y obtega 10 puntos";
			testExpectedStr2="R Puntos obtenidos por iniciar sesión";
			op.loopClickElement("Register_SeemoreLink_es",3, 5, explicitWaitTimeoutLoop);;
		}else{
			testExpectedStr = "Register successfully and get 10 points";
			testExpectedStr2= "R Points gained for signing in";
			op.loopClickElement("Register_SeemoreLink", 3, 5, explicitWaitTimeoutLoop); //seemore的按钮
		}
		op.loopClickElement("Register_look", 3, 10, 20);
		Page.pause(2);
		List<WebElement> rows=op.getElements("RegisterPromotionCodelist");
		Log.logInfo(rows.size());
		for (int i = 1; i < rows.size()+3; i++) {
		String	getIntegralStr = rows.get(i).findElement(By.cssSelector("td:nth-child(5)")).getText();
			//取得获取积分的那一个文案
			if (getIntegralStr.equals(testExpectedStr)||getIntegralStr.equals(testExpectedStr2)) {
				Log.logInfo("找到了赠送的积分的文案，验证通过");
				break;
			} else{					
			Log.logError("没有找到增送的积分，验证不通过,文本显示："+getIntegralStr);
			}					
		}		

	}
	
	/**
	 * 
	 * @测试点: 注册成功接口查询
	 * @验证点: 通过接口能够查询用户 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月5日
	 */
	@Test
	public void register_success_b() {
		int testExpectedInt = 1;
		try {
			int res=InterfaceMethod.IF_QueryUserRegisterStatus(domainName, new_register_email);
			Assert.assertEquals(testExpectedInt, res); // checkpoint
		} catch (Exception e) {
			e.printStackTrace();
	
		}
	}
	/**
	 * 
	 * @测试点:注册成功，收到欢迎邮件
	 * @验证点: 使用邮箱注册成功后，邮箱收到欢迎邮件 @使用环境： 测试
	 * @备注： register_verificationerror和注册成功register_succeed（a,b,c）关联性强
	 * @author zhangjun
	 * @date 2016年11月29日
	 */
	@Test
	public void register_succeed_c() {		
	      try{
	    	// String url="http://rosewholesale.com.d.s1.egomsl.com/eload_admin/crontab/send_email_queue.php";//收到邮件有延迟，先刷新一下
	    	String sender = "Rosewholesale.com"; //发送邮件
	    	//Pub.get(url);
			MailReceiver MR = new MailReceiver();
		   // String subject="Please make your register email verified on Rosewholesale";
			 String subject="Congratulations! You have successfully registered - you’re ready to start saving big!";
			String mail_content = null;
			long time_start_check = System.currentTimeMillis();
			long check_receive_email = 120;
	        boolean rec_email_falg = false;
			while (true) {
				Thread.sleep(1000);
				try {
					mail_content = MR.resmg(new_register_email, register_email_user_password, sender, subject);					
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
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(exceptionNote + "(" + info.testCheckpoint + ").");
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}
		

	
	/**
	 * 初始化url
	 * 
	 */
	public void urlInit(){
		if(baseURL.contains(".trunk.")&&baseURL.contains("/es/")){//西班牙语测试环境
			Log.logInfo("现在是西班牙语测试环境");
			comparedAddress = "http://es.rosewholesale.com.trunk.s1.egomsl.com/m-article-id-36.html";
			domainName="http://rosewholesale.com.trunk.s1.egomsl.com/";
		}else if(baseURL.contains("/es/")){
			Log.logInfo("现在是西班牙语正式环境");
			comparedAddress = "https://es.rosewholesale.com/m-article-id-36.html";
			domainName="https://rosewholesale.com/";
		}else if(baseURL.contains(".d.")){
			comparedAddress = "https://rosewholesale.com.d.s1.egomsl.com/m-article-id-36.html";
			domainName="http://rosewholesale.com.d.s1.egomsl.com/";
		}else {
			comparedAddress="https://www.rosewholesale.com/m-article-id-36.html";
			domainName="https://rosewholesale.com/";
		}
	}
}
