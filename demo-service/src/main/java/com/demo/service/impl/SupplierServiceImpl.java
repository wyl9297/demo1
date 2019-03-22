package com.demo.service.impl;

import cn.bidlink.framework.util.gen.IdWork;
import com.demo.model.*;
import com.demo.persistence.dao.*;
import com.demo.service.SupplierService;
import com.google.common.collect.Maps;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("SupplierService")
public class SupplierServiceImpl implements SupplierService {

    private static final Logger log = LoggerFactory.getLogger(SupplierServiceImpl.class);

    @Autowired
    @Qualifier("uniregJdbcTemplate")
    protected JdbcTemplate uniregJdbcTemplate;
    @Autowired
    @Qualifier("approveJdbcTemplate")
    protected JdbcTemplate approveJdbcTemplate;

    @Autowired
    private ApprovingMapper approvingMapper;
    @Autowired
    BsmCompanySupplierApplyMapper bsmCompanySupplierApplyMapper;

    @Autowired
    BsmCompanySupplierMapper bsmCompanySupplierMapper;

    @Autowired
    BsmSupplierCatalogRelationMapper bsmSupplierCatalogRelationMapper;

    @Autowired
    RegUserMapper regUserMapper;

    @Autowired
    private ApproveTaskRecodeMapper approveTaskRecodeMapper;

    /**
     * 查询新中心库信息
     *
     * @return
     */
    public Map<String, Object> findByCondition(Long companyId) {
        Map<String, Object> map = uniregJdbcTemplate.queryForMap(" select id,name from t_reg_user where company_id = ? and is_subuser = 0", companyId);
        if (CollectionUtils.isEmpty(map)) {
            throw new RuntimeException("查询中心库信息失败");
        } else {
            return map;
        }
    }

    @Override
    public String handleSupplierAdmittanceRecode(Long companyId, Long oriCompanyId, List<Long> list) {
        String sql = "INSERT INTO supplier_admittance_record ( id, company_id, supplier_id, starting_time, admittance_time, admittance_user_name, join_time, reason, is_past_due, create_time, " +
                "create_user_name, create_user_id, update_time, update_user_name, update_user_id )  " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // 查询中心库信息
        Map<String, Object> userMap = findByCondition(companyId);
        long main_user_id = (long) userMap.get("id");
        String main_user_name = (String) userMap.get("name");
        List<Object[]> parms = new ArrayList<>();
        Map<Long, BsmCompanySupplier> supplierInfoMap = bsmCompanySupplierMapper.getSupplierInfoList(oriCompanyId, list);
        for (int i = 0; i < list.size(); i++) {
            log.info("-------------进入循环，开始遍历，需要遍历的数量:{}----------", list.size());
            Long id = IdWork.nextId();
            Long supplierId = list.get(i);
            Date nowTime = new Date();
            BsmCompanySupplier info = supplierInfoMap.get(supplierId);
            if (null != info) {
                parms.add(new Object[]{id, companyId, supplierId, info.getCreateTime(), info.getCreateTime(), info.getCreateUserName(), info.getResponseTime(), null, 0, nowTime, main_user_name, main_user_id, nowTime, main_user_name, main_user_id});
            } else {
                System.out.println("未查到供应商信息：" + supplierId);
                parms.add(new Object[]{id, companyId, supplierId, nowTime, nowTime, null, nowTime, null, 0, nowTime, main_user_name, main_user_id, nowTime, main_user_name, main_user_id});
            }
            log.info("newId:{}", id);
        }
        int[] batchUpdate = uniregJdbcTemplate.batchUpdate(sql, parms);
        for (int i = 0; i < batchUpdate.length; i++) {
            System.out.println(batchUpdate[i]);
        }
        return "success";
    }


    @Override
    public String bsmToSupplier(Long originCompanyId, Long destCompanyId) {
        String sql = "INSERT INTO supplier ( id , company_id , supplier_id , supplier_code , symbiosis_status , supplier_source , approve_status , create_time " +
                ", create_user_name , create_user_id , update_time , update_user_name , update_user_id  ) " +
                " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) ";
        List<BsmCompanySupplierApply> supplierApplyList
                = bsmCompanySupplierApplyMapper.selectByCompanyId(originCompanyId);

        Map<String, Object> map = uniregJdbcTemplate.queryForMap(" select id,name from t_reg_user where company_id = ? and is_subuser = 0", destCompanyId);
        long main_user_id = (long) map.get("id");
        String main_user_name = (String) map.get("name");
        List<Object[]> parms = new ArrayList<>();
        List<Long> supplierIdList = new ArrayList<>();
        //待准入供应商
        for (BsmCompanySupplierApply bsmCompanySupplierApply : supplierApplyList) {
            Integer newSupplierStatus = getNewSupplierStatus(bsmCompanySupplierApply);
            if (null != newSupplierStatus) {
                Long createUserId = main_user_id;
                String createUserName = main_user_name;
                if (bsmCompanySupplierApply.getCreateUserId() != null) {
                    RegUser regUser = new RegUser();
                    regUser.setId(bsmCompanySupplierApply.getCreateUserId());
                    regUser.setCompanyId(originCompanyId);
                    RegUser user = regUserMapper.findByCondition(regUser);
                    createUserId = (null == user || null == user.getBidlinkId()) ? main_user_id : user.getBidlinkId();
                    createUserName = (null == user || null == user.getName()) ? createUserName : user.getName();
                }
                parms.add(new Object[]{IdWork.nextId(), destCompanyId, bsmCompanySupplierApply.getSupplierId(), bsmCompanySupplierApply.getSupplierCode(), newSupplierStatus, 0, 1, bsmCompanySupplierApply.getCreateTime()
                        , createUserName, createUserId, new Date(), createUserName, createUserId});
                supplierIdList.add(bsmCompanySupplierApply.getSupplierId());
            }
        }

        //合作供应商
        List<BsmCompanySupplier> cooperateSupplier = bsmCompanySupplierMapper.getCooperateSupplier(originCompanyId);
        for (BsmCompanySupplier bsmCompanySupplier : cooperateSupplier) {
            Long createUserId = main_user_id;
            String createUserName = main_user_name;
            if (bsmCompanySupplier.getCreateUserId() != null) {
                RegUser regUser1 = new RegUser();
                regUser1.setId(bsmCompanySupplier.getCreateUserId());
                regUser1.setCompanyId(originCompanyId);
                RegUser user = regUserMapper.findByCondition(regUser1);
                createUserId = (null == user || null == user.getBidlinkId()) ? main_user_id : user.getBidlinkId();
                createUserName = (null == user || null == user.getName()) ? createUserName : user.getName();
            }
            parms.add(new Object[]{IdWork.nextId(), destCompanyId, bsmCompanySupplier.getSupplierId(), bsmCompanySupplier.getSupplierCode(), 2, 0, 1, bsmCompanySupplier.getCreateTime()
                    , createUserName, createUserId, new Date(), createUserName, createUserId});
            supplierIdList.add(bsmCompanySupplier.getSupplierId());
        }
        //插入供应商主表
        int[] batchUpdate = uniregJdbcTemplate.batchUpdate(sql, parms);
        //添加准入记录
        String s = this.handleSupplierAdmittanceRecode(destCompanyId, originCompanyId, supplierIdList);
        return "success";
    }

    @Override
    public String catalogRelationMigrate(Long originCompanyId, Long destCompanyId) {

        String sql = "INSERT INTO supplier_category ( id , category_id , company_id , " +
                " supplier_id , create_time , create_user_name , create_user_id ," +
                " update_time , update_user_name , update_user_id , is_past_due ) " +
                " VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
        //主账号
        Map<String, Object> map = uniregJdbcTemplate.queryForMap(" select id,name from t_reg_user where company_id = ? and is_subuser = 0", destCompanyId);
        String main_user_id = map.get("id").toString();
        String main_user_name = map.get("name").toString();
        //旧库采购品供应商关联关系表
        List<BsmSupplierCatalogRelation> bsmSupplierCatalogRelations = bsmSupplierCatalogRelationMapper.selectByCompanyId(originCompanyId);
        //新旧 采购品关系对照表
        List<Map<String, Object>> maps = uniregJdbcTemplate.queryForList("select catalog_id , old_id from middle_corp_catalog where company_id = ?", destCompanyId);
        //新旧采购分类映射id
        Map oldToNewMap = new HashMap();
        for (Map m : maps) {
            oldToNewMap.put(m.get("old_id"), m.get("catalog_id"));
        }
        List<Object[]> parms = new ArrayList<>();
        for (BsmSupplierCatalogRelation bsmSupplierCatalogRelation : bsmSupplierCatalogRelations) {
            parms.add(new Object[]{IdWork.nextId(), oldToNewMap.get(bsmSupplierCatalogRelation.getCatalogId()), destCompanyId, bsmSupplierCatalogRelation.getSupplierId()
                    , bsmSupplierCatalogRelation.getCreateTime(), main_user_name, main_user_id, new Date(), main_user_name, main_user_id, 0});
        }
        uniregJdbcTemplate.batchUpdate(sql, parms);
        return "success";
    }

    private Integer getNewSupplierStatus(BsmCompanySupplierApply bsmCompanySupplierApply) {
        Byte supplierStatus = bsmCompanySupplierApply.getSupplierStatus();
        if (supplierStatus == null) {
            return null;
        }
        Integer status = Integer.valueOf(supplierStatus);
        // bsm 2是待准入 4是合作 6是黑名单 其他均属盘外或审批中
        if (status == 2 || status == 3 || status == 5) {
            return 1;
        }
        /*else if ( status == 4 ){
            return 2;
        } else if ( status == 6 ) {
            return 3;
        }*/
        return null;
    }

    @Transactional
    @Override
    public String handleApproveTaskRecode(Long originCompanyId, Long destCompanyId) {

        String insertSql = "INSERT INTO approve_task_recode (\n" +
                "id,\n" +
                "proc_instance_id,\n" +
                "task_id,\n" +
                "project_id,\n" +
                "custom_id,\n" +
                "task_def_key,\n" +
                "current_node_index,\n" +
                "type,\n" +
                "STATUS,\n" +
                "mult_instance,\n" +
                "approve_suggestion,\n" +
                "approve_type,\n" +
                "approve_time,\n" +
                "assign,\n" +
                "assign_id,\n" +
                "assign_name,\n" +
                "description,\n" +
                "company_id,\n" +
                "create_user_id,\n" +
                "create_user_name,\n" +
                "create_time,\n" +
                "update_user_id,\n" +
                "update_user_name,\n" +
                "update_time \n" +
                ")\n" +
                "VALUES\n" +
                "\t( ?, ?, ?, ?, ?,?,?,?, ?, ?, ?, ?,?, ?, ?, ?, ?,?,?,? ,? ,? , ? ,?)";

        // 根据悦采companyId生成 供应商supplierId拼接字段
        String supplierIds = approveTaskRecodeMapper.concatSupplierId(originCompanyId);

        // 查询悦采审批记录  条件：供应商id  + 悦采 companyId
        List<ApproveTaskRecode> approveTaskRecodes = approveTaskRecodeMapper.selTaskRecodeBySupplierIds(supplierIds, originCompanyId);

        List<Object[]> parms = new ArrayList<>();
        Map<String, Object> userMap = this.findByCondition(originCompanyId);
        long main_user_id = (long) userMap.get("id");
        String main_user_name = (String) userMap.get("name");

        List<Map<String, Object>> idMaps = uniregJdbcTemplate.queryForList("select id,supplier_id from supplier_admittance_record where company_id = ?", destCompanyId);
        Map<Long, Long> converMap = Maps.newHashMap();
        for (Map<String, Object> map : idMaps) {
            converMap.put(Long.valueOf(String.valueOf(map.get("supplier_id"))), Long.valueOf(String.valueOf(map.get("id"))));
        }

        Set<String> hashSet = new HashSet<>();
        StringBuilder stringBuilder = new StringBuilder();

        // 遍历查询结果  批量插入
        for (int i = 0; i < approveTaskRecodes.size(); i++) {
            ApproveTaskRecode approveTaskRecode = approveTaskRecodes.get(i);
            String procInstanceId = approveTaskRecode.getProcInstanceId();

            String assign = approveTaskRecode.getAssign();

            Byte type;
            if (approveTaskRecode.getType() == 1) {
                type = 2;
            } else if (approveTaskRecode.getType() == 2 || approveTaskRecode.getType() == 4) {
                type = 1;
            } else {
                type = approveTaskRecode.getType();
            }

            Long supplierId = approveTaskRecode.getSupplierId();

            // 若供应商无审批记录，project_id 设置为-1
            // 创建人名称 + 更新人名称  --> 使用新companyId查询中心库获取name
            if (converMap.get(supplierId) == null) {
                log.info("supplierId 对应的id为null");
            } else {
                Long assignId = main_user_id;
                String assignName = main_user_name;
                RegUser regUser = new RegUser();
                regUser.setLoginName(assign);
                regUser.setCompanyId(originCompanyId);
                RegUser user = regUserMapper.findByCondition(regUser);
                if (null != user) {
                    assignId = user.getBidlinkId();
                    assignName = user.getName();
                }
                parms.add(new Object[]{IdWork.nextId(), "yc_" + approveTaskRecode.getProcInstanceId(), "yc_" + approveTaskRecode.getTaskId(), converMap.get(supplierId), approveTaskRecode.getCustomId(),
                        approveTaskRecode.getTaskDefKey(), approveTaskRecode.getCurrentNodeIndex(), type, approveTaskRecode.getStatus(), approveTaskRecode.getMultInstance(),
                        approveTaskRecode.getApproveSuggestion(), approveTaskRecode.getApproveType(), approveTaskRecode.getApproveTime(), approveTaskRecode.getAssign(), assignId,
                        assignName, approveTaskRecode.getDescription(), destCompanyId, approveTaskRecode.getCreateUserId(), main_user_name, approveTaskRecode.getCreateTime(),
                        approveTaskRecode.getUpdateUserId(), main_user_name, new Date()});
            }
            if (!hashSet.contains(procInstanceId)) {
                stringBuilder.append(procInstanceId);
                stringBuilder.append(",");
                hashSet.add(procInstanceId);
            }
        }

        String insertApproveSql = " INSERT INTO  approve  ( id ,  project_id ,  project_no ,  project_name ,  project_status ,  project_create_user_id ,  project_create_user_name ,  project_create_time ,  `module` ,  module_node_type ,  proc_inst_id " +
                " ,  custom_id ,  apply_reason ,  custom_version ,  approve_status ,  approve_result ,  company_id ,  create_user_id ,  create_user_name ,  create_time ,  update_user_id ,  update_user_name ,  update_time )" +
                " VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?) ";
        String procInsanceIds = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
        // 查询悦采审批信息 条件：流程实例  + 悦采 companyId
        List<Approving> approvingList = approvingMapper.selApprovingByProcInstanceIds(procInsanceIds, originCompanyId);
        List<Object[]> approvingParms = new ArrayList<>();
        for (Approving approving : approvingList) {

            if (converMap.get(approving.getProjectId()) == null) {
                log.info("projectId 对应的id为null");
            } else {
                approvingParms.add(new Object[]{IdWork.nextId(), converMap.get(approving.getProjectId()), approving.getProjectNo(), approving.getProjectName(), approving.getProjectStatus(), approving.getProjectCreateUserId(), approving.getProjectCreateUserName(), approving.getCreateTime(), 6, 61, "yc_" + approving.getProcInstId()
                        , -1, null, approving.getCustomVersion(), approving.getApproveStatus(), approving.getApproveResult(), destCompanyId, main_user_id, main_user_name, new Date(), main_user_id, main_user_name, new Date()});
            }
        }

        int[] batchUpdate = approveJdbcTemplate.batchUpdate(insertSql, parms);
        int[] update = approveJdbcTemplate.batchUpdate(insertApproveSql, approvingParms);
        return "success";
    }

}
