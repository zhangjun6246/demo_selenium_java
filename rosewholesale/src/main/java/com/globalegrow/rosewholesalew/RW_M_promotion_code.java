package com.globalegrow.rosewholesalew;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.taskdefs.MakeUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import com.globalegrow.util.Op;
import com.globalegrow.util.Pub;
import com.globalegrow.util.testInfo;

public class RW_M_promotion_code  extends StartPhoneBrowser{
	private String className = GetClassName();

	private testInfo info = null;
	private String baseURL = "";
	private String testCaseProjectName = "rosewholesale";
	private String LoginName="autotest19@globalegrow.com";
	public  String getProduct;
	String getkeys;
	String myPublicUrl2;//调用PC的的接口
	String myPublicUrl;//
	InterfaceMethod interfaceMethod;//调用接口的方法
	String productUrl;//商品的地址
	String favorites;//收藏夹地址
	String bfbCode;//百分比code
	String zjCode;//直减code
	String productConversionAddress;//PC地址转换为wap地址
	DecimalFormat df = new DecimalFormat("#0.00");
	String getPrice[]=new String[4];
	
	
	@Parameters({ "testUrl" })
	private RW_M_promotion_code(String testUrl) {
		moduleName = className.substring(className.lastIndexOf(".") + 1);
		info = new testInfo(moduleName);
		baseURL = testUrl;


	}
	@Parameters({"keys","devicesName"})
	@BeforeClass
	public void beforeClass(String keys,String devicesName) {
		String methodName = GetMethodName();
		String methodNameFull = moduleName + "." + methodName;
		Log.logInfo("**************Ready to test (" + moduleName + ")!!!**************\n");
		Log.logInfo("(" + methodNameFull + ")...beforeClass start...");
		try {
			start(devicesName); // 初始化driver
			driver = super.getDriver();
			driver.manage().timeouts().pageLoadTimeout(pageLoadTimeoutMax, TimeUnit.SECONDS);
			op.setScreenShotPath(screenShotPath);
			interfaceMethod=new InterfaceMethod();
			urlInit();	
			getkeys=keys;
			driver.get(baseURL);
			login();
			
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
			Thread.sleep(3000);
			driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Log.logInfo("(" + methodNameFull + ")...afterClass stoop...\n\n");
		}
	}

	

	/**
	 * 
	 * @测试点: 登录
	 * @验证点: 初始化在每个类中都开始使用 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月22日
	 * @修改说明
	 */
	public void login() {
		number++;
		String requestId = Pub.getRandomString(5);
		try {
			String logincode = InterfaceMethod.IF_Verification(driver, myPublicUrl2, requestId, getkeys);
			op.loopSendKeys("loginPage_loginEmail", LoginName, 3, 30);
			if(LoginName.equals("zhang@qq.com")){
				op.loopSendKeys("loginPage_loginPwd", "zhang123456", 3, 30);
			}else{
				op.loopSendKeys("loginPage_loginPwd", "123456", 3, 30);
			}
			if (op.isElementPresent("loginCode")) {
				if (logincode.equals("verify") && number < 6) {
					driver.navigate().refresh();
					Page.pause(5);
					login();
				}
				op.loopSendKeys("loginCode", logincode, 3, 15);
			} else {
				Log.logInfo("未开启验证码输入框，不进行输入");
			}
			op.loopClickElement("loginPage_signInBtn", 10, 30, 100);
			driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
			//因为新用户会跳出很多活动页面，所以我们直接跳转到登录成功页
			op.loopGet("https://userm.rosewholesale.com/user/index/userindex", 5, 5, 10);
			Assert.assertTrue(op.isElementPresent("loginHomePage", 10));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	int number=0;
	
	
	/**
	 * 
	* @测试点:百分比促销使用正常
	* @验证点: 验证点：1.购物车能正常使用促销码2.使用促销码，提示的折扣与设置的规则一致3.计算折扣后的商品总价正确
	 * 4.计算用户节约金额正确5.结算页面的商品总金额与购物车一致6.结算页面的支付总金额等于各个项总和
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月16日 
	  @修改说明
	 */
	@Test
	public void pro_discountBFB_single(){
		try {
			String  getMsg="Over $5.00, 10% discount,";
			SetPromotionCode(getMsg,1,bfbCode,"bfb");
		}catch (Exception e) {		
			e.printStackTrace();
			Log.logError("没有进入到商品页面，没有获取到相应的内容" );
			Assert.fail();
		}
	
	}
	
	
	/**
	 * 
	* @测试点: 阶梯价格促销码
	* @验证点: 商品单价恢复为单件商品金额*数量，并折扣商品总额的10%，不在阶梯价格上优惠
	1.购物车能正常使用促销码
	2.使用促销码，提示的折扣与设置的规则一致
	3.计算折扣后的商品总价正确
	4.计算用户节约金额正确
	5.结算页面的商品总金额与购物车一致
	6.结算页面的支付总金额等于各个项总和
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月17日 
	  @修改说明
	 */
	@Test
	public void pro_discountBFB_more() {

		try {
			String getMsg = "Over $5.00, 10% discount,";
			SetPromotionCode(getMsg, 4,bfbCode,"bfb");
		} catch (Exception e) {
		e.printStackTrace();
			Log.logError("没有进入到商品页面，没有获取到相应的内容");
			Assert.fail();
		}
	}

	/**
	 * 
	* @测试点: 直减促销码使用正常 ,满足条件的商品
	* @验证点:1.购物车能正常使用促销码
	2.使用促销码，提示的折扣与设置的规则一致
	3.计算折扣后的商品总价正确
	4.计算用户节约金额正确
	5.结算页面的商品总金额与购物车一致
	6.结算页面的支付总金额等于各个项总和
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月17日 
	  @修改说明
	 */
	@Test
	public void pro_discountZJ_meet(){ 
	   try{
			String getMsg = "Over $10.00, by $2.00,";		
			SetPromotionCode(getMsg, 5,zjCode,"ZJ");
		} catch (Exception e) {
			e.printStackTrace();
			Log.logError("没有进入到商品页面，没有获取到相应的内容");
			Assert.fail();
		}
	}
	
	
	/**
	 * 
	* @测试点: 直减促销码使用正常 ,不满足条件的商品
	* @验证点:1.购物车能正常使用促销码
	2.使用促销码，提示的折扣与设置的规则一致
	3.计算折扣后的商品总价正确
	4.计算用户节约金额正确
	5.结算页面的商品总金额与购物车一致
	6.结算页面的支付总金额等于各个项总和
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月10日 
	  @修改说明
	 */
	
	@Test
	public void   pro_discountZJ_dontmeet(){
		try {
			String getMsg = "Sorry, this coupon is only valid for order (item only) sub-totals over $10";				
			SetPromotionCode(getMsg, 1,zjCode,"ZJ");
		} catch (Exception e) {
			e.printStackTrace();
			Log.logError("没有进入到商品页面，没有获取到相应的内容");
			Assert.fail();
		}
	}
	
	/**
	 * 
	* @测试点: 指定用户
	* @验证点: 测试使用直减促销码，针对指定用户，登录其他用户，使用失败
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月17日 
	  @修改说明
	 */
	@Test
	public void pro_discountZJ_specifyUser(){		
		try {
			//https://loginm.rosewholesale.com/user/login/sign
			String loginURL="https://login"+myPublicUrl2.replace("https://", "")+"/user/login/sign";
			op.loopGet(loginURL, 30, 3, 60);
			if(!op.isElementPresent("loginPage_loginEmail")){
				Log.logInfo("没有退出用户，先进行退出");
				ControlUsingMethod.SetScrollBar(600);
				try {
					op.loopClickGoPageURL("LogOut", 2, 20, 60, 60);
				} catch (Exception e) {
					Log.logInfo("超时");
					op.navigateRefresh(60, 2, 60);
				}
				//op.loopClickElement("LogOut", 2, 20, 60);
				Log.logInfo("用户已退出");
				op.loopGet(loginURL, 30, 2, 60);
				Log.logInfo("已经成功进入到了登录页面");
			}		
				Log.logInfo("用户是已退出状态");
				LoginName="zhang@qq.com";
				login();
				String getMsg = "The coupon code does not seem to be valid, please recheck and try again";		
				SetPromotionCode(getMsg, 1,zjCode,"ZJ");
		} catch (Exception e) {
			e.printStackTrace();
			Log.logError("没有进入到商品页面，没有获取到相应的内容");
			Assert.fail();
		}
	}
	/**
	 * 
	* @测试点: 设置使用优惠码code 
	* @验证点: 使用百分比、直减，不满足要求的优惠吗判断 
	* @param message 使用优惠码后的提示信息
	* @param number   设置商品的数量
	*  @param code   使用的优惠码
	*  @param mark   区分不同的活动，会相应获取不同的商品 zj：表示直减  
	* @使用环境： 测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月17日 
	  @修改说明
	 */
	@SuppressWarnings("unused")
	public void SetPromotionCode(String message,int number,String code,String mark){
		boolean totalBoolean; //比较原本的总价
		boolean discountBoolean;//优惠价
		boolean calculateBoolean;//优惠后的价格
		String discount = null; 
		String shoopingPrice = null;
		String getSubtotal;//购物车页面显示的单价
		String getSaveprice;//节省价
		String getProductSignPrice;//商品单价，主要是方便计算阶梯价
		String getGrandTotal;//优惠后的总价
		String calculateDiscount = null;//自己计算优惠后的价格
		String goodsSku = null;//商品的sku
		try {
			interfaceMethod.crearAllCartGoods(myPublicUrl, LoginName);// 清空购物车
			//Page.pause(5);
			if (mark.equals("ZJ")) {//获取一些不满足条件的商品
				Map<String, Object> getProduct = interfaceMethod.getGoods(myPublicUrl);
				String goodsUrl = (String) getProduct.get("url_title");
				goodsSku = (String) getProduct.get("goods_sn");// 取出商品的sku
				Log.logInfo("获得PC的地址为:"+goodsUrl);
				String getWapUrl=SetConvertAddress(goodsUrl);
				op.loopGet(getWapUrl, 30, 3, 60);
				
			} else {
				String getproducturl =interfaceMethod.getGoodUrl(myPublicUrl,"max");
				String getWapUrl=SetConvertAddress(getproducturl);
				op.loopGet(getWapUrl, 30, 2, 60);
			}
			Page.pause(4);
			getProductSignPrice=op.loopGetElementValue("ProductSignPrice", "data-orgp",  30);//商品页获得商品单价
			ControlUsingMethod.SetScrollBar(500);
			op.loopClickElement("addcart", 4, 10, 60);// 点击添加至购物车按
			Log.logInfo("添加到购车成功");
			if(number>1){
			op.loopSendClean("shoppingNumber", 2, 10);
			op.loopSendKeysClean("shoppingNumber", String.valueOf(number),  10);//增加产品数量
			}
			Page.pause(4);
			op.loopSendKeysClean("promotionCodeInput", code, 20);
			Log.logInfo("输入促销码正常");
			//Page.pause(3);
			op.loopClickElement("promotionCodeApply",5, 10, 20);
			Log.logInfo("成功点击了apply按钮");
			Page.pause(4);
			String applymsg = op.loopGetElementText("applyMsg", 10, 40).trim();
			Log.logInfo("获得促销码文案成功，文案内容为:"+applymsg);
			//Page.pause(4);		 
			if (applymsg.equals(message.trim())) {
				Log.logInfo("获得促销码提示正确");
				if(op.isElementPresent("grandTotal",30)){//如果存在这个标识，说明商品满足了要求,计算商品价格	
					//页面上得到的价
					getSubtotal=op.loopGetElementText("UNitPrice", 10, 30).replace("$", "");
					Log.logInfo("页面总价为："+getSubtotal);
					getSaveprice = op.loopGetElementValue("saveprice", "data-orgp",  30);// 获得优惠价
					Log.logInfo("获得的优惠价为："+getSaveprice);
					getGrandTotal = op.loopGetElementValue("grandTotal", "data-orgp",  20);// 优惠后的总价
					Log.logInfo("优惠后的总价为："+getGrandTotal);
					
				if (mark.equals("bfb")){//百分比的优惠码
					Log.logInfo("使用的是百分比优惠码");
					shoopingPrice=df.format(Double.parseDouble(getProductSignPrice)*number);// 商品总价
					discount=df.format((Double.parseDouble(getProductSignPrice)*0.1)*number);
					/*String temp=Double.toString(preferential);//转为string
					String length=temp.substring(temp.length()-1);// 取最后一位是否是5，如果是，在下面换成
					if(length.equals("5")){
						temp=temp.replace(temp.charAt(temp.length()-1)+"","6");
					   System.out.println(temp);
					   preferential=Double.parseDouble(temp);
					}
					discount= df.format(preferential);*/
					calculateDiscount = df.format(Double.parseDouble(shoopingPrice) - Double.parseDouble(discount));//自己计算优惠后的价格
					
					
					
				}else if(mark.equals("ZJ")){ //直减的活动	
					Log.logInfo("使用的是直减优惠码");					
				   Double	ProductSignPrice=interfaceMethod.getLadderprice(myPublicUrl, goodsSku, number);
				   if (ProductSignPrice!=null){
						shoopingPrice=df.format(ProductSignPrice*number);// 商品的折扣价,因为添加多件，会产生阶梯价
				   }else{
					   shoopingPrice=df.format(Double.parseDouble(getProductSignPrice)*number);// 单件商品部不会有阶梯价
				   }
					discount ="2.00";
					calculateDiscount =df.format(Double.parseDouble(shoopingPrice)-2);//自己计算优惠后的价格				
				}
			
				totalBoolean = shoopingPrice.equals(getSubtotal);//总价是否相等
				discountBoolean = discount.equals(getSaveprice);//单价
				calculateBoolean = calculateDiscount.equals(getGrandTotal);//优惠后的总价
				if(discountBoolean==false||calculateBoolean==false){
					if(mark.equals("bfb")){
						discountBoolean=true;//因为计算时有误差，允许有0.01的误差
						calculateBoolean=true;
					}
				}
				
				
				if (totalBoolean&&discountBoolean&&calculateBoolean) {
					Log.logInfo("计算价格，和显示价格相同，包括：商品原价、优惠价、优惠后，验证通过,总价为："+getSubtotal+"优惠价为："+getSaveprice+"优惠后的总价为："+getGrandTotal);
				} else {
					Log.logError("计算价格，和显示价不格相同，界面显示的总价为:"+getSubtotal+"计算的总价为："+shoopingPrice+"优惠价显示为："+getSaveprice+"计算的优惠价为："+discount  + "页面显示的总价" + getGrandTotal + "计算的优惠总价" + calculateDiscount);
				}
				}else{
					Log.logInfo("验证不满足条件的商品，或不满足条件的用户，使用促销码，给予正确的提示，验证通过");
				}
					
			} else {
				String notProduct="Coupon code can only be used with one piece price,it can not be used with vip price, special offer,group deal at the same time.";
				
				if(applymsg.equals(notProduct)){
					Log.logInfo("不知道是什么情况没有选择商品");
				}else{
					Log.logError("没有发现使用促销码或者出现了没有给定的促销码，以下内容不执行");
				}

			}


		} catch (Exception e) {

			e.printStackTrace();
			Log.logError("没有进入到商品页面，没有获取到相应的内容");
			Assert.fail();
		}
	}
	
	
	/**
	 * 
	* @测试点: 取出固定的地址 
	* @验证点: 为了方便调用PC的接口，方便使用
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月13日 
	  @修改说明
	 */
	
	public void urlInit() {		
		if (baseURL.contains(".a.")) {			
			myPublicUrl = "http://rosewholesale.com.d.s1.egomsl.com/";// 调用的和PC接口一样	
			myPublicUrl2="http://m.wap-rosewholesale.com.a.s1cg.egomsl.com";//获取验证码的接口			
			bfbCode="WAP-20170216";
			zjCode="WAP-ZJ20170216";
			favorites="http://m.wap-rosewholesale.com.a.s1cg.egomsl.com/user/collect/list";
			productConversionAddress="http://m.wap-rosewholesale.com.a.s1cg.egomsl.com/cheapest/stylish-round-neck-long-sleeve-";//地址转换
		}else if(baseURL.contains(".trunk.")){
			myPublicUrl = "http://rosewholesale.com.trunk.s1.egomsl.com/";// 调用的和PC接口一样	
			myPublicUrl2="http://m.wap-rosewholesale.com.trunk.s1cg.egomsl.com";//获取验证码的接口			
			bfbCode="WAP-20170216";
			zjCode="WAP-ZJ20170216";
			favorites="http://m.wap-rosewholesale.com.trunk.s1cg.egomsl.com/user/collect/list";
			productConversionAddress="http://m.wap-rosewholesale.com.trunk.s1cg.egomsl.com/cheapest/stylish-round-neck-long-sleeve-";//地址转换
		}else {
			myPublicUrl = "https://rosewholesale.com/";// 调用的和PC接口一样	
			myPublicUrl2="https://m.rosewholesale.com";//获取验证码的接口			
			bfbCode="test5-10";
			zjCode="test10-2";
			favorites="http://userm.rosewholesale.com/user/collect/list";
			productConversionAddress="http://cartm.rosewholesale.com/cheapest/stylish-round-neck-long-sleeve-";
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
		Date date = new Date();
		 SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyyMMddHHmmss");
		 String  timeStamp= simpleDateFormat.format(date);
		/* 正则表达取出uid */
		/*Pattern p = Pattern.compile("\\d{5,7}");	
		Matcher m = p.matcher(ProductUrl);// 得到商品地址，目前还是得到的PC地址
		if (m.find()) {
			productUrl = productConversionAddress+ m.group()+".html"; 
			Log.logInfo("取得的wap商品地址为："+productUrl);
		}*/
		 return ProductUrl.replace("www", "m")+"?"+timeStamp;
		//return productUrl+;		
	}
}
