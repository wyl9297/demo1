package cn.bidlink.datacenter.actualize.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.util.Date;

@ExcelTarget("corpDirectorys")
public class CorpDirectorys implements Serializable {

    @Excel(name = "id")
    private Long id;

    @Excel(name = "采购品编码")
    private String code;

    @Excel(name = "采购目录的ID")
    private Long catalogId;

    @Excel(name = "所属分类")
    private String catalogName;

    @Excel(name = "web")
    private String demo;

    @Excel(name = "采购品名称")
    private String name;

    @Excel(name = "规格")
    private String spec;

    @Max(value = 4,message = "abandon 最大长度为4")
    @Excel(name = "是否禁用")
    private Long abandon;

    @Excel(name = "货号")
    private String pcode;

    @Excel(name = "制造商")
    private String productor;

    // 对应LDY Catalog_id_path
    @Excel(name = "目录树")
    private String treepath;

    // 对应LDY的Update_time
    @Excel(name = "最后修改时间")
    private Date lastmodifytime;

    @Excel(name = "计量单位名")
    private String unitName;

    @Excel(name = "创建时间")
    private Date createTime;

    @Excel(name = "品牌")
    private String brand;

    @Excel(name = "用途")
    private String purpose;

    @Excel(name = "市场参考价格")
    private String marketPrice;

    @Excel(name = "产品特性")
    private String speciality;

    @Excel(name = "技术参数")
    private String techParameters;

    // 不为空，直接存储，否则，查询主账号
    @Excel(name = "创建人")
    private Long creator;

    // 不为空，直接存储，否则，查询主账号
    @Excel(name = "修改人")
    private Long modifier;

    @Excel(name = "产地")
    private String producingAddress;

    @Excel(name = "企业ID")
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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