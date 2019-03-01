import { IMONITOR } from "../../api";
import { Message } from "element-ui";

const ResCount = function() {
  return {
    url: null,
    count: null,
    duration: null
  };
},
ResCountAPI = {
  resCount: new ResCount(),
  get: () => ResCountAPI.resCount,
  set: (value) => {
    ResCountAPI.resCount = Object.assign(ResCountAPI.resCount, value);
  },
  init: () => new ResCount(),
  getResCount(params) {
    return new Promise((resolve) => {
      IMONITOR.resCount.getResCount(params).then(({data, res}) => {
        let item = {};
        if (data.code === 1) {item = data.data;}
        resolve({ data, item, res });
        });
    });
  },
  listResCount(params) {
    return new Promise((resolve) => {
      IMONITOR.resCount.listResCount(params).then(({data, res}) => {
        let list = [];
        if (data.code === 1) {list = data.data.data;}
        resolve({ data, list, res });
        });
    });
  },
  insertResCount(params) {
    return new Promise((resolve) => {
      IMONITOR.resCount.insertResCount(params).then(({data, res}) => {
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
  updateResCount(params) {
    return new Promise((resolve) => {
      IMONITOR.resCount.updateResCount(params).then(({data, res}) => {
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
  deleteResCount(params) {
    return new Promise((resolve) => {
      IMONITOR.resCount.deleteResCount(params).then(({data, res}) => {
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

export { ResCountAPI };
