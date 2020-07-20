import request from '@/utils/request';
import { Constant } from '@/utils/constant';
import { stringify } from "qs";

/**
* 查询分页列表
* @param params
*/
export async function query${tsNameUpperFirst}PageList(params:{}) {
console.log(stringify(params));
return request(`${r"${"}Constant.baseUrl${r"}"}/${tsNameLowerFirst}/list`, {
method: 'POST',
data: stringify(params),
});
}

/**
* 查询——名称查重接口
* @param params
*/
export async function edit${tsNameUpperFirst}Name(params:{}): Promise<any>{
return request(`${r"${"}Constant.baseUrl${r"}"}/${tsNameLowerFirst}/exist`, {
method: 'POST',
data: stringify(params),
});
}

/**
* 查询——根据Id
* @param params
*/
export async function get${tsNameUpperFirst}(params: {}) {
return request(`${r"${"}Constant.baseUrl${r"}"}/${tsNameLowerFirst}/get`, {
method: 'POST',
data: stringify(params),
});
}

/**
* 添加
* @param params
*/
export async function add${tsNameUpperFirst}(params:{}) {
return request(`${r"${"}Constant.baseUrl${r"}"}/${tsNameLowerFirst}/add`, {
method: 'POST',
data: stringify(params),
});
}

/**
* 编辑
* @param params
*/
export async function edit${tsNameUpperFirst}Name(params:{}) {
return request(`${r"${"}Constant.baseUrl${r"}"}/${tsNameLowerFirst}/edit`, {
method: 'POST',
data: stringify(params),
});
}

/**
* 删除
* @param params
*/
export async function del${tsNameUpperFirst}(params: {}) {
return request(`${r"${"}Constant.baseUrl${r"}"}/${tsNameLowerFirst}/del`, {
method: 'POST',
data: stringify(params),
});
}
