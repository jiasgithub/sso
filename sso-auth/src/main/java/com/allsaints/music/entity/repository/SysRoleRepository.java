package com.allsaints.music.entity.repository;

import com.allsaints.music.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface SysRoleRepository extends JpaRepository<SysRole, Serializable>, JpaSpecificationExecutor<SysRole> {

    SysRole findByNameAndStatus(String name, Integer status);
}
