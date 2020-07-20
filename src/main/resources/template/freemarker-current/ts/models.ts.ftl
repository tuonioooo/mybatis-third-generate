import {Effect, Reducer} from "@@/plugin-dva/connect";
import {ResultData} from "@/utils/types";
import {get${tsNameUpperFirst}, add${tsNameUpperFirst}, edit${tsNameUpperFirst}, del${tsNameUpperFirst}} from "@/services/${tsNameLowerFirst}";
import { message } from "antd";
import { Constant } from "@/utils/constant";

export interface ModelType {
    namespace: string;
    state: ResultData;
    effects: {
        get: Effect;
        add: Effect;
        edit: Effect;
        del: Effect;
    };
    reducers: {
        store: Reducer<ResultData>;
    };
}

const ${tsNameUpperFirst}Model: ModelType={
    namespace: '${tsNameLowerFirst}',
    state: {},
    effects:{
        *get({afterAction, payload}, {call, put}){
            let result: ResultData  = yield call(get${tsNameUpperFirst}, payload);
            if(result.code === Constant.success){
                yield put({
                    type: 'store',
                    payload: result
                })
            }
        },
        *add({ afterAction, payload }, { call, put }){
            const hide = message.loading("正在添加...");
            let result: ResultData = yield call(add${tsNameUpperFirst}, payload);
            if (result.code === Constant.success) {
                hide();
                message.success(result.msg);
                afterAction.handleModalVisible(false);
                afterAction.actionRef.current.reloadAndRest();
            } else {
                hide();
                result.msg && message.error(result.msg);
            }

        },
        *edit({ afterAction, payload }, { call, put }){
            const hide = message.loading("正在编辑...");
            let result: ResultData = yield call(edit${tsNameUpperFirst}, payload);
            if (result.code === Constant.success) {
                hide();
                message.success(result.msg);
                afterAction.handleUpdateModalVisible(false);
                afterAction.actionRef.current.reloadAndRest();
            } else {
                hide();
                result.msg && message.error(result.msg);
            }
        },
        *del({ afterAction, payload }, { call, put }){
            const hide = message.loading('正在删除...');
            let result: ResultData = yield call(del${tsNameUpperFirst}, payload);
            if (result.code === Constant.success) {
                hide();
                message.success(result.msg);
                afterAction.actionRef.current.reloadAndRest();
            } else {
                hide();
                result.msg && message.error(result.msg);
            }
        },
    },
    reducers:{
        store(state, {payload}) {
            return {...state, ...payload};
        }
    }

}

export default ${tsNameUpperFirst}Model
