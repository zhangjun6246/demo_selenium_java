package com.globalegrow.rosewholesalea;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.globalegrow.base.StartApp;
import com.globalegrow.code.Page;
import com.globalegrow.custom.InterfaceMethod;
import com.globalegrow.util.Log;

import com.globalegrow.util.Pub;
import com.globalegrow.util.testInfo;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidKeyCode;

public class RW_A_cart extends StartApp {
	private String className = GetClassName();
	private String testCaseProjectName = "rosewholesale";
	private testInfo info = null;
	public String baseUrl;
	private String myPublicUrl = "";
	private long setWaitTimeout = 20;
	double productTotal;// 计算总价
	private String total;// 获取商品总价
	private  String shoopingPrice;//商品价格
	private  String  productOne;//商品1的价格 
	private  String  productTwo;//商品2的价格 
	DecimalFormat df = new DecimalFormat("#0.00");
	InterfaceMethod interfaceMethod;
	String[] productPrice;

	@Parameters({ "testUrl" })
	public RW_A_cart(String testurl) {
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
		
		interfaceMethod.crearAllCartGoods(myPublicUrl, "autotest08@globalegrow.com");// 清除购物车
		
		opApp.ClickElement("addressAccount", setWaitTimeout);
		opApp.SendKeysClean("loginName", "autotest08@globalegrow.com", setWaitTimeout);
		opApp.SendKeysClean("logingPassword", "123456", setWaitTimeout);
		opApp.ClickElement("loginSign", setWaitTimeout);
		boolean account = opApp.elementExists(10, "addressAccount");
		Assert.assertTrue(account);
	}

	/**
	 * 
	 * @测试点: 进入空购物车，点击底部的Bag，点击shop Top Fashion按钮
	 * @验证点: 1.购物车为空的提示:Your Shopping Bag is empty 2.点击shop Top
	 *       Fashion连接到网站首页，查到某一个商品 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年6月19日
	 * @修改说明
	 */
	@Test
	public void empty_shopping() {
		interfaceMethod.crearAllCartGoods(myPublicUrl, "autotest08@globalegrow.com");
		opApp.ClickElement("bagbtn", 10);
		boolean emptyMessage = opApp.elementExists(10, "emptyMessage");
		Assert.assertTrue(emptyMessage);
		opApp.ClickElement("emptyBtn", setWaitTimeout);
		Assert.assertTrue(opApp.elementExists(10, "homeImage"));
		

	}

	/**
	 * 
	 * @测试点: 添加一个普通商品到购物车，购物车中已有商品1
	 * @验证点: 1.商品数量增加，购物车图标显示的商品数量已为1 2.增加一个新商品，购物车显示为2 2.Bag
	 *       Total为：本店售价*商品数量
	 * @使用环境： 测试环境，正式环境
	 * @author zhangjun
	 * @date 2017年6月19日
	 * @修改说明
	 */
	@Test
	public void cart_product_regular_price() {
		addShooping(2);// 增加商品
		
		productTotal = Double.parseDouble(productOne) + Double.parseDouble(productTwo);
		Log.logInfo("计算的总价:" + productTotal);
		//需要下滑才能展示
		total = opApp.GetElementText("shoopingtotal", setWaitTimeout, 10).replace("$", "");// 商品的总价
		Log.logInfo("获取的总价为:" + total);
		Assert.assertEquals(total, df.format(productTotal));
	}

	/**
	 * 
	 * @测试点: 添加一个促销商品到购物车，修改商品数量，查看商品的价格
	 * @验证点: 1.商品金额Bag Total:（商品数量*促销价）设置的商品数量为2, 2.计算的总价与页面显示的总价一致
	 * @使用环境：测试环境，正式环境 
	 * @备注： void
	 * @author zhangjun
	 * @date 2017年6月19日
	 * @修改说明
	 */
	@Test
	public void cart_promote_product() {
		deleteAllShooping();
		returnHome();
		Map<String, Object> goods = interfaceMethod.getGoods(myPublicUrl, "n", "y");
		total(goods,  2);

	}

	/**
	 * 
	 * @测试点: 从商品页重复添加同一个商品到购物车。添加2次，生成2个
	 * @验证点: 商品数量叠加价格正确，数量为：2，价格为2*单价 商品为普通商品，不产生阶梯价 
	 * @使用环境： 测试环境，正式环境
	 *  @备注： void
	 * @author zhangjun
	 * @date 2017年6月19日
	 * @修改说明
	 */
	@Test
	public void cart_addShooping_price() {
		deleteAllShooping();
		Map<String, Object> goods = interfaceMethod.getGoods(myPublicUrl, "n", "n");
		total(goods,  2);
	}

	/**
	 * 
	 * @测试点: 从收藏夹添加商品到购物车
	 * @验证点: 1.购物车中显示刚从收藏夹的商品
	 * @使用环境： 测试环境，正式环境
	 * @备注： void
	 * @author zhangjun
	 * @date 2017年6月20日
	 * @修改说明
	 */
	@Test
	public void cart_addShooping_favorite() {
		interfaceMethod.crearAllCartGoods(myPublicUrl, "autotest08@globalegrow.com");

		Map<String, Object> goods = interfaceMethod.getGoods(myPublicUrl, "n", "n");
		String sku = (String) goods.get("goods_sn");
		searchSku(sku);
		Page.pause(1);
		opApp.swipe(0.5, 0.3, 0.5, 0.6, 1000);//需要下滑一部分
		opApp.ClickElement("favoriteBtn", setWaitTimeout);// 点击收藏
		Page.pause(1);
		returnHome();
		opApp.ClickElement("addressAccount", setWaitTimeout);//进入到主页面

		opApp.ClickElement("accountFavorites", setWaitTimeout);// 点击进入收藏夹中
		String title = opApp.GetElementText("favoriteTitle", setWaitTimeout, 10);
		Log.logInfo("获取商品标题为:" + title);
		opApp.ClickElement("BuyBtn", setWaitTimeout);// 点击buy
		opApp.ClickElement("favoriteShopping", setWaitTimeout);

		String shoopingTitle = opApp.GetElementText("shoopingTitle", setWaitTimeout, 10);
		if (shoopingTitle.contains(title)) {
			Log.logInfo("添加到购物车成功");
		} else {
			Log.logError("添加到购物车失败");
		}
	}

	/**
	 * 
	 * @测试点: 添加一个清仓商品到购物车，修改商品数量，查看商品的价格
	 * @验证点: 1.BagTotal:商品数量*本店售价 设置的商品数量为2, 2.计算的总价与页面显示的总价一致
	 *  @使用环境： 测试环境，正式环境 
	 *  @备注： void
	 * @author zhangjun
	 * @date 2017年6月19日
	 * @修改说明
	 */
	@Test
	public void cart_clearance_product() {
		Map<String, Object> goods = interfaceMethod.getGoods(myPublicUrl, "y", "n");
		total(goods,  2);//清仓商品没有阶梯价
	}

	/**
	 * 
	 * @测试点: 1.不登录，添加1件商品到购物车 2.再登录，进入购物车中登录前购物车中的产品合并到了登录后的购物车中
	 * @验证点: 1.未登录前添加商品到购物车，跳转到了登录页面 2.登录成功商品合并到了购物车中
	 *  @使用环境： 测试环境，正式环境
	 *   @备注： void
	 * @author zhangjun
	 * @date 2017年6月20日
	 * @修改说明
	 */

	@Test
	public void notLogin_addshooping() {
		returnHome();// 返回到主页
		interfaceMethod.crearAllCartGoods(myPublicUrl, "autotest08@globalegrow.com");
		opApp.ClickElement("homeaccount", setWaitTimeout);
		if (!opApp.elementExists(5, "favoriteBtn")) {
			WebElement signOut = opApp.findElementBySwipe("quit", 0.5, 0.6, 0.5, 0.2);
			signOut.click();// 点击退出
			opApp.ClickElement("confirmDelete", setWaitTimeout);// 点击确认
			// 选择商品
		/*	opApp.ClickElement("homeImage", setWaitTimeout);// 主页上任意一款商品
			String getshoopingTitle = opApp.GetElementText("shoopingGetTitle", 10, 20);
			Log.logInfo("获取的title的文本:" + getshoopingTitle);
			opApp.ClickElement("Addshopping", setWaitTimeout);*/
			Map<String, Object> goods = interfaceMethod.getGoods(myPublicUrl, "n", "n");
			String sku = (String) goods.get("goods_sn");
			searchSku(sku);
			
			opApp.ClickElement("shoopinggo", setWaitTimeout);// 进入到购物车
			opApp.ClickElement("shoopingCheck", setWaitTimeout);// 结算
			// 登录
			opApp.SendKeysClean("loginName", "autotest08@globalegrow.com", setWaitTimeout);
			opApp.SendKeysClean("logingPassword", "123456", setWaitTimeout);
			opApp.ClickElement("loginSign", setWaitTimeout);

			if (opApp.elementExists(5, "addshooping")) {
				try {
					opApp.actionPressKeyCode(AndroidKeyCode.BACK);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			String LoginshoopingTitle = opApp.GetElementText("shoopingGetTitle", 10, 20);
			Log.logInfo("成功登录后进入到购物车内获取的title的文本:" + LoginshoopingTitle);

		}

	}

	/**
	 * 
	 * @测试点: 1.添加普通商品到购物车页面 2.在购物车输入框输入不同的商品数量为10，对比Bag Total是否正确
	 * @验证点: 1.添加商品数量为1，单价为：XX 2.增加商品为10，总价为:数量*单价
	 *  @使用环境： 测试环境，正式环境
	 *   @备注： void
	 * @author zhangjun
	 * @date 2017年6月21日
	 * @修改说明
	 */
	@Test
	public void modify_goods_number() {
		returnHome();
		opApp.ClickElement("bagbtn", setWaitTimeout);
		deleteAllShooping();
		Map<String, Object> goods = interfaceMethod.getGoods(myPublicUrl, "n", "n");
		total(goods,10);
	}

	/**
	 * 
	 * @测试点: 1.添加清仓商品加入购物车 2.输入大于库存的数量
	 * @验证点: 1.当输入大于库存时，数量自动变为商品的库存 2.商品数量为商品的库存时，“+”号变灰，不能增加数量
	 * 		3.输入的库存大于100，+号和输入框内容不起效
	 *  @使用环境： 测试环境，正式环境 
	 * @备注： void
	 * @author zhangjun
	 * @date 2017年6月21日
	 * @修改说明
	 */
	@Test
	public void modify_goods_number_inventory() {

		Map<String, Object> goods = interfaceMethod.getGoods(myPublicUrl, "y", "y");
		interfaceMethod.crearAllCartGoods(myPublicUrl, "autotest08@globalegrow.com");
		returnHome();// 返回到主页面
		String sku = (String) goods.get("goods_sn");
		String goodsNumber = (String) goods.get("goods_number");
		Log.logInfo("获取商品的sku为：" + sku);
		Log.logInfo("获取商品的库存为：" + goodsNumber);
		searchSku(sku);
		opApp.ClickElement("shoopinggo", setWaitTimeout);// 进入到购物车页面
		// 需要点击进行结算的按钮
		opApp.ClickElement("shoopingEditBtn", setWaitTimeout);
		opApp.SendKeysClean("shoopingSetNumber", String.valueOf(2000), setWaitTimeout);
		opApp.ClickElement("shoopingEditBtn", setWaitTimeout);
		String number = opApp.GetElementText("shoopingSetNumber", setWaitTimeout, 10);// 商品的总价
		Log.logInfo("获取的库存为:" + number);
		if(Double.parseDouble(goodsNumber)>100){
			goodsNumber="100";//
			Log.logInfo("库存数量已经超过了100,目前超过100就不能进行增加了");
		}
		
		Assert.assertEquals(goodsNumber, number);
		if(opApp.elementExists(5, "shoopingInventory")){
			opApp.ClickElement("shoopingInventory", setWaitTimeout);// 需要在点击编辑按钮,做收尾工作
		}
		
		
	}

	/**
	 * 
	 * @测试点: 在购物车中点击edit按钮，并在对应的商品后点击删除按钮，弹出Delete并点击ok
	 * @验证点: 商品删除成功,购物车中商品总数减少 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年6月21日
	 * @修改说明
	 */
	@Test
	public void delete_one_product() {
		returnHome();
		opApp.ClickElement("bagbtn", setWaitTimeout);
		if (opApp.elementExists(5, "emptyBtn")) {// 如果购物车中没有商品，就添加商品
			addShooping(2);
		}
		// 购物车数量集合
		List<WebElement> shoopinglist = opApp.GetElementList("shoopingList");
		int startsProduct = shoopinglist.size();
		Log.logInfo("最开始购物车的总数量为:" + startsProduct);
		opApp.ClickElement("shoopingEditBtn", setWaitTimeout);// 编辑按钮
		opApp.ClickElement("shoppingdelete", setWaitTimeout);
		opApp.ClickElement("confirmDelete", setWaitTimeout);

		List<WebElement> shoopinglistnew = opApp.GetElementList("shoopingList");
		int deleteProduct = shoopinglistnew.size();
		Log.logInfo("现在购物车的总数量为:" + deleteProduct);
		Assert.assertEquals(startsProduct - 1, deleteProduct);
		opApp.ClickElement("shoopingEditBtn", setWaitTimeout);// 编辑按钮做收尾工作
	}

	/**
	 * 
	 * @测试点: 在购物车中点击edit按钮，删除购物车所有的商品
	 * @验证点:1.页面提示:Your shopping bag is empty 2.点击shop Top Fashion
	 *                  连接到网站首页
	 * @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年6月21日
	 * @修改说明
	 */
	@Test
	public void delete_all_product() {
		returnHome();
		deleteAllShooping();
		opApp.ClickElement("emptyBtn", setWaitTimeout);
		Assert.assertTrue(opApp.elementExists(10, "homeImage"));
	}

	/**
	 * 
	 * @测试点: 通过sku查询
	 * @验证点:通过sku查询到商品数据，并添加到购物车
	 * @param skus 输入sku  
	 * @param  i 生成几个数，用于数组存放价格
	 * @使用环境：测试环境，正式环境
	 * @备注： void
	 * @author zhangjun
	 * @date 2017年6月19日
	 * @修改说明
	 */
	public String  searchSku(String skus) {
		returnHome();
		opApp.ClickElement("Categories", setWaitTimeout);
		opApp.sendKeys("search", skus);
		try {
			opApp.actionPressKeyCode(AndroidKeyCode.ENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		opApp.ClickElement("resultSearch", setWaitTimeout);// 查询到内容
		Page.pause(1);
		opApp.swipe(0.5, 0.6, 0.5, 0.3, 1000);//需要上滑一部分
	
	    shoopingPrice = opApp.GetElementText("shoopingPrice", setWaitTimeout, 10).replace("$", "");
		opApp.ClickElement("Addshopping", setWaitTimeout);// 添加到购物车
		Log.logInfo("添加到购物车成功");
		return shoopingPrice;
	}

	/**
	 * 
	 * @测试点: 计算商品的总价
	 * @验证点: 计算商品的总价,,M版没有阶梯价
	 * @param goods
	 *            map中给定的一个sku
	 * @param number
	 *            商品件数 @备注： void
	 * @author zhangjun
	 * @date 2017年6月20日
	 * @修改说明
	 */
	public void total(Map<String, Object> goods,  int number) {
		interfaceMethod.crearAllCartGoods(myPublicUrl, "autotest08@globalegrow.com");
		returnHome();// 返回到主页面
		String sku = (String) goods.get("goods_sn");
		Log.logInfo("获取商品的sku为：" + sku);
		searchSku(sku);
		String product = opApp.GetElementText("shoopingPrice", setWaitTimeout, 10).replace("$", "");// 获取商品价格
		opApp.ClickElement("shoopinggo", setWaitTimeout);// 进入到购物车页面   
		
		// 需要点击进行结算的按钮
		opApp.ClickElement("shoopingEditBtn", setWaitTimeout);
		opApp.SendKeysClean("shoopingSetNumber", String.valueOf(number), setWaitTimeout);
		total = opApp.GetElementText("shoopingtotal", setWaitTimeout, 10).replace("$", "");// 商品的总价
		Log.logInfo("获取的总价为:" + total);

		productTotal = Double.parseDouble(product) * number;
		Log.logInfo("计算的总价为:" + productTotal);
		Assert.assertEquals(df.format(Double.parseDouble(total)),df.format(productTotal));
		//Assert.assertTrue(Double.parseDouble(df.format(total))==productTotal);
		opApp.ClickElement("shoppingdelete", setWaitTimeout);// 点击删除
		opApp.ClickElement("confirmDelete", setWaitTimeout);// 确认删除

	}

	/**
	 * 
	 * @测试点: 从搜索页面查询数据  @备注： void
	 * @author zhangjun
	 * @date 2017年6月21日
	 * @修改说明
	 */
	public void addShooping(int number) {
		returnHome();
		Map<String, Object> goods = interfaceMethod.getGoods(myPublicUrl, "n", "n");
		String sku = (String) goods.get("goods_sn");
	    productOne=searchSku(sku);
	    if(number==2){
			goods = interfaceMethod.getGoods(myPublicUrl, "n", "y");
			sku = (String) goods.get("goods_sn");
		   productTwo=searchSku(sku);
		}
		String getShoopingText = opApp.GetElementText("shoopingNumber", setWaitTimeout, 10);// 获取购物车的商品数量
		Log.logInfo("3");
		Assert.assertEquals(getShoopingText, "2");
		opApp.ClickElement("shoopingNumber", setWaitTimeout);// 跳转到购物车
	}

	/**
	 * 
	 * @测试点: 判断购物车中是否有商品，有就删除，直到一件商品都不存在 @备注： void
	 * @author zhangjun
	 * @date 2017年6月21日
	 * @修改说明
	 */
	public void deleteAllShooping() {
		int i=0;
		returnHome();
		opApp.ClickElement("bagbtn", setWaitTimeout);// bag按钮
		if (!opApp.elementExists(5, "emptyMessage")) {
			opApp.ClickElement("shoopingEditBtn", setWaitTimeout);
			while (!opApp.elementExists(5, "emptyBtn")) {
				i++;
				opApp.ClickElement("shoppingdelete", setWaitTimeout);// 点击删除
				opApp.ClickElement("confirmDelete", setWaitTimeout);// 确认删除
				if(i==5){
					break;
				}
			}
		}

	}

	/**
	 * 
	 * @测试点: 返回到主页面
	 * @验证点: 用于在任何页面，都返回到主页面
	 * @修改说明
	 */
	public void returnHome() {
		opApp.swipe(0.5, 0.4, 0.5, 0.6, 1000);//防止查不到控件
		while (opApp.elementExists(3, "homeaccount") == false) {
			try {
				opApp.actionPressKeyCode(AndroidKeyCode.BACK);
			} catch (Exception e) {
				e.printStackTrace();
			} // 返回到主页
		}
	}

}
