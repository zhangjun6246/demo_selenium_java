package com.globalegrow.rosewholesale;

import java.text.DecimalFormat;
import org.openqa.selenium.JavascriptExecutor;
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
import com.globalegrow.util.Pub;
import com.globalegrow.util.testInfo;
public class RW_promotion_code extends Startbrowser{
	private String className = GetClassName();
	private String testCaseProjectName = "rosewholesale";
	private testInfo info = null;
	private String baseURL;
	private  String  getkeys;
	private String classNameShort;
	private String myPublicUrl;
    private	String  getMsg;//提示信息
    private String bfbCode;//百分比code
    private String zjCode;//直减code
    private String  loginURL;//登录页面
    private String timeStamp;//时间戳
    private String loginName="autotest18@globalegrow.com";
    private double rate;
    private String rates;
	DecimalFormat df = new DecimalFormat("#0.00");
	private String screenShotPath="test-output/html-report/screenShot/";
	int SetNumber=1;
	InterfaceMethod interfacemethod;
	
	@Parameters({ "testUrl" })
	private RW_promotion_code(String testUrl) {
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
			op.loopGet(baseURL, 60, 2, 60);
			Log.logInfo("baseURL:" + baseURL);
			urlInit();// 初始化URL
			getkeys=keys;
			interfacemethod=new InterfaceMethod();
			login(loginName);
		} catch (Exception e) {
			e.printStackTrace();
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
			Thread.sleep(3000);
			driver.quit();
			beforeTestRunFlag = false; // 必要
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
		Log.logInfo("(" + info.methodNameFull + ")...afterMethod stoop...\n\n");
	
	}
	
	
	/**
	 * 
	* @测试点:百分比促销使用正常
	* @验证点: 验证点：1.购物车能正常使用促销码2.使用促销码，提示的折扣与设置的规则一致3.计算折扣后的商品总价正确
	 * 4.计算用户节约金额正确5.结算页面的商品总金额与购物车一致6.结算页面的支付总金额等于各个项总和
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月7日 
	  @修改说明
	 */
	@Test
	public void pro_discountBFB_single(){
		try {
			interfacemethod.crearAllCartGoods(myPublicUrl,loginName);// 清空购物车
	        getMsg="Over $5.00, 10% discount";	
			SetPromotionCode(getMsg,SetNumber,bfbCode,"bfb");
		}catch (Exception e) {
			
			e.printStackTrace();
			Pub.printTestCaseExceptionInfo(info);
			Log.logWarn("没有进入到商品页面，没有获取到相应的内容" );
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
	* @date 2017年1月10日 
	  @修改说明
	 */
	@Test
	public void pro_discountBFB_more() {
		try {
			interfacemethod.crearAllCartGoods(myPublicUrl,loginName);// 清空购物车
			String getMsg = "Over $5.00, 10% discount";
			SetPromotionCode(getMsg, SetNumber,bfbCode,"bfb");
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("没有进入到商品页面，没有获取到相应的内容");
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
	* @date 2017年1月10日 
	  @修改说明
	 */
	@Test
	public void pro_discountZJ_meet(){ 		
		try {
			interfacemethod.crearAllCartGoods(myPublicUrl,loginName);// 清空购物车
			String getMsg = "Over $10.00, by $2.00";		
			SetPromotionCode(getMsg, 6,zjCode,"ZJ");
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("没有进入到商品页面，没有获取到相应的内容");
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
		String getMsg;
		try {
			interfacemethod.crearAllCartGoods(myPublicUrl,loginName);// 清空购物车
			getMsg = "Sorry, this coupon is only valid for order (items only) sub-totals over $10.";		
			if(baseURL.contains("/es/")){				
				getMsg="Lo sentimos, el cupón sólo es válido para el pedido (sólo artículos) subtotales de más de $10.";
			}
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
	* @date 2017年1月11日 
	  @修改说明
	 */
	@Test
	public void pro_discountZJ_specifyUser(){	
		String getMsg ;
		try {			
			String getName=op.loopGetElementText("signlinkName", 3, 20);
			boolean loginState=getName.contains("autot");//区分测试与正式环境
			if(loginState){
				Signout_Success();// 登出		
			}		
			op.loopGet(loginURL, 20, 2, 100);
			loginName="zhang@qq.com";
			login(loginName);
			getMsg= "Sorry, You can not use this promotional code.";		
			if(baseURL.contains("/es/")){
				getMsg="Lo sentimos, no se puede usar este código promocional.";
			}
			SetPromotionCode(getMsg, 1,zjCode,"ZJ");
		} catch (Exception e) {
			e.printStackTrace();
			Log.logError("没有进入到商品页面，没有获取到相应的内容");
			Assert.fail();
		}
	}
	
	/**
	 * 
	* @测试点: 设置使用优惠码
	* @验证点: 使用优惠码的场景
	* @param message 使用优惠码时给予的正确提示
	* @param number   设置商品的数量
	* @param code   输入优惠码
	* @param mark   区分优惠活动  bfb：为百分比活动     ZJ：为直减活动
	*  测试环境，正式环境
	* @备注： 设置的直减：10-2 和百分比 5:10  
	* @author zhangjun 
	* @date 2017年1月10日 
	  @修改说明
	  
	 */
	public void SetPromotionCode(String message,int number,String code,String mark){
		boolean totalBoolean; //比较原本的总价
		boolean discountBoolean;//优惠价
		boolean calculateBoolean;//优惠后的价格
		String discount = null; 
		String shoopingPrice = null;
		String getSubtotal;//购物车页面显示的单价
		String getSaveprice;//节省价
		String getGrandTotal;//优惠后的总价
		String calculateDiscount = null;//自己计算优惠后的价格
		String getproducturl;//获取商品的地址		
		String methodName = GetMethodName();
		try {		
			if (mark.equals("ZJ")) {//获取一些不满足条件的商品
				if(baseURL.contains("/es/")){
					 getproducturl =interfacemethod.getGoodUrl("min", myPublicUrl,"n", "n","EUR");
					 interfacemethod.convertAddress(op, getproducturl);
				}else{
					 getproducturl =interfacemethod.getGoodUrl("min",myPublicUrl, "n", "n");
					 op.loopGet(getproducturl+"?"+timeStamp, 40, 3, 60);
					 Log.logInfo("获取的商品地址为:"+getproducturl);	
				}
				
			} else {				
				if(baseURL.contains("/es/")){
					 getproducturl =interfacemethod.getGoodUrl("max", myPublicUrl,"n", "n","EUR");
					 interfacemethod.convertAddress(op, getproducturl);
				}else{
					getproducturl =interfacemethod.getGoodUrl("max", myPublicUrl,"n", "n");
					op.loopGet(getproducturl, 40, 3, 60);
					Log.logInfo("获取的商品地址为:"+getproducturl);
				}				
			}			
			op.loopClickElement("addToBag", 4, 10, 300);// 点击添加至购物车按
			Log.logInfo("添加到购车成功");
			if(number==1){
				Log.logInfo("设置数量为1，不进行增加数量");
			}else{
				op.loopSendKeysClean("numberinputbox", String.valueOf(number),  10); // 增加商品数量	
				Page.pause(10);
			}
			op.loopSendKeysClean("codeInput", code, 20);
			Log.logInfo("输入促销码正常");
			
			WebElement enadble=op.MyWebDriverWait2("Whetherchoose", 10, true);
			if(enadble.isSelected()){	
				Log.logInfo("选择按钮已经被选中了,不用在点击");
			}else{				
				Log.logInfo("按钮没有被选中,点击按钮");
				op.loopClickElement("Whetherchoose", 5, 5, 10);
				Page.pause(3);
				op.loopCheckSendKeys("codeInput", code, 3, 60);//点击了按钮后，促销码被清空了
				Log.logInfo("输入促销码正常");
			}
			Page.pause(3);
			op.loopClickElement("applyBtn", 10, 30, 80);
			Log.logInfo("成功点击了apply按钮");		
			if(op.isElementPresent("applyMsg",40)){//判断，有时候会没有显示
				Log.logInfo("获取促销码提示元素成功");
			}else{
				Log.logError("获取促销码失败，请查明原因");
			}
			Page.pause(5);//输入后要等待很长的一段时间才显示内容，实际控件已经存在，用死等
			String applymsgs = op.loopGetElementText("applyMsg", 20, 60);
			Log.logInfo("获得促销码文案成功，文案内容为:"+applymsgs);			
			if (applymsgs.equals(message)) {
				String getTile = op.loopGetElementText("savetitle", 10, 60);
				if (getTile.equals("save")||getTile.equals("GUARDAR")) {
					// 页面上的价格，为了兼容正式和外网的环境，不使用orgp的方式，使用获取文本内容的方式
					getSubtotal = op.loopGetElementText("subtotal",  20, 60).replace("$", "").replace("€", "");// 获得总价
					getSaveprice = op.loopGetElementText("saveprice", 20, 60).replace("$", "").replace("€", "");;// 获得优惠价
					getGrandTotal = op.loopGetElementText("grandTotal", 20, 60).replace("$", "").replace("€", "");// 优惠后的总价

					if (mark.equals("bfb")) {// 百分比的优惠码
						shoopingPrice = op.loopGetElementText("UNitPrice", 3, 100).replace("$", "").replace("€", "");// 商品的原价
						discount = df.format(Double.parseDouble(shoopingPrice) * 0.1);// 优惠的价格
						calculateDiscount = df.format(Double.parseDouble(shoopingPrice) - Double.parseDouble(discount));// 自己计算优惠后的价格
						if(baseURL.contains("/es/")){
							String Saveprice=op.loopGetElementValue("es_total","data-orgp", 20);
							Log.logInfo("价格:"+Saveprice);
							String getSave=op.loopGetElementValue("saveprice","data-orgp",  20);
							discount=df.format(Double.parseDouble(getSave)*rate);
							calculateDiscount=df.format(Double.parseDouble(Saveprice)*rate);	
							
							}
					} else if (mark.equals("ZJ")) { // 直减的活动
						if(baseURL.contains("/es/")){
							shoopingPrice = op.loopGetElementValue("UNitPricezj","data-orgp", 20);
							shoopingPrice = df.format(Double.parseDouble(shoopingPrice) * number*rate);
							String Preferential=op.loopGetElementValue("esPreferential", "data-orgp",  20);
							discount =df.format(Double.parseDouble(Preferential)*rate);// 自己计算优惠后的价格	
							//calculateDiscount = df.format(Double.parseDouble(shoopingPrice) - Double.parseDouble(discount));// 自己计算优惠后的价格
							String Saveprice=op.loopGetElementValue("es_total","data-orgp", 20);
							Log.logInfo("价格:"+Saveprice);
							calculateDiscount=df.format(Double.parseDouble(Saveprice)*rate);
						}else{
							shoopingPrice = op.loopGetElementText("UNitPricezj", 3, 100).replace("$", "").replace("€", "");// 商品的原价
							shoopingPrice = df.format(Double.parseDouble(shoopingPrice) * number);
							discount = df.format(Double.parseDouble(shoopingPrice) - Double.parseDouble(getGrandTotal)).replace(".00", "");// 优惠的价格,写死的价格
							calculateDiscount = df.format(Double.parseDouble(shoopingPrice) - 2);// 自己计算优惠后的价格
						}
					}
					totalBoolean = shoopingPrice.equals(getSubtotal);// 总价
					discountBoolean = discount.equals(getSaveprice);// 单价
					calculateBoolean = calculateDiscount.equals(getGrandTotal);// 优惠后的总价
					if (totalBoolean && discountBoolean && calculateBoolean) {
						Log.logInfo("计算价格，和显示价格相同，包括：商品原价、优惠价、优惠后，验证通过");
					} else {
						Log.logError("计算价格，和显示价不格相同，总价,页面总价:"+getSubtotal+"计算的总价："+shoopingPrice+"页面的单价："+getSaveprice+"计算的单价："+discount+"页面的优惠总价："+getGrandTotal+"计算的优惠总价："+calculateDiscount);
					}
				} else {
					Log.logInfo("验证不满足条件的商品，或不满足条件的用户，使用促销码，给予正确的提示，验证通过");
				}

			} else {				
				Log.logError("没有发现使用促销码，以下内容不执行");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			Log.logError("没有进入到商品页面，没有获取到相应的内容");
			Assert.fail();
		}
}
	
	

	/**
	 * 
	* @测试点: 登录成功
	* @验证点: 登录，由于测试地址的前置条件，在beforeClass中运行 
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月7日 
	  @修改说明
	 */
	public void login(String name) {
		String requestId = Pub.getRandomString(6);
		String methodName = GetMethodName();
		try {
			Page.pause(4);
			boolean loginFlag = true;// 判断是否有登录
			number++;
			String logincode = InterfaceMethod.IF_Verification(driver, myPublicUrl, requestId, getkeys, "login");
			if (logincode.equals("verify") && number < 6) {
				driver.navigate().refresh();
				Page.pause(8);
				login(loginName);
			} else {
				Log.logInfo("验证码正确");
				
				op.loopSendKeys("Login_EmailEnter", loginName, 3, 20);
				if(name.equals("zhang@qq.com")){
					op.loopSendKeys("Login_PasswordEnter","zhang123456", 3, 20);
				}else{
					op.loopSendKeys("Login_PasswordEnter","123456", 3, 20);
				}
				Page.pause(1);
				if (op.isElementPresent("Login_verification")) {
					op.loopSendKeys("Login_verification", logincode, 3, 20);
				} else {
					Log.logInfo("验证码输入框已经关闭，不输入验证码进行登录");
				}
				
				op.loopClickElement("Login_SignBtn", 300, 5, 200);

				for (int i = 0; i < 10; i++) {// 判断登录成功				
					Log.logInfo("第" + i + "次");
					Page.pause(2);
					String LoginStats = op.loopGetElementText("signlink", 5, 20);
					if (LoginStats.contains("Hi, autot")||LoginStats.contains("Hi, zhang")) {
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
			Log.logInfo("页面超时，登录失败");
			//driver.quit();
		}finally {
			if(baseURL.contains("/es/")){//针对多语言的换算
				rate=interfacemethod.rates(rates);//获取的价格
			}
		}
			

	}
	int number=0;
	
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
		Page.pause(3);
		
		try {
			if(baseURL.contains("/es/")){
				//需要点击第一个
				op.loopClickElement("logout_link_esone", 3, 20, 50);						
				myjs = "document.querySelector(\"#header > div > div.user_box > ul\").style.display='block';";
				js.executeScript(myjs);
				op.loopClickElement("logout_link_es", 3, 20, 50);
			}else{
				op.loopClickElement("logout_link", 3, 20, 500);
				Page.pause(3);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}
	
	/**
	 * 
	* @测试点: 获取地址 
	* @验证点: 通过区分测试环境的地址，获取正式和测试环境的url
	* @使用环境：     测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年2月14日 
	  @修改说明
	 */
	public void urlInit() {
		if(baseURL.contains(".trunk.")&&baseURL.contains("/es/")){//西班牙语测试环境
			Log.logInfo("现在是西班牙语测试环境");
			myPublicUrl = "http://rosewholesale.com.trunk.s1.egomsl.com/";
			bfbCode="ES-201701071506";
			zjCode="ES-201701071537";
			loginURL = "http://login.rosewholesale.com.trunk.s1.egomsl.com/es/m-users-a-sign.htm";
			rates="http://cart.rosewholesale.com.trunk.s1.egomsl.com/data-cache/currency_huilv.js";//汇率换算
		}else if(baseURL.contains("/es/")){
			
			Log.logInfo("现在是西班牙语正式环境");
			myPublicUrl = "https://rosewholesale.com/";
			bfbCode="rosewholesale.com1";
			zjCode="rosewholesale.com";
			loginURL = "http://login.rosewholesale.com/es/m-users-a-sign.htm";
			rates="https://cart.rosewholesale.com/data-cache/currency_huilv.js";//汇率换算//汇率换算
		}else if (baseURL.contains(".d.")) {
			myPublicUrl = "http://rosewholesale.com.d.s1.egomsl.com/";
			bfbCode="ES-201701071506";
			zjCode="ES-201701071537";
			loginURL = "http://login.rosewholesale.com.d.s1.egomsl.com/m-users-a-sign.htm";
		}  else {
			myPublicUrl = "https://rosewholesale.com/";
			loginURL ="https://login.rosewholesale.com/m-users-a-sign.htm";
			bfbCode="rosewholesale.com1";
			zjCode="rosewholesale.com";
			loginURL = "https://login.rosewholesale.com/m-users-a-sign.htm";
		}
	}
	
	
}
