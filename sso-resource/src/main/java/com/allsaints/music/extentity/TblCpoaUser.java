package com.allsaints.music.extentity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the tbl_cpoa_user database table.
 */
@Entity
@Audited
@AuditOverride(forClass = PrimaryPk.class)
@DynamicInsert
@DynamicUpdate
@Table(name = "tbl_cpoa_user", schema = "allsaintsmusic", catalog = "allsaintsmusic")
public class TblCpoaUser extends PrimaryPk implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "cppartner_id")
    private Long cppartnerId;

    @Column(length = 50)
    private String email;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login_time")
    @LastModifiedDate
    private Date lastLoginTime;

    private Boolean locked;

    @Column(length = 20)
    private String password;

    @Column(length = 20)
    private String phone;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "register_time")
    @CreatedDate
    private Date registerTime;

    @Column(length = 20)
    private String username;

    public Long getCppartnerId() {
        return cppartnerId;
    }

    public void setCppartnerId(Long cppartnerId) {
        this.cppartnerId = cppartnerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}