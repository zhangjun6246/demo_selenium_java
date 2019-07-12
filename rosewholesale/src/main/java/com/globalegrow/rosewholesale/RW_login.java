package com.globalegrow.rosewholesale;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.globalegrow.base.Startbrowser;
import com.globalegrow.code.Locator;
import com.globalegrow.code.Page;
import com.globalegrow.custom.InterfaceMethod;
import com.globalegrow.util.Log;
import com.globalegrow.util.MailReceiver;
import com.globalegrow.util.Op;
import com.globalegrow.util.Pub;
import com.globalegrow.util.testInfo;
import com.google.gson.JsonObject;


public class RW_login extends Startbrowser {
	private String className = GetClassName(); // 必要
	private String baseURL = "";	
	private testInfo info = null;

	public String currentWindow = null;
	private final String exceptionNote = "执行流程异常";
	public Date sendEmailDate = new Date();
	private String domainName;
	public String baseURL0 = "";
	private String testCaseProjectName = "rosewholesale";
	private String testExpectedStr;
	private String requestId ;	 
	private String registercode;
	private String loginURL = "";
	private String resetpwdURL = "";
	private String getkeys;//登录时的key
	private  String url;//邮件有延迟，刷新就较快
    
	private long explicitWaitTimeoutLoop = 20;
	
	@Parameters({ "testUrl" }) // 必要,二选一
	private RW_login(String testUrl) { // 必要
		moduleName = className.substring(className.lastIndexOf(".") + 1); // 必要
		Log.logInfo(moduleName);
		info = new testInfo(moduleName); // 必要
		baseURL = testUrl;
	}

	
	@Parameters({ "browserName","keys"})
	@BeforeClass
	public void beforeClass(String browserName, String keys) {
		String methodName = GetMethodName();
		Log.logInfo("**************Ready to test (" + moduleName + ")!!!**************\n");
		// 加载登录
		try {
			start(browserName); // 必要,初始化driver,web,二选一
			driver = super.getDriver(); // 启动driver			
			driver.manage().timeouts().pageLoadTimeout(pageLoadTimeoutMax, TimeUnit.SECONDS);
			op.loopGet(baseURL, 80, 3, 60);
			Log.logInfo("baseURL:" + baseURL);
			urlInit();
            getkeys=keys;
         
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			Log.logInfo("(" + methodName + ")...beforeClass stoop...\n\n");
		}
	}
	
	
	/**
	 * 退出浏览器
	 */
	@AfterClass
	public void afterClass(){ 
		 beforeTestRunFlag=false;
		 driver.quit();//退出浏览器		 
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
		if(op.isElementPresent("Login_verification")){
			try {
				op.loopSendKeysClean("Login_verification", registercode, explicitWaitTimeoutLoop);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			Log.logInfo("验证码输入框已被关闭，不输入验证码登录");
		}
	}
	
	
	
	/**
	 * 
	 * @测试点: 错误邮箱、登录失败
	 * @验证点: 输入不满足格式的邮箱，登录，提示Please enter a valid email address @使用环境： 测试环境 @备注：
	 * @author zhangjun
	 * @date 2016年12月1日
	 */
	@Test
	public void signin_wrongemail() {
		correFail = false;
		Pub.checkStatusBrowser();

		try {
			op.navigateRefresh(30, 2, 60);
			testExpectedStr = "Please enter a valid email address";
			requestId = Pub.getRandomString(6);	 
			registercode = InterfaceMethod.IF_Verification(driver, domainName, requestId, getkeys, "login");
			op.loopSendKeysClean("Login_EmailEnter","zhang.com", explicitWaitTimeoutLoop);
			op.loopSendKeysClean("Login_PasswordEnter", "123456", explicitWaitTimeoutLoop);			
			IsExistVerification(registercode);//判断是否有验证码输入框
			op.loopClickElement( "Login_SignBtn", 5, 3, explicitWaitTimeoutLoop);
			String getErrInfoEmail = op.loopGetElementText("login_ErrInfoEmail", 3,explicitWaitTimeoutLoop);
			if(baseURL.contains("/es/")){
				testExpectedStr="Por favor, introduzca una dirección de correo electrónico válida";
			}
			Log.logInfo("getEmailErrorText=" + getErrInfoEmail);
			Log.logInfo("testExpectedStr=" + testExpectedStr);			
			Assert.assertEquals(testExpectedStr, getErrInfoEmail);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		

	}

	/**
	 * 
	 * @测试点: 密码错误、登录失败
	 * @验证点: 提示“Your email/password is incorrect. Please try
	 *       again..”页面继续停留在当前页 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月1日
	 */
	
	@Test
	public void signin_wrongpassword() {
	
		driver.navigate().refresh();
		try {
			Page.pause(4);
			testExpectedStr ="Your email/password is incorrect. Please try again.";
			requestId = Pub.getRandomString(6);
			registercode = InterfaceMethod.IF_Verification(driver, domainName, requestId, getkeys, "login");
			op.loopSendKeysClean("Login_EmailEnter", "autotest01@globalegrow.com",  explicitWaitTimeoutLoop);
			op.loopSendKeysClean("Login_PasswordEnter", "1223344454",  explicitWaitTimeoutLoop);
			IsExistVerification(registercode);//判断是否有验证码输入框
			op.loopClickElement("Login_SignBtn", 5, 3, explicitWaitTimeoutLoop);
			Page.pause(4);
			String getErrInfopassword = op.loopGetElementText("login_ErrInfopassword", 3, explicitWaitTimeoutLoop);
			if(baseURL.contains("/es/")){
				testExpectedStr="Su correo electrónico/contraseña es incorrecto. Por favor, inténtelo de nuevo.";
			}
			Log.logInfo("getEmailErrorText=" + getErrInfopassword);
			Log.logInfo("testExpectedStr=" + testExpectedStr);
			Assert.assertEquals(testExpectedStr, getErrInfopassword);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		

	}
	/**
	 * 
	 * @测试点: 密码<6,登录失败
	 * @验证点: 提示“Enter at least 6 characters” 
	 * @使用环境： 测试环境 
	 * @备注： void
	 * @author zhangjun
	 * @date 2016年12月1日
	 */

	@Test
	public void sigin_passwordLength() {
		try {
			op.navigateRefresh(30, 2, 60);
			testExpectedStr = "Enter at least 6 characters";
			requestId = Pub.getRandomString(6);
			registercode = InterfaceMethod.IF_Verification(driver, domainName, requestId, getkeys, "login");
			
			op.loopSendKeysClean( "Login_EmailEnter", "autotest01@globalegrow.com", explicitWaitTimeoutLoop);
			op.loopSendKeysClean("Login_PasswordEnter", "123",explicitWaitTimeoutLoop);
			IsExistVerification(registercode);//判断是否有验证码输入框
			op.loopClickElement("Login_SignBtn", 5, 3,explicitWaitTimeoutLoop);
			String getErrInfopasswordlength = op.loopGetElementText("login_ErrInfopasswordlength", 3, explicitWaitTimeoutLoop);

			Log.logInfo("getErrInfopasswordlength:" + getErrInfopasswordlength);
			Log.logInfo("testExpectedStr=" + testExpectedStr);
			if(baseURL.contains("/es/")){
				testExpectedStr="introduzca al menos de 6 caracteres";
			}
			Assert.assertEquals(testExpectedStr, getErrInfopasswordlength);
		

		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(exceptionNote + "(" + info.testCheckpoint + ").");
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 登录成功
	 * @验证点: 登录成功跳转到http://user.rosewholesale.com/m-users-a-profile.htm页
	 *       2.获取登录的登录名 @使用环境： 测试环境 @备注：
	 * @author zhangjun
	 * @throws Exception 
	 * @date 2016年12月1日
	 */

	@Test
	public void sigin_successful() {		

		driver.navigate().refresh();
		boolean loginFlag = true;// 判断是否有登录
		String methodName = GetMethodName();
		
		try {
			String ftc = "login";
			String requestId = Pub.getRandomString(6);
			String logincode = InterfaceMethod.IF_Verification(driver, domainName, requestId, getkeys, ftc);
			number++;
			Page.pause(3);
			if (logincode.equals("verify") && number < 6) {// 为了避免拿到的验证码为verify
				driver.navigate().refresh();
				Page.pause(4);
				sigin_successful();
			} else {
				op.loopSendKeys("Login_EmailEnter", "autotest01@globalegrow.com", 3, explicitWaitTimeoutLoop);
				op.loopSendKeys("Login_PasswordEnter","zhang123456", 3,explicitWaitTimeoutLoop);
				IsExistVerification(logincode);// 判断是否有验证码输入框
				op.loopClickElement("Login_SignBtn", 3, 20, explicitWaitTimeoutLoop);

				for (int i = 0; i < 10; i++) {// 判断登录成功				
					Log.logInfo("第" + i + "次");
					Page.pause(2);
					String LoginStats = op.loopGetElementText("signlinkName", 5, 20);
					if (LoginStats.contains("Hi, autot")) {
						Log.logInfo("登录成功");
						loginFlag = false;
						break;
					}
				}
				if (loginFlag == true) {
					captureScreenAll(projectName, methodName);
					Log.logError("登录失败，以下内容不执行");
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}		
	}
	int number = 0;
	
	/**
	 * 
	 * @测试点: //先测试一个点，获取cookies ，主要是测试facebook
	 * @验证点: 1.登录成功2.跳转到信息页面，通过查找登录名判断已成功 @使用环境： 正式环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月1日
	 */
	@Test
	public void sigin_facebooksuccessful() {

		String testExpectedStr ="autotest03@globalegrow.com";
		currentWindow = driver.getWindowHandle();// 保存当前窗口句柄
		try {
			String getName = op.loopGetElementText("signlinkName", 3, 20);
			boolean loginState = getName.contains("autot");// 区分测试与正式环境
			if (loginState) {// 存在登录按钮，如果已登录，按钮不可见
				Signout_Success();// 登出
			}
			op.loopGet(loginURL, 6, 2, 80);
			if (op.isElementPresent("loginFaceBook",20)) {// 判断facebook登录按钮是否存在
				op.loopClickElement("loginFaceBook", 3, 20, 300);//
				Log.logInfo("facebook登录按钮已找到!!");
			} else {
				Log.logError("找不到facebook的元素!!");
			}
			if (Page.switchToWindow(driver, "Facebook")) { // 切换窗口
				op.loopSendKeys("emailenter", "autotest03@globalegrow.com'", 3, explicitWaitTimeoutLoop);
				op.loopSendKeys("passwordentr", "autotest123456!", 3, explicitWaitTimeoutLoop);
				op.loopClickElement("loginbtn", 3, 20, explicitWaitTimeoutLoop);
			} else {
				Log.logInfo("没有找到Facebook登录窗口！！！");
			}
			driver.switchTo().window(currentWindow);// 切换回默认窗口
			if (op.isElementPresent("login_getEmaiName",20)) {
				String getEmaiName = op.loopGetElementText("login_getEmaiName", 3, 20);
				Assert.assertEquals(getEmaiName, testExpectedStr);
				Log.logInfo("Facebook登录成功！！！");
			} else {
				Log.logError("Facebook登录失败，未找到My Orders！！！");
			}


		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}
	

	/**
	 * 
	 * @测试点: 修改密码为空，失败
	 * @验证点: 提示：Provide a password @使用环境： 测试环境 @备注：
	 *       siginsuccessful_passwordempty与siginsuccessful_passwordLength紧密联系
	 * @author zhangjun
	 * @date 2016年12月1日
	 */
	@Test
	public void modify_passwordempty() {
		correFail = false;
		Pub.checkStatusBrowser();
		try {
			op.navigateRefresh(30, 3, 60);
			testExpectedStr = "Provide a password";
			//因为新用户会跳出很多活动页面，所以我们直接跳转到登录成功页
			
			if (baseURL.contains("/es/")) {
				op.loopGet("https://user.rosewholesale.com/es/m-users-a-change_password.htm",5,5,10);
				op.loopClickElement("successful_Edit_es", 3, 20, explicitWaitTimeoutLoop);
				testExpectedStr = "Proporcionar una contraseña";
			} else {
				//op.loopGet("https://user.rosewholesale.com/user/index/userindex", 5, 5, 10);
				//有活动页，点击姓名修改地址
				op.loopGet("https://user.rosewholesale.com/m-users-a-change_password.htm", 5, 5, 10);
				op.loopClickElement("successful_Edit", 3, 20, explicitWaitTimeoutLoop);
			}

			op.loopClickElement("Edit_confirmEditbtn", 3, 20, explicitWaitTimeoutLoop);
			String getpasswordempty = op.loopGetElementText("Modify_passwordempty", 20, 40);
			Log.logInfo("getErrInfopasswordlength:" + getpasswordempty);
			Log.logInfo("testExpectedStr=" + testExpectedStr);
			Assert.assertEquals(getpasswordempty, testExpectedStr);
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(exceptionNote + "(" + info.testCheckpoint + ").");
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 修改密码<6,失败
	 * @验证点: 修改密码小于6,失败
	 * @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月1日
	 */
	@Test
	public void modify_passwordlength() {		

		driver.navigate().refresh();
		
		try {
			testExpectedStr ="Enter at least 8 characters";
			op.loopSendKeys("Edit_changporiginalinput","autotest123456!", 3,explicitWaitTimeoutLoop);			
			op.loopSendKeys("Edit_changNewpassword", "12345", 3,explicitWaitTimeoutLoop);
			op.loopSendKeys("Edit_confirmpassword", "12345", 3,explicitWaitTimeoutLoop);
			op.loopClickElement("Edit_confirmEditbtn", 3, 20, explicitWaitTimeoutLoop);
			String getErrInfopassword = op.loopGetElementText("Edit_ErrInfopassword", 3, explicitWaitTimeoutLoop);
			
			if(baseURL.contains("/es/")){
				testExpectedStr="introduzca al menos de 8 caracteres";
			}
			Log.logInfo("getErrInfopasswordlength:" + getErrInfopassword);
			Log.logInfo("testExpectedStr=" + testExpectedStr);
			Assert.assertEquals(getErrInfopassword, testExpectedStr);

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 修改密码不一致，失败
	 * @验证点:提示：please type the same password @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月1日
	 */
	@Test
	public void modify_passwordontmatch() {	
		driver.navigate().refresh();	
		try {
			String testExpectedStr = "Enter the same password as above";
			op.loopSendKeys("Edit_changporiginalinput", "autotest123456!", 3,explicitWaitTimeoutLoop);
			op.loopSendKeys("Edit_changNewpassword", "zhang123456", 3,explicitWaitTimeoutLoop);
			op.loopSendKeys("Edit_confirmpassword", "zhang1234567", 3,explicitWaitTimeoutLoop);
			op.loopClickElement("Edit_confirmEditbtn", 3, 20, explicitWaitTimeoutLoop);
			String getErrInfopasswordmatch = op.loopGetElementText("dontmatch", 3, explicitWaitTimeoutLoop);		
			if(baseURL.contains("/es/")){
				testExpectedStr="Escriba la misma contraseña que escribio anteriormente";
			}
			Log.logInfo("getErrInfopasswordlength:" + getErrInfopasswordmatch);
			Log.logInfo("testExpectedStr=" + testExpectedStr);
			Assert.assertEquals(getErrInfopasswordmatch, testExpectedStr);

			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	
	/**
	 * 
	 * @测试点: 修改密码成功
	 * @验证点: 修改密码成功1.登录成功，输入正确的旧密码，输入6位数以上的新密码2.提示成功3.新密码登录成功，获取到登录邮箱名称 
	 * @使用环境：测试环境
	 * @备注： 
	 * @author zhangjun
	 * @date 2016年12月5日
	 */
	
	@Test
	public void modify_scuccesful() {	

		try {
			op.navigateRefresh(30, 3, 60);
			testExpectedStr ="Your new password has been set up successfully！";
			op.loopSendKeys("Edit_changporiginalinput", "zhang123456", 3,explicitWaitTimeoutLoop);
			op.loopSendKeys("Edit_changNewpassword", "zhang123456", 3,explicitWaitTimeoutLoop);
			op.loopSendKeys("Edit_confirmpassword", "zhang123456", 3,explicitWaitTimeoutLoop);
			op.loopClickElement("Edit_confirmEditbtn", 3, 20, explicitWaitTimeoutLoop);
		/*	if(op.isElementPresent("Login_EmailEnter",5)){  //目前逻辑有改，修改密码成功后不给提示了
				Log.logInfo("修改密码成功");
			}else{
				Log.logError("修改密码不成功");
			}*/
		
			String getErrInfopasswordmatch = op.loopGetElementText("passwordupdataSuccess", 3, explicitWaitTimeoutLoop);
			if(baseURL.contains("/es/")){
				testExpectedStr="Su nueva contraseña ha sido configurada correctamente！";
			}
			Log.logInfo("getErrInfopasswordlength:" + getErrInfopasswordmatch);
			Log.logInfo("testExpectedStr=" + testExpectedStr);
			
			Assert.assertEquals(getErrInfopasswordmatch, testExpectedStr);			
			op.loopClickElement("modifylogin", 3, 10, 20);//进行重新登录		
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(exceptionNote + "(" + info.testCheckpoint + ").");
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 忘记密码
	 * @验证点: 1.正常发送链接，用户邮箱接收到验证邮件2.读取邮件内容，打开修改密码页链接，是否正确" @使用环境： 测试环境 
	 * @备注： sigin_forgetpassword与密码reset_forgetpassword相关性强
	 *       
	 * @author zhangjun
	 * @date 2016年12月1日
	 */

	@Test
	public void reset_forgetpassword() {
		/*correFail = false;
		Pub.checkStatusBrowser();*/
	try {
		String getName=op.loopGetElementText("signlinkName", 3, 20);
		boolean loginState=getName.contains("autot");//区分测试与正式环境
		if(loginState){
			Signout_Success();// 登出		
		}
		//点击登录
		/*op.loopClickElement("loginBtn", 5, settimeout, explicitWaitTimeoutLoop);*/
		
		op.loopGet(resetpwdURL, 40, 3, 60);
		String requestId = Pub.getRandomString(6);	
			String registercode = InterfaceMethod.IF_Verification(driver, domainName, requestId, getkeys, "");
			number2++;
			if (registercode.equals("verify") && number2<6) {
				Page.pause(3);
				reset_forgetpassword();
				
			}else{
			op.loopSendKeys("forgetpassword_EmailEnt", "autotest01@globalegrow.com", 3,explicitWaitTimeoutLoop);
				if (op.isElementPresent("forgetpassword_code")) {
					op.loopSendKeys("forgetpassword_code", registercode, 3, explicitWaitTimeoutLoop);
				} else {
					Log.logInfo("不存在验证码输入框，跳过验证码");
				}
			op.loopClickElement("forgetpassword_btnjs", 3, 20, explicitWaitTimeoutLoop);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	
		// 获取邮件地址
		// 读取邮件内容，判断修改密码链接是否正确
	
		String subject = "Reset your password of Rosewholesale.com";
	       if(baseURL.contains("/es/")){
	    	   subject="Reiniciar su contraseña de Rosewholesale.com";
	       }
		String mail_content = null;
		String sender = "Rosewholesale.com";						
		MailReceiver MR = new MailReceiver();
		Date sendEmailDate_2 = null;
		Pattern p;
		try {
			 //收到邮件有延迟，先刷新一下
			if(baseURL.contains(".d.")||baseURL.contains(".trunk.")){
			    Pub.get(url);
			}
			sendEmailDate_2 = new Date(sendEmailDate.getTime() - 120000);// 设置发送邮件的前两分钟
			// 循环判断是否收到邮件,60秒为上线
			int timeout = 0;
			while (true) {
				if (timeout > 240) {
					Log.logError("获取邮件超时！！！");
					break;
				}
				try {
					mail_content = MR.resmg("autotest01@globalegrow.com",
							"autotest123456!", sender, subject, sendEmailDate_2);
					if (mail_content.equals("0")) {
						Page.pause(3);
						timeout++;
						continue;
					} else {
						if(baseURL.contains(".d.")||baseURL.contains(".trunk.")){//正式环境和测试环境的链接地址不一样
							p = Pattern.compile("http://(.*?).html"); // 正则取值,匹配http开头.html结尾的字符串
							Log.logInfo("使用测试环境的规则");
						}else{
							p = Pattern.compile("https://(.*?).html"); // 正则取值,匹配http开头.html结尾的字符串
							Log.logInfo("使用正式环境的规则");
						}
						
						Matcher m = p.matcher(mail_content); // 取出忘记密码邮件中链接地址
						while (m.find()) {
							System.out.println("获取的邮件地址为"+m.group());
							driver.navigate().to(m.group());
						}
						break;
					}
				} catch (Exception e) {
					Log.logError("获取邮件出错" + e.getMessage());
				}
			}
			
			String testExpectedStr = "Select your new password";
			String getforgethead = op.loopGetElementText("forgetpassword_head",3,50);
			if(baseURL.contains("/es/")){
				testExpectedStr="Seleccione su nueva contraseña";
			}
			Log.logInfo("getErrInfopasswordlength:" + getforgethead);
			Log.logInfo("testExpectedStr:" + testExpectedStr);
			Page.pause(1);
			if (getforgethead.equals(testExpectedStr)) {
				Log.logInfo("收取邮件成功，并且打开了邮件地址，验证成功");
			}else {
				Log.logError("收取邮件失败，并且没有打开，验证不通过");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(exceptionNote + "(" + info.testCheckpoint + ").");
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}

	}
	int number2=0;

	/**
	 * 
	 * @测试点: 重置密码邮箱格式错误，登录失败
	 * @验证点:无效邮箱提示:Please enter a valid email address @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月1日
	 */
	@Test
	public void reset_Wrongemail() {
		testExpectedStr ="Please enter a valid email address";
		try {
			op.loopSendKeys("forgetpassword_EmailEnt", "autotest01globalegrow.com", 3,explicitWaitTimeoutLoop);
			op.loopSendKeys("forgetpassword_enter", "123456", 3, explicitWaitTimeoutLoop);
			op.loopSendKeys("forgetpassword_ReenterEnt", "123456", 3,explicitWaitTimeoutLoop);
			op.loopClickElement("forgetpassword_btnjs", 3, 20, explicitWaitTimeoutLoop);

			String getErrInfopasswordmatch = op.loopGetElementText("reset_ErrInfoEmail", 3, explicitWaitTimeoutLoop);
			if(baseURL.contains("/es/")){
				testExpectedStr="Por favor, introduzca una dirección de correo electrónico válida";
			}
			Log.logInfo("getErrInfopasswordlength:" + getErrInfopasswordmatch);
			Log.logInfo("testExpectedStr=" + testExpectedStr);
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("testExpectedStr=" + testExpectedStr);
			Log.logWarn(exceptionNote + "(" + info.testCheckpoint + ").");
			Assert.fail();
			
		}

	}

	/**
	 * 
	 * @测试点: 重置密码为空，失败
	 * @验证点: 1.密码和确认密码为空时,提示：Provide a password @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月1日
	 */
	@Test
	public void reset_Passwordempty() {		
		testExpectedStr = "Please enter a valid email address";
		driver.navigate().refresh();
		try {
			op.loopSendKeys("forgetpassword_EmailEnt", "autotest01globalegrow.com", 3,explicitWaitTimeoutLoop);
			op.loopClickElement("forgetpassword_btnjs", 3, 20, explicitWaitTimeoutLoop);
			String getErrInfopasswordmatch = op.loopGetElementText("reset_ErrInfopassword", 3, explicitWaitTimeoutLoop);
			if(baseURL.contains("/es/")){
				testExpectedStr="Proporcionar una contraseña";
			}
			Log.logInfo("getErrInfopasswordlength:" + getErrInfopasswordmatch);
			Log.logInfo("testExpectedStr=" + testExpectedStr);
			Pub.printTestCaseInfo(info);

		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("testExpectedStr=" + testExpectedStr);
			Log.logWarn(exceptionNote + "(" + info.testCheckpoint + ").");
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 重置密码，确认密码不一致，失败
	 * @验证点: 提示：Enter the same password as above @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月1日
	 */
	@Test
	public void reset_Passwordontmatch() {
		driver.navigate().refresh();

		testExpectedStr ="Enter the same password as above";
		try {
			op.loopSendKeys("forgetpassword_EmailEnt", "autotest01@globalegrow.com", 3,explicitWaitTimeoutLoop);
			op.loopSendKeys("forgetpassword_passwordEnt", "zh123456", 3,explicitWaitTimeoutLoop);
			op.loopSendKeys("forgetpassword_ReenterEnt", "zh1234567", 3,explicitWaitTimeoutLoop);
			op.loopClickElement("forgetpassword_btnjs", 3, 20, explicitWaitTimeoutLoop);
			String getErrInfopassworddontmatch = op.loopGetElementText("Modify_passworddifferent", 3,explicitWaitTimeoutLoop);
			if(baseURL.contains("/es/")){
				testExpectedStr="Escriba la misma contraseña que escribio anteriormente";
			}
			Log.logInfo("getErrInfopasswordlength:" + getErrInfopassworddontmatch);
			Log.logInfo("testExpectedStr=" + testExpectedStr);
			Assert.assertEquals(getErrInfopassworddontmatch, testExpectedStr);
		/*	if(baseURL.contains("/es/")){
				op.loopClickElement("forgetpassword_return_es", 3, 20, explicitWaitTimeoutLoop);// 点击返回
			}else{
				op.loopClickElement("forgetpassword_return", 3, 20, explicitWaitTimeoutLoop);// 点击返回
			}*/
			

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 重置密码成功
	 * @验证点: 1.修改密码成功，提示成功 2.使用新密码重新登录，获取登录名，成功 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月2日
	 */
	@Test
	public void reset_passwordSuccessful() {
	
		driver.navigate().refresh();
		testExpectedStr ="Your new password has been set up successfully！";
		try {
			if(baseURL.contains("/es/")){
				testExpectedStr="Su nueva contraseña ha sido configurada correctamente！";
			}
			op.loopSendKeys("forgetpassword_EmailEnt", "autotest01@globalegrow.com", 3,explicitWaitTimeoutLoop);
			op.loopSendKeys("forgetpassword_passwordEnt", "zhang123456", 3,explicitWaitTimeoutLoop);
			op.loopSendKeys("forgetpassword_ReenterEnt", "zhang123456", 3,explicitWaitTimeoutLoop);
			op.loopClickElement("forgetpassword_btnjs", 3, 20, explicitWaitTimeoutLoop);
			String getModifysuccessful = op.loopGetElementText("Edit_Modifysuccessful", 3, explicitWaitTimeoutLoop);

			Log.logInfo("getErrInfopasswordlength:" + getModifysuccessful);
			Log.logInfo("testExpectedStr=" + testExpectedStr);
			Pub.assertInfo("assertEquals", "", "");
			Assert.assertEquals(getModifysuccessful, testExpectedStr);
			op.loopClickElement("needtolog", 3, 6, 300);
		/*	driver.navigate().refresh();
			Page.pause(6);
			driver.navigate().refresh(); //两次刷新是因为忘记密码后在登录，获取的验证码会是忘记密码的验证码，
*/	
		} catch (Exception e) {
			e.printStackTrace();
		
			Assert.fail();
		}
		
	}
	

	/**
	 * 
	* @测试点: Signout_Success 
	* @验证点: 退出登录的操作 
	* @使用环境：     测试环境
	* @备注： void    
	* @author zhangjun 
	* @date 2016年12月6日
	 */
	public void Signout_Success() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String myjs = "document.querySelector(\"#header > div > div.user_box > ul\").style.display='block';";
		js.executeScript(myjs);		
		try {
			if(baseURL.contains("/es/")){
				op.loopClickElement("logout_link_es", 3, 20, 500);
			}else{
				
				op.loopClickElement("logout_link", 3, 20, 500);
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 初始化url
	 * 
	 * 
	 */
	public void urlInit() {
		if(baseURL.contains(".trunk.")&&baseURL.contains("/es/")){//西班牙语测试环境
			Log.logInfo("现在是西班牙语测试环境");
			domainName="http://rosewholesale.com.trunk.s1.egomsl.com/";
			resetpwdURL = "http://user.rosewholesale.com.trunk.s1.egomsl.com/es/m-users-a-reset_password.htm";
			url="http://rosewholesale.com.trunk.s1.egomsl.com/eload_admin/crontab/send_email_queue.php";
		}else if(baseURL.contains("/es/")){
			Log.logInfo("现在是西班牙语正式环境");
			domainName="https://rosewholesale.com/";
			resetpwdURL = "https://user.rosewholesale.com/es/m-users-a-reset_password.htm";
		}else if(baseURL.contains(".trunk.")){
			loginURL = "http://login.rosewholesale.com.d.s1.egomsl.com/m-users-a-sign.htm";
			resetpwdURL = "http://user.rosewholesale.com.trunk.s1.egomsl.com/m-users-a-reset_password.htm";
			url="http://rosewholesale.com.d.s1.egomsl.com/eload_admin/crontab/send_email_queue.php";
			domainName="http://rosewholesale.com.trunk.s1.egomsl.com/";
		}else{
			loginURL ="http://login.rosewholesale.com/m-users-a-sign.htm";
			resetpwdURL="http://user.rosewholesale.com/m-users-a-reset_password.htm";
			domainName="https://rosewholesale.com/";
		}
		
	}

}
