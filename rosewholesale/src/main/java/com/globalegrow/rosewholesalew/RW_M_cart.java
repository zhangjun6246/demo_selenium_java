package com.globalegrow.rosewholesalew;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;

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

public class RW_M_cart extends StartPhoneBrowser {
	private String className = GetClassName();
	private testInfo info = null;
	private String baseURL = "";
	private String testCaseProjectName = "rosewholesale";
	private String LoginName = "autotest06@globalegrow.com";
	public String getProduct;
	private String getkeys;
	private String myPublicUrl2;// 调用PC的的接口
	private String myPublicUrl;//
	InterfaceMethod interfaceMethod;// 调用接口的方法
	String productUrl;// 商品的地址
	String favorites;// 收藏夹地址
	DecimalFormat df;
	String getPrice[] = new String[4];// 为了获取商品总价;
	ControlUsingMethod controlmethod;
	 String timeStamp;// 时间戳
	boolean Ladderprice = false;

	@Parameters({ "testUrl" })
	private RW_M_cart(String testUrl) {
		moduleName = className.substring(className.lastIndexOf(".") + 1);
		info = new testInfo(moduleName);
		baseURL = testUrl;

	}

	@Parameters({ "keys", "devicesName" })
	@BeforeClass
	public void beforeClass(String keys, String devicesName) {
		String methodName = GetMethodName();
		String methodNameFull = moduleName + "." + methodName;
		Log.logInfo("**************Ready to test (" + moduleName + ")!!!**************\n");
		Log.logInfo("(" + methodNameFull + ")...beforeClass start...");
		try {
			start(devicesName); // 初始化driver
			driver = super.getDriver();
			driver.manage().timeouts().pageLoadTimeout(pageLoadTimeoutMax, TimeUnit.SECONDS);

			op.setScreenShotPath(screenShotPath);
			interfaceMethod = new InterfaceMethod();
			df = new DecimalFormat("#0.00");
			controlmethod = new ControlUsingMethod();
			urlInit();
			getkeys = keys;
			driver.get(baseURL);
			login();
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			timeStamp = simpleDateFormat.format(date);

		} catch (Exception e) {
			e.printStackTrace();
			/* Assert.fail(); */
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
			op.loopSendKeys("loginPage_loginEmail", LoginName, 3, 10);
			op.loopSendKeys("loginPage_loginPwd", "123456", 3, 10);
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
			op.loopClickElement("loginPage_signInBtn", 10, 10, 20);
			//因为新用户会跳出很多活动页面，所以我们直接跳转到登录成功页
			op.loopGet("https://userm.rosewholesale.com/user/index/userindex", 5, 5, 10);
			Assert.assertTrue(op.isElementPresent("loginHomePage", 10));
			/*
			 * if (getHomeText.equals("Welcome")) { Log.logInfo("登录成功"); } else
			 * { Log.logError("登录失败，其他内容不执行"); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	int number = 0;

	/**
	 * 
	 * @测试点: 空购物车显示
	 * @验证点: 1.页面中间显示一个空购物车文案：Your shopping cart is empty
	 *       2.点击购物按钮，达到链接的首页 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年2月13日
	 * @修改说明
	 */
	@Test
	public void cart_empty_a() {

		correFail = false;
		try {
			interfaceMethod.crearAllCartGoods(myPublicUrl, LoginName);
			op.loopClickElement("shoppingButton", 2, 15, 20);
			String testExpectedStr = "Your shopping bag is empty!";
			String getEmpytText = op.loopGetElementText("shoopingEmpty", 10, 20);
			Assert.assertEquals(getEmpytText, testExpectedStr);
			Log.logInfo("空页面显示文案正确,文案内容为:" + getEmpytText);
			op.loopClickElement("continueShoppingBtn", 3, 10, 20);
			Page.pause(2);
			String getUrl = driver.getCurrentUrl();
			String equalsPublicUrl = myPublicUrl2 + "/";
			if (getUrl.equals(equalsPublicUrl)) {
				Log.logInfo("空页面，点击继续购物按钮，跳转到首页，验证通过");
			} else {
				Log.logError("空页面，点击继续购物按钮，没有跳转到首页，验证失败，实际获取的地址为:" + getUrl + "需要对比的地址：" + equalsPublicUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Log.logInfo("没有找到空页面的控件，下面内容不执行");
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 添加商品到购物车
	 * @验证点:从产品页添加商品数量为1商品到购物车， 购物车增加对应商品，数量价格正确 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年2月13日
	 * @修改说明
	 */
	@Test
	public void cart_product_regular_price() {
		correFail = false;
		try {
			interfaceMethod.crearAllCartGoods(myPublicUrl, LoginName);
			op.loopGet(productUrl, 30, 3, 60);
			Log.logInfo("获取的地址为：" + productUrl);
			String getShoppingPrice = op.loopGetElementValue("shoppingPrice", "data-orgp", 20);// 商品页面单价价格
			Log.logInfo("获取的价格为:" + getShoppingPrice);
			ControlUsingMethod.SetScrollBar(400);
			op.loopClickElement("addcart", 10, 20, 90);// 加入到购物车
			String getShoppingTotal = op.loopGetElementValue("shoppingTotal", "data-orgp", 20);// 购物车页面总价
			if (getShoppingPrice.equals(getShoppingTotal)) {
				Log.logInfo("商品页面总价与购物车页面总价一致，验证通过，购物车总价为：" + getShoppingTotal + "商品页面价格：" + getShoppingPrice);
			} else {
				Log.logInfo("商品页面总价与购物车页面总价一不致，验证不通过，购物车总价为：" + getShoppingTotal + "商品页面价格：" + getShoppingPrice);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.logInfo("没有找到空页面的控件，下面内容不执行");
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 添加多件商品到购物车
	 * @验证点: 1.添加一件普通商品，数量增加到4件，点击添加到购物车， 2.商品的数量，价格显示正确（阶梯价）
	 *       3.计算的阶梯价和页面显示的阶梯价一致 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年2月13日
	 * @修改说明
	 */
	@Test
	public void cart_addLadder_price() {
		correFail = false;
		try {
			getProductPrice("n", "n", 4, "Product", "productPage");

		} catch (Exception e) {
			e.printStackTrace();
			Log.logInfo("没有找到控件，下面内容不执行");
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 添加促销商品到购物车
	 * @验证点: 1.添加促销商品到购物车，数量增加到3件 2.结算时显示正常的促销折扣价, 3.计算的总价与结算总价一致
	 *       4.显示的价格与后台对比是促销价并且不会随数量变化，并且promotion的促销标识 @使用环境： 测试环境，正式环境 @备注：
	 *       void
	 * @author zhangjun
	 * @date 2017年2月15日
	 * @修改说明
	 */

	@Test
	public void cart_promote_product() {
		correFail = false;
		try {
			getProductPrice("n", "y", 3, "promoteProduct", "productPage");

			String getIdentificationTerxt = op.loopGetElementText("clearIdentification", 10, 20);
			if (getIdentificationTerxt.equals("promotion")) {
				Log.logInfo("获取促销标识正确，验证通过，获取的标识为:" + getIdentificationTerxt);
			} else {
				Log.logInfo("获取促销标识错误，验证失败，获取的标识为:" + getIdentificationTerxt);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logInfo("没有找到控件");
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 添加清仓商品到购物车
	 * @验证点: 1.添加一个清仓商品到购物车，数量增加到2件 2.结算时每件的价格还是为原价，不会随着数量增多，单价发生变化
	 *       3.并且带有clearance标识。 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年2月15日
	 * @修改说明
	 */
	@Test
	public void cart_clearance_product() {
		correFail = false;
		try {
			getProductPrice("y", "n", 2, "clearanceProduct", "productPage");
			if (Ladderprice == true) {
				String getIdentificationTerxt = op.loopGetElementText("clearIdentification", 10, 20);
				if (getIdentificationTerxt.equals("clearance")) {
					Log.logInfo("获取清仓标识正确，验证通过，获取的标识为:" + getIdentificationTerxt);
				} else {
					Log.logInfo("获取清仓标识错误，验证失败，获取的标识为:" + getIdentificationTerxt);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.logInfo("没有找到控件");
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 添加多个商品
	 * @验证点: 1.购物车中增加商品数量 2.计算的价格与实际支付的总价正确 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年2月15日
	 * @修改说明
	 */
	@Test
	public void cart_addShooping_price() {
		correFail = false;
		try {
			getProductPrice("n", "n", 4, "Product", "shoppingPage");

		} catch (Exception e) {
			e.printStackTrace();
			Log.logInfo("没有找到控件，下面内容不执行");
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 从收藏夹添加商品到购物车
	 * @验证点: 1.从商品页添加一个商品到购物车 2.在从收藏夹中的商品添加到购物车 3.添加成功后，数量和价格正确 @使用环境：
	 *       测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年2月15日
	 * @修改说明
	 */
	@Test
	public void favorites_addShooping() {
		correFail = false;
		try {
			// getProductPrice("n", "n",4,"Product","shoppingPage");
			interfaceMethod.clearFavoritesProduct(myPublicUrl, LoginName);// 清除收藏夹记录
			interfaceMethod.crearAllCartGoods(myPublicUrl, LoginName);// 清空购物车
			op.loopGet(productUrl, 30, 3, 60);
			String getShoppingPrice = op.loopGetElementValue("shoppingPrice", "data-orgp", 20);// 商品页面单价价格
			Log.logInfo("添加在收藏夹前,商品的价格为:" + getShoppingPrice);
			ControlUsingMethod.SetScrollBar(550);
			op.loopClickElement("shoppingAddFavorites", 2, 10, 30);
			Page.pause(2);
			// 模糊匹配控件
			driver.findElement(By.xpath("//span[starts-with(@type,'1')]")).click();
			op.loopGet(favorites, 40, 3, 60);
			// a[@class='cart after_icon']
			op.loopClickElement("gotoshiiping", 10, 20, 90);
			String total = op.loopGetElementValue("shoppingTotal", "data-orgp", 400);// 页面实际获取的总价内容
			if (Double.parseDouble(getShoppingPrice) == Double.parseDouble(total)) {
				Log.logInfo("从收藏夹添加商品到购物车，验证通过，价格和数量一致，获取的总价为:" + total);
			} else {
				Log.logError("从收藏夹添加商品到购物车，验证不通过，价格和数量不一致，获取的总价为:" + total + "实际收藏夹的价格为：" + getShoppingPrice);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 删除1个商品
	 * @验证点: 1.选择某一个商品删除 2.购物车中商品消失 3. 购物车中的商品总额正确 @使用环境： 测试环境，正式环境 @备注：
	 *       delete_one_product_a与 delete_all_product_b关联性强，需按序执行
	 * @author zhangjun
	 * @date 2017年2月15日
	 * @修改说明
	 */
	@Test
	public void delete_one_product_a() {
		correFail = false;
		interfaceMethod.crearAllCartGoods(myPublicUrl, LoginName);// 清空购物车
		try {
			String[] product = { "min", "max" }; // 添加2种商品到购物车
			for (int i = 0; i < 2; i++) {
				// Page.pause(4);
				String url = interfaceMethod.getGoodUrl(myPublicUrl, product[i]);
				Log.logInfo("获取的PC商品地址为" + url);
				String getCovertUrl = SetConvertAddress(url);
				op.loopGet(getCovertUrl, 30, 2, 60);
				Page.pause(1);
				String getShoppingPrice = op.loopGetElementValue("shoppingPrice", "data-orgp", 20);// 商品页面单价价格
				Log.logInfo("商品单价为:" + getShoppingPrice);
				ControlUsingMethod.SetScrollBar(500);
				op.loopClickElement("addcart", 10, 20, 90);// 加入到购物车
			}

			String grandtotal = op.loopGetElementValue("shoppingTotal", "data-orgp", 60);// 商品总价
			String getSinglePrice = op.loopGetElementText("shoppingSignPrice", 6, 50).replace("$", "");// 第一件商品的价格

			op.loopClickElement("selectAllLink", 3, 10, 60);// 选择所有把所有已经选择的项取消掉
			Page.pause(2);
			op.loopClickElement("selectDelect", 3, 8, 60);
			Page.pause(1);
			// driver.findElement(By.xpath("//*[contains(text(),'YES')]")).click();//确认删除
			op.loopClickElement("deleteOKYes", 30, 8, 60);// 确认删除
			Page.pause(3);
			op.loopClickElement("selectAllLink", 10, 30, 60);// 在点击选择所有项

			String calculatePrice = df.format(Double.parseDouble(grandtotal) - Double.parseDouble(getSinglePrice));
			Page.pause(1);
			String grandtotal2 = op.loopGetElementValue("shoppingTotal", "data-orgp", 60);
			if (Double.parseDouble(calculatePrice) == Double.parseDouble(grandtotal2)) {
				Log.logInfo("删除一件商品，价格计算正确，验证通过,计算的价格为：" + calculatePrice + "页面上的价格:" + grandtotal2);
			} else {
				Log.logError("删除一件商品，价格计算不正确，验证不通过，计算的价格为：" + calculatePrice + "页面上的价格:" + grandtotal2);
			}

		} catch (Exception e) {
			e.printStackTrace();

			Log.logWarn("刷新页面错误");
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 删除所有商品商品后，购物车为空
	 * @验证点: 1.删除购物车中全部商品后，购物车显示为空的页面 2.显示空页面的内容“Your shopping bag is
	 *       empty!” @使用环境： 测试环境，正式环境 @备注： delete_one_product_a与
	 *       delete_all_product_b关联性强，需按序执行
	 * @author zhangjun
	 * @date 2017年2月16日
	 * @修改说明
	 */
	@Test
	public void delete_all_product_b() {
		String testExpectedStr = "";
		try {
			op.loopClickElement("selectDelect", 3, 8, 60);

			Page.pause(1);
			op.loopClickElement("deleteOKYes", 3, 8, 60);// 确认删除
			testExpectedStr = "Your shopping bag is empty!";
			Page.pause(1);
			String getEmpytText = op.loopGetElementText("shoopingEmpty", 10, 20);
			if (getEmpytText.equals(testExpectedStr)) {
				Log.logInfo("删除全部商品成功，显示空白的文案，验证通过，文案为:" + testExpectedStr);
			} else {
				Log.logError("删除全部商品失败，显示空白的文案，验证不通过，实际没有获取文本" + testExpectedStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("没有选择加载任何内容");
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 修改商品数量为0
	 * @验证点: 不允许修改，重置为1 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年2月16日
	 * @修改说明
	 */
	@Test
	public void modify_goods_amount_a() {
		correFail = false;
		try {
			interfaceMethod.crearAllCartGoods(myPublicUrl, LoginName);// 清空购物车
			String url = interfaceMethod.getGoodUrl(myPublicUrl, "one");
			Log.logInfo("获取的PC商品地址为" + url);
			String getCovertUrl = SetConvertAddress(url);
			op.loopGet(getCovertUrl, 30, 2, 60);
			// Page.pause(3);

			ControlUsingMethod.SetScrollBar(500);
			op.loopClickElement("addcart", 10, 20, 90);// 加入到购物车
			op.loopSendClean("shoppingNumber", 3, 20);
			try {
				op.loopSendKeys("shoppingNumber", "0", 1, 10);// 修改商品的数量
			} catch (Exception e) {
				Log.logInfo("已设置数量为0");
			}
			Page.pause(1);
			String getNumber = op.loopGetElementValue("shoppingNumber", "value", 20);
			if (getNumber.equals("1")) {
				Log.logInfo("设置数量框为0，默认恢复到1，验证通过");
			} else {
				Log.logError("设置数量框为0，没有恢复到1，验证失败吗，实际获取的内容为:" + getNumber);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 修改值为非数字
	 * @验证点: 不允许修改，重置为1 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年2月16日
	 * @修改说明
	 */
	@Test
	public void modify_goods_number_b() {
		try {
			try {
				op.loopSendClean("shoppingNumber", 3, 20);
				op.loopSendKeys("shoppingNumber", "ab", 1, 10);// 修改商品的数量
			} catch (Exception e) {
				Log.logInfo("已设置数量为为ab");
			}
			Page.pause(1);
			String getNumber = op.loopGetElementValue("shoppingNumber", "value", 20);
			if (getNumber.equals("1")) {
				Log.logInfo("设置数量框为ab，默认恢复到1，验证通过");
			} else {
				Log.logError("设置数量框为ab，没有恢复到1，验证失败吗，实际获取的内容为:" + getNumber);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 修改库存为1001
	 * @验证点: 1.设置商品数量为999 2.点击按钮增加到1001 3.默认数量恢复到1 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年2月16日
	 * @修改说明
	 */
	@Test
	public void modify_goods_inventory_c() {
		try {
			op.loopSendClean("shoppingNumber", 3, 20);
			op.loopSendKeys("shoppingNumber", "999", 1, 10);// 修改商品的数量
			Page.pause(3);
			op.loopClickElement("shoppingNumberBtn", 2, 5, 6);
			Page.pause(3);
			/*op.loopClickElement("shoppingNumberBtn", 2, 5, 6);
			Page.pause(3);*/
			String getNumber = op.loopGetElementValue("shoppingNumber", "value", 20);
			if (getNumber.equals("1")||getNumber.equals("1000")) {
				Log.logInfo("设置数量框为1001，默认恢复到1，验证通过");
			} else {
				Log.logError("设置数量框为1001，没有恢复到1，验证失败吗，实际获取的内容为:" + getNumber);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 未登录时商品移到收藏夹
	 * @验证点: 1.未登录，添加商品到购物车 2.跳转到登录页面，登录后跳回购物车页 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年2月16日
	 * @修改说明
	 */
	@Test
	public void favorites_product_notlogin() {
		correFail = false;
		try {
			String getmyPublicUrl = myPublicUrl2 + "/user/login/sign";
			Log.logInfo("获取的跳转页面地址为:" + getmyPublicUrl);
			op.loopGet(getmyPublicUrl, 20, 2, 60);
			if (!op.isElementPresent("loginPage_loginEmail")) {
				Log.logInfo("是已登录状态，需要退出");
				ControlUsingMethod.SetScrollBar(500);
				op.loopClickElement("LoGout", 2, 8, 30);
			} else {
				Log.logInfo("已经是未登录状态,不需要退出");
			}
			String url = interfaceMethod.getGoodUrl(myPublicUrl, "one");
			Log.logInfo("获取的PC商品地址为" + url);
			String getCovertUrl = SetConvertAddress(url);
			op.loopGet(getCovertUrl, 30, 2, 60);
			ControlUsingMethod.SetScrollBar(500);
			// Page.pause(3);
			op.loopClickElement("shoppingAddFavorites", 2, 10, 30);// 点击收藏按钮
			// controlmethod.WebDriverJumpDelay("shoppingAddFavorites");
			if (op.isElementPresent("loginPage_loginEmail", 60)) {
				Log.logInfo("跳转到登录页成功");
				op.loopSendKeys("loginPage_loginEmail", LoginName, 3, 30);
				op.loopSendKeys("loginPage_loginPwd", "123456", 3, 30);
				String requestId = Pub.getRandomString(5);
				Log.logInfo("myPublicUrl2:" + myPublicUrl2);
				;
				;
				;
				String logincode = InterfaceMethod.IF_Verification(driver, myPublicUrl2, requestId, getkeys);
				if (op.isElementPresent("loginCode")) {
					op.loopSendKeys("loginCode", logincode, 3, 15);
				} else {
					Log.logInfo("未开启验证码输入框，不进行输入");
				}
				op.loopClickElement("loginPage_signInBtn", 10, 30, 100);
			} else {
				Log.logError("没有跳转到登录页，以下内容不执行");
			}
			// Page.pause(4);
			if (op.isElementPresent("addcart", 60)) {
				Log.logInfo("已经跳转到了商品页面，验证通过");
			} else {
				Log.logError("没有跳转到了商品页面，验证失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 转换地址，
	 * @验证点: 把取得的PC商品地址转换成wap地址 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年2月15日
	 * @修改说明
	 */
	public String SetConvertAddress(String ProductUrl) {
		/* 正则表达取出uid */
		/*
		 * Pattern p = Pattern.compile("\\d{5,7}"); Matcher m =
		 * p.matcher(ProductUrl);// 得到商品地址，目前还是得到的PC地址 if (m.find()) {
		 * productUrl =myPublicUrl2+"/cheapest/stylish-round-neck-long-sleeve-"+
		 * m.group()+".html?"+interfaceMethod;
		 * Log.logInfo("取得的wap商品地址为："+productUrl); }
		 */

		return ProductUrl.replace("www", "m") + "?" + timeStamp;
		
	}

	/**
	 * 
	 * @测试点: 得到商品阶梯价
	 * @验证点: 计算阶梯价与自己计算的价格是否一致
	 * @param is_clearance
	 *            是否清仓Y清仓，N不清仓
	 * @param is_promote
	 *            是否促销Y促销，N不促销
	 * @param SetNumber
	 *            设置数量
	 * @param type
	 *            Product:普通商品 promoteProduct：促销商品 clearanceProduct：清仓商品
	 *            测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年2月15日
	 * @修改说明
	 */
	public void getProductPrice(String is_clearance, String is_promote, int SetNumber, String type, String flage) {
		String total = "";
		String calculationPrice = "";
		try {
			interfaceMethod.crearAllCartGoods(myPublicUrl, LoginName);// 清空购物车
			Map<String, Object> getProduct = interfaceMethod.getGoods(myPublicUrl, is_clearance, is_promote);
			if (getProduct.size() > 1) {
				Ladderprice = true;// 说明清仓商品有大于2的数量
				String goodsUrl = (String) getProduct.get("url_title");
				String goodsSku = (String) getProduct.get("goods_sn");// 取出商品的sku
				Log.logInfo("获取的PC商品为：" + goodsUrl);
				String getCovertUrl = SetConvertAddress(goodsUrl);
				Log.logInfo("获取的商品地址为" + getCovertUrl);
				op.loopGet(getCovertUrl, 30, 2, 100);
				Page.pause(2);
				String getShoppingPrice = op.loopGetElementValue("shoppingPrice", "data-orgp", 30);// 商品页面单价价格

				if (type == "Product") {// 普通商品
					double ladder = interfaceMethod.getLadderprice(myPublicUrl, goodsSku, SetNumber); // 取出给定数量的阶梯价
					Log.logInfo("设置的数量为：" + SetNumber + "取出的阶梯价格为：" + ladder);
					calculationPrice = df.format(ladder * SetNumber);// 自己计算阶梯价格
					Page.pause(2);

				} else if (type == "promoteProduct" || type == "clearanceProduct") {// 促销商品,清仓商品没有阶梯价，按页面价格计算
					calculationPrice = df.format(Double.parseDouble(getShoppingPrice) * SetNumber);// 自己计算阶梯价格,页面单价*数量
				}
				/************************* 区分在商品页增加商品数量和在购物车页面增加商品数量 ******************************/
				if (flage.equals("productPage")) {// 产品页增加产品数量
					Log.logInfo("在商品页面增加商品的数量");
					op.loopSendClean("shoppingAddNumber", 3, 15); // 在商品页设置商品数量
					op.loopSendKeysClean("shoppingAddNumber", String.valueOf(SetNumber), 20);
					op.loopClickElement("addcart", 10, 20, 60);// 加入到购物车
				} else if (flage.equals("shoppingPage")) {// 购物车页面增加产品数量
					Log.logInfo("在购物车页面增加商品的数量");
					ControlUsingMethod.SetScrollBar(700);
					op.loopClickElement("addcart", 10, 20, 60);// 加入到购物车
					op.loopSendClean("shoppingNumber", 2, 10);
					op.loopSendKeysClean("shoppingNumber", String.valueOf(SetNumber), 10);
				}
				Page.pause(2);
				total = op.loopGetElementText("shoppingTotal", 10, 50).replace("$", "");
				// total = op.loopGetElementValue("shoppingTotal", "orgp", 3,
				// 400);// 页面实际获取的总价内容
				if (calculationPrice.equals(total)) {
					Log.logInfo("计算的价格与实际页面的价格一致，验证通过，页面获取的总价格为：" + total + "计算的价格为:" + calculationPrice + "设置的件数为:"
							+ SetNumber);
				} else {
					Log.logError("计算的价格与实际页面的价格不一致，验证不通过，页面获取的价格为:" + total + "计算的价格为:" + calculationPrice + "设置的件数为:"
							+ SetNumber);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logError("计算的总价为：" + calculationPrice);
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 取出固定的地址
	 * @验证点: 为了方便调用PC的接口，方便使用 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年2月13日
	 * @修改说明
	 */

	public void urlInit() {

		if (baseURL.contains(".a.")) {
			myPublicUrl = "http://rosewholesale.com.d.s1.egomsl.com/";// 调用的和PC接口一样
			myPublicUrl2 = "http://m.wap-rosewholesale.com.a.s1cg.egomsl.com";// 获取验证码的接口
			// 转换商品的地址
			getProduct = interfaceMethod.getGoodUrl(myPublicUrl, "min");
			SetConvertAddress(getProduct);
			favorites = "http://m.wap-rosewholesale.com.a.s1cg.egomsl.com/user/collect/list";

		} else if (baseURL.contains(".trunk.")) {
			myPublicUrl = "http://rosewholesale.com.trunk.s1.egomsl.com/";// 调用的和PC接口一样
			myPublicUrl2 = "http://www.wap-rosewholesale.com.trunk.s1cg.egomsl.com";// 获取验证码的接口
			// 转换商品的地址
			getProduct = interfaceMethod.getGoodUrl(myPublicUrl, "min");
			SetConvertAddress(getProduct);
			favorites = "http://m.wap-rosewholesale.com.trunk.s1cg.egomsl.com/user/collect/list";
		} else {
			myPublicUrl = "https://rosewholesale.com/";// 调用的和PC接口一样
			myPublicUrl2 = "https://m.rosewholesale.com";// 获取验证码的接口
			getProduct = interfaceMethod.getGoodUrl(myPublicUrl, "min");
			productUrl=SetConvertAddress(getProduct);
			favorites = "https://userm.rosewholesale.com/user/collect/list";
		}
	}

}
