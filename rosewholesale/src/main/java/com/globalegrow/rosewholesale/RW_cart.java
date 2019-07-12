package com.globalegrow.rosewholesale;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.globalegrow.base.Startbrowser;
import com.globalegrow.code.Page;
import com.globalegrow.custom.ControlUsingMethod;
import com.globalegrow.custom.InterfaceMethod;
import com.globalegrow.util.Log;
import com.globalegrow.util.Pub;


public class RW_cart extends Startbrowser {
	private String className = GetClassName();
	private String testCaseProjectName = "rosewholesale";
	public String baseURL0 = "";
	private String baseURL;
	private String getkeys;
	private String classNameShort;
	private String myPublicUrl;
	private String timeStamp;// 时间戳
	private String collectionUrl;// 收藏夹地址
	private String saveForLateUrl;// saveforlater的地址
	private String saveloginUrl;// 登录地址
	private String FavoritePrice;// 收藏夹商品的价格
	private String loginName = "autotest03@globalegrow.com";// 登录账户
	DecimalFormat df = new DecimalFormat("#0.00");
	int SetNumber;
	private String getPrice[] = new String[4];
	private String myPublicUrl2;// 主要为了区分正式和测试环境跳转到首页时,正式环境会带一个www
	InterfaceMethod interfaceMethod;
	private String getproducturl;// 获取商品的地址
	private String productdatapice;// 商品数据的原始价格
	// 主要为了获取到地址后转换地址
	private long explicitWaitTimeoutLoop = 40;
	private double rate;
	private String rates;

	@Parameters({ "testUrl" })
	private RW_cart(String testUrl) {
		moduleName = className.substring(className.lastIndexOf(".") + 1); // 必要
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
			try {
				op.loopGet(baseURL, 40, 3, 60);
			} catch (Exception e) {
				Log.logInfo("页面已经加载成功了，但是还是提示没有加载完，不影响继续执行下一步");
			}

			Log.logInfo("baseURL:" + baseURL);
			interfaceMethod = new InterfaceMethod();
			urlInit();// 初始化URL
			getkeys = keys;
			login();
			// 用于时间戳
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
			Thread.sleep(3000);
			Log.logInfo("已经执行afterclass");
			driver.quit();
			beforeTestRunFlag = false; // 必要
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Log.logInfo("(" + methodNameFull + ")...afterClass stoop...\n\n");
		}
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
		boolean loginFlag = true;// 判断是否有登录
		try {
			Page.pause(4);
			number++;
			String logincode = InterfaceMethod.IF_Verification(driver, myPublicUrl, requestId, getkeys, "login");
			if (logincode.equals("verify") && number < 6) {
				op.navigateRefresh(50, 2, 30);
				Page.pause(5);
				login();
			} else {
				op.loopSendKeys("Login_EmailEnter", "autotest03@globalegrow.com", 3, 20);
				op.loopSendKeys("Login_PasswordEnter", "123456", 3, 20);

				if (op.isElementPresent("Login_verification", 40)) {// 判断验证码输入框是否被关闭
					op.loopSendKeys("Login_verification", logincode, 3, 20);
				} else {
					Log.logInfo("验证码输入框已被关闭，不输入验证码登录");
				}
				op.loopClickElement("Login_SignBtn", 300, 1, 60);
				for (int i = 0; i < 10; i++) {// 判断登录成功
					Page.pause(2);
					Log.logInfo("第" + i + "次");
					String LoginStats = op.loopGetElementText("signlinkName", 10, 10);
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

			if (baseURL.contains("/es/")) {
				ControlUsingMethod.setDisplayCSS(driver,
						"#js_topOperal > ul > li:nth-child(3) > div.lh.bzlist.jd_bzList");
				op.loopClickElement("esEUR", 3, 20, explicitWaitTimeoutLoop);
			}
		} catch (Exception e) {

			e.printStackTrace();
			//driver.quit();
		} finally {
			if (baseURL.contains("/es/")) {// 针对多语言的换算
				rate = interfaceMethod.rates(rates);// 获取的价格
			}
		}

	}

	int number = 0;

	/**
	 * 
	 * @测试点: 空购物车显示
	 * @验证点: 1.页面中间显示一个空购物车文案：Your shopping cart is empty @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月24日
	 */
	@Test
	public void cart_empty_a() {
	
	
		String testExpectedStr = "Your shopping cart is empty.";
		String getEmpty = "";
		
		try {
			interfaceMethod.crearAllCartGoods(myPublicUrl, loginName);
			op.loopClickElement("navBar_MyCart", 3, 40, 60);
			Page.pause(1);
			getEmpty = op.loopGetElementText("cartPage_EmptyCartNote", 10, 60);
			if (baseURL.contains("/es/")) {
				testExpectedStr = "Su carrito está vacío.";
			}
			Assert.assertEquals(getEmpty, testExpectedStr);

		} catch (Exception e) {
			e.printStackTrace();
			Log.logInfo("获取文案错误，实际获取的文案为：" + getEmpty);
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 空购物车显示
	 * @验证点: 点击购物车中的continue shopping 按钮,页面跳转到首页 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月26日
	 */
	@Test
	public void cart_empty_b() {

		String getcurrenturl = "";
		try {
			if (op.isElementPresent("cartPage_ContinueShopping", 20)) {
				Log.logInfo("点击继续购物的按钮");
				op.loopClickElement("cartPage_ContinueShopping", 3, 10, 40);
			} else {
				// 因为有可能页面延时，原本已经点击的按钮，页面已经跳转成功了，下次再重新执行时会报错，现在做容错
				Log.logInfo("已经跳转到了首页");
			}
			Page.pause(5);
			getcurrenturl = driver.getCurrentUrl();
			Log.logInfo("获取到的浏览器地址为:" + getcurrenturl);
			String testExpectString = "https://" + myPublicUrl2;
			if(baseURL.contains("/es/")){
				testExpectString = "https://" + myPublicUrl2;
				if(baseURL.contains(".trunk.")){
					testExpectString = "http://" + myPublicUrl2;
				}
			}
			Log.logInfo("需要对比的浏览器地址:" + testExpectString);
			if (getcurrenturl.equals(testExpectString)) {
				Log.logInfo("获取的地址与对比的地址一致，验证通过");
			} else {
				Log.logError("获取的地址与对比的地址不一致，验证失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @测试点: 添加商品到购物车
	 * @验证点: 1.添加一件普通商品商品到购物车，2.价格正确 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月26日
	 */
	@Test
	public void cart_product_regular_price() {
		String total = "";
		try {
			interfaceMethod.crearAllCartGoods(myPublicUrl, loginName);// 清空购物车
			// Page.pause(5);
			if (baseURL.contains("/es/")) {
				getproducturl = interfaceMethod.getGoodUrl("one", myPublicUrl, "n", "n", "EUR");
				interfaceMethod.convertAddress(op, getproducturl);
			} else {
				getproducturl = interfaceMethod.getGoodUrl("one", myPublicUrl, "n", "n");
				Log.logInfo("取得的 商品地址为:" + getproducturl);
				op.loopGet(getproducturl, 80, 3, 60);
			}
			String OriginalPrice = op.loopGetElementText("unitPrice", 10, 40).replace("$", "").replace("€", "");
			Log.logInfo("获取商品的单价为:" + OriginalPrice);
			op.loopClickElement("addToBag", 4, 10, 30);// 点击添加至购物车按钮
			// total = op.loopGetElementValue("shoppingtotal", "orgp", 3, 400);
			total = op.loopGetElementText("shoppingtotal", 10, 20).replace("$", "").replace("€", "");
			Assert.assertEquals(OriginalPrice, total);
		
		} catch (Exception e) {
			e.printStackTrace();
			Log.logError("获得的总价为：" + total);
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 添加商品到购物车
	 * @验证点:1.添加商品到购物车2.数量增加到4个，计算总价格计算正确（阶梯价） @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月26日
	 */
	@Test
	public void cart_product_Ladder_price() {
		int SetNumber = 4;
		try {
			assertLadderPrice("n", "n", SetNumber, "shoppingcart");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 添加促销商品到购物车
	 * @验证点: 1.添加促销商品到购物车，数量增加到4件 2.结算时显示正常的价格
	 *       3.计算的总价格结算总价一致，不会受数量多少，影响单价 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月26日
	 */
	@Test
	public void cart_product_promote_product() {
		int SetNumber = 4;
		String getPagePrice = "";
		String totalPrice = "";
		String prouctPice = "";
		Map<String, Object> getproduct = null;
		try {
			interfaceMethod.crearAllCartGoods(myPublicUrl, loginName);// 清空购物车
			if (baseURL.contains("/es/")) {
				getproduct = interfaceMethod.getGoods(myPublicUrl, "n", "y", "EUR");
				getproducturl = (String) getproduct.get("url_title");
				prouctPice = (String) getproduct.get("org_price");
				interfaceMethod.convertAddress(op, getproducturl);
			} else {
				getproducturl = interfaceMethod.getGoodUrl("min", myPublicUrl, "n", "y");
				op.loopGet(getproducturl, 50, 3, 60);
			}

			String OriginalPrice = op.loopGetElementText("unitPrice", 10, 20).replace("$", "").replace("€", "");
			Log.logInfo("获取的单价为:" + OriginalPrice);
			op.loopClickElement("addToBag", 4, 10, 60);// 点击添加至购物车按钮
			op.loopSendKeysClean("numberinputbox", String.valueOf(SetNumber), 90); // 增加商品数量
			Page.pause(10);
			if (baseURL.contains("/es/")) {
				totalPrice = df.format(Double.parseDouble(prouctPice) * SetNumber * rate);// 原价（美元价格）*数量*汇率
			} else {
				totalPrice = df.format(Double.parseDouble(OriginalPrice) * SetNumber);
			}
			getPagePrice = op.loopGetElementText("shoppingtotal", 10, 20).replace("$", "").replace("€", "");
			Log.logInfo("计算的总价" + totalPrice);
			Log.logInfo("实际获取的价格" + getPagePrice);
			Assert.assertEquals(totalPrice, getPagePrice);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	/**
	 * 
	 * @测试点: 添加清仓商品到购物车
	 * @验证点: 1.添加一个清仓商品到购物车，数量增加到2件 2.结算时每件的价格还是为原价，不会随着数量增多，单价发生变化 @使用环境： 测试环境
	 * @备注：在前一个用例执行时，需要先清空购物车
	 * @author zhangjun
	 * @date 2016年12月26日
	 */
	@Test
	public void cart_product_clearance_product() {

		String getPagePrice = "";
		String totalPrice = "";
		String getproducturl = "";
		int SetNumber = 2;
		double ladder;
		Map<String, Object> getproduct = null;
		try {

			interfaceMethod.crearAllCartGoods(myPublicUrl, loginName);// 清空购物车
			if (baseURL.contains("/es/")) {
				getproduct = interfaceMethod.getGoods(myPublicUrl, "y", "n", "EUR");
				if (getproduct.size() > 1) {
					getproducturl = (String) getproduct.get("url_title");
					productdatapice = (String) getproduct.get("org_price");
					interfaceMethod.convertAddress(op, getproducturl);
				}

			} else {
				getproduct = interfaceMethod.getGoods(myPublicUrl, "y", "n");
				if (getproduct.size() > 1) {
					getproducturl = (String) getproduct.get("url_title");
					getproducturl = getproducturl + "?" + timeStamp;// 刷新缓存'
					Log.logInfo("获取的测试商品地址为：" + getproducturl + "?" + timeStamp);
					op.loopGet(getproducturl, 50, 3, 80);
				}
			}

			if (getproduct.size() > 1) {
				String goodsSku = (String) getproduct.get("goods_sn");
				if (baseURL.contains("/es/")) {
					ladder = interfaceMethod.getLadderprice(myPublicUrl, goodsSku, SetNumber, "EUR");
				} else {
					ladder = interfaceMethod.getLadderprice(myPublicUrl, goodsSku, SetNumber);
				}
				String OriginalPrice = op.loopGetElementText("unitPrice", 10, 20).replace("$", "").replace("€", "");
				Log.logInfo("获取的单价为:" + OriginalPrice);
				op.loopClickElement("addToBag", 4, 10, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
				Page.pause(3);
				op.loopSendKeysClean("numberinputbox", String.valueOf(SetNumber), 5); // 增加商品数量
				Page.pause(10);
				if (ladder == 0.0) {// 说明没有阶梯价
					if (baseURL.contains("/es/")) {
						totalPrice = df.format(Double.parseDouble(productdatapice) * SetNumber * rate);
					} else {
						totalPrice = df.format(Double.parseDouble(OriginalPrice) * SetNumber);
					}

				} else {// 取到了阶梯价
					Log.logInfo("有阶梯价格，获得阶梯的价格为:" + ladder);
					if (baseURL.contains("/es/")) {
						totalPrice = df.format(ladder * SetNumber * rate);
					} else {
						totalPrice = df.format(ladder * SetNumber);
					}
				}
				getPagePrice = op.loopGetElementText("shoppingtotal", 10, 20).replace("$", "").replace("€", "");
				Log.logInfo("自己计算的总价" + totalPrice);
				Log.logInfo("获取页面的总价" + getPagePrice);
				Assert.assertEquals(totalPrice, getPagePrice);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 重复添加商品
	 * @验证点:从商品页重复添加4个商品,商品数量叠加价格正确 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月7日
	 * @修改说明
	 */
	@Test
	public void cart_product_repeat() {
		int SetNumber = 4;
		try {
			interfaceMethod.crearAllCartGoods(myPublicUrl, loginName);// 清空购物车
			assertLadderPrice("n", "n", SetNumber, "product");
		} catch (Exception e) {
			e.printStackTrace();
		
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 多个商品的阶梯价
	 * @验证点: 加载满足条件的商品， 计算满足条件的商品多个数量阶段价
	 * @param is_clearance
	 *            是否清仓
	 * @param is_promote
	 *            是否促销
	 * @param SetNumber
	 *            设置商品的数量
	 * @param types
	 *            是哪个页面内容，product：从商品页面添加商品 ，不是就为购物车页面添加商品数量 @备注： void
	 * @author zhangjun
	 * @date 2017年1月9日
	 * @修改说明
	 */
	@Test
	public void assertLadderPrice(String is_clearance, String is_promote, int SetNumber, String types) {
		String total = "";
		String calculationPrice = "";
		Map<String, Object> goods = null;// 商品
		String goodsUrl = "";// 商品的url
		String goodsSku = "";// sku商品
		double ladder;

		try {
			interfaceMethod.crearAllCartGoods(myPublicUrl, loginName);// 清空购物车
			if (baseURL.contains("/es/")) {
				goods = interfaceMethod.getGoods(myPublicUrl, is_clearance, is_promote, "EUR");// 获取商品
				goodsUrl = (String) goods.get("url_title");
				interfaceMethod.convertAddress(op, goodsUrl);
			} else {
				goods = interfaceMethod.getGoods(myPublicUrl, is_clearance, is_promote);
				goodsUrl = (String) goods.get("url_title");
				goodsUrl = goodsUrl + "?" + timeStamp;
				op.loopGet(goodsUrl, 60, 2, 80);
				Log.logInfo("取得的 商品地址为:" + goodsUrl);
			}
			goodsSku = (String) goods.get("goods_sn");

			if (types == "product") {// 产品详情页增加多件商品
				op.loopSendClean("productNumber", 3, 100);
				Page.pause(3);
				op.loopSendKeys("productNumber", String.valueOf(SetNumber), 3, explicitWaitTimeoutLoop);
				op.loopClickElement("addToBag", 4, 10, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
			} else {// 购物车页面增加多件商品
				op.loopClickElement("addToBag", 4, 10, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
				op.loopSendKeysClean("numberinputbox", String.valueOf(SetNumber), explicitWaitTimeoutLoop); // 增加商品数量
			}
			Page.pause(4);// 添加商品数量后，js执行耗时很长
			if (baseURL.contains("/es/")) {
				ladder = interfaceMethod.getLadderprice(myPublicUrl, goodsSku, SetNumber, "EUR");
				calculationPrice = df.format(ladder * SetNumber * rate);// 计算阶梯价格
																		// "总价 =
																		// 原单价*数量*汇率（取两位小数点）"

			} else {
				ladder = interfaceMethod.getLadderprice(myPublicUrl, goodsSku, SetNumber);
				calculationPrice = df.format(ladder * SetNumber);// 计算阶梯价格
			}

			Page.pause(4);
			total = op.loopGetElementText("shoppingtotal",20, 40).replace("$", "").replace("€", "");
			// Assert.assertEquals(total, calculationPrice);
			if (total.equals(calculationPrice)) {
				Log.logInfo("计算的阶梯价与显示的阶梯价相同，验证通过");
			} else {
				Log.logError("计算的阶梯价与显示不相同，验证失败，计算的阶梯价为：" + calculationPrice + "页面显示的价格为:" + total);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.logError("计算的总价为：" + calculationPrice);
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 合并商品到购物车
	 * @验证点: 1.未登录，添加商品到购物车2.登录后，商品依然存在购物车内 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月26日
	 */
	@Test
	public void mergeshoppingcart_product() {
		boolean goodstitle = false;
		boolean goodstotal = false;
		try {
			if (op.isElementPresent("signlink", 20)) {
				Signout_Success();// 登出
			}
			if (baseURL.contains("/es/")) {
				getproducturl = interfaceMethod.getGoodUrl("one", myPublicUrl, "n", "n", "EUR");
				interfaceMethod.convertAddress(op, getproducturl);
			} else {
				getproducturl = interfaceMethod.getGoodUrl("one", myPublicUrl, "n", "n");
				Log.logInfo("取得的 商品地址为:" + getproducturl);
				op.loopGet(getproducturl + "?" + timeStamp, 50, 3, 90);
			}
			op.loopClickElement("addToBag", 4, 10, 60);// 点击添加至购物车按钮
			String goods[] = getGoods();// 获取商品的标题
			// 登录
			op.loopClickElement("signlinkCart", 1, 20, 60);
			login();
			op.loopClickElement("navBar_MyCart", 3, 20, 20);
			String te = op.loopGetElementText("Commoditytitle", 30, 60);
			goodstitle = goods[0].equals(te);
			goodstotal = goods[1]
					.equals(op.loopGetElementText("shoppingtotal", 10, 60).replace("$", "").replace("€", ""));
			if (goodstitle && goodstotal) {
				Log.logInfo("查找到未登录时添加到购物车的商品，验证通过");
			} else {
				Log.logError("没有查找到未登录时，添加到购物车的商品，验证失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logError("goodstitle:" + goodstitle + "goodstotal" + goodstotal);
			Assert.fail();
		}

	}

	/**
	 * 
	 * @测试点: 删除1个商品
	 * @验证点: 1.选择某一个商品删除2.购物车中商品消失3. 购物车中的商品总额正确 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月2日
	 */
	@Test
	public void delete_one_product_a() {
		interfaceMethod.crearAllCartGoods(myPublicUrl, loginName);// 清空购物车
		try {
			String[] product = { "one", "min", "max" }; // 添加3种商品到购物车
			for (int i = 0; i < 3; i++) {
				getproducturl = interfaceMethod.getGoodUrl(product[i], myPublicUrl, "n", "n");
				Page.pause(2);
				if (baseURL.contains("/es/")) {
					interfaceMethod.convertAddress(op, getproducturl);
				} else {
					op.loopGet(getproducturl, 50, 3, 80);
				}
				getPrice[i] =op.loopGetElementValue("unitPrice", "data-orgp", 20);
				op.loopClickElement("addToBag", 4, 10, 60);// 点击添加至购物车按钮
			}

			String grandtotal = op.loopGetElementValue("grandTotal","data-orgp", 20);		
			String getSinglePrice = op.loopGetElementValue("singlePrice","data-orgp", 20);// 第一件商品的价格

			op.loopClickElement("signleDelete", 3, 8, 60);
			op.loopClickElement("ConfirmDeletion", 3, 8, 60);// 确认删除
			String calculatePrice = df.format(Double.parseDouble(grandtotal) - Double.parseDouble(getSinglePrice));
			Page.pause(7);
			String grandtotal2 = op.loopGetElementValue("grandTotal", "data-orgp", 20);
			if (calculatePrice.equals(grandtotal2)) {
				Log.logInfo("删除一件商品，价格计算正确，验证通过");
			} else {
				Log.logError("删除一件商品，价格计算不正确，验证不通过，计算的价格为：" + calculatePrice + "页面上的价格:" + grandtotal2);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: clear all删除
	 * @验证点:1.选中购物车中的商品，点击clear all,弹出的弹窗中确认删除2.选中的商品从购物车中消失，
	 *                          subtotal处金额正确 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月2日
	 */
	@Test
	public void delete_all_product_b() {
		try {
			if (op.isElementPresent("cartPage_EmptyCartNote", 20)) {
				Log.logInfo("删除页面，商品为空，目前没有处理，但是购物车已经为空了");
			} else {
				op.navigateRefresh(30, 2, 50);
				String grandtotal =op.loopGetElementValue("grandTotal", "data-orgp", 20);
				
				String getSinglePrice = op.loopGetElementValue("singlePrice", "data-orgp", 50);// 第一件商品的价格
				Page.pause(2);
				boolean Enabled = driver
						.findElement(By.cssSelector(
								"#cart_list > div > section > div:nth-child(2) > ul:nth-child(1) > li.item.item0 > input"))
						.isEnabled();
				if (Enabled) {
					op.loopClickElement("Uncheck", 10, 30, explicitWaitTimeoutLoop); // 第一款商品																					// 取消勾选
				} else {
					Log.logInfo("已经取消了勾选");
				}

				Page.pause(3);
				op.loopClickElement("delectALL", 10, 30, explicitWaitTimeoutLoop);
				Page.pause(3);
				op.loopClickElement("confirmALL", 10, 30, explicitWaitTimeoutLoop);// 确认删除
				Page.pause(3);
				driver.findElement(By.cssSelector("#cart_list > div > section > div:nth-child(2) > ul:nth-child(1) > li.item.item0 > input")).click();
				//op.loopClickElement("Uncheck", 10, 40, explicitWaitTimeoutLoop); // 第一款商品																				// 在勾选
				Page.pause(6);
				String grandtotal2 = op.loopGetElementValue("grandTotal", "data-orgp", 60);
				if (grandtotal2.equals(getSinglePrice)) {
					Log.logInfo("商品已确认删除，剩余的金额正确，验证通过");
				} else {
					Log.logError("商品已确认删除，剩余的金额不正确，验证不通过，实际的金额应该为：" + grandtotal + "页面上的价格:" + grandtotal2);
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点:未选择商品删除
	 * @验证点:提示：Are you sure clear the goods you choose in the cart? @使用环境：
	 *             测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月4日
	 * @修改说明
	 */
	@Test
	public void delete_all_product_c() {
		String testExpectedStr = "Are you sure clear the goods you choose in the cart?";
		String getMessage = "";
		try {
			if (op.isElementPresent("cartPage_EmptyCartNote", 20)) {
				Log.logInfo("删除页面，商品为空，目前没有处理，但是购物车已经为空了");
			} else {
				boolean Enabled = driver
						.findElement(By.cssSelector("#cart_list > div > section > div:nth-child(2) > ul:nth-child(1) > li.item.item0 > input"))
						.isEnabled();
				if (Enabled) {
					Log.logInfo("商品已经被勾选了");
				} else {
					op.loopClickElement("Uncheck", 1, 30, explicitWaitTimeoutLoop); // 第一款商品
																					// 取消勾选
				}
				op.loopClickElement("delectALL", 7, 50, explicitWaitTimeoutLoop);
				getMessage = op.loopGetElementText("saveMessage", 15, 40);
				Log.logInfo("获得的商品" + getMessage);
				if (baseURL.contains("/es/")) {
					testExpectedStr = "¿Estás seguro de que desea eliminar los productos que ha elegido en su carrito?";
				}
				Assert.assertEquals(getMessage, testExpectedStr);
			}
	
		} catch (Exception e) {
			e.printStackTrace();
			Log.logError("实际获取的文本为:" + getMessage);
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 查看历史记录
	 * @验证点: 购物车的浏览记录页，刚刚浏览的记录 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月4日
	 * @修改说明
	 */
	@Test
	public void check_historical_record_d() {
		interfaceMethod.crearAllCartGoods(myPublicUrl, loginName);// 清空购物车
		Log.logInfo("分割线以下是删除了cookie的功能*************************************");
		driver.manage().deleteCookieNamed("browserHistories");// 清除历史记录
		// driver.manage().deleteAllCookies();// 删除所有cookies
		for (org.openqa.selenium.Cookie cookie : driver.manage().getCookies()) {
			Log.logInfo("作用域：" + cookie.getDomain() + ", 名称：" + cookie.getName() + ", 值：" + cookie.getValue() + ", 范围："
					+ cookie.getPath());
		}
		try {
			String[] product = { "one", "max" }; // 添加3种商品到购物车
			for (int i = 0; i < 2; i++) {
				if (baseURL.contains("/es/")) {
					getproducturl = interfaceMethod.getGoodUrl(product[i], myPublicUrl, "n", "n", "EUR");
					interfaceMethod.convertAddress(op, getproducturl);
					;
				} else {
					getproducturl = interfaceMethod.getGoodUrl(product[i], myPublicUrl, "n", "n");
					op.loopGet(getproducturl, 30, 3, 50);
				}
				Page.pause(2);
				getPrice[i] = op.loopGetElementText("unitPrice", 10, 20).replace("$", "").replace("€", "");
				Log.logInfo("第" + i + "个商品价格为：" + getPrice[i]);
				op.loopClickElement("addToBag", 4, 10, 200);// 点击添加至购物车按钮
			}
			Page.pause(2);
			List<WebElement> getListPrice = op.getElements("historyList");
			int j = 0;
			for (int i = getListPrice.size() - 1; i >= 0; i--) {
				Page.pause(2);
				String price = getListPrice.get(i).findElement(By.cssSelector("p > strong.my_shop_price")).getText()
						.replace("$", "").replace("€", "");
				;
				Log.logInfo("历史记录浏览框中获取第" + i + "个的商品价格为:" + price);
				if (getPrice[j].equals(price)) {// 1
					Log.logInfo("商品页预先存入的值与历史记录中相同，且时间最近排在最前面，验证通过");
					j++;
				} else {
					Log.logError("商品页预先存入的值与历史记录中不相同，且时间不是排在最前面，验证不通过，获取的价格" + price);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @测试点: 删除商品后，购物车为空
	 * @验证点:删除商品 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月2日
	 */
	@Test
	public void delete_product() {
		String testExpectedStr ="Your shopping cart is empty.";
		interfaceMethod.crearAllCartGoods(myPublicUrl, loginName);// 清空购物车
		String getEmpty = "";
		try {
			if (baseURL.contains("/es/")) {
				getproducturl = interfaceMethod.getGoodUrl("one", myPublicUrl, "n", "n", "EUR");
				interfaceMethod.convertAddress(op, getproducturl);
			} else {
				getproducturl = interfaceMethod.getGoodUrl("one", myPublicUrl, "n", "n");
				Log.logInfo("取得的 商品地址为:" + getproducturl);
				op.loopGet(getproducturl, 50, 30, 60);
			}

			op.loopClickElement("addToBag", 4, 10, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
			op.loopClickElement("delectALL", 3, 8, 60);
			op.loopClickElement("confirmALL2", 3, 8, 60);// 确认删除
			if(op.isElementPresent("cartPage_EmptyCartNote",20)){
				getEmpty = op.loopGetElementText("cartPage_EmptyCartNote", 6, 60);
			}
			if (baseURL.contains("/es/")) {
				testExpectedStr = "Su carrito está vacío.";
			}
			Assert.assertEquals(getEmpty, testExpectedStr);
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 修改购物车商品数量为0
	 * @验证点: 不允许修改，重置为1 @使用环境： 测试环境 @备注：
	 *       modify_goods_amount_a与modify_goods_number_b 相关，需按需执行
	 * @author zhangjun
	 * @date 2017年1月3日
	 */
	@Test
	public void modify_goods_amount_a() {
		interfaceMethod.crearAllCartGoods(myPublicUrl, loginName);// 清空购物车
		String getText = "";
		try {
			if (baseURL.contains("/es/")) {
				getproducturl = interfaceMethod.getGoodUrl("one", myPublicUrl, "n", "n", "EUR");
				interfaceMethod.convertAddress(op, getproducturl);
			} else {
				getproducturl = interfaceMethod.getGoodUrl("one", myPublicUrl, "n", "n");
				Log.logInfo("取得的 商品地址为:" + getproducturl);
				op.loopGet(getproducturl, 30, 3, 60);
			}
			op.loopClickElement("addToBag", 4, 10, 300);// 点击添加至购物车按钮
			op.loopSendKeysClean("numberinputbox", "0", 20);
			Page.pause(3);
			WebElement element = op.MyWebDriverWait2("numberinputbox", 10);
			getText = element.getAttribute("value");
			Assert.assertEquals(getText, "1");

			
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("不允许修改，重置为1，验证失败，实际获得的内容为:" + getText);
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 修改商品数量为非数字
	 * @验证点: 不允许修改，重置为1 @使用环境： 测试环境
	 * @备注：modify_goods_amount_a与modify_goods_number_b 相关，需按需执行
	 * @author zhangjun
	 * @date 2017年1月4日
	 */
	@Test
	public void modify_goods_number_b() {
		String getText = "";
		try {
			try {
				op.loopSendKeysClean("numberinputbox", "ab", 60);
			} catch (Exception e) {
				Page.pause(5);
				WebElement element = op.MyWebDriverWait2("numberinputbox", 50, false);
				getText = element.getAttribute("value");
				Assert.assertEquals(getText, "1");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logError("不允许修改，重置为1，验证失败，实际获得的内容为:" + getText);
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 修改库存为10000
	 * @验证点: 改数量大于商品库存时提示该商品的库存值，并将数量自动回归它的库存值 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月4日
	 */
	@Test
	public void modify_goods_inventory() {
		String getText = "";
		Map<String, Object> goods = null;
		String goodsUrl = "";
		String goodsNumber = "";

		try {
			if (baseURL.contains("/es/")) {
				goods = interfaceMethod.getGoods(myPublicUrl, "n", "n", "EUR");
				goodsUrl = (String) goods.get("url_title");
				goodsNumber = (String) goods.get("goods_number");
				interfaceMethod.convertAddress(op, goodsUrl);
			} else {
				goods = interfaceMethod.getGoods(myPublicUrl, "n", "n");
				goodsUrl = (String) goods.get("url_title");
				goodsNumber = (String) goods.get("goods_number");
				Log.logInfo("商品数量为" + goodsNumber);
				op.loopGet(goodsUrl, 40, 3, 60);
				Log.logInfo("获取的产品地址为:" + goodsUrl);
			}
			op.loopClickElement("addToBag", 10, 10, explicitWaitTimeoutLoop);// 点击添加至购物车按钮
			op.loopSendKeysClean("numberinputbox", "100000",  explicitWaitTimeoutLoop);
			Page.pause(5);
			WebElement element = op.MyWebDriverWait2("numberinputbox", 60, false);
			getText = element.getAttribute("value");

			Assert.assertEquals(getText, goodsNumber);
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.logError("不允许修改，重置为1，验证失败，实际获得的内容为:" + getText);
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 1.购物车中，未选择商品时点击save for later链接2.弹出提示请选择商品，
	 * @验证点: 弹出弹窗提示：Please select the products first. @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月4日
	 */
	@Test
	public void savefor_later_a() {

		interfaceMethod.crearAllCartGoods(myPublicUrl, loginName);// 清空购物车
		String testExpectedStr = "Please select the products first.";
		String getMessage = "";
		try {
			if (baseURL.contains("/es/")) {
				getproducturl = interfaceMethod.getGoodUrl("one", myPublicUrl, "n", "n", "EUR");
				interfaceMethod.convertAddress(op, getproducturl);
			} else {
				getproducturl = interfaceMethod.getGoodUrl("one", myPublicUrl, "n", "n");
				op.loopGet(getproducturl, 40, 3, 80);
			}
			op.loopClickElement("addToBag", 4, 10, 60);// 点击添加至购物车按钮
			op.loopClickElement("confimCheck", 2, 10, 60);
			Page.pause(2);
			op.loopClickElement("saveForLate", 10, 30, 80);
			getMessage = op.loopGetElementText("saveMessage", 30, 30);
			Log.logInfo("getMessage:" + getMessage);
			if (baseURL.contains("/es/")) {
				testExpectedStr = "Por favor, seleccione los productos primero.";
			}
			Assert.assertEquals(getMessage, testExpectedStr);
		
			op.loopClickElement("saveMessageOkBtn", 3, 20, 80);// 做收尾的工作

		} catch (Exception e) {
			e.printStackTrace();
			Log.logError("验证失败，实际获得的文本为:" + getMessage);
		
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点:个人中心的收藏夹的save for later栏目中增加了这些商品
	 * @验证点: 1.选择的商品,商品从购物车中消失2.个人中心的收藏夹的save for later栏目中增加了这些商品 @使用环境：
	 *       测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月4日
	 * @修改说明
	 */
	@Test
	public void savefor_later_b() {
		String testExpectedStr ="Your shopping cart is empty.";
		interfaceMethod.clearSaveForLater(myPublicUrl, loginName);// 清空save for
																	// later
		interfaceMethod.crearAllCartGoods(myPublicUrl, loginName);// 清空购物车
		String getEmpty = "";
		String goods[] = null;
		boolean Title = false;
		boolean Price = false;
		try {
			if (baseURL.contains("/es/")) {
				getproducturl = interfaceMethod.getGoodUrl("one", myPublicUrl, "n", "n", "EUR");
				interfaceMethod.convertAddress(op, getproducturl);
			} else {
				getproducturl = interfaceMethod.getGoodUrl("one", myPublicUrl, "n", "n");
				op.loopGet(getproducturl, 40, 2, 80);
			}

			op.loopClickElement("addToBag", 4, 10, 40);// 点击添加至购物车按钮
			goods = getGoods();// 获取商品的标题
			Page.pause(2);
			op.loopClickElement("saveForLate", 20, 20, 40); // savefor late按钮
			Page.pause(2);
			getEmpty = op.loopGetElementText("cartPage_EmptyCartNote", 20, 30);
			if (baseURL.contains("/es/")) {
				testExpectedStr = "Su carrito está vacío.";
			}
			if (getEmpty.equals(testExpectedStr)) {
				Log.logInfo("选择save  for later按钮后购物车为空，验证通过");
				op.loopGet(saveForLateUrl, 40, 3, 80);
				Page.pause(1);
				String getTitle = op.loopGetElementText("FavoritesProductTitle", 3, 80);
				String getPrice = op.loopGetElementText("FavoritesProductPrice", 3, 20).replace("$", "").replace("€",""); // 商品的总价
				Title = goods[0].contains(getTitle);
				Price = goods[1].equals(getPrice);
				if (Title && Price) {
					Log.logInfo("收藏的商品名和收藏前的商品一致，验证通过，收藏前的价格为:" + getPrice);
				} else {
					Log.logError("收藏的价格和收藏前的价格不一致，验证不通过,收藏前的价格为：" + goods[0] + "收藏后的价格为:" + getPrice);
				}
			} else {
				Log.logError("购物车不为空，验证不通过，实际获得文本内容为:" + getEmpty);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("验证失败，实际获取到的文本内容为:" + getEmpty);
			Log.logWarn("验证失败,商品名为:" + Title);
			Log.logWarn("验证失败,价格为:" + Price);

			Assert.fail();

		}
	}

	/**
	 * 
	 * @测试点: 未登录添加商品到购物车
	 * @验证点: 1.未登录，添加商品到购物车2.跳转到登录页 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月4日
	 * @修改说明
	 */
	@Test
	public void savefor_later_notlogin() {
		interfaceMethod.crearAllCartGoods(myPublicUrl, loginName);// 清空购物车
		String getUrl = "";
		try {
			Signout_Success();// 退出登录
			if (baseURL.contains("/es/")) {
				getproducturl = interfaceMethod.getGoodUrl("one", myPublicUrl, "n", "n", "EUR");
				interfaceMethod.convertAddress(op, getproducturl);

			} else {
				getproducturl = interfaceMethod.getGoodUrl("one", myPublicUrl, "n", "n");
				op.loopGet(getproducturl, 40, 2, 80);
			}
			op.loopClickElement("addToBag", 4, 10, 80);// 点击添加至购物车按钮
			op.loopClickElement("saveForLate", 3, 20, 80); // savefor late按钮
			Page.pause(4);
			getUrl = driver.getCurrentUrl();
			Log.logInfo("实际获取地址为:" + getUrl);

			if (getUrl.equals(saveloginUrl)) {
				Log.logInfo("获取的地址与需要匹配的地址一致，验证通过，地址为:" + getUrl);
			} else {
				Log.logError("获取的地址与需要匹配的地址不一致，验证失败，实际获取地址为:" + getUrl + "需要对比的地址为:" + saveloginUrl);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("验证失败,实际获得的地址为:" + getUrl);
			
			Assert.fail();

		}
	}

	/**
	 * 
	 * @测试点: 未登录时商品移到收藏夹
	 * @验证点: 1.未登录，点击收藏商品按钮2.进入到登录页，登录成功3.跳回到商品主页 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月5日
	 * @修改说明
	 */
	@Test
	public void favorites_product_notlogin_a() {

		Signout_Success();
		String getLoginUrl = "";
		try {

			if (baseURL.contains("/es/")) {
				getproducturl = interfaceMethod.getGoodUrl("one", myPublicUrl, "n", "n", "EUR");
				interfaceMethod.convertAddress(op, getproducturl);
				getproducturl = driver.getCurrentUrl();// 获取转换后的地址
				op.loopClickElement("AddFavorites2es", 3, 20, 100);// 添加到收藏夹
			} else {
				getproducturl = interfaceMethod.getGoodUrl("one", myPublicUrl, "n", "n");
				Log.logInfo("获取的商品地址为：" + getproducturl);
				op.loopGet(getproducturl, 40, 3, 60);
				op.loopClickElement("AddFavorites2", 3, 20, 100);// 添加到收藏夹
			}

			if (op.isElementPresent("Login_EmailEnter", 20)) {
				Log.logInfo("未登录点击收藏夹，跳转到登录页，验证通过");
				login();
				Page.pause(3);
				getLoginUrl = driver.getCurrentUrl();
				if (getproducturl.equals(getLoginUrl)) {
					Log.logInfo("成功进入到商品主页，验证通过,获取的地址为：" + getLoginUrl);
				} else {
					Log.logError("没有进入到商品主页，验证不通过，实际获取的地址为：" + getLoginUrl + "对比的地址为:" + getproducturl);
				}
			} else {
				Log.logError("未登录点击收藏夹，没有到登录页，验证失败，下面的操作都不执行");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("实际获取的地址为:" + getLoginUrl);
			
			Assert.fail();
		}

	}

	/**
	 * 
	 * @测试点: 产品页移到收藏夹
	 * @验证点: 1.将商品移到收藏夹2.收藏夹中显示移动的商品 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月5日
	 * @修改说明 因为b一旦失败，重跑会出问题，所以添加增加商品的方式
	 */
	@Test
	public void favorites_product_mobile_b() {
		interfaceMethod.clearFavoritesProduct(myPublicUrl, loginName);
		interfaceMethod.clearSaveForLater(myPublicUrl, loginName);
		WebElement element;
		try {
			if (baseURL.contains("/es/")) {
				getproducturl = interfaceMethod.getGoodUrl("one", myPublicUrl, "n", "n", "EUR");
				interfaceMethod.convertAddress(op, getproducturl);
			} else {
				// 调用以前的方法
				getproducturl = interfaceMethod.getGoodUrl("one", myPublicUrl, "n", "n");
				Log.logInfo("获取的商品地址为：" + getproducturl);
				op.loopGet(getproducturl, 40, 3, 60);
			}

			String productTitle = op.loopGetElementText("productTile", 3, 80);// 商品标题
			String productPrice = op.loopGetElementText("productPrice", 3, 20).replace("$", "").replace("€", ""); // 商品的总价

			if (baseURL.contains("/es/")) {
				op.loopClickElement("AddFavorites2es", 3, 20, explicitWaitTimeoutLoop);
				element = op.MyWebDriverWait2("AddFavorites2es", 20, false);
			} else {
				op.loopClickElement("AddFavorites2", 3, 20, explicitWaitTimeoutLoop);
				element = op.MyWebDriverWait2("AddFavorites2", 20, false);

			}

			String getSrc = element.getAttribute("data-src");
			Log.logInfo("js脚本为:" + getSrc);
			op.loopGet(getSrc, 40, 3, 90);
			op.loopGet(collectionUrl, 50, 2, 90);
			op.loopClickElement("allfavorite", 10, 10, explicitWaitTimeoutLoop);
			String getTitle = op.loopGetElementText("FavoritesProductTitle", 3, 20);
			String getPrice = op.loopGetElementText("FavoritesProductPrice", 3, 20).replace("$", "").replace("€", ""); // 商品的总价

			boolean Title = productTitle.contains(getTitle);
			boolean Price = productPrice.equals(getPrice);

			if (Title || Price) {
				Log.logInfo("收藏夹的商品标题正确，价格也正确，验证通过");
			} else {
				Log.logError("收藏夹的商品标题不正确" + Title + "或者价格不正确" + Price + "验证不通过");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 从产品页添加收藏夹
	 * @验证点: 1.添加商品到收藏夹2.收藏夹中存在收藏的商品 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月5日
	 * @修改说明
	 */
	@Test
	public void favorites_product_add_a() {
		interfaceMethod.clearFavoritesProduct(myPublicUrl, loginName);
		interfaceMethod.clearSaveForLater(myPublicUrl, loginName);// 为了避免相同
		try {
			String cheapUrl = myPublicUrl + "cheap/red/";
			op.loopGet(cheapUrl, 50, 3, 60);
			Log.logInfo("查询页生成的地址为：" + cheapUrl);
			Page.pause(1);
			String productTile = op.loopGetElementText("cheapTitle", 10, 60);
			String productTotal = op.loopGetElementText("cheapPrice", 3, 20).replace("$", "").replace("€", "");
			op.loopClickElement("cheapFacorite", 10, 10, explicitWaitTimeoutLoop);
			
			WebElement cheapFacorite = op.MyWebDriverWait2("cheapFacorite", 10);
			Log.logInfo("添加商品到收藏夹成功");
			String getSrc = cheapFacorite.getAttribute("data-src");
			Log.logInfo("js脚本是:" + getSrc);
			op.loopGet(myPublicUrl + getSrc, 50, 2, 60);

			op.loopGet(collectionUrl, 50, 3, 60);
			op.loopClickElement("allfavorite", 10, 10, explicitWaitTimeoutLoop);
			String getTitle = op.loopGetElementText("FavoritesProductTitle", 3, 50);
			String getPrice = op.loopGetElementText("FavoritesProductPrice", 3, 20).replace("$", "").replace("€", ""); // 商品的总价

			boolean Title = productTile.equals(getTitle);
			boolean Price = productTotal.equals(getPrice);
			if (Title || Price) {
				Log.logInfo("收藏夹的商品标题正确，价格也正确，验证通过");
			} else {
				Log.logError("收藏夹的商品标题不正确" + Title + "或者价格不正确" + Price + "验证不通过");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("收藏夹的商品标题不正确,价格不正确");
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 从产品页添加收藏夹，重复添加
	 * @验证点: 多次添加商品，收藏夹只显示一个 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月5日
	 * @修改说明
	 */
	@Test
	public void favorites_product_add_b() {
		
		int getSize = 0;
		try {
			String url = myPublicUrl + "cheap/red/";
			op.loopGet(url, 50, 2, 200);
			op.loopClickElement("cheapFacorite", 3, 10, 80);
			op.loopGet(collectionUrl, 30, 2, 100);
			Page.pause(2);

			getSize = op.getElements("favoriteList").size();
			if (getSize == 1) {
				Log.logInfo("重复添加商品，个数没有变化，验证通过");
			} else {
				Log.logError("重复添加商品，个数有变化，验证失败，实际获得的个数为:" + getSize);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("重复添加商品，个数有变化，验证失败，实际获得的个数为:" + getSize);
			Assert.fail();
		}

	}

	/**
	 * 
	 * @测试点: 添加收藏夹个商品
	 * @验证点: 1.从收藏夹收藏商品2.添加成功， 购物车数量价格正确 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月26日
	 */
	@Test
	public void cart_favorites_product_a() {
		interfaceMethod.crearAllCartGoods(myPublicUrl, loginName);// 清空购物车
		try {
			op.loopGet(collectionUrl, 80, 2, 300);
			Page.pause(2);
			int getSize = op.getElements("favoriteList").size();
			if (getSize > 0) {
				Log.logInfo("收藏夹中有商品");
				String getTitle = op.loopGetElementText("FavoritesProductTitle", 3, 80);
				FavoritePrice = op.loopGetElementText("FavoritesProductPrice", 3, 20).replace("$", "").replace("€", ""); // 商品的总价
				op.actionSingleClick("favoriteLink");// 商品的图片链接进入
				if (op.isElementPresent("addToBag", 20)) {
					op.loopClickElement("addToBag", 4, 10, 300);// 点击添加至购物车按钮
					String goods[] = getGoods();// 获取商品的标题
					boolean Title = goods[0].contains(getTitle);
					boolean Price = goods[1].equals(getPrice);
					if (Title || Price) {
						Log.logInfo("收藏夹添加到购物车商品标题正确，价格也正确，验证通过");
					} else {
						Log.logError("收藏夹添加到的商品标题不正确" + Title + "或者价格不正确" + Price + "验证不通过");
					}
				}else{
					Log.logInfo("商品已经被销售完了");
				}

			} else {
				Log.logInfo("收藏夹中没有商品，实际获得的个数为:" + getSize + " 用例不执行");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			Log.logError("收藏夹的商品标价格不正确,获取的价格不正确");
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 查看收藏记录
	 * @验证点: 1.购物车中添加1件商品2.购物车下方favorites存在收藏的商品 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月5日
	 * @修改说明
	 */
	@Test
	public void cart_favorites_product_b() {
		String getPrice = "";
		try {
			if (op.isElementPresent("FavoriteHead", 10)) {
				op.loopClickElement("FavoriteHead", 10, 40, 100);
				getPrice = op.loopGetElementText("Facoriteprice", 10, 20).replace("$", "").replace("€", "");
				Assert.assertEquals(getPrice, FavoritePrice);
			} else {
				Log.logInfo("没有商品");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("实际获取的价格为" + getPrice);
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 没勾选，删除一个商品
	 * @验证点:提示需要勾选(弹框是js执行内容，无法获取，换成验证点) 没有选择商品，勾选全部，点击删除，收藏个数没有变化 @使用环境：
	 *                                   测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月6日
	 * @修改说明
	 */
	@Test
	public void delect_all_notcheck_a() {
		int delectSize = 0;
		WebElement element;
		try {
			String[] product = { "one", "min", "max" }; // 添加3种商品到 收藏夹
			for (int i = 0; i < 3; i++) {
				if (baseURL.contains("/es/")) {
					getproducturl = interfaceMethod.getGoodUrl(product[i], myPublicUrl, "n", "n");
					interfaceMethod.convertAddress(op, getproducturl);
				} else {
					getproducturl = interfaceMethod.getGoodUrl(product[i], myPublicUrl, "n", "n");
					op.loopGet(getproducturl, 50, 3, 60);
				}
				if (baseURL.contains("/es/")) {
					op.loopClickElement("AddFavorites2es", 3, 20, 60);
					element = op.MyWebDriverWait2("AddFavorites2es", 20, false);
				} else {
					op.loopClickElement("AddFavorites2", 3, 20, 60);

					element = op.MyWebDriverWait2("AddFavorites2", 20, false);
				}

				Log.logInfo("添加商品到收藏夹成功");
				String getSrc = element.getAttribute("data-src");
				Log.logInfo("test:" + getSrc);
				op.loopGet(getSrc, 50, 2, 200);
				op.navigateBack(40);
			}
			op.loopGet(collectionUrl, 30, 2, 80);// 跳转到商品列表页
			Page.pause(2);
			List<WebElement> favorite = op.getElements("favoriteList");
			int size = favorite.size();
			op.loopClickElement("delectSign", 10, 30, 100);
			favorite = op.getElements("favoriteList");
			delectSize = favorite.size();

			if (size == delectSize) {
				Log.logInfo("点击删除按钮后，个数没有变化，验证通过");
			} else {
				Log.logError("点击删除按钮后，个数有变化，验证失败，获取的实际个数为" + delectSize);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("验证失败,添加到收藏夹失败");
			Assert.fail();

		}
	}

	/**
	 * 
	 * @测试点: 勾选，删除一个商品
	 * @验证点: 1.删除成功，数量显示正常 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月6日
	 * @修改说明
	 */
	@Test
	public void delect_single_product_b() {
		int delectSize = 0;
		try {
			Page.pause(2);
			List<WebElement> favorite = op.getElements("favoriteList");
			int size = favorite.size() - 1;
			Log.logInfo("收藏夹总共有:" + favorite.size() + "件商品");
			int oldNumber = favorite.size();// 最开始的个数

			op.loopClickElement("delectSignProduct", 3, 10, 60);
			op.loopClickElement("delectSignOk", 10, 40, explicitWaitTimeoutLoop);
			op.navigateRefresh(50, 3, 60);
			Page.pause(2);
			favorite = op.getElements("favoriteList");
			delectSize = favorite.size();
			if (size == delectSize) {
				Log.logInfo("点击删除按钮后，个数有变化，验证成功，最开始的个数为" + oldNumber + "删除后的各个数为:" + delectSize);
			} else {
				Log.logError("点击删除按钮后，个数和计算的个数相同，验证不通过，最开始的个数为" + oldNumber + "删除后的各个数为:" + delectSize);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("验证失败,实际获得个数为:" + delectSize);
			Assert.fail();

		}

	}

	/**
	 * 
	 * @测试点: 勾选全部，点击删除
	 * @验证点: 1.删除所有商品2.列表显示正常,列表中没有了收藏的产品 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月6日
	 * @修改说明
	 */
	@Test
	public void delect_all_product_c() {
		try {
			if (!op.isElementPresent("delectSignOk", 20)) {// 兼容代码删除所有商品后还报错
				Log.logInfo("商品已经已经全部为空");
			} else {
				op.loopClickElement("delectAllCheck", 10, 20, 80);
				op.loopClickElement("delectSign", 10, 20, 80);
				op.loopClickElement("delectSignOk", 10, 40, 100);
				driver.navigate().refresh();
				op.navigateRefresh(50, 2, 60);
				List<WebElement> favorite = op.getElements("favoriteList");

				if (favorite.size() == 0) {
					Log.logInfo("删除所有的收藏内容,验证通过");
				} else {
					Log.logError("没有删除所有收藏的内容，验证不通过，实际的个数:" + favorite.size());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn("验证失败,原本个数应该为0");
			Assert.fail();

		}

	}

	// 退出登录的程序
	public void Signout_Success() {
		String getName;
		try {
			getName = op.loopGetElementText("signlinkName", 3, 20);
			boolean loginState = getName.contains("autot");// 区分测试与正式环境
			if (loginState) {// 为真则已登录
				// Signout_Success();// 登出
				JavascriptExecutor js = (JavascriptExecutor) driver;
				String myjs = "document.querySelector(\"#header > div > div.user_box > ul\").style.display='block';";
				js.executeScript(myjs);
				Page.pause(3);
				try {
					if (baseURL.contains("/es/")) {
						//需要点击第一个
						op.loopClickElement("logout_link_esone", 3, 20, 50);						
						myjs = "document.querySelector(\"#header > div > div.user_box > ul\").style.display='block';";
						js.executeScript(myjs);
						op.loopClickElement("logout_link_es", 3, 20, 50);
					} else {
						op.loopClickElement("logout_link", 3, 20, 50);
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}
				Log.logInfo("用户退出登录");
			}
			Log.logInfo("用户已经是退出状态");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @测试点: getGoods
	 * @验证点: 获取商品的一些信息，标题、总价，购物车页面获得内容 @使用环境：
	 * @return @备注： String[]
	 * @author zhangjun
	 * @date 2017年1月2日
	 */
	public String[] getGoods() {
		String goods[] = new String[2];
		try {
			Page.pause(4);
			goods[0] = op.loopGetElementText("Commoditytitle", 30, 70);// 商品标题
			Log.logInfo("商品标题为:" + goods[0]);
			goods[1] = op.loopGetElementText("shoppingtotal", 10, 20).replace("$", "").replace("€", ""); 

		} catch (Exception e) {
			e.printStackTrace();
		}
		return goods;
	}

	/**
	 * 
	 * @测试点: takenumbers
	 * @验证点: 获取商品编号
	 * @param getproducturl
	 *            得到的商品地址
	 * @return 返回得到的sku @备注： String
	 * @author zhangjun
	 * @date 2016年12月29日
	 */
	public String takeNumbers(String getproducturl) {
		String goodsOrder = "";
		String regEx = "[^0-9]"; // 从url中取得数字
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(getproducturl);
		goodsOrder = m.replaceAll("").trim();
		System.out.println(goodsOrder);
		if (goodsOrder.length() > 7) {
			goodsOrder = goodsOrder.substring(1, goodsOrder.length());
		}
		Log.logInfo("取到的商品编号为：" + goodsOrder);

		return goodsOrder;
	}

	/* 初始化一些地址 */
	public void urlInit() {
		if (baseURL.contains(".trunk.") && baseURL.contains("/es/")) {// 西班牙语测试环境
			Log.logInfo("现在是西班牙语测试环境");
			myPublicUrl = "http://rosewholesale.com.trunk.s1.egomsl.com/";
			myPublicUrl2 = "cart.rosewholesale.com.trunk.s1.egomsl.com/es/m-flow-a-cart.htm";
			collectionUrl = "http://user.rosewholesale.com.trunk.s1.egomsl.com/es/m-users-a-collection_list.htm";
			saveForLateUrl = "http://user.rosewholesale.com.trunk.s1.egomsl.com/es/m-users-a-save_for_later.htm";
			saveloginUrl = "http://login.rosewholesale.com.trunk.s1.egomsl.com/m-users-a-sign.htm?ref=http://cart.rosewholesale.com.trunk.s1.egomsl.com/es/m-flow-a-cart.htm";
			rates = "http://cart.rosewholesale.com.trunk.s1.egomsl.com/data-cache/currency_huilv.js";// 汇率换算"
		} else if (baseURL.contains("/es/")) {
			Log.logInfo("现在是西班牙语正式环境");
			myPublicUrl =" https://rosewholesale.com/";
			myPublicUrl2 = "es.rosewholesale.com/";
			collectionUrl = "https://user.rosewholesale.com/es/m-users-a-collection_list.htm";
			saveForLateUrl = "https://user.rosewholesale.com/es/m-users-a-save_for_later.htm";
			saveloginUrl = "https://login.rosewholesale.com/m-users-a-sign.htm?ref=https://cart.rosewholesale.com/es/m-flow-a-cart.htm";
			rates = "https://cart.rosewholesale.com/data-cache/currency_huilv.js";// 汇率换算

		} else if (baseURL.contains(".d.")) {
			myPublicUrl = "rosewholesale.com.d.s1.egomsl.com/";
			myPublicUrl2 = "rosewholesale.com.d.s1.egomsl.com/";
			collectionUrl = "http://user.rosewholesale.com.d.s1.egomsl.com/m-users-a-collection_list.htm";
			saveForLateUrl = "http://user.rosewholesale.com.d.s1.egomsl.com/m-users-a-save_for_later.htm";
			saveloginUrl = "http://login.rosewholesale.com.d.s1.egomsl.com/m-users-a-sign.htm?ref=http://cart.rosewholesale.com.d.s1.egomsl.com/m-flow-a-cart.htm";
		} else {
			myPublicUrl = "https://rosewholesale.com/";
			myPublicUrl2 = "www.rosewholesale.com/";
			collectionUrl = "http://user.rosewholesale.com/m-users-a-collection_list.htm";
			saveForLateUrl = "http://user.rosewholesale.com/m-users-a-save_for_later.htm";
			saveloginUrl = "https://login.rosewholesale.com/m-users-a-sign.htm?ref=https://cart.rosewholesale.com/m-flow-a-cart.htm";
		}
	}
}
