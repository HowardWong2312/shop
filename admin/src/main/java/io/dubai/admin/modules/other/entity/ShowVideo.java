package io.dubai.admin.modules.other.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * @author mother fucker
 * @name 我的视频
 * @date 2021-12-25 19:13:36
 */
@Data
@TableName("t_show_video")
public class ShowVideo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Integer id;

    //用户id
    @TableField("userId")
    private Integer userId;

    //用户昵称
    @TableField(exist = false)
    private String userPhone;

    //视频URL
    @TableField("videoUrl")
    private String videoUrl;

    //视频第一帧
    @TableField("firstImage")
    private String firstImage;

    //视频时长（秒）
    @TableField("videoDuration")
    private String videoDuration;

    //是否bb秀视频0否1是
    @TableField("isShow")
    private Integer isShow;

    //点赞数量
    @TableField("supportNum")
    private Integer supportNum;

    //浏览数量
    @TableField("viewNum")
    private Integer viewNum;


}
