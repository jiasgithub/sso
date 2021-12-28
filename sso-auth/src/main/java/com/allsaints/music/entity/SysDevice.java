package com.allsaints.music.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.allsaints.music.entity.listener.AuditListener;

import lombok.Data;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_device")
@EntityListeners(value = { AuditingEntityListener.class, AuditListener.class })
public class SysDevice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1065345468165681840L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "brand_id")
	private Long brandId;

	private String code;

	private String name;

	private String description;

	@Column(name = "sequence_num")
	private Integer sequenceNum;

	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "create_by")
	private String createBy;

	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	@Column(name = "update_time")
	private Date updateTime;

	@Column(name = "update_by")
	private String updateBy;

}