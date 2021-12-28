package com.allsaints.music.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.allsaints.music.entity.listener.AuditListener;

import lombok.Data;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_user")
@EntityListeners(value = {AuditingEntityListener.class, AuditListener.class})
public class SysUser implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2666889235827247007L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String nickname;

    private String password;

    private String phone;

    private String email;

    @Column(name = "dept_id")
    private Long deptId;

    private Integer status;

    @Column(name = "lock_status")
    private Integer lockStatus;

    @Column(name = "login_ip")
    private String loginIp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "login_time")
    private Date loginTime;

    @Column(name = "try_login_amount")
    private Integer tryLoginAmount;

    @Column(name = "super_flag")
    private Integer superFlag;

    @Column(name = "admin_flag")
    private Integer adminFlag;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_by")
    @CreatedBy
    private String createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_by")
    @CreatedBy
    private String updateBy;

    @OneToOne
    @JoinColumn(name = "dept_id", insertable = false, updatable = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private SysDept dept;

    @OneToMany
    @JoinTable(name = "sys_user_role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {
            @JoinColumn(name = "role_id")})
    @Where(clause = "status = 1")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<SysRole> supportRoles;
}