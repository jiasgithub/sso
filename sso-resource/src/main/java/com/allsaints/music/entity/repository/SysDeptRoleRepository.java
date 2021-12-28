package com.allsaints.music.entity.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allsaints.music.entity.SysDeptRole;

public interface SysDeptRoleRepository extends JpaRepository<SysDeptRole, Serializable> {
	
	void deleteByDeptId(Long deptId);
	
	List<SysDeptRole> findByRoleId(Long roleId);
	
}
