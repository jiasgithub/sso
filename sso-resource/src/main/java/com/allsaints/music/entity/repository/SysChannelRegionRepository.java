package com.allsaints.music.entity.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allsaints.music.entity.SysChannelRegion;

public interface SysChannelRegionRepository extends JpaRepository<SysChannelRegion, Serializable> {
	
	void deleteByChannelId(Long channelId);
	
}
