import ResCount from "./res-count/res-count.vue";
import ResDailyCount from "./res-daily-count/res-daily-count.vue";
import ResError from "./res-error/res-error.vue";
import ResLog from "./res-log/res-log.vue";
import ResLongTime from "./res-long-time/res-long-time.vue";

export const components = [
        {title: "访问总计", name: "ResCount", component: ResCount, path: "/res-count"},
        {title: "访问日计", name: "ResDailyCount", component: ResDailyCount, path: "/res-daily-count"},
        {title: "异常日志", name: "ResError", component: ResError, path: "/res-error"},
        {title: "调试日志", name: "ResLog", component: ResLog, path: "/res-log"},
        {title: "耗时接口", name: "ResLongTime", component: ResLongTime, path: "/res-long-time"}
    ];

