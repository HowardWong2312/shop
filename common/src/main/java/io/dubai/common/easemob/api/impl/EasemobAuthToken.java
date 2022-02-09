package io.dubai.common.easemob.api.impl;


import io.dubai.common.easemob.api.AuthTokenAPI;
import io.dubai.common.easemob.comm.TokenUtil;
import org.springframework.stereotype.Service;

@Service
public class EasemobAuthToken implements AuthTokenAPI {

	@Override
	public Object getAuthToken(){
		return TokenUtil.getAccessToken();
	}
}
