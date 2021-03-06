package io.dubai.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created bymother fucker on 2017/11/6
 */
public class HttpUtils extends Thread{

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.isNotBlank(ip)) {
            ip = ip.split(",")[0];
        }
        return ip;

    }


    public JSONObject getConnection(URL url) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.connect();

        JSONObject jsono = JSONObject.parseObject(IOUtils.toString(connection.getInputStream()));
        connection.disconnect();
        return jsono;
    }

    public static String getJson(String url, String authorization) {
        String result = null;
        try {
            // ????????????????????????
            HttpGet request = new HttpGet(url);
            request.addHeader("Content-type","application/json");
            // ???????????????????????????
            HttpClient httpClient = new DefaultHttpClient();
            if (authorization != null) {
                request.setHeader("Authorization", "Basic " + authorization);
            }
            // ????????????????????????????????????
            HttpResponse response = httpClient.execute(request);
            // ???????????????????????????????????????(0--200????????????)
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }


    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) throws Exception {
        JSONObject jsonObject = null;
        // ??????SSLContext?????????????????????????????????????????????????????????
        TrustManager[] tm = {new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};

        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, tm, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
        // ?????????SSLContext???????????????SSLSocketFactory??????
        SSLSocketFactory ssf = sslContext.getSocketFactory();

        URL url = new URL(requestUrl);
        HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
        httpUrlConn.setSSLSocketFactory(ssf);
        httpUrlConn.setReadTimeout(10000);
        httpUrlConn.setConnectTimeout(12000);
        httpUrlConn.setDoOutput(true);
        httpUrlConn.setDoInput(true);
        httpUrlConn.setUseCaches(false);
        //httpUrlConn.setRequestProperty("User-Agent:","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        // ?????????????????????GET/POST???
        httpUrlConn.setRequestMethod(requestMethod);

        if ("GET".equalsIgnoreCase(requestMethod)) {
            httpUrlConn.connect();
        }

        // ???????????????????????????
        if (null != outputStr) {
            OutputStream outputStream = httpUrlConn.getOutputStream();
            // ???????????????????????????????????????
            outputStream.write(outputStr.getBytes("UTF-8"));
            outputStream.close();
        }

        // ???????????????????????????????????????
        String buffer = IOUtils.toString(httpUrlConn.getInputStream());
        httpUrlConn.disconnect();
        jsonObject = JSONObject.parseObject(buffer.toString());
        return jsonObject;
    }

    public static String returnFeeSSLPost(SSLConnectionSocketFactory sslFactory, String url, String data) throws Exception {

        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslFactory).build();
        try {
            HttpPost httpost = new HttpPost(url);
            StringEntity myEntity = new StringEntity(data, ContentType.APPLICATION_JSON);// ??????????????????
            httpost.setEntity(myEntity);// ???????????????

            CloseableHttpResponse response = httpclient.execute(httpost);
            System.out.println("response:" + response);
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));

            try {
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    String jsonStr = EntityUtils.toString(entity, "UTF-8");
                    EntityUtils.consume(entity);
                    return jsonStr;
                }
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return "";
    }

    public static String getFullPath(HttpServletRequest request) {
        String basePath = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if (StringUtils.isNotEmpty(queryString)) {
            queryString = "?" + queryString;
        } else {
            queryString = "";
        }

        return basePath + queryString;
    }

    public static void getMedia(String url, String mediaId, String savePath) {

        HttpURLConnection http = null;
        try {
            URL urlGet = new URL(url);
            http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // ?????????get????????????
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// ????????????30???
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // ????????????30???
            http.connect();
            File file = new File(savePath, mediaId + ".jpg");
            FileUtils.copyInputStreamToFile(http.getInputStream(), file);
            System.out.println("getMedia:url = " + url);
            System.out.println("getMedia:file = " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (http != null)
                http.disconnect();
        }
    }

    public static String sendPostJson(String sendUrl, String authorization, String data) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(sendUrl);
        if (authorization != null) {
            post.setHeader("Authorization", "Basic " + authorization);
        }
        StringEntity myEntity = new StringEntity(data, ContentType.APPLICATION_JSON);// ??????????????????
        post.setEntity(myEntity);// ???????????????
        CloseableHttpResponse response = null;
        try {
            response = client.execute(post);
            if (response.getEntity() != null) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, "UTF-8");
            }
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (client != null)
                        client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String sendPostXml(String sendUrl, String data) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(sendUrl);
        StringEntity myEntity = new StringEntity(data, ContentType.APPLICATION_XML);// ??????????????????
        post.setEntity(myEntity);// ???????????????
        CloseableHttpResponse response = null;
        try {
            response = client.execute(post);
            if (response.getEntity() != null) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, "UTF-8");
            }
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (client != null)
                        client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public static String sendPostData(String url, String authorization, Map<String, String> data) {
        try {
            // ??????HttpClientBuilder
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            // HttpClient
            CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
            // ????????????
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.3; en-us; SM-N900T Build/JSS15J) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
            if (authorization != null) {
                httpPost.setHeader("Authorization", "Basic " + authorization);
            }
            // ??????????????????
            List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                paramsList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));//??????
            }
            UrlEncodedFormEntity entity;
            entity = new UrlEncodedFormEntity(paramsList, "UTF-8");
            httpPost.setEntity(entity);
            CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            int code = response.getStatusLine().getStatusCode();
            if (code == 302) {
                Header header = response.getFirstHeader("location");
                String newUrl = header.getValue();
                return newUrl;
            }
            if (httpEntity != null) {
                return EntityUtils.toString(httpEntity, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static String sendPostData(String url, Map<String, String> data) {
        try {
            // ??????HttpClientBuilder
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            // HttpClient
            CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
            // ????????????
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.3; en-us; SM-N900T Build/JSS15J) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
            // ??????????????????
            List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                paramsList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));//??????
            }
            UrlEncodedFormEntity entity;
            entity = new UrlEncodedFormEntity(paramsList, "UTF-8");
            httpPost.setEntity(entity);
            CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            int code = response.getStatusLine().getStatusCode();
            if (code == 302) {
                Header header = response.getFirstHeader("location");
                String newUrl = header.getValue();
                return newUrl;
            }
            if (httpEntity != null) {
                return EntityUtils.toString(httpEntity, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static String sendPostData(String url, String data) {
        try {
            // ??????HttpClientBuilder
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            // HttpClient
            CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
            // ????????????
            HttpPost httpPost = new HttpPost(url);
            // ??????????????????
            List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
            paramsList.add(new BasicNameValuePair("data", data));//??????
            UrlEncodedFormEntity entity;
            entity = new UrlEncodedFormEntity(paramsList, "UTF-8");
            httpPost.setEntity(entity);
            CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
            // getEntity()
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                return EntityUtils.toString(httpEntity, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static HttpClient getSSLInsecureClient() throws Exception {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        return HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
    }

}
