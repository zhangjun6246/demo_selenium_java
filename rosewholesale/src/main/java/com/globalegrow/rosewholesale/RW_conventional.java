package com.globalegrow.rosewholesale;
import java.awt.Desktop.Action;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTML;

import org.junit.Ignore;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.globalegrow.autotest_encrypt.Operate;
import com.globalegrow.base.Startbrowser;
import com.globalegrow.code.Page;

import com.globalegrow.custom.ControlUsingMethod;
import com.globalegrow.custom.InterfaceMethod;

import com.globalegrow.util.Log;

import com.globalegrow.util.Pub;





public class RW_conventional extends Startbrowser {
	private String baseURL =null;


	private String myPublicUrl;//域名
	private String getkeys; //关键字key
	private long explicitWaitTimeoutLoop = 20;
	private long settimeout=10;
	private String loginURL;//登录页面的url
	private String goodTestUrl;//测试的url地址
	private String[] itemList;//获取页面信息内容
    private String userName;
    private String myOrderUrl;//我的订单地址
    private String mianHandles;//主句柄
    private String ppaccount;//支付的PP账号
    private String totalprice;//计算的总价
    private String payType;//获取支付的方式
    private String payMoney;//获取支付的价格
    private String getproducturl;//购物车商品
    private String myPublicUrl2;//获取url地址
    private String timeStamp;//时间戳
    private String classification;//分类地址
    private String rates;//汇率的url
    private double rate;//汇率
    InterfaceMethod interfaceMethod;
 	DecimalFormat df = new DecimalFormat("#0.00");
 	//private static List<Thread> activeThreads = new ArrayList<Thread>();
 	List<String> Listurl=new ArrayList<String>();
 	
	@Parameters({ "testUrl" }) // 必要,二选一
	private RW_conventional(String testUrl) { // 必要

		baseURL = testUrl;
	}

	@Parameters({ "browserName", "keys","PPaccount","preRelease"})
	@BeforeClass
	public void beforeClass(String browserName, String keys,String PPaccount,String preRelease) {
		String methodName = GetMethodName();
		String methodNameFull = moduleName + "." + methodName;
		Log.logInfo("**************Ready to test (" + moduleName + ")!!!**************\n");
		Log.logInfo("(" + methodNameFull + ")...beforeClass start...");
		// 加载登录
		try {
			start(browserName); // 必要,初始化driver,web,二选一
			driver = super.getDriver(); // 启动driver
			driver.manage().timeouts().pageLoadTimeout(pageLoadTimeoutMax, TimeUnit.SECONDS);
			op.loopGet(baseURL, 60, 4, 80);
			//说明是预发布环境
			if(preRelease.equals("true")){
				 System.out.println("cookie个数"+driver.manage().getCookies().size());
				 Cookie cookie = new Cookie("staging", "true",".rosewholesale.com", "/", null);
				 driver.manage().addCookie(cookie);
				//直接执行代码
				/*JavascriptExecutor js = (JavascriptExecutor) driver;
				String myjs ="document.cookie = 'staging=true;path=/;domain=.rosewholesale.com'";//切换到预发布环境
				js.executeScript(myjs);*/
				 Log.logInfo("已经切换到了预发布环境");
				 System.out.println("cookie个数"+driver.manage().getCookies().size());	
			}
			getkeys=keys;
			ppaccount=PPaccount;//登录账
			interfaceMethod=new InterfaceMethod();
			urlInit();
			Date date=new Date(); 
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			timeStamp = simpleDateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			Log.logInfo("(" + methodNameFull + ")...beforeClass stoop...\n\n");
			//并且取出获取
			if(baseURL.contains("/es/")){//针对多语言的换算
				rate=interfaceMethod.rates(rates);//获取的价格
			}
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
	 * @测试点: 新注册一个用户U，能成功注册
	 * @验证点: 注册成功后成功进入到用户资料页面
	 * @author zhangjun
	 * @date 2016年11月28日
	 */

	@Test
	public void registered() {
		Boolean loginFlag = false;	
		try {
			op.loopGet(loginURL, 50, 3, 60);
			//driver.navigate().refresh();
			InterfaceMethod.IF_DelUserAccount2(myPublicUrl, userName);	
			if(baseURL.contains("es.")){
				userName="autotest02@globalegrow.com";
			}else{
				userName="autotest07@globalegrow.com";
			}
			op.loopSendKeysClean("Register_EmailInput", userName, 3);
			op.loopSendKeysClean("Register_passwordInput", "123456", 3);
			op.loopSendKeysClean("Register_passwordconfirmInput", "123456", 3);
			String requestId = Pub.getRandomString(6);
			String registercode = InterfaceMethod.IF_Verification(driver, myPublicUrl, requestId, getkeys, "regist");

			if (registercode.equals("verify") && number < 6) {// 为了避免拿到的验证码为verify
				driver.navigate().refresh();
				Page.pause(4); 
				registered();
			} else {
				op.loopSendKeys("Register_verificationcodeInput", registercode, 3, explicitWaitTimeoutLoop);
				op.loopClickElement("Register_joinBtn", 3, 5, explicitWaitTimeoutLoop); // 注册按钮
				/*String getError=op.loopGetElementText("registererror", 5,5);
				if(getError.equals("The code appears to be wrong. Please try again.")){
					Log.logInfo("验证码获取错误");
					Page.pause(2);
					registercode = InterfaceMethod.IF_Verification(driver, myPublicUrl, requestId, getkeys, "regist");
					op.loopSendKeys("Register_verificationcodeInput", registercode, 3, explicitWaitTimeoutLoop);
					op.loopClickElement("Register_joinBtn", 3, 5, explicitWaitTimeoutLoop); // 注册按钮
				}*/
				/*if(op.isElementPresent("registererror",5)){
					Log.logInfo("验证码获取错误");
					Page.pause(2);
					registercode = InterfaceMethod.IF_Verification(driver, myPublicUrl, requestId, getkeys, "regist");
					op.loopSendKeys("Register_verificationcodeInput", registercode, 3, explicitWaitTimeoutLoop);
					op.loopClickElement("Register_joinBtn", 3, 5, explicitWaitTimeoutLoop); // 注册按钮
				}*/
				for (int i = 0; i < 10; i++) {// 判断登录成功
				 	String LoginStats = op.loopGetElementText("signlinkName", 5, 5);
					if (LoginStats.contains("Hi, autot")) {
						Log.logInfo("登录成功");
						loginFlag = false;
						break;
					}
				}
				if (loginFlag == true) {
					captureScreenAll(projectName, GetMethodName());
					Log.logError("注册失败，以下内容不执行");

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @测试点: 退出刚刚新注册的用户，输入用户名，密码，成功登录
	 * @验证点:登录成功，跳转到和用户资料页面
	 * @使用环境： 测试环境 @备注：
	 * @author zhangjun
	 * @throws Exception 
	 * @date 2016年12月1日
	 */
	 @Test
    public void login(){
		 if(baseURL.contains("es.")){
				userName="autotest02@globalegrow.com";
			}else{
				userName="autotest07@globalegrow.com";
			}
		 loginprd(userName);
    }
	
	public void loginprd(String name) {
		boolean loginFlag = true;// 判断是否有登录
		String methodName = GetMethodName();
		String LoginStats;
		try {
			LoginStats = op.loopGetElementText("signlinkName", 8, 5);
			if (LoginStats.contains("Hi, autot")) {
				signoutSuccess();// 退出用户
			}
			op.loopGet(loginURL, 50, 3, 60);
			int number = InterfaceMethod.IF_QueryUserRegisterStatus(myPublicUrl, name);
			if (number == 1) {
				Log.logInfo("已经注册了");
				String requestId = Pub.getRandomString(6);
				String logincode = InterfaceMethod.IF_Verification(driver, myPublicUrl, requestId, getkeys, "login");
				number++;

				if (logincode.equals("verify") && number < 8) {// 为了避免拿到的验证码为verify
					driver.navigate().refresh();
					Page.pause(4);
					loginprd(name);
				} else {
					op.loopSendKeys("Login_EmailEnter", name, 3, explicitWaitTimeoutLoop);
					op.loopSendKeys("Login_PasswordEnter", "123456", 3, explicitWaitTimeoutLoop);
					op.loopSendKeys("Login_verification", logincode, 3, explicitWaitTimeoutLoop);
					op.loopClickElement("Login_SignBtn", 3, 20, explicitWaitTimeoutLoop);

					for (int i = 0; i < 10; i++) {// 判断登录成功
						Log.logInfo("第" + i + "次");
						Page.pause(2);
						LoginStats = op.loopGetElementText("signlinkName", 5, 20);
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
			} else {
				Log.logInfo("未注册用户");
			}

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	int number = 0;
	
	/**
	 * 
	* @测试点: facebook登录 
	* @验证点: 登录成功，跳转到和用户资料页面
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月3日 
	  @修改说明
	 */
	@Test
	public void facebooklogin() {
		String currentWindow;
		String testExpectedStr ="autotest03@globalegrow.com";
		currentWindow = driver.getWindowHandle();// 保存当前窗口句柄
		try {
			String LoginStats = op.loopGetElementText("signlinkName", 3, 15);
			if (LoginStats.contains("Hi, autot")) {
				signoutSuccess();//退出用户
			}
			op.loopGet(loginURL, 40, 2, 80);
			if (op.isElementPresent("loginFaceBook",20)) {// 判断facebook登录按钮是否存在
				op.loopClickElement("loginFaceBook", 3, 20, 300);//
				Log.logInfo("facebook登录按钮已找到!!");
			} else {
				Log.logError("找不到facebook的元素!!");
			}
			if (Page.switchToWindow(driver, "Facebook")) { // 切换窗口
				op.loopSendKeys("emailenter", "autotest03@globalegrow.com", 3, explicitWaitTimeoutLoop);
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
	* @测试点: 
	* @验证点: 获取商品的数据
	  @return   
	* @备注： String  
	* @author zhangjun 
	* @date 2017年9月6日 
	  @修改说明
	 */
	
	/**
	 * 
	* @测试点: 获取商品  
	* @验证点: 获取商品的数据
	  @param  clearance 是否清仓
	  @param  promote 是否促销
	  @return   返回从接口中返回的商品 ,和商品的sku
	* @备注： Map<String,String>  
	* @author zhangjun 
	* @date 2017年9月6日 
	  @修改说明
	 */
	
	public Map<String, String>  getProduct(String clearance, String promote){
		
		Map<String, String> getgoods=new HashMap<String,String>();
		Map<String, Object> goods = interfaceMethod.getGoods(myPublicUrl, clearance, promote);
		String goodsUrl = (String) goods.get("url_title");
		goodsUrl = goodsUrl + "?" + timeStamp;
		String goodsSku = (String) goods.get("goods_sn");//取出sku
		
		if(baseURL.contains("es.")){
			goodsUrl=goodsUrl.replace("www", "es");//替换成多语言
		}
		getgoods.put("url", goodsUrl);
		getgoods.put("sku", goodsSku);
		return getgoods;
	}
    
	/**
	 * 
	 * @测试点: 1.选择一款普通商品，加入到购物车中，点击按钮，对应的价格显示正常
	 * @验证点:1.点击增加按钮，总价进行变化，验证是否与计算价格一致2.点击删除按钮价格恢复为原价，对比价格
	 * @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月26日
	 */
	@Test
	public void cart_price() {
		String total = "";
		try {
			String LoginStats = op.loopGetElementText("signlinkName", 3, 15);
			if (LoginStats.contains("Hi, autot")) {
				signoutSuccess();//退出用户
			}		
			if (baseURL.contains("es.")) {
				userName="autotest24@globalegrow.com";
			}else{
				userName="autotest23@globalegrow.com";
			}
			loginprd(userName);
			interfaceMethod.crearAllCartGoods(myPublicUrl, userName);// 清空购物车
			Map<String, String> goods=getProduct("n","n");
			op.loopGet(goods.get("url"), 60, 2, 80);
		    Double ladder = interfaceMethod.getLadderprice(myPublicUrl, goods.get("sku"), 2);//获取阶梯价 
		    String   calculationPrice = df.format(ladder * 2);// 计算阶梯价格
			String OriginalPrice = op.loopGetElementValue("unitPrice", "data-orgp", 10);//商品页价格
			Log.logInfo("获取商品的单价为:" + OriginalPrice);
			op.loopClickElement("addToBag", 4, 10, 30);// 点击添加至购物车按钮
			
			Log.logInfo("进行商品的增加");
			op.loopClickElement("cartAddProduct", 2, 15, explicitWaitTimeoutLoop);
			while (op.isElementPresent("laoding",5)){
				Page.pause(1);
				if(!op.isElementPresent("laoding",5)){
					break;
				}
			}
			total=op.loopGetElementValue("shoopingpay", "data-orgp", explicitWaitTimeoutLoop);
			Assert.assertEquals(calculationPrice, total);
			//需要等
			Log.logInfo("进行商品的减少");
			op.loopClickElement("cartReduceProduct", 2, 15, explicitWaitTimeoutLoop);

			while (op.isElementPresent("laoding",5)){
				Page.pause(1);
				if(!op.isElementPresent("laoding",5)){
					break;
				}
			}
			total = op.loopGetElementValue("shoopingpay", "data-orgp", explicitWaitTimeoutLoop);
			Assert.assertEquals(OriginalPrice, total);
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	

	/**
	 * 
	 * @测试点:删除所有商品，显示重新添加商品按钮
	 * @验证点:删除所有商品成功，出现继续添加的按钮
	 * @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月2日
	 */
	@Test
	public void cart_delete() {
		correFail=false;
		try {
			if (op.isElementPresent("cartPage_EmptyCartNote", 10)) {
				Log.logInfo("删除页面，商品为空，目前没有处理，但是购物车已经为空了");
			} else {
				op.loopClickElement("delectALL", 10, 30, explicitWaitTimeoutLoop);
				op.loopClickElement("confim", 10, 30, explicitWaitTimeoutLoop);// 确认删除
				if(op.isElementPresent("cartPage_ContinueShopping",20)){
					Log.logInfo("商品删除成功，出现购物车按钮");
				}else{
					Log.logError("商品没有删除成功，购物车按钮失败");
				}
				
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	
	/**
	 * 
	 * @测试点: 空购物车显示
	 * @验证点: 点击购物车中的continue shopping 按钮,页面跳转到首页 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @throws Exception 
	 * @date 2016年12月26日
	 */
	@Test
	public void cart_addProduct() throws Exception {
		Pub.checkStatusBrowser();
		String getcurrenturl = "";
		if (op.isElementPresent("cartPage_ContinueShopping", 20)) {
			Log.logInfo("点击继续购物的按钮");
			op.loopClickElement("cartPage_ContinueShopping", 3, 10, 40);
		} else {
			// 因为有可能页面延时，原本已经点击的按钮，页面已经跳转成功了，下次再重新执行时会报错，现在做容错
			Log.logInfo("已经跳转到了首页");
		}
		getcurrenturl = driver.getCurrentUrl();
		Log.logInfo("获取到的浏览器地址为:" + getcurrenturl);
		String testExpectString = "https://" + myPublicUrl2;
		if (baseURL.contains("es.")) {
			testExpectString = "https://" + myPublicUrl2;
		}
		
		Log.logInfo("需要对比的浏览器地址:" + testExpectString);
		if (getcurrenturl.equals(testExpectString)) {
			Log.logInfo("获取的地址与对比的地址一致，验证通过");
		} else {
			Log.logError("获取的地址与对比的地址不一致，验证失败");
		}

	}
	
	
	/**
	 * 
	* @测试点: 快捷支付，支付成功 
	* @验证点:  1.订单费用正确（对应的运费正确）2.显示正确支付状态
	* @使用环境：     测试环境
	* @备注： void    
	* @author zhangjun 
	* @date 2016年12月15日
	 */
	
	@Test
	public void quick_pay() {	
		double total;
		String pptotal = null;
		try {

			//signoutSuccess();// 退出用户
			interfaceMethod.crearAllCartGoods(myPublicUrl,userName);	
			mianHandles=driver.getWindowHandle();
			goodsPage();// 打开商品页面并获取商品信息
			
			if(baseURL.contains("es.")){
				//更改币种
				modifycurrency();
			}
			
			op.loopClickElement("addToBag", 4, 20, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
			total=Double.parseDouble(op.loopGetElementValue("cartprice", "data-orgp", explicitWaitTimeoutLoop).trim());
			op.loopClickElement("quickpaybtn", 4, 20, explicitWaitTimeoutLoop);// 点击PP支付按钮
			if(op.isElementPresent("paypalES", 20)){
				pptotal=op.loopGetElementText("paypalES", 10, explicitWaitTimeoutLoop).replace("$", "").replace("USD", "").replace("€", "").trim();
			}else{
				Log.logError("没有进入到支付页面");
			}
			if(op.isElementPresent("SecondaryClassificationBanner",3)){
				Log.logInfo("这里是为了测试如果截图会不会影响到其他地方的使用");
			}
			
			Assert.assertEquals(df.format(total), pptotal);
			/*Assert.assertTrue(false);*/
	
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	* @测试点: 1.购物下单，使用PP支付
			2.支付成功，订单详情里显示的支付方式，数量、金额正确	 
	* @验证点: 1.支付成功，跳转成功页面正确 
				2.成功提示的数量、金额与支付一致 3.订单中数量、金额、订单编号及付款状态正确 4.感谢购物语句正确
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月9日 
	  @修改说明
	 */
	@Test
	public void pp_pay(){
		try {
			op.loopGet(loginURL, 40, 3, 60);
			userName = "autotest23@globalegrow.com";
			if(baseURL.contains("es.")){
				userName = "autotest24@globalegrow.com";
			}
			loginprd(userName);
			interfaceMethod.crearAllCartGoods(myPublicUrl, userName);
			mianHandles = driver.getWindowHandle();
			String[] goods = goodsPage();// 打开商品页面并获取商品信
			op.loopClickElement("addToBag", 80, 10, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
			//修改币种，因为用EUR对比不了数据
			if(baseURL.contains("es.")){
				modifycurrency();
			}
			op.loopClickElement("proceedToCheckout", 20, 10, explicitWaitTimeoutLoop);// 点击checkout，进入结算页面
			itemList = getItemList();// 获取支付时的内容
			op.loopClickElement("paymentlistPayPal", 3, 3, explicitWaitTimeoutLoop);// 选择使用PP支付
			Log.logInfo("选择的是PP支付方式");
			// 商品页的原价和下单时的原价一致
			boolean Ordinarymail = df.format(Double.parseDouble(goods[1]))
					.equals(df.format(Double.parseDouble(itemList[0]))); //
			Log.logInfo("获取支付页面商品的单间原价为：" + goods[1]);
			Log.logInfo("获取支付页面商品的运输价为：" + itemList[2]);
			Log.logInfo("获取支付页面商品的追踪码价为：" + itemList[3]);
			Log.logInfo("获取支付页面商品的保险费为：" + itemList[4]);

			Double itemSum = Double.parseDouble(itemList[0]) + Double.parseDouble(itemList[2])+ Double.parseDouble(itemList[4]);// 计算的总价
			Log.logInfo("自己计算的总价为:" + itemSum);

			double total = Double.parseDouble(itemList[5]); // 页面上的总价
			Log.logInfo("页面显示的原价为:" + itemList[5]);
			boolean gettotal = df.format(Double.parseDouble(itemList[5])).equals(df.format(itemSum));
				if (!gettotal && Ordinarymail) {// 计算的总价和界面总价是否一致
					Log.logError("支付页面商品总价与界面的商品总价不一致,计算的值:" + itemSum + ",界面值：" + itemList[5]);
				} else {
					Log.logInfo("支付页面商品总价与界面的商品总价一致，且单价与商品页面单价一致，验证通过");
				}
			

			op.loopClickElement("placeYourOrder", 3, 30, explicitWaitTimeoutLoop);// 点击支付进入支付页面
			Log.logInfo("点击提交按钮，进入到支付页面");
			String[] ordercode = paypp(total);// PP登录并进行支付后页面的核对,并返回订单号

			/* 订单页面的处理对比价格和对比支付方式 */
			openOrderTail(ordercode[0], ordercode[1], ordercode[2]);

		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("支付地址不一致");
			Assert.fail();
		}

		}
	/**
	 * 
	* @测试点:    1.购物下单，选择信用卡支付 2.下单成功，订单详情里显示的支付方式，数量、金额正确 
	* @验证点: 1.下单成功 2.下单页面的总价与订单的总价相同 3.未支付状态正常 
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月10日 
	  @修改说明
	 */
	
	@Test
	public void card_pay(){
		interfaceMethod.crearAllCartGoods(myPublicUrl,userName);
		String[] goods = goodsPage();// 打开商品页面并获取商品信
		boolean Ordinarymail;// 商品页的原价和下单时的原价一致
		boolean gettotal = false;//总价
		Double itemSum;//计算的总价
	
		try {
			//更改币种
			if(baseURL.contains("es.")){
				modifycurrency();
			}
			op.loopClickElement("addToBag", 80, 20, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
			// 商品数量、商品实际价格、商品总价
			op.loopClickElement("proceedToCheckout", 20, 3, explicitWaitTimeoutLoop);// 点击checkout，进入结算页面
			//判断是否登录，没有登录进行登录
			
			if (op.isElementPresent("Login_EmailEnter", 10)) {
				if (baseURL.contains("es.")) {
					loginprd("autotest24@globalegrow.com");
				}else{
					loginprd("autotest23@globalegrow.com");
				}
			}
			String[] itemList = getItemList();// 获取支付时的内容
			itemSum = Double.parseDouble(itemList[0]) + Double.parseDouble(itemList[2])
					+ Double.parseDouble(itemList[4]);
			Ordinarymail = goods[1].equals(itemList[0]);
			gettotal = df.format(Double.parseDouble(itemList[5])).equals(df.format(itemSum));
			// 计算的总价和界面总价是否一致
			if (!gettotal && Ordinarymail) {
				Log.logError("支付页面商品总价与界面的商品总价不一致,计算的值:" + itemSum + ",界面值：" + itemList[5]);
			}
			op.loopClickElement("paymentWebcollect", 3, 30, explicitWaitTimeoutLoop);// 选择信用卡
			op.loopClickElement("placeYourOrder", 3, 30, explicitWaitTimeoutLoop);// 点击支付进入支付页面

			if (op.isElementPresent("placeYourOrder", 20)) {
				Log.logInfo("已经成功进入到了信用卡支付页面,生成订单成功");
			} else {
				Log.logError("没有进入了信用卡支付页面，生成订单失败！！！");
			}

			op.loopGet(myOrderUrl, 40, 3, 80);// 转向我的订单
			String getorderstat = op.loopGetElementText("orderstate", 3, explicitWaitTimeoutLoop);
			if (getorderstat.equals("Waiting for payment") || getorderstat.equals("Por pagar")) {
				Log.logInfo("订单状态正确，为：Waiting for payment");
				Log.logInfo("订单号" + op.loopGetElementText("OrderNo", 10, 20));
				op.loopClickElement("OrderNo", 3, 10, explicitWaitTimeoutLoop);
				String getMethodpayment = op.loopGetElementText("payment", 10, explicitWaitTimeoutLoop);// 支付方式d
				if (getMethodpayment.equals("Credit Card")) {
					Log.logInfo("支付方式正确，验证通过");
				} else {
					Log.logError("支付方式不正确，页面显示为：" + getMethodpayment + "实际为Credit card via PayPal");
				}

			} else {
				Log.logError("订单状态不正确，页面显示为：" + getorderstat + "实际为Waiting for payment");
			}

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	/**
	 * 
	* @测试点: 1.网站首页，获取顶部banner小banner以及专题模块的跳转链接
				2.请求已获取的链接,href=""，链接包含图片链接以及网页跳转
	* @验证点: 1.网站跳转的url请求数据以及网站的图片请求，都返回200，不返回404，验证通过
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月3日 
	  @修改说明
	 */
	
	@Test(threadPoolSize = 3, invocationCount = 1)
	public void home_page(){
		op.loopGet(myPublicUrl, 40, 3, 60);
		
	/*	 * String source=driver.getPageSource();//获取网页源码 String
		 * imageSrc="img\\s*src=\"?(http:\"?(.*?)(\"|>|\\s+))";//图片的正则表达式
		 * ,正式环境是https String
		 * jumpAdders="a\\s*href=\"?(http:\"?(.*?)(\"|>|\\s+))";//获取html的地址
		 */
		String source = driver.getPageSource();// 获取网页源码
		String imageSrc = "img\\s*src=\"?(https:\"?(.*?)(\"|>|\\s+))";
		Regular(imageSrc, source);// 所有图片的地址
		//由于使用正则会把 主页中所有涉及到的链接包含三级页面都会获取下来，所以不能使用正则，只能使用找到控件位置的方式
		List<WebElement> banner = driver.findElements(By.xpath("//*[@id='js-slider']/div/ul/li"));// banner图
		pageImage(banner);
		List<WebElement> page1 = driver.findElements(By.xpath("//*[@id='mainWrap']/div/a"));// banner图1
		custrom(page1);
		// 需要下滑一部分，才能检查到页面的图片
		JavascriptExecutor driver_js = (JavascriptExecutor) driver;
		driver_js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
		List<WebElement> page2 = driver.findElements(By.xpath("//*[@id='mainWrap']/section[1]/div[2]/div[1]/div/ul/li"));
		custom(page2, "p[1]/a[1]");

		List<WebElement> page3 = driver.findElements(By.xpath("//*[@id='mainWrap']/section[3]/div[2]/div[1]/div/ul/li"));
		custom(page3, "p[1]/a[1]");
		getRequest(Listurl);//对图片所有的跳转进行请求
	}
	
	
	/*
	 * 获取集合数据中指定的行
	 */
	public  void pageImage(List<WebElement> list){
		for (int i = 0; i < list.size(); i++) {
			String html=list.get(i).findElement(By.xpath("a")).getAttribute("href");
			Listurl.add(html);
		}
		
		
	}
	public  void custom(List<WebElement> list,String xapth){
		for (int i = 0; i < list.size(); i++) {
			String html=list.get(i).findElement(By.xpath(xapth)).getAttribute("href");
			Listurl.add(html);
		}
			
	}
	
	public void custrom(List<WebElement> list){
		for (int i = 0; i < list.size(); i++) {
			String html=list.get(i).getAttribute("href");
			Listurl.add(html);
		}
	}
	
	
	/**
	 * 
	* @测试点: 1.成功进入到new页面，
				2.获取new商品页面商品链接，请求所有商品链接（图片）
	* @验证点: 1.进入到new页面，地址后面加上new
			2.请求图片地址、跳转地址数据都返回200，不返回404，验证通过
	  @param    
	* @备注： void  
	* @author zhangjun 
	 * @throws Exception 
	* @date 2017年8月4日 
	  @修改说明
	 */

	@Test(threadPoolSize = 3, invocationCount = 1)
	public void new_classification() throws Exception {
		op.loopGet("https://"+myPublicUrl2, 40, 3, 60);
		enterPage("new", "new");
		

	}
	
	
	/**
	 * 
	* @测试点: 1.正常进入到swimwear页面
			2.页面数据显示正确
			3.可正常的进行翻页  
	* @验证点: 1.进入到swimwear页面，地址后面加上swimwear
			2.图片请求以及跳转链接地址请求返回都是200，
			3.选择某一个页面进行翻页，页面地址显示正确
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月7日 
	  @修改说明
	 */
	@Test(threadPoolSize = 3, invocationCount = 1)
	public void swimwear_classification(){
		enterPage("swimwear","swimwear");
		System.out.println("主线程执行完了，我获取了状态2");
	}
    

	/**
	 * 
	* @测试点: 1.随机点击new页面下的分类
		2.在获取分类下的商品链接，请求所有商品链接（图片）以及需要跳转的url地址分类正常
		3.在进行三级页面进行分类，分类获取商品链接以及需要跳转的url地址 
	* @验证点: 1.进入到new页面，地址后面加上new
		2.每个分类下的地址与实际选择的分类一致
		3.数据内容显示，请求数据都返回200，不返回404，验证通过
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月5日 
	  @修改说明
	 */
	@Test(threadPoolSize = 3, invocationCount = 1)
	public void swimwear_leve3_classification(){
		//进入到二级分类	
		try {
			op.loopClickElement("swimwear", 5, settimeout, explicitWaitTimeoutLoop);
			Log.logInfo("检查三级分类");
			List<WebElement> threeStageClassification = op.getElements("threeStageClassification");
			if (threeStageClassification.size() != 0) {
				threeStageClassification.get(InterfaceMethod.randomNumber(threeStageClassification.size() - 1))
						.findElement(By.xpath("a")).click();// 在8个二级分类中随机点击一个
				getImageGather();
			} else {
				Log.logInfo("页面没有三级页面");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 
	* @测试点: 1.分类，使用价格从高到低进行排序
	* @验证点: 整个页面数据的价格进行对比，排序正常
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月5日 
	  @修改说明
	 */
	@Test(threadPoolSize = 3, invocationCount = 1)
	public void swimwear_lowtoHighSorting() {
		priceSort(GetMethodName());
	
	}
	
	/**
	 * 
	* @测试点: 使用价格从低到高，能正常排序 
	* @验证点: 整个页面数据的价格进行对比，排序正常
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月5日 
	  @修改说明
	 */
	@Test(threadPoolSize = 3, invocationCount = 1)
	public void swimwear_hightoLowSorting() {
		priceSort(GetMethodName());
	}
	
	/**
	 * 
	* @测试点: 1.在分页2、3、4页码下进行翻页，翻页时对比翻页地址
			2.翻页成功对比数据是否显示正常 
	* @验证点: 1.选择某一个翻页，页面地址后面显示正确页码
				2.请求页面中的图片链接和跳转的链接都正常，返回数据都为200
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月5日 
	  @修改说明
	 */
	@Test(threadPoolSize = 3, invocationCount = 1)
	public void swimwear_turnPage(){
		try {
			op.loopClickElement("swimwear", 2, 10, explicitWaitTimeoutLoop);
			List<WebElement> pageTurning=op.getElements("pageTurning");
			if(pageTurning.size()==0){
				Log.logInfo("内容太少，不存在翻页的按钮");
			}else{
			int n=InterfaceMethod.randomNumber(3);
			Log.logInfo("获取的随机数:"+n);
			// 需要下滑一部分，才能点击到页码
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 3000)");  
			//随机点击一个页码，并对比是否已经跳转到了正确页码
			String element="//*[@id='js_cateTopBar2']/div[2]/div[4]/p/a["+n+"]";
			WebElement  elementStr=driver.findElement(By.xpath(element));
			String pageUrl=elementStr.getAttribute("href");
			Actions  action=new Actions(driver);
			action.moveToElement(elementStr).click().perform();
			Page.pause(1);
			Assert.assertTrue(driver.getCurrentUrl().contains(pageUrl));
			getImageGather();
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	* @测试点: 1.进入到WOMEN页面 
	* @验证点: 
			1.进入到WOMEN页面，地址包含WOMEN
			2.图片请求以及跳转链接地址请求返回都是200，
			5.选择某一个页面进行翻页，页面地址显示正确
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月7日 
	  @修改说明
	 */
	@Test
	public void women_classification(){
		enterPage("womenLink","apparel");
	}
	
	/**
	 * 
	* @测试点: 1.进入到bagss页面  
	* @验证点: 1.进入到bags页面页面，地址包含bags页面
		    2.图片请求以及跳转链接地址请求返回都是200，
			4.选择某一个页面进行翻页，页面地址显示正确
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月7日 
	  @修改说明
	 */
	@Test
	public void bagss_classification(){
		enterPage("bagss","bags");
	}
	
	/**
	 * 
	* @测试点: 进入到PLUSE SIZE页面页面 
	* @验证点: 1.进入到PLUSE SIZE页面页面，地址包含PLUSE SIZE页面
		    2.图片请求以及跳转链接地址请求返回都是200，
			4.选择某一个页面进行翻页，页面地址显示正确
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月7日 
	  @修改说明
	 */
	@Test
	public void plusesize_classification(){
		enterPage("plussizeLink", "sizes");
	} 
	
	
	/**
	 * 
	* @测试点: 1.进入到MEN页面 
	* @验证点: 
			1.进入到MEN页面，地址包含MEN
			2.图片请求以及跳转链接地址请求返回都是200，
			6.选择某一个页面进行翻页，页面地址显示正确
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月7日 
	  @修改说明
	 */
	@Test
	public void men_classification(){
		enterPage("menLink", "men");
	} 
	
	
	/**
	 * 
	* @测试点: 1.正常进入到HAIR页面
			2.页面数据显示正确
			3.可正常的进行翻页  
	* @验证点: 1.进入到HAIR页面，地址包含HAIR
			2.图片请求以及跳转链接地址请求返回都是200，
			3.选择某一个页面进行翻页，页面地址显示正确
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月7日 
	  @修改说明
	 */
	@Test
	public void HAIR_classification(){
		enterPage("hair", "wigs");
	}
	
	/**
	 * 
	* @测试点: 1.正常进入到wathers页面2.页面数据显示正确3.可正常的进行翻页 
	* @验证点: 1.进入到watchs页面，地址后面加上watchs2.图片请求以及跳转链接地址请求返回都是200，3.选择某一个页面进行翻页，页面地址显示正确
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月14日 
	  @修改说明
	 */
	@Test
	public void watch_classification(){
		enterPage("watches", "watches");
	}
	
	/**
	 * 
	* @测试点: 1.进入到JEWELRY页面
	* @验证点:1.进入到JEWELRY页面，地址包含JEWELRY
		2.图片请求以及跳转链接地址请求返回都是200，
		7.选择某一个页面进行翻页，页面地址显示正确
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月7日 
	  @修改说明
	 */
	@Test
	public void jewelry_classification(){
		enterPage("jewelry", "jewelry");
	} 
	
	
	
	/**
	 * 
	* @测试点: 1.进入到SHOES页面 
	* @验证点: 
			1.进入到SHOES页面，地址包含SHOES
			2.图片请求以及跳转链接地址请求返回都是200，
			8.选择某一个页面进行翻页，页面地址显示正确
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月7日 
	  @修改说明
	 */
	@Test
	public void shoes_classification(){
		enterPage("shoes", "shoess");
	}
	
	
	
	/**
	 * 
	* @测试点: 1.进入到ACCESSORIES页面
	* @验证点: 1.进入到ACCESSORIES页面，地址包含ACCESSORIES
			2.图片请求以及跳转链接地址请求返回都是200，
			9.选择某一个页面进行翻页，页面地址显示正确
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月7日 
	  @修改说明
	 */
	@Test
	public void accessories_classification(){
		enterPage("accessories", "accessories");
	}
	
	
	/**
	 * 
	* @测试点: 1.正常进入到HOME页面
			 2.页面数据显示正确
			 3.可正常的进行翻页  
	* @验证点: 1.进入到home页面，地址包含home
			2.图片请求以及跳转链接地址请求返回都是200，
			3.选择某一个页面进行翻页，页面地址显示正确
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月7日 
	  @修改说明
	 */
	@Test
	public void home_classification(){
		enterPage("home", "home");
	}
	
	
	/**
	 * 
	* @测试点: 1.正常进入到kid页面
			2.页面数据显示正确
			3.可正常的进行翻页  
	* @验证点: 1.进入到kid页面，地址包含kid
			2.图片请求以及跳转链接地址请求返回都是200，
			3.选择某一个页面进行翻页，页面地址显示正确
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月7日 
	  @修改说明
	 */
	
	@Test
	public void kids_classification(){
		enterPage("kids", "kids");
	}
	
	
	/**
	 * 
	* @测试点: 
			1.正常进入到CLEARANCE页面
			2.页面数据显示正确
			3.可正常的进行翻页  
	* @验证点: 1.进入到CLEARANCE页面，地址包含CLEARANCE
			2.图片请求以及跳转链接地址请求返回都是200，
			3.选择某一个页面进行翻页，页面地址显示正确
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月7日 
	  @修改说明
	 */
	@Test
	public void clearance_classification(){
		enterPage("clearance", "clearance");
	}
	
	
	
	/**
	 * 
	* @测试点: 1.商品主页，输入关键词long dress，正确显示过滤的数据，
			2.数据内容显示正常 
	* @验证点: 1.数据中有数据显示
				2.并且数据请求图片链接和链接地址都是200
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月7日 
	  @修改说明
	 */
	@Test
	public void Fuzzy_search(){
		checkSerch(GetMethodName(),"long dress","null");
		
	}
	
	/**
	 * 
	* @测试点: 输入要查询的sku，点击搜索显示商品 
	* @验证点: 显示相匹配的商品数据
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月7日 
	  @修改说明
	 */
	@Test
	public void sku_search(){
		Map<String, Object> getproduct = interfaceMethod.getGoods(myPublicUrl, "n", "y");
		getproducturl = (String) getproduct.get("url_title");
		String sku=(String) getproduct.get("goods_sn");
		Log.logInfo("sku为："+sku);
		checkSerch(GetMethodName(),sku,getproducturl);
		
	
	}
	
	/**
	 * 
	* @测试点: 显示相匹配的商品数据
	* @验证点: 显示相匹配的商品数据
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月7日 
	  @修改说明
	 */
	@Test
	public void title_search(){
		Map<String, Object> getproduct = interfaceMethod.getGoods(myPublicUrl, "n", "n");
		getproducturl = (String) getproduct.get("goods_title");
		checkSerch(GetMethodName(),getproducturl,getproducturl);
	}
	
	/**
	 * 
	* @测试点: 1.进入到某一个分类页面中，选择一款商品，并获取商品的价格
			2.点击进入到商品详情页，价格一致 
	* @验证点:商品价格与详情页价格一致
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月8日 
	  @修改说明
	 */
	@Test(threadPoolSize = 2, invocationCount = 1)
	public void product_price(){
		op.loopGet(classification, 60, 3, 60);
		try {
			String getSingerPrice = op.loopGetElementValue("price", "data-orgp", explicitWaitTimeoutLoop);
			op.loopClickElement("oneImage", 2, settimeout, explicitWaitTimeoutLoop);
			String getproducePrice = op.loopGetElementValue("productPrice", "data-orgp", explicitWaitTimeoutLoop);
			Assert.assertEquals(getSingerPrice, getproducePrice);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	* @测试点: 1.选择图片，图片能正常切换，
				2.图片并且能正常显示 
	* @验证点: 1.点击图片进行切换
			2.图片请求正常
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月8日 
	  @修改说明
	 */
	@Test(threadPoolSize = 2, invocationCount = 1)
	public void product_picture(){
		String twoShowImage = null;
		try {
			
			String urlProduct=interfaceMethod.getGoodUrl("one",myPublicUrl,"n", "y");
			if(baseURL.contains("es.")){
				op.loopGet(urlProduct.replace("www", "es"), 40, 3,20);
			}else{
				op.loopGet(urlProduct, 40, 3,20);
			}
			String oneShowImage=op.loopGetElementValue("showImage", "src", explicitWaitTimeoutLoop);
			 List<WebElement> 	imageList=op.getElements("cartImageList");
			 for (int i = 0; i < imageList.size(); i++) {
				 String url=imageList.get(i).findElement(By.xpath("img")).getAttribute("src");
				 getRequest(url);
			}
			 if(op.isElementPresent("imageTwo", 3)){
				 op.actionMoveTo("imageTwo");
				   int k=0;//因为图片loading中，耗时很长，所以加延时
					while(k>5){
						twoShowImage=op.loopGetElementValue("showImage", "src", explicitWaitTimeoutLoop);
						if(twoShowImage.equals(oneShowImage)){
							Page.pause(1);
						}else{
							break;
						}
						k++;
					}
					
					Assert.assertNotEquals(oneShowImage,twoShowImage);
			 }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	* @测试点: 进入到商品详情页，切换不同尺寸和选择不同的颜色 
	* @验证点: 1.商品的标题进行更改，并且包含尺寸和颜色
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月8日 
	  @修改说明
	 */
	@Test(threadPoolSize = 2, invocationCount = 1)
	public void product_sizeColor(){
		String sizeText = null;
		String colorText = null;
		 try {
			
			/*List<WebElement> 	colorList=op.getElements("cartColor");*/
			if(op.isElementPresent("cartSize",3)){
				List<WebElement> 	sizeList=op.getElements("cartSize");
				sizeText=getElementSize(sizeList);
			}
			if(op.isElementPresent("cartColor",3)){
				List<WebElement> 	colorList=op.getElements("cartColor");
				colorText=getElementSize(colorList);
			}
			
			String cartTitle=op.getText("cartTitle",explicitWaitTimeoutLoop);
			if(!(sizeText==null)){
				Assert.assertTrue(cartTitle.toLowerCase().contains(sizeText.toLowerCase()));
			}
			if(!(colorText==null)){
				Assert.assertTrue(cartTitle.toLowerCase().contains(colorText.toLowerCase()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 
	* @测试点: 获取颜色和尺寸的大小 
	* @验证点: 如果颜色和尺寸存在就随机选择一个颜色或者尺寸
	  @param Lists 传入的元素集合
	  @return  返回颜色和尺寸选择的内容  
	* @备注： String  
	* @author zhangjun 
	* @date 2017年8月9日 
	  @修改说明
	 */
	public String getElementSize(List<WebElement> Lists){
		String getText = null;//
		if(Lists.size()>2){
			int number=InterfaceMethod.randomNumber(Lists.size()-2);
			WebElement  element=Lists.get(number).findElement(By.xpath("a"));//选择尺寸			
				getText=element.getText();	
			element.click();	
		}
		return getText;
		
	}
	
	/**
	 * 
	* @测试点: 进入到指定的一级分类，判断是否 已经进入到分类页面，并且在一级页面的数据内容中去请求图片和url,兵器包含翻页的方法
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月7日 
	  @修改说明
	 */
	public void enterPage(String locator,String containsurl){
		
		try {
			if(op.isElementPresent("floatingLayer", 5)){
				op.loopClickElement("floatingLayer", 10, 10, explicitWaitTimeoutLoop);
			}
			
			if(locator!=null){
			op.loopClickElement(locator, 5, settimeout, explicitWaitTimeoutLoop);
			if (driver.getCurrentUrl().contains(containsurl)) {
				if(op.isElementPresent("SecondaryClassificationBanner",3)){// 分类是否存在一个大的bannner图			
					//如果存在就把图片的链接取出来，单独进行请求，但是一般就很少
					String bannerSrc=op.loopGetElementValue("SecondaryClassificationBanner", "src", explicitWaitTimeoutLoop);
					getRequest(bannerSrc); // 请求数据
				}
			}    
			 //把所有图片都给筛选出来
				getImageGather();
				//swimwear_turnPage();//进入到翻页的页面
			} else {
				Log.logError("没有进入指定页面页面，实际进入页面的地址为:"+driver.getCurrentUrl());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	/**
	 * 
	* @测试点: 对价格进行排序 
	* @验证点: 把排序后的页面数据的价格与算法排序后的价格进行对比，如果不一致就抛错
	  @param    method  根据方法名，选择排序方法，new_lowtoHighSorting 从小到大  new_lowtoHighSorting 从大到小
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月5日 
	  @修改说明
	 */
	public void priceSort(String method){
		   Double[] sortPrice;
		try {
			op.loopClickElement("swimwear", 2, 10, explicitWaitTimeoutLoop);
			ControlUsingMethod.setDisplayCSS(driver, "#js_cateTopBar > div.operate_list.fr.sort > ul");
			if(method.equals("new_lowtoHighSorting")){//从小到大排序
				op.loopClickElement("priceSortByLowPrice", 2, 10, explicitWaitTimeoutLoop);
			}else {//从大到小
				op.loopClickElement("priceSortByHighPrice", 2, 10, explicitWaitTimeoutLoop);
			}
			getImageGather();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	/**
	 * 
	* @测试点: 得到数据集合 ,注意主干和线上的图片不一致
	* @验证点: 每一个分类图片的控件都相同，获取图片的链接地址和跳转的地址
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月5日 
	  @修改说明
	 */
	public void getImageGather(){
		List<WebElement> newimage = null;
		//1212
		try {
			if(op.isElementPresent("SecondaryClassificationBanner",2)){// 分类是否存在一个大的bannner图			
				//如果存在就把图片的链接取出来，单独进行请求，但是一般就很少
				String bannerSrc=op.loopGetElementValue("SecondaryClassificationBanner", "src", explicitWaitTimeoutLoop);
				getRequest(bannerSrc); // 请求数据
			}
			if(op.isElementPresent("Imagegather", 3)){
				newimage = op.getElements("Imagegather");// 图片的控件集合 trunk 环境
			}else if(op.isElementPresent("Imagegatherprd", 3)){
				newimage= op.getElements("Imagegatherprd");//正式环境
			}else if(op.isElementPresent("Imagesingle", 3)){//正式环境单个图片
				newimage = op.getElements("Imagesingle");
			}
			
			List<String> htmlList=new ArrayList<String>();
			List<String> htmlListimage=new ArrayList<String>();
			//List<String> htmlListimage2=new ArrayList<String>();
			for (int i = 0; i < newimage.size(); i++) {
				String contentURL = newimage.get(i).findElement(By.xpath("p[1]/a[1]")).getAttribute("href");// 图片的跳转地址
				htmlList.add(contentURL);	
			}
			for(int i=0;i<newimage.size();i++){
				String imageURL = newimage.get(i).findElement(By.xpath("p[1]/a[1]/img")).getAttribute("src");// 图片的链接地址
				htmlListimage.add(imageURL);
			}
			
			/*  AddNumberTask task1 = new AddNumberTask(htmlList);  
			  AddNumberTask task2 = new AddNumberTask(htmlListimage); */
			
			/*  AddNumberTask callable1 = new AddNumberTask(htmlList);                       // 要执行的任务  
			  AddNumberTask callable2 = new AddNumberTask(htmlListimage);  
		  
		        FutureTask<Integer> futureTask1 = new FutureTask<Integer>(callable1);// 将Callable写的任务封装到一个由执行者调度的FutureTask对象  
		        FutureTask<Integer> futureTask2 = new FutureTask<Integer>(callable2);  
		   
		        ExecutorService executor = Executors.newFixedThreadPool(4);        // 创建线程池并返回ExecutorService实例  
		        executor.execute(futureTask1);  // 执行任务  
		        executor.execute(futureTask2);    
		        if(futureTask1.isDone() && futureTask2.isDone()){//  两个任务都完成  
                    System.out.println("Done");  
                    executor.shutdown();                          // 关闭线程池和服务   
                    return;  
                }  */
			
			/*ThreJo t2 = new ThreJo(htmlList);
			t2.start();
			ThreJo t3= new ThreJo(htmlListimage);
			t3.start();
			
			t2.join();
			t3.join();*/
		/*	5454*/
			   ExecutorService executor = Executors.newFixedThreadPool(6);  
		        ArrayList<Future<Integer>> resultList = new ArrayList<>();  
		        //创建并提交任务1  
		        AddNumberTask task1 = new AddNumberTask(htmlList);  
		        Future<Integer> future1 = executor.submit(task1);  
		        resultList.add(future1);  
		          
		        //创建并提交任务2  
		        AddNumberTask task2 = new AddNumberTask(htmlListimage); 
		        Future<Integer> future2 = executor.submit(task2);  
		       
		        resultList.add(future2);  
		      
		        executor.shutdown();  
		          for(Future<Integer> future : resultList){  
		            while(true){  
		                if(future.isDone() && !future.isCancelled()){  //是否已完成
		                	int number=future.get();
		                	if(number==1){
		                		  Assert.fail();
		                	  }
		                    break;  
		                }  
		                
		            }  
		        }  
		        		
		        
		    /*  //用线程池的方式提高运行速度
				ThreadPoolExecutor executor1 = new ThreadPoolExecutor(100, 120, 200, TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<Runnable>());
				
				myThread myTask = new myThread(htmlList);
				//executor.submit(myTask);
				executor1.execute(myTask);
				System.out.println("线程池中线程数目：" + executor1.getPoolSize() + "，队列中等待执行的任务数目：" + executor1.getQueue().size()
						+ "，已执行玩别的任务数目：" + executor1.getCompletedTaskCount());  
				executor1.shutdown();
		        */
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	
	

	//用线程池的方式提高运行速度
	
	/**
	 * 
	* @测试点: 搜索页面内容检查 
	* @验证点: 搜索页面内容的检查，检测搜索是否有结果，如果有结果，每个图片的请求，和网址的跳转，是否正常
	  @param method  方法名
	  @param searchkey   搜索关键词
	  @param matchcontent   商品的url或者标题
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月7日 
	  @修改说明
	 */
	public void checkSerch(String method, String searchkey, String matchcontent) {
		
		List<WebElement> newimage = null;
		String pageGetTile;// 获取的title
		try {
			op.loopSendKeysClean("search", searchkey, explicitWaitTimeoutLoop);
			op.loopClickElement("searchBtn", 10, settimeout, explicitWaitTimeoutLoop);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 判断是否有值
		try {
			if (op.isElementPresent("Imagegather", 3)) {// trunk环境
				newimage = op.getElements("Imagegather");
			} else if (op.isElementPresent("Imagegatherprd", 3)) {// 正式环境图片内容
				newimage = op.getElements("Imagegatherprd");
			} else if (op.isElementPresent("Imagesingle", 3)) {// 正式环境单个图片
				newimage = op.getElements("Imagesingle");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.logInfo("使用的总数为:" + newimage.size());
		if (newimage.size() != 0) {
			getImageGather();
			if (method.equals("sku_search")) {
				
				String pageGetUrl = newimage.get(0).findElement(By.xpath("p[1]/a[1]")).getAttribute("href");
				if (baseURL.contains("es.")) {
					pageGetUrl = pageGetUrl.substring(pageGetUrl.length() - 12, pageGetUrl.length());
					matchcontent = matchcontent.substring(matchcontent.length() - 12, matchcontent.length());

				}
				if (!pageGetUrl.contains(matchcontent)) {
					Log.logError(
							"获取的页面的url地址或标题与实际的地址或标题不一致，页面的url或标题为：" + pageGetUrl + "实际接口获取的url或标题为:" + matchcontent);
				}
			} else if (method.equals("title_search")) {
				for (int i = 0; i < newimage.size(); i++) {
					if (driver.getCurrentUrl().contains("trunk")) {
						pageGetTile = newimage.get(i).findElement(By.xpath("p[2]/a")).getText();// trunk
					} else {
						pageGetTile = newimage.get(i).findElement(By.xpath("h3/a")).getText();// 正式环境
					}

					if (pageGetTile.contains(matchcontent)) {
						Log.logInfo("已经查询到了内容");
						break;
					} else {
						Log.logError("获取的页面的url地址或标题与实际的地址或标题不一致，页面的url或标题为：" + pageGetTile + "实际获取的url或标题为:"
								+ matchcontent);
					}
				}
			}
		} else {
			Log.logError("搜索出来没有内容，请查看搜索的关键词，关键词为:" + searchkey);
		}

	}
	
	
	
	/**
	 * 
	* @测试点: 使用get方式请求数据 url,请求的是单个url地址
	* @验证点: 得到取出的数据，使用get的方式请求数据连接，判断是否返回的是200,不是200就是死链
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月5日 
	  @修改说明
	 */
	public void  getRequest(String url){
			Map<String, String> result = new HashMap<String, String>();
			result=InterfaceMethod.get(url);
			if (!"200".equals(result.get("Code"))) {
				Log.logError("请求失败，请检查图片或者是网页链接否正常显示，请求地址为："+url);
		}
	}
	
	public void getRequest(List<String> htmlList){
		Map<String, String> result = new HashMap<String, String>();			
			Iterator<String> it = htmlList.iterator();
			while (it.hasNext()) {
				String url=it.next();
				result=InterfaceMethod.get(url);;
				if (!"200".equals(result.get("Code"))) {
					Log.logError("请求失败，请检查图片或者是网页链接否正常显示，请求地址为：" + url);
					
				}
			}
			
	}
	
	
	
	
	
	/**
	 * 
	* @测试点:  正则表达式取出图片
	* @验证点:  正则表达式的使用,取出数据以后在请求数据
	  @param @param expressions 输入的表达式
	  @param @param sourceFile   要正则匹配的资源
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月4日 
	  @修改说明
	 */

	public void   Regular(String expressions, String sourceFile) {
		List<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile(expressions);
		Matcher m = p.matcher(sourceFile);
		String regularURL = "";
		while (m.find()) {
		/*	System.out.println(m.group());*/
			regularURL = m.group().replace("img src=", "").replace("a href=", "");
			regularURL = regularURL.substring(1, regularURL.length() - 1);// 会多引号
			list.add(regularURL);
		}

		Iterator<String> it = list.iterator();
		while (it.hasNext()){
			getRequest(it.next());
		}
	
		/*//用线程池的方式提高运行速度
		ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 120, 200, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>());
		
		ThreadPoolExecutor executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
		
		myThread myTask = new myThread(list);
		//executor.submit(myTask);
		executor.execute(myTask);
		System.out.println("线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" + executor.getQueue().size()
				+ "，已执行玩别的任务数目：" + executor.getCompletedTaskCount());  
         executor.shutdown();*/
	
	}

	
	
	/**
	 * 
	* @ClassName: 自己编写的线程
	* @Description: 主要是去使用线程去请求数据
	* @author zhangjun
	* @date 2017年8月8日 上午10:51:31
	*
	 */
	public class myThread implements Runnable {
		private List<String> thredsurl;// 获取的内容
		public myThread(List<String> url) {
			this.thredsurl = url;
		}
		@Override
		public void run() {
			System.out.println("正在执行task "+thredsurl);
			Map<String, String> result = new HashMap<String, String>();			
			Iterator<String> it = thredsurl.iterator();
			while (it.hasNext()) {
				String url=it.next();
				result=Pub.get(url);
				if (!"200".equals(result.get("Code"))) {
					
					Log.logError("请求失败，请检查图片或者是网页链接否正常显示，请求地址为：" + url);
					Assert.fail();
				}
			}
			 System.out.println("执行完毕task "+thredsurl+"");
		}
	}

	/**
	 * 
	* @测试点: 取出商品以后，请求是否正常
	* @验证点:  取出商品以后，get商品请求是否正常
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年8月4日 
	  @修改说明
	 */

	/**
	 * 
	* @测试点: 对比支付方式，支付的状态和支付的价格是否一致 
	* @验证点: 支付成功后的支付方式以及支付的状态和支付的价格是与订单列表中一致
	  @param @param orderCode  获取的订单号 
	  @param @param price   支付的价格
	  @param @param methodpayment  支付时付款的方式
	  @param @return   
	* @备注： String[]  
	* @author zhangjun 
	* @date 2017年8月3日 
	  @修改说明
	 */
	public void  openOrderTail(String orderCode, String price, String methodpayment) {
		String getcode;
		try {
			op.loopGet(myOrderUrl, 40, 3, 60);// 转向我的订单
			Log.logInfo("成功进入到订单页面");
			List<WebElement> rows = op.getElements("ordersNumber");// 获取列表行数，统计列表有多少个产品
			Log.logInfo(rows.size());
			getcode = rows.get(1).findElement(By.cssSelector("td:nth-child(1)> a")).getText();
			// 取得支付成功的那一个订单
			if (getcode.equals(orderCode)) {
				rows.get(1).findElement(By.cssSelector("td:nth-child(1)> a")).click();
				Log.logInfo("支付页生成的订单号" + getcode);
				Log.logInfo("进入订单页的订单编号为：" + getcode);
				Log.logInfo("查找到对应的订单号！！！！");
			}
			
			/**
			 * 订单详情页面，对比总价和订单的状态
			 */
			// 概览信息 订单编号
			String getorderStute = op.loopGetElementText("orderStute", 3, explicitWaitTimeoutLoop);// 订单状态
			String getorderDate = op.loopGetElementText("orderPrice", 3, 20);// 订单总价

			String getMethodpayment = op.loopGetElementText("payment", 3, explicitWaitTimeoutLoop).substring(0,
					methodpayment.length());// 支付方式
			Log.logInfo("订单页面订单状态为：" + getorderStute);
			Log.logInfo("订单页面订单总价为：" + getorderDate);
			Log.logInfo("订单页面支付方式为：" + getMethodpayment);

			boolean orderStute = getorderStute.equals("Paid") || getorderStute.equals("Pagado");
			boolean orderDate = df.format(Double.parseDouble(getorderDate)).equals(df.format(Double.parseDouble(price)));

			boolean orderpay = String.format("%s", getMethodpayment).equals(String.format("%s", methodpayment));
			Log.logInfo("支付成功页的总价与订单页面的总价，状态为：" + orderDate);
			Log.logInfo("支付成功页的支付状态与订单页面状态:" + orderStute);
			Log.logInfo("支付成功页支付状态为:" + orderpay);

			if (orderStute && orderDate && orderpay) {
				Log.logInfo("我的订单详情中总价和订单状态都正常,且支付方式也一致，验证通过");
			} else {
				Log.logError("下单页面，订单状态，" + getorderStute + "下单页面，总价为：" + getorderDate + "下单页面，支付方式为:" + getMethodpayment
									+ "支付时的价格：" + df.format(Double.parseDouble(price)) + "支付时的支付方式:" + methodpayment);
			}
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
	public void signoutSuccess() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String myjs = "document.querySelector(\"#header > div > div.user_box > ul\").style.display='block';";
		js.executeScript(myjs);		
		try {
			if(baseURL.contains("es.")){
				op.loopClickElement("logout_link_es", 3, 20, 500);
			}else{
				op.loopClickElement("logout_link", 3, 20, 500);
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	/**
	 * 商品页面,打开商品，并获取商品的标题，价格，数量，sku信息
	 * 
	 */
	public String[] goodsPage() {
		String[] goods = new String[4];// 记录商品标题、价格、编码、数量
		try {
			if(baseURL.contains("es.")){
				interfaceMethod.convertAddress(op, goodTestUrl);
			}else{
				op.loopGet(goodTestUrl,50, 3, 60);
				Log.logInfo("加载的页面地址为："+goodTestUrl);
			}
			goods[0] = op.loopGetElementText("goodsTittle", 3, explicitWaitTimeoutLoop);
			goods[1] =  op.loopGetElementText("unitPrice",3,10).replace("$", "").replace("€", ""); // 结算页面需要用到				
			goods[2] = op.loopGetElementText("goodsCode", 3, explicitWaitTimeoutLoop);
			goods[3] = op.loopGetElementValue("quantity", "data-orgp",  explicitWaitTimeoutLoop);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return goods;

	}
	
	/**
	 * 
	* @测试点: 修改币种 
	* @验证点: 把币种切换为usd
	  @param    
	* @备注： void  
	* @author zhangjun 
	* @date 2017年9月7日 
	  @修改说明
	 */
	
	public void modifycurrency(){
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String myjs = "document.querySelector(\"#js_topOperal > ul > li:nth-child(3) > div.lh.bzlist.jd_bzList\").style.display='block';";
		js.executeScript(myjs);	
		
		try {
			op.loopClickElement("switchcurrency", 10, 10, 30);//切换为usd
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	/**
	 * 
	 * @测试点: 获取结算页面价格统计信息
	 * @验证点:  获取结算页面价格统计信息 @使用环境： @return 测试环境 @备注： String[]
	 * @author zhangjun
	 * @date 2016年12月9日
	 */
	public String[] getItemList() {
		//由于有些商品的data-orgp的价格会出现10位小数，所以现在都从界面上取值

		String[] itemList = new String[6];
		try {		
			if(op.isElementPresent("items_sub_total",5)){
				itemList[0] =op.loopGetElementValue("items_sub_total", "data-orgp", explicitWaitTimeoutLoop);
			}
			if(op.isElementPresent("R_Points_Saving",5)){
				itemList[1] = op.loopGetElementValue("R_Points_Saving", "data-orgp",  explicitWaitTimeoutLoop);// 快递价
			}
			if(op.isElementPresent("shipping_sub_total",5)){
				itemList[2] =op.loopGetElementValue("shipping_sub_total", "data-orgp", explicitWaitTimeoutLoop);
			}
			if(op.isElementPresent("Tracking_number",5)){
				itemList[3] =op.loopGetElementValue("Tracking_number", "data-orgp",  explicitWaitTimeoutLoop);
			}
			if(op.isElementPresent("insurance",5)){
				itemList[4] =op.loopGetElementValue("insurance", "data-orgp", explicitWaitTimeoutLoop);
			}
			if(op.isElementPresent("PriceTotal",5)){
				itemList[5] =op.loopGetElementValue("PriceTotal", "data-orgp", explicitWaitTimeoutLoop);
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemList;

	}
	
	
	
	/**
	 * 
	 * @测试点: paypp
	 * @验证点: PP支付和支付会被共用，单独拿出来,登录PhP完成下单后，对价格进行对比，对比是否一致 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月14日
	 */
	public String[] paypp(double price) {
		String ppOrderCode = null;
		String[] ppMoney = new String[3];
		String[] Successfultip = new String[3];
		String ppwinow = null;
		try {		
			//获取窗口句柄时加个时间
			if (driver.getWindowHandles().size() > 1) {
				ControlUsingMethod.switchingWindow(driver, mianHandles);
				Log.logInfo("新的句柄:"+driver.getWindowHandle());
			} else {				
				if (op.isElementPresent("iframesouterlayer", 5)) {
					WebElement ppcontinue = op.MyWebDriverWait2("iframesouterlayer", 5);
					driver.switchTo().frame(ppcontinue);
					WebElement ppcontinueiframe = op.MyWebDriverWait2("iframes", 10);
					Log.logInfo("切换到第二个窗口");
					driver.switchTo().frame(ppcontinueiframe);
				}else{
					Log.logInfo("是一个窗口内容");
				}
			}
			
			if(driver.getWindowHandles().size()==1){//窗口句柄
				driver.switchTo().window(mianHandles);//切换到主窗体，现在由2个窗口变为了一个窗口
			}
		

			/*判断是否在老的测试环境还是正式的测试环境*/
				
				if (op.isElementPresent("newpagecontinubtn", 5)) {//是否存在继续支付按钮
					totalprice = op.loopGetElementText("newOrdersprice", 10, 50).replace("$", "").replace("USD", "").trim();// 获得订单总价
					Log.logInfo("页面显示继续支付按钮");
					op.loopClickElement("newpagecontinubtn", 3, 60, explicitWaitTimeoutLoop);
				} else {	
					/*PP支付时获取价格和提示*/
					if (op.isElementPresent("newpagepppay", 5)) {// 是否是登录的用户名输入框
						if(op.isElementPresent("ppiframes")){
							driver.switchTo().frame("injectedUl");
						}
						op.loopSendKeysClean("newpagepppay", ppaccount,explicitWaitTimeoutLoop);
						WebElement testorder=op.MyWebDriverWait2("newpagepppasword", 10, false);
						Operate.sendkey(ppaccount,testorder);
						op.loopClickElement("newpageloginbtn", 3, 20, explicitWaitTimeoutLoop);
						driver.switchTo().defaultContent();
					} // 不存在登录按钮Ordersprice					
					
					
					// 获取支付总价
					if (op.isElementPresent("newOrdersprice", 20)) {// 兼容支付继续按钮时，页面不一致，导致支付失败，目前做的一些兼容
						if(driver.getPageSource().contains("€")){
							totalprice = op.loopGetElementText("paypalES", 10, explicitWaitTimeoutLoop).replace("€", "").replace("EUR", "").replace("€", "").replace("$", "").replace("USD", "").trim();// 获得订单总价					
						}else{						
						   totalprice = op.loopGetElementText("newOrdersprice", 10, explicitWaitTimeoutLoop).replace("$", "").replace("USD", "").replace("€", "").trim();// 获得订单总价						
						   Log.logInfo("*****物价总计：" + totalprice);
						}
						Log.logInfo("点击继续支付按钮");
						op.loopClickElement("newpagecontinubtn", 1, 30, explicitWaitTimeoutLoop);
						
					} else {
						if(op.isElementPresent("Ordersprice",10)){
							Log.logError("内容不存在呢，不执行，目前没有做处理");
						}else{
							try {
								ppwinow=driver.getWindowHandle();
								Log.logInfo("ppwinow:"+ppwinow);
								totalprice = op.loopGetElementText("Ordersprice", 10, explicitWaitTimeoutLoop).replace("Total $", "").replace(",", ".").replace("USD", "").replace("€", "").trim();// 获得订单总价
								Log.logInfo("*****物价总计：" + totalprice);
								Log.logInfo("点击继续支付按钮");
								op.loopClickElement("oldePPcontinue", 3, 60, explicitWaitTimeoutLoop);
							} catch (Exception e) {
								e.printStackTrace();
								Log.logInfo("是支付页面卡有被锁了，");
							}
						}
					}
				}
				ppMoney[0] = totalprice;// 存入总价				
			if(driver.getWindowHandles().size()>1){
				driver.close();//关掉当前这个窗口
				driver.switchTo().window(mianHandles);
			}else{
				Log.logInfo("目前只有一个句柄");
				driver.switchTo().window(mianHandles);
			}
			
			/* 我是分割线，以下内容正式环境和测试环境都相同 */
			if (op.isElementPresent("quickpagebtn",7)) {// 判断是否是快捷PP支付
				Log.logInfo("目前使用的是快捷PP支付！！！");
				op.loopClickElement("quickpagebtn", 3, 50, explicitWaitTimeoutLoop);// 地址按钮
				op.loopClickElement("paymentlistPayPal", 3, 50, explicitWaitTimeoutLoop);// 选择PP方式
				totalprice = op.loopGetElementText("PriceTotal", 10, 30).replace("$", "").replace("€", "");// 保存总价
				price=Double.parseDouble(totalprice);//原因这里才是传入到页面的真实价格
				ppMoney[0] = totalprice;  //快捷支付只需要对比支价格和提交的价格，这个其实是多余的，为了不让后面出错，才这样写
				// 确认支付按钮
				op.loopClickElement("placeYourOrder", 3, 50, explicitWaitTimeoutLoop);
			} else {
				Log.logInfo("目前使用的是普通支付！！！");
			}
			
			if (op.isElementPresent("ppPromptMsg",7)) {
				Log.logInfo("成功进入到了支付页面，并且获取的支付成功提示语！！！");
			} else {
				Log.logError("没有获取到支付成功的提示语，未进入支付成功页面，无法进行下一步的操作！！！");
			}
			
			
			/*支付成功页面的一些对比*/
			if (baseURL.contains("/es/")) {
				Log.logInfo("多语言环境不对比价格和支付成功页面的所有内容，原因是支付成功的价格还是用的网站价格与PP支付价格不一致");
				ppOrderCode = op.loopGetElementText("OrderGenerated", 5, explicitWaitTimeoutLoop);// 获取订单号
				payType = op.loopGetElementText("obtainpayment", 5, explicitWaitTimeoutLoop);// 获取支付方式
				Log.logInfo("获取的订单编号：" + ppOrderCode);
				Log.logInfo("获取的支付方式：" + payType);
				Successfultip[0] = ppOrderCode;
				Successfultip[1] = "0";
				Successfultip[2] = payType;
			} else {
				/* 多语言支付环境不对比订单价格和支付价格 */
				Log.logInfo("页面实际支付金额：" + df.format(Double.parseDouble(ppMoney[0])));
				Log.logInfo("提交订单的支付金额" + df.format(price));
				boolean placeorder = df.format(Double.parseDouble(ppMoney[0])).replace("$", "").replace("USD", "").equals(df.format(price));// 判断下单页面和实际价格
				if (placeorder) {
					Log.logInfo("下单页面总价与提交订单页面总价一致！");
				} else {
					Log.logError("下单页面总价与提交订单页面总价不一致，后面执行出错！");
				}
				/**
				 * 支付成功页面，获取内容
				 */
				String ppPromptMsg = op.loopGetElementText("ppPromptMsg", 5, explicitWaitTimeoutLoop);// 获取的成功语句
				ppOrderCode = op.loopGetElementText("OrderGenerated", 5, explicitWaitTimeoutLoop);// 获取订单号
				
					Log.logInfo("2017年2月17日支付页面正式环境控件位置有变化");
					payType = op.loopGetElementText("obtainpayment", 5, explicitWaitTimeoutLoop);// 获取支付方式
					payMoney=op.loopGetElementValue("payMoneny", "data-orgp", explicitWaitTimeoutLoop);
					//payMoney = op.loopGetElementText("totalprice", 10, explicitWaitTimeoutLoop).replace("$", "").replace("€", "");// 获取支付的总价
				Log.logInfo("获取的成功提示语：" + ppPromptMsg);
				Log.logInfo("获取的订单编号：" + ppOrderCode);
				Log.logInfo("获取的支付方式：" + payType);
				Log.logInfo("获取的支付金额：" + payMoney);
				Successfultip[0] = ppOrderCode;
				Successfultip[1] = payMoney;
				Successfultip[2] = payType;

				String sucessMsg = "We appreciate your shopping! Your order has been submitted successfully, please remember your order number";
				if (!ppPromptMsg.contains(sucessMsg)) {
					Log.logInfo("支付成功提示语有误，页面显示的提示语句为:" + ppPromptMsg);
				} else {		
					Log.logInfo("支付成功, 且提示语正确");
				}
				// 0=普通支付 1=快捷支付
				// 支付页金额判断
				if (payMoney.equals(ppMoney[0])) {
					// 支付方式判断
					if (payType.equals("PayPal")) {
						Log.logInfo("选择的支付方式为:PayPal,成功界面显示的方式:" + payType + ",验证成功！！！");

					} else if (payType.equals("PayPal|Wallet")) {
						Log.logInfo("选择的支付方式为:PayPal|Wallet,成功界面显示的方式:" + payType + ",验证成功！！！");

					} else {
						Log.logError("选择的支付方式为:PayPal,成功界面显示的方式:" + payType + ",不一致,验证失败！！！");
					}

				} else {
					if (df.format(Double.parseDouble(payMoney)).equals(df.format(Double.parseDouble(ppMoney[1])))) {
						Log.logInfo("支付:" + ppMoney[1] + ",与支付成功页面金额:" + payMoney + ",一致,验证成功！！！！");
					} else {
						Log.logError("支付:" + ppMoney[1] + ",与支付成功页面金额:" + payMoney + ",不一致,验证失败！！！");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(driver.getWindowHandles().size()>1){
				Log.logInfo("在最外层已经报错了");
				driver.close();//关掉当前这个窗口
				driver.switchTo().window(mianHandles);
			}else{
				Log.logInfo("目前只有一个句柄");
				driver.switchTo().window(mianHandles);
			}
			
		}
		return Successfultip;
	}
	
	/**
	 * 初始化url
	 * 
	 */
	public void urlInit(){
		if(baseURL.contains("es.")){//西班牙语测试环境
			Log.logInfo("现在是西班牙语正式环境");
	
			myPublicUrl="https://rosewholesale.com/";
			rates="https://cart.rosewholesale.com/data-cache/currency_huilv.js";//汇率换算
			myPublicUrl2="es.rosewholesale.com/";
			classification="https://es.rosewholesale.com/online/relojes-c14/";
			goodTestUrl=interfaceMethod.getGoodUrl(myPublicUrl,"one");	
			myOrderUrl = "https://user.rosewholesale.com/es/m-users-a-order_list.htm";
			loginURL="https://login.rosewholesale.com/es/m-users-a-sign.htm";
		}else if(baseURL.contains(".trunk.")){
			myPublicUrl="http://rosewholesale.com.trunk.s1.egomsl.com/";
			loginURL="http://login.rosewholesale.com.trunk.s1.egomsl.com/m-users-a-sign.htm";
			goodTestUrl=interfaceMethod.getGoodUrl(myPublicUrl,"one");	
			myOrderUrl = "http://user.rosewholesale.com.trunk.s1.egomsl.com/m-users-a-order_list.htm";// 订单页面上面还有问题
			myPublicUrl2="rosewholesale.com.trunk.s1.egomsl.com/";
			classification="http://rosewholesale.com.trunk.s1.egomsl.com/cheap-online/wigs-c22/";//分类地址
			//url="http://login.rosewholesale.com.trunk.s1.egomsl.com/m-users-a-sign.htm";//测试的地址
		}else {
	
			myPublicUrl="https://rosewholesale.com/";
			loginURL="https://login.rosewholesale.com/m-users-a-sign.htm";
			goodTestUrl=interfaceMethod.getGoodUrl(myPublicUrl,"one");	
			myOrderUrl = "https://user.rosewholesale.com/m-users-a-order_list.htm";// 订单页面上面还有问题
			myPublicUrl2="www.rosewholesale.com/";
			classification="https://www.rosewholesale.com/cheap-online/wigs-c22/";//分类地址
		}
	}

	 /**
	  * 
	 * @ClassName: 实现callbel接口，主要是为了返回执行接口，如果执行失败，就断言失败
	 * @Description: TODO(这里用一句话描述这个类的作用)
	 * @author zhangjun
	 * @date 2017年8月19日 下午4:25:21
	 *
	  */

	class AddNumberTask implements Callable<Integer> {
		List<String> list;
	    int   isbreaks=0; //是否已经中止
		public AddNumberTask(List<String> htmlList) {
			list = htmlList;
		}

		public Integer call() throws Exception {
			Map<String, String> result = new HashMap<String, String>();
			Iterator<String> it = list.iterator();
			while (it.hasNext()) {
				String url = it.next();
				result = InterfaceMethod.get(url);
				if (!"200".equals(result.get("Code"))) {
					isbreaks = 1;
					Log.logError("请求失败，请检查图片或者是网页链接否正常显示，请求地址为：" + url);
				}
			}
			return isbreaks;
		}

	}

	/*class ThreJo extends Thread {
		List<String> list;

		ThreJo(List<String> htmlList) {
			list = htmlList;
		}

		public void run() {
			Map<String, String> result = new HashMap<String, String>();
			Iterator<String> it = list.iterator();
			while (it.hasNext()) {
				String url = it.next();
				result = InterfaceMethod.get(url);
				if (!"200".equals(result.get("Code"))) {
					Log.logError("请求失败，请检查图片或者是网页链接否正常显示，请求地址为：" + url);

				}
			}

		}
	}*/

	/* } */
}
