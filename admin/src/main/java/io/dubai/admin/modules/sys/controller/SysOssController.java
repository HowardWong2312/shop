package io.dubai.admin.modules.sys.controller;

import com.google.gson.Gson;
import io.dubai.admin.modules.sys.entity.SysOssEntity;
import io.dubai.admin.modules.sys.service.SysOssService;
import io.dubai.common.exception.RRException;
import io.dubai.common.group.AliyunGroup;
import io.dubai.common.group.QcloudGroup;
import io.dubai.common.group.QiniuGroup;
import io.dubai.common.oss.CloudStorageConfig;
import io.dubai.common.oss.ConfigManager;
import io.dubai.common.oss.OSSFactory;
import io.dubai.common.sys.service.SysConfigService;
import io.dubai.common.utils.ConfigConstant;
import io.dubai.common.utils.Constant;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 文件上传
 *
 * @author mother fucker
 */
@RestController
@RequestMapping("sys/oss")
public class SysOssController {

    @Resource
    private SysOssService sysOssService;

    @Resource
    private SysConfigService sysConfigService;

    private final static String KEY = ConfigConstant.CLOUD_STORAGE_CONFIG_KEY;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:oss:all")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = sysOssService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 云存储配置信息
     */
    @RequestMapping("/config")
    @RequiresPermissions("sys:oss:all")
    public R config() {
        CloudStorageConfig config = sysConfigService.getConfigObject(KEY, CloudStorageConfig.class);

        return R.ok().put("config", config);
    }


    /**
     * 保存云存储配置信息
     */
    @RequestMapping("/saveConfig")
    @RequiresPermissions("sys:oss:all")
    public R saveConfig(@RequestBody CloudStorageConfig config) {
        //校验类型
        ValidatorUtils.validateEntity(config);

        if (config.getType() == Constant.CloudService.QINIU.getValue()) {
            //校验七牛数据
            ValidatorUtils.validateEntity(config, QiniuGroup.class);
        } else if (config.getType() == Constant.CloudService.ALIYUN.getValue()) {
            //校验阿里云数据
            ValidatorUtils.validateEntity(config, AliyunGroup.class);
        } else if (config.getType() == Constant.CloudService.QCLOUD.getValue()) {
            //校验腾讯云数据
            ValidatorUtils.validateEntity(config, QcloudGroup.class);
        }

        sysConfigService.updateValueByKey(KEY, new Gson().toJson(config));

        return R.ok();
    }


    /**
     * 上传文件
     */
    @RequestMapping("/upload")
    @RequiresPermissions("sys:oss:all")
    public R upload(@RequestParam("file") MultipartFile file) throws Exception {
        return R.ok().put("url", uploadToOss(file));
    }

    /**
     * Ueditor上传文件
     */
    @RequestMapping("/uploadUEditor")
    public String uploadUEditor(
            HttpServletRequest request,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "action", required = false) String action) throws Exception {
        if ("config".equals(action)) {
            String configJsonPath = "/statics/ueditor/config.json";
            ConfigManager configManager = new ConfigManager(configJsonPath);
            return configManager.getAllConfig().toJSONString();
        }

        return JSONObject.fromObject(R.ok()
                .put("url", uploadToOss(file))
                .put("state", "SUCCESS")
                .put("type", file.getContentType())
                .put("original", file.getOriginalFilename())).toString();
    }

    public String uploadToOss(MultipartFile file) {
        try{
            if (file.isEmpty()) {
                throw new RRException("上传文件不能为空");
            }
            //上传文件
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);
            //保存文件信息
            SysOssEntity ossEntity = new SysOssEntity();
            ossEntity.setUrl(url);
            ossEntity.setCreateDate(new Date());
            sysOssService.save(ossEntity);
            return url;
        }catch (Exception e){
            throw new RRException("上传失败，请检查OSS阿里云存储的账户余额");
        }
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:oss:all")
    public R delete(@RequestBody Long[] ids) {
        sysOssService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
