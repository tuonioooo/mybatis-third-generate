
import {Input, Switch, Divider} from "antd";
import TextArea from "antd/lib/input/TextArea";
import React from "react";
// @ts-ignore
import {ProColumns} from "@ant-design/pro-table";
import { EditTwoTone, DeleteTwoTone } from "@ant-design/icons";

/*
* 定义数据字段
*/
export interface ${tsNameUpperFirst}FieldItem {
    key: number;  // 必须key 可以用主键Id设置, react 列表中每行都需要key且唯一
<#list table.fields as field>
    <#if field.propertyName == "remove">
    <#else>
        ${field.propertyName}:${field.tsPropertyType};
    </#if>
</#list>
}


/**
* 定义表格列特殊渲染处理
*/
export interface ${tsNameUpperFirst}HandleTableListItemRender {
    setFormValues: (record: ${tsNameUpperFirst}TableListItem) => void;
    handleUpdateModalVisible: (visible: boolean) => void;
    handleDelete: ({}) => void;
    getDetail: (props:any, record: ${tsNameUpperFirst}TableListItem)=>void;
}

/**
* 定义表格列
*/
export interface ${tsNameUpperFirst}TableListItem extends ${tsNameUpperFirst}FieldItem {
}

/**
* 定义表单字段
*/
export interface ${tsNameUpperFirst}FormItem extends ${tsNameUpperFirst}FieldItem {

}


/**
* 定义表单渲染字段
*/
export const ${tsNameUpperFirst}FormItemList = (props?: any) => (
[
<#list table.fields as field>
    <#if (field.keyFlag?string('yes', 'no'))=='no' && field.propertyName != "remove" && field.propertyName != "createTime" && field.propertyName != "lastModified" && field.propertyName != "tenantId">
    {
        name: '${field.propertyName}',
        key: '${field.propertyName}',
        <#if field.comment??>
        label: '${field.comment}',
        </#if>
        <#if field.propertyName == "effect" || field.propertyName == "status" || field.propertyName == "remark" || field.propertyName == "desc" || field.propertyName == "description">
        <#else>

        rules: [    //表单校验规则
            {
            required: true,
            <#if field.propertyType == "Integer">
            type: "number",
            </#if>
            <#if field.comment??>
            message: '${field.comment}为必填项',
            </#if>
            },
            <#if field.propertyName?contains("name") || field.propertyName?contains("Name") || field.propertyName?contains("title") || field.propertyName?contains("word")>
            { validator: props.validatorExist },
            </#if>
        ],
        </#if>
        children: ( //定义Form.Item包裹内的渲染元素
        <#if field.propertyName?contains("effect") || field.propertyName?contains("status") || field.propertyName?contains("enable")>
            <Switch checkedChildren={"开启"} unCheckedChildren={"禁用"}
                    checked={props.${field.propertyName}Checked}
                    onClick={(checked, event) => props.handleSwitch('${field.propertyName}', checked)}
                />
        <#elseif field.propertyName?contains("remark") || field.propertyName?contains("describe") || field.propertyName?contains("description")>
            <TextArea placeholder={'请输入备注'} />
        <#elseif field.propertyType == "date" || field.propertyType == "LocalDateTime">
            <DatePicker />
        <#elseif field.propertyType == "Integer">
            <InputNumber min={1} max={50}/>
        <#elseif field.propertyName?contains("password")>
            <Input.Password placeholder={'请输入密码'} autoComplete="new-password"/> //autoComplete解决表单自动填充问题
        <#else>

            <Input placeholder={'请输入<#if field.comment??>${field.comment}</#if>'} autoComplete={'new-${field.propertyName}'}/>
        </#if>
        )
    },
    </#if>
</#list>
])

//=====================================================================================================================\\

//定义表格列的渲染字段
export const columns = (props: ${tsNameUpperFirst}HandleTableListItemRender): ProColumns<${tsNameUpperFirst}TableListItem>[] => (
[
<#list table.fields as field>
    <#if (field.keyFlag?string('yes', 'no'))=='yes'>
            {
            title: '主键Id',
            dataIndex: 'id',
            hideInSearch: true, // 查询表单中不展示此项
            hideInTable: true,// 表格列中不展示此项
            },
    </#if>
    <#if (field.keyFlag?string('yes', 'no'))=='no' && field.propertyName != "tenantId">
        <#if field.propertyName?contains("effect") || field.propertyName?contains("status") || field.propertyName?contains("enable")>
            {
            title: '<#if field.comment??>${field.comment}</#if>',
            dataIndex: '${field.propertyName}',
            hideInSearch: true, // 查询表单中不展示此项
            valueEnum: {
            'ON': { text: '开启', status: 'Success' },
            'OFF': { text: '关闭', status: 'Default' },
            },
            },
        <#elseif field.propertyName == "remove">
        <#elseif field.propertyName?contains("remark") || field.propertyName?contains("describe") || field.propertyName?contains("description")>
        <#elseif field.propertyName == "lastModified" || field.propertyName == "createTime" || field.propertyName == "createTimeStr" || field.propertyName == "lastModifiedStr">
            {
            title: '<#if field.comment??>${field.comment}</#if>',
            dataIndex: '${field.propertyName}',
            hideInSearch: true, // 查询表单中不展示此项
            sorter: (a: any, b: any) => {//时间排序
                return Date.parse(a.${field.propertyName}) - Date.parse(b.${field.propertyName})
            }
            },
        <#elseif field.propertyName == "sort" || field.propertyName == "ordernum" || field.propertyName == "orderNum">
            {
            title: '<#if field.comment??>${field.comment}</#if>',
            dataIndex: '${field.propertyName}',
            hideInSearch: true, // 查询表单中不展示此项
            sorter: (a: any, b: any) => {//数字排序
                return a.${field.propertyName} - b.${field.propertyName}
            }
            },
        <#else>
            {
            title: '<#if field.comment??>${field.comment}</#if>',
            dataIndex: '${field.propertyName}',
            hideInSearch: true, // 查询表单中不展示此项
            <#if field.propertyName?contains("name") || field.propertyName?contains("title") || field.propertyName?contains("word")>
            copyable: true, //表格列支持复制
            </#if>
            },
        </#if>
    </#if>
</#list>
    {
        title: '操作',
        dataIndex: 'option',
        valueType: 'option',
        hideInForm: true,
        render: (_:any, record:${tsNameUpperFirst}TableListItem) => (
    <>
        <a onClick={() => {

                    //方式一，通过表格列获取记录进行操作表单，场景推荐表单修改字段较小的情况下
                    props.setFormValues(record);
                    props.handleUpdateModalVisible(true);
                    //方式二，通过记录Id获取数据，操作表单，场景推荐数据字段较多的情况下
                    props.getDetail(props, record);
                    }}>
            <EditTwoTone title={'編輯'}/>
        </a>
        <Divider type="vertical" />
        <a onClick={() => props.handleDelete({ id : record.id})}>
            <DeleteTwoTone title={'刪除'} />
        </a>
    </>
),
},
])

