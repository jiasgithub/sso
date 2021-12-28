package com.allsaints.music.entity.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allsaints.music.entity.SysService;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysServiceRepository extends JpaRepository<SysService, Serializable>, JpaSpecificationExecutor<SysService> {

}
