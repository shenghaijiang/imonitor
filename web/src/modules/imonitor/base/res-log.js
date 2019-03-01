import { IMONITOR } from "../../api";
import { Message } from "element-ui";

const ResLog = function() {
  return {
    resDate: null,
    mes: null
  };
},
ResLogAPI = {
  resLog: new ResLog(),
  get: () => ResLogAPI.resLog,
  set: (value) => {
    ResLogAPI.resLog = Object.assign(ResLogAPI.resLog, value);
  },
  init: () => new ResLog(),
  getResLog(params) {
    return new Promise((resolve) => {
      IMONITOR.resLog.getResLog(params).then(({data, res}) => {
        let item = {};
        if (data.code === 1) {item = data.data;}
        resolve({ data, item, res });
        });
    });
  },
  listResLog(params) {
    return new Promise((resolve) => {
      IMONITOR.resLog.listResLog(params).then(({data, res}) => {
        let list = [];
        if (data.code === 1) {list = data.data.data;}
        resolve({ data, list, res });
        });
    });
  },
  insertResLog(params) {
    return new Promise((resolve) => {
      IMONITOR.resLog.insertResLog(params).then(({data, res}) => {
        if (data.code === 1) {
          Message({
            message: "新增成功",
            type: "success"
          });
        }
        resolve({data, res});
        });
    });
  },
  updateResLog(params) {
    return new Promise((resolve) => {
      IMONITOR.resLog.updateResLog(params).then(({data, res}) => {
        if (data.code === 1) {
          Message({
            message: "修改成功",
            type: "success"
          });
        }
        resolve({data, res});
        });
    });
  },
  deleteResLog(params) {
    return new Promise((resolve) => {
      IMONITOR.resLog.deleteResLog(params).then(({data, res}) => {
        if (data.code === 1) {
          Message({
            message: "删除成功",
            type: "success"
          });
        }
        resolve({data, res});
        });
    });
  }
};

export { ResLogAPI };
