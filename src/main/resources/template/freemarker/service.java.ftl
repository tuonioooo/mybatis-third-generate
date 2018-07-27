package ${package.Service};

import com.alibaba.fastjson.JSONObject;
import ${package.Entity}.${entity};
import ${package.Entity}.Page;
import ${package.Parent}.dao.DaoSupport;
import ${package.Parent}.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
  *
  * @author ${author}
  *
  */
@Service("${entity?uncap_first}Service")
public class ${entity}Service{

	private static Logger logger = LoggerFactory.getLogger(${entity}Service.class);
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@SuppressWarnings("unchecked")
	public List<${entity}> findByListPage(Page page) {
		return (List<${entity}>) dao.findForList("${entity}Mapper.findByListPage", page);
	}
	
	public int save(${entity} ${entity?uncap_first}){
		try {
			return (int) dao.save("${entity}Mapper.insert", ${entity?uncap_first});
		} catch (Exception e) {
			logger.error("${entity}Mapper insert exception", e);
			return 0;
		}
	}
	
	public int update(${entity} ${entity?uncap_first}){
		try {
			return (int) dao.save("${entity}Mapper.update", ${entity?uncap_first});
		} catch (Exception e) {
			logger.error("${entity}Mapper update exception", e);
			return 0;
		}
	}

	public JSONObject saveOrUpdate(${entity} ${entity?uncap_first}) {
		JSONObject result = new JSONObject();
		try {
			Date nowDateTime = DateUtil.fomatDate(DateUtil.getTime(), "yyyy-MM-dd HH:mm:ss");
			if (${entity?uncap_first}.get${table.PRI?cap_first}() != null) {//修改
				${entity} old${entity} = this.get(${entity?uncap_first}.get${table.PRI?cap_first}());
				<#list table.fields as field>
                <#if field.propertyType == "Date" && field.propertyName == "updatedAt">
                ${entity?uncap_first}.set${field.capitalName}(nowDateTime);
                </#if>
                </#list>
				int rowNum = this.update(${entity?uncap_first});
				if (rowNum == 1) {
					result.put("status", 1);
					result.put("message", "修改成功");
				} else {
					result.put("status", -2);
					result.put("message", "修改异常，请稍候重试");
				}
			} else {//增加
                <#list table.fields as field>
                <#if field.propertyType == "Date">
                ${entity?uncap_first}.set${field.capitalName}(nowDateTime);
                </#if>
                <#if field.propertyName == "state">
                ${entity?uncap_first}.setState(1);//默认数据状态 为：1
                </#if>
                </#list>
				int rowNum = this.save(${entity?uncap_first});
				if (rowNum == 1) {
					result.put("status", 1);
					result.put("message", "保存成功");
				} else {
					result.put("status", -2);
					result.put("message", "保存异常，请稍候重试");
				}
			}
		} catch (Exception e) {
			logger.error("exception-info", e);
			result.put("status", -2);
			result.put("message", "数据操作异常，请稍候重试");
		}
		return result;
	}
	
	
	public ${entity} get(${idClassType?cap_first} id) {
		return id !=null ? (${entity}) dao.findForObject("${entity}Mapper.get", id) : null;
	}
	
	public JSONObject getById(${idClassType?cap_first} id) {
		JSONObject result = new JSONObject();
		${entity} ${entity?uncap_first} = this.get(id);
		if(${entity?uncap_first} !=null ){
			result.put("status", 1);
			result.put("message", "查找成功");
		}else{
			result.put("status", -2);
			result.put("message", "查询异常，请稍候重试");
		}
		return result;
	}
	
	public int delete(${idClassType?cap_first} id){
		try {
			return (int) dao.delete("${entity}Mapper.delete", id);
		} catch (Exception e) {
			logger.error("${entity}Mapper delete exception", e);
			return 0;
		}
	}
	
	public JSONObject delById(${idClassType?cap_first} id){
		JSONObject result = new JSONObject();
		if(id !=null ){
			int rowNum = this.delete(id);
			if (rowNum == 1) {
				result.put("status", 1);
				result.put("message", "删除成功");
			} else {
				result.put("status", "-2");
				result.put("message", "删除异常，请稍候重试");
			}
		}else {
			result.put("status", -2);
			result.put("message", "参数异常，请稍候重试");
		}
	    return result;
	}
}