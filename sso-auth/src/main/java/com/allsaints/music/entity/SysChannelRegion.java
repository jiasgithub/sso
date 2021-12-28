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
@Table(name = "sys_channel_region")
@EntityListeners(value = { AuditingEntityListener.class, AuditListener.class })
public class SysChannelRegion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4759492692057081843L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "channel_id")
	private Long channelId;

	@Column(name = "region_id")
	private Long regionId;

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