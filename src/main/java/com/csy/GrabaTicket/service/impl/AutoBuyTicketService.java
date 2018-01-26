package com.csy.GrabaTicket.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csy.GrabaTicket.model.AutoBuyTicketModel;
import com.csy.GrabaTicket.model.PreOrderModel;
import com.csy.GrabaTicket.service.IBuyTicketService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unchecked")
@Service
public class AutoBuyTicketService {
	
	@Autowired
	private IBuyTicketService buyTicketService;
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	public Map<String,String> start(AutoBuyTicketModel model){
		Map<String,String> res = new HashMap<>();
		try {
			PreOrderModel preOrderModel = new PreOrderModel();
			BeanUtils.copyProperties(model,preOrderModel);
			Map<String,Object> data = mapper.readValue(buyTicketService.queryZ(preOrderModel), HashMap.class);
			data = (Map<String, Object>) data.get("data");
			List<String> arr = (List<String>)data.get("result");
			if( screeningTrain(model,arr,preOrderModel)) {
				res.put("messageStatus", "success");
				return res;
			} else {
				res.put("messageStatus", "fail");
				return res;
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private boolean screeningTrain(AutoBuyTicketModel model,List<String> arr,PreOrderModel preOrderModel) {
		for(String train : arr) {
			String[] data = train.split("\\|");
			if(data[0].equals(""))
				continue;
			int i = 0;
			for(;i<model.getPeriodTime().length; i++) {
				int hour = Integer.parseInt(data[8].split(":")[0]);
				if( hour >= model.getPeriodTime()[i].getStartTime() && hour <= model.getPeriodTime()[i].getEndTime()) {
					break;
				}
			}
			if(i == model.getPeriodTime().length)
				continue;
			//硬座
			if(!data[29].equals("") && !data[29].equals("无") && model.getTrainDate().contains("1")) {
				try {
					preOrderModel.setStationTrainCode(data[3]);
					preOrderModel.setPassengerTicketStr(model.getPassengerTicketStr().replaceAll("#","1"));
					Map<String,String> res = buyTicketService.preOrder(preOrderModel);
					if(res.get("status").equals("2") && res.get("messageStatus").equals("success")) {
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//软座
			if(!data[24].equals("") && !data[24].equals("无") && model.getTrainDate().contains("2")) {
				try {
					preOrderModel.setStationTrainCode(data[3]);
					preOrderModel.setPassengerTicketStr(model.getPassengerTicketStr().replaceAll("#","2"));
					Map<String,String> res = buyTicketService.preOrder(preOrderModel);
					if(res.get("status").equals("2") && res.get("messageStatus").equals("success")) {
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//硬卧
			if(!data[28].equals("") && !data[28].equals("无") && model.getTrainDate().contains("3")) {
				try {
					preOrderModel.setStationTrainCode(data[3]);
					preOrderModel.setPassengerTicketStr(model.getPassengerTicketStr().replaceAll("#","3"));
					Map<String,String> res = buyTicketService.preOrder(preOrderModel);
					if(res.get("status").equals("2") && res.get("messageStatus").equals("success")) {
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//软卧
			if(!data[23].equals("") && !data[23].equals("无") && model.getTrainDate().contains("5")) {
				try {
					preOrderModel.setStationTrainCode(data[3]);
					preOrderModel.setPassengerTicketStr(model.getPassengerTicketStr().replaceAll("#","5"));
					Map<String,String> res = buyTicketService.preOrder(preOrderModel);
					if(res.get("status").equals("2") && res.get("messageStatus").equals("success")) {
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//高级软卧
			if(!data[21].equals("") && !data[21].equals("无") && model.getTrainDate().contains("6")) {
				try {
					preOrderModel.setStationTrainCode(data[3]);
					preOrderModel.setPassengerTicketStr(model.getPassengerTicketStr().replaceAll("#","6"));
					Map<String,String> res = buyTicketService.preOrder(preOrderModel);
					if(res.get("status").equals("2") && res.get("messageStatus").equals("success")) {
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//二等
			if(!data[30].equals("") && !data[30].equals("无") && model.getTrainDate().contains("O")) {
				try {
					preOrderModel.setStationTrainCode(data[3]);
					preOrderModel.setPassengerTicketStr(model.getPassengerTicketStr().replaceAll("#","O"));
					Map<String,String> res = buyTicketService.preOrder(preOrderModel);
					if(res.get("status").equals("2") && res.get("messageStatus").equals("success")) {
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//一等
			if(!data[31].equals("") && !data[31].equals("无") && model.getTrainDate().contains("M")) {
				try {
					preOrderModel.setStationTrainCode(data[3]);
					preOrderModel.setPassengerTicketStr(model.getPassengerTicketStr().replaceAll("#","M"));
					Map<String,String> res = buyTicketService.preOrder(preOrderModel);
					if(res.get("status").equals("2") && res.get("messageStatus").equals("success")) {
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//特等
			if(!data[32].equals("") && !data[32].equals("无") && model.getTrainDate().contains("P")) {
				try {
					preOrderModel.setStationTrainCode(data[3]);
					preOrderModel.setPassengerTicketStr(model.getPassengerTicketStr().replaceAll("#","P"));
					Map<String,String> res = buyTicketService.preOrder(preOrderModel);
					if(res.get("status").equals("2") && res.get("messageStatus").equals("success")) {
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//动卧
			if(!data[33].equals("") && !data[33].equals("无") && model.getTrainDate().contains("4")) {
				try {
					preOrderModel.setStationTrainCode(data[3]);
					preOrderModel.setPassengerTicketStr(model.getPassengerTicketStr().replaceAll("#","4"));
					Map<String,String> res = buyTicketService.preOrder(preOrderModel);
					if(res.get("status").equals("2") && res.get("messageStatus").equals("success")) {
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		return false;
	}
}
