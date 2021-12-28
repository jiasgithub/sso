package com.allsaints.music.extentity;

import com.allsaints.music.entity.listener.AuditListener;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(value = { AuditingEntityListener.class, AuditListener.class })
public abstract class PrimaryPk implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	public Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	@CreatedDate
	public Date createTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	@LastModifiedDate
	public Date updateTime;

	public PrimaryPk() {
		super();
	}

}