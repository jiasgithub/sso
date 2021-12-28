package com.allsaints.music.entity.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allsaints.music.entity.SysPermission;

public interface SysPermissionRepository extends JpaRepository<SysPermission, Serializable> {
	
}
