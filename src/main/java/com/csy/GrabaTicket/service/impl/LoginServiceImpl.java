package com.csy.GrabaTicket.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csy.GrabaTicket.service.IImageService;
import com.csy.GrabaTicket.service.ILoginService;
import com.csy.GrabaTicket.util.HttpKeepSessionUtil;
import com.csy.GrabaTicket.util.NumberUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LoginServiceImpl implements ILoginService{
	
	private final static String[] imgLocations = {"","32,43","120,43","169,44","240,44","34,117","104,122","180,117","256,121"};
	private final Log log = LogFactory.getLog(LoginServiceImpl.class);
	
	private final static String domain = "kyfw.12306.cn";
	private final static String url1 = "https://kyfw.12306.cn/passport/captcha/captcha-check";
	private final static String url2 = "https://kyfw.12306.cn/passport/web/auth/uamtk";
	private final static String url3 = "https://kyfw.12306.cn/otn/login/init";
	private final static String url4 = "https://kyfw.12306.cn/passport/captcha/captcha-image?login_site=E&module=login&rand=sjrand&0." + (new Random()).nextInt(1000000);
	private final static String url5 = "https://kyfw.12306.cn/passport/web/login";
	private final static String url6 = "https://kyfw.12306.cn/otn/uamauthclient";
	private final static String url7 = "https://kyfw.12306.cn/otn/dynamicJs/${param}";
	private final static String url8 = "https://kyfw.12306.cn/otn/login/loginOut";
	private final static String url9 = "https://kyfw.12306.cn/otn/HttpZF/logdevice?algID=G6OqpQOFB9&hashCode=vnaoGDin6xyyXFMY3tH8s8XowIhp5ITIBxtYPQBcdYM&FMQw=0&q4f3=zh-CN&"
										+ "VPIf=1&custID=133&VEek=unknown&dzuS=0&yD16=0&EOQP=8f58b1186770646318a429cb33977d8c&lEnu=3232235930&jp76=e237f9703f53d448d77c858b634154a5&hAqN=Win32&platform=WEB&"
										+ "ks0Q=b9a555dce60346a48de933b3e16ebd6e&TeRS=1040x1920&tOHY=24xx1080x1920&Fvje=i1l1o1s1&q5aJ=-8&"
										+ "wNLf=99115dfb07133750ba677d055874de87&0aew=Mozilla/5.0%20(Windows%20NT%2010.0;%20WOW64)%20AppleWebKit/537.36%20(KHTML,%20like%20Gecko)%20Chrome/63.0.3239.132%20Safari/537.36&"
										+ "E3gR=5fd7dade1220db783b42e35a2fd353d7&timestamp=" + System.currentTimeMillis();
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private IImageService imageService;
	@Autowired
	private HttpKeepSessionUtil httpService;
	
	@Override
	public boolean checkImg(String imgLocation) {
		try {
			if(NumberUtil.isValid(imgLocation)){
				String imgCoordinates = "";
				for(char i:imgLocation.toCharArray()) {
					imgCoordinates += imgLocations[Integer.parseInt(i+"")] + ",";
				}
				imgCoordinates = imgCoordinates.substring(0, imgCoordinates.length()-1);
				String result = httpService.post(url1, "login_site=E&rand=sjrand&answer="+imgCoordinates);
				log.info(result);
				if(result.indexOf("\"result_code\":\"4\"") > 0) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean login(String username, String password) {
		try {
			String result = httpService.post(url5,"appid=otn&username=" + username + "&password=" + password);
			log.info(result);
			result = httpService.post(url2,"appid=otn");
			log.info(result);
			ObjectMapper mapper = new ObjectMapper();
			Map d = mapper.readValue(result, HashMap.class);
			if(d.get("result_message").equals("验证通过")) {
				result = httpService.post(url6,"tk=" + d.get("newapptk"));
				log.info(result);
				if(result.indexOf("验证通过") > 0) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String init() {
		
		try {
			String res = new String(httpService.get(url3),"UTF-8");
			String param = res.substring(res.indexOf("/otn/dynamicJs/") + 15, res.indexOf("/otn/dynamicJs/") + 15 + 7);
			httpService.get(url7.replace("${param}", param));
			res = httpService.post(url2,"appid=otn");
			log.info(res);
			byte[] d = httpService.get(url4);
			res = new String(httpService.get(url9),"UTF-8");
			log.info(res);
			res = res.replace("callbackFunction('", "").replace(")", "");
			Map<String, String> data = mapper.readValue(res, HashMap.class);
			httpService.addCookie("RAIL_DEVICEID", data.get("dfp"), domain, "");
			httpService.addCookie("RAIL_EXPIRATION", data.get("exp"), domain, "");
			return imageService.upload(d);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void loginOut() {
		try {
			byte[] d = httpService.get(url8);
			log.info(new String(d,"UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
	}

}
