package io.dubai.common.easemob.api;



import java.util.List;

/**
 * @copyright {@link 9iu.org}
 * @author springrain<Auto generate>
 * @version  2018-06-19 10:39:29
 */
public interface IMessageService {
	
	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 * @throws Exception
	 */
	void push(String content,List<String> users,String type,String id,Integer categoryId)throws Exception ;

	void pushSingleMsg(String content,String user,String type,Integer id)throws Exception;

	void cmdPush(String content,List<String> users,List<Integer> userIds,String conversationId,String userName,String userHeader)throws Exception ;


}
