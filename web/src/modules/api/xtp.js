import { _axios } from "./axios.config";
import { GetUrl, HttpRequest } from "./http-request";

const xtpUrl = GetUrl("XTP").url,
    XTP = HttpRequest(xtpUrl, [
        "organize",
        "user"
    ]).api;

XTP.organize.listOrganizeTree = (params) => _axios.post(xtpUrl + "organize/listOrganizeTree", params);

XTP.user.loginUser = (params) => _axios.post(xtpUrl + "user/loginUser", params);

export {
    XTP
};
