package com.allsaints.music.entity.repository;

import com.allsaints.music.entity.SysChannel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface SysChannelRepository extends JpaRepository<SysChannel, Serializable> {
	
}
