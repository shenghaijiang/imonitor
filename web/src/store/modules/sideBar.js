const sideBar = {
  state: {
    isCollapse: false
  },
  mutations: {
    CHANGE_ISCOLLAPSE: (state) => {
      state.isCollapse = !state.isCollapse;
    },
    DEL_MENU_LIST: (state) => {
      state.menuList = [];
      state.allMenuList = [];
    },
    DEL_MENU_OPERATION: (state) => {
      state.menuOperation = [];
    }
  },
  actions: {
    changeIsCollapse({commit}) {
      commit("CHANGE_ISCOLLAPSE");
    },
    delMenuList({ commit, state }) {
      return new Promise((resolve) => {
        commit("DEL_MENU_LIST");
        resolve([...state.menuList, ...state.allMenuList]);
      });
    },
    delMenuOperation({ commit, state }) {
      return new Promise((resolve) => {
        commit("DEL_MENU_OPERATION");
        resolve([...state.menuOperation]);
      });
    }
  }
};

export default sideBar;
