package com.csy.GrabaTicket.service.impl;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csy.GrabaTicket.model.ConfirmOrderModel;
import com.csy.GrabaTicket.model.PreOrderModel;
import com.csy.GrabaTicket.service.IBuyTicketService;
import com.csy.GrabaTicket.service.IImageService;
import com.csy.GrabaTicket.util.HttpKeepSessionUtil;
import com.csy.GrabaTicket.util.NumberUtil;
import com.csy.GrabaTicket.util.StationCodeUtil;
import com.csy.GrabaTicket.util.StationCodeUtil.StationCode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("rawtypes")
@Service
public class BuyTicketServiceImpl implements IBuyTicketService {
	
	private final static String domain = "kyfw.12306.cn";
	
	private final static String url1 = "https://kyfw.12306.cn/otn/leftTicket/init";
	private final static String url2 = "https://kyfw.12306.cn/otn/leftTicket/queryZ?leftTicketDTO.train_date=${trainDate}&leftTicketDTO.from_station=${from}&leftTicketDTO.to_station=${to}&purpose_codes=ADULT";
	private final static String url3 = "https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest";
	private final static String url4 = "https://kyfw.12306.cn/otn/confirmPassenger/initDc";
//	private final static String url5 = "https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs";
	private final static String url6 = "https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo";
	private final static String url7 = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=passenger&rand=randp&0." + (new Random()).nextInt(1000000);
	private final static String url8 = "https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue";
	private final static String url9 = "https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime?random=${stampTime}&tourFlag=dc&_json_att=&REPEAT_SUBMIT_TOKEN=${REPEAT_SUBMIT_TOKEN}";
	private final static String url10 = "https://kyfw.12306.cn/otn/confirmPassenger/resultOrderForDcQueue";
	private final static String url11 ="https://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn1";
	private final static String url12 ="https://kyfw.12306.cn/otn/passengers/query";
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	private final static String[] imgLocations = {"","32,43","120,43","169,44","240,44","34,117","104,122","180,117","256,121"};
	private final Log log = LogFactory.getLog(BuyTicketServiceImpl.class);
	
	private final static Map<String,StationCode> stationCodes = StationCodeUtil.getData();
	
	@Autowired
	private IImageService imageService;
	@Autowired
	private HttpKeepSessionUtil httpService;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,String> preOrder(PreOrderModel preOrderModel) {
		
		try {
			Map<String,Object> data = mapper.readValue(queryZ(preOrderModel), HashMap.class);
			data = (Map<String, Object>) data.get("data");
			List<String> arr = (List<String>)data.get("result");
			for(String train : arr) {
				String[] t = train.split("\\|");
				if(t[3].equals(preOrderModel.getStationTrainCode())) {
					if(t[0] == null || t[0].equals("")) {
						throw new RuntimeException("没有票了");
					}
					String res = sendSubmitOrderRequest(t,preOrderModel);
					if(res.indexOf("200") > 0) {
						res = httpService.post(url4, "_json_att=");
						log.info(res);
						Map<String,String> cachData = new HashMap<>();
						getInitDcData(res,cachData);
						getQueryZData(t,cachData);
						
						String path = getImage();
						res = sendCheckOrderInfo(cachData,preOrderModel);
						if(checkDisplayImg(res)) {
							cachData.put("image", path);
							//1代表还有验证图片
							cachData.put("status", "1");
							cachData.put("messageStatus", "success");
							return cachData;
						} else {
							ConfirmOrderModel confirmOrderModel = new ConfirmOrderModel();
							confirmOrderModel.setPassengerTicketStr(preOrderModel.getPassengerTicketStr());
							confirmOrderModel.setOldPassengerStr(preOrderModel.getOldPassengerStr());
							confirmOrderModel.setRepeatSubmitToken(cachData.get("REPEAT_SUBMIT_TOKEN"));
							confirmOrderModel.setTrainLocation(cachData.get("train_location"));
							confirmOrderModel.setKeyCheckIsChange(cachData.get("keyCheckIsChange"));
							confirmOrderModel.setLeftTicketStr(cachData.get("leftTicketStr"));
							if(confirmOrder(confirmOrderModel).get("messageStatus").equals("success")) {
								cachData.put("status", "2");
								cachData.put("messageStatus", "success");
							} else {
								cachData.put("status", "2");
								cachData.put("messageStatus", "fail");
							}
						}
						return cachData;
					} else {
						throw new RuntimeException("重新提交");
					}
				}
			}
			throw new RuntimeException("没找到" + preOrderModel.getStationTrainCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public Map<String,String> confirmOrder(ConfirmOrderModel confirmOrderModel) {
		Map<String,String> resultData = new HashMap<>();
		try {
			String res = sendConfirmSingleForQueue(confirmOrderModel);
			if(res.equals("")) {
				throw new RuntimeException("请重新请求");
			}
			Map data = mapper.readValue(res, HashMap.class);
			if(!(Boolean)data.get("status")) {
				throw new RuntimeException(data.get("data").toString());
			}
			data = (Map)(data.get("data"));
			if((Boolean)data.get("submitStatus")){
				String orderId = sendQueryOrderWaitTime(confirmOrderModel);
				if(orderId == null) {
					resultData.put("messageStatus", "fail");
					resultData.put("message", "没抢到");
				} else {
					if(resultOrderForDcQueue(orderId,confirmOrderModel.getRepeatSubmitToken())) {
						resultData.put("messageStatus", "success");
					} else {
						resultData.put("messageStatus", "fail");
						resultData.put("message", "没抢到");
					}
				}
				return resultData;
			} else {
				throw new RuntimeException(data.get("errMsg").toString());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public boolean checkImage(String imgLocation, String repeatSubmitToken) {
		try {
			if(NumberUtil.isValid(imgLocation)){
				String imgCoordinates = "";
				for(char i:imgLocation.toCharArray()) {
					imgCoordinates += imgLocations[Integer.parseInt(i+"")] + ",";
				}
				String params = "&randCode=" +  imgCoordinates +
						"&_json_att=&rand=randp" + 
						"&REPEAT_SUBMIT_TOKEN=" + repeatSubmitToken ;
				String res = httpService.post(url11, params);
				if(res.indexOf("FALSE") > 0) {
					return false;
				} else {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return false;
	}

	@Override
	public List<String> listUser() {
		List<String> users = new ArrayList<>();
		try {
			String res = httpService.post(url12, "pageIndex=1&pageSize=1000");
			if(res.equals("")) {
				return users;
			}
			List datas = (List)((Map)(mapper.readValue(res, HashMap.class).get("data"))).get("datas");
			for(Object o : datas) {
				String user = "";
				Map m = (Map)o;
				user = user+m.get("passenger_name") +
						"," + m.get("passenger_type") +
						"," + m.get("passenger_id_no") +
						"," + m.get("passenger_id_type_code");
				users.add(user);
			}
			return users;
		} catch (Exception e) {
			e.printStackTrace();
			return users;
		}
		
	}

	@Override
	public String getImage() {
		byte[] imgData;
		try {
			imgData = httpService.get(url7);
			String path = imageService.upload(imgData);
			return path;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	@Override
	public String queryZ(PreOrderModel preOrderModel) {
		try {
			if(stationCodes.get(preOrderModel.getFromCity()) == null) {
				throw new RuntimeException("出发城市不存在");
			}
			if(stationCodes.get(preOrderModel.getToCity()) == null) {
				throw new RuntimeException("目标城市不存在");
			}
			httpService.get(url1);
			byte[] result = httpService.get(url2.replace("${trainDate}", preOrderModel.getTrainDate())
										.replace("${from}", stationCodes.get(preOrderModel.getFromCity()).getCode())
										.replace("${to}", stationCodes.get(preOrderModel.getToCity()).getCode()));
			addCookieByQueryZ(preOrderModel, stationCodes);
			String res = new String(result,"UTF-8"); 
			log.info(res);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private String sendQueryOrderWaitTime(ConfirmOrderModel confirmOrderModel) {
		try {
			int i = 10;
			while(i>0) {
				i--;
				byte[] b = httpService.get(url9.replace("${stampTime}", System.currentTimeMillis()/1000 + "")
																			.replace("${REPEAT_SUBMIT_TOKEN}", confirmOrderModel.getRepeatSubmitToken())); 
				String res = new String(b,"UTF-8");
				log.info(res);
				Map data = mapper.readValue(res, HashMap.class);
				data = (Map)data.get("data");
				if(data.get("orderId") != null) {
					return data.get("orderId").toString();
				} else {
					if(data.get("msg") != null) {
						throw new RuntimeException(data.get("msg").toString());
					}
				}
				Thread.sleep(3000);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private boolean resultOrderForDcQueue(String orderId,String repeatSubmitToken) {
		try {
			int i = 3;
			while(i>0) {
				i--;
				String params = "&orderSequence_no=" +  orderId +
								"&_json_att=" + 
								"&REPEAT_SUBMIT_TOKEN=" + repeatSubmitToken ;
				String res = httpService.post(url10, params);
				log.info(res);
				Map data = (Map)(mapper.readValue(res, HashMap.class).get("data"));
				if((Boolean)data.get("submitStatus")){
					return true;
				}
				Thread.sleep(3000);
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private String sendConfirmSingleForQueue(ConfirmOrderModel confirmOrderModel) {
		try {
			String params = "randCode=&purpose_codes=00" + 
					"&passengerTicketStr=" +  confirmOrderModel.getPassengerTicketStr() +
					"&oldPassengerStr=" + confirmOrderModel.getOldPassengerStr() + 
					"&choose_seats=&seatDetailType=000&whatsSelect=1&_json_att=&roomType=00" + 
					"&dwAll=N" +
					"&REPEAT_SUBMIT_TOKEN=" + confirmOrderModel.getRepeatSubmitToken() + 
					"&train_location=" + confirmOrderModel.getTrainLocation() +
					"&key_check_isChange=" + confirmOrderModel.getKeyCheckIsChange() +
					"&leftTicketStr=" + confirmOrderModel.getLeftTicketStr();
			String res = httpService.post(url8, params);
			log.info(res);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private String sendSubmitOrderRequest(String[] t,PreOrderModel preOrderModel) {
		try {
			String params = "secretStr=" + t[0] + "&train_date=" + preOrderModel.getTrainDate() +
					"&back_train_date=" +  (new SimpleDateFormat("yyyy-MM-dd")).format(new Date()) +
					"&tour_flag=dc&purpose_codes=ADULT" + 
					"&query_from_station_name=" + preOrderModel.getFromCity() +
					"&query_to_station_name=" + preOrderModel.getToCity() +"&undefined";
			String res = httpService.post(url3, params);
			log.info(res);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private String sendCheckOrderInfo(Map<String,String> cachData,PreOrderModel preOrderModel) {
		try {
			String params = "cancel_flag=2&bed_level_order_num=000000000000000000000000000000" + 
					"&passengerTicketStr=" +  preOrderModel.getPassengerTicketStr() +
					"&oldPassengerStr=" + preOrderModel.getOldPassengerStr() + 
					"&tour_flag=dc&randCode=&whatsSelect=1&_json_att=" + 
					"&REPEAT_SUBMIT_TOKEN=" + cachData.get("globalRepeatSubmitToken");
			String res = httpService.post(url6, params);
			log.info(res);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean checkDisplayImg(String res) {
		try {
			Map m = mapper.readValue(res,HashMap.class);
			if(!(Boolean)m.get("status")) {
				throw new RuntimeException(m.get("messages").toString());
			} else {
				m = (Map<String,Object>)m.get("data");
				if(!(Boolean)m.get("submitStatus")) {
					throw new RuntimeException(m.get("errMsg").toString());
				}
				if(m.get("ifShowPassCode").equals("Y")) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} 
		return false;
	}
	
	private void getQueryZData(String[] arr,Map<String,String> data){
		data.put("train_no",arr[2]);
		data.put("fromStationTelecode",arr[6]); 
		data.put("toStationTelecode",arr[7]);
		data.put("leftTicketStr",arr[12]);
		data.put("train_date",arr[13]);
		data.put("train_location",arr[15]);
	}
	
	private void getInitDcData(String data,Map<String,String> d){
		d.put("REPEAT_SUBMIT_TOKEN",data.substring(data.indexOf("globalRepeatSubmitToken = '")+27, data.indexOf("globalRepeatSubmitToken = '")+32+27));
		d.put("keyCheckIsChange",data.substring(data.indexOf("key_check_isChange':'")+21, data.indexOf("key_check_isChange':'")+21+56));
	}
	
	private void addCookieByQueryZ(PreOrderModel preOrderModel,Map<String,StationCode> data) {
		try {
			httpService.addCookie("_jc_save_toDate", (new SimpleDateFormat("yyyy-MM-dd")).format(new Date()), domain, "");
			httpService.addCookie("_jc_save_fromDate", preOrderModel.getTrainDate(), domain, "");
			httpService.addCookie("_jc_save_wfdc_flag", "dc", domain, "");
			httpService.addCookie("_jc_save_toStation", URLEncoder.encode(preOrderModel.getToCity() + "," + data.get(preOrderModel.getToCity()).getCode(),"UTF-8"), domain, "");
			httpService.addCookie("_jc_save_fromStation", URLEncoder.encode(preOrderModel.getFromCity() + "," + data.get(preOrderModel.getFromCity()).getCode(),"UTF-8"), domain, "");
			httpService.addCookie("_jc_save_showIns", "true", domain, "");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

}
