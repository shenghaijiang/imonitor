import { _axios } from "./axios.config";
import { GetUrl, HttpRequest } from "./http-request";

const imonitorUrl = GetUrl("IMONITOR").url,
IMONITOR = HttpRequest(imonitorUrl, [
      "resCount",
      "resDailyCount",
      "resError",
      "resLog",
      "resLongTime"
    ]).api;

export {
  IMONITOR
};
