package com.globalegrow.rosewholesalew;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.globalegrow.autotest_encrypt.Operate;
import com.globalegrow.base.StartPhoneBrowser;
import com.globalegrow.code.Locator;
import com.globalegrow.code.Page;
import com.globalegrow.crypt.pp;
import com.globalegrow.custom.ControlUsingMethod;
import com.globalegrow.custom.InterfaceMethod;
import com.globalegrow.util.Log;
import com.globalegrow.util.Op;

import com.globalegrow.util.Pub;

import com.globalegrow.util.testInfo;

import net.sourceforge.htmlunit.corejs.javascript.ast.WhileLoop;

public class RW_M_order_manage extends StartPhoneBrowser {
	private String className = GetClassName();
	//public RW_M_geturl register;
	private testInfo info = null;
	public Date sendEmailDate = new Date();

	private String testCaseProjectName = "rosewholesale";
	private String baseURL = "";
	private String paypal_password;//PP密码
	private String ppaccount;//登录账户
	private String timeStamp;// 时间戳
	String getkeys;
	String loginUrl = "";// 登录的url
	String addressUrl = "";// 增加地址页面
	String myAccountUrl = "";// 个人信息页面
	String urlList = "";// 地址列表
	String  LoginURL="";//登录地址
	String myPublicUrl="";//调用接口的域名
	String AddressList="";//地址列表
	String productUrl="";//商品页面
	String PersonalInformation;//个人信息页面
	String  LoginName="autotest19@globalegrow.com";//登录的用
	String OrderList;//地址列表的list
	String myPublicUrl2;//获取验证码的接口
	String OrderListURL;//订单列表的地址
	String projectName="rosewholesalew";
	String  url;// 对比的url
	String[] balanceAddress=null;//预先存入地址
	String[] balanceSum=null;   //预先存入的总和
	InterfaceMethod  interfaceMethod;
	DecimalFormat df ;
	WebDriverWait wait;//显示等待页面元素出现
	@Parameters({ "testUrl" })
	private RW_M_order_manage(String testUrl) {
		moduleName = className.substring(className.lastIndexOf(".") + 1);
		info = new testInfo(moduleName);
		baseURL = testUrl;


	}
	@Parameters({"PPaccount","keys","devicesName"})
	@BeforeClass
	public void beforeClass(String PPaccount,String keys,String devicesName) {
		String methodName = GetMethodName();
		String methodNameFull = moduleName + "." + methodName;
		Log.logInfo("**************Ready to test (" + moduleName + ")!!!**************\n");
		Log.logInfo("(" + methodNameFull + ")...beforeClass start...");
		try {
			start(devicesName); // 初始化driver
			driver = super.getDriver();			
			op.setScreenShotPath(screenShotPath);
			interfaceMethod=new InterfaceMethod();
			df = new DecimalFormat("#0.00");
			urlInit();
			getkeys=keys;
			ppaccount=PPaccount;//登录账
			driver.get(baseURL);
			login();
			wait = new WebDriverWait(driver, 80);//显示等待页面元素出现
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			timeStamp = simpleDateFormat.format(date);
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
		//	DelectCancelOrder();
			driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Log.logInfo("(" + methodNameFull + ")...afterClass stoop...\n\n");
		}
	}


	
	/**
	 * 
	* @测试点: 普通支付，未登录，跳转到登录页
	* @验证点: 1.未登录，购物车下单后，点击结算
	2.页面跳转到登录页,验证成功
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月22日 
	  @修改说明
	 */
	@Test
	public void payment_notlogged(){
		try {
			op.loopGet(AddressList, 30, 3, 50);
			if(op.isElementPresent("addShipAddr")){
				Log.logInfo("存在地址列表页");
				DeleteAdressAll();
			}
			Log.logInfo("不存在地址列表页，不用删除，正常执行一下步");
			/*分割线，以上用例，是为了检查是否有地址，如果有地址，就删除地址*/
			op.loopGet(LoginURL, 30, 3, 60);
			if (op.isElementPresent("loginHomePage")) {
				Log.logInfo("用户已登录了");
				ControlUsingMethod.SetScrollBar(600);
				Page.pause(2);
				op.loopClickElement("LoGout", 3, 10, 40);
				Log.logInfo("退出用户成功");
			}
			Addshoping();
			// 跳转到登录页
			if (op.isElementPresent("loginPage_loginEmail")) {
				Log.logInfo("已经跳转到了登录页，验证成功");
			} else {
				Log.logError("没有在登录页，验证失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	
	/**
	 * 
	* @测试点: 普通支付，已登录，无地址 
	* @验证点: 1.添加商品到购物车成功，
			 2.登录后，无地址，新增地址成功
			 3.跳转到结算页面
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月23日 
	  @修改说明
	 */
	@Test
	public void payment_notaddress(){
		try {

			Addshoping();// 添加商品到购物车
			String requestId = Pub.getRandomString(5);
			String logincode = InterfaceMethod.IF_Verification(driver, myPublicUrl2, requestId, getkeys);
			op.loopSendKeys("loginPage_loginEmail", LoginName, 3, 30);
			op.loopSendKeys("loginPage_loginPwd", "123456", 3, 30);
			if (op.isElementPresent("loginCode")) {
				op.loopSendKeys("loginCode", logincode, 3, 15);
			} else {
				Log.logInfo("未开启验证码输入框，不进行输入");
			}

			op.loopClickElement("loginPage_signInBtn", 10, 30, 100);
			//op.loopGet(AddressList, 15, 3, 100);
			//Page.pause(3);
			setAllFieldVaule();// 新增地址
			if (op.isElementPresent("PlaceYourOrderBtn",20)) {
				Log.logInfo("新增地址后，跳转到结算页面，验证通过");
			} else {
				Log.logError("新增地址后，没有跳转到结算页面，验证失败");
			}


		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	/**
	 * 
	* @测试点: 登录时的快速支付流程 
	* @验证点: 1.用户已登录，有寄送地址
			2.下单，并且进行付款，付款后，地址、状态、价格都正常
			3.提交订单的价格与付款界面一致，支付成功页面支付方式和订单状态，以及支付总价与订单页面相同
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月24日 
	  @修改说明
	 */
	
	@Test
	public void pay_pp(){
		try {
			Addshoping();
			Map<String, String[]> balanceMap = getBalancePageInfo();
			balanceSum=balanceMap.get("sum");
			balanceAddress=balanceMap.get("adress");
			op.loopClickGoPageURL("PlaceYourOrderBtn", 5, 10,  90, 40);
			getPayPayment("Payment");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	
	/**
	 * 
	* @测试点: 平邮加跟踪码 
	* @验证点: 1邮费价正确，物流的方式正确
			  2总价正确,
			  3.下单后在订单列表中状态正确
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月2日 
	  @修改说明
	 */
	@Test
	public void shippingordinary_trackingcode() {
		try {
			//Addshoping();
			if(shippingMethod().contains("1")){
			op.loopClickElement("Traceability", 3, 10, 20);// 点击跟踪码
			getTransportationMode("traking_number","Flat Rate Shipping");
			}
			Pub.printTestCaseExceptionInfo(info);
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}
	
	
	/**
	 * 
	* @测试点: 平邮不加跟踪码 
	* @验证点: 1邮费价正确，物流的方式正确 2总价正确3.下单后在订单列表中运输方式正确，总价也正确 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月3日 
	  @修改说明
	 */

	@Test
	public void shipping_ordinary() {
		try {
			//Addshoping();
			if(shippingMethod().contains("1")){
			getTransportationMode("not_traking_number","Flat Rate Shipping");
			}
			Pub.printTestCaseExceptionInfo(info);
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}
	
	
	/**
	 * 
	* @测试点: 标准邮 
	* @验证点: 1邮费价正确，物流的方式正确 2总价正确 3.下单后在订单列表中运输方式正确，总价也正确 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月3日 
	  @修改说明
	 */
	@Test
	public void shipping_standard() {
		try {
			//Addshoping();
			if(shippingMethod().contains("2")){
			if (baseURL.contains(".a.")) {
				op.loopClickElement("standsShippingMethod", 2, 5, 20);
			} else {
				op.loopClickElement("officialStandsShipping", 2, 5, 20);
			}
			getTransportationMode("Standard", "Standard Shipping");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}
	
	/**
	 * 
	* @测试点: 快递 
	* @验证点: 1邮费价正确，物流的方式正确 2总价正确 3.下单后在订单列表中状态正确，总价也正确 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月3日 
	  @修改说明
	 */
	@Test
	public void shipping_expedited(){
		try {
			//Addshoping();		
			if(shippingMethod().contains("4")){
			if (baseURL.contains(".a.")) {
				op.loopClickElement("ExpeditedShippingMethod", 2, 3, 20);
			} else {
				op.loopClickElement("officialExpeditedShippingMethod", 2, 5, 20);
			}			
			getTransportationMode("Expedited","Expedited Shipping");
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}
	
	
	
	/**
	 * 
	* @测试点: 不选择保险费 
	* @验证点: 1邮费价正确，物流的方式正确 2总价正确 3.下单后在订单列表中状态正确，总价也正确 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月3日 
	  @修改说明
	 */
	@Test
	public void shipping_dontInsurance(){
		try {
			//Addshoping();
			if(shippingMethod().contains("2")){
			if (baseURL.contains(".a.")) {
				op.loopClickElement("shippingInsurance", 2, 3, 20);
			} else {
				op.loopClickElement("officialShippingInsurance", 2, 5, 20);
			}
		
			Log.logInfo("去掉保险费成功");
			getTransportationMode("not_insurance","Flat Rate Shipping");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}

	/**
	 * 
	* @测试点: 计算保险费 
	* @验证点: 1.订单费用正确（对应的运费计算正确）2订单列表，价格总价和保险费显示正确3.下单后在订单列表中状态正确
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月3日 
	  @修改说明
	 */
	@Test
	public void shipping_insurance(){

		try {
			//Addshoping();	
			if (shippingMethod().contains("1")) {
				getTransportationMode("insurance", "Flat Rate Shipping");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}
	
	
	/**
	 * 
	* @测试点: 快捷支付，支付成功 
	* @验证点: 1.订单费用正确（对应的运费正确）2.显示正确支付状态3.感谢购物语句正确4.订单概况的订单号订单金额订单支付方式与下单时的信息一致 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月4日 
	  @修改说明
	 */
	@Test
	public void quick_payment(){
		String ppPaySum="";
		try {

			interfaceMethod.crearAllCartGoods(myPublicUrl, LoginName);// 清空购物车
			op.loopGet(productUrl, 40, 3, 80);// 得到商品地址
			Log.logInfo("获取的商品地址 为："+productUrl);
			ControlUsingMethod.SetScrollBar(500);		
			
			op.loopClickGoPageURL("addcart", 5, 20, 40, 60);
			//op.loopClickElement("addcart", 10, 20, 90);// 加入到购物车
			String sumMoney = op.loopGetElementValue("shppingsubTotal", "data-orgp",  40);// 商品总额
			
			Log.logInfo("商品页商品的总价格:" + sumMoney);
			op.actionSingleClick("PlaceYourOrderBtn");// 点击进入pp支付页面
			if (op.isElementPresent("ppLogin")||op.isElementPresent("officiaTotal")) {
				Log.logInfo("成功进入支付页面!!!");
			} else {
				Log.logError("进入支付页面失败,以下内容不执行!!!");
			}
			
			if(op.isElementPresent("officiaTotal")){//外网支付的总价
				ppPaySum= op.loopGetElementText("officiaTotal", 10, 60).replace("$", "").replace("USD", "")
						.replace(",", ".").trim();
			}else {//测试环境的总价
				ppPaySum= op.loopGetElementText("ppPaySum", 10, 60).replace("$", "").replace("USD", "")
						.replace(",", ".").trim();
			}
			
			Log.logInfo("支付页面支付总金额:" + ppPaySum);
			if (Double.parseDouble(sumMoney)==Double.parseDouble(ppPaySum)) {
				Log.logInfo("结算页面的金额与支付页面的金额一致,商品页面金额：" + sumMoney + ",支付页面金额:" + ppPaySum);
			} else {
				Log.logError("结算页面的金额与支付页面的金额不一致,商品页面金额：" + sumMoney + ",支付页面金额:" + ppPaySum);
			}
			//去掉支付成功getPayPayment("quickPayment");// 快捷支付方式处理

		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
		
	}
	
	/**
    * 
   * @测试点: 信用卡支付 
   * @验证点: 1.下单成功
			2.下单页面的总价与订单的总价相同
			3.未支付状态正常
			4.收货地址相同
   * @使用环境：     测试环境，正式环境
   * @备注： void    
   * @author zhangjun 
   * @date 2017年2月9日 
     @修改说明
    */
   @Test
   public void  pay_credit_a() {
	   correFail = false;
		try {
			Addshoping();
			Page.pause(2);
			ControlUsingMethod.SetScrollBar(400);
			op.loopClickElement("cardPay", 3, 10, 30);
			getContinuePayment("card");
			Pub.printTestCaseExceptionInfo(info);
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
}
   
   /**
    * 
   * @测试点: 信用卡订单继续支付 
   * @验证点: 1.购物下单，选择信用卡支付方式，提交订单
			2.生成的订单号正确，状态正确，价格正确，点击可进入
			3.点击继续支付按钮，正确跳转到支付页面 
   * @使用环境：     测试环境，正式环境
   * @备注： pay_credit_a与   continue_pay_credit_b紧密相关
   * @author zhangjun 
   * @date 2017年2月9日 
     @修改说明
    */
   
   @Test
	public void continue_pay_credit_b() {
	   Pub.checkStatusPhoneBrowser();
		try {
           // Page.pause(2);
			op.loopClickElement("payconntiue", 3, 10, 30);
			if (op.isElementPresent("cardPayContiue")) {
				Log.logInfo("点击进入继续支付页面，跳转到支付页面，验证通过");
			} else {
				Log.logError("点击进入继续支付页面，没有跳转到支付页面，验证失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}
   
   /**
    * 
   * @测试点: PP继续支付 
   * @验证点: 1.生成订单，不支付
			2.在订单列表页面， 确认状态正确，价格正确，地址正确，点击继续支付
			3.支付成功
			4.订单状态更改为paid
   * @使用环境：     测试环境，正式环境
   * @备注： void    
   * @author zhangjun 
   * @date 2017年2月10日 
     @修改说明
    */
   @Test
   public void continue_pp_pay(){
		boolean ReverseState=false;
		try {
			Addshoping();
			getContinuePayment("pp");//生成订单，点击继续支付
			Log.logInfo("点击继续支付按钮");
			ControlUsingMethod.SetScrollBar(700);
			Page.pause(1);
			op.loopClickElement("payconntiue", 3, 10, 30);
			int i=0;
			String url=driver.getCurrentUrl();
			while (i<10){		
				if(url.contains(".paypal")){
					Page.pause(1);
					ReverseState=true;
					break;
				}
				i++;
			}
			if(ReverseState==true){
				Log.logInfo("正常跳转到支付成功页，验证通过");
			}else{
				Log.logError("没有跳转到支付成功页，验证失败");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
   }
   
   
   
   /**
    * 
   * @测试点: 取消订单 
   * @验证点: 1.下一个使用了积分的订单但不支付，进入用户中心的订单栏里取消该订单
			2.该订单被取消，订单状态变更为cancelled，订单中使用的积分原额返还给用户 
   * @使用环境：     测试环境，正式环境
   * @备注： void    
   * @author zhangjun 
   * @date 2017年2月10日 
     @修改说明
    */
   @Test
   public void cancelorder(){
		try {
			op.loopGet(LoginURL, 60, 3, 60);
			ControlUsingMethod.SetScrollBar(300);
			String getOldPoint = op.loopGetElementText("loginPoint", 5, 20);
			Log.logInfo("目前积分为:" + getOldPoint);

			Addshoping();// 添加商品
			String getpointSave = op.loopGetElementValue("pointSave", "data-orgp",  20);//有积分的数据
			Log.logInfo("扣除积分:" + getpointSave);
			
			//生成订单，在订单列表中查询
			ControlUsingMethod.SetScrollBar(700);
			op.actionSingleClick("PlaceYourOrderBtn");// 点击进入结算页面
			if (op.isElementPresent("ppLogin",30)||op.isElementPresent("officiaTotal",30)) {
				Log.logInfo("进入到继续支付页面，生成订单成功，继续执行");
			} else {
				Log.logError("没有进入到继续支付页面，生成订单失败");
			}
			// 转到订单列表
			op.loopGet(OrderList, 90, 3, 60);
			op.loopClickElement("CancaleOrder", 3, 8, 50);
			
			op.loopClickElement("cancelYes", 3, 5, 20);// 确认取消按钮
			Log.logInfo("取消订单成功");
			Page.pause(1);
			op.loopGet(LoginURL, 60, 3, 80);
			ControlUsingMethod.SetScrollBar(400);
			String getLoginPoint = op.loopGetElementText("loginPoint", 5, 20);
			if (getLoginPoint.equals(getOldPoint)) {
				Log.logInfo("取消订单，积分退还成功，目前积分为：" + getLoginPoint + " 验证通过");
			} else {
				Log.logError("取消订单，积分退还失败，目前积分为：" + getLoginPoint + "下单前的订单为:" + getOldPoint + "验证失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
   }
   
   
   /**
    * 
   * @测试点: 未登录时的快速支付流程 
   * @验证点:1.付款成功，订单费用正确（对应的运费正确）
			2.显示正确支付状态
			3.感谢购物语句正确
			4.登录pp账户，账户正确
   * @使用环境：     测试环境，正式环境
   * @备注： void    
   * @author zhangjun 
   * @date 2017年2月10日 
     @修改说明
    */
  /* @Test
   public void quick_paymentnotlogin(){
		correFail = false;
		Pub.getTestCaseInfoWap(testCasemap, GetMethodName(), info, true);
			
		try {
			//Page.pause(3);
			// 是否登录
			op.loopGet(LoginURL, 50, 3, 40);
			if (op.isElementPresent("loginPage_signInBtn")) {
				Log.logInfo("没有用户登录，正常执行流程");
			} else {
				Log.logInfo("是登录状态，执行退出操作");
				ControlUsingMethod.SetScrollBar(600);
				op.loopClickElement("LoGout", 2, 10, 30);
			}
			interfaceMethod.crearAllCartGoods(myPublicUrl, ppaccount);// 清空购物车
			op.loopGet(productUrl, 50, 3, 60);// 得到商品地址
			Log.logInfo("获取的URL地址为：" + productUrl);
			ControlUsingMethod.SetScrollBar(500);
			try {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.id("addCart"))).click();// 加入到购物车
				Log.logInfo("成功进入支付成功页面!!!");
			} catch (Exception e) {
				Log.logError("控件没有获取成功，执行失败，以下内容不执行");
			}
			try {
				op.actionSingleClick("PlaceYourOrderBtn");
			} catch (Exception e) {
				Log.logInfo("页面超过60s还没有加载完成，刷新页面");
				driver.navigate().refresh();
			}
			getPayPayment("quickPayment");
			op.loopGet(PersonalInformation, 40, 3, 60);
			String getPPName = op.loopGetElementValue("PPlogin", "value", 3, 20);
			if (getPPName.equals(ppaccount)) {
				Log.logInfo("PP未登录支付，支付成功后显示的PP支付账号，验证通过，PP支付账号为:" + getPPName);
			} else {
				Log.logError("PP未登录支付，支付成功后显示的PP支付账号，验证不通过，PP实际支付账号为:" + ppaccount + "获取的账号为" + getPPName);
			}
			Pub.printTestCaseExceptionInfo(info);
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
		
   }*/
   
	/**
	 * 
	 * @测试点: 登录
	 * @验证点: 初始化在每个类中都开始使用 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月22日
	 * @修改说明
	 */
	public void login() {
		String requestId = Pub.getRandomString(5);
		try {
			
			String logincode = InterfaceMethod.IF_Verification(driver, myPublicUrl2, requestId, getkeys);
			op.loopSendKeys("loginPage_loginEmail", LoginName, 3, 30);
			op.loopSendKeys("loginPage_loginPwd", "123456", 3, 30);
			if (op.isElementPresent("loginCode")) {
				op.loopSendKeys("loginCode", logincode, 3, 15);
			} else {
				Log.logInfo("未开启验证码输入框，不进行输入");
			}
			op.loopClickElement("loginPage_signInBtn", 10, 30, 100);
			driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
			String getHomeText = op.loopGetElementText("loginHomePage", 15, 60);
			if (getHomeText.equals("Welcome")) {
				Log.logInfo("登录成功");
			} else {
				Log.logError("登录失败，其他内容不执行");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * 
	* @测试点:PP支付以及快捷支付
	* @验证点:  处理订单信心内容，以及PP支付和快捷支付时的一些方式
	* @param type    传入的类型  quickPayment：快捷支付   Payment：普通支付，
	* @使用环境： 测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月9日 
	  @修改说明
	 */
	public void getPayPayment(String type){	 
		String code;
		// 显示等待页面元素出现
		try {
			if (op.isElementPresent("officiaLoginBtn",60)) {//
				Log.logInfo("外网支付，存在登录PP账号前的一个控件，点击");
				op.loopClickElement("officiaLoginBtn", 10, 20, 30);
			}
			Page.pause(5);
			String getUrl=driver.getCurrentUrl();
			//if (op.isElementPresent("officialPayCard",30) || op.isElementPresent("officialsignupBtn",30)|| op.isElementPresent("officialppContinue",30)) {// 测试环境没有的一个控件
			if(getUrl.contains(url)){	
			    Log.logInfo("使用的外网支付");
				if (op.isElementPresent("officialppContinue",20)) {// 为了防止第一次支付成功后进入的继续支付。第二次支付，直接进入到继续支付页面
					Log.logInfo("进入了继续支付页面，前一个操作已经登录，不用进行登录。继续下一步!!!");
					op.actionSingleClick("officialppContinue");
					//Page.pause(4);
					//会遇到登录超时的问题，所以又要重新登录
					if(op.isElementPresent("officialLoginName",20)){
						Log.logError("登录状态的session丢失，不知道会出现多少这样的问题，先暂时不做处理");
					}
				} else {
					Log.logInfo("进入到了输入用户名密码的输入框页面!!!");
					if(op.isElementPresent("iframes",30)){
						Log.logInfo("登录PP账号是存在iframe中，切换到iframe中");
						driver.switchTo().frame("injectedUl");// iframe 的name 定位
						/*op.loopSendKeysCheck("officialLoginName", ppaccount, 5, 10);*/
						
						op.loopSendKeysClean("officialLoginName", ppaccount, 5);
						WebElement testorder=op.MyWebDriverWait2("officialLoginPassword", 10, false);
						Operate.sendkey(ppaccount,testorder);
						//pp.loopSendPwd(op, projectName, "officialLoginPassword", paypal_password, 3, 30);
						//op.actionSingleClick("officialLgoinBtn");
						op.loopClickGoPageURL("officialLgoinBtn", 10, 40, 60, 50);
						
						driver.switchTo().defaultContent();
					}else{
						Log.logInfo("PP账号登录框，没有发现iframe，这个问题很奇怪，先截图后查看");
						/*op.loopSendKeysCheck("officialLoginName", ppaccount, 5, 10);
						pp.loopSendPwd(op, projectName, "officialLoginPassword", paypal_password, 3, 30);*/
						//op.actionSingleClick("officialLgoinBtn");
						op.loopSendKeysClean("officialLoginName", ppaccount, 5);
						WebElement testorder=op.MyWebDriverWait2("officialLoginPassword", 10, false);
						Operate.sendkey(ppaccount,testorder);
						op.loopClickGoPageURL("officialLgoinBtn", 10,  40, 60, 50);
					}
					
					/*Page.pause(5);*/
					if (op.isElementPresent("officialppContinue",20)) {	
						Log.logInfo("成功进入继续支付页面!!!");
						try {
							op.loopClickGoPageURL("officialppContinue", 5,  50, 70, 40);
						} catch (Exception e) {
							e.printStackTrace();
						   Log.logInfo("点击继续支付页面，页面超时没有响应，在此刷新页面");
						   
						}						
						//Page.pause(4);
					} else {
						Log.logError("进入继续支付页面失败,以下内容不执行!!!");
					}
				}
				
			} else {
				Log.logInfo("使用的测试环境支付");
				op.loopSendClean("ppName", 3, 15);
				sendKeys("ppName", ppaccount);
				sendKeys("ppPwd", paypal_password);
				op.actionSingleClick("ppLogin");
				//Page.pause(4);
				if (op.isElementPresent("ppContinue",20)) {
					Log.logInfo("成功进入继续支付页面!!!");
					op.actionSingleClick("ppContinue");
					Page.pause(4);
				} else {
					Log.logError("进入继续支付页面失败,以下内容不执行!!!");
				}
			}
			
			
			if (type.equals("quickPayment")) {
				Log.logInfo("使用的是快捷支付");
				Map<String, String[]> balanceMap = getBalancePageInfo();
				balanceSum = balanceMap.get("sum");
				balanceAddress = balanceMap.get("adress");
				//Page.pause(3);
				try {
				     op.navigateRefresh(20, 2, 60);
					if (op.isElementPresent("Point")) { // 不知道为何会使用积分减掉商品价格，所以加上这句代码
						Log.logInfo("存在积分输入框，把能使用的积分设置为0");
						op.loopSendClean("Point", 3, 10);
						op.loopClickElement("subTotal", 2, 5, 30);
					} else {
						Log.logError("不存在积分输入框，暂时没做处理");
					}
					op.loopClickGoPageURL("PlaceYourOrderBtn", 10, 60, 40, 40);
					//Page.pause(4);
				} catch (Exception e) {
					Log.logInfo("点击提交按钮，页面超时了,但是页面已经显示了支付成功页，继续执行下一步");
				}
			} else if (type.equals("Payment")) {
				Log.logInfo("使用的是普通支付");
			}
			//Page.pause(5);
  
		//设置一个隐时等待，避免到这个页面超时的问题
		try {			
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#mainWrap > div.msg-page > p")));		
			Log.logInfo("进入支付成功页面,获取成功提示语句!!!");				
		} catch (Exception e) {
			Log.logInfo("执行查找元素，显示等待了25秒，实际页面已经显示了，不影响到下一步，继续执行");
		}
			
		String successMsg1 = op.loopGetElementText("successMsg1", 10, 60);
		Log.logInfo("支付成功提示语为:"+successMsg1);
		
		code= op.loopGetElementText("orderCodeSuccess", 10, 60);
		Log.logInfo("生成订单号为:"+code);
	
		String expSuccessMsg1 = "Success!";			
		if(successMsg1.equals(expSuccessMsg1)){
			Log.logInfo("支付成功页面，成功提示语验证成功！！");
		}else {
			Log.logError("支付成功页面，成功提示语验证失败！！");
		}	
		try {
			driver.manage().timeouts().setScriptTimeout(60,TimeUnit.SECONDS);
			driver.findElement(By.cssSelector("#mainWrap > div.msg-page > a")).click();			
			//点击提示
		} catch (Exception e) {	
			 Log.logInfo("wap版网络原因，实际已经点击了订单页面的编码，但是会提示报错，为了不影响下一步，继续执行");			 
			 Log.logInfo("地址是"+driver.getCurrentUrl());
			 op.loopGet(driver.getCurrentUrl(), 40, 3, 50);

		}
	
		Map<String, String[]> orderInfo = getOrderPageInfo();
		String[] payOrderInfo=orderInfo.get("payOrder");
		String[] payAddressInfo=orderInfo.get("payAddress");
		String[] payToalcostInfo=orderInfo.get("payToalcost");
		

		if(op.isElementPresent("orderNo")){
			Log.logInfo("点击订单编号，进入到订单列表中！！");
			
			if(payOrderInfo[0].equals(code)){
				Log.logInfo("生成的订单的编号与打开的的订单编号一致");
			}
			if(payOrderInfo[1].equals("Paid")&&payOrderInfo[3].equals("PayPal")){
				Log.logInfo("我的订单详情中订单状态正确,订单状态:"+payOrderInfo[1]+",预期为:Paid");
				Log.logInfo("我的订单详情中订单支付方式正确,支付方式为:"+payOrderInfo[3]+",预期为:PayPal");
			}else {
				Log.logError("我的订单详情中订单状态不正确，订单状态:"+payOrderInfo[1]+",预期为:Paid");
				Log.logInfo("我的订单详情中订单支付方式不正确,支付方式为:"+payOrderInfo[3]+",预期为:PayPal");
			}			
			/*地址页面的对比*/
			for(int i = 0; i < payAddressInfo.length; i++){
				if (i>=2) {//主要区分下单地址页面，和支付成功页面的地址，相匹配
					if(balanceAddress[i+1].equals(payAddressInfo[i].replaceAll(" ", ""))){					
						Log.logInfo("我的订单详情中的地址与结算页面地址一致,我的订单中地址为:"+balanceAddress[i+1]+",结算页面地址为:"+payAddressInfo[i]);
					}else{
						Log.logError("我的订单详情中的地址与结算页面地址不一致,我的订单中地址为:"+balanceAddress[i+1]+",结算页面地址为:"+payAddressInfo[i]);
					}
						
				} else {  
					if(balanceAddress[i].equals(payAddressInfo[i])){
						Log.logInfo("我的订单详情中的地址与结算页面地址一致,我的订单中地址为:"+balanceAddress[i]+",结算页面地址为:"+payAddressInfo[i]);
					}else {
						Log.logError("我的订单详情中的地址与结算页面地址不一致,我的订单中地址为:"+balanceAddress[i]+",结算页面地址为:"+payAddressInfo[i]);
					}
				}
				
			}
			/*金额的对比
			for(int i = 0; i < payToalcostInfo.length; i++){
				if(balanceSum[i].equals(payToalcostInfo[i])){
					Log.logInfo("我的订单详情中的金额与结算页面金额一致,我的订单中金额为:"+payToalcostInfo[i]+",结算页面金额为:"+balanceSum[i]);
				}else {
					Log.logError("我的订单详情中的金额与结算页面金额不一致,我的订单中金额为:"+payToalcostInfo[i]+",结算页面金额为:"+balanceSum[i]);
				}
			}
			*/
		}else{
			Log.logError("点击订单编号，没有进入到订单列表中！！");
		}
		
		Pub.printTestCaseExceptionInfo(info);
	} catch (Exception e) {
		e.printStackTrace();
		Log.logWarn(info.testCorrelation);
		Assert.fail();
	}
   }
   
   
   /**
    * 
   * @测试点: 生成订单 ，继续支付， 
   * @验证点: 生成订单，检测继续支付按钮可以点击，并且下单时价格与订单价格一致，状态正确 
   * @使用环境：测试环境，正式环境
   * @param type    类型，PP：pp支付方式  crad：信用卡支付方式
   * @备注： void    
   * @author zhangjun 
   * @date 2017年2月10日 
     @修改说明
    */
   public void getContinuePayment(String type){
	   String getOrder="";//订单
	   boolean ReverseState=false;
		try {
			Map<String, String[]> balanceMap = getBalancePageInfo();
			balanceSum = balanceMap.get("sum");
			balanceAddress = balanceMap.get("adress");
			// 信用卡支付
			op.actionSingleClick("PlaceYourOrderBtn");// 点击进入pp支付页面'
			if (type.equals("card")) {//区分PP或者是信用卡支付
				if (op.isElementPresent("cardPayContiue")) {
					Log.logInfo("进入到继续支付页面，生成订单成功，继续执行");
				} else {
					Log.logError("没有进入到继续支付页面，生成订单失败");
				}
			} else if (type.equals("pp")) {
				int i=0;
				String url=driver.getCurrentUrl();
				while (i<13){		
					if(url.contains(".paypal")){
						Page.pause(1);
						ReverseState=true;
						break;
					}
					i++;
				}
				if(ReverseState==true){
					Log.logInfo("正常跳转到支付成功页，验证通过");
				}else{
					Log.logError("没有跳转到支付成功页，验证失败");
				}
				
			}
			
			Page.pause(3);
			// 转到订单列表
			op.loopGet(OrderList, 50, 3, 60);
			if (baseURL.contains(".a.")) {//外网与内网控件的路径不同，获取订单
				getOrder=op.loopGetElementText("Order", 10, 30);
				Log.logInfo("生成的订单号为："+getOrder);
				op.loopClickElement("Order", 10, 15, 50);
			} else {
				getOrder=op.loopGetElementText("officiaOrder", 10, 30);
				Log.logInfo("生成的订单号为："+getOrder);
				op.loopClickElement("officiaOrder", 10, 15, 50);
			}	
			String orderDeliverise = op.loopGetElementText("Deliverise", 6, 30);
			System.out.println(orderDeliverise);
			String addresListTotal = op.loopGetElementText("addresListTotal", 10, 50);
			String orderStaus = op.loopGetElementText("orderStaus", 3, 20);
			boolean totalbool = addresListTotal.equals(balanceSum[4]);
			boolean orderStausbool = orderStaus.equals("Waiting for payment");
			System.out.println("总价正确:" + totalbool);
			System.out.println("状态正确" + orderStausbool);
			if (totalbool && orderStausbool) {
				Log.logInfo("订单列表中总价与结算时一致, 总价为:" + addresListTotal + "且状态也正确,状态为:" + orderStaus + "验证通过");
			} else {
				Log.logError( "订单列表中总价与结算时不一致,实际获取总价为:" + addresListTotal + "且状态验证不通过,实际获取的内容为" + orderStaus + "验证不通过");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
   }
	/**
	 * 
	* @测试点:  删除所有地址 
	* @验证点: 删除所有地址
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月23日 
	  @修改说明
	 */
	public void DeleteAdressAll() {
		try {
			List<WebElement> elements = op.getElements("addressList");
			Log.logInfo("删除前地址栏的地址个数：" + elements.size());
			if (elements.size() <= 0) {
				Log.logError("没有一个地址,剩余内容不执行");
			} else {
				Log.logInfo("目前地址个数为:" + elements.size());
				for (int i = 0; i < elements.size(); i++) {
					Page.pause(3);
					op.loopClickElement("delectBtn", 3, 10, 50);																		
					op.loopClickElement("delete_yes", 3, 10, 30);
								
				}
			}
		//	Page.pause(3);	
			if (op.isElementPresent("firstname",60)) {
				Log.logInfo("删除全部地址成功，目前已经到了地址新增页面，验证通过");
			} else {
				Log.logError("删除全部地址不成功，目前还剩余地址" + elements.size());
			}
		}catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * 
		 * @测试点: 设置各个字段的值,并且保存地址
		 * @验证点: 无
		 * @使用环境： @return 测试环境，正式环境 
		 * @备注： 
		 * @author zhangjun
		 * @date 2017年1月20日
		 * @修改说明
		 */
		public void setAllFieldVaule() {
			try {
				op.loopSendKeys("firstname", "jun", 3, 20);
				op.loopSendKeys("lastname", "zhang", 3, 20);
				op.loopSendKeys("email", "jun@qq.com", 3, 20);
				op.loopSendKeys("addressline1", "shengzheng", 3, 20);
				ControlUsingMethod.SetScrollBar(400);
				op.getSelect("CountryRegion", 10).selectByVisibleText("France");
				Page.pause(1);
				op.getSelect("province", 10).selectByIndex(1);
				op.loopSendKeys("city", "shenzhen", 3, 20);
				op.loopSendKeys("tel", "028456789", 3, 20);
				op.loopSendKeys("zipcode", "123456", 3, 20);			
				op.loopClickElement("saveBtn", 15, 60, 70);// 点击保存
				Log.logInfo("新增地址成功");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
	
		
		/**
		 * 
		* @测试点: 不同的运输方式 
		* @验证点: 运输方式的不同，下单成功后对比运输方式是否相同，价格是否相同 
		* @使用环境：     测试环境，正式环境
		* @备注： void    
		* @author zhangjun 
		* @date 2017年2月3日 
		  @修改说明
		 */
	public void getTransportationMode(String type, String shippingMethod) {
		String getShiipingPrice="";
		String getOrder="";//订单号
		Double total = null;
		String	actualTotal="";
		Map<String, String[]> balanceMap = getBalancePageInfo();
		String[] balanceSum = balanceMap.get("sum");
		Double ShippingTotal;
		
		try {	
			
			// 跟踪码,运输方式为：Flat Rate Shipping
			if (type.equals("traking_number")) {//平邮—+跟踪码
				total = Double.parseDouble(balanceSum[0])+Double.parseDouble(balanceSum[1])+Double.parseDouble(balanceSum[2])+Double.parseDouble(balanceSum[3]);
				Log.logInfo("选择了跟踪码计算的结果为：" + total);
				Page.pause(2);			 
				actualTotal =op.loopGetElementText("sumTotal", 10, 20).replace("$", "");
				Log.logInfo("实际获取的页面的总价格为 :" + actualTotal);
				if (df.format(total).equals(actualTotal)) {
					Log.logInfo("勾选了跟踪码，计算的总价与页面的价格相同，验证通过，总价为："+actualTotal);
					balanceSum[4]=actualTotal; //相同了后重新给总价赋值,方便在订单列表中计算
				} else {
					Log.logError("勾线了跟踪码，价格计算错误,验证不通过，页面的价格为:"+actualTotal+"计算的价格为:"+total);
				}
				
			}else if(type.equals("Standard")){//标准邮方式
			   ShippingTotal=Double.parseDouble(balanceSum[0])+Double.parseDouble(balanceSum[1])+Double.parseDouble(balanceSum[3]);	
			  
				actualTotal = op.loopGetElementText("sumTotal", 10, 20).replace("$", "");
				if(df.format(ShippingTotal).equals(actualTotal)){
					Log.logInfo("选择的方式是标准邮，计算的总价与页面的价格相同，验证通过，总价为："+df.format(ShippingTotal));
					balanceSum[4]=actualTotal; //相同了后重新给总价赋值,方便在订单列表中计算
				}else{
					Log.logError("选择的方式是标准邮，计算的总价与页面的价格不相同，验证不通过，页面的价格为:"+balanceSum[4]+"计算的价格为:"+df.format(ShippingTotal));
				}
				
			}else if(type.equals("Expedited")){//快递方式
			   ShippingTotal=Double.parseDouble(balanceSum[0])+Double.parseDouble(balanceSum[1])+Double.parseDouble(balanceSum[3]);				
				actualTotal = op.loopGetElementText("sumTotal", 10, 20).replace("$", "");
		
				if(df.format(ShippingTotal).equals(actualTotal)){
					Log.logInfo("选择的方式是快递方式，计算的总价与页面的价格相同，验证通过，总价为："+df.format(ShippingTotal));
					balanceSum[4]=actualTotal; //相同了后重新给总价赋值,方便在订单列表中计算
				}else{
					Log.logError("选择的方式是快递方式，计算的总价与页面的价格不相同，验证不通过，页面的价格为:"+balanceSum[4]+"计算的价格为:"+df.format(ShippingTotal));
				}
				
			}else if(type.equals("not_insurance")){//不选择保险费	
				if (baseURL.contains(".a.")) {//外网与内网控件的路径不同
					getShiipingPrice=op.loopGetElementText("shippingInsurancePrice", 10, 30);
				} else {
					getShiipingPrice=op.loopGetElementText("officialShippingInsurancePrice", 10, 30);
				}				
				actualTotal = op.loopGetElementValue("sumTotal", "data-orgp", 80);

				if(balanceSum[3].equals("0.00")){
					Log.logInfo("去掉保险费，计算的总价与页面的价格相同，验证通过，去掉保险费后总价为："+actualTotal+"保险费的价格为:"+getShiipingPrice);
					balanceSum[4]=actualTotal; //相同了后重新给总价赋值,方便在订单列表中计算
				}else{
					Log.logError("去掉保险费，计算的总价与页面的价格不相同，验证不通过，页面的价格为:"+balanceSum[4]+"实际获取页面的总价为:"+actualTotal);
				}
				
			}else if(type.equals("insurance")){//已选择保险，计算保险费
				Double InsuranceTotal=Double.parseDouble(balanceSum[0])*0.02+1;
				if(df.format(InsuranceTotal).equals(balanceSum[3])){
					Log.logInfo("计算保险费，计算的总价与页面的价格相同，验证通过，计算的保险费价格为："+df.format(InsuranceTotal));
				}else{
					Log.logError("计算保险费，计算的总价与页面的价格不相同，验证不通过，页面的价格为:"+balanceSum[4]+"计算的保费为:"+InsuranceTotal);
				}
			}
			
			Page.pause(2);		
			//生成订单，在订单列表中查询
			op.actionSingleClick("PlaceYourOrderBtn");
			 String url = null;
			for(int i=0;i<10;i++){
				url=driver.getCurrentUrl();
				Page.pause(2);
				if(baseURL.contains(".paypal.com")){
					break;
				}
			}
		     Log.logInfo("实际进入到页面获取的地址是:"+url);
		     if(url.contains(".paypal.com")){//跳转到支付页面，通过判断地址中是否包含Paypal，不用控件的方式判断，因为控件的方式太慢
		         Log.logInfo("跳转到支付页面，已生成订单，查看订单");
		         Page.pause(3);	         
				op.loopGet(OrderList, 40, 3, 60);
				
				if (baseURL.contains(".a.")) {//外网与内网控件的路径不同
					getOrder=op.loopGetElementText("Order", 10, 30);
					Log.logInfo("生成的订单号为："+getOrder);
					op.loopClickElement("Order", 10, 15, 50);
				} else {
					getOrder=op.loopGetElementText("officiaOrder", 10, 30);
					Log.logInfo("生成的订单号为："+getOrder);
					op.loopClickElement("officiaOrder", 10, 15, 50);
				}	
				String orderDeliverise = op.loopGetElementText("Deliverise", 6, 30);
				
				String addresListTotal = op.loopGetElementText("addresListTotal", 10, 50);
				boolean totalbool = Double.parseDouble(addresListTotal)==Double.parseDouble(balanceSum[4]);
				boolean transportationbool = orderDeliverise.equals(shippingMethod);
				System.out.println("总价正确:"+totalbool);
				System.out.println("物流方式正确"+transportationbool);
				if (totalbool && transportationbool) {
					Log.logInfo("订单列表中总价与结算时一致, 总价为:"+addresListTotal+"且选择的运输的方式也一致,运输方式为:"+orderDeliverise+"验证通过");
				} else {
					Log.logError("订单列表中总价与结算时不一致,实际获取总价为:" + addresListTotal+"预先存入的价格为:"+balanceSum[4] + "选择的运输的方式验证不通过,实际获取的内容为" + orderDeliverise+"原本选择的是:"+shippingMethod+ "验证不通过");
				}

			} else {
				Log.logError("没有生成订单，后面内容不执行");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
		
	/**
	 * 
	* @测试点: 获取订单信息 
	* @验证点: 获取订单中的订单部分信息
	* @param code 订单编号
	* @使用环境： @return    测试环境，正式环境
	* @备注： orderDetail[0]:订单编号      orderDetail[1]：订单状态  orderDetail[2]:订单日期（没用） orderDetail[3]:支付方式  orderDetail[4]:邮递方式    
	* @author zhangjun 
	* @date 2017年1月24日 
	  @修改说明
	 */
		 
	public HashMap<String, String[]> getOrderPageInfo() {
		HashMap<String , String[]>  pageInfo=new HashMap<String ,String[]>();
		try {
			
			//订单中订单信息**********************************************************");
			Log.logInfo("页面地址需要刷新");
			Log.logInfo("地址为:"+driver.getCurrentUrl());
			op.loopGet(driver.getCurrentUrl(), 40, 3, 60);
			List<WebElement> orderDetails = op.getElements("orderDetailList");
			String[] orderDetail  = new String[orderDetails.size()];
		
			for (int i = 0; i < orderDetails.size(); i++) {
				orderDetail[i] = orderDetails.get(i).findElement(By.cssSelector("span")).getText();
			}
			
			//("订单中地址信息**********************************************************");
			List<WebElement> shippingaddress = op.getElements("orderShippingAddress");
			String[] shippingaddres  = new String[shippingaddress.size()];
			for (int i = 0; i < shippingaddress.size(); i++) {				
					shippingaddres[i] = shippingaddress.get(i).findElement(By.cssSelector("span")).getText();				    
					if (i==4){
						String  oldAddress=shippingaddres[3];				
						Log.logInfo("支付页面中原本的地址："+oldAddress);
						Log.logInfo("截图最后一个字段："+shippingaddres[2]);
						Log.logInfo("第3列的地址："+shippingaddres[3]);
						shippingaddres[3]=shippingaddres[2].split(",")[2].replace(" ", "");//与第2个地址和第3个地址进行对换		
						Log.logInfo("交换位置重新生成的地址："+shippingaddres[3]);
						shippingaddres[2]=shippingaddres[2].replace(shippingaddres[2].split(",")[2], oldAddress);							
						Log.logInfo("重新生成的地址"+shippingaddres[2]);
				    }				
			}			
			//("订单中金额信息**********************************************************");
			List<WebElement> ToalCosts = op.getElements("orderTotalCost");
			 String[] ToalCost = new String[ToalCosts.size()];				
				for (int i = 0; i < ToalCosts.size(); i++) {			
					ToalCost[i] = ToalCosts.get(i).findElement(By.cssSelector("strong.price > span.my_shop_price")).getAttribute("orgp");
				}		
		
			pageInfo.put("payOrder", orderDetail);//订单列表信息
			pageInfo.put("payAddress", shippingaddres);//订单列表地址页面
			pageInfo.put("payToalcost", ToalCost);//订单支付金额
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return pageInfo;
	}
	
	
	/**
	 * 
	* @测试点: 订单列表中获取订单价格 
	* @验证点: 获取订单列表中价格
	* @使用环境： @return    测试环境，正式环境
	* @备注： String[]    
	* @author zhangjun 
	* @date 2017年1月24日 
	  @修改说明
	 */
	public String[] getOrderToalCost(){
		String[] ToalCost = null;
		try {
			ToalCost = new String[6];
			List<WebElement> ToalCosts = op.getElements("orderTotalCost");
			for (int i = 0; i < ToalCosts.size(); i++) {			
				ToalCost[i] = ToalCosts.get(i).findElement(By.cssSelector("strong.price > span.my_shop_price")).getAttribute("orgp");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ToalCost;
		
	}
		
	/**
	 * 控件无法获取到焦点无法输入值情况下,使用该方法
	 * @param identification:yaml对象属性文件中的标识
	 * @param value:输入的值
	 * @throws Exception 
	 */
	public void sendKeys(String identification,String value) throws Exception{

		Actions actions = new Actions(driver);
		actions.moveToElement(op.findElementAssert(identification));
		actions.click();
		actions.sendKeys(value);
		actions.build().perform();
	}	
	/**
	 * 
	* @测试点: 添加商品到购物车 
	* @验证点: 获取商品，并且添加到购物车
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月23日 
	  @修改说明
	 */
	public void Addshoping(){
		try {
			interfaceMethod.crearAllCartGoods(myPublicUrl, LoginName);// 清空购物车
			try {
				op.loopGet(productUrl, 40, 3, 60);// 得到商品地址
				Log.logInfo("获取的商品地址为：" + productUrl);
			} catch (Exception e) {
				e.printStackTrace();
				Log.logInfo("实际是已经加载成功了");
			}
			ControlUsingMethod.SetScrollBar(650);
			try {
				Log.logInfo("点击添加到购物车");
				op.loopClickGoPageURL("addcart", 5, 20, 40, 60);
			} catch (Exception e) {
				Log.logInfo("实际页面地址已经开始加载了");
			}

			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#js_payBtn > a.btn.btn-db.btn-info.ttu.fb.f18"))).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 
	* @测试点: 获取页面信息
	* @验证点: 获取结算页面，地址，金额
	* @使用环境： @return    测试环境，正式环境
	* @备注： Map<String,String[]>    
	* @author zhangjun 
	* @date 2017年1月24日 
	  @修改说明
	 */
	public Map<String, String[]> getBalancePageInfo(){
		Map<String, String[]> balanceMap = null;
		try {
			
		Log.logInfo("地址处理**********************************************************");
		List<WebElement> blAddress =op.getElements("blAddress");
		String[] adressList = new String[blAddress.size()];
		for(int i=0;i<blAddress.size();i++){
			Log.logInfo("地址信息"+blAddress.get(i).getText());
			if (i==3){
				adressList[3] =blAddress.get(3).findElement(By.cssSelector("span")).getText().replace(" ", "");
			}else{
				adressList[i] =blAddress.get(i).findElement(By.cssSelector("span")).getText();
			}
		}
		Log.logInfo("汇总金额处理**********************************************************");
		String[] sum = new String[5];
		if (op.isElementPresent("Point")){
			Log.logInfo("存在积分输入框，把能使用的积分设置为0");
			op.loopSendClean("Point", 3, 10);
			op.loopClickElement("subTotal", 2, 5, 30);
		}else{
			Log.logInfo("不存在积分输入框，暂时没做处理");
		}
		//某些商品获取使用value方式可能会少一个0，更改获取方式
		//sum[0] =op.loopGetElementValue("subTotal", "orgp", 10, 40);
		sum[0] =op.loopGetElementText("subTotal", 5, 20).replace("$", "");
		sum[1] =op.loopGetElementText("postTotal", 5, 20).replace("$", "");
		sum[2] =op.loopGetElementText("tracking", 5, 20).replace("$", "");
		sum[3] =op.loopGetElementText("insurance", 5, 20).replace("$", "");
		//sum[3] =op.loopGetElementValue("insurance", "orgp", 10, 40);  		
		//有款商品，获取value后，显示为5.2700000000000005。更改获取方式sum[4] =op.loopGetElementValue("sumTotal", "orgp", 10, 40);
		sum[4] =op.loopGetElementText("sumTotal", 10, 40).replace("$", "");
		
		
		for(String ssString:sum){
 			Log.logInfo("获取的支付页面的信息"+ssString);
		}
		
		balanceMap= new HashMap<String, String[]>();
		balanceMap.put("adress", adressList);//地址信息
		balanceMap.put("sum", sum);//汇总金额统计	
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return balanceMap;
	}
	
	/**
	 * 
	* @测试点: 结算页面的支付方式 
	* @验证点:  结算页面的支付方式  
	* @使用环境： @return    测试环境，正式环境
	* @备注： String[]    
	* @author zhangjun 
	 * @throws Exception 
	* @date 2017年2月3日 
	  @修改说明
	 */
	public String[] getPayType() throws Exception{			
		List<WebElement> payList = op.getElements("blpayTypeList");
		String[] payTyleList = new String[payList.size()];
		for(int i=0;i<payList.size();i++){
			payTyleList[i] = payList.get(i).findElement(By.cssSelector("input[name = 'payment']")).getAttribute("value");
			Log.logInfo("获取的支付方式 有 :"+payList.get(i).findElement(By.cssSelector("input[name = 'payment']")).getAttribute("value"));
		}
		return payTyleList;
	}
	
	
	
	/**
	 * 
	* @测试点: 删除取消订单 
	* @验证点: 删除取消的订单
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月10日 
	  @修改说明
	 */
	public  void DelectCancelOrder(){
		String requestId = Pub.getRandomString(5);
		try {			
			Page.pause(3);
			// 是否登录
			int j = 1;
			op.loopGet(OrderListURL, 90, 3, 80);
			List<WebElement> listCancel = op.getElements("cancelListOrder");
			Log.logInfo("获取到的个数为:"+listCancel.size());
			Log.logInfo("开始执行取消操作......");
			while (j <6) {				
				for (int i = 1; i < listCancel.size(); i++) {
					String orderStatus = listCancel.get(i).findElement(By.cssSelector(" div.tit > p:nth-child(2) > a")).getText().replace(">", "").trim();// 订单状态
					if (orderStatus.contains("Waiting for payment")) {
						listCancel.get(i).findElement(By.cssSelector(" div.action-order > a.btn.btn-default.ttu.f14.js_cancel")).click();
						op.loopClickElement("cancelYes", 3, 5, 20);// 确认取消按钮
						Log.logInfo("取消订单成功");
						Page.pause(2);
						listCancel = op.getElements("cancelListOrder");
					} else {
						Log.logInfo("是其他状态,不用处理,获取的状态为:"+orderStatus);
					}
				}			
			String NewsAddress = OrderListURL + "?page=" + (++j);
			op.loopGet(NewsAddress, 40, 3, 80);
			Page.pause(2);
			listCancel = op.getElements("cancelListOrder");
			}
			interfaceMethod.deleteCancelOrder(myPublicUrl,"autotest19@globalegrow.com");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	* @测试点: 获取一些固定地址 
	* @验证点: 获取一些地址
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月13日 
	  @修改说明
	 */
	public void urlInit() {
		
		if (baseURL.contains(".a.")) {
			LoginURL = "http://m.wap-rosewholesale.com.a.s1cg.egomsl.com/user/index/userindex";
			AddressList = "http://m.wap-rosewholesale.com.a.s1cg.egomsl.com/user/address/list";
			myPublicUrl = "http://rosewholesale.com.d.s1.egomsl.com/";// 调用的和PC接口一样
			myPublicUrl2="http://m.wap-rosewholesale.com.a.s1cg.egomsl.com";//获取验证码的接口
			OrderListURL="http://m.wap-rosewholesale.com.a.s1cg.egomsl.com/user/order/list";
			String getProduct=interfaceMethod.getGoodUrl(myPublicUrl,"min");
			SetConvertAddress(getProduct);
			OrderList="http://m.wap-rosewholesale.com.a.s1cg.egomsl.com/user/order/list";
			PersonalInformation="http://m.wap-rosewholesale.com.a.s1cg.egomsl.com/user/index/account-setting";
			url="https://www.sandbox.paypal.com";
			
		} else if(baseURL.contains(".trunk.")) {
			LoginURL = "https://m.wap-rosewholesale.com.trunk.s1cg.egomsl.com/user/index/userindex";
			AddressList = "https://user.wap-rosewholesale.com.trunk.s1cg.egomsl.com/user/address/list";
			myPublicUrl = "http://rosewholesale.com.trunk.s1.egomsl.com/";// 调用的和PC接口一样
			myPublicUrl2="http://www.wap-rosewholesale.com.trunk.s1cg.egomsl.com";//获取验证码的接口
			OrderListURL="https://user.wap-rosewholesale.com.trunk.s1cg.egomsl.com/user/order/list";
			String getProduct=interfaceMethod.getGoodUrl(myPublicUrl,"min");
			SetConvertAddress(getProduct);
			OrderList="https://m.wap-rosewholesale.com.trunk.s1cg.egomsl.com/user/order/list";
			PersonalInformation="https://m.wap-rosewholesale.com.trunk.s1cg.egomsl.com/user/index/account-setting";
		}else {
			LoginURL = "https://loginm.rosewholesale.com/user/index/userindex";
			AddressList = "https://userm.rosewholesale.com/user/address/list";
			myPublicUrl = "https://rosewholesale.com/";// 调用的和PC接口一样
			myPublicUrl2="https://m.rosewholesale.com";//获取验证码的接口
			OrderListURL="https://userm.rosewholesale.com/user/order/list";
			String getProduct=interfaceMethod.getGoodUrl(myPublicUrl,"min");
			SetConvertAddress(getProduct);
			OrderList="https://userm.rosewholesale.com/user/order/list";
			PersonalInformation="https://m.rosewholesale.com/user/index/account-setting";
			url="https://www.paypal.com/";
			
		}
	}
	
	/**
	 * 
	* @测试点: 转换地址，
	* @验证点: 把取得的PC商品地址转换成wap地址 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月15日 
	  @修改说明 
	 */
	public String SetConvertAddress(String ProductUrl){
		/* 正则表达取出uid */
		Pattern p = Pattern.compile("\\d{5,7}");	
		Matcher m = p.matcher(ProductUrl);// 得到商品地址，目前还是得到的PC地址
		if (m.find()) {
			productUrl =myPublicUrl2+"/cheapest/stylish-round-neck-long-sleeve-"+ m.group()+".html";  
			Log.logInfo("取得的wap商品地址为："+productUrl);
		}
		return productUrl;		
	}
	
	
	public String shippingMethod() {
		String shippingMethods = null;
		try {
			interfaceMethod.crearAllCartGoods(myPublicUrl, LoginName);// 清空购物车
			Map<String, Object> getproduct = interfaceMethod.getGoods(myPublicUrl, "n", "n");
			String getproducturl = (String) getproduct.get("url_title");
			shippingMethods = (String) getproduct.get("goods_shipping_method");
			getproducturl = getproducturl + "?" + timeStamp;// 刷新缓存'
			Log.logInfo("获取的测试商品地址为：" + getproducturl + "?" + timeStamp);

			op.loopGet(SetConvertAddress(getproducturl), 50, 3, 80);
			ControlUsingMethod.SetScrollBar(500);
			Log.logInfo("点击添加到购物车");
			op.loopClickGoPageURL("addcart", 5, 20, 30, 60);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#js_payBtn > a.btn.btn-db.btn-info.ttu.fb.f18"))).click();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return shippingMethods;
	}

}
