package io.dubai.common.easemob;

import com.google.gson.Gson;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.AuthenticationApi;
import io.swagger.client.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by easemob on 2017/3/14.
 */
public class TokenUtil {
    public static String GRANT_TYPE;
    private static String CLIENT_ID_USER;
    private static String CLIENT_SECRET_USER;
    private static Token BODY;
    private static AuthenticationApi API = new AuthenticationApi();
    private static String ACCESS_TOKEN_USER;
    private static String ACCESS_TOKEN_DOC;
    private static Double EXPIREDAT_USER = -1D;
    private static Double EXPIREDAT_DOC = -1D;
    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    /**
     * get token from server
     */
    static {
        InputStream inputStream = TokenUtil.class.getClassLoader().getResourceAsStream("config.properties");
        Properties prop = new Properties();
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        GRANT_TYPE = prop.getProperty("GRANT_TYPE");
        CLIENT_ID_USER = prop.getProperty("CLIENT_ID_USER");
        CLIENT_SECRET_USER = prop.getProperty("CLIENT_SECRET_USER");
        BODY = new Token().clientId(CLIENT_ID_USER).grantType(GRANT_TYPE).clientSecret(CLIENT_SECRET_USER);
    }

    public static void initTokenByProp() {
        String resp = null;
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath("http://a1-sgp.easemob.com");
        try {
            API.setApiClient(apiClient);
            resp = API.orgNameAppNameTokenPost(OrgInfo.ORG_NAME, OrgInfo.APP_NAME_USER, BODY);
        } catch (ApiException e) {
            logger.error(e.getMessage());
        }
        Gson gson = new Gson();
        Map map = gson.fromJson(resp, Map.class);
        ACCESS_TOKEN_USER = " Bearer " + map.get("access_token");
        EXPIREDAT_USER = System.currentTimeMillis() + (Double) map.get("expires_in");
    }


    /**
     * get Token from memory
     *
     * @return
     */
    public static String getAccessToken() {
        if (ACCESS_TOKEN_USER == null || isExpired()) {
            initTokenByProp();
        }
        return ACCESS_TOKEN_USER;
    }


    private static Boolean isExpired() {
        return System.currentTimeMillis() > EXPIREDAT_USER;
    }
    private static Boolean isExpired_doc() {
        return System.currentTimeMillis() > EXPIREDAT_DOC;
    }

}

