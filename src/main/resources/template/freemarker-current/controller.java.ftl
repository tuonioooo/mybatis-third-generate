package ${package.Parent}.controller;


@RestController
@Slf4j
public class ${entity}Controller extends BaseController {

	@Autowired
	private ${entity}Service ${entity?uncap_first}Service;

	@PostMapping("/${entity?uncap_first}/list")
	public ResultData<?> list(${table.qoName} ${table.qoName?uncap_first}) {
		try {
			${table.qoName?uncap_first}.setTenantId(getTenantId());
			return ${entity?uncap_first}Service.findPageList(${table.qoName?uncap_first});
		} catch (Exception e) {
			log.error("【/${entity?uncap_first}/list】分页列表查询接口出现异常", e);
			return ResultData.fail("【/${entity?uncap_first}/list】分页列表查询接口出现异常");
		}
	}

	@PostMapping("/${entity?uncap_first}/add")
	public ResultData<?> add(@Validated(${table.qoName}.Insert.class) ${table.qoName} ${table.qoName?uncap_first}, BindingResult bindingResult) {
		try{
			return this.saveOrUpdate(${table.qoName?uncap_first}, bindingResult);
		}catch (Exception e){
			log.error("【/${entity?uncap_first}/add】新增接口出现异常", e);
			return ResultData.fail("【/${entity?uncap_first}/add】新增接口出现异常");
		}
	}

	@PostMapping("/${entity?uncap_first}/edit")
	public ResultData<?> edit(@Validated(${table.qoName}.Update.class) ${table.qoName} ${table.qoName?uncap_first}, BindingResult bindingResult) {
		try{
			return this.saveOrUpdate(${table.qoName?uncap_first}, bindingResult);
		}catch (Exception e){
			log.error("【/${entity?uncap_first}/edit】编辑接口出现异常", e);
			return ResultData.fail("【/${entity?uncap_first}/edit】编辑接口出现异常");
		}
	}

	private ResultData saveOrUpdate(NovelCategoryRequest novelCategoryRequest, BindingResult bindingResult){
		${table.qoName?uncap_first}.setTenantId(getTenantId());
		${table.qoName?uncap_first}.setOpId(getUserId());
		${table.qoName?uncap_first}.setOpName(getUserName());
		if (bindingResult.hasErrors()) {
			return ResultData.failMsg(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(",")));
		}
		return ${entity?uncap_first}Service.saveOrUpdate(${table.qoName?uncap_first});
	}

	@PostMapping("/${entity?uncap_first}/getDetailById")
	public ResultData<?> getDetailById(Integer id) {
		try {
			return Objects.isNull(id) ? ResultData.failMsg("id不能为空！") : ${entity?uncap_first}Service.getDetailById(id);
		} catch (Exception e) {
			log.error("【/${entity?uncap_first}/getDetailById】获取详情接口出现异常", e);
			return ResultData.fail("【/${entity?uncap_first}/getDetailById】获取详情接口出现异常");
		}
	}

	@PostMapping("/${entity?uncap_first}/exist")
	public ResultData<?> exist(String name, Integer id) {
		try {
			return ${entity?uncap_first}Service.exist(name, id);
		} catch (Exception e) {
			log.error("【/${entity?uncap_first}/exist】名称查重接口出现异常", e);
			return ResultData.fail("【/${entity?uncap_first}/exist】名称查重接口出现异常");
		}
	}

	@PostMapping("/${entity?uncap_first}/deleteById")
	public ResultData<?> deleteById(Integer id) {
		try {
			if(Objects.isNull(id)){
			return ResultData.failMsg("id不能为空！");
			}
			return ${entity?uncap_first}Service.deleteById(id);
		} catch (Exception e) {
			log.error("【/${entity?uncap_first}/deleteById】删除接口出现异常", e);
			return ResultData.fail("【/${entity?uncap_first}/deleteById】删除接口出现异常");
		}
	}

}
