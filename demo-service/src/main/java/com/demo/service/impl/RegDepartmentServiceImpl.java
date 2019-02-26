package com.demo.service.impl;

import cn.bidlink.base.BusinessException;
import cn.bidlink.base.ServiceResult;
import cn.bidlink.framework.util.gen.IdWork;
import cn.bidlink.resacl.dto.CallBackDTO;
import cn.bidlink.resacl.model.Org;
import cn.bidlink.resacl.service.OrgcService;
import cn.bidlink.usercenter.server.entity.TRegCompany;
import cn.bidlink.usercenter.server.entity.TRegUser;
import cn.bidlink.usercenter.server.service.DubboTRegCompanyService;
import cn.bidlink.usercenter.server.service.DubboTRegUserService;
import com.demo.model.RegDepartment;
import com.demo.persistence.dao.RegDepartmentMapper;
import com.demo.service.RegDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("RegDepartmentService")
public class RegDepartmentServiceImpl implements RegDepartmentService {

    @Autowired
    private RegDepartmentMapper regDepartmentMapper;

    @Autowired
    private OrgcService orgcService;

    @Autowired
    private DubboTRegUserService dubboTRegUserService;

    @Autowired
    private DubboTRegCompanyService dubboTRegCompanyService;

    @Autowired
    @Qualifier("aclJdbcTemplate")
    protected JdbcTemplate aclJdbcTemplate;

    //迁移部门
    @Override
    public String handleRegDepartment(Long originCompanyId , Long destCompanyId) {
        TRegUser regUser = new TRegUser();
        regUser.setCompanyId(destCompanyId);
        regUser.setIsSubuser(0);
        ServiceResult<List<TRegUser>> condition = dubboTRegUserService.findByCondition(regUser);
        if( !condition.getSuccess() || condition.getResult().size() == 0 ){
            return "error" ;
        }
        List<TRegUser> regUserList = condition.getResult();
        List<Map<String,String>> userRelation = regDepartmentMapper.getDepUserRelation(originCompanyId);
        List<RegDepartment> department = regDepartmentMapper.getRegDepartmentByCompanyId(originCompanyId);

        //悦采 reg_department 转换为隆道云的 sys_org 数据格式
        Map<Long,Org> orgMap = convertDepartmentToOrg(department, regUserList.get(0), destCompanyId);

        //将人员关系的list按照部门分配到对应的map中
        Map<String,List<Map<String,String>>> userOrgRelationMap = new HashMap();
        for ( Map m : userRelation ){
            Object o = m.get("depId");
            if( null != o ){
                List<Map<String, String>> list = userOrgRelationMap.get(o.toString());
                if( null == list || list.size() == 0 ){
                    List<Map<String,String>> l = new ArrayList<>();
                    l.add(m);
                    userOrgRelationMap.put( o.toString() , l );
                } else {
                    list.add(m);
                }
            }
        }

        //新增组织 并匹配人员
        for( Long key : orgMap.keySet()){
            if( null != key ){
                //添加新组织
                Org org = orgMap.get(key);
                System.out.println(org.getOrgId());
                int result = aclJdbcTemplate.update("INSERT INTO sys_org (ORGID, COMPANYID,ORGNAME,ORGDESC,ORGSUPID,PATH,DEPTH,CREATEBY,CREATETIME,UPDATEBY,UPDATETIME,SN,ORGPATHNAME,ISDEL,ORGTYPE,CODE,ADDRESS,AREACODES,AREANAMES) " +
                        "VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ? , ?, ? , ? , ? , ? , ?)"
                        ,org.getOrgId(),org.getCompanyId(),org.getOrgName(),org.getOrgDesc(),org.getOrgSupId(),org.getPath(),org.getDepth(),org.getCreateBy(),org.getCreatetime(),org.getUpdateBy(),org.getUpdatetime(),org.getSn(),org.getOrgPathname(),org.getIsDel(),org.getOrgType(),org.getCode(),org.getAddress(),org.getAreaCodes(),org.getAreaNames());
                System.out.println("成功插入【" + result + "】条");
                //根据旧库对应关系给用户设置部门
                List<Map<String, String>> list = userOrgRelationMap.get(key.toString());
                if( null != list && list.size() > 0 ){
                    for ( Map map : list ){
                        //System.out.println(org.getOrgId() + "---------" + map.get("userId") );
                        aclJdbcTemplate.update("INSERT INTO sys_user_org (USERORGID, ORGID, USERID, ISPRIMARY, ISCHARGE, CREATEBY, CREATETIME) " +
                                "VALUES (?, ?, ?, '1', '0', NULL , ?);\n",IdWork.nextId(),org.getOrgId(),map.get("userId").toString(),new Date());
                    }
                }
            }
        }
        //设置Path （子部门根据ORGSUPID匹配父部门 此sql执行一次会修改一级的所有部门 并返回修改的行数 再次执行会修改下一级 ... 当返回为0行的时候 说明所有部门的path全部修改完毕 跳出循环）
        for( int i = 0 ; i < 15 ; i ++ ){
            int update = aclJdbcTemplate.update("UPDATE sys_org s1 JOIN sys_org s2 ON s1.ORGSUPID = s2.ORGID SET s1.ORGPATHNAME = concat( s2.ORGPATHNAME , concat('/',s1.ORGNAME,'') , '' ), s1.PATH = CONCAT( s2.PATH , concat('',s1.ORGID,'') , '.' )WHERE s1.COMPANYID = ?" , destCompanyId);
            if( update == 0 ){
                break;
            }
        }
        return "success";
    }

    private Map<Long,Org> convertDepartmentToOrg(List<RegDepartment> regDepartmentList,TRegUser regUser,Long companyId){
        Map<Long,Org> map = new HashMap();
        ServiceResult<TRegCompany> tRegCompanyServiceByPk = dubboTRegCompanyService.findByPk(companyId);
        if( !tRegCompanyServiceByPk.getSuccess() || tRegCompanyServiceByPk.getResult() == null ){
            throw new BusinessException("未找到 【" + companyId + "】对应的隆道云企业");
        }
        TRegCompany company = tRegCompanyServiceByPk.getResult();
        //新旧id对照的map key旧id value新id
        Map<Long, Long> oldToNewMap = new HashMap();
        //最上级分类
        Map<String, Object> queryForMap = aclJdbcTemplate.queryForMap("select ORGID FROM sys_org where COMPANYID = ? and ORGSUPID = ? and ORGTYPE = ?",companyId,companyId,1);
        //数据清洗
        for ( RegDepartment regDepartment : regDepartmentList) {
            Org org = new Org();
            long newId = IdWork.nextId();
            if( BigDecimal.ZERO.equals(regDepartment.getParentId())){
               if( null != queryForMap ){
                   if( null != queryForMap.get("ORGID") ){
                       org.setOrgSupId(Long.valueOf(queryForMap.get("ORGID").toString()));
                   }
               }
            }else {
                org.setOrgSupId(Long.valueOf(regDepartment.getParentId().toString()));
            }
            Long oldId = regDepartment.getId();
            oldToNewMap.put(oldId,newId);
            org.setOrgId(newId);
            org.setCompanyId(companyId.toString());
            org.setOrgName(regDepartment.getName());
            org.setSn(newId);
            org.setIsDel((short) 0);
            org.setOrgType((short) ("1".equals(regDepartment.getIsCompany())?2:3));
            org.setCreatetime(new Date());
            org.setUpdatetime(new Date());
            org.setCreateBy(regUser.getId().toString());
            org.setUpdateBy(regUser.getId().toString());
            map.put(oldId,org);
        }
        Set<Long> keys = map.keySet();
        for( Long key : keys ){
            Org org = map.get(key);
            if( null != oldToNewMap.get(org.getOrgSupId()) ){
                org.setOrgSupId(oldToNewMap.get(org.getOrgSupId()));
            }
        }
        return map;
    }
}