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
      <el-form-item label="缴费时间" prop="payTime">
        <el-date-picker
          v-model="payTimeRange"
          style="width: 220px"
          value-format="yyyy-MM"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="缴费档次" prop="payLevel">
        <el-select v-model="queryParams.payLevel" style="width: 220px" clearable>
          <el-option
            v-for="dict in dict.type.simc_pay_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="state">
        <el-select v-model="queryParams.state" style="width: 220px" clearable>
          <el-option
            v-for="dict in dict.type.simc_resident_old_land_losing_state"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="退费状态" prop="returnFeeState">
        <el-select v-model="queryParams.returnFeeState" style="width: 220px" clearable>
          <el-option
            v-for="dict in dict.type.simc_return_fee_state"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
      <el-table-column label="缴费时间" align="center" prop="strPayTime" />
      <el-table-column label="缴费档次" align="center" prop="payLevel">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.simc_pay_level" :value="scope.row.payLevel"/>
        </template>
      </el-table-column>
      <el-table-column label="缴费金额" align="center" prop="payMoney"/>
      <el-table-column label="开户行" align="center" prop="bank"/>
      <el-table-column label="银行卡号" align="center" prop="bankCode"/>
      <el-table-column label="首次领取时间" align="center" prop="strTheFirstReceiveTime"/>
      <el-table-column label="状态" align="center" prop="state">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.simc_resident_old_land_losing_state" :value="scope.row.state"/>
        </template>
      </el-table-column>
      <el-table-column label="退出时间" align="center" prop="strQuitTime"/>
      <el-table-column label="退费状态" align="center" prop="returnFeeState">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.simc_return_fee_state" :value="scope.row.returnFeeState"/>
        </template>
      </el-table-column>
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
import {tableList} from "@/api/simc/roll";
import {listChaiSangDistrict} from "@/api/simc/district";
import {getToken} from "@/utils/auth";

export default {
  name: "ResidentOldLandLosing",
  dicts: ['simc_sex', 'simc_pay_level', 'simc_resident_old_land_losing_state', 'simc_return_fee_state'],
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
      payTimeRange: [],
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
        url: process.env.VUE_APP_BASE_API + "/simc/roll/importData"
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        districtId: undefined,
        residentName: undefined,
        residentSex: undefined,
        residentIdCardNo: undefined,
        residentPhone: undefined,
        payLevel: undefined,
        state: undefined,
        returnFeeState: undefined
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
      tableList(this.addDateRange(this.queryParams, this.payTimeRange, 'PayTime')).then(response => {
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
      this.payTimeRange = [];

      this.handleQuery();
    },
    /** 导入按钮操作 */
    handleFileImport() {
      this.upload.title = "老失地档案导入";
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
