package com.allsaints.music.entity.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allsaints.music.entity.SysUserRole;

public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Serializable> {
	
	void deleteByUserId(Long userId);
	
	List<SysUserRole> findByRoleId(Long roleId);
	
}
