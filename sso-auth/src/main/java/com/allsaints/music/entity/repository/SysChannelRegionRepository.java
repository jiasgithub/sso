package com.allsaints.music.entity.repository;

import com.allsaints.music.entity.SysChannelRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface SysChannelRegionRepository extends JpaRepository<SysChannelRegion, Serializable> {

    List<SysChannelRegion> findByChannelId(Long channelId);
}
