package com.allsaints.music.entity.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allsaints.music.entity.SysRoleChannel;

public interface SysRoleChannelRepository extends JpaRepository<SysRoleChannel, Serializable> {
	
	void deleteByRoleId(Long roleId);
	
	
}
