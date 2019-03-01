import axios from "axios";
import qs from "qs";
import {Message} from "element-ui";

const _axios = axios.create({
    baseURL: `http://${window.APP_SETTING.SERVER_HOST}:${window.APP_SETTING.SERVER_PORT}/`, // 请求基础地址
    timeout: 60000,
    headers: {"Content-Type": "application/x-www-form-urlencoded"}
  }),
  promiseArr = {},
  errorCode = [{code: 1, msg: "成功"}, {code: 0, msg: "逻辑错误"}, {code: -1, msg: "服务器错误"}, {code: -2, msg: "记录已存在"}, {code: -3, msg: "存在关联数据"}];

/* 请求 */
_axios.defaults.transformRequest = (data = {}, headers) => {
  if (typeof data === "string") {
    return data;
  }

  const Authorization = localStorage.getItem(window.TOKEN_KEY) || sessionStorage.getItem(window.TOKEN_KEY) || null;
  if (Authorization) {
    data.oauth = Authorization;
  }
  return qs.stringify(data);
};

let cancel;
// 请求拦截器
_axios.interceptors.request.use((config) => {
// 发起请求时，取消掉当前正在进行的相同请求
  if (promiseArr[config.url]) {
    promiseArr[config.url]("操作取消");
    promiseArr[config.url] = cancel;
  } else {
    promiseArr[config.url] = cancel;
  }
  return config;
}, (error) => Promise.reject(error));

// 响应拦截器即异常处理
_axios.interceptors.response.use((response) => {
    if (response.data && response.data.code === 1) {
      return {data: response.data || {code: 1}, res: response};
    } else {
      let message = "请求成功";
      if (response.data) {
        if (typeof response.data === "object" || response.data.hasOwnProperty("code")) {
          const errorCodeItem = errorCode.find((element) => element.code === response.data.code);
          message = errorCodeItem ? errorCodeItem.msg : "";
          message = response.data ? response.data.msg : message;
          return {data: response.data || {code: 1, data: "", msg: message}, res: {data: response.data || {code: 1, data: "", msg: message}}};
        } else {
          message = response.data || message;
        }
      }
      return {data: {code: 1, data: "", msg: message}, res: {data: {code: 1, data: "", msg: message}}};
    }
  },
  (error) => {
    if (error && error.response) {
      switch (error.response.status) {
        case 400:
          error.message = "错误请求";
          break;
        case 401:
          error.message = "未授权，请重新登录";
          if (process.env.NODE_ENV === "development") {
            window._Vue._router.push("/login");
          }
          break;
        case 403:
          error.message = "拒绝访问";
          break;
        case 404:
          error.message = "请求错误,未找到该资源";
          break;
        case 405:
          error.message = "请求方法未允许";
          break;
        case 408:
          error.message = "请求超时";
          break;
        case 500:
          error.message = "服务器端出错";
          break;
        case 501:
          error.message = "网络未实现";
          break;
        case 502:
          error.message = "网络错误";
          break;
        case 503:
          error.message = "服务不可用";
          break;
        case 504:
          error.message = "网络超时";
          break;
        case 505:
          error.message = "http版本不支持该请求";
          break;
        default:
          error.message = `连接错误${error.response.status}`;
      }
    } else {
      error.message = "连接到服务器失败";
    }
    Message.closeAll();
    Message({
      type: "error",
      message: error.message,
      duration: 3000
    });
    return Promise.resolve({data: {}, res: {code: -1, data: {}, msg: "请求失败"}});
  });

export {
  _axios
};
