// side-nav menus
/* @freemark */

const modules = [
    {sort: 1, name: "访问总计", icon: "fa fa-th-large", path: "/res-count", children: [], displayFlag: true},
    {sort: 2, name: "访问日计", icon: "fa fa-th-large", path: "/res-daily-count", children: [], displayFlag: true},
    {sort: 3, name: "异常日志", icon: "fa fa-th-large", path: "/res-error", children: [], displayFlag: true},
    {sort: 4, name: "调试日志", icon: "fa fa-th-large", path: "/res-log", children: [], displayFlag: true},
    {sort: 5, name: "耗时接口", icon: "fa fa-th-large", path: "/res-long-time", children: [], displayFlag: true}
],
// 菜单栏
menus = [
  {sort: 2, name: "基础模块", path: "modules", icon: "fa fa-database", displayFlag: true, children: modules}
];

export default menus;
