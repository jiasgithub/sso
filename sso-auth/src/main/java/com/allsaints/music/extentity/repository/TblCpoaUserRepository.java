package com.allsaints.music.extentity.repository;

import com.allsaints.music.extentity.TblCpoaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface TblCpoaUserRepository extends JpaRepository<TblCpoaUser, Serializable> {

    TblCpoaUser findByUsername(String username);
}
