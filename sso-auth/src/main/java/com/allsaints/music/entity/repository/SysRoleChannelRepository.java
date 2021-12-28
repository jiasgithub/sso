package com.allsaints.music.entity.repository;

import com.allsaints.music.entity.SysRoleChannel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface SysRoleChannelRepository extends JpaRepository<SysRoleChannel, Serializable> {

    List<SysRoleChannel> findByRoleId(Long roleId);
}
