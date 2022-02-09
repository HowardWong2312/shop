package io.dubai.common.easemob.api.impl;

import io.dubai.common.easemob.api.IMessageService;
import io.dubai.common.easemob.api.SendMessageAPI;
import io.swagger.client.model.Msg;
import io.swagger.client.model.UserName;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @copyright {@link 9iu.org}
 * @author springrain<Auto generate>
 * @version  2018-06-19 10:39:29
 */
@Service("messageService")
public class MessageServiceImpl implements IMessageService {

	@Resource
	private SendMessageAPI sendMessageAPI;

	public void push(String content,List<String> users,String type,String id,Integer categoryId)throws Exception {
		ThreadMessageClazz clazz = new ThreadMessageClazz(content,users,type,id,categoryId,null);
		clazz.start();

		return;
	}

	public void cmdPush(String content,List<String> users,List<Integer> userIds,String conversationId,String userName,String userHeader)throws Exception {
		ThreadCMDMessageClazz clazz = new ThreadCMDMessageClazz(content,users,conversationId,userName,userHeader);
		clazz.start();

		return;
	}


	/**
	 * 一个多线程的内部类，用来执行大批指定用户的推送
	 */

	private  class ThreadMessageClazz extends  Thread{
		public ThreadMessageClazz(String content,List<String> imIdentifiers,String type,String id,Integer categoryId,Integer homeType) {
			super();
			this.content= content ;
			this.imIdentifiers  = imIdentifiers ;
			this.type  = type ;
			this.id  = id ;
			this.categoryId  = categoryId ;
			this.homeType  = homeType ;
		}

		public List<String> imIdentifiers;
		public String content ;
		public String type ;
		public String id ;
		public Integer categoryId;
		public Integer homeType;

		public void run(){
			try {


				AtomicInteger flag = new AtomicInteger(0);


				Msg msg = new Msg();
				UserName target = new UserName();
				JSONObject msgJson = new JSONObject();

				msgJson.put("type", "txt");
				msgJson.put("msg", content);
				JSONObject extJson = new JSONObject();
				extJson.put("action",  "action_official");
				extJson.put("type", type);
				extJson.put("mutable-content",1);
				if(null !=id){
					extJson.put("id",id);
				}
				if(null !=categoryId){
					extJson.put("categoryId",categoryId);
				}
				if(null !=homeType){
					extJson.put("homeType",homeType);
				}
				JSONObject emapnsJson = new JSONObject();
				emapnsJson.put("type", type);
				if(null !=id){
					emapnsJson.put("id",id);
				}
				if(null !=categoryId){
					emapnsJson.put("categoryId",categoryId);
				}
				//emapnsJson.put("mutable-content",1);
				extJson.put("em_apns_ext",emapnsJson);
				msg.targetType("users").msg(msgJson).ext(extJson);


				for (int i = 0,size =imIdentifiers.size() ; i < size; i++) {
					// 控制次数，环信文档限流30次/秒，每次目标人数不超过20个，所以以600人为一大组，达到后休息1秒（因为有可能每个大组的发送达不到1秒，又不清楚具体会多长时间，就干脆休息1秒）
					if (i%600 == 0 && i!=0) {
						Thread.sleep(1000);
					}
					// 增加到target组中，用于下方发送消息
					target.add(flag.intValue(), imIdentifiers.get(i));

					// 分割成每组20个，满足20个时才发送一次，环信文档要求每组目标人员不超过20个
					int flagAdd = flag.incrementAndGet();
					if (flagAdd == 20 || (size -i)<20) {
						msg.target(target);
						sendMessageAPI.sendMessage(msg);
						target = new UserName();
						flag.set(0);
						continue;
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private  class ThreadCMDMessageClazz extends  Thread{
		public ThreadCMDMessageClazz(String content,List<String> imIdentifiers,String conversationId,String userName,String userHeader) {
			super();
			this.content= content ;
			this.imIdentifiers  = imIdentifiers ;
			this.conversationId  = conversationId ;
			this.userName  = userName ;
			this.userHeader  = userHeader ;
		}

		public List<String> imIdentifiers;
		public String content ;
		public String conversationId ;
		public String userName ;
		public String userHeader;

		public void run(){
			try {


				AtomicInteger flag = new AtomicInteger(0);


				Msg msg = new Msg();
				UserName target = new UserName();
				JSONObject msgJson = new JSONObject();

				msgJson.put("type", "cmd");
				msgJson.put("msg", content);
				JSONObject extJson = new JSONObject();
				extJson.put("action",  "action_official");
				extJson.put("type", "friend_apply_agree_book");
				if(StringUtils.isNotEmpty(conversationId)){
					extJson.put("conversationId", conversationId);
				}
				if(StringUtils.isNotEmpty(userName)){
					extJson.put("userName", userName);
				}
				if(StringUtils.isNotEmpty(userHeader)){
					extJson.put("userHeader", userHeader);
				}
				JSONObject emapnsJson = new JSONObject();
				emapnsJson.put("type", "friend_apply_agree_book");
				if(StringUtils.isNotEmpty(conversationId)){
					emapnsJson.put("conversationId", conversationId);
				}
				if(StringUtils.isNotEmpty(userName)){
					emapnsJson.put("userName", userName);
				}
				if(StringUtils.isNotEmpty(userHeader)){
					emapnsJson.put("userHeader", userHeader);
				}
				emapnsJson.put("em_apns_ext",emapnsJson);
				msg.targetType("users").msg(msgJson).ext(extJson);


				for (int i = 0,size =imIdentifiers.size() ; i < size; i++) {
					// 控制次数，环信文档限流30次/秒，每次目标人数不超过20个，所以以600人为一大组，达到后休息1秒（因为有可能每个大组的发送达不到1秒，又不清楚具体会多长时间，就干脆休息1秒）
					if (i%600 == 0 && i!=0) {
						Thread.sleep(1000);
					}
					// 增加到target组中，用于下方发送消息
					target.add(flag.intValue(), imIdentifiers.get(i));

					// 分割成每组20个，满足20个时才发送一次，环信文档要求每组目标人员不超过20个
					int flagAdd = flag.incrementAndGet();
					if (flagAdd == 20 || (size -i)<20) {
						msg.target(target);
						sendMessageAPI.sendMessage(msg);
						target = new UserName();
						flag.set(0);
						continue;
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public  void pushSingleMsg(String content,String user,String type,Integer id)throws Exception {
		try {
			Msg msg = new Msg();
			UserName target = new UserName();
			JSONObject msgJson = new JSONObject();
			msgJson.put("type", "txt");
			msgJson.put("msg", content);
			JSONObject extJson = new JSONObject();
			extJson.put("action",  "action_official");
			extJson.put("type", type);
			extJson.put("id",id);
			msg.targetType("users").msg(msgJson).ext(extJson);
			target.add(0, user);
			msg.target(target);
			Object object = sendMessageAPI.sendMessage(msg);
			System.out.println(object.toString());
		}catch (Exception e){
			e.printStackTrace();
		}

		return;
	}










}
