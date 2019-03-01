import * as basics from "../views/index";

const compConfig = (modules) => modules.components.map((component, index) => ({
    sort: index + 2,
    title: component.title,
    name: component.name,
    path: component.path,
    icon: "fa fa-bookmark",
    displayFlag: true,
    component: component.component,
    meta: {title: component.title},
    children: [],
    ...component
  })),
  basicRoutes = compConfig(basics);

export default {
  basicRoutes
};
