package com.allsaints.music.entity.repository;

import com.allsaints.music.entity.SysRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface SysRegionRepository extends JpaRepository<SysRegion, Serializable> {
	
}
