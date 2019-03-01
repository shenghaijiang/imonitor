export default {
  data () {
    return {
      filters: {},
      pageInfo: {pageIndex: 1, pageSize: 10, count: 0}
    };
  },
  methods: {
    // 一页的条数
    handleSizeChange (val) {
      this.pageInfo.pageSize = val;
      this.getMainList();
    },
    // 翻页
    handleCurrentChange (val) {
      this.pageInfo.pageIndex = val;
      this.getMainList();
    },
    // 搜索
    handleSearch () {
      this.pageInfo.pageIndex = 1;
      this.getMainList();
    },
    getParams (params = {}) {
      const oldFilter = {...this.filters, ...params};
      const filter = {},
        newPageInfo = JSON.parse(JSON.stringify(this.pageInfo));
      Object.keys(oldFilter).map((item) => {
        if (oldFilter[item] || oldFilter[item] === false || oldFilter[item] === 0) {
          const lowerItem = item.toLocaleLowerCase();
          if (lowerItem.indexOf("date") >= 0) {
            filter[item] = new Date(oldFilter[item]).Format("yyyy-MM-dd hh:mm:ss");
            if ((lowerItem.indexOf("date") >= 0) && (lowerItem.indexOf("start") >= 0)) {
              filter[item] = new Date(oldFilter[item]).Format("yyyy-MM-dd 00:00:00");
            }
            if ((lowerItem.indexOf("date") >= 0) && (lowerItem.indexOf("end") >= 0)) {
              filter[item] = new Date(oldFilter[item]).Format("yyyy-MM-dd 23:59:59");
            }
          } else {
            if (typeof (oldFilter[item]) === "string") {
              filter[item] = oldFilter[item] ;
            } else {
              filter[item] = oldFilter[item];
            }
          }
        }
      });
      if (filter.id === 0) {
        delete filter.id;
      }
      delete newPageInfo.count;
      return {...filter, ...newPageInfo};
    },
    getMainList () {
      // getMainList
    }
  }
};
