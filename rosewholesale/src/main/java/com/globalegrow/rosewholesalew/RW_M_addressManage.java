package com.globalegrow.rosewholesalew;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
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


public class RW_M_addressManage extends StartPhoneBrowser {
	private String className = GetClassName();

	public Date sendEmailDate = new Date();

	private String testCaseProjectName = "rosewholesale";
	private String baseURL = "";
	String  myPublicUrl2;//域名
	String loginUrl = "";// 登录的url
	String addressUrl = "";// 增加地址页面
	String myAccountUrl = "";// 个人信息页面
	String urlList = "";// 地址列表
	String getkeys;
	String adduserData;//增加地址数据

	private RW_M_addressManage() {
		moduleName = className.substring(className.lastIndexOf(".") + 1);
		

	}
	@Parameters({ "keys" ,"testUrl","devicesName"})
	@BeforeClass
	public void beforeClass(String keys,String testUrl,String devicesName) {
		String methodName = GetMethodName();
		String methodNameFull = moduleName + "." + methodName;
		Log.logInfo("**************Ready to test (" + moduleName + ")!!!**************\n");
		Log.logInfo("(" + methodNameFull + ")...beforeClass start...");
		try {
			start(devicesName); // 初始化driver
			driver = super.getDriver();
			driver.manage().timeouts().pageLoadTimeout(pageLoadTimeoutMax, TimeUnit.SECONDS);
			urlInit();
			baseURL = testUrl;
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
            driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Log.logInfo("(" + methodNameFull + ")...afterClass stoop...\n\n");
		}
	}



	/**
	 * 
	 * @测试点: 登录成功
	 * @验证点: 登录的用户前置条件 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月19日
	 * @修改说明
	 */
	public void login() {
		 String registercode = "";
		try {
			String requestId = Pub.getRandomString(6);
			try {
				registercode = InterfaceMethod.IF_Verification(driver,myPublicUrl2,requestId,getkeys);
			} catch (Exception e) {
				e.printStackTrace();
				Log.logInfo("获取促销码接口有问题");
			    registercode= InterfaceMethod.IF_Verification(driver,myPublicUrl2,requestId,getkeys);
			 
			}
		  		  
			op.loopSendKeys("loginPage_loginEmail", "autotest12@globalegrow.com", 3, 30);
			op.loopSendKeys("loginPage_loginPwd", "123456", 3, 30);
			IsExistCode(registercode);
			op.loopClickElement("loginPage_signInBtn", 10, 30, 100);
			//因为新用户会跳出很多活动页面，所以我们直接跳转到登录成功页
			op.loopGet("https://userm.rosewholesale.com/user/index/userindex", 5, 5, 10);
			Assert.assertTrue(op.isElementPresent("loginHomePage", 10));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @测试点: 必填项全部为空
	 * @验证点:1.提示必填项字段不能为空，每个必填项都给予了提示 2.保存失败 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月20日
	 * @修改说明
	 */
	@Test
	public void AllFieldIsNull() {
		correFail = false;
	
		try {
			op.navigateRefresh(40, 3, 60);
			String[] autResult = { "Please enter the first name", "Please enter the last name",
					"Please enter a valid email address", "Please enter the full address line1",
					"Please enter consignee's State", "Please enter consignee's city", "Please write your phone number",
					"Please enter the Zip/Postal Code" };
			if (op.isElementPresent("addShipAddr")) {
				Log.logInfo("账户中已经存在地址,点击新增按钮");
				op.loopClickElement("addShipAddr", 6, 10, 40);
				op.getSelect("CountryRegion", 10).selectByIndex(0);
			}
			ControlUsingMethod.SetScrollBar(400);
			op.loopClickElement("BookBotton", 15, 60, 70); // 进入到地址页面
			ControlUsingMethod.SetScrollBar(700);
			op.loopClickElement("saveBtn", 15, 60, 70);// 点击保存
			Page.pause(1);
			
			/*op.loopGet(adduserData, 40, 3, 60);
			if(op.isElementPresent("loginPage_loginEmail")){
				Log.logInfo("用户没有登录，现在进行登录");
				String requestId = Pub.getRandomString(6);
			    String registercode= InterfaceMethod.IF_Verification(driver,myPublicUrl2,requestId,getkeys);
				op.loopSendKeys("loginPage_loginEmail", "autotest12@globalegrow.com", 3, 30);
				op.loopSendKeys("loginPage_loginPwd", "123456", 3, 30);
				IsExistCode(registercode);
				op.loopClickElement("loginPage_signInBtn", 10, 30, 100);
			}*/

			List<String> msg = new ArrayList<String>();
			msg.add(op.loopGetElementText("firstNameErrorMsg", 12, 60));
			msg.add(op.loopGetElementText("lastNameErrorMsg", 12, 60));
			msg.add(op.loopGetElementText("emailErrorMsg", 12, 60));
			msg.add(op.loopGetElementText("addressline1ErrorMsg", 12, 60));
			msg.add(op.loopGetElementText("statesErrorMsg2", 12, 60));
			msg.add(op.loopGetElementText("cityErrorMsg", 12, 60));
			msg.add(op.loopGetElementText("telErrorMsg", 12, 60));
			msg.add(op.loopGetElementText("zipcodeErrorMsg", 12, 60));

			boolean flag = false;
			for (int i = 0; i < autResult.length; i++) {
				if (!msg.get(i).equals(autResult[i])) {
					Log.logInfo("界面提示语:" + msg.get(i) + ",与实际提示语:" + autResult[i] + "不一致");
					flag = true;
				} else {
					Log.logInfo("界面提示语:" + msg.get(i) + ",与实际提示语:" + autResult[i] + "一致");
				}
			}
			if (flag) {
				Log.logError("全部字段为空验证失败！！！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	/**
	 * 
	 * @测试点: 所有必填项为空，进行每个必填项的提示
	 * @验证点: 1.提示【First name】不能为空 2.保存失败 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月20日
	 * @修改说明
	 */
	@Test
	public void eachFieldIsNull() {
		correFail = false;
		try {
			//driver.navigate().to(addressUrl);
			op.loopGet(addressUrl, 60, 2, 60);
			String[] autResult = { "Please enter the first name", "Please enter the last name",
					"Please enter a valid email address", "Please enter the full address line1",
					"Please select country of the recipient", "Please enter consignee's State",
					"Please enter consignee's city", "Please write your phone number",
					"Please enter the Zip/Postal Code" };
			String[] address = { "jun", "zhang", "testadders@163.com", "testjun", "Angola", "Alabama", "shanghai",
					"185766722337", "547506" };
			String[] element = { "firstname", "lastname", "email", "addressline1", "CountryRegion", "province", "city",
					"tel", "zipcode" };
			String[] msStrings = { "firstNameErrorMsg", "lastNameErrorMsg", "emailErrorMsg", "addressline1ErrorMsg",
					"countryErrorMsg", "statesErrorMsg2", "cityErrorMsg", "telErrorMsg", "zipcodeErrorMsg" };
			setAllFieldVaule();// 设置所有字段的值

			/**
			 * 循环验证各个字段不能为空
			 */
			for (int i = 0; i < autResult.length; i++) {

				if ((i == 4) || (i == 5)) {// 清空字段的值
					Page.pause(3);
					op.getSelect(element[i], 10).selectByIndex(0);
					Page.pause(3);
				} else {
					op.loopSendClean(element[i], 3, 20);
				}
				op.loopClickElement("saveBtn", 15, 60, 70);// 点击保存
				if (op.isElementPresent(msStrings[i])) {// 判断提示对象及提示语是否正确
					String msg = op.loopGetElementText(msStrings[i], 5, 20);
					if (msg.equals(autResult[i])) {
						Log.logInfo(element[i] + "字段不为空，验证成功,字段不为空提示语为:" + msg + ",实际为:" + autResult[i]);
					} else {
						Log.logError(element[i] + "字段不为空提示语为：" + msg + ",实际应该为:" + autResult[i] + ",验证失败！！！");
					}
				} else {
					Log.logError("未找到字段：" + element[i] + "为空的提示语，验证为空失败！！！");
				}
				if ((i == 4) || (i == 5)) {// 把清空字段的值设置回来
					op.getSelect(element[i], 10).selectByVisibleText(address[i]);
				} else {
					op.loopSendKeys(element[i], address[i], 3, 20);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 电话号码小于6位，特殊字符，大于20
	 * @验证点:1.提示电话不能小于6/不能输入特殊字符/不能大于20个字符 2.保存失败 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月20日
	 * @修改说明
	 */
	@Test
	public void PhoneNumberChar() {

		correFail = false;	
		try {
			String[] autResult = { "Please enter at least 6 characters", "Please only use: numbers, +, -, or ()",
					"You can write a maximum of 20 characters" };

			//driver.navigate().to(addressUrl); // 需要正式环境中删除
			//op.loopGet(addressUrl, 60, 2, 60);
			String[] value = { "123", "@jk", "1234567890123456789012" };
			for (int i = 0; i < autResult.length; i++) {
				op.loopSendClean("tel", 3, 20);
				op.loopSendKeysClean("tel", value[i], 20);
				op.loopClickElement("saveBtn", 15, 60, 70);// 点击保存
				if (op.isElementPresent("telErrorMsg")) {// 判断提示对象及提示语是否正确
					String msg = op.loopGetElementText("telErrorMsg", 6, 30);
					if (msg.equals(autResult[i])) {
						Log.logInfo(autResult[i] + "验证成功！！！");
					} else {
						Log.logError("电话号码提示语为：" + msg + ",实际应该为:" + autResult[i] + ",验证失败！！！");
					}
				} else {
					Log.logError("未找到电话号码字段为的提示语对象，验证为空失败！！！");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 添加1个地址，并且小于5个
	 * @验证点: 1.保存成功2.地址列表中存在新增adress @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月20日
	 * @修改说明
	 */
	@Test
	public void AdressLess5() {
		correFail = false;
		try {
			op.navigateRefresh(40, 3, 60);
			op.loopGet(addressUrl, 40, 3, 60);
			/*driver.navigate().to(addressUrl);*/
		   // Page.pause(3);
			if (!op.isElementPresent("addShipAddr",40)) {// 不存在新增地址的按钮
				Log.logInfo("目前没有一个地址");
				setAllFieldVaule();
				op.loopClickGoPageURL("saveBtn", 15, 35, 50, 70);// 点击保存
				//op.loopClickElement("saveBtn", 15, 60, 70);
				List<WebElement> elements = op.getElements("addressList");
				Log.logInfo("新增地址后，地址栏的地址个数：" + (elements.size()+1));
				if (elements.size() == 1) {
					Log.logInfo("地址新增成功，验证通过");
				} else {
					Log.logInfo("地址新增失败，验证失败");
				}
			} else {
				Log.logInfo("已经有地址了，单个地址不执行");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 添加地址大于5个
	 * @验证点: 1.添加地址页，大于5个地址 2.隐藏添加按钮 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月20日
	 * @修改说明
	 */
	@Test
	public void AdressEquals5() {
		correFail = false;
		try {
			//driver.navigate().to(urlList);
			op.loopGet(urlList, 30, 3, 60);
			if(!op.isElementPresent("addShipAddr")){//避免没有地址的时候进行的一些判断
				Log.logInfo("没有一个地址");
				setAllFieldVaule();// 设置地址
				op.loopClickElement("saveBtn", 10, 15, 70);// 点击保存
				Log.logInfo("新增地址成功");
			}
			Page.pause(2);
			List<WebElement> elements = op.getElements("addressList");
			Log.logInfo("初始前地址栏的地址个数：" + elements.size());
			int EndAddressAcount = 0;
			if (elements.size() < 5) {
				for (int i = 0; i < 5 - elements.size(); i++) {
					op.loopClickElement("addShipAddr", 10, 15, 60);// 点击新增地址
					setAllFieldVaule();// 设置地址
					op.loopClickElement("saveBtn", 10, 15, 70);// 点击保存
					Log.logInfo("新增第"+(i+1)+"地址成功");
					Page.pause(1);
				}
				Page.pause(1);
				List<WebElement> elements2 = op.getElements("addressList");
				EndAddressAcount = elements2.size();
				Log.logInfo("新增后地址栏的地址个数：" + EndAddressAcount);
			}

			if (EndAddressAcount == 5) {
				if (op.isElementPresent("addShipAddr")) {
					Log.logError("地址等于5个，任然可以新增地址，验证失败！！！");
				} else {
					Log.logInfo("地址等于5个，无法在新增地址，验证成功！！！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 编辑地址-保存
	 * @验证点: 1.选择一个地址进行编辑 2.在地址栏修改地址，选择保存 3.对比修改前的地址与保存后的地址都正确 @使用环境：
	 *       测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月20日
	 * @修改说明
	 */

	@Test
	public void EditAdressAndSave() {
		correFail = false;
		try {		
			//driver.navigate().to(urlList);
			op.loopGet(urlList, 30, 3, 60);
			//Page.pause(3);
			if (op.isElementPresent("Edit",50)) {
				op.loopClickElement("Edit", 3, 6, 20); // 点击第一个编辑按钮
				Page.pause(3);
				// 修改前的值
				String[] OldAddress = new String[3];
				OldAddress[0] = op.loopGetElementValue("firstname", "value", 70);
				OldAddress[1] = op.loopGetElementValue("lastname", "value",  70);
				OldAddress[2] = op.loopGetElementValue("email", "value",  60);

				String[] modAddress = { "Modifyjun", "Modifyzhang", "Modifyzhang@test.com" };
				// 修改文件内容
				op.loopSendClean("firstname", 5, 10);
				op.loopSendKeys("firstname", modAddress[0], 20, 10);
				op.loopSendClean("lastname", 5, 10);
				op.loopSendKeys("lastname", modAddress[1], 3, 20);
				op.loopSendClean("email", 5, 10);
				op.loopSendKeys("email", modAddress[2], 3, 20);

				op.loopClickElement("saveBtn", 15, 60, 70);// 点击保存
				op.loopClickElement("Edit", 3, 6, 20); // 点击第一个编辑按钮

				// 获取修改后的地址
				String[] newAddress = new String[3];
				newAddress[0] = op.loopGetElementValue("firstname", "value",  70);
				newAddress[1] = op.loopGetElementValue("lastname", "value",  70);
				newAddress[2] = op.loopGetElementValue("email", "value", 60);

				@SuppressWarnings("unused")
				boolean flag = false;
				for (int i = 0; i < OldAddress.length; i++) {
					if (OldAddress[i].equals(newAddress[i])) {
						flag = true;
						Log.logInfo("地址修改成功,修改前值:" + OldAddress[i] + ",修改的地址内容:" + modAddress[i] + ",修改后显示的地址:"
								+ newAddress[i] + "验证通过");
					} else {
						Log.logInfo("地址修改是失败,修改前值:" + OldAddress[i] + ",修改的地址内容:" + modAddress[i] + ",修改后显示的地址:"
								+ newAddress[i] + "验证失败");
					}
				}
				if (flag = false) {
					Log.logError("修改地址验证失败！！！");
				}
			} else {
				Log.logError("不存在一个地址，编辑失败，用例失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 编辑地址-取消
	 * @验证点: 1选择一个地址，修改first用户名,lastname，email， 2.点击刷新
	 *       3.在打开修改的的地址信息，信息内容还是以前的内容 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月21日
	 * @修改说明
	 */
	@Test
	public void EditAdressAndCancle() {
		correFail = false;
		try {
			/*Page.pause(3);
			driver.navigate().to(urlList);*/
			op.loopGet(urlList, 30, 3, 60);
			//Page.pause(3);
			if (op.isElementPresent("Edit",60)) {
			op.loopClickElement("Edit", 3, 6, 20); // 点击第一个编辑按钮
			Page.pause(7);
			// 修改前的值
			String[] OldAddress = new String[3];
			OldAddress[0] = op.loopGetElementValue("firstname", "value",  70);
			OldAddress[1] = op.loopGetElementValue("lastname", "value", 70);
			OldAddress[2] = op.loopGetElementValue("email", "value", 60);

			String[] modAddress = { "EditCanclejun", "EditCanclezhang", "EditCanclezhang@test.com" };
			// 修改文件内容
			op.loopSendClean("firstname", 5, 10);
			op.loopSendKeys("firstname", modAddress[0], 3, 20);
			op.loopSendClean("lastname", 5, 10);
			op.loopSendKeys("lastname", modAddress[1], 3, 20);
			op.loopSendClean("email", 5, 10);
			op.loopSendKeys("email", modAddress[2], 3, 20);

			driver.navigate().refresh();

			driver.navigate().to(urlList);
			op.loopClickElement("Edit", 3, 6, 20); // 点击第一个编辑按钮

			// 获取修改后的地址
			String[] newAddress = new String[3];
			newAddress[0] = op.loopGetElementValue("firstname", "value",  70);
			newAddress[1] = op.loopGetElementValue("lastname", "value",  70);
			newAddress[2] = op.loopGetElementValue("email", "value",  60);

			boolean flag = false;
			for (int i = 0; i < OldAddress.length; i++) {
				if (OldAddress[i].equals(newAddress[i])) {
					flag = true;
					Log.logInfo("地址修改失败,修改前值:" + OldAddress[i] + ",修改的地址内容:" + modAddress[i] + ",修改后显示的地址:"
							+ newAddress[i] + "验证通过");
				} else {
					Log.logInfo("地址修改成功,修改前值:" + OldAddress[i] + ",修改的地址内容:" + modAddress[i] + ",修改后显示的地址:"
							+ newAddress[i] + "验证失败");
				}
			}
			if (flag = false) {
				Log.logError("修改地址验证失败！！！");
			}
			}else{
				Log.logError("不存在一个地址，编辑失败，用例失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 删除一个地址
	 * @验证点: 1.删除成功2.该条地址从地址本中消失； @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月21日
	 * @修改说明
	 */
	@Test
	public void DeleteAdressSinger() {
		correFail = false;
		
		try {
			op.loopGet(urlList, 60, 3, 70);
			Page.pause(4);
			List<WebElement> elements = op.getElements("addressList");
			Log.logInfo("删除前,地址栏的地址个数：" + elements.size());
			if (elements.size() <= 0) {
				Log.logError("没有一个地址,剩余内容不执行");
			} else {
				op.loopClickElement("delectBtn", 3, 10, 50);
				op.loopClickElement("delete_yes", 3, 10, 30);
				Page.pause(3);
				WebElement Delectelement = op.MyWebDriverWait2("delectBtn", 40);
				if (Delectelement == null) {
					Log.logInfo("页面已经超时了，刷新页面");
					Page.pause(3);
					op.navigateRefresh(30, 3, 50);
					
				}
				List<WebElement> Delectelements = op.getElements("addressList");
				int EndAccount = Delectelements.size();
				if (elements.size() - 1 == EndAccount) {
					Log.logInfo("删除成功,删除前地址个数：" + elements.size() + ",删除后地址个数:" + EndAccount + "验证通过");
				} else {
					Log.logError("删除成功,删除前地址个数：" + elements.size() + ",删除后地址个数:" + EndAccount + "验证失败");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 删除全部地址
	 * @验证点:1.删除成功 2.地址栏中无删除后的地址 @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月21日
	 * @修改说明
	 */
	@Test
	public void DeleteAdressAall() {
		correFail = false;
		//driver.navigate().refresh();
		try {		
			//driver.navigate().to(urlList);
			op.loopGet(urlList, 30, 3, 60);
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
					Page.pause(3);				
				}
			}
			Page.pause(5);
			
			if (op.isElementPresent("firstname")||!op.isElementPresent("addressList")) {//删除所有地址后，会显示填写的所有名称，或者不存在集合控件
				Log.logInfo("删除全部地址成功，目前已经到了地址新增页面，验证通过");
			} else {
				Log.logError("删除全部地址不成功，目前还剩余地址" + elements.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}



	/**
	 * 
	 * @测试点: 设置各个字段的值
	 * @验证点: 无 @使用环境： @return 测试环境，正式环境 @备注： String[]
	 * @author zhangjun
	 * @date 2017年1月20日
	 * @修改说明
	 */
	public String[] setAllFieldVaule() {

		String[] address = new String[9];
		address[0] = getRandomString(8, 1);
		address[1] = getRandomString(8, 1);
		address[2] = "zhangjuntest@163.com";
		address[3] = getRandomString(8, 1);
		address[6] = getRandomString(10, 1);
		address[7] = getRandomString(10, 2);
		address[8] = getRandomString(8, 2);
		try {
			if(op.isElementPresent("firstname")){
			op.loopSendKeys("firstname", address[0], 3, 20);
			op.loopSendKeys("lastname", address[1], 3, 20);
			op.loopSendKeys("email", address[2], 3, 20);
			op.loopSendKeys("addressline1", address[3], 3, 20);
			op.getSelect("CountryRegion", 10).selectByIndex(1);
			Page.pause(4);
			op.getSelect("province", 10).selectByIndex(1);
			op.loopSendKeys("city", address[6], 3, 20);
			op.loopSendKeys("tel", address[7], 3, 20);
			op.loopSendKeys("zipcode", address[8], 3, 20);
			address[4] = op.getSelect("CountryRegion", 10).getFirstSelectedOption().getText();
			address[5] = op.getSelect("province", 10).getFirstSelectedOption().getText();
			}else{
				Log.logInfo("填写名称控件不存在，地址已经有5个，不做新增，以下内容不执行");
			}
			} catch (Exception e) {
			e.printStackTrace();
		}

		return address;
	}

	/**
	 * 根据不同的类型和长度获取随机字符串//0：字符加数字，1：纯字符串，2：纯数字，3：纯小写字母，4：纯大写字母
	 * 
	 * @param length
	 *            长度
	 * @param type 类型
	 * @return String 返回的是一个字符
	 * @author linchaojiang
	 */
	public static String getRandomString(int length, int type) {
		Random random = new Random();
		StringBuffer randomstr = new StringBuffer();
		String str = "";
		int number = 0;
		for (int i = 0; i < length; ++i) {
			if (type == 0) {
				str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
				number = random.nextInt(62);
			} else if (type == 1) {
				str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
				number = random.nextInt(52);
			} else if (type == 2) {
				str = "0123456789";
				number = random.nextInt(10);
			} else if (type == 3) {
				str = "abcdefghijklmnopqrstuvwxyz";
				number = random.nextInt(26);
			} else if (type == 4) {
				str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
				number = random.nextInt(26);
			} else {
				Log.logInfo("传入的type类型有误，0：字符加数字，1：纯字符串，2：纯数字，3：纯小写字母，4：纯大写字母");
			}
			randomstr.append(str.charAt(number));
		}
		return randomstr.toString();
	}

	/**
	 * 判断是否有验证码输入框，如果有就输入，否则不输入
	 */
	public void IsExistCode(String registercode){
		try {
			if (op.isElementPresent("loginCode")){
				op.loopSendKeys("loginCode", registercode, 3, 20);	
			}else{
				Log.logInfo("不存在验证码输入框，不执行验证码，跳过");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void urlInit() {
		if (baseURL.contains(".a.")) {
			addressUrl = "http://m.wap-rosewholesale.com.a.s1cg.egomsl.com/user/address/add";
			myAccountUrl = "http://m.wap-rosewholesale.com.a.s1cg.egomsl.com/user/index/userindex";
			urlList = "http://userm.wap-rosewholesale.com.a.s1cg.egomsl.com/user/address/list";
			myPublicUrl2="http://m.wap-rosewholesale.com.a.s1cg.egomsl.com";
			adduserData="http://m.wap-rosewholesale.com.a.s1cg.egomsl.com/user/address/add";
		}else if(baseURL.contains(".trunk.")){
			addressUrl = "http://m.wap-rosewholesale.com.trunk.s1cg.egomsl.com/user/address/add";
			myAccountUrl = "http://m.wap-rosewholesale.com.trunk.s1cg.egomsl.com/user/index/userindex";
			urlList = "http://userm.wap-rosewholesale.com.trunk.s1cg.egomsl.com/user/address/list";
			myPublicUrl2="http://m.wap-rosewholesale.com.trunk.s1cg.egomsl.com";
		}else {
			addressUrl = "https://m.rosewholesale.com/user/address/list";
			myAccountUrl = "https://m.rosewholesale.com/user/index/userindex";
			urlList = "https://userm.rosewholesale.com/user/address/list";
			myPublicUrl2="https://m.rosewholesale.com";
			adduserData="https://userm.rosewholesale.com/user/address/add";
		}
	}

}
