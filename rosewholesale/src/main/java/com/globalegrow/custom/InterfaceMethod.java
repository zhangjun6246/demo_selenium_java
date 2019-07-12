package com.globalegrow.custom;


import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import com.globalegrow.util.JsonUtil;
import com.globalegrow.util.Log;
import com.globalegrow.util.Op;
import com.globalegrow.util.Pub;
import com.globalegrow.util.JsonUtil.TypeEnum;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.bcel.internal.generic.RETURN;

import net.sf.json.JSONObject;

public class InterfaceMethod {
	 Object min;
	 Object max;
	 String timeStamp;// 时间戳

	 public InterfaceMethod(){
		 Date date = new Date();
		 SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyyMMddHHmmss");
		 timeStamp= simpleDateFormat.format(date);
		
	 }
	
	/**
	 * 
	 * @测试点: getGoodUrl
	 * @验证点: 获取最大最小、第一个商品地址
	 * @param myPublicUrl
	 *            域名
	 * @param type
	 *            min:最小价格商品 one：第一个商品 max:最大价格商品
	 * 
	 * @使用环境：测试环境，正式环境
	 * @return 返回地址 @备注： String
	 * @author zhangjun
	 * @date 2017年2月13日
	 * @修改说明
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getGoodUrl(String myPublicUrl,String type) {
		long startTime = System.currentTimeMillis();
		String url = myPublicUrl
				+ "auto/api.php?mod=common&act=search_goods&min_price=3&max_price=70&page_size=30&is_clearance=n&is_promote=n&is_freeshipping=n";

		Map<String, String> result = new HashMap<String, String>();
		try {
			result = Pub.get(url);

		} catch (Exception e1) {
			Log.logInfo("获取get请求出错，请求接口：" + url);
			Log.logInfo("报错信息：" + e1.getMessage());
			e1.printStackTrace();
		}
		String jsonString = result.get("Response");
		Object obj = JsonUtil.getObjectByJson(jsonString, "goods_list", TypeEnum.map);// 层级递归Map
		Set<String> keSet = ((Map) obj).keySet();

		List<Double> priceList = new ArrayList<Double>();
		List<String> urlList = new ArrayList<String>();
		for (Iterator<String> iterator = keSet.iterator(); iterator.hasNext();) {
			String goodid = iterator.next();
			Map goodInfo = (Map) ((Map) obj).get(goodid);
			priceList.add(Double.parseDouble(goodInfo.get("shop_price").toString()));
			urlList.add(goodInfo.get("url_title").toString());
		}
		double prices = 0.00;
		int wz = 0;
		if (type.equals("min")) {
			for (int i = 0; i < priceList.size(); i++) {
				if (i == 0) {
					prices = priceList.get(i);
					continue;
				}
				if (priceList.get(i) < prices) {
					prices = priceList.get(i);
					wz = i;
				}
			}
		} else if (type.equals("max")) {
			for (int i = 0; i < priceList.size(); i++) {
				if (i == 0) {
					prices = priceList.get(i);
					continue;
				}
				if (priceList.get(i) > prices) {
					prices = priceList.get(i);
					wz = i;
				}
			}
		} else if (type.equals("one")) {
			wz = 0;
		} else {
			Log.logInfo("传入的参数有误，参数类型有min/max,实际传入的参数：" + type);
		}
		String good_url = urlList.get(wz);
		System.out.print("PC获取的商品" + good_url);
		long endTime = System.currentTimeMillis();
		System.out.println("得到地址的时间为：" + (endTime - startTime) + "ms");

		good_url = good_url + "?" + timeStamp;// 刷新缓存
		return good_url;
	}

	/**
	 * 
	 * @测试点: 获取一款商品
	 * @验证点: 给出指定的条件查询一款商品
	 * @param type
	 *            取的的类型 类型[min/max/one]min:返回价格最小的商品url,max:返回价格最大商品的url,one:
	 * @param myPublicUrl
	 *            获取域名
	 * @param clearance
	 *            是否为清仓 y:是 N：不是
	 *
	 * @param promote
	 *            是否为促销 y:是 N：不是
	 * @return 返回获取到的第一款商品 @备注： String
	 * @author
	 * @date 2016年12月26日
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getGoodUrl(String type, String myPublicUrl, String clearance, String promote) {
		String url = myPublicUrl
				+ "auto/api.php?mod=common&act=search_goods&min_price=3&max_price=60&page_size=30&is_clearance="
				+ clearance + "&is_promote=" + promote + "&is_freeshipping=n";

		Map<String, String> result = new HashMap<String, String>();
		try {
			result = Pub.get(url);
		} catch (Exception e1) {
			Log.logInfo("获取get请求出错，请求接口：" + url);
			Log.logInfo("报错信息：" + e1.getMessage());
			e1.printStackTrace();
		}
		String jsonString = result.get("Response");
		Object obj = JsonUtil.getObjectByJson(jsonString, "goods_list", TypeEnum.map);// 层级递归Map
		Set<String> keSet = ((Map) obj).keySet();

		List<Double> priceList = new ArrayList<Double>();
		List<String> urlList = new ArrayList<String>();
		for (Iterator<String> iterator = keSet.iterator(); iterator.hasNext();) {
			String goodid = iterator.next();
			Map goodInfo = (Map) ((Map) obj).get(goodid);
			priceList.add(Double.parseDouble(goodInfo.get("shop_price").toString()));
			urlList.add(goodInfo.get("url_title").toString());
		}
		double prices = 0.00;
		int wz = 0;
		if (type.equals("min")) {
			for (int i = 0; i < priceList.size(); i++) {
				if (i == 0) {
					prices = priceList.get(i);
					continue;
				}
				if (priceList.get(i) < prices) {
					prices = priceList.get(i);
					wz = i;
				}
			}
		} else if (type.equals("max")) {
			for (int i = 0; i < priceList.size(); i++) {
				if (i == 0) {
					prices = priceList.get(i);
					continue;
				}
				if (priceList.get(i) > prices) {
					prices = priceList.get(i);
					wz = i;
				}
			}
		} else if (type.equals("one")) {
			wz = 0;
		} else {
			Log.logInfo("传入的参数有误，参数类型有min/max,实际传入的参数：" + type);
		}
		String good_url = urlList.get(wz);
		System.out.print("获取的商品" + good_url);
		// 取得商品库存，和商品的编号
		good_url = good_url + "?" + timeStamp;// 刷新缓存
		return good_url;

	}

	/**
	 * 
	 * @测试点:获取指定币种的价格和商品
	 * @验证点:取指定的币种
	 * @param type
	 *            min:最小价格商品 one：第一个商品 max:最大价格商品
	 * @param myPublicUrl
	 *            域名
	 * @param bizhong
	 *            币种
	 *  @param clearance
	 *            是否为清仓 y:是 N：不是
	 *
	 * @param promote
	 *            是否为促销 y:是 N：不是
	 * @使用环境：测试环境，正式环境
	 * @return 返回地址 @备注： String
	 * @author zhangjun
	 * @date 2017年2月13日
	 * @修改说明
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getGoodUrl(String type, String myPublicUrl, String clearance, String promote, String bizhong) {
		long startTime = System.currentTimeMillis();
		String url =  myPublicUrl
				+ "auto/api.php?mod=common&act=search_goods&min_price=3&max_price=50&page_size=30&is_clearance="+clearance+"&is_promote="+promote+"&is_freeshipping=n&bizhong="
				+ bizhong;

		Map<String, String> result = new HashMap<String, String>();
		try {
			result = Pub.get(url);

		} catch (Exception e1) {
			Log.logInfo("获取get请求出错，请求接口：" + url);
			Log.logInfo("报错信息：" + e1.getMessage());
			e1.printStackTrace();
		}
		String jsonString = result.get("Response");
		Object obj = JsonUtil.getObjectByJson(jsonString, "goods_list", TypeEnum.map);// 层级递归Map
		Set<String> keSet = ((Map) obj).keySet();

		List<Double> priceList = new ArrayList<Double>();
		List<String> urlList = new ArrayList<String>();
		for (Iterator<String> iterator = keSet.iterator(); iterator.hasNext();) {
			String goodid = iterator.next();
			Map goodInfo = (Map) ((Map) obj).get(goodid);
			priceList.add(Double.parseDouble(goodInfo.get("shop_price").toString()));
			urlList.add(goodInfo.get("url_title").toString());
		}
		double prices = 0.00;
		int wz = 0;
		if (type.equals("min")) {
			for (int i = 0; i < priceList.size(); i++) {
				if (i == 0) {
					prices = priceList.get(i);
					continue;
				}
				if (priceList.get(i) < prices) {
					prices = priceList.get(i);
					wz = i;
				}
			}
		} else if (type.equals("max")) {
			for (int i = 0; i < priceList.size(); i++) {
				if (i == 0) {
					prices = priceList.get(i);
					continue;
				}
				if (priceList.get(i) > prices) {
					prices = priceList.get(i);
					wz = i;
				}
			}
		} else if (type.equals("one")) {
			wz = 0;
		} else {
			Log.logInfo("传入的参数有误，参数类型有min/max,实际传入的参数：" + type);
		}
		String good_url = urlList.get(wz);
		System.out.print("PC获取的商品" + good_url);
		long endTime = System.currentTimeMillis();
		System.out.println("得到地址的时间为：" + (endTime - startTime) + "ms");
		good_url = good_url + "?" + timeStamp;// 刷新缓存
		return good_url;
	}

	/**
	 * 
	 * 
	 */
	/**
	 * 
	 * @测试点: 清除购物车商品
	 * @验证点: 使用接口进行清空，如果接口清空失败，在界面进行清空
	 * @param myPublicUrl
	 *            涉及的url,域名，目前调用的PC的接口
	 * @param email
	 *            清除的邮件 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年2月3日
	 * @修改说明
	 */
	public void crearAllCartGoods(String myPublicUrl, String email) {
		String url =myPublicUrl + "auto/api.php?mod=user&act=clear_cart&email=" + email;
		Log.logInfo("url" + url);
		Map<String, String> result = new HashMap<String, String>();
		try {
			result = Pub.get(url);
		} catch (Exception e1) {
			Log.logInfo("获取get请求出错，请求接口：" + url);
			Log.logInfo("报错信息：" + e1.getMessage());
			e1.printStackTrace();
		}
		String jsonString = result.get("Response");// 获取响应的数据
		Object obj = JsonUtil.getObjectByJson(jsonString, "_resultcode", TypeEnum.string);// 层级递归Map

		if (obj.toString().equals("200")) {
			Log.logInfo("购物车商品清空成功！！！");
		} else {
			Log.logInfo("清空购物车失败！！！！");
		}
	}

	/**
	 * 获取登录的验证码
	 */
	/**
	 * 
	 * @测试点: 获取验证码
	 * @验证点: 通过R_SESSIONID获取cookie
	 * @param driver
	 * @param domainName
	 *            域名
	 * @param requestId
	 *            随机的用户名
	 * @param privatekey
	 *            测试的key，分正式环境和测试环境 @使用环境： 测试环境，正式环境
	 * @return @备注： String
	 * @author zhangjun
	 * @date 2017年2月9日
	 * @修改说明
	 */
	public static String IF_Verification(WebDriver driver, String domainName, String requestId, String privatekey) {
		String getdata = null;
		String getsign;
		String getResponse;
		String getverification;
		Set<Cookie> cookies = driver.manage().getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("R_SESSIONID")) { // 取出cookie值
				// getdata = "a:1:{s:10:%22session_id22;s:26:%" +
				// cookie.getValue() + "%22;}";// 生成虚序列化data
				getdata = "a:1:{s:10:\"session_id\";s:26:\"" + cookie.getValue() + "\";}";// 生成虚序列化data
			}
		}
		getsign = privatekey + requestId + getdata;// 获得私钥
		System.out.println(requestId);
		String sign = Pub.GetMD5String(getsign);// 生成MD5
		String URLCommand = domainName + "/auto/auto/get-verify-code/?api_session=11&requestId=" + requestId
				+ "&sign=" + sign + "&data=" + getdata;
		System.out.println(URLCommand);
		Map<String, String> res =Pub.get(URLCommand);
		getResponse = res.get("Response");

		int beginIndex = getResponse.lastIndexOf("s:6");// jsoin解析不出来，暂用字符串形式截取验证码
		getverification = getResponse.substring(beginIndex + 6, beginIndex + 12); // 取得字符串
		System.out.println(getverification);
		return getverification;

	}

	
	/**
	 * @测试点: PC上获取验证码 
	 * @验证点:通过cookies 获得验证码
	 * @param driver  
	 * @param domainName   测试路径
	 * @param requestId  请求的id（每次请求保持唯一性）
	 * @param privatekey 测试的key，需分区正式和测试
	 * @param ftc   验证码类型，登录=login，注册=regist，忘记密码ftc=''
	 * @return    测试环境
	 * @备注： String    
	 * @author zhangjun,创建时间：2016-12-02,更新时间：2016-12-07
	 * 更新说明(2016-12-07)：by yuyang,由PublicFun移动到Pub
	 */
	public static String IF_Verification(WebDriver driver, String domainName, String requestId, String privatekey, String ftc) {
		String getdata = null;
		String getsign;
		String getResponse;
		String getverification;
		Set<Cookie> cookies = driver.manage().getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("PHPSESSID")) { // 取出cookie值
				getdata = "a:1:{s:10:\"session_id\";s:26:\"" + cookie.getValue() + "\";}";// 生成虚序列化data
			}
		}

		getsign = privatekey + requestId + getdata;// 获得私钥
		System.out.println(requestId);
		String sign = Pub.GetMD5String(getsign);// 生成MD5
		String URLCommand = domainName + "auto/api.php?mod=common&act=get_user_sign&requestId=" + requestId + "&sign=" + sign + "&data="
				+ getdata + "&ftc=" + ftc;
		System.out.println(URLCommand);
		Map<String, String> res = Pub.get(URLCommand);
		getResponse = res.get("Response");

		int beginIndex = getResponse.lastIndexOf("s:6");//字符串形式截取验证码
		getverification = getResponse.substring(beginIndex + 6, beginIndex + 12); // 取得字符串
		System.out.println(getverification);
		return getverification;

	}

	
	/**
	 * 
	 * @测试点: 取消订单成功
	 * @验证点: 清除取消的订单
	 * @使用环境： 测试环境，正式环境
	 * @param myPublicUrl传入的域名
	 * @param email 操作的邮箱
	 * @author zhangjun
	 * @date 2017年2月11日
	 * @修改说明
	 */
	public void deleteCancelOrder(String myPublicUrl, String email) {

		Map<String, String> result = new HashMap<String, String>();
		String url = myPublicUrl + "auto/api.php?act=del_user_order&mod=user&email=" + email;
		try {
			result = Pub.get(url);
		} catch (Exception e1) {
			Log.logInfo("获取get请求出错，请求接口：" + url);
			Log.logInfo("报错信息：" + e1.getMessage());
			e1.printStackTrace();
		}
		String jsonString = result.get("Response");// 获取响应的数据
		Object obj = JsonUtil.getObjectByJson(jsonString, "_resultcode", TypeEnum.string);
		if (obj.toString().equals("200")) {
			Log.logInfo("清除取消的订单成功！");
		} else {
			Log.logInfo("清除取消的订单失败，执行接口url：" + url);
		}
	}

	

	/**
	 * 
	 * @测试点: clearFavoritesProduct
	 * @验证点: 清理用户收藏商品
	 * @param myPublicUrl传入的域名
	 * @param email 操作的邮箱
	 * @使用环境： 测试环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月2日
	 */
	public void clearFavoritesProduct(String myPublicUrl,String email ) {
		String url = myPublicUrl + "auto/api.php?mod=user&act=clear_user_collect&email=" + email;

		Map<String, String> result = new HashMap<String, String>();
		try {
			result = Pub.get(url);
		} catch (Exception e1) {
			Log.logInfo("获取get请求出错，请求接口：" + url);
			Log.logInfo("报错信息：" + e1.getMessage());
			e1.printStackTrace();
		}
		String jsonString = result.get("Response");// 获取响应的数据
		Object obj = JsonUtil.getObjectByJson(jsonString, "_resultcode", TypeEnum.string);// 层级递归Map

		if (obj.toString().equals("200")) {
			Log.logInfo("收藏夹商品清空成功！！！");
		} else {
			Log.logInfo("收藏夹清空失败！！！！");
		}
	}

	/**
	 * 
	 * @测试点: clearSaveForLater
	 * @验证点: 清空save for later 的记录
	 * @param myPublicUrl传入的域名
	 * @param email 操作的邮箱
	 * @使用环境： @param email 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月4日
	 * @修改说明
	 */

	public void clearSaveForLater(String myPublicUrl,String email) {
		String url = myPublicUrl + "auto/api.php?mod=user&act=clear_save_for_later&email=" + email;

		Map<String, String> result = new HashMap<String, String>();
		try {
			result = Pub.get(url);
		} catch (Exception e1) {
			Log.logInfo("获取get请求出错，请求接口：" + url);
			Log.logInfo("报错信息：" + e1.getMessage());
			e1.printStackTrace();
		}
		String jsonString = result.get("Response");// 获取响应的数据
		Object obj = JsonUtil.getObjectByJson(jsonString, "_resultcode", TypeEnum.string);// 层级递归Map

		if (obj.toString().equals("200")) {
			Log.logInfo("save for later清空成功！！！");
		} else {
			Log.logInfo("save for later清空失败！！！！");
		}
	}

	/**
	 * 
	 * @测试点: 删除浏览的历史记录
	 * @验证点: TODO(这里用一句话描述这个方法的作用) @使用环境： 测试环境，正式环境 @备注： void
	 * @author zhangjun
	 * @date 2017年1月4日
	 * @修改说明
	 */
	@SuppressWarnings("unused")
	public void clearDeleteHistory(String myPublicUrl) {
		String url = myPublicUrl + "auto/api.php?mod=common&act=clear_history";

		Map<String, String> result = new HashMap<String, String>();
		try {
			result = Pub.get(url);
		} catch (Exception e1) {
			Log.logInfo("获取get请求出错，请求接口：" + url);
			Log.logInfo("报错信息：" + e1.getMessage());
			e1.printStackTrace();
		}

	}

	/**
	 * 
	 * @测试点: 获取返回商品的所有内容，由用户去选择需要获取的商品的某些信息
	 * @验证点: 获取商品的goods_number
	 *
	 * @param clearance
	 *            是否清仓
	 * @param promote
	 *            是否促销
	 * @return 返回库存集合 @备注：
	 * @author zhangjun
	 * @date 2017年1月9日
	 * @修改说明
	 */

	@SuppressWarnings("unchecked")
	public   Map<String, Object> getGoods(String myPublicUrl,String clearance, String promote) {
		String url =  myPublicUrl
				+ "auto/api.php?mod=common&act=search_goods&min_price=6&max_price=50&page_size=30&is_clearance="
				+ clearance + "&is_promote=" + promote + "&is_freeshipping=n";
		Log.logInfo("测试地址" + url);
		Map<String, String> result = new HashMap<String, String>();
		try {
			result = Pub.get(url);

		} catch (Exception e1) {
			Log.logInfo("获取get请求出错，请求接口：" + url);
			Log.logInfo("报错信息：" + e1.getMessage());
			e1.printStackTrace();
		}
		String jsonString = result.get("Response");
	
		
		Map<String, Object> maps = (Map<String, Object>) JsonUtil.getObjectByJson(jsonString, "goods_list",TypeEnum.map);// 层级递归Map
		Map<String, Object> m = new HashMap<String, Object>();
		for (Entry<String, Object> entry : maps.entrySet()) {
			@SuppressWarnings("rawtypes")
			String getNumber = (String) ((Map) entry.getValue()).get("goods_number");
			if (Double.parseDouble(getNumber) > 2) {
				m = (Map<String, Object>) entry.getValue();
				break;
			}
		}
		return m;
	}
	
	
	/**
	 * 
	* @测试点: 获取商品的详细信息，不同是，商品数量库存大于2，价格小于10 
	* @验证点:  重载getGood，
	* @param myPublicUrl 域名
	*  @return  返回一个map ： Map<String,Object>    
	* @author zhangjun 
	* @date 2017年6月19日 
	  @修改说明
	 */
	@SuppressWarnings("unchecked")
	public   Map<String, Object> getGoods(String myPublicUrl) {
		String url = myPublicUrl
				+ "auto/api.php?mod=common&act=search_goods&min_price=5&max_price=50&page_size=30&is_clearance=n&is_promote=n&is_freeshipping=n";
		Log.logInfo("测试地址" + url);
		Map<String, String> result = new HashMap<String, String>();
		try {
			result = Pub.get(url);

		} catch (Exception e1) {
			Log.logInfo("获取get请求出错，请求接口：" + url);
			Log.logInfo("报错信息：" + e1.getMessage());
			e1.printStackTrace();
		}
		String jsonString = result.get("Response");

		Map<String, Object> m = new HashMap<String, Object>();
		Map<String, Object> maps = (Map<String, Object>) JsonUtil.getObjectByJson(jsonString, "goods_list",
				TypeEnum.map);// 层级递归Map
		for (Entry<String, Object> iterable_element : maps.entrySet()) {
			String getshop_price = (String) ((Map) iterable_element.getValue()).get("shop_price");
			if (Double.parseDouble(getshop_price) < 10) {
				m = (Map<String, Object>) iterable_element.getValue();
			}
		}
		return m;
	}
	/**
	 * 
	 * @测试点: 获取返回商品的所有内容，由用户去选择需要获取的商品的某些信息
	 * @验证点: 获取商品的goods_number
	 *
	 * @param clearance
	 *            是否清仓
	 * @param promote
	 *            是否促销
	 * @param bizhong
	 *            货币的币种
	 * @return 返回库存集合 @备注：
	 * @author zhangjun
	 * @date 2017年1月9日
	 * @修改说明
	 */

	@SuppressWarnings("unchecked")
	public   Map<String, Object> getGoods(String myPublicUrl,String clearance, String promote,String bizhong) {
		String url = myPublicUrl
				+ "auto/api.php?mod=common&act=search_goods&min_price=3&max_price=60&page_size=30&is_clearance="
				+ clearance + "&is_promote=" + promote + "&is_freeshipping=n"+"&bizhong="+bizhong;
		Log.logInfo("测试地址" + url);
		Map<String, String> result = new HashMap<String, String>();
		try {
			result = Pub.get(url);

		} catch (Exception e1) {
			Log.logInfo("获取get请求出错，请求接口：" + url);
			Log.logInfo("报错信息：" + e1.getMessage());
			e1.printStackTrace();
		}
		String jsonString = result.get("Response");
	
		Map<String, Object> maps = (Map<String, Object>) JsonUtil.getObjectByJson(jsonString, "goods_list",TypeEnum.map);// 层级递归Map
		Map<String, Object> m = new HashMap<String, Object>();
		for (Entry<String, Object> entry : maps.entrySet()) {
			@SuppressWarnings("rawtypes")
			String getNumber = (String) ((Map) entry.getValue()).get("goods_number");
			if (Double.parseDouble(getNumber) > 2) {
				m = (Map<String, Object>) entry.getValue();
				break;
			}
		}
		return m;
	}

	/**
	 * 
	 * @测试点: 查询商品的阶梯价
	 * @验证点: 输入sku，查询不同数量的阶梯价
	 * @param myPublicUrl 域名
	 * @param productId
	 *            商品的SKU
	 * @param number
	 *            商品的件数 @使用环境： @return 每件数量的阶梯价 @备注： double
	 * @author zhangjun
	 * @date 2016年12月28日
	 */
	public  double getLadderprice(String myPublicUrl,String productId, int number) {
		double goodsNumber = 0;
		String url =  myPublicUrl + "auto/api.php?mod=common&act=get_goods_info&goods_sn=" + productId;
		Map<String, String> result = new HashMap<String, String>();
		try {
			result = Pub.get(url);
		} catch (Exception e1) {
			Log.logInfo("获取get请求出错，请求接口：" + url);
			Log.logInfo("报错信息：" + e1.getMessage());
			e1.printStackTrace();
		}
		String jsonString = result.get("Response");
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>) JsonUtil.getObjectByJson(jsonString,
				"volume_price_list", TypeEnum.arrayList);// 层级递归Map
		for (Map<String, Object> volumePrice : list) {

			List<String> specification = new ArrayList<String>();
			Pattern pattern = Pattern.compile("[0-9,\\.]{1,}");
			Matcher matcher = pattern.matcher((CharSequence) volumePrice.get("number"));// 取出范围
			while (matcher.find()) {
				if (!"".equals(matcher.group()))
					specification.add(matcher.group());
			}
			if (specification.size() > 0)
				volumePrice.put("min", specification.get(0));// 分别取出第一位值，
			if (specification.size() > 1)
				volumePrice.put("max", specification.get(1));// 第二位值
			min = volumePrice.get("min");
			max = volumePrice.get("max");
			// Log.logInfo("取得的最小的件数为："+min+"取得最大的件数为:"+max);
			if (min != null && min.equals("1") && min.equals(number)) { // 对传入的件数进行比较
				goodsNumber = (double) volumePrice.get("price");
			} else if (null != min && null != max && Integer.valueOf(min.toString()) <= number
					&& Integer.valueOf(max.toString()) >= number) {
				goodsNumber = Double.parseDouble((String) volumePrice.get("price"));
			} else if (null != min && null == max && !min.equals("1") && Integer.valueOf(min.toString()) < number) {
				goodsNumber = Double.parseDouble((String) volumePrice.get("price"));
			}

		}

		return goodsNumber;
	}
	/**
	 * 
	 * @测试点: 查询商品的阶梯价
	 * @验证点: 输入sku，查询不同数量的阶梯价
	 * @param productId
	 *            商品的SKU
	 * @param number
	 *            商品的件数 @使用环境： @return 每件数量的阶梯价 @备注： double
	 * @param bizhong         
	 *        货币的币种 
	 * @author zhangjun
	 * @date 2016年12月28日
	 */
	public  double getLadderprice(String myPublicUrl,String productId, int number,String bizhong) {
		double goodsNumber = 0;
		String url = myPublicUrl + "auto/api.php?mod=common&act=get_goods_info&goods_sn=" + productId+"&bizhong="+bizhong;
		Map<String, String> result = new HashMap<String, String>();
		try {
			result = Pub.get(url);
		} catch (Exception e1) {
			Log.logInfo("获取get请求出错，请求接口：" + url);
			Log.logInfo("报错信息：" + e1.getMessage());
			e1.printStackTrace();
		}
		String jsonString = result.get("Response");
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>) JsonUtil.getObjectByJson(jsonString,
				"volume_price_list", TypeEnum.arrayList);// 层级递归Map
		for (Map<String, Object> volumePrice : list) {

			List<String> specification = new ArrayList<String>();
			Pattern pattern = Pattern.compile("[0-9,\\.]{1,}");
			Matcher matcher = pattern.matcher((CharSequence) volumePrice.get("number"));// 取出范围
			while (matcher.find()) {
				if (!"".equals(matcher.group()))
					specification.add(matcher.group());
			}
			if (specification.size() > 0)
				volumePrice.put("min", specification.get(0));// 分别取出第一位值，
			if (specification.size() > 1)
				volumePrice.put("max", specification.get(1));// 第二位值
			min = volumePrice.get("min");
			max = volumePrice.get("max");
			// Log.logInfo("取得的最小的件数为："+min+"取得最大的件数为:"+max);
			if (min != null && min.equals("1") && min.equals(number)) { // 对传入的件数进行比较
				goodsNumber = (double) volumePrice.get("org_price");
			} else if (null != min && null != max && Integer.valueOf(min.toString()) <= number
					&& Integer.valueOf(max.toString()) >= number) {
				goodsNumber = Double.parseDouble((String) volumePrice.get("org_price"));
			} else if (null != min && null == max && !min.equals("1") && Integer.valueOf(min.toString()) < number) {
				goodsNumber = Double.parseDouble((String) volumePrice.get("org_price"));
			}

		}

		return goodsNumber;
	}
	
	/**
	 * 
	* @测试点: convertAddress 
	* @验证点: 转换为es地址
	* @param op  op对象
	* @param url 测试环境，正式环境
	* @备注： void    
	* @author zhangjun 
	* @date 2017年5月12日 
	  @修改说明
	 */
	public  void convertAddress(Op op,String url){
		try {
			if (url.contains("www")) {
				op.loopGet(url.replace("www", "es") + "?" + timeStamp, 80, 3, 60);
			} else {
				StringBuffer buffer = new StringBuffer(url);
				String bf = buffer.insert(7, "es.").toString();
				//String Converturl=bf  + timeStamp;
				op.loopGet(bf, 80, 3, 60);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	* @测试点: 返回汇率点 
	* @验证点: 从汇率表中取出特定的汇率点，目前取出的是EUR
	* @使用环境：测试环境，正式环境
	* @param rates  汇率列表地址
 	* @param  return 取出固定的汇率值    
	* @备注： double    
	* @author zhangjun 
	* @date 2017年5月22日 
	  @修改说明
	 */
	public double rates(String rates){
		double rate = 0;
		Map<String, String> result = new HashMap<String, String>();
		result=Pub.get(rates);
		String jsonString = result.get("Response");
		String[] content=jsonString.split(";");
		//主要是为了存取数据
		Map<String, String> m = new HashMap<String, String>();
		for (int i = 0; i < content.length; i++) {
			String getcontte=content[i];
			String[]  gettest=getcontte.split("=");
		   m.put(gettest[0], gettest[1]);
		}
		//遍历map
		Iterator<Entry<String, String>> entries1 = m.entrySet().iterator();   
		while (entries1.hasNext()) {  
		    Entry<String, String> entry = entries1.next();  
		    if(entry.getKey().trim().equals("my_array['EUR']")){
		    	rate=Double.parseDouble(entry.getValue());
		    }
		    	 
		} 
		Log.logInfo("取出的汇率价格为:"+rate);
		return rate ;
	}
	
	
	
	/**
	 * 把json字符串转换成Map
	 * @param str_json 待转换的json字符串
	 * @return 转换的结果Map
	 * @author yuyang 创建时间：2016-09-02 更新时间:2016-12-06
	 * 更新说明(2016-12-06)：by yuyang,由PublicFun移动到Pub
	 */
	public static Map<String, String> json2map(String str_json) {
		Map<String, String> res = null;
		try {
			Gson gson = new Gson();
			res = gson.fromJson(str_json, new TypeToken<Map<String, String>>() {
			}.getType());
		} catch (JsonSyntaxException e) {
			// e.printStackTrace();
		}
		return res;
	}

	
	/**
	* 接口：删除用户的邮箱帐户。如果刪除失败，会抛出异常
	* @param DomainName 域名
	* @param UserEmailAccount 用户的邮箱帐户
	* @throws Exception 抛出全部异常
	* @author zhangjun 创建时间：2016-12-05 更新时间:2016-12-13
	* 更新说明(2016-12-06)：by yuyang,由PublicFun移动到Pub
	* 更新说明(2016-12-13)：by yuyang,增加校验_resultcode以及抛出异常的步骤
	*/
	public static void IF_DelUserAccount2(String DomainName, String UserEmailAccount) throws Exception {
		String URLCommand =  DomainName + "/auto/api.php?mod=user&act=del_auto_account&email=" + UserEmailAccount;
		Map<String, String> res = json2map(Pub.get(URLCommand).get("Response"));
		if (!"200".equals(res.get("_resultcode"))) {
			if ("1001".equals(res.get("_resultcode"))) {
				Log.logInfo("邮箱帐户不存在,不需要删除(" + UserEmailAccount + "),域名(" + DomainName + ").");
			} else {
				throw new Exception("Delete user account(" + UserEmailAccount + ") failed!(" + res + ")(" + DomainName + ")");
			}
		} else {
			Log.logInfo("删除邮箱帐户(" + UserEmailAccount + "),域名(" + DomainName + ").");
		}
	}
	
	/**
	 * 接口：查询并获取用户相关信息，数据结果是一个JSONObject
	 * @param DomainName 域名
	 * @param UserEmailAccount 用户的邮箱帐号
	 * @throws Exception 抛出全部异常
	 * @author yuyang,创建时间：2016-12-13,更新时间：2016-12-13
	 *//*
	public static JSONObject IF_GetUserInfo(String DomainName, String UserEmailAccount) throws Exception {
		String URLCommand = "https://" + DomainName + "/auto/api.php?act=get_user_info&mod=user&email=" + UserEmailAccount;
		//JSONObject res = JSONObject.fromObject(get(URLCommand).get("Response"));
		String res0 = Pub.get(URLCommand).get("Response");
		String res1 = res0.substring(res0.indexOf("{"),res0.lastIndexOf("}")+1);
		JSONObject res = JSONObject.fromObject(res1);
		if ("1001".equals(res.get("_resultcode").toString())) {
			res = null;
		} else if ("200".equals(res.get("_resultcode").toString())) {
		} else {
			throw new Exception("Get user info failed!(" + res.get("_resultcode") + ")(" + DomainName + ")(" + UserEmailAccount + ")");
		}
		return res;
	}*/

	
	/**
	 * 接口：查询用户邮箱帐号是否己注册
	 * @param DomainName 域名
	 * @param UserEmailAccount 用户的邮箱帐号
	 * @return 1-用户邮箱帐号己注册;0-未注册
	 * @author yuyang 创建时间：2016-09-02 更新时间:2016-12-06
	 * 更新说明(2016-12-06)：by yuyang,由PublicFun移动到Pub
	 */
	public static int IF_QueryUserRegisterStatus(String DomainName, String UserEmailAccount) throws Exception {
		String URLCommand =  DomainName + "/auto/api.php?act=check_email&email=" + UserEmailAccount;
		int ret = Integer.valueOf((json2map(Pub.get(URLCommand).get("Response"))).get("status")).intValue();
		if (ret != 0 && ret != 1)
			throw new Exception(
					"Query user register status failed,The user account(" + UserEmailAccount + ") is abnormal!(" + ret + ")(" + DomainName + ")");
		return ret;
	}
	
	/**
	 * 
	* @测试点: 产生一个随机数 
	* @验证点: 给定一个范围，在范围中产生一个随机数，
	  @param @param number  传入一个需要在某范围中，生成的范围数
	  @param @return   返回一个数字的随机数 
	* @备注： int  
	* @author zhangjun 
	* @date 2017年8月5日 
	  @修改说明
	 */
	public static  int randomNumber(int number){
		Random  random=new Random();
		int randomenumber=random.nextInt(number)+1;
		return randomenumber;
	}
	
	/**
	 * 向指定URL发送GET方法的请求
	 * @param url 发送请求的URL
	 * @return Result 所代表远程资源的响应,头信息
	 * @author linchaojiang
	 * 创建时间：2016-09-01 更新时间:2016-12-06
	 * 更新说明(2016-12-06)：by yuyang,由PublicFun移动到Pub
	 * 
	 */
	public static Map<String, String> get(String url) {
		Cookie staging = null;
		Cookie ORIGINDC = null;
		int defaultConnectTimeOut = 30000; // 默认连接超时,毫秒
		int defaultReadTimeOut = 30000; // 默认读取超时,毫秒

		Map<String, String> result = new HashMap<String, String>();
		BufferedReader in = null;

		try {
			Log.logInfo("通过java请求访问：["+url+"]");
			// 打开和URL之间的连接
			URLConnection connection = new URL(url).openConnection();
			// 此处的URLConnection对象实际上是根据URL的请求协议(此处是http)生成的URLConnection类的子类HttpURLConnection
			// 故此处最好将其转化为HttpURLConnection类型的对象,以便用到HttpURLConnection更多的API.
			HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

			// 设置通用的请求属性
			httpURLConnection.setRequestProperty("accept", "*/*");
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			httpURLConnection.setConnectTimeout(defaultConnectTimeOut);
			httpURLConnection.setReadTimeout(defaultReadTimeOut);
			if (staging != null) {
				httpURLConnection.setRequestProperty("Cookie", staging.toString());
			}
			if (ORIGINDC != null) {
				httpURLConnection.setRequestProperty("Cookie", ORIGINDC.toString());
				ORIGINDC = null;
			}

			// // Fidder监听请求
			// if ((!proxyHost.equals("") && !proxyPort.equals(""))) {
			// System.setProperty("http.proxyHost", proxyHost);
			// System.setProperty("http.proxyPort", proxyPort);
			// }

			// 建立连接
			httpURLConnection.connect();
			result = Pub.getResponse(httpURLConnection, in, result);

		} catch (Exception requestException) {
			System.err.println("发送GET请求出现异常!" + requestException);
			// requestException.printStackTrace();
		}
		// 关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception closeException) {
				closeException.printStackTrace();
			}
		}
		
		return result;
	}

	
	
}
