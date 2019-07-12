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
import com.globalegrow.custom.ControlUsingMethod;
import com.globalegrow.custom.InterfaceMethod;
import com.globalegrow.util.Log;

import com.globalegrow.util.Pub;
import com.globalegrow.util.testInfo;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidKeyCode;

public class RW_A_order extends StartApp {
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
	private String subTotal;//商品总价
	private String  shippingFee;//商品的消费
	private String  insurance;//保险费
	DecimalFormat df = new DecimalFormat("#0.00");
	InterfaceMethod interfaceMethod;
	String[] productPrice;

	@Parameters({ "testUrl" })
	public RW_A_order(String testurl) {
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
			testCasemap = Pub.readTestCase(moduleName, testCaseProjectName, projectName);
			interfaceMethod = new InterfaceMethod();
			
			start();
			//ControlUsingMethod.runadb();
			opApp.setScreenShotPath(screenShotPath);
			
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
		interfaceMethod.crearAllCartGoods(myPublicUrl, "autotest23@globalegrow.com");// 清除购物车
		
		opApp.ClickElement("addressAccount", setWaitTimeout);
		opApp.sendKeys("loginName", "autotest23@globalegrow.com");
		opApp.sendKeys("logingPassword", "123456");
		opApp.ClickElement("loginSign", setWaitTimeout);
		boolean account = opApp.elementExists(20, "addressAccount");
		Assert.assertTrue(account);
	}
	
	
	/**
	 * 
	* @测试点: 添加一件商品到购物车，点击去结算，选择平邮加跟踪码，提交订单 
	* @验证点: 1.订单费用正确（对应的运费和跟踪码费用正确）
			2.订单详情页显示的物流方式正确
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年7月3日 
	  @修改说明
	 */
	@Test
	public void ordinary_mail(){
		interfaceMethod.crearAllCartGoods(myPublicUrl, "autotest23@globalegrow.com");// 清除购物车
		returnHome();
		Double  totalPrice=searchSku(1,false,1);  //获取的总价
		payOrder(df.format(totalPrice),"NUll");
		//returnHome();
	
		
		
	}
	
	/**
	 * 
	* @测试点: 添加一件商品到购物车，点击去结算，选择标准邮递方式，提交订单 
	* @验证点: 1.订单费用正确（对应的运费正确）
			2.订单详情页显示的物流方式正确
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年7月4日 
	  @修改说明
	 */
	@Test
	public void standard_mail(){
		interfaceMethod.crearAllCartGoods(myPublicUrl, "autotest23@globalegrow.com");// 清除购物车
		Double  totalPrice=searchSku(1,false,2);  //获取的总价
		payOrder(df.format(totalPrice),"NUll");

	}
	
	
	/**
	 * 
	* @测试点: 添加一件商品到购物车，点击去结算，选择快递方式，提交订单 
	* @验证点: 订单费用正确（对应的运费正确），订单详情页显示的物流方式正确
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年7月5日 
	  @修改说明
	 */
	@Test
	public void expedited_mail(){
		interfaceMethod.crearAllCartGoods(myPublicUrl, "autotest23@globalegrow.com");// 清除购物车
		Double  totalPrice=searchSku(1,false,3);  //获取的总价
		payOrder(df.format(totalPrice),"NUll");
		
	}
	
	
	/**
	 * 
	* @测试点: 添加一件商品到购物车，点击去结算，不勾选保险费，提交订单 
	* @验证点: 1.支付前的订单金额与订单详情的金额一致
			2.且保险费为0 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年7月5日 
	  @修改说明
	 */
	@Test
	public void not_insurance(){
		
		Double  totalPrice=searchSku(1,false,4);  //获取的总价
		payOrder(df.format(totalPrice),"NUll");
		
	}
	
	
	/**
	 * 
	* @测试点: 添加一件商品到购物车，点击去结算，勾选保险费，提交订单 
	* @验证点:1.计算保险费金额正确正确性（保险费用=订单商品总额*0.02+1）
				2.支付前的订单金额与订单向前页面的一致，且保险费显示正确 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年7月5日 
	  @修改说明
	 */
	@Test
	public void calculate_insurance(){
		Double  totalPrice=searchSku(1,false,5);  //获取的总价
		payOrder(df.format(totalPrice),"NUll");

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
	public void cardPayContinue(){
		interfaceMethod.crearAllCartGoods(myPublicUrl, "autotest23@globalegrow.com");// 清除购物车
		Double  totalPrice=searchSku(1,false,6);  //获取的总价
		payOrder(df.format(totalPrice),"card");
		opApp.ClickElement("payOrder", setWaitTimeout);
		opApp.ClickElement("placeSecureOrder", setWaitTimeout);
		Assert.assertTrue(opApp.elementExists(60, "cardContinue"));//是否生成了订单
	
	}
	

	
	
	

	


	
	/**
	 * 
	* @测试点: 通过sku查询,默认使用的是不清仓，不促销的商品 
	* @验证点:
	*
	* @param number  设置商品的数量
	*  @param same   是否要添加相同产品，如果为false，就为同一个商品
	* @param payMethod  支付方式  1.勾选跟踪码  2.标准邮支付方式,勾选促销码   3.快递 4.去掉运费  5.计算保险费  6.更换支付方式信用卡
	*  @return    返回计算出来的价格，价格只包含：商品总价+运输总价+运输保险总价
	*  @使用环境：  测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年6月27日 
	  @修改说明
	 */
	

	public double   searchSku(int number,boolean same, int payMethod ) {
		Map<String, Object> goods;
		String sku;
		double total = 0;
		String shoopingPrice;//商品的提交单价
		String calculateTheInsurance = null;//计算的保险费价格
		// 以下是几个商品数据
		goods = interfaceMethod.getGoods(myPublicUrl, "n", "n");// 第一个sku
		sku = (String) goods.get("goods_sn");

		returnHome();
		opApp.ClickElement("Categories", setWaitTimeout);
		opApp.sendKeys("search", sku);
		try {
			opApp.actionPressKeyCode(AndroidKeyCode.ENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		opApp.ClickElement("resultSearch", setWaitTimeout);// 查询到内容
		if(opApp.elementExists(5, "shoopinggo")){
			opApp.swipe(0.5, 0.7, 0.5, 0.3, 1000);// 需要上滑一部分
		}
		opApp.ClickElement("Addshopping", setWaitTimeout);// 添加到购物车
		Log.logInfo("添加到购物车成功");
		opApp.ClickElement("shoopinggo", setWaitTimeout);// 点击购物车图标
		shoopingPrice=opApp.GetElementText("orderPrice", 10,10).replace("$", "");
		Log.logInfo("商品的单价为:"+shoopingPrice);
		opApp.ClickElement("checkOut", setWaitTimeout); // 点击确认购买	
		
		if(opApp.elementExists(10, "placeSecureOrder")){//进入到支付提交订单页面
			opApp.swipe(0.5, 0.2, 0.5, 0.6,1000);
		}
	
		/*提交订单的支付页面
		 * 
		 * 为1就勾选跟踪码*/
		Page.pause(2);
		if(payMethod==1){
			opApp.ClickElement("payTrackingNumber", setWaitTimeout);//勾选跟踪码
		}else if(payMethod==2){
			opApp.GetElementByBy("palyPayMethod", 1).click();//标准邮
		}else if(payMethod==3){
			opApp.GetElementByBy("palyPayMethod", 2).click();//快递
		}else if(payMethod==4){//取消勾选保险费
			opApp.ClickElement("insurance", setWaitTimeout);//去掉运费
		}else if(payMethod==5){//计算保险费
			calculateTheInsurance=df.format(Double.parseDouble(shoopingPrice)*0.02+1);
			Log.logInfo("计算的保险费:"+calculateTheInsurance);
		}else{
			opApp.swipe(0.5, 0.6, 0.5, 0.2,1000);
			opApp.ClickElement("payCard", setWaitTimeout);//支付方式的更改,更改为信用卡
		}
		Page.pause(1);
		opApp.swipe(0.5, 0.9, 0.5, 0.1,1000);
		opApp.SendKeysClean("paycordMessage","0",  setWaitTimeout);
		subTotal=opApp.GetElementText("payCountSubTotal", 10,10).replace("$", "");// 总价
		shippingFee=opApp.GetElementText("payCountFree", 10,10).replace("+$", "");//free的价格
		if(opApp.elementExists(5, "payInsurance")){
			insurance=opApp.GetElementText("payInsurance", 10,10).replace("+$", "");//保险的价格
		}

		if(payMethod==1){		
			String 	trankingcode=opApp.GetElementText("payTranckingCode", 10,10).replace("+$", "");//促销码
			total=Double.parseDouble(subTotal)+Double.parseDouble(shippingFee)+Double.parseDouble(insurance)+Double.parseDouble(trankingcode);
		}else if(payMethod==4){
			 total=Double.parseDouble(subTotal)+Double.parseDouble(shippingFee);
		}else if(payMethod==5){
				Assert.assertEquals(calculateTheInsurance, insurance);	//对比计算的结果	
				total=Double.parseDouble(subTotal)+Double.parseDouble(shippingFee)+Double.parseDouble(insurance);
		}else{
			total=Double.parseDouble(subTotal)+Double.parseDouble(shippingFee)+Double.parseDouble(insurance);
		}
		return total;
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
	public void payOrder(String price,String flage){
		
		getPice = opApp.GetElementText("placePrice", 10, 10).replace("$", "");// 获取价格
		Log.logInfo("支付的总价为：" + getPice); 
		Assert.assertEquals(getPice, price);//对比计算的总价与实际总价是否一致
		
		opApp.ClickElement("placeSecure", setWaitTimeout);
		if (flage.equals("card")){
			Assert.assertTrue(opApp.elementExists(60,"cardContinue"));
		}else{
			Assert.assertTrue(opApp.elementExists(60, "ppEmail")||opApp.elementExists(60,"cardContinue"));//是否生成了订单
		}
	
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
		
		getpayTotal=opApp.GetElementText("payTotal", 10,10).replace("$", "");
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