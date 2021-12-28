package com.allsaints.music.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

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
@Table(name = "sys_dept")
@EntityListeners(value = {AuditingEntityListener.class, AuditListener.class})
public class SysDept implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8954515186644512422L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    private String name;

    private String description;

    @Column(name = "sequence_num")
    private Integer sequenceNum;

    private Integer status;

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

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_dept_role", joinColumns = {@JoinColumn(name = "dept_id")}, inverseJoinColumns = {
            @JoinColumn(name = "role_id")})
    @Where(clause = "status = 1")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<SysRole> supportRoles;
}