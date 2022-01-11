package io.dubai.common.oss;


import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 配置管理器
 *
 * @author mother fucker
 */
public final class ConfigManager {

    private final String configJsonPath;
    private JSONObject jsonConfig = null;

    /*
     * 通过一个给定的路径构建一个配置管理器， 该管理器要求地址路径所在目录下必须存在config.properties文件
     */
    public ConfigManager(String configJsonPath) {

        configJsonPath = configJsonPath.replace("\\", "/");

        this.configJsonPath = configJsonPath;
        this.initEnv();

    }

    // 验证配置文件加载是否正确
    public boolean valid() {
        return this.jsonConfig != null;
    }

    public JSONObject getAllConfig() {

        return this.jsonConfig;

    }

    private void initEnv() {
        try {
            Resource resource = new ClassPathResource(configJsonPath);
            InputStream is = resource.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String data = null;
            while ((data = br.readLine()) != null) {
                sb.append(data);
            }

            br.close();
            isr.close();
            is.close();

            JSONObject jsonConfig = JSONObject.parseObject(sb.toString());
            this.jsonConfig = jsonConfig;
        } catch (Exception e) {
            e.printStackTrace();
            this.jsonConfig = null;
        }

    }

}
