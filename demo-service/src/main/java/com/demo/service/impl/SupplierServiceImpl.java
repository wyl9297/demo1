package com.demo.service.impl;

import cn.bidlink.base.ServiceResult;
import cn.bidlink.framework.util.gen.IdWork;
import cn.bidlink.usercenter.server.entity.TRegUser;
import cn.bidlink.usercenter.server.service.DubboTRegUserService;
import com.demo.model.BsmSupplierCatalogRelation;
import com.demo.persistence.dao.BsmSupplierCatalogRelationMapper;
import com.demo.service.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("SupplierService")
public class SupplierServiceImpl implements SupplierService {

    private static final Logger log = LoggerFactory.getLogger(SupplierServiceImpl.class);

    @Autowired
    private BsmSupplierCatalogRelationMapper supplierCatalogRelationMapper;

    @Autowired
    private DubboTRegUserService tRegUserService;

    @Autowired
    @Qualifier("uniregJdbcTemplate")
    protected JdbcTemplate uniregJdbcTemplate;

    /**
     * 查询新中心库信息
     *
     * @return
     */
    public List<TRegUser> findByCondition(Long companyId) {
        TRegUser tRegUser = new TRegUser();
        tRegUser.setCompanyId(companyId);
        tRegUser.setIsSubuser(0);

        ServiceResult<List<TRegUser>> byCondition = tRegUserService.findByCondition(tRegUser);
        if (!byCondition.getSuccess()) {
            log.error("{}调用{}时发生未知异常,error Message:{}", "com.demo.controller.CorpExportController.test",
                    "byCondition", byCondition.getCode() + "_" + byCondition.getMessage());
            throw new RuntimeException("err_code:" + byCondition.getCode() + ",err_msg:" + byCondition.getMessage());
        }
        List<TRegUser> result = byCondition.getResult();
        if (result == null) {
            log.warn("com.demo.controller.CorpExportController.test时未获取到结果");
        }
        return result;
    }

    @Override
    public String handleSupplierCatalogRelation(Long companyId) {

        // 根据companyId查询记录
        List<BsmSupplierCatalogRelation> result = supplierCatalogRelationMapper.selectByCompanyId(companyId);

        // 查询中心库信息
        List<TRegUser> tRegUsers = findByCondition(companyId);
        if (tRegUsers.size() > 0) {

            TRegUser tRegUser = tRegUsers.get(0);
            for (BsmSupplierCatalogRelation relation : result) {

                log.info("-------------进入循环，开始遍历，需要遍历的数量:{}----------",result.size());
                Long id = IdWork.nextId();
                Long compId = relation.getCompId();
                Long supplierId = relation.getSupplierId();
                // 发起时间  准入时间  准入发起人  理由 设置为null  数据是否过期 初始化为0
                // 加入时间 创建时间  无值，最新时间   创建人用户名
                Long creator = relation.getCreator();
                Long createUserId = null;
                String createUserName = null;

                // creator字段为null，查询主账号的id，name进行赋值
                if (creator == null) {
                    log.warn("----------creator 为null，即将使用用户中心主账号信息进行填充-----------");
                    createUserId = tRegUser.getId();
                    createUserName = tRegUser.getName();
                }

                Long modifier = relation.getModifier();
                String updateUserName = null;
                Long updateUserId = null;
                if (modifier == null) {
                    log.warn("----------modifier 为null，即将使用用户中心主账号信息进行填充-----------");
                    updateUserId = tRegUser.getId();
                    updateUserName = tRegUser.getName();
                }

                Date nowTime = new Date();
                Date createTime = relation.getCreateTime();
                if (createTime == null) {
                    createTime = new Date();
                }
                int res = uniregJdbcTemplate.update(
                        "INSERT INTO supplier_admittance_record ( id, company_id, supplier_id, starting_time, admittance_time, admittance_user_name, join_time, reason, is_past_due, create_time, " +
                                "create_user_name, create_user_id, update_time, update_user_name, update_user_id )  " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);\n",
                        id, compId, supplierId, null, null, null, nowTime, null, 0, createTime, createUserName, createUserId, nowTime, updateUserName, updateUserId);
                log.info("newId:{}", id);
                System.out.println("插入数据的返回值：\t" + res);
            }
        } else {
            log.error("查询用户中心失败，companyId为：{}", companyId);
        }
        return "success";
    }
}
