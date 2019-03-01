import {_axios} from "./axios.config";
import {camelCase, lowerFirst, upperFirst} from "lodash";

const HttpRequest = function (url, content) {
    if (this instanceof HttpRequest) {
      const api = {};
      this.list = content;
      this.list.map((l) => {
        api[camelCase(l)] = {
          [`get${upperFirst(l)}`]: (params) => _axios.post(url + `${lowerFirst(l)}/get${upperFirst(l)}`, {...params}),
          [`list${upperFirst(l)}`]: (params) => _axios.post(url + `${lowerFirst(l)}/list${upperFirst(l)}`, {...params}),
          [`insert${upperFirst(l)}`]: (params) => _axios.post(url + `${lowerFirst(l)}/insert${upperFirst(l)}`, {data: JSON.stringify(params)}),
          [`update${upperFirst(l)}`]: (params) => _axios.post(url + `${lowerFirst(l)}/update${upperFirst(l)}`, {data: JSON.stringify(params)}),
          [`delete${upperFirst(l)}`]: (params) => _axios.post(url + `${lowerFirst(l)}/delete${upperFirst(l)}`, {...params})
        };
      });
      this.api = api;
    } else {
      return new HttpRequest(url, content);
    }
  },
  GetUrl = function (program) {
    if (this instanceof GetUrl) {
      const allUrl = {};
      if (window.APP_SETTING[program]) {
        allUrl[program] = `http://${window.APP_SETTING[program].SERVER_HOST}:${window.APP_SETTING[program].SERVER_PORT}/`;
        if (Object(window.APP_SETTING[program]).hasOwnProperty("API")) {
          allUrl[program] = `http://${window.APP_SETTING[program].SERVER_HOST}:${window.APP_SETTING[program].SERVER_PORT}/${window.APP_SETTING[program].API}/`;
        }
      } else {
        allUrl[program] = `http://${window.APP_SETTING.SERVER_HOST}:${window.APP_SETTING.SERVER_PORT}/`;
        if (Object(window.APP_SETTING).hasOwnProperty("API")) {
          allUrl[program] = `http://${window.APP_SETTING.SERVER_HOST}:${window.APP_SETTING.SERVER_PORT}/${window.APP_SETTING.API}/`;
        }
      }
      this.url = allUrl[program];
    } else {
      return new GetUrl(program);
    }
  };

export {
  HttpRequest,
  GetUrl
};
