import { IMONITOR } from "../../api";
import { Message } from "element-ui";

const ResDailyCount = function() {
  return {
    url: null,
    count: null,
    duration: null,
    avgDuration: null,
    resDate:null
  };
},
ResDailyCountAPI = {
  resDailyCount: new ResDailyCount(),
  get: () => ResDailyCountAPI.resDailyCount,
  set: (value) => {
    ResDailyCountAPI.resDailyCount = Object.assign(ResDailyCountAPI.resDailyCount, value);
  },
  init: () => new ResDailyCount(),
  getResDailyCount(params) {
    return new Promise((resolve) => {
      IMONITOR.resDailyCount.getResDailyCount(params).then(({data, res}) => {
        let item = {};
        if (data.code === 1) {item = data.data;}
        resolve({ data, item, res });
        });
    });
  },
  listResDailyCount(params) {
    return new Promise((resolve) => {
      IMONITOR.resDailyCount.listResDailyCount(params).then(({data, res}) => {
        let list = [];
        if (data.code === 1) {list = data.data.data;}
        resolve({ data, list, res });
        });
    });
  },
  insertResDailyCount(params) {
    return new Promise((resolve) => {
      IMONITOR.resDailyCount.insertResDailyCount(params).then(({data, res}) => {
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
  updateResDailyCount(params) {
    return new Promise((resolve) => {
      IMONITOR.resDailyCount.updateResDailyCount(params).then(({data, res}) => {
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
  deleteResDailyCount(params) {
    return new Promise((resolve) => {
      IMONITOR.resDailyCount.deleteResDailyCount(params).then(({data, res}) => {
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

export { ResDailyCountAPI };
