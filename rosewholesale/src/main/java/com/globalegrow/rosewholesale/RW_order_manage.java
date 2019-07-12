package com.globalegrow.rosewholesale;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;
import java.util.Map;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.globalegrow.autotest_encrypt.Operate;
import com.globalegrow.base.Startbrowser;
import com.globalegrow.code.Locator;
import com.globalegrow.code.Page;
import com.globalegrow.custom.ControlUsingMethod;
import com.globalegrow.custom.InterfaceMethod;

import com.globalegrow.util.Log;
import com.globalegrow.util.Op;
import com.globalegrow.util.Pub;
import com.globalegrow.util.testInfo;



public class RW_order_manage extends Startbrowser {
	private String className = GetClassName();
	private String testCaseProjectName = "rosewholesale";
	private String classNameShort;
	private testInfo info = null;
	private String baseURL;
	private String setpayUrl;
	private String timeStamp;// 时间戳
	private long explicitWaitTimeoutLoop =40;
	boolean paySucessMsgFlag = false;
	public Date sendEmailDate = new Date();
	private long expliceTimeout=20;
	boolean loginFlag = true;// 判断是否有登录
	private String getppHandle;
	private String myPublicUrl = "";
	private String loginUrl = "";// 登录url
	private String checkoutUrl = "";// 结算url
	private String myOrderUrl = "";// 我的订单页面url
	private String goodTestUrl = "";// 商品的测试url地址
	private String checkoutUrl2 = "";// 未登录检查连接
	private String checknologintUrl="";
	private String url;
	//private String loginName = "autotest18@globalegrow.com";// 登录账户
	private String loginName;
	private String addressbook;
	private String totalprice;
	private String getkeys;
	private String ppaccount; //pp账户
	private String payType;//支付方式
	private String payMoney;//支付的金额
	private  String mianHandles;
	private long  explicitWaitTimeout=60;
	private Double rate;//汇率
	private String rates;//获取汇率的地址
 	DecimalFormat df = new DecimalFormat("#0.00");
	InterfaceMethod interfaceMethod;
	boolean paymentPage=false;//是否进入到了支付页面
	String[] itemList;//获取页面信息内容
	@Parameters({ "testUrl" }) 
	private RW_order_manage(String testUrl) {
		moduleName = className.substring(className.lastIndexOf(".") + 1); // 必要
		info = new testInfo(moduleName); // 必要	
		baseURL = testUrl;
		
	}
	@Parameters({ "browserName", "keys","PPaccount","loginEmial"})
	@BeforeClass
	public void beforeClass(String browserName, String keys,String PPaccount,String loginEmial) {
		String methodName = GetMethodName();
		String methodNameFull = moduleName + "." + methodName;
		Log.logInfo("**************Ready to test (" + moduleName + ")!!!**************\n");
		Log.logInfo("(" + methodNameFull + ")...beforeClass start...");
		// 加载登录
		try {
			start(browserName); // 必要,初始化driver,web,二选一
	
			op.setScreenShotPath(screenShotPath);
			driver = super.getDriver(); // 启动driver
			driver.manage().timeouts().pageLoadTimeout(pageLoadTimeoutMax, TimeUnit.SECONDS);
			interfaceMethod=new InterfaceMethod();
			getkeys=keys;			
			loginName=loginEmial;
			ppaccount=PPaccount;//登录账
			urlInit();// 初始化URL
			op.loopGet(baseURL, 60, 3, 80);
			Log.logInfo("baseURL:" + baseURL);
			login();
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			timeStamp = simpleDateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			//driver.quit();
			
		} finally {
			Log.logInfo("(" + methodNameFull + ")...beforeClass stoop...\n\n");
		}
	}
	
	
	@AfterClass
	public void afterClass() {
		String methodName = GetMethodName();
		String methodNameFull = classNameShort + "." + methodName;
		Log.logInfo("(" + methodNameFull + ")...afterClass start...");
	
		try {
			/*op.loopGet(loginUrl, 40, 3, 60);
			if (op.isElementPresent("Login_EmailEnter",10)){
				login();
			}
 			if(op.isElementPresent("LoginSuccessful",10)){
				Log.logInfo("清除所有已经取消的订单场景，登录成功");
				deleteCancelOrder();//删除订单
			}else{
				Log.logInfo("清除所有已经取消的订单场景，登录失败");				
			}*/
			driver.quit();
			beforeTestRunFlag = false; // 必要
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			interfaceMethod.deleteCancelOrder(myPublicUrl,loginName);// 删除订单
			Log.logInfo("(" + methodNameFull + ")...afterClass stoop...\n\n");
		}
	}

	/**
	 * 每个测试用例执行完成时，延时1秒并打印相关信息
	 * 
	 * @author yuyang 创建时间:2016-11-07 更新时间:2016-11-07
	 */
	@AfterMethod
	public void afterMethod() {
		info.lastMethodName = info.methodName;
		Log.logInfo("(" + info.methodNameFull + ")...afterMethod stoop...\n\n");
	
	}

	/**
	 * 登录，由于测试地址的前置条件，在beforeClass中运行
	 * 
	 * @throws RetryException
	 *
	 */
	public void login() {
		String requestId = Pub.getRandomString(6);
		String methodName = GetMethodName();
		try {
			number++;
			String logincode = InterfaceMethod.IF_Verification(driver, myPublicUrl, requestId, getkeys, "login");
				if (logincode.equals("verify") && number < 6) {
					driver.navigate().refresh();
					Page.pause(3);
					login();
				} else {
					op.loopSendKeys("Login_EmailEnter", loginName, 3,explicitWaitTimeoutLoop);
					op.loopSendKeys("Login_PasswordEnter", "123456", 3,explicitWaitTimeoutLoop);
					Page.pause(1);
					if (op.isElementPresent("Login_verification",20)) {
						op.loopSendKeys("Login_verification", logincode, 3, explicitWaitTimeoutLoop);
					} else {
						Log.logInfo("验证码输入框已被关闭，不输入验证码登录");
					}
					op.loopClickElement("Login_SignBtn", 20, 10, explicitWaitTimeoutLoop);
				     //判断登录成功
					for (int  i= 0; i<10; i++) {
						Page.pause(3);
						Log.logInfo("第"+i+"次");
						  String LoginStats=op.loopGetElementText("signlink", 20, explicitWaitTimeoutLoop);
						  if (LoginStats.contains("Hi, autot")) {
							  Log.logInfo("登录成功");
							  mianHandles=driver.getWindowHandle();
							  loginFlag=false;
							  break;
						}
					}
					if(loginFlag==true){
						captureScreenAll(projectName, methodName);
				    	 Log.logError("登录失败，以下内容不执行");
				    
					}
				}

		} catch (Exception e) {
			e.printStackTrace();
			//driver.quit();
		}finally {
			if(baseURL.contains("/es/")){//针对多语言的换算
				rate=interfaceMethod.rates(rates);//获取的价格
			}
			
		}

	}
	int number = 0;
	
	/**
	 * 
	 * @测试点: 普通支付，未登录
	 * @验证点: 购物下单点击Proceed to checkout,点击则进入登陆页面 @使用环境： 测试环境
	 * @备注：payment_notlogged与payment_notaddress需按顺序执行
	 * @author zhangjun
	 * @date 2016年12月12日
	 * 修改说明：2016.12.30 区分登录和退出的区别
	 */
	@Test
	public void payment_notlogged() {		
		;
		String geturl = null;
		try {
			op.loopGet(addressbook, 40, 3, 60);	
			 mianHandles=driver.getWindowHandle();//防止没有正常登录	
			int startSumAdress = op.getElements("ShippingAddressList").size();
			if (startSumAdress >= 1) {
				Log.logInfo("初始地址个数：" + startSumAdress);
				deleteAdress();// 删除全部的地址
			}
					
			/*我是分割线，做以上内容，是为了让下一个用例正常执行，以上的内容是先删除地址*/
			String getName=op.loopGetElementText("signlink", 3, explicitWaitTimeoutLoop);
			boolean loginState=getName.contains("autot");//区分测试与正式环境
			boolean loginStateFormal=getName.contains("shopp");//主要是区分登录与登出
			if (loginState||loginStateFormal) {// 为真则已登录
				Signout_Success();// 登出		
				Log.logInfo("用户已经成功退出");
			}			
			Log.logInfo("用户已经是退出状态,不需要退出登录");
			if(baseURL.contains("/es/")){
				interfaceMethod.convertAddress(op,goodTestUrl);
			}else{
				op.loopGet(goodTestUrl, 60, 3, 80);// 打开商品页面并获取商品信息
			}

			op.loopClickElement("addToBag", 5, 10, explicitWaitTimeoutLoop); // 添加至购物车
			op.loopClickElement("proceedToCheckout", 5, 10, explicitWaitTimeoutLoop);// 点击购买
			Page.pause(3);
		    geturl = driver.getCurrentUrl();
		    Log.logInfo("获取页面的url地址为"+geturl);
			if (geturl.equals(checknologintUrl)) {
				Log.logInfo("已经到登录页面,与需要验证的url匹配，验证通过");
			} else {
				Log.logError("没有到登录页面。验证不通过,实际获取到的url！！！"+geturl);
			}
			Pub.printTestCaseInfo(info);

		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("geturl=" + geturl);
			Assert.fail();
		}
	
	}
	/**
	 * 
	* @测试点: 普通支付，已登录，无地址 
	* @验证点: 1.登录后，无地址，新增地址成功2.跳转到结账地址 
	* @使用环境：     测试环境
	* @备注： payment_notlogged与payment_notaddress需按顺序执行   
	* @author zhangjun 
	* @date 2016年12月18日
	 */

	@Test
	public void payment_notaddress(){
		
		String geturl = null;
		try {
			op.navigateRefresh(40, 3, 40);
			login();
			interfaceMethod.crearAllCartGoods(myPublicUrl,loginName);// 清空购物车		
			if(baseURL.contains("/es/")){
				interfaceMethod.convertAddress(op, goodTestUrl);
			}else{
				op.loopGet(goodTestUrl, 40, 3, 80);// 打开商品页面并获取商品信息
			}
			op.loopClickElement("addToBag", 5, 10, explicitWaitTimeoutLoop); // 添加至购物车
			op.loopClickElement("proceedToCheckout", 5, 10, explicitWaitTimeoutLoop);// 点击购买
			addaddress();
			geturl = driver.getCurrentUrl();
			Log.logInfo("获取页面的url地址为"+geturl);
			if (geturl.equals(checkoutUrl)||geturl.equals(checkoutUrl2)) {
				Log.logInfo("已经到结算页面,与需要验证的url匹配，验证通过");
			} else {
				Log.logError("没有到结算页面。验证不通过，实际获取的地址为：！！！" + geturl);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("geturl=" + geturl);
			Assert.fail();
		}

	}
	
	
	/**
	 * 
	 * @测试点: 普通支付，已登录，有地址
	 * @验证点: 1.用户未登录，有寄送地址2.下单，并且进行付款，付款后，地址、状态、价格都正常
     @使用环境： 测试环境 
     @备注： void
	 * @author zhangjun
	 * @date 2016年12月12日 修改时间2016-12
	 * 更新说明(2016-12-29)：登录与登出控件定位相同增加它的唯一标识
	 */

	@Test
	public void paymentdetermine_address() {
		;
		try {	
			pay_pp();//支付
						
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("支付未成功" );
			Assert.fail();
		}
	}

	
	/**
	 * 
	 * @测试点: 平邮加跟踪码
	 * @验证点: 1.订单费用正确（对应的运费正确）2.对应的运费价格+平邮价格显示正确 
	 * @使用环境： 测试环境
	 * @备注：Shipping开头的
	 *       Ordinarymail紧密相关，等涉及到快递方式，需要先执行Ordinarymail_trackingcode
	 * @author zhangjun
	 * @date 2016年12月10日
	 * 修改：增加下单后在订单页面进行对比总价和物流方式
	 */

	@Test
	public void Shippingordinary_trackingcode() {
		;
		try {	
			String[] goods =getGoodsSettlement();			
			if (op.isElementPresent("Login_EmailEnter")) {
				Log.logInfo("在Shippingordinary_trackingcode类之前，是未的登录的状态，现在开始登录");
				login();
			} else {
				Log.logInfo("状态是已登录");
			}
			
			op.loopClickElement("doRackingcode", 8, 10, explicitWaitTimeoutLoop);// 勾选上跟踪码
			//setCancelWall();
			Log.logInfo("已经勾选上跟踪码");
			op.loopClickElement("paymentlistPayPal", 5, 10, 30);
			// 获取价格
			String gettrackingcode = op.loopGetElementText("trackingcode_price",10, explicitWaitTimeoutLoop).replace("$", "").replace("€", "");// 获取跟踪码的价格
			String gettotalprice = op.loopGetElementText("PriceTotal", 10, explicitWaitTimeoutLoop).replace("$", "").replace("€", "");// 获得总价
			Log.logInfo("获取跟踪吗价格为："+gettrackingcode);
			Log.logInfo("获取页面商品的总价为："+gettotalprice);
			
			String[] itemList = getItemList();
			Double itemSum = Double.parseDouble(itemList[0]) + Double.parseDouble(itemList[2]) + Double.parseDouble(itemList[3])+ Double.parseDouble(itemList[4]);
			Log.logInfo("获取下单页面商品的单间原价为："+goods[1]);
			Log.logInfo("获取下单页面商品的运输价为："+itemList[2]);
			Log.logInfo("获取下单页面商品的追踪码价为："+itemList[3]);
			Log.logInfo("获取下单页面商品的保险费为："+itemList[4]);
			Log.logInfo("自己计算的总价为："+itemSum);
			boolean Ordinarymail = df.format(Double.parseDouble(goods[1])).equals(df.format(Double.parseDouble(itemList[0]))); // 商品原价和结算价一致			
			boolean gettrackingprice = df.format(Double.parseDouble(itemList[3])).equals(df.format(Double.parseDouble(gettrackingcode)));// 使用跟踪码价格一致		
			boolean gettotal = df.format(itemSum).equals(gettotalprice); // 总价一致
            
			if(baseURL.contains("/es/")){
				/*多语言下价格要用美元的原价乘燚汇率*/
				Ordinarymail = df.format(Double.parseDouble(goods[1])).equals(df.format(Double.parseDouble(itemList[0])*rate));
				gettotal = df.format(itemSum*rate).equals(gettotalprice); // 总价一致
				gettrackingprice = df.format(Double.parseDouble(itemList[3])*rate).equals(df.format(Double.parseDouble(gettrackingcode)));
			}			
			if (Ordinarymail  && gettrackingprice && gettotal) {				
				Log.logInfo("物流计算价格和结算时一致，且商品单价与结算页也一致");
			} else {
				Log.logError("物流计算价格和结算时不一致,验证不通过,商品原价:"+Ordinarymail+"跟踪码价格:"+gettrackingprice+"总价价格:"+gettotal);
			}
			op.loopClickElement("paymentlistPayPal", 3, 3, 20);// 选择使用PP支付
			op.loopClickElement("placeYourOrder", 3, 30, explicitWaitTimeoutLoop);// 点击支付进入支付页面
		
			//ControlUsingMethod.switchingWindow(driver, mianHandles);		
			if(enterPaymentPage()==true){
				Log.logInfo("成功进入到支付页面");
			}else {
				Log.logError("进入到支付页面失败");
			}	
			
			//ControlUsingMethod.switchingCloseWindow(driver, mianHandles);
			if(baseURL.contains("/es/")){
				transportationState("Rango de Envío Promedio",gettotalprice);
			}else{
				transportationState("Flat Rate Shipping",gettotalprice);
			}
			
			Pub.printTestCaseInfo(info);
		} catch (Exception e) {	
			e.printStackTrace();
			Log.logWarn("平邮加运费价格和结算时不一致,验证不通过");
			Assert.fail();
		}
	
	}

	/**
	 * 
	 * @测试点: 平邮不加跟踪码
	 * @验证点: 1.订单费用正确（对应的运费正确），订单总价正确2.对应的运费价格显示正确3.显示的物流方式正确 @使用环境： 测试环境
	 * @备注：需要先执行Ordinarymail_trackingcode
	 * @author zhangjun
	 * @date 2016年12月12日
	 * 修改说明：功能的跟踪码有变化，现在默认去掉跟踪码
	 */
	@Test
	public void Shippingordinary() {
		;
		String gettotalprice = null;
		try {
			getGoodsSettlement();
			String[] itemList = getItemList();
			Double itemSum = Double.parseDouble(itemList[0]) + Double.parseDouble(itemList[2]) + Double.parseDouble(itemList[4]);
			Log.logInfo("获取下单页面商品的运输价为："+itemList[2]);
			Log.logInfo("获取下单页面商品的保险费为："+itemList[4]);
			Log.logInfo("计算总价为：" + itemSum);// 计算的总价
			
			gettotalprice =op.loopGetElementText("PriceTotal", 10, 30).replace("$", "").replace("€", "");// 获得总价
			Log.logInfo("获得总价为：" + itemSum);// 计算的总价
			boolean gettotal = df.format(itemSum).equals(gettotalprice); // 总价是否一致
			
			if(baseURL.contains("/es/")){
				/*多语言下价格要用美元的原价乘燚汇率*/
				gettotal = df.format(itemSum*rate).equals(gettotalprice); // 总价一致
			}
			if (gettotal) {
				Log.logInfo("计算的价格和与结算时一致");
			} else {			
				Log.logError("计算的价格和结算时不一致");
			}
			
			getppHandle=driver.getWindowHandle();
			op.loopClickElement("paymentlistPayPal", 3, 3, 20);// 选择使用PP支付
			op.loopClickElement("placeYourOrder", 3, 30, 100);// 点击支付进入支付页面
			
			//ControlUsingMethod.switchingWindow(driver, mianHandles);		
			if(enterPaymentPage()==true){
				Log.logInfo("成功进入到支付页面");
			}else {
				Log.logError("进入到支付页面失败");
			}			
				
			//ControlUsingMethod.switchingCloseWindow(driver, mianHandles);
			if(enterPaymentPage()==true){
				transportationState("Rango de Envío Promedio",gettotalprice);
			}else{
				transportationState("Flat Rate Shipping",gettotalprice);
			}
			
			Pub.printTestCaseInfo(info);
	
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("gettotalprice:"+gettotalprice);
			Assert.fail();
		}
	
	}

	/**
	 * 
	 * @测试点: 标准邮
	 * @验证点: 1.订单费用正确（对应的运费正确）2.运费价格和快递方式显示正确 @使用环境： 测试环境
	 * @备注：需要先执行Ordinarymail_trackingcode
	 * @author zhangjun
	 * @date 2016年12月12日
	 */
	
	@Test
	public void Shippingstandard() {
		;
		String getOrdinarymail=null;
		try {

			if (shippingMethod().contains("2")) {//标准邮
			
			op.loopClickElement("Standardbtn", 5, 10, explicitWaitTimeoutLoop);
			op.loopClickElement("paymentlistPayPal", 5, 10, explicitWaitTimeoutLoop);
		    itemList = getItemList();
			// 总价	
			itemList[5] = op.loopGetElementText("PriceTotal", 10, 30).replace("$", "").replace("€", "");
			Double itemSum = Double.parseDouble(itemList[0]) + Double.parseDouble(itemList[2]) + Double.parseDouble(itemList[4]);
			String gettotalprice = op.loopGetElementText("PriceTotal", 10, 30).replace("$", "").replace("€", "");// 获得总价	
			getOrdinarymail =  op.loopGetElementText("Standardprice", 10, 30).replace("$", "").replace("€", "");
			Log.logInfo("获得总价为:"+gettotalprice);
			Log.logInfo("自己计算的总价为:"+itemSum);
			
			boolean Ordinarymailprice = df.format(Double.parseDouble(itemList[2])).equals(getOrdinarymail);// 邮费价格正确
			boolean gettotal = df.format(itemSum).equals(gettotalprice); // 总价是否一致	
			
			if(baseURL.contains("/es/")){
				/*多语言下价格要用美元的原价乘以汇率*/
				gettotal = df.format(itemSum*rate).equals(gettotalprice); // 总价一致
				Ordinarymailprice =df.format(Double.parseDouble(itemList[2])*rate).equals(getOrdinarymail);// 邮费价格正确
			}	
			
			if (Ordinarymailprice && gettotal) {			
				Log.logInfo("物流价格正确，计算的价格和结算时一致,验证通过");
			} else {				
				Log.logError("计算的价格和结算时不一致，验证不通过,物流价格:"+Ordinarymailprice+"总价一致"+gettotal);
			}	
			getppHandle=driver.getWindowHandle();
			setCancelWall();
			op.loopClickElement("paymentlistPayPal", 3, 3, 20);// 选择使用PP支付
			op.loopClickElement("placeYourOrder", 80, 20, explicitWaitTimeoutLoop);// 点击支付进入支付页面
	
		
			
			if(enterPaymentPage()==true){
				Log.logInfo("成功进入到支付页面");
			}else {
				Log.logError("进入到支付页面失败");
			}			
				
			//ControlUsingMethod.switchingCloseWindow(driver, mianHandles);
			if(baseURL.contains("/es/")){
				transportationState("Envío Estandar",gettotalprice);
			}else{
				transportationState("Standard Shipping",gettotalprice);
			}
			
			ControlUsingMethod.switchingCloseWindow(driver, getppHandle);
			}else{
				Log.logInfo("页面不存在标准邮的运输方式");
			}
			Pub.printTestCaseInfo(info);
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("getOrdinarymail=" + getOrdinarymail);
			Assert.fail();
			
		}
	
	}

	/**
	 * 
	 * @测试点: 快递
	 * @验证点: 1.订单费用正确（对应的运费正确）2.运费价格和快递方式显示正确 @使用环境： 测试环境 @备注：
	 *       需要先执行Ordinarymail_trackingcode
	 * @author zhangjun
	 * @date 2016年12月12日
	 */
	@Test
	public void Shippingexpedited() {
		String getOrdinarymail=null;
	
		try {
			if (shippingMethod().contains("4")) {// 快递运输方式
				op.loopClickElement("expeditedbtn", 80, 10, explicitWaitTimeoutLoop);
				String[] itemList = getItemList();
				Double itemSum = Double.parseDouble(itemList[0]) + Double.parseDouble(itemList[2])
						+ Double.parseDouble(itemList[4]);
				Log.logInfo("获取下单页面商品的运输价为：" + itemList[2]);
				Log.logInfo("获取下单页面商品的保险费为：" + itemList[4]);
				Log.logInfo("计算的总价：" + itemSum);// 计算的总价
				String gettotalprice = op.loopGetElementText("PriceTotal", 10, 30).replace("$", "").replace("€", "");// 获得总价
				getOrdinarymail = op.loopGetElementText("expeditedprice", 10, 30).replace("$", "").replace("€", ""); // 获得快递价格
				Log.logInfo("获得总价为:" + gettotalprice);
				Log.logInfo("获得邮费价格为:" + getOrdinarymail);

				boolean Ordinarymailprice = df.format(Double.parseDouble((itemList[2]))).equals(getOrdinarymail);// 标准邮价格一致
				boolean gettotal = df.format(itemSum).equals(gettotalprice); // 总价是否一致
				if (baseURL.contains("/es/")) {
					/* 多语言下价格要用美元的原价乘燚汇率 */
					gettotal = df.format(itemSum * rate).equals(gettotalprice); // 总价一致
					Ordinarymailprice = df.format(Double.parseDouble(itemList[2]) * rate).equals(getOrdinarymail);// 邮费价格正确
				}

				if (Ordinarymailprice && gettotal) {
					Log.logInfo("物流价格正确，计算的价格和结算一致,验证通过");
				} else {
					Log.logError("物流价格正确，计算的价格和结算不一致,验证失败，总价格:" + gettotal + "运输方式：" + Ordinarymailprice);
				}
				setCancelWall();
				op.loopClickElement("paymentlistPayPal", 3, 3, 20);// 选择使用PP支付
				op.loopClickElement("placeYourOrder", 3, 80, explicitWaitTimeoutLoop);// 点击支付进入支付页面
				//ControlUsingMethod.switchingWindow(driver, mianHandles);
				if (enterPaymentPage() == true) {
					Log.logInfo("成功进入到支付页面");
				} else {
					Log.logError("进入到支付页面失败");
				}
				//ControlUsingMethod.switchingCloseWindow(driver, mianHandles);
				if (baseURL.contains("/es/")) {
					transportationState("Envío Rápido", gettotalprice);
				} else {
					transportationState("Expedited Shipping", gettotalprice);
				}
			} else {
				Log.logInfo("快递运输方式后台已经设置，目前获取的商品没有这个方式");
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("getOrdinarymail=" + getOrdinarymail);
			Assert.fail();
		}
	}
	/**
	 * 
	* @测试点: 计算保险费
	* @验证点: 1.订单费用正确（对应的运费正确）2订单列表，价格总价和保险费显示正确
	* @使用环境：     测试环境
	* @备注： void    
	* @author zhangjun 
	* @date 2016年12月19日
	 */
	@Test
	public void Shippinginsurance(){
		;
		String Settlementprice=null;	
		try {	
			getGoodsSettlement();
			//Page.pause(3);
			//String gettotal=df.format(Double.parseDouble(goods[1])*0.02+1);//计算的邮费
			String[] itemList = getItemList();// 获取支付时的内容}

			Double itemSum = Double.parseDouble(itemList[0]) + Double.parseDouble(itemList[2])
					+ Double.parseDouble(itemList[4]);
			Log.logInfo("获取下单页面商品的运输价为：" + itemList[2]);
			Log.logInfo("获取下单页面商品的保险费为：" + itemList[4]);
			Log.logInfo("获取下单页面追踪码为：" + itemList[3]);

			String gettotalprice = op.loopGetElementText("PriceTotal", 10, 30).replace("$", "").replace("€", "");// 获得总价
			Log.logInfo("页面获取的订单总价:" + gettotalprice);
			if (baseURL.contains("/es/")) {
				itemSum = itemSum * rate;
			}
			if (df.format(itemSum).equals(gettotalprice)) {
				Log.logInfo("物流价格正确，计算的价格和结算时一致,验证通过");
			} else {
				Log.logError("计算的价格和结算时不一致,验证失败,计算的结果：" + itemSum + "实际支付的价格：" + gettotalprice);
			}
			op.loopClickElement("paymentlistPayPal", 3, 3, 20);// 选择使用PP支付
			op.loopClickElement("placeYourOrder", 3, 30, explicitWaitTimeoutLoop);// 点击支付进入支付页面
			//ControlUsingMethod.switchingWindow(driver, mianHandles);
			if(enterPaymentPage()==true) {
				Log.logInfo("成功进入到支付页面");
			} else {
				Log.logError("进入到支付页面失败");
			}
			if (baseURL.contains("/es/")) {
				transportationState("Rango de Envío Promedio", gettotalprice);// 进入到我订单页面,进行价格和订单状态的对比
			} else {
				transportationState("Flat Rate Shipping", gettotalprice);// 进入到我订单页面,进行价格和订单状态的对比
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("Settlementprice=" + Settlementprice);
			Assert.fail();
		}
		
	}

	/**
	 * 
	 * @测试点: 快递不选择保险费
	 * @验证点: 1.订单费用正确（对应的运费正确）2.订单列表，价格显示正确 @使用环境： 测试环境
	 * @备注：需要先执行Ordinarymail_trackingcode
	 * @author zhangjun
	 * @date 2016年12月12日
	 */
	@Test
	public void Shippingdontinsurance() {
		;
		String gettotalprice=null;
		try {
			getGoodsSettlement();//必要
			op.loopClickElement("Insurancebtn", 80, 10, explicitWaitTimeout);
			String[] itemList = getItemList();
			Double itemSum = Double.parseDouble(itemList[0])+ Double.parseDouble(itemList[2]);		
			Log.logInfo("获取下单页面商品的运输价为："+itemList[2]);
			Log.logInfo("计算的总价：" + itemSum);// 计算的总价
			
			gettotalprice = op.loopGetElementText("PriceTotal", 10, 30).replace("$", "").replace("€", "");// 获得总价
			boolean gettotal = df.format(itemSum).equals(gettotalprice); // 总价是否一致
			
			if (baseURL.contains("/es/")) {
				gettotal = df.format(itemSum * rate).equals(gettotalprice); 
			}
			
			if (gettotal) {
				Log.logInfo("物流价格正确，计算的价格和结算时一致,验证通过");
			} else {
				Log.logError("计算的价格和结算时不一致,验证失败,计算的总价:"+itemSum+"实际总价："+gettotalprice);
			}
			op.loopClickElement("paymentlistPayPal", 3, 3, 20);// 选择使用PP支付
			op.loopClickElement("placeYourOrder", 3, 30, explicitWaitTimeout);// 点击支付进入支付页面	
			
			
			//ControlUsingMethod.switchingWindow(driver, mianHandles);		
			if(enterPaymentPage()==true){
				Log.logInfo("成功进入到支付页面");
			}else {
				Log.logError("进入到支付页面失败");
			}							
			//ControlUsingMethod.switchingCloseWindow(driver, mianHandles);
			if(baseURL.contains("/es/")){
				transportationState("Rango de Envío Promedio",gettotalprice);//进入到我订单页面,进行价格和订单状态的对比
			}else{
				transportationState("Flat Rate Shipping",gettotalprice);
			}
			Pub.printTestCaseInfo(info);
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("gettotalprice=" + gettotalprice);
			Assert.fail();
		}
	}
	
	/**
	 * 测试项：pp支付成功验证 验证点：1.支付成功，跳转成功页面正确,2.成功提示的数量、金额与支付一致,3.订单中数量、金额、订单编号及付款状态正确
	 * 适用环境：所有环境 备注：验证PP支付成功金额，数量，及金额是否正确
	 * 
	 * 
	 */
	
	@Test
	public void pay_pp() {
		;
		try {
			interfaceMethod.crearAllCartGoods(myPublicUrl,loginName);
			
			String[] goods = goodsPage();// 打开商品页面并获取商品信
			op.loopClickElement("addToBag", 80, 10, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
			op.loopClickElement("proceedToCheckout", 20, 10, explicitWaitTimeoutLoop);// 点击checkout，进入结算页面
			String[] placeadder = orderadder();// 得到地址信息
			itemList = getItemList();// 获取支付时的内容			
			setCancelWall();//是否存在电子钱包支付方式
			op.loopClickElement("paymentlistPayPal", 3, 3, explicitWaitTimeoutLoop);// 选择使用PP支付
			Log.logInfo("选择的是PP支付方式");
			// 商品页的原价和下单时的原价一致
			boolean Ordinarymail = df.format(Double.parseDouble(goods[1])).equals(df.format(Double.parseDouble(itemList[0]))); //
			Log.logInfo("获取支付页面商品的单间原价为："+goods[1]);
			Log.logInfo("获取支付页面商品的运输价为："+itemList[2]);
			Log.logInfo("获取支付页面商品的追踪码价为："+itemList[3]);
			Log.logInfo("获取支付页面商品的保险费为："+itemList[4]);
			
			Double itemSum = Double.parseDouble(itemList[0])+ Double.parseDouble(itemList[2])
					+ Double.parseDouble(itemList[4]);// 计算的总价
			Log.logInfo("自己计算的总价为:"+itemSum);
			
			double total = Double.parseDouble(itemList[5]); // 页面上的总价
			Log.logInfo("页面显示的原价为:"+itemList[5]);
			
			boolean gettotal = df.format(Double.parseDouble(itemList[5])).equals(df.format(itemSum));		
			if(baseURL.contains("/es/")){
				Log.logInfo("多语言环境不对比提交的价格");
			}else{
				if (!gettotal && Ordinarymail) {// 计算的总价和界面总价是否一致
					Log.logError("支付页面商品总价与界面的商品总价不一致,计算的值:" + itemSum + ",界面值：" + itemList[5]);
				}else{
					Log.logInfo("支付页面商品总价与界面的商品总价一致，且单价与商品页面单价一致，验证通过");
				}
			}
						
			op.loopClickElement("placeYourOrder", 3, 30, explicitWaitTimeoutLoop);// 点击支付进入支付页面
			IsExitsshiiping();//是否会弹出选择运输方式的弹框
			Log.logInfo("点击提交按钮，进入到支付页面");	
	
			String[] ordercode = paypp(total);// PP登录并进行支付后页面的核对,并返回订单号
             
			/* 订单页面的处理	*/	 
			String[] orderadder = openOrderTail(ordercode[0], ordercode[1], ordercode[2]);
			if (!baseURL.contains("/es/")) {
				for (int i2 = 0; i2 < placeadder.length; i2++) {
					if (placeadder[i2].equals(orderadder[i2])) {
						Log.logInfo("下单的地址，价格和支付方式与订单的地址价格支付方式一致！，验证通过");
					} else {
						Log.logError(" 下单的地址，价格和支付方式与订单的地址价格支付方式不一致！,支付前的地址显示" + placeadder[i2] + "订单中的地址" + orderadder[i2]);
					}

				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("支付地址不一致");
			Assert.fail();
		}

		}
	

	/**
	 * 
	 * @测试点: 普通支付，已登录，无地址
	 * @验证点: 1.新增地址成功2.跳转到结账地址 @使用环境： 测试环境 @备注： payment_notlogged需要优先执行
	 * @author zhangjun
	 * @date 2016年12月12日
	 */
	
	public void addaddress() {
		try {
			op.loopSendKeys("Shippingfirstname", "zhang", 3, 100);
			op.loopSendKeys("Shippinglastname", "zhang", 3, 100);
			op.loopSendKeys("Shippingaddressline1", "shanghai", 3, 100);
			op.loopSendKeys("Shippingaddressline2", "shenzhen", 3, 100);	
			op.getSelect("ShippingCountryRegion", 10).selectByVisibleText("France");
			op.getSelect("Shippingprovince", 10).selectByIndex(1);	//#6
			op.loopSendKeys("Shippingcity", "shanghai", 3, 100);
			op.loopSendKeys("Shippingtel", "3383276", 3, 100);
			op.loopSendKeys("Shippingzipcode", "547506", 3, 100);
			op.loopClickElement("Shippingplaceorder", 3, 100, 100);
		} catch (Exception e) {
			e.printStackTrace();			
		}
	}

	/**
	 * 测试项：商品总金额<300 验证点：支付方式显示有paypal，信用卡，worlpay 适用环境： 内外网 备注：无
	 * 
	 */
	
	@Test
	public void pay_less_300() {
		interfaceMethod.crearAllCartGoods(myPublicUrl,loginName);// 清空购物车
		goodTestUrl = interfaceMethod.getGoodUrl(myPublicUrl,"max");
		try {
			if(baseURL.contains("/es/")){
				interfaceMethod.convertAddress(op, goodTestUrl);
			}else{
				op.loopGet(goodTestUrl, 80, 2, 60);
			}
			op.loopClickElement("addToBag", 80, 10, 100);// 点击添加至购物车按钮
			Page.pause(3);
			setGoodsAccount(150);// 根据商品总价设置商品的数量
			op.loopClickElement("proceedToCheckout", 20, 10, explicitWaitTimeoutLoop);// 点击checkout，进入结算页面	
			//获取的支付方式
			Boolean getPayPal = op.isElementPresent("paymentlistPayPal");
			Boolean getCreditCard = op.isElementPresent("paymentlistCreditCard");
	
			if (getPayPal && getCreditCard) {
				Log.logInfo("支付方式paypal，信用卡都显示了，验证通过");
			} else {
				Log.logError("支付方式paypal，信用卡，显示不全,验证不通过");
			}
		} catch (Exception e) {

			e.printStackTrace();
			Log.logWarn("支付方式paypal，信用卡，显示不全,验证不通过");
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 300<=商品总金额<400
	 * @验证点: 支付方式显示有paypal，信用卡，西联，worlpay， @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月13日
	 */
	
	@Test
	public void pay_between300_400() {
		;
		interfaceMethod.crearAllCartGoods(myPublicUrl,loginName);// 清空购物车
		goodTestUrl =interfaceMethod.getGoodUrl(myPublicUrl,"max");
		try {
			if(baseURL.contains("/es/")){
				interfaceMethod.convertAddress(op, goodTestUrl);
			}else{
				op.loopGet(goodTestUrl, 80, 2, 60);
			}
			op.loopClickElement("addToBag", 80, 10, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
			setGoodsAccount(350);// 根据商品总价设置商品的数量

			op.loopClickElement("proceedToCheckout", 20, 20, explicitWaitTimeoutLoop);// 点击checkout，进入结算页面	
			Boolean getPayPal = op.isElementPresent("paymentlistPayPal");
			Boolean getCreditCard = op.isElementPresent("paymentlistCreditCard");
			Boolean getWesternUnion = op.isElementPresent("paymentlistWesternUnion");
	
			if (getPayPal && getCreditCard && getWesternUnion) {
				Log.logInfo("支付方式paypal，信用卡，西联，支付方式都显示了，验证通过");
			} else {

				Log.logError("支付方式paypal，信用卡，西联显示不全,验证不通过");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("支付方式paypal，信用卡，西联显示不全,验证不通过");
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 400<商品总金额<500
	 * @验证点: 支付方式显示有paypal，信用卡, worlpay，西联,电汇（wire transfer）
	 * @使用环境： 测试环境 @备注：
	 *       void
	 * @author zhangjun
	 * @date 2016年12月13日
	 */
	@Test
	public void pay_between400_500() {
		;
		interfaceMethod.crearAllCartGoods(myPublicUrl,loginName);// 清空购物车
		goodTestUrl =interfaceMethod.getGoodUrl(myPublicUrl,"max");
		try {
			if(baseURL.contains("/es/")){
				interfaceMethod.convertAddress(op, goodTestUrl);
			}else{
				op.loopGet(goodTestUrl, 80, 2, 60);
			}
			op.loopClickElement("addToBag",80, 10, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
			setGoodsAccount(450);// 根据商品总价设置商品的数量
			op.loopClickElement("proceedToCheckout", 40, 10, explicitWaitTimeoutLoop);// 点击checkout，进入结算页面
	
			Boolean getPayPal = op.isElementPresent("paymentlistPayPal");
			Boolean getCreditCard = op.isElementPresent("paymentlistCreditCard");	
			Boolean getWesternUnion = op.isElementPresent("paymentlistWesternUnion");
			if (getPayPal && getCreditCard && getWesternUnion ) {
				Log.logInfo("支付方式paypal，信用卡, 西联,都显示了,验证通过");
			} else {
				Log.logError("支付方式paypal，信用卡，西联显示不全，验证不通过");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("支付方显示不全,验证不通过");
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 500<=商品总金额<3000
	 * @验证点: 支付方式显示有paypal，信用卡, worlpay，西联,电汇（wire transfer） @使用环境： 测试环境 @备注：
	 *       void
	 * @author zhangjun
	 * @date 2016年12月13日
	 */
	
	@Test
	public void pay_between500_3000() {
		;
		interfaceMethod.crearAllCartGoods(myPublicUrl,loginName);// 清空购物车
		goodTestUrl = interfaceMethod.getGoodUrl(myPublicUrl,"max");
		try {
			if(baseURL.contains("/es/")){
				interfaceMethod.convertAddress(op, goodTestUrl);
			}else{
				op.loopGet(goodTestUrl, 80, 2, 60);
			}
			op.loopClickElement("addToBag", 90, 10, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
			setGoodsAccount(850);// 根据商品总价设置商品的数量
			op.loopClickElement("proceedToCheckout", 20, 10, explicitWaitTimeoutLoop);// 点击checkout，进入结算页面
	
			Boolean getPayPal = op.isElementPresent("paymentlistPayPal");
			Boolean getCreditCard = op.isElementPresent("paymentlistCreditCard");
			Boolean getWesternUnion = op.isElementPresent("paymentlistWesternUnion");
			Boolean getWiredTransfer = op.isElementPresent("paymentlistWiredTransfer");
	
			if (getPayPal && getCreditCard && getWesternUnion && getWiredTransfer) {
				Log.logInfo("支付方式paypal，信用卡, worlpay，西联,电汇（wire transfer都显示了");
			} else {

				Log.logError("支付方式paypal，信用卡, worlpay，西联,电汇（wire transfer显示不全");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("支付方显示不全,验证不通过");
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: pay_greater_3000
	 * @验证点: 西联，电汇， @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月13日
	 */
	@Test
	public void pay_greater_3000() {
		;
		interfaceMethod.crearAllCartGoods(myPublicUrl,loginName);// 清空购物车
		goodTestUrl = interfaceMethod.getGoodUrl(myPublicUrl,"max");	
		try {
			if(baseURL.contains("/es/")){
				interfaceMethod.convertAddress(op, goodTestUrl);
			}else{
				op.loopGet(goodTestUrl, 80, 2, 60);
			}
			op.loopClickElement("addToBag", 40, 10, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
			setGoodsAccount(3200);// 根据商品总价设置商品的数量
		
			
			String total=op.loopGetElementText("shoopingpay", 20, explicitWaitTimeoutLoop).replace("$", "").replace("€", "");
			if(Double.parseDouble(total)>1000){
			op.loopClickElement("proceedToCheckout", 1, 1, explicitWaitTimeoutLoop);// 点击checkout，进入结算页面
			Boolean getWesternUnion = op.isElementPresent("paymentlistWesternUnion");
			Boolean getWiredTransfer = op.isElementPresent("paymentlistWiredTransfer");
			if (getWesternUnion && getWiredTransfer) {	
				Log.logInfo("支付方式西联，电汇都显示了，验证通过");
			} else {
				Log.logError("支付方式西联，电汇，显示错误，验证不通过");
			}
			}else{
				Log.logInfo("取出的商品数据库存不够，总和价格没有大于3000的");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("支付方显示不全,验证不通过");
			Assert.fail();
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
	public void quick_payment() {	
		
		//String getcode;
		boolean orderStute = false;
		boolean orderDate = false;
		String getURL;
		try {
			interfaceMethod.crearAllCartGoods(myPublicUrl,loginName);			
			goodsPage();// 打开商品页面并获取商品信息
			op.loopClickElement("addToBag", 4, 20, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
			setCancelWall();//存在电子钱包，取消
			op.loopClickElement("quickpaybtn", 4, 20, explicitWaitTimeoutLoop);// 点击PP支付按钮
			Page.pause(10);
	
			if (driver.getWindowHandles().size() > 1) {
				ControlUsingMethod.switchingWindow(driver, mianHandles);
				getURL=driver.getCurrentUrl();	
			} 
			getURL=driver.getCurrentUrl();
			
		    boolean  payUrl=getURL.contains(".paypal.");
			if(driver.getWindowHandles().size()==1){//窗口句柄
				driver.switchTo().window(mianHandles);//切换到主窗体，现在由2个窗口变为了一个窗口
			}else{
				ControlUsingMethod.switchingCloseWindow(driver, mianHandles);
			}
		    
		    Assert.assertTrue(payUrl);
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("总价和订单状态、以及支付方式和选择的不一样，验证不通过,orderStute"+orderStute+"orderDate"+orderDate);
			Assert.fail();
		}
	}

	/**
	 * 以下内容是支付合并,以下的支付也不要了
	* @测试点: 未登录时的快速支付流程 
	* @验证点:1.付款成功，订单费用正确（对应的运费正确）2.显示正确支付状态3.感谢购物语句正确4.登录pp账户，账户正确
	* @使用环境：     测试环境
	* @备注： void    
	* @author zhangjun 
	* @date 2016年12月15日
	* 更新说明(2016-12-29)：修改登录与登出的确认方式
	 *//*
	
	@Test
	public void quick_paymentnotlogin(){
		;
		Pub.getTestCaseInfo(testCasemap, GetMethodName(), info, true);
		op.retryRefresh(info.lastMethodName, info.methodName);
		boolean orderStute = false;
		boolean orderDate = false;

		try {
			String getName = op.loopGetElementText("signlink", 3, 20);
			boolean loginState = getName.contains("autot");// 区分测试与正式环境
			boolean loginStateFormal = getName.contains("shopp");// 主要是区分登录与登出
			boolean loginstateTest = getName.contains("44181");// 主要是区分登录与登出
			if (loginState || loginStateFormal || loginstateTest) {// 为真则已登录
				Signout_Success();// 登出
			} else {
				Log.logInfo("已经退出了登录，正常执行下一步");
			}
			String[] goods = goodsPage();// 打开商品页面并获取商品信息
			String getcode;
			op.loopClickElement("addToBag", 4, 20, 100);// 点击添加至购物车按钮

			setCancelWall();
			op.loopClickElement("quickpaybtn", 4, 10, 10);// 点击PP支付按钮
			//String[] ordercode = paypp(Double.parseDouble(goods[1]));// PP登录并进行支付后页面的核对,并返回订单号
			// 返回订单号，和价格，以及支付方式
			op.loopGet(myOrderUrl, 90, 3, 80);// 转向我的订单
			String getemailname = op.loopGetElementText("ppacunont", 5, explicitWaitTimeoutLoop);
			if (getemailname.equals(ppaccount)) {
				Log.logInfo("自动登录PP账户成功，验证通过！！！！");
			} else {
				Log.logError("自动登录的不是PP账户，验证不通过！！！！,实际登录的用户为:" + getemailname);
			}
			Page.pause(2);
			List<WebElement> rows = op.getElements("ordersNumber");// 获取列表行数，统计列表有多少个产品
			Log.logInfo(rows.size());
			for (int i = 1; i < rows.size(); i++) {
				getcode = rows.get(i).findElement(By.cssSelector("td:nth-child(1)> a")).getText();
				// 取得支付成功的那订单
				if (getcode.equals(ordercode[0])) {
					rows.get(i).findElement(By.cssSelector("td:nth-child(1)> a")).click();
					Log.logInfo("查找到对应的订单号，验证通过！！！！");
					Log.logInfo("订单编号为:" + getcode);
					break;
				} else {
					Log.logError("没有查找到对应的订单号，验证不通过！！！！");
				}
				Log.logInfo("getcode:" + getcode);
			}
			 订单详情页面，对比总价和订单的状态quick_payment 

			String getorderStute = op.loopGetElementText("orderStute", 3, explicitWaitTimeoutLoop);// 订单状态
			String getorderDate = op.loopGetElementText("orderPrice", 3, explicitWaitTimeoutLoop);// 订单总价
			String getorderPayment = op.loopGetElementText("orderpayment", 3, explicitWaitTimeoutLoop); // 支付方式
			orderStute = getorderStute.equals("Paid")||getorderStute.equals("Pagado");
			orderDate = df.format(Double.parseDouble(getorderDate)).equals(df.format(Double.parseDouble(ordercode[1])));

			if (baseURL.contains("/es/")) {
				if (orderStute && getorderPayment.contains("PayPal")) {
					Log.logInfo("多语言总价和订单状态、以及支付方式和选择一样，验证通过");
				} else {
					Log.logError("多语言总价和订单状态、以及支付方式和选择的不一样，验证不通过,orderDate:" + orderDate + "orderStute：" + orderStute);
				}

			} else {
				if (orderStute && orderDate && getorderPayment.contains("PayPal")) {
					Log.logInfo("总价和订单状态、以及支付方式和选择一样，验证通过");
				} else {
					Log.logError("总价和订单状态、以及支付方式和选择的不一样，验证不通过");
				}
			}
		
			Pub.printTestCaseInfo(info);
		} catch (Exception e) {
			e.printStackTrace();
			Pub.printTestCaseExceptionInfo(info);
			Log.logWarn("总价和订单状态、以及支付方式和选择的不一样，验证不通过,orderStute" + orderStute + "orderDate" + orderDate);
			Assert.fail();
		}
	}*/
	
	

	/**
	 * 
	* @测试点: 信用卡支付
	* @验证点: 转到相应的支付页面，提交的总价正确
	* @使用环境：     测试环境
	* @备注： void    
	* @author zhangjun 
	* @date 2016年12月15日
	 */
	
	@Test
	public void  pay_credit(){
		String gettotal = null;
		interfaceMethod.crearAllCartGoods(myPublicUrl,loginName);
		try {
			goodsPage();// 打开商品页面并获取商品信
			op.loopClickElement("addToBag", 60, expliceTimeout, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
			op.loopClickElement("proceedToCheckout", 4, expliceTimeout, explicitWaitTimeoutLoop);// 去结算

	        op.loopClickElement("paymentWebcollect", 10, expliceTimeout, explicitWaitTimeoutLoop); 
	        Log.logInfo("选择信用卡支付方式成功");
	        String[] itemList = getItemList();//获取支付时的内容
	        op.loopClickElement("placeYourOrder", 3, expliceTimeout, explicitWaitTimeoutLoop);	        
	        gettotal=op.loopGetElementText("cardpay", 5, explicitWaitTimeoutLoop).replace("total:", "").replace("USD", "").replace("EUR", "").trim();
	        
	       // 获得订单总价;
			Log.logInfo("*****获取的页面物价总计：" + gettotal);
			if(baseURL.contains("/es/")){
				 if(op.isElementPresent("placeYourOrder")){
			        	Log.logInfo("进入到多语言信用卡支付页成功，验证通过");
			        }else{
			        	Log.logError("进入到信用卡支付页失败，请检查");
			        }
			}else{
				 Boolean toatalconsistent=df.format(Double.parseDouble(itemList[5])).equals(df.format(Double.parseDouble(gettotal)));
			       if(op.isElementPresent("placeYourOrder")&&toatalconsistent){
			        	Log.logInfo("进入到信用卡支付页成功，且付款的总金额与商品页面相等，验证通过");
			        }else{
			        	Log.logError("进入到信用卡支付页成功，且付款的总金额与商品页面不相等，验证失败，获取的商品价格为:"+gettotal+"页面计算的价格为:"+itemList[5]);
			        }
			}
	       
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("gettotal订单总价" + gettotal);
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
			
	
	}

	/**
	 * 
	 * @测试点: 西联支付
	 * @验证点:1.下单成功 2.下单页面的总价与订单的总价相同 3.未支付状态正常 4.订单列表中收货地址相同 
	 * @使用环境： 测试环境
	 *  @备注： void
	 * @author zhangjun
	 * @date 2016年12月15日
	 */
	@Test
	public void pay_westernUnion(){
		;
		boolean orderstate = false;
		boolean orderprice = false;
		interfaceMethod.crearAllCartGoods(myPublicUrl,loginName);		
		try {
			
			if(baseURL.contains("/es/")){
				interfaceMethod.convertAddress(op, goodTestUrl);
			}else{
				op.loopGet(goodTestUrl, 40, 3, 60);//打开商品
			}
			
			op.loopClickElement("addToBag", 4, 10, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
		    setGoodsAccount(350);// 根据商品总价设置数量
		   
		    op.loopClickElement("proceedToCheckout", 4, 10, explicitWaitTimeoutLoop);// 去结算
		    String[] itemList = getItemList();//获取支付时的内容
		    setCancelWall();
		    IsExitsshiiping();//是否选择了物流方式
	        op.loopClickElement("paymentlistWesternUnion", 10, 10, explicitWaitTimeoutLoop);//西联支付方式
	        Log.logInfo("选择西联支付方式成功");
	        op.loopClickElement("placeYourOrder", 3, 20, explicitWaitTimeoutLoop);//跳转
	        String getordercode=op.loopGetElementText("OrderGenerated", 20, explicitWaitTimeoutLoop);//生成订单号
	        
	        
	        op.loopGet(myOrderUrl, 40,3, 60);// 转向我的订单
			Page.pause(3);
			List<WebElement> rows = op.getElements("ordersNumber");// 获取列表行数，统计列表有多少个产品
			Log.logInfo(rows.size());
			for (int i = 1; i < rows.size(); i++) {
				String getcode = rows.get(i).findElement(By.cssSelector("td:nth-child(1)> a")).getText();
				// 取得支付成功的那订单
				if (getcode.equals(getordercode)) {
					rows.get(i).findElement(By.cssSelector("td:nth-child(1)> a")).click();
					Log.logInfo("查找到对应的订单号！！！！");
					Log.logInfo("订单编号为:"+getordercode);
					break;
				} else {
					Log.logError("没有查找到对应的订单号！！！！");
				}
				Log.logInfo("getcode:" + getcode);
			}
			/**
			 * 订单详情页面，对比总价和订单的状态
			 */
			// 概览信息 订单编号
			String getorderStute = op.loopGetElementText("orderStute", 3, explicitWaitTimeoutLoop);// 订单状态
			String getorderDate =op.loopGetElementText("orderPrice", 3, 20);// 订单总价	
			orderstate = getorderStute.equals("Waiting for payment")||getorderStute.equals("Por pagar");//Por pagar西班牙语的待支付
			orderprice = df.format(Double.parseDouble(getorderDate)).equals(df.format(Double.parseDouble(itemList[5])));
			Log.logInfo("orderDate:" + orderprice);
			Log.logInfo("orderStute:" + orderstate);
			if(baseURL.contains("/es/")){
				if (orderstate) {
					Log.logInfo("订单状态正确，验证通过");
				} else {
					Log.logError("订单状态不正确，验证不通过");
				}
			}else{
				if (orderstate && orderprice) {
					Log.logInfo("总价和订单状态正确，验证通过");
				} else {
					Log.logError("总价和订单状态不正确，验证不通过");
				}
			}		
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("总价和订单状态不正确orderstate:" + orderstate+"orderprice"+orderprice);
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
		
	}

	/**
	 * 
	* @测试点: WT支付
	* @验证点: 1.下单成功 2.下单页面的总价与订单的总价相同 3.未支付状态正常 4.收货地址相同 
	* @使用环境：     测试环境
	* @备注： void    
	* @author zhangjun 
	* @date 2016年12月15日
	 */
	@Test
	public void pay_wt(){
		;
		boolean orderstate = false;
		boolean orderprice = false;
		interfaceMethod.crearAllCartGoods(myPublicUrl,loginName);		
		try {
			if(baseURL.contains("/es/")){
				interfaceMethod.convertAddress(op, goodTestUrl);
			}else{
				op.loopGet(goodTestUrl, 40, 3, 60);//打开商品
			}
			op.loopClickElement("addToBag", 4, 10, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
		    setGoodsAccount(600);// 根据商品总价设置数量		    
		    
		    op.loopClickElement("proceedToCheckout", 20, 30, explicitWaitTimeoutLoop);// 去结算
		    String[] itemList = getItemList();//获取支付时的内容	
		    setCancelWall();
		    IsExitsshiiping();
	        op.loopClickElement("paymentlistWiredTransfer", 2, 2, explicitWaitTimeoutLoop);//电汇支付方式
	        Log.logInfo("选择电汇方式支付方式成功");
	        op.loopClickElement("placeYourOrder", 3, 10, explicitWaitTimeoutLoop);//跳转
	       Page.pause(1);
	       String getordercode=op.loopGetElementText("OrderGenerated", 20, explicitWaitTimeoutLoop);//生成订单号
	       op.loopGet(myOrderUrl, 60, 3, 60);// 转向我的订单
			Page.pause(1);
			List<WebElement> rows = op.getElements("ordersNumber");// 获取列表行数，统计列表有多少个产品
			Log.logInfo(rows.size());
			for (int i = 1; i < rows.size(); i++) {
				String getcode = rows.get(i).findElement(By.cssSelector("td:nth-child(1)> a")).getText();
				// 取得支付成功的那订单
				if (getcode.equals(getordercode)) {
					rows.get(i).findElement(By.cssSelector("td:nth-child(1)> a")).click();
					Log.logInfo("查找到对应的订单号！！！！");
					Log.logInfo("订单编号为:"+getordercode);
					break;
				} else {
					Log.logError("没有查找到对应的订单号！！！！");
				}
				Log.logInfo("getcode:" + getcode);
			}
			/**
			 * 订单详情页面，对比总价和订单的状态
			 */
			// 概览信息 订单编号
			String getorderStute = op.loopGetElementText("orderStute", 3, 300);// 订单状态
			String getorderDate = op.loopGetElementText("orderPrice",  3, 20);// 订单总价
	
			orderstate = getorderStute.equals("Waiting for payment")||getorderStute.equals("Por pagar");//Por pagar西班牙语的待支付;
			orderprice = df.format(Double.parseDouble(getorderDate)).equals(df.format(Double.parseDouble(itemList[5])));
			Log.logInfo("orderDate:" + orderprice);
			Log.logInfo("orderStute:" + orderstate);
			
			if(baseURL.contains("/es/")){
				if (orderstate) {
					Log.logInfo("订单状态正确，验证通过");
				} else {
					Log.logError("订单状态不正确，验证不通过");
				}
			}else{
				if (orderstate && orderprice) {
					Log.logInfo("总价和订单状态正确，验证通过");
				} else {
					Log.logError("总价和订单状态不正确，验证不通过");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("总价和订单状态不正确orderstate:" + orderstate+"orderprice"+orderprice);
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
		
	}

	/**
	 * 
	* @测试点: 巴西支付 
	* @验证点: 1.下单成功 2.下单页面的总价与订单的总价相同 3.未支付状态正常 4.收货地址相同 
	* @使用环境：     测试环境
	* @备注： void    
	* @author zhangjun 
	* @date 2016年12月15日
	 */
	@Test
	public void pay_Brazil(){
		;
		String getMethodpayment="";
		interfaceMethod.crearAllCartGoods(myPublicUrl,loginName);		
		try {			
			//Page.pause(3);
			if(baseURL.contains("/es/")){
				interfaceMethod.convertAddress(op, goodTestUrl);
			}else{
				op.loopGet(goodTestUrl, 40, 4, 60);//打开商品
				Log.logInfo("商品地址为："+goodTestUrl);
			}
			
			op.loopClickElement("addToBag", 4, 20, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
		    op.loopClickElement("proceedToCheckout", 4, 20, explicitWaitTimeoutLoop);// 去结算
		  //改变地址，选择巴西地址
		    op.loopClickElement("address_btn",4,20,explicitWaitTimeoutLoop);
		    op.loopClickElement("address_select",4,20,explicitWaitTimeoutLoop);//点击编辑
		    Log.logInfo("4月13日地址选择方式发生了改变");
		   Page.pause(3);//内容加载有些慢
		    
		    String selectedSale=new Select(driver.findElement(By.name("country"))).getFirstSelectedOption().getText();
		    if (!selectedSale.equals("Brazil")){
		    	 op.getSelect("ShippingCountryRegion", 10).selectByVisibleText("Brazil");//需要判断是否是巴西e支付
		    	 Page.pause(2);
				 op.getSelect("Shippingprovince", 10).selectByIndex(1);
				 Log.logInfo("设置巴西地址成功");
		    }
		    Log.logInfo("目前已经是巴西地址了");
		    Page.pause(1);
		    op.loopClickElement("changeaddressOk",3,10,explicitWaitTimeoutLoop);
		    
		    /**支付页面*/ 
		    setCancelWall();
		    IsExitsshiiping();
	        op.loopClickElement("paymentlistboletoBancario", 10, 10, explicitWaitTimeoutLoop);//巴西支付方式
	        op.loopClickElement("placeYourOrder", 3, 20, explicitWaitTimeoutLoop);//跳转
	        
	        Page.pause(5);
	     //   String   Brazil="https://checkout.ebanx.com/checkout/payment/method";
	        String geturl = driver.getCurrentUrl();
	       if(geturl.contains("ebanx")){
	        	Log.logInfo("进入到巴西支付页成功验证通过");
	        }else{
	        	Log.logError("进入到巴西支付页不成功，验证失败，实际进入的地址为："+geturl);
	        }  	       
	       op.loopGet(myOrderUrl, 60, 3, 60);// 转向我的订单
	       String getorderstat = op.loopGetElementText("orderstate", 3, explicitWaitTimeoutLoop);
			if (getorderstat.equals("Waiting for payment")||getorderstat.equals("Por pagar")) {
				Log.logInfo("订单状态正确，为：Waiting for payment巴西语言为Por pagar");	
		       String getordercode=op.loopGetElementText("ordercode", 3, explicitWaitTimeoutLoop);
				Log.logInfo("订单编号为:"+getordercode);
				Page.pause(3);
				op.loopClickElement("OrderNo", 3, 20, explicitWaitTimeoutLoop);
				Page.pause(3);
				getMethodpayment=op.loopGetElementText("payment", 3, explicitWaitTimeoutLoop);//支付方式d
				if (getMethodpayment.equals("boleto Bancario")){
					Log.logInfo("支付方式正确，验证通过");
				}else{
					Log.logError("支付方式不正确，页面显示为：" + getMethodpayment + "实际为Credit card via PayPal");
				}

			} else {
				Log.logError("订单状态不正确，页面显示为：" + getorderstat + "实际为Waiting for payment");
			}
			
			} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("订单状态不正确:" + getMethodpayment);
			Log.logWarn(info.testCorrelation);
			Assert.fail();
			
		}
		
	}
	
	/**
	 * 
	 * @测试点: 钱包+PP支付
	 * @验证点: 1.下单成功 2.下单页面的总价与订单的总价相同 3.未支付状态正常 4.收货地址相同 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月21日
	 */
	@Test
	public void pay_blendPay() {
		;
		interfaceMethod.crearAllCartGoods(myPublicUrl,loginName);
		try {		
			if(baseURL.contains("/es/")){
				interfaceMethod.convertAddress(op, goodTestUrl);
			}else{
				op.loopGet(goodTestUrl, 40, 4, 60);//打开商品
			}	
			op.loopClickElement("addToBag", 4, 10, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
			op.loopClickElement("proceedToCheckout", 4, 10, explicitWaitTimeoutLoop);// 去结算
			
			if (op.isElementPresent("wallet_pass")) {// 电子钱包
				op.loopSendKeysClean("wallet_pass", "jun123", 20);// 密码输入
				String[] itemList = getItemList();// 获取支付时的内容

				// 获取电子钱包金额
				double myWalletMoney = Double.parseDouble(op.loopGetElementText("js_myWallet", 3, explicitWaitTimeoutLoop).replace("$", "").replace("€", ""));//
				Log.logInfo("钱包的金额为:" + myWalletMoney);
				double payMoney = Double.parseDouble(op.loopGetElementText("paymentCardcont", 10, 20).replace("$", "").replace("€", ""));
				Log.logInfo("支付金额为:" + payMoney);
				if (myWalletMoney < Double.parseDouble(itemList[5])) {
					// 电子钱包的支付金额不足，和PP一起使用
					op.loopClickElement("paymentlistPayPal", 3, 3, 20);// 选择使用PP支付
					op.loopClickElement("placeYourOrder", 3, 20, explicitWaitTimeoutLoop);// 点击付款
					//paypp(payMoney);
				} else {
					Log.logInfo("实际支付的金额为:" + payMoney);
					op.loopClickElement("placeYourOrder", 3, 20, explicitWaitTimeoutLoop);// 点击付款
					// Page.pause(4);
					op.loopClickElement("cardsuccess", 3, 5, explicitWaitTimeoutLoop);

					String getprice = op.loopGetElementText("cardprice", 10, explicitWaitTimeoutLoop);
					if (getprice.equals(itemList[5])) {
						Log.logInfo("订单与实际支付价格一致验证通过");
					} else {
						Log.logInfo("订单与实际支付价格不一致，验证失败，实际支付:" + itemList[5] + "实际支付:" + getprice);
					}
				}
			}else {
				Log.logInfo("不存在电子钱包使用栏，后面操作不执行");
			}			
	
		
		} catch (Exception e) {

			e.printStackTrace();
		
			Log.logWarn("价格和支付方式与订单的地址不一致");
			Assert.fail();
		}
	}

	/**
	 * 商品页面,打开商品，并获取商品的标题，价格，数量，sku信息
	 * 
	 */
	public String[] goodsPage() {

		String[] goods = new String[4];// 记录商品标题、价格、编码、数量
		try {
			if(baseURL.contains("/es/")){
				interfaceMethod.convertAddress(op, goodTestUrl);
			}else{
				op.loopGet(goodTestUrl,50, 3, 60);
				Log.logInfo("加载的页面地址为："+goodTestUrl);
			}
			goods[0] = op.loopGetElementText("goodsTittle", 3, explicitWaitTimeoutLoop);
			goods[1] =  op.loopGetElementText("unitPrice",3,10).replace("$", "").replace("€", ""); // 结算页面需要用到				
			goods[2] = op.loopGetElementText("goodsCode", 3, explicitWaitTimeoutLoop);
			goods[3] = op.loopGetElementValue("quantity", "value",  explicitWaitTimeoutLoop);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return goods;

	}

	/**
	 * 
	 * @测试点: deleteAdress
	 * @验证点: 删除地址 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @throws Exception 
	 * @date 2016年12月7日
	 */
	public void deleteAdress() throws Exception {

		List<WebElement> ement = op.getElements("delectcollection");
		for (int i = 0; i < ement.size(); i++) {
			//Page.pause(1);
			try {
				op.loopClickElement("delectcollection", 3, 30, 20);
				Page.pause(1);
				op.loopClickElement("ConfirmDeletion", 3, 30, 20);
				op.loopClickElement("Address_page", 3, 30, 20);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
	/*商品页面信息*/
	public String[] shoppingCartPage() {
		//Page.pause(1);
		String[] shoppingCart = new String[3];
		String goodsAcutalPrice;
		try {
			goodsAcutalPrice = op.loopGetElementValue("goodsAcutalPrice", "orgp", explicitWaitTimeoutLoop);
			String sumPrice = op.loopGetElementValue("CartTotal", "orgp", explicitWaitTimeoutLoop);
			String getaccount = op.loopGetElementText("Numberinputbox", 3, 30);
			shoppingCart[0] = getaccount;// 记录商品的数量
			shoppingCart[1] = goodsAcutalPrice;// 记录商品实际价格
			shoppingCart[2] = sumPrice;// 记录商品总价
			Log.logInfo("商品总价" + sumPrice);
			Page.pause(1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return shoppingCart;// 返回商品数量及总价信息
	}

	/**
	 * 
	 * @测试点: Signout_Success
	 * @验证点: 退出登录 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月12日
	 */

	public void Signout_Success() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String myjs = "document.querySelector(\"#header > div > div.user_box > ul\").style.display='block';";
		js.executeScript(myjs);
		Page.pause(2);
		try {
			if(baseURL.contains("/es/")){
				op.loopClickElement("logout_link_es", 3, 20, 50);
			}else{
				op.loopClickElement("logout_link", 3, 20, 50);
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * 根据商品的实际价格，及商品总价，设置商品的数量，当设置的数量商品总价大于等于sumPrice则设置成功
	 * 
	 * @param sumPrice
	 * 
	 */
	public void setGoodsAccount(double sumPrice) {
		int accout = 0;// 设置商品数量
		int timeout = 0;// 设置超时时间
		try {
			double prices = Double.parseDouble(op.loopGetElementText("CartTotal", 3, explicitWaitTimeoutLoop).replace("$", "").replace("€", ""));
			System.out.println("prices:" + prices);
			accout = (int) (sumPrice / prices) + 1;
			if (op.isElementPresent("numberinputbox")) {				
				op.loopSendKeysClean("numberinputbox", String.valueOf(accout), 120);
				Log.logInfo("设置的件数为:"+accout);
				Page.pause(3);//必须要
				//double sumMoney = 0.00;
				if (timeout > 15) {
					Log.logInfo("设置商品的数量超时，无法设置价格在预期的范围");
				}
				timeout++;
		
			} else {
				Log.logInfo("控件没有查找到");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @测试点: paypp
	 * @验证点: PP支付和支付会被共用，单独拿出来,登录PP完成下单后，对价格进行对比，对比是否一致 @使用环境： 测试环境 @备注： void
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
			Page.pause(10);
			if (driver.getWindowHandles().size() > 1) {
				ControlUsingMethod.switchingWindow(driver, mianHandles);
				Log.logInfo("新的句柄:"+driver.getWindowHandle());
			} else {				
				if (op.isElementPresent("iframesouterlayer", 10)) {
					WebElement ppcontinue = op.MyWebDriverWait2("iframesouterlayer", 5);
					driver.switchTo().frame(ppcontinue);
					WebElement ppcontinueiframe = op.MyWebDriverWait2("iframes", 10);
					Log.logInfo("切换到第二个窗口");
					driver.switchTo().frame(ppcontinueiframe);
				}else{
					Log.logInfo("是一个窗口内容");
				}
			}
			
			if (op.isElementPresent("paycardLogin", 10)) {
				Log.logInfo("登录时存在一个登录按钮");
				op.loopClickElement("paycardLogin", 10, 10, 40);
			}
			
			if(driver.getWindowHandles().size()==1){//窗口句柄
				driver.switchTo().window(mianHandles);//切换到主窗体，现在由2个窗口变为了一个窗口
			}
			if (op.isElementPresent("pppay",10)) {
				Log.logInfo("选择支付方式");
				op.loopClickElement("pppay", 3, 50, explicitWaitTimeoutLoop);
			}	
			
			
			
			/*判断是否在老的测试环境还是正式的测试环境*/
			if (op.isElementPresent("oldePPloginButton",10 )) {
				Log.logInfo("进入的是老页面支付");
				Log.logInfo("2017年1月23日正式环境与测试环境支付都同一个支付页面，现在进行兼容");
				if (!op.isElementPresent("oldePPloginButton",20)) {
					op.loopClickElement("ppPaymentaccount", 3, 50, explicitWaitTimeoutLoop);
					Log.logInfo("已经成功进入到了PP支付页面");
				}
				Log.logInfo("已经进入了PP支付页面！！！");								
				String gettotalplain = op.loopGetElementText("Ordersprice", 10, explicitWaitTimeoutLoop);
				Log.logInfo("gettotalplain:" + gettotalplain);
				if (gettotalplain.contains("总计")) {
					totalprice = op.loopGetElementText("Ordersprice", 10, explicitWaitTimeoutLoop).replace("总计$", "").replace("USD", "").replace("€", "").trim();// 获得订单总价
					//totalprice=op.loopGetElementValue("Ordersprice", "data-orgp", 10, explicitWaitTimeoutLoop);
					Log.logInfo("*****物价总计：" + totalprice);
				} else {
					totalprice = op.loopGetElementText("Ordersprice", 10, explicitWaitTimeoutLoop).replace("Total $", "").replace(",", ".").replace("USD", "").replace("€", "").trim();// 获得订单总价
					//totalprice=op.loopGetElementValue("Ordersprice", "data-orgp", 10, explicitWaitTimeoutLoop);
					Log.logInfo("*****物价总计：" + totalprice);
				}
				ppMoney[0] = totalprice;// 存入总价
				//Page.pause(5);
				// 登录账号
				op.loopSendKeysClean("oldePPemail", ppaccount,  explicitWaitTimeoutLoop);
				WebElement orderPassword=op.MyWebDriverWait2("oldePPpwd", 10, false);
				Operate.sendkey(ppaccount,orderPassword);
				
				op.loopClickElement("oldePPloginButton", 3, 60, explicitWaitTimeoutLoop);
				//Page.pause(6);
				if (op.isElementPresent("oldePPcontinue",50)) {
					op.loopClickElement("oldePPcontinue", 3, 20, explicitWaitTimeoutLoop);// 在此确认付款按
				} else {
					Log.logError("没有进入到付款继续页面，下面的内容不执行");
				}
			} else {
				Log.logInfo("进入的是新支付页面，用户正式环境支付");				
				if (op.isElementPresent("newpagecontinubtn", 10)) {//是否存在继续支付按钮
					totalprice = op.loopGetElementText("newOrdersprice", 10, 50).replace("$", "").replace("USD", "").trim();// 获得订单总价
					Log.logInfo("页面显示继续支付按钮");
					op.loopClickElement("newpagecontinubtn", 3, 60, explicitWaitTimeoutLoop);
				} else {
					
					/*PP支付时获取价格和提示*/
					if (op.isElementPresent("newpagepppay", 10)) {// 是否是登录的用户名输入框
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
							Log.logInfo("使用的是多语言支付");
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
			}
			
			
			
			Page.pause(8);
			if(driver.getWindowHandles().size()>1){
				driver.close();//关掉当前这个窗口
				driver.switchTo().window(mianHandles);
			}else{
				Log.logInfo("目前只有一个句柄");
				driver.switchTo().window(mianHandles);
			}
			
			/* 我是分割线，以下内容正式环境和测试环境都相同 */
			if (op.isElementPresent("quickpagebtn",10)) {// 判断是否是快捷PP支付
				Log.logInfo("目前使用的是快捷PP支付！！！");
				op.loopClickElement("quickpagebtn", 3, 50, explicitWaitTimeoutLoop);// 地址按钮
				setCancelWall();
				op.loopClickElement("paymentlistPayPal", 3, 50, explicitWaitTimeoutLoop);// 选择PP方式
				totalprice = op.loopGetElementText("PriceTotal", 10, 30).replace("$", "").replace("€", "");// 保存总价
				price=Double.parseDouble(totalprice);//原因这里才是传入到页面的真实价格
				ppMoney[0] = totalprice;  //快捷支付只需要对比支价格和提交的价格，这个其实是多余的，为了不让后面出错，才这样写
				// 确认支付按钮
				op.loopClickElement("placeYourOrder", 3, 50, explicitWaitTimeoutLoop);
				IsExitsshiiping();
			} else {
				Log.logInfo("目前使用的是普通支付！！！");
			}
			
			if (op.isElementPresent("ppPromptMsg",10)) {
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
				if (baseURL.contains(".d.")) {
					Log.logInfo("2017年2月14日支付页面测试环境控件位置有变化");
					payType = op.loopGetElementText("testObtainpayment", 5, explicitWaitTimeoutLoop);// 获取支付方式
					payMoney = op.loopGetElementText("testTotalprice", 10, explicitWaitTimeoutLoop).replace("$", "").replace("€", "");
				} else {
					Log.logInfo("2017年2月17日支付页面正式环境控件位置有变化");
					payType = op.loopGetElementText("obtainpayment", 5, explicitWaitTimeoutLoop);// 获取支付方式
					payMoney = op.loopGetElementText("totalprice", 10, explicitWaitTimeoutLoop).replace("$", "").replace("€", "");// 获取支付的总价
				}

				Log.logInfo("获取的成功提示语：" + ppPromptMsg);
				Log.logInfo("获取的订单编号：" + ppOrderCode);
				Log.logInfo("获取的支付方式：" + payType);
				Log.logInfo("获取的支付金额：" + payMoney);
				Successfultip[0] = ppOrderCode;
				Successfultip[1] = payMoney;
				Successfultip[2] = payType;

				String sucessMsg = "We appreciate your shopping! Your order has been submitted successfully, please remember your order number";
				if (!ppPromptMsg.contains(sucessMsg)) {
					paySucessMsgFlag = false;
					Log.logInfo("支付成功提示语有误，页面显示的提示语句为:" + ppPromptMsg);
				} else {
					paySucessMsgFlag = true;
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
	 * 
	* @测试点: orderadder 
	* @验证点: 存取的下单页面的地址，并把地址进行一些转换
	* @使用环境：     测试环境
	* @备注： void    
	* @author zhangjun 
	* @date 2016年12月14日
	 */
	
	public String[] orderadder(){
				
		//获取地址信息，Name,AddressLine1,Address,PostalCode,Phone,Emailaddress
		String[] adressList = new String[8];
		String[] adressList2 = new String[9];
			try {
				for(int k=1;k<8;k++){
				Page.pause(1);	
				String test	= driver.findElement(By.xpath("//*[@id='jsconsigneeList']/li["+k+"]")).getText();
				System.out.println(test);
				adressList[k] =test;
				}
				
				for(int j=1;j<adressList.length;j++){
					if(adressList[j].split(":")[0].contains("Name")){
						adressList2[0]=adressList[j].split(":")[1].trim().split(" ")[0];//name:zhang
						adressList2[1]=adressList[j].split(":")[1].trim().split(" ")[1];//name:zhang
					}else //不同
					if(adressList[j].split(":")[0].contains("Line1")){
						adressList2[3]=adressList[j].split(":")[1].trim();//地址1zhang
					}else 
					//4不同
					if(adressList[j].split(":")[0].equals("Address")){
						String[] adders=adressList[j].split(":")[1].trim().split(" ");
						String adder = "";
						if(adders.length>2){
							for(int k=2 ; k<adders.length ;k++){
								adder+=adders[k]+" ";
							}
						}
						adressList2[4]=adder.substring(0, adder.length()-1);//地址Armenia
						
						adressList2[5]=adders[1];//地址Armavir
						adressList2[6]=adders[0];//地址dsdsdsds
					}else 
					if(adressList[j].split(":")[0].contains("Postal")){
						adressList2[8]=adressList[j].split(":")[1].trim();//code 45454
					}else 
					if(adressList[j].split(":")[0].contains("Phone")){
						adressList2[7]=adressList[j].split(":")[1].trim(); //电话78785445
					}else 
					if(adressList[j].split(":")[0].contains("E-mail")){
						adressList2[2]=adressList[j].split(":")[1].trim();//邮箱zhangjun1@globalegrow.com
					}
			}}catch (Exception e) {
				e.printStackTrace();
			}
		
		//地址转换,按顺序存入数组：fistName,lastName,email,addressline1,country,state,city,phone,postalCode
		
		return adressList2;
	}

	/**
	 * 
	* @测试点: 能正常取消订单
	* @验证点: 下单，取消订单，订单状态为cancelled 
	* @使用环境：     测试环境
	* @备注： void    
	* @author zhangjun 
	* @date 2016年12月16日
	 */
	@Test
	public void cancelorder(){
		interfaceMethod.crearAllCartGoods(myPublicUrl,loginName);	
		;
		try {
			op.loopGet(goodTestUrl, 40, 3, 90);//打开商品
			op.loopClickElement("addToBag", 4, 10, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
		    op.loopClickElement("proceedToCheckout", 4, 10, explicitWaitTimeoutLoop);// 去结算
		    setCancelWall();
		    op.loopClickElement("paymentWebcollect", 4, 10, explicitWaitTimeoutLoop);// 去结算
		    op.loopClickElement("placeYourOrder", 3, 20, explicitWaitTimeoutLoop);//点击付款
		    IsExitsshiiping();
		     if (op.isElementPresent("placeYourOrder",50)) {				
					Log.logInfo("已经成功进入到 了支付页面,生成订单成功");
				}else{
					Log.logError("没有进入到支付页面，生成订单失败！！！");
				}
		    

		   op.loopGet(myOrderUrl, 60, 3, 60);// 转向我的订单
		   String getorderstat=op.loopGetElementText("orderstate", 3, explicitWaitTimeoutLoop);
	    
		   if(getorderstat.equals("Waiting for payment")||getorderstat.equals("Por pagar")){
			   Log.logInfo("订单状态正确，为：Waiting for payment");
			   op.loopClickElement("Cancelorder", 3, 3, explicitWaitTimeoutLoop);//取消订单
			   op.loopClickElement("cancelconfirm", 3, 20,explicitWaitTimeoutLoop);
			   String getorderstat2=op.loopGetElementText("orderstate", 3, explicitWaitTimeoutLoop);//状态变化
			   if(getorderstat2.equals("Cancelled")||getorderstat2.contains("Cancelado")){
				   Log.logInfo("取消订单成功");
			   }else{
				   Log.logError("取消订单失败，目前的状态为："+getorderstat2);
			   }			   
		   }else{
			   Log.logError("订单状态不正确，页面显示为："+getorderstat);
		   }

		}catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("订单状态不正确");
			Assert.fail();
		}
	}
	/**
	 * 
	* @测试点: paypal订单继续支付
	* @验证点: paypal订单继续支付 
	* @使用环境：     测试环境
	* @备注： void    
	* @author zhangjun 
	* @date 2016年12月16日
	 */
	@Test
	public void continue_pp_pay(){
		interfaceMethod.crearAllCartGoods(myPublicUrl,loginName);
		String[] goods = goodsPage();// 打开商品页面并获取商品信
		try {
			op.loopClickElement("addToBag", 40, 10, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
			// 商品数量、商品实际价格、商品总价
			op.loopClickElement("proceedToCheckout", 20, 3, explicitWaitTimeoutLoop);// 点击checkout，进入结算页面	
			setCancelWall();
			String[] itemList = getItemList();// 获取支付时的内容、
			op.loopClickElement("paymentlistPayPal", 3, 3, explicitWaitTimeoutLoop);// 选择使用PP支付
			// 商品页的原价和下单时的原价一致
			boolean Ordinarymail = df.format(Double.parseDouble((goods[1]))).equals(df.format(Double.parseDouble(itemList[0]))); //
			// 计算的总价
			Double itemSum = Double.parseDouble(itemList[0]) + Double.parseDouble(itemList[2])+ Double.parseDouble(itemList[4]);
			boolean gettotal = df.format(Double.parseDouble(itemList[5])).equals(df.format(itemSum));
			// 计算的总价和界面总价是否一致
			if (!gettotal && Ordinarymail) {
				Log.logError("支付页面商品总价与界面的商品总价不一致,计算的值:" + itemSum + ",界面值：" + itemList[5]);
			}
			op.loopClickElement("placeYourOrder", 3, 30, explicitWaitTimeoutLoop);// 点击支付进入支付页面
			IsExitsshiiping();//是否没有选择运输方式
	/*		ControlUsingMethod.switchingWindow(driver, mianHandles);//判断是否有句柄
			Page.pause(4);
			url=driver.getCurrentUrl();
			if(url.contains(setpayUrl)||op.isElementPresent("iframeorder",10)){
				Log.logInfo("已经成功进入到了PP支付页面");
			} else {
				Log.logError("没有进入了PP支付页面！！！");
			}
			
			Page.pause(1);
			if(driver.getWindowHandles().size()>1){
				ControlUsingMethod.switchingCloseWindow(driver, mianHandles);//关闭窗口句柄
			}else{
				Log.logInfo("目前只有一个句柄");
			}*/
			if(enterPaymentPage()==true){
				Log.logInfo("成功进入到支付页面");
			}else {
				Log.logError("进入到支付页面失败");
			}	
			
			
			op.loopGet(myOrderUrl, 60, 3, 80);// 转向我的订单		
			String getorderstat = op.loopGetElementText("orderstate", 3, explicitWaitTimeoutLoop);
			if (getorderstat.equals("Waiting for payment")||getorderstat.equals("Por pagar")) {
				Log.logInfo("订单状态正确，为：Waiting for payment");
				String getOrderNo=op.loopGetElementText("OrderNo", 10,10);
				Log.logInfo("订单号为："+getOrderNo);
				op.loopClickElement("OrderNo", 3, 20, explicitWaitTimeoutLoop);
				
				op.loopClickElement("continuePay", 3, 40, explicitWaitTimeoutLoop);
				if(op.isElementPresent("iframeorder",10)){
					Log.logInfo("已经成功进入到了PP继续支付页面，验证通过");
				} else {
					Log.logError("没有进入了PP支付页面，验证失败！！！");
				}
				
				
				/*String[] orderconfirm = paypp(total2);
				op.loopGet(myOrderUrl, 60, 3, 80);// 转向我的订单
				
				*//** 查看订单状态是否有变化 *//*		
				List<WebElement> rows = op.getElements("ordersNumber");// 获取列表行数，统计列表有多少个产品
				Log.logInfo(rows.size());
				for (int i = 1; i < rows.size(); i++) {
					String getcode = rows.get(i).findElement(By.cssSelector("td:nth-child(1)> a")).getText();
					// 取得支付成功的那一个订单
					if (getcode.equals(orderconfirm[0])) {
						//rows.get(i).findElement(By.cssSelector("td:nth-child(1)> a")).click();
						Log.logInfo("查找到对应的订单号！！！！");
						String getorderstat2 = op.loopGetElementText("orderstate", 3, 300);// 状态变化
						if (getorderstat2.equals("Paid")||getorderstat2.equals("Pagado")) {
							Log.logInfo("订单付款成功，状态更改为paid");
						} else {
							Log.logError("状态显示不正确，目前的状态为：" + getorderstat2);
						}
						break;
					} else {
						Log.logError("没有查找到对应的订单号！！！！");
					}
					Log.logInfo("getcode:" + getcode);
				}*/

			} else {
				Log.logError("订单状态不正确，页面显示为：" + getorderstat + "实际为Waiting for payment");
			}
			Pub.printTestCaseInfo(info);
		} catch (Exception e) {
			e.printStackTrace();
			Pub.printTestCaseExceptionInfo(info);
			Log.logWarn("订单状态不正确");
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}

	/**
	 * 
	* @测试点: 信用卡订单继续支付
	* @验证点: 1.购物下单，选择信用卡支付方式，提交订单2.生成的订单号正确，状态正确，价格正确，点击可进入3.点击继续支付按钮，正确跳转到支付页面
	* @使用环境：     测试环境
	* @备注： void    
	* @author zhangjun 
	* @date 2016年12月21日
	* 修改说明：2016.12.30，对选择的地区做了一个判断，增强容错
	 */
	@Test
	public void continue_pay_credit(){
	
		interfaceMethod.crearAllCartGoods(myPublicUrl,loginName);
		String[] goods = goodsPage();// 打开商品页面并获取商品信
		boolean Ordinarymail;// 商品页的原价和下单时的原价一致
		boolean gettotal = false;//总价
		Double itemSum;//计算的总价
	
		try {
			op.loopClickElement("addToBag", 80, 20, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
			// 商品数量、商品实际价格、商品总价
			op.loopClickElement("proceedToCheckout", 20, 3, explicitWaitTimeoutLoop);// 点击checkout，进入结算页面
			String[] itemList = getItemList();// 获取支付时的内容
			setCancelWall();
			 op.loopClickElement("address_btn",4,20,explicitWaitTimeoutLoop);			
			 op.loopClickElement("address_select",4,20,explicitWaitTimeoutLoop);//点击编辑
			 Page.pause(1);
			
			String selectedSale=new Select(driver.findElement(By.name("country"))).getFirstSelectedOption().getText();
		    if (!selectedSale.equals("France")){
		    	 op.getSelect("ShippingCountryRegion", 10).selectByVisibleText("France");//需要判断是否是巴西e支付
		    	 Page.pause(1);
				 op.getSelect("Shippingprovince", 10).selectByIndex(1);
				 Log.logInfo("设置France地址成功");
		    }
		    Log.logInfo("已经是France的地址");
		    op.loopClickElement("changeaddressOk",3,10,explicitWaitTimeoutLoop);
		    
		    if(baseURL.contains("/es/")){		    	
		    	itemSum =(Double.parseDouble(itemList[0]) + Double.parseDouble(itemList[2]) + Double.parseDouble(itemList[4]))*rate;
		    	String totals=df.format(Double.parseDouble(itemList[0])*rate);
		    	String totalDoubel=df.format(Double.parseDouble(itemList[5])*rate);
		    	Ordinarymail = goods[1].equals(totals);
		    	gettotal =totalDoubel.equals(df.format(itemSum));
		    }else{
		    	itemSum = Double.parseDouble(itemList[0]) + Double.parseDouble(itemList[2]) + Double.parseDouble(itemList[4]);
		    	Ordinarymail = goods[1].equals(itemList[0]);
		    	 gettotal = df.format(Double.parseDouble(itemList[5])).equals(df.format(itemSum));
		    }
		      //
			
			// 计算的总价和界面总价是否一致
			if (!gettotal && Ordinarymail) {
				Log.logError("支付页面商品总价与界面的商品总价不一致,计算的值:" + itemSum + ",界面值：" + itemList[5]);
			}
			setCancelWall();//取消电子钱包
			op.loopClickElement("paymentWebcollect", 3, 30, explicitWaitTimeoutLoop);// 选择信用卡
			op.loopClickElement("placeYourOrder", 3, 30, explicitWaitTimeoutLoop);// 点击支付进入支付页面
			IsExitsshiiping();
			
			if (op.isElementPresent("placeYourOrder",20)) {
				Log.logInfo("已经成功进入到了信用卡支付页面,生成订单成功");
			} else {
				Log.logError("没有进入了信用卡支付页面，生成订单失败！！！");
			}

			op.loopGet(myOrderUrl, 40, 3, 80);// 转向我的订单
			String getorderstat = op.loopGetElementText("orderstate", 3, explicitWaitTimeoutLoop);
			if (getorderstat.equals("Waiting for payment")||getorderstat.equals("Por pagar")) {
				Log.logInfo("订单状态正确，为：Waiting for payment");
				op.loopClickElement("OrderNo", 3, 10, expliceTimeout);

				Page.pause(3);
				String getMethodpayment = op.loopGetElementText("payment", 3, explicitWaitTimeoutLoop);// 支付方式d
				if (getMethodpayment.equals("Credit Card")) {
					Log.logInfo("支付方式正确，验证通过");
					op.loopClickElement("continuePayCredit", 3, 10, explicitWaitTimeoutLoop);
					if (op.isElementPresent("placeYourOrder",20)) {
						Log.logInfo("已经成功进入到了信用卡支付页面,继续支付，验证通过");
					} else {
						Log.logError("没有进入了信用卡支付页面，继续支付，验证失败！！！");
					}

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
	* @测试点: 根据订单编号和分类，打开订单详情(所在的页面:我的订单页面)
	* @验证点: 根据订单编号和分类，打开订单详情(所在的页面:我的订单页面)
	* @ @param orderCode 支付成功生成的订单编号
	* @param price   支付时的价格
	* @param methodpayment  支付方式
	* @使用环境：测试环境，正式环境
	* @return    
	* @备注： String[]    
	* @author zhangjun 
	* @date 2017年5月17日 
	  @修改说明
	 */
	public String[] openOrderTail(String orderCode, String price,String methodpayment) {

		String[] adressList = new String[16];
		String[] adressList2 = new String[9];
		String getcode;
		try {
			op.loopGet(myOrderUrl, 40, 3, 60);// 转向我的订单
			Log.logInfo("成功进入到订单页面");
			List<WebElement> rows = op.getElements("ordersNumber");// 获取列表行数，统计列表有多少个产品
			Log.logInfo(rows.size());
			for (int i = 1; i < rows.size(); i++) {
				getcode = rows.get(i).findElement(By.cssSelector("td:nth-child(1)> a")).getText();
				// 取得支付成功的那一个订单
				if (getcode.equals(orderCode)) {
					rows.get(i).findElement(By.cssSelector("td:nth-child(1)> a")).click();
					Log.logInfo("支付页生成的订单号"+getcode);
					Log.logInfo("进入订单页的订单编号为："+getcode);
					Log.logInfo("查找到对应的订单号！！！！");
					break;
				} else {
					Log.logError("没有查找到对应的订单号！！！！");
				}
				Log.logInfo("页面获得的订单号为:" + getcode);
			}
			/**
			 * 订单详情页面，对比总价和订单的状态
			 */
			// 概览信息 订单编号
			String getorderStute = op.loopGetElementText("orderStute", 3, explicitWaitTimeoutLoop);// 订单状态
			String getorderDate = op.loopGetElementText("orderPrice", 3, 20);// 订单总价
			
			String getMethodpayment=op.loopGetElementText("payment", 3, explicitWaitTimeoutLoop).substring(0,methodpayment.length());//支付方式
			Log.logInfo("订单页面订单状态为："+getorderStute);
			Log.logInfo("订单页面订单总价为："+getorderDate);
			Log.logInfo("订单页面支付方式为："+getMethodpayment);
			
			boolean orderStute = getorderStute.equals("Paid")||getorderStute.equals("Pagado");
			boolean orderDate = df.format(Double.parseDouble(getorderDate)).equals(df.format(Double.parseDouble(price)));
			
			boolean orderpay=String.format("%s", getMethodpayment).equals(String.format("%s",methodpayment));
			Log.logInfo("支付成功页的总价与订单页面的总价，状态为：" + orderDate);
			Log.logInfo("支付成功页的支付状态与订单页面状态:" + orderStute);
			Log.logInfo("支付成功页支付状态为:" + orderpay);
			
			if(baseURL.contains("/es/")){
				if (orderStute && orderpay) {
					Log.logInfo("我的订单详情中总价和订单状态都正常,且支付方式也一致，验证通过");			
				} else {
					Log.logError("下单页面，订单状态，"+getorderStute+"支付时的支付方式:"+methodpayment);
				}
			}else{
				if (orderStute && orderDate&&orderpay) {
					Log.logInfo("我的订单详情中总价和订单状态都正常,且支付方式也一致，验证通过");				
				} else {
					Log.logError("下单页面，订单状态，"+getorderStute+"下单页面，总价为："+getorderDate+"下单页面，支付方式为:"+getMethodpayment+"支付时的价格："+df.format(Double.parseDouble(price))+"支付时的支付方式:"+methodpayment);
				}
			}
			/**
			 * 订单详情页去对比数据
			 */
			List<WebElement> detail = op.getElements("orderdetai");
			for (int k = 3; k < detail.size(); k++) {
				String orderinstructions = detail.get(k).findElement(By.xpath("th")).getText();
				String ordervalue = detail.get(k).findElement(By.xpath("td")).getText();
				String contentvalue = orderinstructions + ordervalue;
				adressList[k] = contentvalue;
			}

			// 地址转换,按顺序存入数组：fistName,lastName,email,addressline1,country,state,city,phone,postalCode

			for (int k1 = 3; k1 < adressList.length; k1++) {
				if (adressList[k1].split(":")[0].contains("name")) {
					adressList2[0] = adressList[k1].split(":")[1].trim().split(" ")[0];
					adressList2[1] = adressList[k1].split(":")[1].trim().split(" ")[1];
				}else 
				if (adressList[k1].split(":")[0].contains("Town")) {
					adressList2[6] = adressList[k1].split(":")[1].trim(); // Town
				}else 
				if (adressList[k1].split(":")[0].contains("address")) {
					adressList2[3] = adressList[k1].split(":")[1].trim().split(" ")[0]; // Town
				}else 
				if (adressList[k1].split(":")[0].contains("Destination")) {
					adressList2[4] = adressList[k1].split(":")[1].trim();// line1
				}else 
				if (adressList[k1].split(":")[0].contains("State")) {
					adressList2[5] = adressList[k1].split(":")[1].trim();// line2
				}else 
				if (adressList[k1].split(":")[0].contains("Zip")) {
					adressList2[8] = adressList[k1].split(":")[1].trim();
				}else 
				if (adressList[k1].split(":")[0].contains("telephone")) {
					adressList2[7] = adressList[k1].split(":")[1].trim();
				}else 
				if (adressList[k1].split(":")[0].contains("E-mail")) {
					adressList2[2] = adressList[k1].split(":")[1].trim();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return adressList2;
	}
	
	
	
 	
	/**
	 * 
	* @测试点:删除canncle状态的订单
	* @验证点:订单太多，删除未支付的订单
	* @使用环境： 
	* @param  
	* @备注： void    
	* @author zhangjun 
	 * @throws Exception 
	* @date 2017年1月3日
	 */
	public void deleteCancelOrder() throws Exception{

		try {
			op.loopGet(myOrderUrl, 30, 3, 60);
		} catch (Exception e) {
		}
		String getUrl = "";
		List<WebElement> orderstats = op.getElements("ordersNumber");
		int j = 1;
		int k=1;
		while (j < 4) {
			for (int i = 1; i < orderstats.size(); i++) {
				if (i % 10 != 0) {
					String getOrderStatus = orderstats.get(i).findElement(By.cssSelector("td.js_replace_status")).getText();
					if (getOrderStatus.equals("Waiting for payment")||getOrderStatus.equals("Por pagar")) {
						WebElement elem = orderstats.get(i).findElement(By.cssSelector("td:nth-child(5)"));
						String province = elem.getAttribute("innerHTML");
						if (province.contains("span")) {
							try {
								WebElement element = orderstats.get(i).findElement(By.cssSelector("td:nth-child(5) > a.cancleOder"));
								String url = element.getAttribute("href");
								element.click();// 存在取消按钮;
								op.loopClickElement("cancelconfirm", 3, 20, explicitWaitTimeoutLoop);
								Log.logInfo("取消订单成功");
								// Page.pause(6);
								op.loopGet(url, 40, 3, 60);
								Page.pause(1);
								driver.navigate().back();
								if (j != 1) {
									op.loopGet(getUrl, 30, 2, 60);// 执行js后有会返回到第一页
								}
								orderstats = op.getElements("ordersNumber");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						Log.logInfo("订单状态是已支付");
						k++;
						if (k>10){
							j++;
							break;
						}
					}
				} else {
					
					getUrl = "http://user." + myPublicUrl.replace("www.", "").replace("https://", "").replace("http://", "") + "m-users-a-order_list.htm?page="
							+ (j + 1);  
					k=0;//标志位，恢复
					System.out.println(getUrl);
					try {
						op.loopGet(getUrl, 40, 3, 60);
					} catch (Exception e) {
						e.printStackTrace();
					}
					j++;
					orderstats = op.getElements("ordersNumber");
				}
			}
		}
		interfaceMethod.deleteCancelOrder(myPublicUrl,loginName);// 删除订单

	}
	
	
	/**
	 * 
	* @测试点: 去掉wall电子钱包的支付方式 
	* @验证点: 因为OMS对账的原因，目前去掉接口充值电子钱包，改用实际账户有钱，进行测试，但是引发的问题就是每个支付页面都会提示先使用电子钱包
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月9日 
	  @修改说明
	 */
	public void setCancelWall(){
		try {
			if (op.isElementPresent("wallet_pass")) {// 电子钱包
				Log.logInfo("存在电子钱包支付方式");
				boolean chooseWallet = driver.findElement(By.id("Js_userWallet")).isSelected();
				if (chooseWallet == true) {
					op.loopClickElement("wallteriswallet", 20, 40, 60);
					Log.logInfo("取消勾选电子钱包支付方式");
				}
			} else {
				Log.logInfo("不存在电子钱包支付方式，不执行电子钱包的支付方式");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("点击失败");
		}
		
	}
	
	
	/**
	 * 
	* @测试点: 获取商品，并跳转到结算页 
	* @验证点: 清除购物车，添加商品发哦购物车，并跳转到结算页
	* @使用环境：     测试环境
	* @备注： void    
	* @author zhangjun 
	* @date 2016年12月30日
	 */
	public String[] getGoodsSettlement(){
		interfaceMethod.crearAllCartGoods(myPublicUrl,loginName);// 清除购物车
		String[] goods = goodsPage();// 打开商品页面并获取商品信息
		try {
			op.loopClickElement("addToBag", 80, 20, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
			op.loopClickElement("proceedToCheckout", 20, 10, explicitWaitTimeoutLoop);// 点击checkout，进入结算页面
			setCancelWall();                                                 
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return goods;
	}
	/**
	 * 
	* @测试点: transportationState 
	* @验证点: 选择每一种快递方式，都需要在订单中对比，订单中运输方式的正确性和支付的价格的正确性 
	* @param mode  你选择运输方式
	* @param price  结算页面的总价
	* @备注： void    
	* @author zhangjun 
	* @date 2016年12月30日
	 */
	public void transportationState(String mode, String price) {
		try {
			op.loopGet(myOrderUrl, 40, 3, 80);
			Log.logInfo("成功进入到订单页面");
			String getOrder=op.loopGetElementText("OrderNo", 3, explicitWaitTimeoutLoop);
			op.loopClickElement("OrderNo", 3, 20, explicitWaitTimeoutLoop);
		    Log.logInfo("生成的订单编号为:"+getOrder);
			String getMethodpayment = op.loopGetElementText("Deliveries", 6, explicitWaitTimeoutLoop);// 物流方式
			String getorderDate = op.loopGetElementText("orderPrice", 3, explicitWaitTimeoutLoop);// 订单总价
			Log.logInfo("订单页面，物流方式为："+getMethodpayment);
			Log.logInfo("订单页面，订单总价为："+getorderDate);
			
			if (getMethodpayment.equals(mode) || getorderDate.equals(price)) {
				Log.logInfo("下单页面与订单页面的物流方式正确，总价正确，验证通过");
			} else {
				Log.logError("物流不正确，验证不通过，实际获取的物流方式为:" + getMethodpayment + "实际获取的总价格为：" + getorderDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	/*获取地址*/
	public void urlInit() {
		if(baseURL.contains(".trunk.")&&baseURL.contains("/es/")){//西班牙语测试环境
			Log.logInfo("现在是西班牙语测试环境");
			loginUrl = "http://login.rosewholesale.com.trunk.s1.egomsl.com/es/m-users-a-sign.htm";	
			checkoutUrl = "http://order.rosewholesale.com.trunk.s1.egomsl.com/es/m-flow-a-checkout.htm?flow=checkout";
			checknologintUrl = "http://login.rosewholesale.com.trunk.s1.egomsl.com/es/m-users-a-sign.htm?flow=checkout";
			checkoutUrl2="http://order.rosewholesale.com.trunk.s1.egomsl.com/es/m-flow-a-checkout.htm";		
			addressbook = "http://user.rosewholesale.com.trunk.s1.egomsl.com/es/m-users-a-address_list.htm";
			myOrderUrl = "http://user.rosewholesale.com.trunk.s1.egomsl.com/es/m-users-a-order_list.htm";// 订单页面上面还有问题
			myPublicUrl = "http://rosewholesale.com.trunk.s1.egomsl.com/";
			
			url="http://rosewholesale.com.d.s1.egomsl.com/eload_admin/crontab/send_email_queue.php";
			setpayUrl="https://www.sandbox.paypal.com/";//支付页面的url地址
			rates="http://cart.rosewholesale.com.trunk.s1.egomsl.com/data-cache/currency_huilv.js";//汇率换算
			goodTestUrl=interfaceMethod.getGoodUrl(myPublicUrl,"one");	
			
		}else if(baseURL.contains("/es/")){
			//取当前页面源码的huilv.js链接
			Log.logInfo("现在是西班牙正式环境");
			loginUrl = "http://login.rosewholesale.com/es/m-users-a-sign.htm";	
			checkoutUrl = "https://order.rosewholesale.com/es/m-flow-a-checkout.htm";
			checknologintUrl = "https://login.rosewholesale.com/es/m-users-a-sign.htm?flow=checkout";
			checkoutUrl2="http://order.rosewholesale.com/es/m-flow-a-checkout.htm";		
			addressbook = "http://user.rosewholesale.com/es/m-users-a-address_list.htm";
			myOrderUrl = "https://user.rosewholesale.com/es/m-users-a-order_list.htm";// 订单页面上面还有问题
			myPublicUrl = "https://rosewholesale.com/";
			
			url="http://rosewholesale.com.d.s1.egomsl.com/eload_admin/crontab/send_email_queue.php";
			setpayUrl="https://www.sandbox.paypal.com/";//支付页面的url地址
			rates="https://cart.rosewholesale.com/data-cache/currency_huilv.js";//汇率换算
			goodTestUrl=interfaceMethod.getGoodUrl(myPublicUrl,"one");	
		}else if (baseURL.contains(".d.")) {
			loginUrl = "http://login.rosewholesale.com.d.s1.egomsl.com/m-users-a-sign.htm";	
			checkoutUrl = "http://order.rosewholesale.com.d.s1.egomsl.com/m-flow-a-checkout.htm?flow=checkout";
			checknologintUrl = "http://login.rosewholesale.com.d.s1.egomsl.com/m-users-a-sign.htm?flow=checkout";
			checkoutUrl2="http://order.rosewholesale.com.d.s1.egomsl.com/m-flow-a-checkout.htm";
			addressbook = "http://user.rosewholesale.com.d.s1.egomsl.com/m-users-a-address_list.htm";
			myOrderUrl = "http://user.rosewholesale.com.d.s1.egomsl.com/m-users-a-order_list.htm";// 订单页面上面还有问题
			myPublicUrl = "http://www.rosewholesale.com.d.s1.egomsl.com/";
			url="http://rosewholesale.com.d.s1.egomsl.com/eload_admin/crontab/send_email_queue.php";
			setpayUrl="https://www.paypal.com";//支付页面的url地址;//支付页面的url地址
			goodTestUrl =interfaceMethod.getGoodUrl(myPublicUrl,"one");
			Log.logInfo("获取的是产品地址是："+goodTestUrl);
		}  else {
			loginUrl = "https://login.rosewholesale.com/m-users-a-sign.htm";			
			checkoutUrl = "https://order.rosewholesale.com/m-flow-a-checkout.htm?flow=checkout";
			checknologintUrl = "https://login.rosewholesale.com/m-users-a-sign.htm?flow=checkout";
			checkoutUrl2="https://order.rosewholesale.com/m-flow-a-checkout.htm";
			addressbook = "https://user.rosewholesale.com/m-users-a-address_list.htm";
			myOrderUrl = "https://user.rosewholesale.com/m-users-a-order_list.htm";// 订单页面上面还有问题
			myPublicUrl = "https://www.rosewholesale.com/";
			url="http://rosewholesale.com/eload_admin/crontab/send_email_queue.php";
			setpayUrl="https://www.paypal.com";//支付页面的url地址
			goodTestUrl =interfaceMethod.getGoodUrl(myPublicUrl,"one");
			Log.logInfo("获取的是产品地址是："+goodTestUrl);
		}
	}

	
	
	 /**
	  * 
	 * @测试点: 是否存在运输方式 
	 * @验证点: 是否存在运输方式 
	 * @使用环境：     测试环境，正式环境
	 * @备注： void    
	 * @author zhangjun 
	 * @date 2017年5月8日 
	   @修改说明
	  */
	//2017年4月14日由于接口和本身功能的bug原因，某些商品没有选择运输方式，目前做一个判断
	 public  void IsExitsshiiping(){
		if(op.isElementPresent("transportation",10)){
			Log.logInfo("没有选择运输方式");
			try {
				op.loopClickElement("transportation", 5, 10, 10);
				op.loopClickElement("Standardbtn", 5, 10, 10);
				Log.logInfo("已经选择一个运输方式");
				op.loopClickGoPageURL("placeYourOrder", 10, 10, 20, 60);// 点击支付进入支付页面
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else{
			Log.logInfo("已正常选择了运输方式");
		}
	 }
	
	 /**
	  * 
	 * @测试点:  进入到支付页面 
	 * @验证点: 循环10次判断地址是否包含paypal，来判断是否进入到了支付页面,支付页面出现了两个窗口所以要切换句柄
	 * @使用环境：     测试环境，正式环境
	 * @备注： void    
	 * @author zhangjun 
	 * @date 2017年5月15日 
	   @修改说明
	  */
	 public  boolean enterPaymentPage(){
		 Page.pause(8);
		 if(driver.getWindowHandles().size()>1){
			 paymentPage=true;
		 }else{
			 for(int i=0;i<13;i++){
				 Log.logInfo("第"+i+"次");
					Page.pause(2);
					if(driver.getCurrentUrl().contains(".paypal.")){
						paymentPage=true;
						break;
					}
			 }
		 }
		 return paymentPage;
		 
	 }
	 
	 
	 /**
	  * 
	 * @测试点: 获取商品的运输方式
	 * @验证点: 获取商品的运输方式
	 * @使用环境：测试环境，正式环境
	 * @return    1,2,4,5分别对应页面上的value运输方式
	 * @备注： String    
	 * @author zhangjun 
	 * @date 2017年5月26日 
	   @修改说明
	  */
	 
	public String shippingMethod() {
		String shippingMethods = null;
		try {
			Map<String, Object> getproduct = interfaceMethod.getGoods(myPublicUrl, "n", "n");
			String getproducturl = (String) getproduct.get("url_title");
			shippingMethods = (String) getproduct.get("goods_shipping_method");
			getproducturl = getproducturl + "?" + timeStamp;// 刷新缓存'
			Log.logInfo("获取的测试商品地址为：" + getproducturl + "?" + timeStamp);
			if(baseURL.contains("/es/")){
				interfaceMethod.convertAddress(op, getproducturl);
			}else{
				op.loopGet(getproducturl, 50, 3, 80);
			}
			
			op.loopClickElement("addToBag", 80, 20, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
			op.loopClickElement("proceedToCheckout", 20, 10, explicitWaitTimeoutLoop);// 点击checkout，进入结算页面
		} catch (Exception e) {
			e.printStackTrace();
		}
		return shippingMethods;
	}
	
}
