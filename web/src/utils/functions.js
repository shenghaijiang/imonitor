/*
* @params 获取用链接传过来的参数：传参数名即可获取
* */
function GetQueryString (name) {
  const reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"),
    params = window.location.search || (window.location.href.indexOf("?") !== -1 ? window.location.href.substring(window.location.href.indexOf("?"), window.location.href.length) : ""),
    r = params.substr(1).match(reg), select = 2;
  if (r !== null) {
    return unescape(r[select]);
  } else {
    return null;
  }
}

export {
  GetQueryString
};
