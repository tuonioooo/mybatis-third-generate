package ${package.Parent}.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ${package.Parent}.bean.${entity};

import ${package.Parent}.controller.base.BaseController;
import ${package.Parent}.service.${entity}Service;

import ${package.Parent}.plugin.page.Page;
import ${package.Parent}.plugin.page.PageData;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "/${entity?uncap_first}")
public class ${entity}Controller extends BaseController {

	@Autowired
	private ${entity}Service ${entity?uncap_first}Service;

	@RequestMapping(value = "/getAll")
	public String getAll(Model model, Page page) throws Exception {	
		PageData pd = this.getPageData();
		PageData pageData = new PageData();
		pageData.put("id", pd.getString("id"));
		page.setPd(pageData);
	
		List<${entity}> list = ${entity?uncap_first}Service.findByListPage(page);
		model.addAttribute("list", list);
		model.addAttribute("pageData", pageData);
		model.addAttribute("page", page);
		return "${entity?uncap_first}/${table.name?replace("_","-")}-list";
	}

	@RequestMapping(value = "/preEdit")
	public String preEdit(${idClassType?cap_first} id, Model model) throws Exception {
		${entity} ${entity?uncap_first} = null;
		if (id != null) {//修改
			${entity?uncap_first} = ${entity?uncap_first}Service.get(id);
		} else {//新增
			${entity?uncap_first} = new ${entity}();
		}
		model.addAttribute("${entity?uncap_first}", ${entity?uncap_first});
		return "${entity?uncap_first}/${table.name?replace("_","-")}-add";
	}
	
	@RequestMapping(value = "/getById")
	public JSONObject getById(${idClassType?cap_first} id) throws Exception {
		return ${entity?uncap_first}Service.getById(id);
	}
	
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject saveOrUpdate(${entity} ${entity?uncap_first}) {
		return ${entity?uncap_first}Service.saveOrUpdate(${entity?uncap_first});
	}

	@RequestMapping(value = "/delById", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject delById(${idClassType?cap_first} id) {
		return ${entity?uncap_first}Service.delById(id);
	}
	
}
