package com.csy.GrabaTicket.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csy.GrabaTicket.model.PreOrderModel;
import com.csy.GrabaTicket.service.IBuyTicketService;
import com.csy.GrabaTicket.websocket.request.AutoBuyTicketMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

@SuppressWarnings("unchecked")
@Service
public class AutoBuyTicketService {
	
	@Autowired
	private IBuyTicketService buyTicketService;
	
	private static ExecutorService threadPool = Executors.newFixedThreadPool(20);
	private static ObjectMapper mapper = new ObjectMapper();
	
	public void start(Channel channel,AutoBuyTicketMessageRequest model){
		stopThreadByName(channel.id().toString());
		threadPool.execute(new Runnable() {		
			@Override
			public void run() {
				try {
					Thread.currentThread().setName(channel.id().toString());
					while(Thread.currentThread().getName() != null) {
						PreOrderModel preOrderModel = new PreOrderModel();
						BeanUtils.copyProperties(preOrderModel, model);
						Map<String,Object> data = mapper.readValue(buyTicketService.queryZ(preOrderModel), HashMap.class);
						data = (Map<String, Object>) data.get("data");
						List<String> arr = (List<String>)data.get("result");
						String train = screeningTrain(model,arr);
						if( train != null) {
							preOrderModel.setStationTrainCode(train.split(",")[0]);
							preOrderModel.setPassengerTicketStr(model.getPassengerTicketStr().replaceAll("#", train.split(",")[1]));
							Map<String,String> res = buyTicketService.preOrder(preOrderModel);
							if(res.get("status").equals("1") && res.get("messageStatus").equals("success")) {
								channel.writeAndFlush(new TextWebSocketFrame("{\"messge\":\"success\"}"));
								break;
							}
						}
						Thread.sleep(5000);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void stop(Channel channel){
		stopThreadByName(channel.id().toString());
	}
	
	public void stopThreadByName(String name) {
		ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
		int noThreads = currentGroup.activeCount();
		Thread[] lstThreads = new Thread[noThreads];
		currentGroup.enumerate(lstThreads);
		for (int i = 0; i < noThreads; i++) {
			if(lstThreads[i].getName().equals(name)) {
				lstThreads[i].setName(null);
			}
		}
	}
	
	private String screeningTrain(AutoBuyTicketMessageRequest model,List<String> arr) {
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
			if(!data[29].equals("") && model.getTrainDate().contains("1")) {
				return data[3] + ",1";
			}
			//软座
			if(!data[24].equals("") && model.getTrainDate().contains("2")) {
				return data[3] + ",2";
			}
			//硬卧
			if(!data[28].equals("") && model.getTrainDate().contains("3")) {
				return data[3] + ",3";
			}
			//软卧
			if(!data[23].equals("") && model.getTrainDate().contains("5")) {
				return data[3] + ",5";
			}
			//高级软卧
			if(!data[21].equals("") && model.getTrainDate().contains("6")) {
				return data[3] + ",6";
			}
			//二等
			if(!data[30].equals("") && model.getTrainDate().contains("O")) {
				return data[3] + ",O";
			}
			//一等
			if(!data[31].equals("") && model.getTrainDate().contains("M")) {
				return data[3] + ",M";
			}
			//特等
			if(!data[32].equals("") && model.getTrainDate().contains("P")) {
				return data[3] + ",P";
			}
			//动卧
			if(!data[33].equals("") && model.getTrainDate().contains("4")) {
				return data[3] + ",4";
			}
			
		}
		return null;
	}
}
