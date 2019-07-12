package com.globalegrow.rosewholesale;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.globalegrow.base.Startbrowser;
import com.globalegrow.code.Locator;
import com.globalegrow.code.Page;
import com.globalegrow.custom.InterfaceMethod;
import com.globalegrow.util.Log;
import com.globalegrow.util.Op;

import com.globalegrow.util.Pub;
import com.globalegrow.util.testInfo;



public class RW_adressManage extends Startbrowser {
	private String className = GetClassName(); // 必要
	public static String baseURL = null;
	private String myProjectName = GetProjectName();
	public String currentWindow = null;// 当前窗口句柄
	public Date sendEmailDate = new Date();
	private testInfo info = null;
	private String testCaseProjectName = "rosewholesale";
	private Locator locator = null;
	private String adressBookUrl = null;//地址
	private 	String domainName;
	
	public String baseURL0 = "";
	private String getkeys;
	
	@Parameters({ "testUrl" }) // 必要,二选一
	private RW_adressManage(String testUrl) { // 必要
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
			op.setScreenShotPath(screenShotPath);
			driver = super.getDriver(); // 启动driver
			driver.manage().timeouts().pageLoadTimeout(pageLoadTimeoutMax, TimeUnit.SECONDS);
			op.loopGet(baseURL, 40, 4, 120);
			Log.logInfo("baseURL:" + baseURL);
			getkeys = keys;
			urlInit();// 初始化URL
			login();
		
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			Log.logInfo("(" + methodNameFull + ")...beforeClass stoop...\n\n");
		}
	}

	/**
	 * 退出浏览器
	 */
	@AfterClass
	public void afterClass() {
		beforeTestRunFlag = false;
		driver.quit();// 退出浏览器
		
	}


	/**
	 * 获取ClassName
	 * 
	 * @param name
	 * @return
	 */
	public String getClassName(String name) {

		return name.replace(".", ",").split(",")[this.getClass().getName().replace(".", ",").split(",").length - 1];
	}

	/**
	 * 
	 * @测试点: 必填项全部为空
	 * @验证点:1.提示必填项字段不能为空，每个必填项都给予了提示2.保存失败 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月6日
	 * 修改说明：进来后先判断是有地址
	 */
	@Test
	public void AllFieldIsNull() {
		boolean a1=false,a2 = false,a3 = false,a4= false,a5 = false,a6= false,a7= false,a8= false,a9= false;
	
		try {
			//Page.pause(8);
			//判断是否有地址
			op.loopGet(adressBookUrl, 50, 3, 60);
			Log.logInfo("跳转到地址列表页");
			int startSumAdress = op.getElements("ShippingAddressList").size();
			if (startSumAdress >= 1) {
				Log.logInfo("初始地址个数：" + startSumAdress);
				deleteAdress();// 删除全部的地址
			}
			if (op.isElementPresent("shippingAddressButtom",40)){
			op.loopClickElement("shippingAddressButtom", 3, 10, 10);// 地址页新增按钮
			Log.logInfo("点击新增地址按钮成功");
			Page.pause(2);
			op.loopClickElement("comfirmEdit", 3, 10, 20);
			if(baseURL.contains("/es/")){
				a1= op.loopGetElementText("firstNameErrorMsg", 3, 30).equals("Por favor, introduzca su primer nombre");
				a2 = op.loopGetElementText("lastNameErrorMsg", 3, 30).equals("Por favor, ingrese su apellido");
				a3 = op.loopGetElementText("emailErrorMsg", 3, 30).equals("Por favor, introduzca una dirección de correo electrónico válida");
				a4 = op.loopGetElementText("addressline1ErrorMsg", 3, 30).equals("Por favor, introduzca la Línea 1 de su dirección completa");
				a5 = op.loopGetElementText("countryErrorMsg", 3, 30).equals("Por favor, seleccione el país del destinatario");
				a6 = op.loopGetElementText("statesErrorMsg", 3, 30).equals("Por favor, introduzca el Estado o Provincia del Destinatario");
				a7 = op.loopGetElementText("cityErrorMsg", 3, 30).equals("Por favor, introduzca la ciudad del destinatario");
				a8 = op.loopGetElementText("telErrorMsg", 3, 30).equals("Por favor, escriba su número de teléfono");
				a9 = op.loopGetElementText("zipcodeErrorMsg", 3, 30).equals("Por favor, introduzca el Código Zip/Postal");
			}else{
				a1= op.loopGetElementText("firstNameErrorMsg", 3, 30)
						.equals("Please enter first name");
			    a2 = op.loopGetElementText("lastNameErrorMsg", 3, 30)
						.equals("Please enter last name");
				a3 = op.loopGetElementText("emailErrorMsg", 3, 30)
						.equals("Please enter a valid email address");
				a4 = op.loopGetElementText("addressline1ErrorMsg", 3, 30)
						.equals("Please enter the full address line1");
				a5 = op.loopGetElementText("countryErrorMsg", 3, 30)
						.equals("Please select country of the recipient");
				a6 = op.loopGetElementText("statesErrorMsg", 3, 30)
						.equals("Please enter consignee the States");
				a7 = op.loopGetElementText("cityErrorMsg", 3, 30)
						.equals("Please enter the consignee's city");
				a8 = op.loopGetElementText("telErrorMsg", 3, 30)
						.equals("Please write your phone number");
				a9 = op.loopGetElementText("zipcodeErrorMsg", 3, 30)
						.equals("Please enter the Zip/Postal Code");				
			}
			
			if (a1 && a2 && a3 && a4 && a5 && a6 && a7 && a8 && a9) {
				Log.logInfo("必填项全部为空，验证通过！！！");
			} else {
				Log.logError("必填项全部为空，验证失败！！！");
			}
			}else{
				Log.logError("控件不存在，以下内容不执行！！！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}

	/**
	 * 测试项：地址-First name为空
	 * 验证点： 1.提示必填项字段不能为空 2.保存失败
	 * 适用环境：测试环境，线上
	 * 
	 */
	@Test
	public void FirstNameIsNull(){
		//correFail = false;
		/*Pub.checkStatusBrowser();*/
		try {
			String [] values={"","kiyi","testzhangzhang001@163.com","testzhangzhang","chengdu","12345678","12245"};
			setFieldValue(GetMethodName(),values);
			if(baseURL.contains("/es/")){
				if (op.loopGetElementText("firstNameErrorMsg", 3, 30).equals("Por favor, introduzca su primer nombre")) {
					Log.logInfo("firstname填写为空，提交失败，验证成功！！");
				} else {
					Log.logError("firstname填写为空，提交失败，验证失败！！");
				}
			}else{
				if (op.loopGetElementText("firstNameErrorMsg", 3, 30).equals("Please enter first name")){
					Log.logInfo("firstname填写为空，提交失败，验证成功！！");
				} else {
					Log.logError("firstname填写为空，提交失败，验证失败！！");
				}
			}
			
			op.loopSendKeysClean("firstname", "lincj", 20);
		} catch (Exception e) {
			e.printStackTrace();
			Pub.printTestCaseExceptionInfo(info);
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}

	/**
	 * 测试项：地址-Last name为空
	 * 验证点：1.提示【Last name】不能为空 2.保存失败
	 * 适用环境：测试环境，线上
	 *
	 */
	@Test
	public void LastNamIsNull(){
		/*Pub.checkStatusBrowser();*/
		try {
			String [] values={"zhang","","testzhangzhang001@163.com","testzhangzhang","chengdu","12345678","12245"};
			clearFieldValue();
			setFieldValue(GetMethodName(),values);
			if (baseURL.contains("/es/")) {
				if (op.loopGetElementText("lastNameErrorMsg", 3, 30).equals("Por favor, ingrese su apellido")) {
					Log.logInfo("lastname填写为空，提交失败，验证成功！！");
				} else {
					Log.logError("lastname填写为空，提交失败，验证失败！！");
				}
			} else {
				if (op.loopGetElementText("lastNameErrorMsg", 3, 30).equals("Please enter last name")) {
					Log.logInfo("lastname填写为空，提交失败，验证成功！！");
				} else {
					Log.logError("lastname填写为空，提交失败，验证失败！！");
				}
			}
			/*op.loopSendKeysClean("lastname", "kiyi", 3, 20);*/
		} catch (Exception e) {
			e.printStackTrace();
		
			Assert.fail();
	}
	}

	/**
	 * 测试项：地址-E-mail address为空
	 * 验证点：1.提示【E-mail address】不能为空 2.保存失败
	 * 适用环境：测试环境，线上
	 * 
	 */
	@Test
	public void EmailaddressIsNull(){
		
		try {
			String [] values={"zhang","kiyi","","testzhangzhang","chengdu","12345678","12245"};
			clearFieldValue();
			setFieldValue(GetMethodName(),values);
			if (baseURL.contains("/es/")) {
				if (op.loopGetElementText("emailErrorMsg", 3, 30).equals("Por favor, introduzca una dirección de correo electrónico válida")) {
					Log.logInfo("E-mail address填写为空，提交失败，验证成功！！");
				} else {
					Log.logError("E-mail address填写为空，提交失败，验证失败！！");
				}
			} else {
				if (op.loopGetElementText("emailErrorMsg", 3, 30).equals("Please enter a valid email address")) {
					Log.logInfo("E-mail address填写为空，提交失败，验证成功！！");
				} else {
					Log.logError("E-mail address填写为空，提交失败，验证失败！！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
	}
	}

	/**
	 * 测试项：地址- Address Line1为空
	 * 验证点：1.提示【Address Line1】不能为空 2.保存失败
	 * 适用环境：测试环境，线上
	 *
	 */
	@Test
	public void AddressLine1IsNull(){
		correFail = false;
		try {
			String [] values={"zhang","kiyi","testzhangzhang001@163.com","","chengdu","12345678","12245"};
			clearFieldValue();
			setFieldValue(GetMethodName(),values);
			if (baseURL.contains("/es/")) {
				if (op.loopGetElementText("addressline1ErrorMsg", 3, 30).equals("Por favor, introduzca la Línea 1 de su dirección completa")) {
					Log.logInfo("Address Line1填写为空，提交失败，验证成功！！");
				} else {
					Log.logError("Address Line1填写为空，提交失败，验证失败！！");
				}
			} else {
				if (op.loopGetElementText("addressline1ErrorMsg", 3, 30).equals("Please enter the full address line1")) {
					Log.logInfo("Address Line1填写为空，提交失败，验证成功！！");
				} else {
					Log.logError("Address Line1填写为空，提交失败，验证失败！！");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}

	/**
	 * 测试项：地址- Country/Region为空
	 * 验证点：1.提示【Country/Region】不能为空 2.保存失败
	 * 适用环境：测试环境，线上
	 * 
	 */
	@Test
	public void CountryOrRegionIsNull(){
		Pub.checkStatusBrowser();
		try {
			String [] values={"zhang","kiyi","testzhangzhang001@163.com","testzhangzhang","chengdu","12345678","12245"};
			clearFieldValue();
			setFieldValue(GetMethodName(),values);
			if (baseURL.contains("/es/")) {
				if (op.loopGetElementText("countryErrorMsg", 3, 30).equals("Por favor, seleccione el país del destinatario")) {
					Log.logInfo("Country/Region填写为空，提交失败，验证成功！！");
				} else {
					Log.logError("Country/Region填写为空，提交失败，验证失败！！");
				}
			} else {
				if (op.loopGetElementText("countryErrorMsg", 3, 30).equals("Please select country of the recipient")) {
					Log.logInfo("Country/Region填写为空，提交失败，验证成功！！");
				} else {
					Log.logError("Country/Region填写为空，提交失败，验证失败！！");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.logWarn(info.testCorrelation);
			Assert.fail();
		}
	}

	/**
	 * 测试项：地址-City为空
	 * 验证点： 1.提示【City】不能为空 2.保存失败
	 * 适用环境：测试环境，线上
	 * 
	 */
	@Test
	public void CityIsNull(){
		Pub.checkStatusBrowser();	
		try {
			String [] values={"zhang","kiyi","testzhangzhang001@163.com","testzhangzhang","","12345678","12245"};
			clearFieldValue();
			setFieldValue(GetMethodName(),values);
			
		   String city=op.loopGetElementText("cityErrorMsg", 3, 20);
		   Log.logInfo("city"+city);
			if (baseURL.contains("/es/")) {
				if (op.loopGetElementText("cityErrorMsg", 3, 20).equals("Por favor, introduzca la ciudad del destinatario")) {
					Log.logInfo("City填写为空，提交失败，验证成功！！");
				} else {
					Log.logError("City填写为空，提交失败，验证失败！！");
				}
			} else {
				if (op.loopGetElementText("cityErrorMsg", 3, 20).equals("Please enter the consignee\'s city")) {
					Log.logInfo("City填写为空，提交失败，验证成功！！");
				} else {
					Log.logError("City填写为空，提交失败，验证失败！！");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 测试项：地址-Phone Number为空
	 * 验证点：1.提示【Phone Number】不能为空 2.保存失败
	 * 适用环境：测试环境，线上
	 * 
	 */
	@Test
	public void PhoneNumberIsNull(){
		Pub.checkStatusBrowser();
		
		try {
			String [] values={"zhang","kiyi","testzhangzhang001@163.com","testzhangzhang","chengdu","","12245"};
			clearFieldValue();
			setFieldValue(GetMethodName(),values);
			if (baseURL.contains("/es/")) {
				if (op.loopGetElementText("telErrorMsg", 3, 20).equals("Por favor, escriba su número de teléfono")) {
					Log.logInfo("Phone Number填写为空，提交失败，验证成功！！");
				} else {
					Log.logError("Phone Number填写为空，提交失败，验证失败！！");
				}
			}else{
			if (op.loopGetElementText("telErrorMsg", 3, 20).equals("Please write your phone number")) {
				Log.logInfo("Phone Number填写为空，提交失败，验证成功！！");
			} else {
				Log.logError("Phone Number填写为空，提交失败，验证失败！！");
			}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 测试项：地址- ZIP/Postal Code为空
	 * 验证点：1.提示【ZIP/Postal Code】不能为空 2.保存失败
	 * 适用环境：测试环境，线上
	 * 
	 */
	@Test
	public void ZIPOrPostalCodeIsNull(){
		//correFail = false;
		Pub.checkStatusBrowser();
		try {
			String [] values={"zhang","kiyi","testzhangzhang001@163.com","testzhangzhang","chengdu","12345678",""};
			clearFieldValue();
			setFieldValue(GetMethodName(),values);
			if (baseURL.contains("/es/")) {
				if (op.loopGetElementText("zipcodeErrorMsg", 3, 20).equals("Por favor, introduzca el Código Zip/Postal")) {
					Log.logInfo("ZIP/Postal Code填写为空，提交失败，验证成功！！");
				} else {
					Log.logError("ZIP/Postal Code填写为空，提交失败，验证失败！！");
				}
			} else {
				if (op.loopGetElementText("zipcodeErrorMsg", 3, 20).equals("Please enter the Zip/Postal Code")) {
					Log.logInfo("ZIP/Postal Code填写为空，提交失败，验证成功！！");
				} else {
					Log.logError("ZIP/Postal Code填写为空，提交失败，验证失败！！");
				}
			}
	
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 测试项：地址-State/County为空
	 * 验证点：  1.提示【State/County】不能为空 2.保存失败
	 * 适用环境：测试环境，线上
	 * 
	 */
	@Test
	public void StateOrCountyIsNull(){
		Pub.checkStatusBrowser();
		try {
			String [] values={"zhang","kiyi","testzhangzhang001@163.com","testzhangzhang","chengdu","12345678","12245"};
			clearFieldValue();
			setFieldValue(GetMethodName(),values);
			String teString = op.loopGetElementText("statesErrorMsg2", 3, 10);
			System.out.println(teString);
			if (baseURL.contains("/es/")) {
				if (op.loopGetElementText("statesErrorMsg2", 3, 30)
						.equals("Por favor, introduzca el Estado o Provincia del Destinatario")) {
					Log.logInfo("State/County填写为空，提交失败，验证成功！！");
				} else {
					Log.logError("State/County填写为空，提交失败，验证失败！！");
				}
			} else {
				if (op.loopGetElementText("statesErrorMsg2", 3, 30)
						.equals("Please enter consignee the States")) {
					Log.logInfo("State/County填写为空，提交失败，验证成功！！");
				} else {
					Log.logError("State/County填写为空，提交失败，验证失败！！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 测试项：地址- 电话号码长度>20
	 * 验证点：1.提示电话号码长度不能大于20个字符 2.保存失败
	 * 适用环境：测试环境，线上
	 * 
	 */
	@Test
	public void PhoneNumberCharGreater20(){
		//correFail = false;
		Pub.checkStatusBrowser();
		try {
			String [] values={"zhang","kiyi","testzhangzhang001@163.com","testzhangzhang","chengdu","1234567845454545454545","12245"};
			clearFieldValue();
			setFieldValue(GetMethodName(),values);
			if (baseURL.contains("/es/")) {
				if (op.loopGetElementText("telErrorMsg", 3, 20).equals("Puede escribir un máximo de 20 caracteres")) {
					Log.logInfo("Phone Number电话号码大于20位，验证通过！！");
				} else {
					Log.logError("Phone Number电话号码大于20位，验证失败！！");
				}
			}else {
				if (op.loopGetElementText("telErrorMsg", 3, 20).equals("You can write a maximum of 20 characters")) {
					Log.logInfo("Phone Number电话号码大于20位，验证通过！！");
				} else {
					Log.logError("Phone Number电话号码大于20位，验证失败！！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		
	}

	/**
	 * 
	 * @测试点: 电话号码长度的校验
	 * @验证点: 1.校验电话号码至少要输入6个字符，最多输入20个字符 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月7日
	 */
	@Test
	public void PhoneNumberCharLess6() {
		Pub.checkStatusBrowser();
		try {
			String [] values={"zhang","kiyi","testzhangzhang001@163.com","testzhangzhang","chengdu","1234","12245"};
			clearFieldValue();
			setFieldValue(GetMethodName(),values);
			if (baseURL.contains("/es/")) {
				if (op.loopGetElementText("telErrorMsg",3,20).equals("Por favor, introduzca al menos de 6 caracteres")) {
					Log.logInfo("Phone Number电话号码小于6位，验证通过！！");
				} else {
					Log.logError("Phone Number电话号码小于6位，验证失败！！");
				}
			}else{
				if (op.loopGetElementText("telErrorMsg",3,20).equals("Please enter at least 6 characters")) {
					Log.logInfo("Phone Number电话号码小于6位，验证通过！！");
				} else {
					Log.logError("Phone Number电话号码小于6位，验证失败！！");
				}
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: 电话号码字符的校验
	 * @验证点:校验电话号码处只能填写数字，+，（，）这些字符 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月7日
	 */
	@Test
	public void PhoneNumberSpecialChar() {
		//correFail = false;
		Pub.checkStatusBrowser();
		try {
			String [] values={"zhang","kiyi","testzhangzhang001@163.com","testzhangzhang","chengdu","+,(,)","12245"};
			clearFieldValue();
			setFieldValue(GetMethodName(),values);
			Page.pause(2);
			if (baseURL.contains("/es/")) {
				if (op.loopGetElementText("telErrorMsg",3,20).equals("Por favor, use solamente: números, +, -, or ()")) {
					Log.logInfo("Phone Number电话号码输入特殊字符，验证通过！！");
				} else {
					Log.logError("Phone Number电话号码输入特殊字符，验证失败！！");
				}
			}else{
				if (op.loopGetElementText("telErrorMsg",3,20).equals("Please only use: numbers, +, -, or ()")) {
					Log.logInfo("Phone Number电话号码输入特殊字符，验证通过！！");
				} else {
					Log.logError("Phone Number电话号码输入特殊字符，验证失败！！");
				}
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 
	 * @测试点: AdressLess5
	 * @验证点: 进入地址本页面1.添加地址,地址本中已为3个地址 2.正常显示地址页 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @throws Exception 
	 * @date 2016年12月7日
	 */
	@Test
	public void AdressLess5() throws Exception {
		
		op.loopGet(adressBookUrl, 40, 3, 60);
		
	    int startSumAdress = op.getElements("ShippingAddressList").size();//locator.getElements("ShippingAddressList", 10).size();
	    Log.logInfo("最开始的地址：!!!"+startSumAdress);
	    if (startSumAdress < 5) {
			addAdress();
			Log.logInfo("添加了到个地址：!!!+"+startSumAdress);
		}
		int endSumAdress = op.getElements("ShippingAddressList").size();
		Log.logInfo("目前有几个地址：!!!"+endSumAdress);
		if ((startSumAdress + 1) == endSumAdress) {
			Log.logInfo("Shipping Address地址小于5个，新增成功!!!");
		} else {
			Log.logError("Shipping Address地址小于5个，新增失败!!!");
		}
	
	}

	/**
	 * 测试项：地址-地址=5个
	 * 验证点：1.保存失败 2.提示最大只能新增5个地址
	 * 适用环境：测试环境，线上
	 * 
	 */
	@Test
	public void AdressEquals5() throws Exception{
	
		op.loopGet(adressBookUrl, 40, 3, 60);
		//获取地址个数  
		int startSumAdress = op.getElements("ShippingAddressList").size();
		if(startSumAdress<5){
			while (startSumAdress<5){			
				addAdress();
				startSumAdress = op.getElements("ShippingAddressList").size();//每次都取最新的集合长度
				Log.logInfo("现在是第几个地址:"+startSumAdress);
			}						
		}		
		if(!(op.isElementPresent("shippingAddressButtom"))){ 
			Log.logInfo("Shipping Address最大地址为5个，验证成功！！！");			
		}else{
			Log.logError("Shipping Address最大地址为5个，验证失败！！！");
		}
	}

	/**
	 * 测试项：地址-  编辑地址保存
	 * 验证点：1.保存成功 2.保存的地址与编辑时的信息一致
	 * 适用环境：测试环境，线上
	 * 
	 */
	@Test
	public void EditAdressAndSave(){
		// 获取原地址各项的值
		try {

			op.loopGet(adressBookUrl, 40, 3, 60);
			// 获取原地址各项的值
	
			op.loopClickElement("EditBtton", 3, 10, 20);
			String firstname = driver.findElement(By.xpath("//*[@id='ship_addr_list']/div[1]/ul/li[1]/span[1]"))
					.getText();
			String lastName = driver.findElement(By.xpath("//*[@id='ship_addr_list']/div[1]/ul/li[1]/span[2]"))
					.getText();
			String email = driver.findElement(By.xpath("//*[@id='ship_addr_list']/div[1]/ul/li[7]/span")).getText();
			//Page.pause(2);
			// 重新赋值
			op.loopSendKeysClean("firstname","yyayy",  20);// 运行不了
			op.loopSendKeysClean("lastname", "jre",  20);
			op.loopSendKeysClean("email", "yyayy@163.com",  20);
			op.loopClickElement("comfirmEdit", 3, 20, 40);
			Page.pause(2);
			if (baseURL.contains("/es/")) {
				if (op.loopGetElementText("sucessMsg", 3, 20).equals("La información de la dirección de entrega se ha actualizado correctamente！")) {
					Log.logInfo("数据提交成功！！！");
				} else {
					Log.logError("数据提交失败！！！");
				}
			}else{
				System.out.println(op.loopGetElementText("sucessMsg", 3, 20));
				if (op.loopGetElementText("sucessMsg", 3, 20).equals("Receipt of your address information has been successfully updated！")) {
					Log.logInfo("数据提交成功！！！");
				} else {
					Log.logError("数据提交失败！！！");
				}
			}
			
			// 获取更改的值
			op.loopGet(adressBookUrl, 40, 3, 60);
			Page.pause(2);
			op.loopClickElement("EditBtton", 3, 10, 20);// 点击地址管理
	
			firstname = driver.findElement(By.xpath("//*[@id='ship_addr_list']/div[1]/ul/li[1]/span[1]")).getText();
			lastName = driver.findElement(By.xpath("//*[@id='ship_addr_list']/div[1]/ul/li[1]/span[2]")).getText();
			email = driver.findElement(By.xpath("//*[@id='ship_addr_list']/div[1]/ul/li[7]/span")).getText();
	
			boolean b1 = firstname.equals("yyayy");
			boolean b2 = lastName.equals("jre");
			boolean b3 = email.equals("yyayy@163.com");
			if (b1 && b2 && b3) {
				Log.logInfo("编辑地址信息，保存成功！！！");
			} else {
				Log.logError("编辑地址信息，保存失败！！！");
			}

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		} 
	}

	/**
	 * 测试项：地址- 编辑地址取消
	 * 验证点： 1.取消编辑 2.地址栏中的地址与编辑器的一致
	 * 适用环境：测试环境，线上
	 * 
	 */
	@Test
	public void EditAdressAndCancle(){	
		try {
			// 获取更改的值
			op.loopGet(adressBookUrl, 40, 3, 60);
			op.loopClickElement("EditBtton", 3, 10, 20);			
			String firstname1 = driver.findElement(By.xpath("//*[@id='ship_addr_list']/div[1]/ul/li[1]/span[1]")).getText();
			String lastName1 = driver.findElement(By.xpath("//*[@id='ship_addr_list']/div[1]/ul/li[1]/span[2]")).getText();
			String email1 = driver.findElement(By.xpath("//*[@id='ship_addr_list']/div[1]/ul/li[7]/span")).getText();
				
			//清除数据
			op.loopSendClean("firstname", 3, 4);
			op.loopSendClean("lastname", 3, 4);
			op.loopSendClean("email", 3, 4);
			driver.navigate().refresh();
	
			op.loopClickElement("EditBtton", 3, 10, 20);
			Page.pause(2);
			String firstname2 = driver.findElement(By.xpath("//*[@id='ship_addr_list']/div[1]/ul/li[1]/span[1]")).getText();
			String lastName2 = driver.findElement(By.xpath("//*[@id='ship_addr_list']/div[1]/ul/li[1]/span[2]")).getText();
			String email2 = driver.findElement(By.xpath("//*[@id='ship_addr_list']/div[1]/ul/li[7]/span")).getText();
			
			if(firstname1.equals(firstname2)&&lastName1.equals(lastName2)&&email1.equals(email2)){
				Log.logInfo("取消编辑，地址栏中的数据与源数据一致！！！！！");
			}else {
				Log.logError("取消编辑，地址栏中的数据与源数据不一致！！！！！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		
		}
		
	}

	/**
	 * 测试项：地址-删除单个地址
	 * 验证点：1.删除成功 2.地址栏中无删除后的地址
	 * 适用环境：测试环境，线上
	 * @throws Exception 
	 * 
	 */
	@Test
	public void DeleteAdressSinger() throws Exception{
		//获取地址的个数
		int startSumAdress = op.getElements("ShippingAddressList").size();
		Log.logInfo("获取的总共的地址个数为："+startSumAdress);
		if(startSumAdress>0){			
			driver.findElement(By.cssSelector("a[class='hBtn js_attr_deleteBtn']")).click();		
			try {
				op.loopClickElement("ConfirmDeletion", 3, 10, 20);
				op.loopClickElement("Address_page", 3, 10, 20);
				Log.logInfo("删除地址成功");
			} catch (Exception e) {
				e.printStackTrace();
			}//点击地址管理
		}
		int endSumAdress =  op.getElements("ShippingAddressList").size();
		Log.logInfo("删除后的地址个数为:"+endSumAdress);
		if((startSumAdress-1)==endSumAdress){
			Log.logInfo("删除单个地址成功,验证通过！！！");
		}else {
			Log.logError("删除单个地址失败，验证失败！！！");
		}
	}

	/**
	 *
	 * @测试点: 删除全部地址
	 * @验证点: 1.删除成功2.地址栏中无删除后的地址 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @throws Exception 
	 * @date 2016年12月7日
	 */
	@Test
	public void DeleteAdressAall() throws Exception {
		Page.pause(2);
		int startSumAdress =  op.getElements("ShippingAddressList").size();
		System.out.println(startSumAdress);
		if (startSumAdress > 1) {
			Log.logInfo("初始地址个数：" + startSumAdress);
			deleteAdress();// 删除全部的地址
		} else {
			
			//新增地址三个地址
			for(int i=0;i<3;i++){
				addAdress();
			}
			 Log.logInfo("初始地址个数："+startSumAdress+3); 
			 deleteAdress();//删除全部的地址
			  }
		// 删除之后地址个数
		int endSumAdress =  op.getElements("ShippingAddressList").size();
		if (endSumAdress == 0) {
			Log.logInfo("删除全部成功！！！");
		} else {
			Log.logError("删除全部失败！！！");
		}
	}

	/**
	 * 登录，由于测试地址的前置条件，在beforeClass中运行
	 *
	 */

	public void login() {
		String requestId = Pub.getRandomString(6);
		String methodName = GetMethodName();
		boolean loginFlag = true;// 判断是否有登录
		try {
			number++;
			String logincode = InterfaceMethod.IF_Verification(driver, domainName, requestId, getkeys, "login");
			if (logincode.equals("verify") && number < 6) {
				driver.navigate().refresh();
				Page.pause(7);
				login();
			} else {
				op.loopSendKeys("Login_EmailEnter", "autotest17@globalegrow.com", 3, 20);
				op.loopSendKeys("Login_PasswordEnter", "123456", 3, 20);
				if (op.isElementPresent("Login_verification",60)) {
					op.loopSendKeys("Login_verification", logincode, 3, 20);
				} else {
					Log.logInfo("验证码输入框已被关闭，不输入验证码登录");
				}
				op.loopClickElement("Login_SignBtn", 300, 5, 20);
				 for (int i = 0; i < 10; i++) {// 判断登录成功
						Page.pause(2);
						Log.logInfo("第" + i + "次");
						String LoginStats = op.loopGetElementText("signlinkName", 5, 20);
						if (LoginStats.contains("Hi, autot")) {
							Log.logInfo("登录成功");
							loginFlag = false;
							break;
						}
					}
					if (loginFlag == true) {
						captureScreenAll(projectName, methodName);
						Log.logError("登录失败，以下内容不执行");
						Assert.fail();
					}
				} 

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	int number = 0;
		
	

	
	
	/**
	 * 根据length长度，获取【a~z】随机字符串
	 * @param length
	 * @return String
	 *
	 */
	public static String getRandomString(int length){  
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";  
        Random random = new Random();  
        StringBuffer randomstr = new StringBuffer();  
          
        for(int i = 0 ; i < length; ++i){  
            int number = random.nextInt(52);//[0,52)              
            randomstr.append(str.charAt(number));  
        }
        return randomstr.toString();
    }
	/**
	 * 
	 * @测试点: clearFieldValue
	 * @验证点: 清空字段中所有的值 @使用环境： 测试环境 
	 * @备注： 目前是使用的老方法
	 * @author zhangjun
	 * @date 2016年12月7日
	 */
	public void clearFieldValue() {

		try {
			if (op.isElementPresent("shippingAddressButtom")){
			op.loopClickElement("shippingAddressButtom", 3, 10, 20);
			// Address按钮
			op.loopSendClean("firstname", 2, 2);
			op.loopSendClean("lastname", 2, 2);
			op.loopSendClean("email", 2, 2);
			op.loopSendClean("addressline1", 2, 2);
			op.loopSendClean("addressline2", 2, 2);
			op.getSelect("CountryRegion",10).selectByIndex(0);
			Page.pause(2);
			//op.getSelect("province",10).selectByIndex(0);
			Page.pause(1);
			op.loopSendClean("city", 2, 2);
			op.loopSendClean("tel", 2, 2);
			op.loopSendClean("addressline2", 2, 2);
			}else{
				Log.logError("新增按钮未找到，以下程序不执行");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @测试点: setFieldValue
	 * @验证点: 设置各个字段的值 @使用环境： @param methodName 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月7日
	 */
	public void setFieldValue(String methodName,String [] values) {
		try {
			op.loopGetElementText("shippingAddressButtom", 3, 20);// 点击Add a new
			// Address按钮
			String[] controls={"firstname","lastname","email","addressline1","city","tel","zipcode"};
			for (int i = 0; i < controls.length; i++) {
				op.loopSendKeysClean(controls[i],values[i],  20);
				
			}			
			if (methodName.equals("CountryOrRegionIsNull")) {
				Log.logInfo("下拉框不需要赋值！！！");
			} else if (methodName.equals("StateOrCountyIsNull")) {
				op.getSelect("CountryRegion", 10).selectByVisibleText("Angola");

				Page.pause(1);
			} else {
				op.getSelect("CountryRegion", 10).selectByVisibleText("Angola");// 给CountryRegion赋值
				Page.pause(2);
				op.getSelect("province",10).selectByVisibleText("Bengo");//给province赋值
				
			}
			if (methodName.equals("StateOrCountyIsNull")) {
				op.getSelect("CountryRegion", 10).selectByVisibleText("Angola");// 给CountryRegion赋值
				Page.pause(1);
			}
			Page.pause(1);
			op.loopClickElement("comfirmEdit", 3, 20, 40);// 点击Add a new

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 新增地址
	 */
	public void addAdress() {
		int x = (int) (Math.random() * 5 + 3);// 随机的下拉框选择值
		Log.logInfo(x);
		try {
			if(op.isElementPresent("shippingAddressButtom")){
			op.loopClickElement("shippingAddressButtom", 3, 20, 40);// 点击Add a// // new
			op.loopSendKeys("firstname", getRandomString(8), 3, 10);
			op.loopSendKeys("lastname", getRandomString(6), 3, 10);
			op.loopSendKeys("email", getRandomString(15) + "@163.com", 3, 10);
			op.loopSendKeys("addressline1", "shanghai," + getRandomString(8), 3, 10);
			op.loopSendKeys("addressline2", "shenzhen," + getRandomString(8), 3, 10);
			op.getSelect("CountryRegion", 10).selectByIndex(x);
			Page.pause(3);
			// 判断元素类型
			WebElement elem = driver.findElement(By.cssSelector("#addressfrom > ul > li:nth-child(7) > div > div"));
			String province = elem.getAttribute("innerHTML");
			if (province.contains("select")) {
				op.getSelect("province", 20).selectByIndex(1);// 给province赋值
			} else {
				op.loopSendKeysClean("province", "chengdu",20);
			}
			op.loopSendKeys("city", "shanghai", 2, 10);// 给city赋值
			op.loopSendKeys("tel", "(0756)3383276", 2, 10);// 给tel赋值
			op.loopSendKeys("zipcode", "547506", 2, 10);// 给zipcode赋值
			Page.pause(1);
			op.loopClickElement("comfirmEdit", 3, 20, 40);// 点击Add a ne
			/*Page.pause(2);
			driver.navigate().to(adressBookUrl);*/
			op.loopGet(adressBookUrl, 40, 3, 60);
			//Page.pause(3);
			}else{
				Log.logError("新增按钮不存在，不执行以下内容");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @测试点: deleteAdress
	 * @验证点: 删除地址 @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2016年12月7日
	 */
	public void deleteAdress() {

		List<WebElement> ement = driver.findElements(By.cssSelector("a[class='hBtn js_attr_deleteBtn']"));
		try {
			for (int i = 0; i < ement.size(); i++) {
				Page.pause(1);
				driver.findElement(By.cssSelector("a[class='hBtn js_attr_deleteBtn']")).click();
				Page.pause(2);
				op.loopClickElement("ConfirmDeletion", 3, 20, 40);// 点击 删除按钮
				op.loopClickElement("Address_page", 3, 20, 50);// 确认删除
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void urlInit() {
		if(baseURL.contains(".trunk.")&&baseURL.contains("/es/")){//西班牙语测试环境
			Log.logInfo("现在是西班牙语测试环境");
			adressBookUrl = "http://user.rosewholesale.com.trunk.s1.egomsl.com/es/m-users-a-address_list.htm";	
			domainName="http://rosewholesale.com.trunk.s1.egomsl.com/";
		}else if(baseURL.contains("/es/")){
			Log.logInfo("现在是西班牙语正式环境");
			adressBookUrl = "http://user.rosewholesale.com/es/m-users-a-address_list.htmm-users-a-profile.htm";	
			domainName="https://rosewholesale.com/";
		}else if (baseURL.contains(".d.")) {
			adressBookUrl = "http://user.rosewholesale.com.d.s1.egomsl.com/m-users-a-address_list.htm";	
			domainName="http://rosewholesale.com.d.s1.egomsl.com";
		}  else {
			adressBookUrl = "http://user.rosewholesale.com/m-users-a-address_list.htmm-users-a-profile.htm";
			domainName="https://rosewholesale.com/";
		}
	}
	
	
	
	@Test
    public void a(){
	  correFail = false;
	  Log.logError("我的案例a，我注定要要失败");
	}
	@Test
	public void b(){
		Pub.checkStatusBrowser();
		Log.logInfo("我是案例b，我调用Pub.checkStatusBrowser是为了做关联，如果a失败了，那么我也就会失败");
	}
	@Test
	public void c(){
		/*correFail = false;
		Pub.checkStatusBrowser();*/
		Log.logInfo("我是案例c，我和a没有一点关系,因为我写上了correFail = false;，或者我可以不用调用Pub.checkStatusBrowser()这个方法，我就可以不用设置关联性了");
	}
}
