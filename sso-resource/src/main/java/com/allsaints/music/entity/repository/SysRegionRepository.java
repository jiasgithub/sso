package com.allsaints.music.entity.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allsaints.music.entity.SysRegion;

public interface SysRegionRepository extends JpaRepository<SysRegion, Serializable> {
	
}
