<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="行政区域" prop="districtName">
        <el-cascader
          v-model="queryParams.districtId"
          :options="districtIdOptions"
          :props="districtProps"
          style="width: 220px"
          size="small"
          clearable
        >
        </el-cascader>
      </el-form-item>
      <el-form-item label="姓名" prop="residentName">
        <el-input
          v-model="queryParams.residentName"
          style="width: 220px"
          placeholder="输入姓名关键字进行模糊查询"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="性别" prop="residentSex">
        <el-select v-model="queryParams.residentSex" style="width: 220px" clearable>
          <el-option
            v-for="dict in dict.type.simc_sex"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="身份证号码" prop="residentIdCardNo">
        <el-input
          v-model="queryParams.residentIdCardNo"
          style="width: 220px"
          placeholder="输入身份证号码进行模糊查询"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系电话" prop="residentPhone">
        <el-input
          v-model="queryParams.residentPhone"
          style="width: 220px"
          placeholder="输入联系电话关键字进行模糊查询"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="参保类型" prop="socialInsuranceType">
        <el-select v-model="queryParams.socialInsuranceType" style="width: 220px" clearable>
          <el-option
            v-for="dict in dict.type.simc_insurance_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="补贴年月" prop="subsidyTime">
        <el-date-picker
          v-model="subsidyTimeRange"
          style="width: 220px"
          value-format="yyyy-MM"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleFileImport"
        >导入
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="tableList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tableDataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="30" align="center"/>
      <el-table-column label="姓名" align="center" prop="residentName"/>
      <el-table-column label="性别" align="center" prop="residentSex">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.simc_sex" :value="scope.row.residentSex"/>
        </template>
      </el-table-column>
      <el-table-column label="身份证号码" align="center" prop="residentIdCardNo"/>
      <el-table-column label="联系电话" align="center" prop="residentPhone"/>
      <el-table-column label="行政区域" align="center" prop="districtName"/>
      <el-table-column label="参保类型" align="center" prop="socialInsuranceType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.simc_insurance_type" :value="scope.row.socialInsuranceType"/>
        </template>
      </el-table-column>
      <el-table-column label="年龄" align="center" prop="age"/>
      <el-table-column label="退休状态" align="center" prop="retireState">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.simc_retire_state" :value="scope.row.retireState"/>
        </template>
      </el-table-column>
      <el-table-column label="补贴年月" align="center" prop="strSubsidyTime"/>
      <el-table-column label="补贴金额" align="center" prop="subsidyMoney"/>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="tableList"
    />

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入xls、xlsx格式文件。</span>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {tableList} from "@/api/simc/rsis";
import {listChaiSangDistrict} from "@/api/simc/district";
import {getToken} from "@/utils/auth";

export default {
  name: "ResidentSocialInsurance",
  dicts: ['simc_sex', 'simc_insurance_type', 'simc_retire_state'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      districtIdOptions: [],
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
      // 日期范围
      subsidyTimeRange: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      // 导入参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: {Authorization: "Bearer " + getToken()},
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/simc/rsis/importData"
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        districtId: undefined,
        residentName: undefined,
        residentIdCardNo: undefined,
        residentSex: undefined,
        residentPhone: undefined,
        socialInsuranceType: undefined,
        socialInsuranceState: undefined,
        fllProjectName: undefined
      },
      districtProps: {
        value: 'id',
        expandTrigger: 'hover',
        checkStrictly: true,
        emitPath: false
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    listChaiSangDistrict().then(response => {
      this.districtIdOptions = response.subordinateDistricts;
    });
    ;
    this.tableList();
  },
  methods: {
    /** 查询居民基本养老保险补贴列表 */
    tableList() {
      this.loading = true;
      tableList(this.addDateRange(this.queryParams, this.subsidyTimeRange, 'SubsidyTime')).then(response => {
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
    /** 导入按钮操作 */
    handleFileImport() {
      this.upload.title = "居民社保基本养老保险补贴信息导入";
      this.upload.open = true;
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", {dangerouslyUseHTMLString: true});
      this.tableList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
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
