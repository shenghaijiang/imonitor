import { IMONITOR } from "../../api";
import { Message } from "element-ui";

const ResLongTime = function() {
  return {
    resDate: null,
    url: null,
    para: null,
    duration: null
  };
},
ResLongTimeAPI = {
  resLongTime: new ResLongTime(),
  get: () => ResLongTimeAPI.resLongTime,
  set: (value) => {
    ResLongTimeAPI.resLongTime = Object.assign(ResLongTimeAPI.resLongTime, value);
  },
  init: () => new ResLongTime(),
  getResLongTime(params) {
    return new Promise((resolve) => {
      IMONITOR.resLongTime.getResLongTime(params).then(({data, res}) => {
        let item = {};
        if (data.code === 1) {item = data.data;}
        resolve({ data, item, res });
        });
    });
  },
  listResLongTime(params) {
    return new Promise((resolve) => {
      IMONITOR.resLongTime.listResLongTime(params).then(({data, res}) => {
        let list = [];
        if (data.code === 1) {list = data.data.data;}
        resolve({ data, list, res });
        });
    });
  },
  insertResLongTime(params) {
    return new Promise((resolve) => {
      IMONITOR.resLongTime.insertResLongTime(params).then(({data, res}) => {
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
  updateResLongTime(params) {
    return new Promise((resolve) => {
      IMONITOR.resLongTime.updateResLongTime(params).then(({data, res}) => {
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
  deleteResLongTime(params) {
    return new Promise((resolve) => {
      IMONITOR.resLongTime.deleteResLongTime(params).then(({data, res}) => {
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

export { ResLongTimeAPI };
