package com.globalegrow.custom;


import java.io.IOException;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.globalegrow.base.StartPhoneBrowser;
import com.globalegrow.code.Page;
import com.globalegrow.util.Log;
import com.globalegrow.util.OpApp;


public class ControlUsingMethod extends StartPhoneBrowser{
	static boolean status=false;//是否弹出网络不好的框
	/**
	 * 
	* @测试点: SetScrollBar 
	* @验证点: 页面滑动
	* @param higth 向下滑动的高度    测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年1月23日 
	  @修改说明
	 */
	public static void SetScrollBar(int higth) {		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, " + higth + ")");
	}	

	
	/**
	 * 
	* @测试点: 设置元素可见 
	* @验证点: 针对ID控件设置元素可见
	* @使用环境： 测试环境，正式环境
	* @param idname    控件id
	* @备注： void    
	* @author zhangjun 
	* @date 2017年3月8日 
	  @修改说明
	 */
	public static void SetDisplay(String idname){
	JavascriptExecutor js= (JavascriptExecutor)driver;
	js.executeScript("document.findElementById('+idname+').style.display='block'; ");
	}
	/**
	 * 
	* @测试点: 使用js CSS的方式修改页面属性 ,显示控件   
	* @使用环境：测试环境，正式环境
	*  @param driver  driver
	*  @param css   控件的css
	* @备注： void    
	* @author zhangjun 
	* @date 2017年4月15日 
	  @修改说明
	 */
	public  static void setDisplayCSS(WebDriver driver, String  css) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String myjs = "document.querySelector(\'"+css+"\').style.display='block';";
		js.executeScript(myjs);
	}
	
	
	/**
	 * 
	* @测试点: 使用js CSS的方式修改页面属性，隐藏属性    
	* @使用环境：测试环境，正式环境
	*  @param driver  driver
	*  @param css   控件的css
	* @备注： void    
	* @author zhangjun 
	* @date 2017年4月15日 
	  @修改说明
	 */
	public static void setHiddenCSS(WebDriver driver, String  css) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String myjs = "document.querySelector(\'"+css+"\').style.display='none';";
		js.executeScript(myjs);
	}

	/**
	 * 
	* @测试点: 获取不同窗口 
	* @验证点: 切换窗口
	* @使用环境：测试环境，正式环境
	* @param driver   driver
	* @param currentwindow   主窗口句柄  
	* @备注： void    
	* @author zhangjun 
	* @date 2017年4月18日 
	  @修改说明
	 */
	public static void switchingWindow(WebDriver driver,String currentwindow) {
		if(driver.getWindowHandles().size()>1){
		Log.logInfo("获取的主窗口句柄为:" + currentwindow);
		for (String toHandle : driver.getWindowHandles()) {
			if (toHandle.equals(currentwindow)) {
				continue;
			}
			driver.switchTo().window(toHandle);// 切换到另外一个窗口
			Log.logInfo("跳转到新页面的句柄为:" + toHandle);
			Log.logInfo("打印现页面的标题为:" + driver.getTitle());
		}
		}else{
			Log.logInfo("窗口句柄只有一个");
		}
	}
	
	/**
	 * 
	* @测试点: 关闭窗口 
	* @验证点: 关闭当前窗口跳转到主窗口
	* @使用环境： 测试环境，正式环境
	* @param currentwindow   主窗口句柄
	* @备注： void    
	* @author zhangjun 
	* @date 2017年3月23日 
	  @修改说明
	 */
	public static void switchingCloseWindow(WebDriver driver,String currentwindow){
		if(driver.getWindowHandles().size()>1){
		driver.switchTo().defaultContent();
		driver.close();//关掉这个窗口
		driver.switchTo().window(currentwindow);//跳转到以前的窗口
		}else{
			Log.logInfo("句柄只有一个，不用关闭");
		}
	}
	
	
	/**
	 * 
	* @测试点: 判断是否有网络异常的问题 
	* @验证点:如果有网络异常，点击按钮，关闭app，在使用命令启动app ,循环5次，如果都还有错误，脚本报错
	* @使用环境：测试环境，正式环境
	* @param appPackage  包名 
	* @param appActivity     app的activity
	* @备注： void    
	* @author zhangjun 
	* @date 2017年6月22日 
	  @修改说明
	 */
	
	public static boolean  existAbnormalBox(OpApp opApp,String packageName,String appactivity ){
		int i = 0;
		while (i < 2) {
			if (opApp.elementExists(5, "AbnormalLoginBox")) {
				status=true;
				String abnorma = opApp.GetElementText("AbnormalLoginBox", 20, 10);
				Log.logInfo("获取的异常信息为:" + abnorma);
				opApp.ClickElement("AbnormalLoginCancel", 20);// 点击cancel退出app
				Page.pause(3);
				String runCommand = "adb shell am start -n" + packageName + "/" + appactivity;
				Log.logInfo("执行启动命令：" + runCommand);
				try {
					Runtime.getRuntime().exec(runCommand);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				break;
			}
			i++;
		}
		return status;
		
	}
	
	/**
	 * 
	* @测试点: 运行adb的命令，
	 */
	public static void runadb(){
	String 	runString="adb shell input keyevent 4";//返回到主界面
		try {
			Runtime.getRuntime().exec(runString);
			Log.logInfo("执行的命令为:"+runString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	* @测试点: 对数字进行排序 
	* @验证点: 排序是从小到大
	  @param @param numbers 排序的内容
	  @param @return  返回排序后的数组 
	* @备注： Double[]  
	* @author zhangjun 
	* @date 2017年8月5日 
	  @修改说明
	 */
	public static Double[] selectSortSmall(Double[] numbers) {   
	    int size = numbers.length;
		Double temp;   
	    for (int i = 0; i < size; i++) {   
	        int k = i;   
	        for (int j = size - 1; j >i; j--)  {   
	            if (numbers[j] < numbers[k])  k = j;   
	        }   
	        temp = numbers[i];   
	        numbers[i] = numbers[k];   
	        numbers[k] = temp;   
	    }
		return numbers;  
	    
	}  
	

	/**
	 * 
	* @测试点: 对数字进行排序 
	* @验证点: 排序是从大到小
	  @param @param numbers 排序的内容
	  @param @return  返回排序后的数组 
	* @备注： Double[]  
	* @author zhangjun 
	* @date 2017年8月5日 
	  @修改说明
	 */
	public static Double[] selectSortBig(Double[] numbers) {   
	    int size = numbers.length;
		Double temp;   
	    for (int i = 0; i < size; i++) {   
	        int k = i;   
	        for (int j = size - 1; j >i; j--)  {   
	            if (numbers[j] > numbers[k])  k = j;   
	        }   
	        temp = numbers[i];   
	        numbers[i] = numbers[k];   
	        numbers[k] = temp;   
	    }
		return numbers;  
	    
	}  
	
}
