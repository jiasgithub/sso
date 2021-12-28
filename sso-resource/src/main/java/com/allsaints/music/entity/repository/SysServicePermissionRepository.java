package com.allsaints.music.entity.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allsaints.music.entity.SysServicePermission;

public interface SysServicePermissionRepository extends JpaRepository<SysServicePermission, Serializable> {
	
	void deleteByServiceId(Long serviceId);
	
	
}
