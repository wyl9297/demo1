package com.demo.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.util.Date;

@ExcelTarget("corpDirectorys")
public class CorpDirectorys implements Serializable {

    @Excel(name = "id")
    private Long id;

    @Max(value = 20,message = "code 最大长度为20")
    @Excel(name = "code")
    private String code;

    @Excel(name = "catalogId")
    private Long catalogId;

    @Excel(name = "catalogName")
    private String catalogName;

    @Excel(name = "demo")
    private String demo;

    @Max(value = 200,message = "name 最大长度为200")
    @Excel(name = "name")
    private String name;

    @Max(value = 500,message = "spec 最大长度为500")
    @Excel(name = "spec")
    private String spec;

    @Max(value = 4,message = "abandon 最大长度为4")
    @Excel(name = "abandon")
    private Long abandon;

    @Excel(name = "pcode")
    private String pcode;

    @Excel(name = "productor")
    private String productor;

    // 对应LDY Catalog_id_path
    @Excel(name = "treepath")
    private String treepath;

    // 对应LDY的Update_time
    @Excel(name = "lastmodifytime")
    private Date lastmodifytime;

    @Excel(name = "unitname")
    private String unitname;

    @Excel(name = "createTime")
    private Date createTime;

    @Excel(name = "brand")
    private String brand;

    @Excel(name = "purpose")
    private String purpose;

    @Excel(name = "marketPrice")
    private String marketPrice;

    @Excel(name = "speciality")
    private String speciality;

    @Excel(name = "techParameters")
    private String techParameters;

    // 不为空，直接存储，否则，查询主账号
    @Excel(name = "creator")
    private Long creator;

    // 不为空，直接存储，否则，查询主账号
    @Excel(name = "modifier")
    private Long modifier;

    @Excel(name = "producingAddress")
    private String producingAddress;

    @Excel(name = "companyId")
    private Long companyId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getProducingAddress() {
        return producingAddress;
    }

    public void setProducingAddress(String producingAddress) {
        this.producingAddress = producingAddress;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName == null ? null : catalogName.trim();
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo == null ? null : demo.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }


    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec == null ? null : spec.trim();
    }

    public Long getAbandon() {
        return abandon;
    }

    public void setAbandon(Long abandon) {
        this.abandon = abandon;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode == null ? null : pcode.trim();
    }

    public String getProductor() {
        return productor;
    }

    public void setProductor(String productor) {
        this.productor = productor == null ? null : productor.trim();
    }

    public String getTreepath() {
        return treepath;
    }

    public void setTreepath(String treepath) {
        this.treepath = treepath == null ? null : treepath.trim();
    }

    public Date getLastmodifytime() {
        return lastmodifytime;
    }

    public void setLastmodifytime(Date lastmodifytime) {
        this.lastmodifytime = lastmodifytime;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname == null ? null : unitname.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand == null ? null : brand.trim();
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose == null ? null : purpose.trim();
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice == null ? null : marketPrice.trim();
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality == null ? null : speciality.trim();
    }

    public String getTechParameters() {
        return techParameters;
    }

    public void setTechParameters(String techParameters) {
        this.techParameters = techParameters == null ? null : techParameters.trim();
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }
}