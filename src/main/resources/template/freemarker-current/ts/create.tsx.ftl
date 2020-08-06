import React, {useEffect, useState} from "react";
import {Form, Modal} from "antd";
import {
${tsNameUpperFirst}FormItem,
${tsNameUpperFirst}FormItemList
} from "../data.d";
import {ModalFormOptionProps, ResultData} from "@/utils/types";
//@ts-ignore
import { connect, Dispatch } from "umi";
import VerticalFormItem from "@/components/VerticalFormItem";
import {exist${tsNameUpperFirst}Name} from "@/services/${tsNameLowerFirst}";


export interface ModalDispatchFormOptionProps extends ModalFormOptionProps<${tsNameUpperFirst}FormItem> {
    dispatch?: Dispatch;
    resultData?: ResultData;
}
/**
* ${table.comment}创建表单组件
* @param props
* @constructor
*/
const CreateForm: React.FC<ModalDispatchFormOptionProps> = (props) => {
    const {modalVisible, onSubmit, onCancel, addLoading} = props;
    const [form] = Form.useForm(); // 表单实例
    //const {dispatch, resultData} = props;
<#list table.fields as field>
    <#if field.propertyName?contains("effect") || field.propertyName?contains("status") || field.propertyName?contains("enable")>
    const [${field.propertyName}Checked, handle${field.propertyName?cap_first}Checked] = useState(true);
    const [${field.propertyName}FormValue, handle${field.propertyName?cap_first}FormValue] = useState('ON');
    </#if>
</#list>
    /**
    * 相当于 componentDidMount，componentDidUpdate 和 componentWillUnmount 这三个函数的组合。官网：https://reactjs.bootcss.com/docs/hooks-effect.html
    * 初始化修改表单数据
    * componentDidMount() 等价于  useEffect(()=>{...},[])   每次渲染完之后会执行一次
    * componentDidUpdate() 等价于  useEffect(()=>{...},props) 只要有state或props更改，useEffect就会执行一次
    * componentWillUnmount()等价于 useEffect(()=>{ return ()=>{} )  卸载组件时执行
    */
    useEffect(() => {
        // 设置表单初始化默认值，如果与Form子元素冲突，以Form 为准和重置useState的默认值
        if (form && !modalVisible) {
           //重置useState的默认值
        }

        if (form && modalVisible){

            form.setFieldsValue({
    <#list table.fields as field>
        <#if field.propertyName?contains("effect") || field.propertyName?contains("status") || field.propertyName?contains("enable")>
            ${field.propertyName}:${field.propertyName}FormValue,
        </#if>
    </#list>
            });
        }

        // dispatch({
        //   type: '',
        //   payload: ''
        // })

    }, [modalVisible]);


  /**
    * 表单Switch onClick回调
    * @param option
    * @param checked
    */
    const handleSwitch = (option: string, checked: boolean) => {
<#list table.fields as field>
    <#if field.propertyName?contains("effect") || field.propertyName?contains("status") || field.propertyName?contains("enable")>
        if (option && option === '${field.propertyName}') {
            handle${field.propertyName?cap_first}Checked(checked);
            handle${field.propertyName?cap_first}FormValue(checked ? ${field.propertyName}FormValue : 'OFF')
        }
    </#if>
</#list>
    }



<#list table.fields as field>
<#if field.propertyName?contains("name") || field.propertyName?contains("title") || field.propertyName?contains("word")  || field.propertyName?contains("author")>
    /**
     * 检测${field.comment}是否存在
     * @param props
     */
     const validatorExist =  (rule: any, name: string, callback: (message?: string) => void) => {
         if(!name){
            callback();
         }
         let payload = {${field.propertyName}: name};
         exist${tsNameUpperFirst}Name(payload).then(res => {
         if (res.code == 200) {
            callback();
         } else {
            callback('${field.comment}已存在');
         }
         }).catch(error => {
            callback('服务异常')
         });
     };
    <#break>
</#if>
</#list>
    /**
    * 表单字段数据渲染处理
    * @param record
    */
    const formDataRender = () => {
        return {
            handleSwitch,
            validatorExist,
<#list table.fields as field>
    <#if field.propertyName?contains("effect") || field.propertyName?contains("status") || field.propertyName?contains("enable")>
          ${field.propertyName}Checked,
    </#if>
</#list>
        }
    };

    /**
    * 提交表单回调函数
    */
    const onSubmitOp = async () => {
        await form.validateFields(); // 校验表单
        const submitFields = form.getFieldsValue(); //获取表单数据 相当于Form 中的 onFinish
        onSubmit({
        ...submitFields,
<#list table.fields as field>
    <#if field.propertyName?contains("effect") || field.propertyName?contains("status") || field.propertyName?contains("enable")>
        ${field.propertyName}:${field.propertyName}FormValue,
    </#if>
</#list>
        });
    };

    /**
    * 关闭表单窗口回调函数
    */
    const onCancelOp = () =>{
        onCancel();
        form.resetFields();
    }

    return (
    <Modal
            destroyOnClose
            title="新建"
            visible={modalVisible}
            onOk={()=> onSubmitOp()}
            onCancel={() => onCancelOp()}
            okText={'提交'}
            cancelText={'关闭'}
            confirmLoading={addLoading}
    >
        <Form layout='vertical' form={form}>
            <VerticalFormItem formItemList={${tsNameUpperFirst}FormItemList} column={1} formDataRender={formDataRender()}/>
        </Form>
    </Modal>
    )
    }

export default connect(
({
    ${tsNameLowerFirst},
    loading,
}: {
    ${tsNameLowerFirst}: ResultData;
    loading: {
        effects: {
        [key: string]: boolean
    }
};
}) => ({
    resultData:${tsNameLowerFirst},
    loading: loading.effects['**'],
}))(CreateForm);
/**
* export default connect 用法
* 1.
* 示例：
* export default connect(({roleGroup,loading,}: {roleGroup: ResultDataModelState;loading: {effects: {[key: string]: boolean}};}) => ({submitting: loading.effects['roleGroup/queryRoleGroupSelectList'],resultData: roleGroup,}))(Products);
* 结构分析： 步骤1、步骤2、步骤3
* export default connect(({步骤1}:{步骤2})=>({步骤3}))
* 步骤1：定义变量，该变量指向models模块暴露的namespace且把它(namespace)作为指向model属性state的key
* 步骤2：定义变量的类型，一般就是定义暴露model属性state的类型
* 步骤3：将reducers的state赋值给当前组件的props，
* 2.
* 示例：
* export default connect(({ roleGroup }) => ({
* roleGroup,
* }))(Products);
* 解析：namespace 表示在全局 state 上的 key，因此roleGroup就是model的namespace，
* 第一个roleGroup 就是代表state的key，第二个roleGroup等于state对应Key的value
*/

