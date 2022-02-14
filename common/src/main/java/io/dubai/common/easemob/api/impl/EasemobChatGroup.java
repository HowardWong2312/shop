package io.dubai.common.easemob.api.impl;

import io.dubai.common.easemob.api.ChatGroupAPI;
import io.dubai.common.easemob.comm.EasemobAPI;
import io.dubai.common.easemob.comm.OrgInfo;
import io.dubai.common.easemob.comm.ResponseHandler;
import io.dubai.common.easemob.comm.TokenUtil;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.StringUtil;
import io.swagger.client.api.GroupsApi;
import io.swagger.client.model.*;
import org.springframework.stereotype.Service;

@Service
public class EasemobChatGroup implements ChatGroupAPI {

    private ResponseHandler responseHandler = new ResponseHandler();
    private GroupsApi api = new GroupsApi();
    @Override
    public Object getChatGroups(final Long limit,final String cursor) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                ApiClient apiClient = new ApiClient();
                apiClient.setBasePath("http://a61.easemob.com");
                api.setApiClient(apiClient);
                return api.orgNameAppNameChatgroupsGet(OrgInfo.ORG_NAME, OrgInfo.APP_NAME_USER, TokenUtil.getAccessToken(),limit+"",cursor);
            }
        });
    }

    @Override
    public Object getChatGroupDetails(final String[] groupIds) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                ApiClient apiClient = new ApiClient();
                apiClient.setBasePath("http://a61.easemob.com");
                api.setApiClient(apiClient);
                return api.orgNameAppNameChatgroupsGroupIdsGet(OrgInfo.ORG_NAME, OrgInfo.APP_NAME_USER, TokenUtil.getAccessToken(), StringUtil.join(groupIds,","));
            }
        });
    }
    @Override
    public Object createChatGroup(final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                ApiClient apiClient = new ApiClient();
                apiClient.setBasePath("http://a61.easemob.com");
                api.setApiClient(apiClient);
                return api.orgNameAppNameChatgroupsPost(OrgInfo.ORG_NAME, OrgInfo.APP_NAME_USER, TokenUtil.getAccessToken(), (Group) payload);
            }
        });
    }

    @Override
    public Object modifyChatGroup(final String groupId,final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                ApiClient apiClient = new ApiClient();
                apiClient.setBasePath("http://a61.easemob.com");
                api.setApiClient(apiClient);
                return api.orgNameAppNameChatgroupsGroupIdPut(OrgInfo.ORG_NAME, OrgInfo.APP_NAME_USER, TokenUtil.getAccessToken(),groupId, (ModifyGroup) payload);
            }
        });
    }

    @Override
    public Object deleteChatGroup(final String groupId) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                ApiClient apiClient = new ApiClient();
                apiClient.setBasePath("http://a61.easemob.com");
                api.setApiClient(apiClient);
                return api.orgNameAppNameChatgroupsGroupIdDelete(OrgInfo.ORG_NAME, OrgInfo.APP_NAME_USER, TokenUtil.getAccessToken(),groupId);
            }
        });
    }

    @Override
    public Object getChatGroupUsers(final String groupId) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                ApiClient apiClient = new ApiClient();
                apiClient.setBasePath("http://a61.easemob.com");
                api.setApiClient(apiClient);
                return api.orgNameAppNameChatgroupsGroupIdUsersGet(OrgInfo.ORG_NAME, OrgInfo.APP_NAME_USER, TokenUtil.getAccessToken(),groupId);
            }
        });
    }

    @Override
    public Object addSingleUserToChatGroup(final String groupId,final String userId) {
        final UserNames userNames = new UserNames();
        UserName userList = new UserName();
        userList.add(userId);
        userNames.usernames(userList);
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                ApiClient apiClient = new ApiClient();
                apiClient.setBasePath("http://a61.easemob.com");
                api.setApiClient(apiClient);
                return api.orgNameAppNameChatgroupsGroupIdUsersPost(OrgInfo.ORG_NAME, OrgInfo.APP_NAME_USER, TokenUtil.getAccessToken(),groupId,userNames);
            }
        });
    }

    @Override
    public Object addBatchUsersToChatGroup(final String groupId,final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                ApiClient apiClient = new ApiClient();
                apiClient.setBasePath("http://a61.easemob.com");
                api.setApiClient(apiClient);
                return api.orgNameAppNameChatgroupsGroupIdUsersPost(OrgInfo.ORG_NAME, OrgInfo.APP_NAME_USER, TokenUtil.getAccessToken(),groupId, (UserNames) payload);
            }
        });
    }

    @Override
    public Object removeSingleUserFromChatGroup(final String groupId,final String userId) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                ApiClient apiClient = new ApiClient();
                apiClient.setBasePath("http://a61.easemob.com");
                api.setApiClient(apiClient);
                return api.orgNameAppNameChatgroupsGroupIdUsersUsernameDelete(OrgInfo.ORG_NAME, OrgInfo.APP_NAME_USER, TokenUtil.getAccessToken(),groupId,userId);
            }
        });
    }

    @Override
    public Object removeBatchUsersFromChatGroup(final String groupId,final String[] userIds) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                ApiClient apiClient = new ApiClient();
                apiClient.setBasePath("http://a61.easemob.com");
                api.setApiClient(apiClient);
                return api.orgNameAppNameChatgroupsGroupIdUsersMembersDelete(OrgInfo.ORG_NAME, OrgInfo.APP_NAME_USER, TokenUtil.getAccessToken(),groupId, StringUtil.join(userIds,","));
            }
        });
    }

    @Override
    public Object transferChatGroupOwner(final String groupId,final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                ApiClient apiClient = new ApiClient();
                apiClient.setBasePath("http://a61.easemob.com");
                api.setApiClient(apiClient);
                return api.orgNameAppNameChatgroupsGroupidPut(OrgInfo.ORG_NAME, OrgInfo.APP_NAME_USER, TokenUtil.getAccessToken(),groupId, (NewOwner) payload);
            }
        });
    }

    @Override
    public Object getChatGroupBlockUsers(final String groupId) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                ApiClient apiClient = new ApiClient();
                apiClient.setBasePath("http://a61.easemob.com");
                api.setApiClient(apiClient);
                return api.orgNameAppNameChatgroupsGroupIdBlocksUsersGet(OrgInfo.ORG_NAME, OrgInfo.APP_NAME_USER, TokenUtil.getAccessToken(),groupId);
            }
        });
    }

    @Override
    public Object addSingleBlockUserToChatGroup(final String groupId,final String userId) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                ApiClient apiClient = new ApiClient();
                apiClient.setBasePath("http://a61.easemob.com");
                api.setApiClient(apiClient);
                return api.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernamePost(OrgInfo.ORG_NAME, OrgInfo.APP_NAME_USER, TokenUtil.getAccessToken(),groupId,userId);
            }
        });
    }

    @Override
    public Object addBatchBlockUsersToChatGroup(final String groupId,final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                ApiClient apiClient = new ApiClient();
                apiClient.setBasePath("http://a61.easemob.com");
                api.setApiClient(apiClient);
                return api.orgNameAppNameChatgroupsGroupIdBlocksUsersPost(OrgInfo.ORG_NAME, OrgInfo.APP_NAME_USER, TokenUtil.getAccessToken(),groupId, (UserNames) payload);
            }
        });
    }

    @Override
    public Object removeSingleBlockUserFromChatGroup(final String groupId,final String userId) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                ApiClient apiClient = new ApiClient();
                apiClient.setBasePath("http://a61.easemob.com");
                api.setApiClient(apiClient);
                return api.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernameDelete(OrgInfo.ORG_NAME, OrgInfo.APP_NAME_USER, TokenUtil.getAccessToken(),groupId,userId);
            }
        });
    }

    @Override
    public Object removeBatchBlockUsersFromChatGroup(final String groupId,final String[] userIds) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                ApiClient apiClient = new ApiClient();
                apiClient.setBasePath("http://a61.easemob.com");
                api.setApiClient(apiClient);
                return api.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernamesDelete(OrgInfo.ORG_NAME, OrgInfo.APP_NAME_USER, TokenUtil.getAccessToken(),groupId, StringUtil.join(userIds,","));
            }
        });
    }
}
