package com.allsaints.music.entity.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allsaints.music.entity.SysRole;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysRoleRepository extends JpaRepository<SysRole, Serializable>, JpaSpecificationExecutor<SysRole> {

}
