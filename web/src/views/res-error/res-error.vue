<template>
  <section>
    <!--#region 搜索条-->

    <xt-search @search="handleSearch">
        <el-date-picker v-model="filters.startDate" type="date" placeholder="开始时间" size="small" class="search-box__input" :editable="false"></el-date-picker>
        <el-date-picker v-model="filters.endDate" type="date" placeholder="结束时间" size="small" class="search-box__input" :editable="false"></el-date-picker>
        <el-input v-model="filters.url" placeholder="接口" size="small" class="search-box__input" clearable></el-input>
        <el-input v-model="filters.original" placeholder="原始信息" size="small" class="search-box__input" clearable></el-input>
        <!-- <el-input v-model="filters.para" placeholder="参数" size="small" class="search-box__input" clearable></el-input>
        <el-input v-model="filters.original" placeholder="原始信息" size="small" class="search-box__input" clearable></el-input> -->
        <!-- <template slot="actions">
          <el-button size="small" class="search-box__button" type="primary" icon="plus" @click="handleAdd">新增</el-button>
        </template> -->
      </xt-search>

    <!--#endregion-->

    <!--#region 页面主要列表-->

    <!--列表-->

    <el-table :data="resErrorList" highlight-current-row v-loading="loading.listLoading" style="width: 100%;" stripe border>
      <el-table-column align="left" header-align="center" type="index" width="56" label="序号">
        <template slot-scope="scope">
          {{(pageInfo.pageIndex-1)*pageInfo.pageSize+scope.$index+1}}
        </template>
      </el-table-column>
      <el-table-column align="left" header-align="center" prop="resDate" label="日期" width="200" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="left" header-align="center" prop="url" label="接口"   width="350" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="left" header-align="center" prop="para" label="参数" width="300" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="left" header-align="center" prop="original" label="原始信息" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="left" header-align="center" label="操作" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="warning" @click="handleEdit(scope.row)">查看</el-button>
          <!-- <el-button size="mini" type="danger" @click="deleteData(scope.row)">删除</el-button> -->
        </template>
      </el-table-column>
    </el-table>
    <!--#endregion-->

    <!--#region 工具条-->

    <!--工具条-->

    <el-row>
      <el-col :span="24" class="toolbar">
        <el-pagination
          style="float:right;"
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageInfo.pageIndex"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="pageInfo.pageSize"
          layout="sizes, prev, pager, next"
          :total="pageInfo.count">
        </el-pagination>
      </el-col>
    </el-row>

    <!--#endregion-->

    <!--#region 新增修改页面-->

    <!--新增界面-->

    <el-dialog width="95%" top="20px" :title="formData.id==0?'新增':'修改'" :visible.sync="dialog.formDataVisible" :close-on-click-modal="false" :modal-append-to-body="false">
      <el-form :model="formData" label-width="100px" :rules="formRules" ref="formData">
        <el-form-item label="日期" prop="resDate">
          <el-input v-model="formData.resDate" auto-complete="off" :maxlength="50" placeholder="请输入日期" size="small"></el-input>
        </el-form-item>
        <el-form-item label="接口" prop="url">
          <el-input v-model="formData.url" auto-complete="off" :maxlength="255" placeholder="请输入接口" size="small"></el-input>
        </el-form-item>
        <el-form-item label="参数" prop="para">
          <el-input v-model="formData.para" auto-complete="off" :maxlength="500" placeholder="请输入参数" size="small"></el-input>
        </el-form-item>
        <el-form-item label="原始信息" prop="original">
          <el-input v-model="formData.original" auto-complete="off" :maxlength="8000" placeholder="请输入原始信息" size="small"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="handleCancel">取消</el-button>
        <el-button type="primary" @click.native="submitData" :loading="loading.addLoading">提交</el-button>
      </div>
    </el-dialog>

    <!--#endregion-->

  </section>
</template>

<script>

/*#region 导入函数*/

import PagePlugIn from "../../mixins/page-plug-in";
import {ResErrorAPI} from "../../modules";

/*#endregion*/

export default {
  name: "ResError",
  methods: {

    /*#region 自定义函数(组件change事件等)*/

    /*#endregion*/

    /*#region 数据提交事件*/

    //提交事件

    submitData() {
      this.$refs.formData.validate((valid) => {
        if (valid) {
          const params = JSON.parse(JSON.stringify(this.formData));
          if (params.id === 0) {
            this.loading.addLoading = true;
            ResErrorAPI.insertResError(params).then(({data}) => {
              if (data.code === 1) {
                this.$refs.formData.resetFields();
                this.getMainList();
                this.dialog.formDataVisible = false;
              }
              this.loading.addLoading = false;
            });
        } else {
          this.loading.addLoading = true;
          ResErrorAPI.updateResError(params).then(({data}) => {
            if (data.code === 1) {
              this.$refs.formData.resetFields();
              this.getMainList();
              this.dialog.formDataVisible = false;
            }
            this.loading.addLoading = false;
          });
          }
        }
      });
    },

    //删除

    deleteData(row) {
      this.$confirm("删除之后将无法恢复, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        ResErrorAPI.deleteResError({id: row.id}).then(({data}) => {
          if (data.code === 1) {
            this.getMainList();
          }
        });
      });
    },


    /*#endregion*/

    /*#region 操作函数(handle)*/

    //修改

    handleEdit(row) {
      this.$refs.formData && this.$refs.formData.resetFields();
      this.formData = JSON.parse(JSON.stringify(row));
      this.dialog.formDataVisible = true;
    },
    handleCancel() {
      this.dialog.formDataVisible = false;
    },
    handleAdd() {
      this.$refs.formData && this.$refs.formData.resetFields();
      this.formData = ResErrorAPI.init();
      this.dialog.formDataVisible = true;
    },

    /*#endregion*/

    /*#region 获取数据*/

    getMainList() {
      const params = {
        ...this.getParams()
      };
      ResErrorAPI.listResError(params).then(({data, list}) => {
        this.resErrorList = list;
        this.pageInfo.pageIndex = data.data ? data.data.currentPage : 1;
        this.pageInfo.count = data.data ? data.data.count : 0;
      });
    }

    /*#endregion*/

  },

  /*#region 生命周期函数(created、mount、等)*/

  created() {
    this.getMainList();
  },

  /*#endregion*/

  /*#region 注册绑定数据*/

  mixins: [PagePlugIn],
  data() {
    return {
      filters: ResErrorAPI.init(),
      formData: ResErrorAPI.init(),
      formRules: {
      },
      dialog: { formDataVisible: false },
      loading: { addLoading: false, listLoading: false },
      resErrorList: []
    };
  }

  /*#endregion */

};
</script>

<style scoped>

</style>
