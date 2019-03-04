package com.demo.service.impl;

import cn.bidlink.base.ServiceResult;
import cn.bidlink.framework.util.gen.IdWork;
import cn.bidlink.usercenter.server.entity.TRegUser;
import cn.bidlink.usercenter.server.service.DubboTRegUserService;
import com.demo.model.*;
import com.demo.persistence.dao.*;
import com.demo.model.BsmCompanySupplierApply;
import com.demo.model.BsmSupplierCatalogRelation;
import com.demo.model.RegUser;
import com.demo.model.bsmCompanySupplier;
import com.demo.persistence.dao.BsmCompanySupplierApplyMapper;
import com.demo.persistence.dao.BsmCompanySupplierMapper;
import com.demo.persistence.dao.BsmSupplierCatalogRelationMapper;
import com.demo.persistence.dao.RegUserMapper;
import com.demo.service.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public String handleSupplierAdmittanceRecode(Long companyId, List<Long> list) {
        String sql = "INSERT INTO supplier_admittance_record ( id, company_id, supplier_id, starting_time, admittance_time, admittance_user_name, join_time, reason, is_past_due, create_time, " +
                "create_user_name, create_user_id, update_time, update_user_name, update_user_id )  " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // 查询中心库信息
        List<TRegUser> tRegUsers = findByCondition(companyId);
        List<Object[]> parms = new ArrayList<>();
        if (tRegUsers.size() > 0) {

            TRegUser tRegUser = tRegUsers.get(0);
            for(int i = 0;i<list.size();i++){
                log.info("-------------进入循环，开始遍历，需要遍历的数量:{}----------",list.size());
                Long id = IdWork.nextId();
                Long supplierId = list.get(i);
                Date nowTime = new Date();
                parms.add(new Object[]{id, companyId, supplierId, null, null, null, nowTime, null, 0, nowTime, tRegUser.getName(), tRegUser.getId(), nowTime, tRegUser.getName(), tRegUser.getId()});
                log.info("newId:{}", id);
            }
        } else {
            log.error("查询用户中心失败，companyId为：{}", companyId);
        }
        int[] batchUpdate = uniregJdbcTemplate.batchUpdate(sql, parms);
        for( int i = 0 ; i < batchUpdate.length ; i ++ ){
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

        Map<String, Object> map = uniregJdbcTemplate.queryForMap(" select id,name from t_reg_user where company_id = ? and is_subuser = 0" , destCompanyId);
        long main_user_id = (long)map.get("id");
        String main_user_name = (String) map.get("name");
        List<Object[]> parms = new ArrayList<>();
        List<Long> supplierIdList = new ArrayList<>();
        //待准入供应商
        for( BsmCompanySupplierApply  bsmCompanySupplierApply : supplierApplyList ){
            Integer newSupplierStatus = getNewSupplierStatus(bsmCompanySupplierApply);
            if( null != newSupplierStatus ){
                Long createUserId = main_user_id;
                String createUserName = main_user_name;
                if( bsmCompanySupplierApply.getCreateUserId() != null ){
                    RegUser regUser = new RegUser();
                    regUser.setId(bsmCompanySupplierApply.getCreateUserId());
                    regUser.setCompanyId(originCompanyId);
                    RegUser user = regUserMapper.findByCondition(regUser);
                    createUserId = ( null == user || null == user.getBidlinkId() ) ? main_user_id : user.getBidlinkId();
                    createUserName = ( null == user || null == user.getName() ) ? createUserName : user.getName();
                }
                parms.add(new Object[]{IdWork.nextId(), destCompanyId, bsmCompanySupplierApply.getSupplierId(), bsmCompanySupplierApply.getSupplierCode(), newSupplierStatus, 0, 1, bsmCompanySupplierApply.getCreateTime()
                        , createUserName, createUserId, new Date(), createUserName, createUserId});
                supplierIdList.add(bsmCompanySupplierApply.getSupplierId());
            }
        }

        //合作供应商
        List<bsmCompanySupplier> cooperateSupplier = bsmCompanySupplierMapper.getCooperateSupplier(originCompanyId);
        for( bsmCompanySupplier  bsmCompanySupplier : cooperateSupplier ){
                Long createUserId = main_user_id;
                String createUserName = main_user_name;
                if( bsmCompanySupplier.getCreateUserId() != null ){
                    RegUser regUser1 = new RegUser();
                    regUser1.setId(bsmCompanySupplier.getCreateUserId());
                    regUser1.setCompanyId(originCompanyId);
                    RegUser user = regUserMapper.findByCondition(regUser1);
                    createUserId = ( null == user || null == user.getBidlinkId() ) ? main_user_id : user.getBidlinkId();
                    createUserName = ( null == user || null == user.getName() ) ? createUserName : user.getName();
                }
                parms.add(new Object[]{IdWork.nextId(), destCompanyId, bsmCompanySupplier.getSupplierId(), bsmCompanySupplier.getSupplierCode(), 2, 0, 1, bsmCompanySupplier.getCreateTime()
                        , createUserName, createUserId, new Date(), createUserName, createUserId});
            supplierIdList.add(bsmCompanySupplier.getSupplierId());
        }
        //插入供应商主表
        int[] batchUpdate = uniregJdbcTemplate.batchUpdate(sql, parms);
        //添加准入记录
        String s = this.handleSupplierAdmittanceRecode(destCompanyId, supplierIdList);
        return "success";
    }

    @Override
    public String catalogRelationMigrate(Long originCompanyId , Long destCompanyId ){

        String sql = "INSERT INTO supplier_category ( id , category_id , company_id , " +
                " supplier_id , create_time , create_user_name , create_user_id ," +
                " update_time , update_user_name , update_user_id , is_past_due ) " +
                " VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
        //主账号
        Map<String, Object> map = uniregJdbcTemplate.queryForMap(" select id,name from t_reg_user where company_id = ? and is_subuser = 0" , destCompanyId);
        String main_user_id = map.get("id").toString();
        String main_user_name = map.get("name").toString();
        //旧库采购品供应商关联关系表
        List<BsmSupplierCatalogRelation> bsmSupplierCatalogRelations = bsmSupplierCatalogRelationMapper.selectByCompanyId(originCompanyId);
        //新旧 采购品关系对照表
        List<Map<String, Object>> maps = uniregJdbcTemplate.queryForList("select catalog_id , old_id from middle_corp_catalog where company_id = ?", destCompanyId);
        //新旧采购分类映射id
        Map oldToNewMap = new HashMap();
        for( Map m : maps ){
            oldToNewMap.put( m.get("old_id") , m.get("catalog_id") );
        }
        List<Object[]> parms = new ArrayList<>();
        for( BsmSupplierCatalogRelation bsmSupplierCatalogRelation : bsmSupplierCatalogRelations ){
            parms.add( new Object[] { IdWork.nextId() , oldToNewMap.get(bsmSupplierCatalogRelation.getCatalogId()) , destCompanyId , bsmSupplierCatalogRelation.getSupplierId()
                    , bsmSupplierCatalogRelation.getCreateTime() , main_user_name , main_user_id , new Date() , main_user_name , main_user_id ,   0  } );
        }
        uniregJdbcTemplate.batchUpdate(sql,parms);
        return "success";
    }

    private Integer getNewSupplierStatus( BsmCompanySupplierApply  bsmCompanySupplierApply ){
        Byte supplierStatus = bsmCompanySupplierApply.getSupplierStatus();
        if( supplierStatus == null ){
            return null;
        }
        Integer status = Integer.valueOf(supplierStatus);
        // bsm 2是待准入 4是合作 6是黑名单 其他均属盘外或审批中
        if( status == 2 || status == 3 || status == 5 ){
            return 1;
        }
        /*else if ( status == 4 ){
            return 2;
        } else if ( status == 6 ) {
            return 3;
        }*/
        return null;
    }

    @Override
    public String handleApproveTaskRecode(Long companyId){
        String sql = "";
        // 根据companyid查询所属公司下的所有供应商信息(supplierId)   bsm_company_supplier_apply
//        List<BsmCompanySupplierApply> bsmCompanySupplierApplies = bsmCompanySupplierApplyMapper.selectByCompanyId(companyId);
        // 将查询出的所有supplierId作为条件  project_id in(supplier_ids)  查询处所有的流程实例id(proc_inst_id)  approving
//        List<Approving> approvings = approvingMapper.selApproveBySupplierIds(bsmCompanySupplierApplies);
        // 以流程实例id(proc_inst_id) 作为条件查询 proc_instance_id in( )  approve_task_record
        List<ApproveTaskRecode> approveTaskRecodes = approveTaskRecodeMapper.selApproveRecodeByCompanyId(companyId);
//        approveJdbcTemplate.update("insert into approve_task_recode(id,proc_inst_id,task_id,project_id,custom_id,task_def_key,\n" +
//                "current_node_index,type,status,mult_instance,approve_suggest,approve_type,approve_time,\n" +
//                "assign,description,company_id,create_user_id,create_time,update_user_id,update_time,platform)\n" +
//                "values()");
        return null;
    }

}
