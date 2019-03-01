import { Message } from "element-ui";
import Vue from "vue";

export const vueHub = new Vue(),
  MessageBox = {
    codeMessage(code, message, fn) {
      return new Promise((resolve, reject) => {
        const errorCode = [{code: 1, msg: "成功"}, {code: 0, msg: "逻辑错误"}, {code: -1, msg: "服务器错误"}, {code: -2, msg: "记录已存在"}, {code: -3, msg: "存在关联数据"}];
        // SystemConfigAPI.getSysConfigList({}).then((res) => {
        //     if(res.data.code==1) Object.assign(errorCode,res.data.data)
        //         let message='';
        //         message=errorCode.find(element => element.code==code).msg;
        //         resolve({message});
        // })
        let message = "";
        message = errorCode.find((element) => element.code === code).msg;
        resolve({message});
      });
    },
    messageBox(res) {
      this.codeMessage(res.data.code).then(({message}) => {
        message = (res.data.msg && "msg" in res.data) ? res.data.msg : message;
        Message({
          message: message,
          type: "error"
        });
      });
    }
  },
  ArrayFun = Array.prototype.remove = function(val) {
    const index = this.indexOf(val);
    if (index > -1) {
      this.splice(index, 1);
    }
  },

  DateFun = Date.prototype.Format = function (fmt) { // author: meizz
    const o = {
      "M+": this.getMonth() + 1, // 月份
      "d+": this.getDate(), // 日
      "h+": this.getHours(), // 小时
      "m+": this.getMinutes(), // 分
      "s+": this.getSeconds(), // 秒
      "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
      "S": this.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt)) {fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));}
    for (const k in o) {if (new RegExp("(" + k + ")").test(fmt)) {fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));}}
    return fmt;
  },
  CheckExp = {
    isTel(tel) {
      // if((/^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$/.test(tel))) return true
      if ((/^1[34578]\d{9}$/.test(tel))) {
        return true;
      } else {return false;}
    },
    isQQ(qq) {
      const re = /[1-9][0-9]{4,}/;
      if (!re.test(qq)) {return false;}
      return true;
    }
  },
  ObjectInit = (params, deleteArray) => { // 删除不需要传的字段，以免造成错误
    let deleteList = ["modifyDate", "modifier", "makeBillMan", "createDate", "deleteFlag"];
    deleteList = deleteList.concat(deleteArray);
    if (!params) {
      return params;
    }
    if (Array.isArray(params)) {
      params.map((paramsItem) => {
        Object.keys(paramsItem).map((item) => {
          deleteList.map((deleteItem) => {
            if (item === deleteItem) {
              delete paramsItem[item];
            }
          });
        });
      });
      return params;
    } else {
      Object.keys(params).map((item) => {
        deleteList.map((deleteItem) => {
          if (item === deleteItem) {
            delete params[item];
          }
        });
      });
      return params;
    }
  },
  ParamsSelect = {
    getAddSelectParams: (row, list, need, field) => {
      const fieldName = Object.keys(field)[0], // 所需要的数据，搜索的数据，需要获取的字段{需要的字段名：数据里面的字段}，匹配字段(在所需要中数据里面能找到){值的字段名：匹配的字段名}
        childrenFun = function(item) {
          if (item[field[fieldName]] === row[fieldName]) {
            Object.keys(need).map((keyItem) => {
              row[keyItem] = item[need[keyItem]];
            });
            return false;
          }
          if (item.children) {
            item.children.map((child) => {
              childrenFun(child);
            });
          }
        };
      list.map((element) => {
        childrenFun(element);
      });
    }
  },
  GetExitData = function(params, api) {
    return new Promise(function(resolve, reject) {
      let isEmpty = true, id = "";
      api(params).then(({res, list}) => {
        if (res.data.data.count !== 0) {
          isEmpty = false;
          id = list[0].id;
        }
        resolve({isEmpty, id});
      });
    });
  };
