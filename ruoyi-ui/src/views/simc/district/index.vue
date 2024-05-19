<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="行政区域名称" prop="districtName">
        <el-input
          v-model="queryParams.districtName"
          placeholder="请输入行政区域名称"
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
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="toggleExpandAll"
        >展开/折叠</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getTableList"></right-toolbar>
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="tableDataList"
      row-key="districtId"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="districtName" label="行政区域名称" width="260"></el-table-column>
      <el-table-column prop="districtType" label="行政区域类型" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.simc_district_type" :value="scope.row.districtType"/>
        </template>
      </el-table-column>
      <el-table-column prop="sortId" label="排序" width="200"></el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-if="scope.row.districtType == 3 || scope.row.districtType == 4 || scope.row.districtType == 5"
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
          >新增</el-button>
          <el-button
            v-if="scope.row.districtType == 6"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改部门对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="620px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24" v-if="form.parentDistrictId !== 0">
            <el-form-item label="上级行政区域" prop="parentId">
              <treeselect v-model="form.parentDistrictId" :options="districtOptions" :normalizer="normalizer" placeholder="选择上级行政区域" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="行政区域名称" prop="districtName">
              <el-input v-model="form.districtName" placeholder="请输入行政区域名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="sortId">
              <el-input-number v-model="form.sortId" controls-position="right" :min="0" />
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
import { listDistrict, listDistrictExcludeChild, getDistrict, addDistrict, updateDistrict, delDistrict} from "@/api/simc/district";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "District",
  dicts: ['simc_district_type'],
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 表格树数据
      tableDataList: [],
      // 部门树选项
      districtOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 查询参数
      queryParams: {
        districtName: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getTableList();
  },
  methods: {
    /** 查询部门列表 */
    getTableList() {
      this.loading = true;
      listDistrict(this.queryParams).then(response => {
        this.tableDataList = this.handleTree(response.data, "districtId", "parentDistrictId");
        this.loading = false;
      });
    },
    /** 转换部门数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.districtId,
        label: node.districtName,
        children: node.children
      };
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        districtId: undefined,
        parentDistrictId: undefined,
        districtName: undefined,
        sortId: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getTableList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      if (row != undefined) {
        this.form.parentDistrictId = row.districtId;
      }
      this.open = true;
      this.title = "添加行政区域";
      listDistrict().then(response => {
        this.districtOptions = this.handleTree(response.data, "districtId", "parentDistrictId");
      });
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getDistrict(row.districtId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改行政区域";
        listDistrictExcludeChild(row.districtId).then(response => {
          this.districtOptions = this.handleTree(response.data, "districtId", "parentDistrictId");
          if (this.districtOptions.length == 0) {
            const noResultsOptions = { districtId: this.form.parentDistrictId, districtName: this.form.parentDistrictName, children: [] };
            this.districtOptions.push(noResultsOptions);
          }
        });
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.districtId != undefined) {
            updateDistrict(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getTableList();
            });
          } else {
            addDistrict(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getTableList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm('是否确认删除名称为"' + row.districtName + '"的数据项？').then(function() {
        return delDistrict(row.districtId);
      }).then(() => {
        this.getTableList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>
