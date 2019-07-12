package com.globalegrow.rosewholesalea;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.aspectj.weaver.AjAttribute.PrivilegedAttribute;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.globalegrow.autotest_encrypt.Operate;
import com.globalegrow.base.StartApp;
import com.globalegrow.code.Page;
import com.globalegrow.custom.InterfaceMethod;
import com.globalegrow.util.Log;

import com.globalegrow.util.Pub;
import com.globalegrow.util.testInfo;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidKeyCode;

public class RW_A_shopping extends StartApp {
	private String className = GetClassName();
	private String testCaseProjectName = "rosewholesale";
	private testInfo info = null;
	public String baseUrl;
	private String myPublicUrl = "";
	private long setWaitTimeout = 20;
	private String getPice;//获取的总价格
	private String getPayStates;//获取订单状态
	private String getOrderNo;//获取订单号
	private String  getpayTotal;//获取的支付总价
	DecimalFormat df = new DecimalFormat("#0.00");
	InterfaceMethod interfaceMethod;
	String[] productPrice;

	@Parameters({ "testUrl" })
	public RW_A_shopping(String testurl) {
		moduleName = className.substring(className.lastIndexOf(".") + 1);
		projectName = className.substring(className.indexOf(testCaseProjectName), className.lastIndexOf("."));
		baseUrl = testurl;
		if (baseUrl.contains("trunk")) {
			myPublicUrl = "http://www.rosewholesale.com.trunk.s1.egomsl.com/";
		} else {
			myPublicUrl = "https://www.rosewholesale.com/";
		}
	}

	@BeforeClass
	public void beforeClass() {
		String methodName = GetMethodName();
	
		String methodNameFull = moduleName + "." + methodName;
		Log.logInfo("**************Ready to test (" + moduleName + ")!!!**************\n");
		Log.logInfo("(" + methodNameFull + ")...beforeClass start...");

		try {
			info = new testInfo(moduleName);
			start();
			opApp.setScreenShotPath(screenShotPath);
			interfaceMethod = new InterfaceMethod();
			login();// 正常执行登录

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
	 * 每个测试用例执行完成时，延时1秒并打印相关信息
	 * 
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
	 * @测试点: 登录 @备注： void
	 * @author zhangjun
	 * @date 2017年6月15日
	 * @修改说明
	 */
	public void login() {
		correFail = false;
		interfaceMethod.crearAllCartGoods(myPublicUrl, "autotest18@globalegrow.com");// 清除购物车
		
		opApp.ClickElement("addressAccount", setWaitTimeout);
		opApp.sendKeys("loginName", "autotest18@globalegrow.com");
		opApp.sendKeys("logingPassword", "123456");
		opApp.ClickElement("loginSign", setWaitTimeout);
		boolean account = opApp.elementExists(20, "addressAccount");
		Assert.assertTrue(account);
	}
	
	
	/**
	 * 
	* @测试点: 1.选择商品，加入购物车，在购物车点击Secure Checkout，
				2.弹出提示框，点击提示框中的Edit进入物流地址页，
 
	* @验证点:1.删除已有的地址，点击某一个商品。添加到购物车
				2.购物车中点击Secure Checkout，弹出提示框，点击Edit，跳转到新增地址页面
				3.填写所有内容，新增地址成功
				4.跳转到支付确认页面
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年6月24日 
	  @修改说明
	 */
	@Test
	public void not_address(){
		correFail = false;
		
		opApp.ClickElement("addressAccount", setWaitTimeout);//account
		opApp.swipe(0.5, 0.6, 0.5, 0.3, 1000);
		opApp.ClickElement("adddressBook", setWaitTimeout);
		
		if(opApp.elementExists(5, "Address")){
			opApp.ClickElement("editAddress", setWaitTimeout);//点击edit
			opApp.ClickElement("deleteAddress", setWaitTimeout);
			opApp.ClickElement("affirmdelete", setWaitTimeout);
		}
		returnHome();
		//先删除地址
		searchSku(1,false);
		
		if(opApp.elementExists(10, "newAddress")){
			opApp.ClickElement("newAddress", setWaitTimeout);
			addAddress();
			// 点击勾选，选择done
			opApp.ClickElement("addressSelection", setWaitTimeout);
			opApp.ClickElement("done", setWaitTimeout);
			Assert.assertTrue(opApp.elementExists(10, "placeSecureOrder"));
			
		}else{
			Log.logError("没有跳转到新增地址页面");
		}
		
		//点击结算
	}
	
	
	/**
	 * 
	* @测试点: 1.在购物车页面点击Secure Checkout进入结算页面，选择信用卡支付方式，
			2.跳转到订单页面，订单已生成 
	* @验证点: 1.进入到信用卡支付页面，生成订单成功
				2.订单页面，订单价格与支付前价格一致 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年6月26日 
	  @修改说明
	 */
	@Test
	public void cardPay(){
		interfaceMethod.crearAllCartGoods(myPublicUrl, "autotest20@globalegrow.com");// 清除购物车
		searchSku(1,false);
		opApp.ClickElement("payCard", setWaitTimeout);//支付方式的更改
		payOrder();
		
	}
	
	/**
	 * 
	* @测试点:1.购物车点击PP支付，不支付，生成订单，
				2.跳转到订单页面，点击继续支付，跳转到支付页面
	* @验证点: 1.生成PP支付订单成功
			2.订单页面，订单状态为waiting for payment
			3.点击继续支付按钮，跳转到支付页面
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年6月26日 
	  @修改说明
	 */
	@Test
	public void continuePay(){
		interfaceMethod.crearAllCartGoods(myPublicUrl, "autotest20@globalegrow.com");// 清除购物车
		returnHome();
		searchSku(1,false);
		payOrder();

	
	}
	
	
	/**
	 * 
	* @测试点: 1.选择1个商品，添加到购物车，数量修改为3，点击Secure Checkout，选择PP支付方式，点击Place secure order，进入第三方支付页面，
			 2.跳转到订单页面， 
	* @验证点: 1.生成订单成功，订单页面支付状态订单状态为waiting for payment
				2.订单价格与支付前价格一致
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年6月27日 
	  @修改说明
	 */
	@Test
	public void multipleShooping(){
		interfaceMethod.crearAllCartGoods(myPublicUrl, "autotest20@globalegrow.com");// 清除购物车
		returnHome();
		searchSku(3,true);
		payOrder();

	}
	
	
	/**
	 * 
	* @测试点: 1.选择3个不同商品，添加到购物车，点击Secure Checkout，选择PP支付方式，点击Place secure order，进入第三方支付页面，2.跳转到订单页面， 
	* @验证点: 1.生成订单成功，订单页面支付状态订单状态为waiting for payment
				2.订单价格与支付前价格一致
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年6月26日 
	  @修改说明
	 */
	@Test
	public void shoppingMany(){
		interfaceMethod.crearAllCartGoods(myPublicUrl, "autotest20@globalegrow.com");// 清除购物车
		returnHome();
		searchSku(3,false);
		payOrder();
	
		
	}
	
	/**
	 * 
	 * @测试点: 通过sku查询,使用的是不清仓，不促销的商品
	 * @验证点:通过sku查询到商品数据，并添加到购物车,跳转到支付订单页面
	 * @使用环境：测试环境，正式环境
	 * @备注： void
	 * @author zhangjun
	 * @date 2017年6月19日
	 * @修改说明
	 */
	
	
	
	/**
	 * 
	* @测试点: 1.选择商品，添加到购物车，进入购物车点击Secure Checkout,弹出用户登入界面进行登入，登入后进入地址页，
			2.选择物流地址，跳转到订单支付页面，
			3.跳转到订单页面 
	* @验证点: 1.未登录点击支付跳转到登录页面
				2.登录成功，购物下单成功
				3生成订单成功，订单状态为waiting for payment
				4.订单价格与支付前价格一致
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年6月27日 
	  @修改说明
	 */
	@Test
	public  void  notLoginPay(){

		interfaceMethod.crearAllCartGoods(myPublicUrl, "autotest20@globalegrow.com");// 清除购物车
		returnHome();
		
		opApp.ClickElement("addressAccount",setWaitTimeout);
		opApp.swipe(0.5, 0.5, 0.5, 0.2, 1000);
		opApp.ClickElement("signOut", setWaitTimeout);
		
		opApp.ClickElement("affirmdelete", setWaitTimeout);//确认
		searchSku(1,false);
		//登录
		opApp.SendKeysClean("loginName", "autotest20@globalegrow.com",setWaitTimeout);
		opApp.SendKeysClean("logingPassword", "123456",setWaitTimeout);
		opApp.ClickElement("loginSign", setWaitTimeout);
		
		if (opApp.elementExists(10, "addressSelection")) {
			opApp.ClickElement("addressSelection", setWaitTimeout);
			opApp.ClickElement("done", setWaitTimeout);
		}
		
		payOrder();//跳转到登录页并且进行支付
	}

	/**
	 * 
	* @测试点: 1.选择一个商品，在购物车页面点击Secure Checkout进入结算页面
				2.选择PP支付方式，点击Place secure order，进入第三方支付页面，输入支付账号和密码进行支付 
	* @验证点: 1.购物成功，进入支付成功页面，支付成功提示、订单号、支付方式，订单金额
			2.在订单列表中能查到该订单，且获取的订单状态是Paid
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年6月26日 
	  @修改说明
	 */
	@Test
	public void payPal() {
		interfaceMethod.crearAllCartGoods(myPublicUrl, "autotest20@globalegrow.com");// 清除购物车
		String keys;
		String password;
		returnHome();
		searchSku(1,false);
		opApp.ClickElement("placeSecure", setWaitTimeout);
		if (baseUrl.contains("trunk")) {
			keys = "441817_1310950685_per@qq.com";
			password="123123123";
		} else {
			keys = "shoppingonline6688@hotmail.com";
			password="egrow$6688968%s";
		}
		opApp.SendKeysClean("ppEmail", keys, 50);
		WebElement orderPassword=opApp.waitForElement("ppPassword", setWaitTimeout);
		orderPassword.sendKeys(password);

		if(opApp.elementExists(10, "ppoldLogin")){
			opApp.ClickElement("ppoldLogin",setWaitTimeout);
		}else{
			opApp.ClickElement("ppLogin", setWaitTimeout);//点击登录
		}
		Page.pause(5);
		if(opApp.elementExists(40, "ppContinue2")){
			opApp.ClickElement("ppContinue2", setWaitTimeout);
		}else{
			opApp.ClickElement("ppContinue", 50);// 继续支付
		}

		opApp.ClickElement("affirmdelete", setWaitTimeout);

		String getmessage = opApp.GetElementText("paySuccessfullyMes", setWaitTimeout, 10);
		Log.logInfo("获取成功的支付为:" + getmessage);
		
		Assert.assertTrue(getmessage.contains("Your order has been submitted successfully"));
		
		String getOrderNo = opApp.GetElementText("payOrder", setWaitTimeout, 10);// 获取的订单编号
		Log.logInfo("获取的订单号为:"+getOrderNo);
		String getPayPrice = opApp.GetElementText("payPrice", setWaitTimeout, 10).replace("USD", "").trim();// 获取总价
		String getPayMethod = opApp.GetElementText("payMethod", setWaitTimeout, 10);// 获取支付方式
		Log.logInfo("支付方式为:"+getPayMethod);
		opApp.ClickElement("payAccount", setWaitTimeout);
		
		returnHome();
		opApp.ClickElement("homeaccount", setWaitTimeout);
		opApp.ClickElement("myOrder", setWaitTimeout);//我的订单
		getPayStates=opApp.GetElementText("payStates", 10,10);
		Assert.assertEquals(getPayStates,"Paid");

		getpayTotal=opApp.GetElementText("payTotal", 10,10).replace("$", "").trim();
		Assert.assertEquals(getPayPrice, getpayTotal);
		
		
		
	}
	
	
	/**
	 * 
	* @测试点: 通过sku查询,默认使用的是不清仓，不促销的商品 
	* @验证点: 如果数量 等于3，会获取3个不同的商品，1、不清仓不促销  2.促销商品  3.清仓商品
	*
	* @param number  设置商品的数量
	*  @param same   是否要添加相同产品，如果为false，就为同一个商品
	*  @使用环境：  测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年6月27日 
	  @修改说明
	 */
	public void   searchSku(int number,boolean same) {
		int sendkesNumber = 0;
		Map<String, Object> goods;
		String[] sku = new String[3];
		// 以下是几个商品数据
		goods = interfaceMethod.getGoods(myPublicUrl, "n", "n");// 第一个sku
		sku[0] = (String) goods.get("goods_sn");
		goods = interfaceMethod.getGoods(myPublicUrl, "n", "y"); // 第2个sku,促销商品
		sku[1] = (String) goods.get("goods_sn");
		goods = interfaceMethod.getGoods(myPublicUrl, "y", "n");// 第3个sku，清仓商品
		sku[2] = (String) goods.get("goods_sn");

		if (number==3 && same == true) {
			sendkesNumber=3;
			number = 1; // 把初始位置为1,
			
		}
		for (int i = 0; i < number; i++) {
			returnHome();
			opApp.ClickElement("Categories", setWaitTimeout);
			opApp.sendKeys("search", sku[i]);
			try {
				opApp.actionPressKeyCode(AndroidKeyCode.ENTER);
			} catch (Exception e) {
				e.printStackTrace();
			}
			opApp.ClickElement("resultSearch", setWaitTimeout);// 查询到内容
			Page.pause(2);
			opApp.swipe(0.5, 0.6, 0.5, 0.3, 1000);// 需要上滑一部分
			Page.pause(2);

			opApp.ClickElement("Addshopping", setWaitTimeout);// 添加到购物车
			Log.logInfo("添加到购物车成功");

		}
		opApp.ClickElement("shoopinggo", setWaitTimeout);// 购物车图标
		if (sendkesNumber==3) {//获取的是相同的商品	
			opApp.ClickElement("shoopingEditBtn", setWaitTimeout);// 点击编辑
			opApp.SendKeysClean("shoopingSetNumber", String.valueOf(3), 10);
			opApp.ClickElement("shoopingEditBtn", setWaitTimeout);
			Page.pause(1);
		}
		if(opApp.elementExists(10, "checkOut")){
			opApp.ClickElement("checkOut", setWaitTimeout); // 点击确认购买
			
		}
		if(opApp.elementExists(5,"affirmdelete")){
			opApp.ClickElement("affirmdelete", setWaitTimeout);//主要解决一个弹框的问题
		}
		
		
		if (opApp.elementExists(10, "addressSelection")) {
			opApp.ClickElement("addressSelection", setWaitTimeout);
			opApp.ClickElement("done", setWaitTimeout);
		}
	}
	
	
	
	

	/**
	 * 
	* @测试点: 支付页面订单的总价，以及支付后的订单内容 
	* @验证点: 获取支付前的订单价格， 支付订单后，对比价格和支付状态是否正确
	*  @param payStats  支付的状态
	*  @param payPrice    支付的价格
	* @备注： void    
	* @author zhangjun 
	* @date 2017年6月27日 
	  @修改说明
	 */
	public void payOrder(){
		
		getPice = opApp.GetElementText("placePrice", 10, 10);// 获取价格
		Log.logInfo("支付的总价为：" + getPice); 
		opApp.ClickElement("placeSecure", setWaitTimeout);
		Assert.assertTrue(opApp.elementExists(120, "ppEmail")||opApp.elementExists(120,"cardContinue"));
		//在支付页面点击返回
		opApp.ClickElement("cardReturn", setWaitTimeout);//点击返回
		opApp.ClickElement("affirmdelete", setWaitTimeout);//确认
		opApp.ClickElement("affirmdelete", setWaitTimeout);//确认
		
		//返回到主页面
		returnHome();
		opApp.ClickElement("homeaccount", setWaitTimeout);
		opApp.ClickElement("myOrder", setWaitTimeout);//我的订单
		getPayStates=opApp.GetElementText("payStates", 10,10);
		Assert.assertEquals(getPayStates, "Waiting for Payment");
		
		getOrderNo=opApp.GetElementText("payOrder", 10,10);
		Log.logInfo("获取的订单号为:"+getOrderNo);
		
		getpayTotal=opApp.GetElementText("payTotal", 10,10);
		Assert.assertEquals(getPice, getpayTotal);
	}
	
	/**
	 * 
	 * @测试点: 返回到主页面
	 * @验证点: 用于在任何页面，都返回到主页面
	 * @修改说明
	 */
	public void returnHome() {
		while (opApp.elementExists(3, "homeaccount") == false) {
			try {
				opApp.actionPressKeyCode(AndroidKeyCode.BACK);
				if(opApp.elementExists(3, "affirmdelete")){
					opApp.ClickElement("affirmdelete", setWaitTimeout);//确认
					//opApp.ClickElement("affirmdelete", setWaitTimeout);//确认
				}
			} catch (Exception e) {
				e.printStackTrace();
			} // 返回到主页
		}
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
		
		opApp.ClickElement("done", setWaitTimeout);
	}
}