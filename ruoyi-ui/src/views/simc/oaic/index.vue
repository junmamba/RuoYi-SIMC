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
      <el-form-item label="代领人姓名" prop="surrogateName">
        <el-input
          v-model="queryParams.surrogateName"
          style="width: 220px"
          placeholder="输入代领人姓名关键字进行模糊查询"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="代领人身份证" prop="surrogateIdCardNo">
        <el-input
          v-model="queryParams.surrogateIdCardNo"
          style="width: 220px"
          placeholder="输入代领人身份证进行模糊查询"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="退保时间" prop="cancelTime">
        <el-date-picker
          v-model="cancelTimeRange"
          style="width: 220px"
          value-format="yyyy-MM-dd"
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
          type="primary"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleFileImport"
        >导入
        </el-button>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
          >删除</el-button>
        </el-col>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="tableList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tableDataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="30" align="center"/>
      <el-table-column label="姓名" align="center" prop="residentName"/>
      <el-table-column label="身份证号码" align="center" prop="residentIdCardNo"/>
      <el-table-column label="联系电话" align="center" prop="residentPhone"/>
      <el-table-column label="行政区域" align="center" prop="districtName"/>
      <el-table-column label="金额" align="center" prop="money"/>
      <el-table-column label="代领人姓名" align="center" prop="surrogateName"/>
      <el-table-column label="代领人身份证" align="center" prop="surrogateIdCardNo"/>
      <el-table-column label="开户行" align="center" prop="bank"/>
      <el-table-column label="银行账号" align="center" prop="bankCode"/>
      <el-table-column label="退保时间" align="center" prop="strCancelTime"/>
      <el-table-column label="备注" align="center" prop="remark"/>
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

    <!-- 修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="880px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="身份证号码" prop="residentIdCardNo">
              <el-input v-model="form.residentIdCardNo" placeholder="请输入身份证号码" maxlength="32" style="width: 185px;" :disabled="true"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="姓名" prop="residentName">
              <el-input v-model="form.residentName" placeholder="请输入姓名" maxlength="32" style="width: 185px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系电话" prop="residentPhone">
              <el-input v-model="form.residentPhone" placeholder="请输入电话" maxlength="32" style="width: 185px;"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <el-form-item label="乡镇" prop="residentTownshipDistrictId">
              <el-select v-model="form.residentTownshipDistrictId" placeholder="请选择乡镇" style="width: 185px;" @change="handleChangeTownshipDistrictId">
                <el-option
                  v-for="dict in districtIdOptions"
                  :key="dict.id"
                  :label="dict.label"
                  :value="dict.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="村（社区）" prop="residentVillageDistrictId">
              <el-select v-model="form.residentVillageDistrictId" placeholder="请选择村（社区）" style="width: 185px;">
                <el-option
                  v-for="dict in villageDistrictIdOptions"
                  :key="dict.id"
                  :label="dict.label"
                  :value="dict.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="金额" prop="money">
              <el-input-number v-model="form.money" :min="0" :precision="2" placeholder="请输入金额" maxlength="8" style="width: 185px;" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <el-form-item label="代领人姓名" prop="residentIdCardNo">
              <el-input v-model="form.surrogateName" placeholder="请输入代领人姓名" maxlength="32" style="width: 185px;" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="代领人身份证" prop="residentName">
              <el-input v-model="form.surrogateIdCardNo" placeholder="请输入代领人身份证" maxlength="32" style="width: 185px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="退保时间" prop="cancelTime">
              <el-date-picker v-model="form.cancelTime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" :style="{width: '185px'}" clearable></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <el-form-item label="开户行" prop="bank">
              <el-input v-model="form.bank" placeholder="请输入开户行" maxlength="128" style="width: 185px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="银行账号" prop="fllTime">
              <el-input v-model="form.bankCode" placeholder="请输入征地时间" maxlength="128" style="width: 185px;"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {tableList, getOldAgriculturalInsuranceCancel, operOldAgriculturalInsuranceCancel, delOldAgriculturalInsuranceCancel} from "@/api/simc/oaic";
import {listChaiSangDistrict} from "@/api/simc/district";
import {getToken} from "@/utils/auth";

export default {
  name: "OldAgriculturalInsuranceCancel",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      districtIdOptions: [],
      villageDistrictIdOptions: [],
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
      cancelTimeRange: [],
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
        url: process.env.VUE_APP_BASE_API + "/simc/oaic/importData"
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        districtId: undefined,
        residentName: undefined,
        residentIdCardNo: undefined,
        residentPhone: undefined,
        surrogateName: undefined,
        surrogateIdCardNo: undefined
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
      rules: {
        residentName: [
          {required: true, message: "姓名不能为空", trigger: "blur"}
        ],
        residentPhone: [
          {required: true, message: "联系电话不能为空", trigger: "blur"}
        ],
        residentTownshipDistrictId: [
          {required: true, message: "请选择乡镇", trigger: ['blur', 'change']}
        ],
        residentVillageDistrictId: [
          {required: true, message: "请选择村（社区）", trigger: ['blur', 'change']}
        ],
        money: [
          { required: true, message: "请输入金额", trigger: "blur" }
        ],
      }
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
    /** 查询居民社保档案列表 */
    tableList() {
      this.loading = true;
      tableList(this.addDateRange(this.queryParams, this.cancelTimeRange, 'CancelTime')).then(response => {
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
      this.cancelTimeRange = [];

      this.handleQuery();
    },
    /** 修改按钮操作*/
    handleUpdate(row) {
      this.reset();
      const residentIdCardNo = row.residentIdCardNo || this.ids;
      getOldAgriculturalInsuranceCancel(residentIdCardNo).then(response => {
        if (null == response.data || undefined == response.data) {
          this.$alert('根据身份证号码：' + residentIdCardNo + '查询不到老农保退保档案信息');
        } else {
          this.initVillageDistrictIdOptions(response.data.residentTownshipDistrictId);
          this.form = response.data;
          this.open = true;
          this.title = "修改老农保退保档案";
        }
      });
    },
    /** 导入按钮操作 */
    handleFileImport() {
      this.upload.title = "老农保退保档案信息导入";
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
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          operOldAgriculturalInsuranceCancel(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.tableList();
          });
        }
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.residentIdCardNo)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleChangeTownshipDistrictId(value) {
      this.initVillageDistrictIdOptions(value);
      this.groupDistrictIdOptions = [];
      this.form.residentVillageDistrictId = undefined;
    },
    initVillageDistrictIdOptions(townshipDistrictId) {
      if (townshipDistrictId == undefined || townshipDistrictId <= 0) {
        this.villageDistrictIdOptions = [];
      }
      for (let i = 0; i < this.districtIdOptions.length; i++) {
        if (this.districtIdOptions[i].id == townshipDistrictId) {
          this.villageDistrictIdOptions = this.districtIdOptions[i].children;
          break;
        }
      }
    },
    reset() {
      this.form = {
        residentIdCardNo: undefined,
        residentName: undefined,
        residentPhone: undefined,
        residentTownshipDistrictId: undefined,
        residentVillageDistrictId: undefined,
        money: undefined,
        surrogateIdCardNo: undefined,
        surrogateName: undefined,
        bank: undefined,
        bankCode: undefined,
        cancelTime: undefined,
        remark: undefined
      };
      this.resetForm("form");
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const residentIdCardNos = row.residentIdCardNo || this.ids;
      this.$modal.confirm('是否确认身份证号码："' + residentIdCardNos + '"有关的农保退保档案数据项？').then(function() {
        return delOldAgriculturalInsuranceCancel(residentIdCardNos);
      }).then(() => {
        this.tableList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>
