package com.allsaints.music.extentity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the tbl_cppartner database table.
 */
@Entity
@Audited
@AuditOverride(forClass = PrimaryPk.class)
@DynamicInsert
@DynamicUpdate
@Table(name = "tbl_cppartner", schema = "allsaintsmusic", catalog = "allsaintsmusic")
public class TblCppartner extends PrimaryPk implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(length = 200)
    private String address;

    @Column(name = "alias_name", length = 100)
    private String aliasName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "apply_time")
    private Date applyTime;

    @Column(name = "bank_account_name", length = 50)
    private String bankAccountName;

    @Column(name = "bank_account_no", length = 50)
    private String bankAccountNo;

    @Column(name = "bank_licence_no", length = 50)
    private String bankLicenceNo;

    @Column(name = "bank_name", length = 50)
    private String bankName;

    @Column(name = "biz_licence", length = 50)
    private String bizLicence;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "biz_licence_expiration")
    private Date bizLicenceExpiration;

    @Column(name = "bizlicence_type", length = 50)
    private String bizlicenceType;

    private Long capital;

    @Column(length = 10)
    private String code;

    @Column(length = 50)
    private String contact;

    @Column(name = "contact_email", length = 50)
    private String contactEmail;

    @Column(name = "contact_mobile", length = 50)
    private String contactMobile;

    @Column(name = "contact_phone", length = 50)
    private String contactPhone;

    @Column(name = "contact_qq", length = 50)
    private String contactQq;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "contract_end")
    private Date contractEnd;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "contract_start")
    private Date contractStart;

    @Lob
    private String introduction;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "logoff_time")
    private Date logoffTime;

    @Column(length = 100)
    private String name;

    @Column(name = "name_en", length = 100)
    private String nameEn;

    @Column(length = 50)
    private String nationality;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "open_biz_time")
    private Date openBizTime;

    @Column(name = "permanent_flag")
    private Boolean permanentFlag;

    @Column(name = "reg_address", length = 200)
    private String regAddress;

    @Lob
    private String remark;

    @Column(length = 50)
    private String representative;

    @Column(name = "representative_email", length = 50)
    private String representativeEmail;

    @Column(name = "representative_id", length = 50)
    private String representativeId;

    @Column(name = "representative_phone", length = 50)
    private String representativePhone;

    private Integer status;

    private Integer type;

    @Column(length = 20)
    private String zipcode;

    public TblCppartner() {
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAliasName() {
        return this.aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Date getApplyTime() {
        return this.applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getBankAccountName() {
        return this.bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccountNo() {
        return this.bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getBankLicenceNo() {
        return this.bankLicenceNo;
    }

    public void setBankLicenceNo(String bankLicenceNo) {
        this.bankLicenceNo = bankLicenceNo;
    }

    public String getBankName() {
        return this.bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBizLicence() {
        return this.bizLicence;
    }

    public void setBizLicence(String bizLicence) {
        this.bizLicence = bizLicence;
    }

    public Date getBizLicenceExpiration() {
        return this.bizLicenceExpiration;
    }

    public void setBizLicenceExpiration(Date bizLicenceExpiration) {
        this.bizLicenceExpiration = bizLicenceExpiration;
    }

    public String getBizlicenceType() {
        return this.bizlicenceType;
    }

    public void setBizlicenceType(String bizlicenceType) {
        this.bizlicenceType = bizlicenceType;
    }

    public Long getCapital() {
        return this.capital;
    }

    public void setCapital(Long capital) {
        this.capital = capital;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContact() {
        return this.contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactEmail() {
        return this.contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactMobile() {
        return this.contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactPhone() {
        return this.contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactQq() {
        return this.contactQq;
    }

    public void setContactQq(String contactQq) {
        this.contactQq = contactQq;
    }

    public Date getContractEnd() {
        return this.contractEnd;
    }

    public void setContractEnd(Date contractEnd) {
        this.contractEnd = contractEnd;
    }

    public Date getContractStart() {
        return this.contractStart;
    }

    public void setContractStart(Date contractStart) {
        this.contractStart = contractStart;
    }

    public String getIntroduction() {
        return this.introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Date getLogoffTime() {
        return this.logoffTime;
    }

    public void setLogoffTime(Date logoffTime) {
        this.logoffTime = logoffTime;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return this.nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNationality() {
        return this.nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Date getOpenBizTime() {
        return this.openBizTime;
    }

    public void setOpenBizTime(Date openBizTime) {
        this.openBizTime = openBizTime;
    }

    public Boolean getPermanentFlag() {
        return this.permanentFlag;
    }

    public void setPermanentFlag(Boolean permanentFlag) {
        this.permanentFlag = permanentFlag;
    }

    public String getRegAddress() {
        return this.regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRepresentative() {
        return this.representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public String getRepresentativeEmail() {
        return this.representativeEmail;
    }

    public void setRepresentativeEmail(String representativeEmail) {
        this.representativeEmail = representativeEmail;
    }

    public String getRepresentativeId() {
        return this.representativeId;
    }

    public void setRepresentativeId(String representativeId) {
        this.representativeId = representativeId;
    }

    public String getRepresentativePhone() {
        return this.representativePhone;
    }

    public void setRepresentativePhone(String representativePhone) {
        this.representativePhone = representativePhone;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getZipcode() {
        return this.zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}