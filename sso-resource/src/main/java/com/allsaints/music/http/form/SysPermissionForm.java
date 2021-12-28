package com.allsaints.music.http.form;

import com.allsaints.music.entity.SysPermission;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * Description:
 * <p>
 * date: 2021/7/16 10:17
 * <p>
 * Author: Mr.S
 */
@Data
public class SysPermissionForm implements Serializable {
    private Long id;

    private Long parentId;

    private Integer top;

    private String name;

    private String title;

    private String icon;

    private String resource;

    private String description;

    private Integer status;

    private Integer orderNum;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;

    private List<SysPermissionForm> permissions;
}
