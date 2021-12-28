package com.allsaints.music.extentity.repository;

import com.allsaints.music.extentity.TblCppartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface TblCppartnerRepository extends JpaRepository<TblCppartner, Serializable>, JpaSpecificationExecutor<TblCppartner> {

}
