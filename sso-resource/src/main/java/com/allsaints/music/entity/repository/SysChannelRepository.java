package com.allsaints.music.entity.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allsaints.music.entity.SysChannel;

public interface SysChannelRepository extends JpaRepository<SysChannel, Serializable> {
	
}
