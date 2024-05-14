<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="姓名" prop="headResidentName">
        <el-input
          v-model="queryParams.headResidentName"
          style="width: 220px"
          placeholder="输入户主姓名关键字进行模糊查询"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="身份证号码" prop="familyNo">
        <el-input
          v-model="queryParams.familyNo"
          style="width: 220px"
          placeholder="输入户主身份证号码进行模糊查询"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="征地项目" prop="projectName">
        <el-input
          v-model="queryParams.projectName"
          style="width: 220px"
          placeholder="输入征地项目关键字进行模糊查询"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="tableList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tableDataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="30" align="center"/>
      <el-table-column label="户主身份证号码" align="center" prop="familyNo"/>
      <el-table-column label="户主姓名" align="center" prop="headResidentName" />
      <el-table-column label="被征地时家庭人数" align="center" prop="fllFamilyMemberNumber" />
      <el-table-column label="被征地时符合参保条件的人数" align="center" prop="fllEligibleMemberNumber" />
      <el-table-column label="原耕地面积（亩）" align="center" prop="originalAgriculturalAcreage" />
      <el-table-column label="共被征地面积（亩）" align="center" prop="fllTotalAgriculturalAcreage" />
      <el-table-column label="现在承包耕地面积（亩）" align="center" prop="currentAgriculturalAcreage" />
      <el-table-column label="现人均耕地面积（亩）" align="center" prop="perAgriculturalAcreage" />
      <el-table-column label="征地项目" align="center" prop="projectName" />
      <el-table-column label="市级征地项目" align="center" prop="projectIsCityLevel" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.simc_is_city_level" :value="scope.row.projectIsCityLevel"/>
        </template>
      </el-table-column>
      <el-table-column label="征地时间" align="center" prop="fllTime" />
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="tableList"
    />
  </div>
</template>

<script>
import {tableList} from "@/api/simc/fll";

export default {
  name: "SimcFamilyLandLosing",
  dicts: ['simc_is_city_level'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 居民社保表格数据
      tableDataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        headResidentName: undefined,
        familyNo: undefined,
        projectName: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.tableList();
  },
  methods: {
    /** 查询居民基本养老保险补贴列表 */
    tableList() {
      this.loading = true;
      tableList(this.queryParams).then(response => {
        this.tableDataList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.tableList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.districtId = [];
      this.resetForm("queryForm");
      this.subsidyTimeRange = [];

      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.postId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    }
  }
};
</script>
