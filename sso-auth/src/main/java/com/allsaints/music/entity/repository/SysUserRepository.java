package com.allsaints.music.entity.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allsaints.music.entity.SysUser;

public interface SysUserRepository extends JpaRepository<SysUser, Serializable> {
	
	SysUser getByUsername(String name);
	
}
