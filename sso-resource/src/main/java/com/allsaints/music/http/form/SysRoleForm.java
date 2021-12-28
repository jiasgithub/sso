package com.allsaints.music.http.form;

import com.allsaints.music.entity.SysChannel;
import com.allsaints.music.entity.SysService;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * <p>
 * date: 2021/6/24 16:48
 * <p>
 * Author: Mr.S
 */
@Data
public class SysRoleForm implements Serializable {
    private Long id;
    private Long parentId;
    private String name;
    private String description;
    private Integer status;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;
    private List<Long> supportChannels;
    private List<Long> supportServices;
    private List<Long> permissions;
}
