
import { PlusOutlined} from '@ant-design/icons';
import {Button, message} from 'antd';
import React, { useState, useRef } from 'react';
import { PageHeaderWrapper } from '@ant-design/pro-layout';
// @ts-ignore
import ProTable, { ProColumns, ActionType } from '@ant-design/pro-table';
import { ${tsNameUpperFirst}TableListItem, columns} from './data.d';
import { query${tsNameUpperFirst}PageList } from '@/services/${tsNameLowerFirst}';
import CreateForm from "./components/CreateForm";
import UpdateForm from './components/UpdateForm';
import {connect} from "umi";
import { ResultData, TableListProps } from '@/utils/types';
import {Constant} from "@/utils/constant";



/**
* @author dz
* @date
* @desc 列表组件
*/

const ${tsNameUpperFirst}TableList: React.FC<TableListProps> = (props) => {
const [createModalVisible, handleModalVisible] = useState<boolean>(false);
const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
const [formValues, setFormValues] = useState({});
const {dispatch, addLoading, editLoading} = props;
// 用于刷新表格操作
const actionRef = useRef<ActionType>();

// 自定义表格查询请求
const handleRequest = async (params: any) => {
//jpa框架 ：params.current-1 ，myabtis：params.current
let result = await query${tsNameUpperFirst}PageList({...params, page: params.current-1, size: params.pageSize})
result.data.list.map((item: ${tsNameUpperFirst}TableListItem, key: number) => {
item.key = key // 设置list unique key
});
const res:any = {
data:result.data.list,
total:result.data.total,
success:result.code == 200,
}
return res;
};

/**
* 获取详情信息并且设置表单form的默认值传递给子组件UpdateForm中
*/
const getDetail = async (props:any, record: ${tsNameUpperFirst}TableListItem) => {
let result: ResultData = await get${tsNameUpperFirst}({id: record.id})
if (result.code == Constant.success) {
props.setFormValues(result.data);
props.handleUpdateModalVisible(true);
} else {
result.msg && message.error(result.msg);
}
}


const handleAdd =  (payload:any) => {
dispatch({
type: '${tsNameLowerFirst}/add',
payload: payload,
afterAction: {
handleModalVisible: handleModalVisible,
actionRef: actionRef
}
})
}

const handleUpdate = (payload: any) => {
dispatch({
type: '${tsNameLowerFirst}/edit',
payload: payload,
afterAction: {
handleUpdateModalVisible: handleUpdateModalVisible,
actionRef: actionRef
}
})
}

const handleDelete = (payload: any) => {
dispatch({
type: '${tsNameLowerFirst}/del',
payload: payload,
afterAction: {
actionRef: actionRef
}
})
}

return (
<PageHeaderWrapper>
    <ProTable<${tsNameUpperFirst}TableListItem>
        headerTitle="列表"
        rowKey="key"
        toolBarRender={(action, { selectedRows }) => [
        <Button type="primary" onClick={() => { handleModalVisible(true);}}>
        <PlusOutlined /> 新建
        </Button>
        ]}
        //表格查询/分页请求
        request={( params , sorter, filter) => handleRequest({ ...params, sorter, filter })}
        // 列
        columns={columns({
            setFormValues,
            handleUpdateModalVisible,
            handleDelete,
            getDetail,
        })}
        rowSelection={{}}
        // 表格触发标识
        actionRef={actionRef}
        pagination={{
        pageSize:10, // 设置默认分页页码
        }}
        />
        <CreateForm
                onSubmit={handleAdd}
                onCancel={() => {handleModalVisible(false)}}
        modalVisible={createModalVisible}
        addLoading={addLoading}
        record={{}}
        />
        {
        formValues && Object.keys(formValues).length ? (
        <UpdateForm
                onSubmit={handleUpdate}
                onCancel={() => {handleUpdateModalVisible(false)}}
        modalVisible={updateModalVisible}
        record={formValues}
        editLoading={editLoading}
        />
        ): null
        }
</PageHeaderWrapper>
);
};

export default connect(({ loading  } : {loading: { effects: { [key: string]: boolean } }} )=>({
addLoading: loading.effects['${tsNameLowerFirst}/add'],
editLoading: loading.effects['${tsNameLowerFirst}/update'],
}))(${tsNameUpperFirst}TableList);
