import { IMONITOR } from "../../api";
import { Message } from "element-ui";

const ResError = function() {
  return {
    resDate: null,
    url: null,
    para: null,
    original: null
  };
},
ResErrorAPI = {
  resError: new ResError(),
  get: () => ResErrorAPI.resError,
  set: (value) => {
    ResErrorAPI.resError = Object.assign(ResErrorAPI.resError, value);
  },
  init: () => new ResError(),
  getResError(params) {
    return new Promise((resolve) => {
      IMONITOR.resError.getResError(params).then(({data, res}) => {
        let item = {};
        if (data.code === 1) {item = data.data;}
        resolve({ data, item, res });
        });
    });
  },
  listResError(params) {
    return new Promise((resolve) => {
      IMONITOR.resError.listResError(params).then(({data, res}) => {
        let list = [];
        if (data.code === 1) {list = data.data.data;}
        resolve({ data, list, res });
        });
    });
  },
  insertResError(params) {
    return new Promise((resolve) => {
      IMONITOR.resError.insertResError(params).then(({data, res}) => {
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
  updateResError(params) {
    return new Promise((resolve) => {
      IMONITOR.resError.updateResError(params).then(({data, res}) => {
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
  deleteResError(params) {
    return new Promise((resolve) => {
      IMONITOR.resError.deleteResError(params).then(({data, res}) => {
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

export { ResErrorAPI };
