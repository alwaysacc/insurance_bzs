package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "temporary_relationship_info")
public class TemporaryRelationshipInfo {
    /**
     * 乐观锁
     */
    @Column(name = "REVISION")
    private Integer revision;

    /**
     * 创建人
     */
    @Column(name = "CREATED_BY")
    private String createdBy;

    /**
     * 创建时间
     */
    @Column(name = "CREATED_TIME")
    private Date createdTime;

    /**
     * 更新人
     */
    @Column(name = "UPDATED_BY")
    private String updatedBy;

    /**
     * 更新时间
     */
    @Column(name = "UPDATED_TIME")
    private Date updatedTime;

    /**
     * id
     */
    @Id
    @Column(name = "temporary_id")
    private String temporaryId;

    /**
     * 名称
     */
    private String name;

    /**
     * 证件号
     */
    @Column(name = "id_card")
    private String idCard;

    /**
     * 证件类型 1：身份证 2: 组织机构代码证5：港澳居民来往内地通行证 6：其他 7:港澳通行证 8:出生证 9: 营业执照（社会统一信用代码）
     */
    @Column(name = "id_card_type")
    private String idCardType;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是公户还是个户 0个人1公户默认0
     */
    @Column(name = "is_public")
    private String isPublic;

    /**
     * 车辆信息id
     */
    @Column(name = "car_info_id")
    private String carInfoId;

    /**
     * 获取乐观锁
     *
     * @return REVISION - 乐观锁
     */
    public Integer getRevision() {
        return revision;
    }

    /**
     * 设置乐观锁
     *
     * @param revision 乐观锁
     */
    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    /**
     * 获取创建人
     *
     * @return CREATED_BY - 创建人
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * 设置创建人
     *
     * @param createdBy 创建人
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * 获取创建时间
     *
     * @return CREATED_TIME - 创建时间
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * 设置创建时间
     *
     * @param createdTime 创建时间
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 获取更新人
     *
     * @return UPDATED_BY - 更新人
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * 设置更新人
     *
     * @param updatedBy 更新人
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * 获取更新时间
     *
     * @return UPDATED_TIME - 更新时间
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * 设置更新时间
     *
     * @param updatedTime 更新时间
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * 获取id
     *
     * @return temporary_id - id
     */
    public String getTemporaryId() {
        return temporaryId;
    }

    /**
     * 设置id
     *
     * @param temporaryId id
     */
    public void setTemporaryId(String temporaryId) {
        this.temporaryId = temporaryId;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取证件号
     *
     * @return id_card - 证件号
     */
    public String getIdCard() {
        return idCard;
    }

    /**
     * 设置证件号
     *
     * @param idCard 证件号
     */
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    /**
     * 获取证件类型 1：身份证 2: 组织机构代码证5：港澳居民来往内地通行证 6：其他 7:港澳通行证 8:出生证 9: 营业执照（社会统一信用代码）
     *
     * @return id_card_type - 证件类型 1：身份证 2: 组织机构代码证5：港澳居民来往内地通行证 6：其他 7:港澳通行证 8:出生证 9: 营业执照（社会统一信用代码）
     */
    public String getIdCardType() {
        return idCardType;
    }

    /**
     * 设置证件类型 1：身份证 2: 组织机构代码证5：港澳居民来往内地通行证 6：其他 7:港澳通行证 8:出生证 9: 营业执照（社会统一信用代码）
     *
     * @param idCardType 证件类型 1：身份证 2: 组织机构代码证5：港澳居民来往内地通行证 6：其他 7:港澳通行证 8:出生证 9: 营业执照（社会统一信用代码）
     */
    public void setIdCardType(String idCardType) {
        this.idCardType = idCardType;
    }

    /**
     * 获取电话
     *
     * @return mobile - 电话
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置电话
     *
     * @param mobile 电话
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取是公户还是个户 0个人1公户默认0
     *
     * @return is_public - 是公户还是个户 0个人1公户默认0
     */
    public String getIsPublic() {
        return isPublic;
    }

    /**
     * 设置是公户还是个户 0个人1公户默认0
     *
     * @param isPublic 是公户还是个户 0个人1公户默认0
     */
    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    /**
     * 获取车辆信息id
     *
     * @return car_info_id - 车辆信息id
     */
    public String getCarInfoId() {
        return carInfoId;
    }

    /**
     * 设置车辆信息id
     *
     * @param carInfoId 车辆信息id
     */
    public void setCarInfoId(String carInfoId) {
        this.carInfoId = carInfoId;
    }
}