package io.dubai.common.sys.service.impl;

import io.dubai.common.easemob.EasemobAPI;
import io.dubai.common.easemob.OrgInfo;
import io.dubai.common.easemob.ResponseHandler;
import io.dubai.common.easemob.TokenUtil;
import io.dubai.common.sys.service.SendMessageAPI;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.MessagesApi;
import io.swagger.client.model.Msg;
import org.springframework.stereotype.Service;

@Service
public class EasemobSendMessage implements SendMessageAPI {

    private ResponseHandler responseHandler = new ResponseHandler();
    private MessagesApi api = new MessagesApi();
    @Override
    public Object sendMessage(final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                ApiClient apiClient = new ApiClient();
                apiClient.setBasePath("http://a1-sgp.easemob.com");
                api.setApiClient(apiClient);
                return api.orgNameAppNameMessagesPost(OrgInfo.ORG_NAME, OrgInfo.APP_NAME_USER, TokenUtil.getAccessToken(), (Msg) payload);
            }
        });
    }
}
