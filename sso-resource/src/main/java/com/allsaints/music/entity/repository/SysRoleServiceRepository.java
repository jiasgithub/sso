package com.allsaints.music.entity.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allsaints.music.entity.SysRoleService;

public interface SysRoleServiceRepository extends JpaRepository<SysRoleService, Serializable> {

	void deleteByRoleId(Long roleId);
	
}
