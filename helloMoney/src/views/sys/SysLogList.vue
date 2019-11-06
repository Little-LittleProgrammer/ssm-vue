<template>
  <el-container>
    <el-header style="background-color:white">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="mini">
        <el-form-item label="访问时间">
          <el-date-picker class="dateStyle" v-model="formInline.beginTime" value-format="yyyy-MM-dd HH:mm:ss" @change="dataFormat" type="datetime" placeholder="选择日期时间"></el-date-picker>
          至&nbsp;<el-date-picker class="dateStyle" v-model="formInline.endTime" value-format="yyyy-MM-dd HH:mm:ss" @change="dataFormat" type="datetime" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="btn" @click="onSubmit"><i class="el-icon-search"></i>查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="danger" class="btn" @click="resetting"><i class="el-icon-refresh"></i>重置</el-button>
        </el-form-item>
      </el-form>
    </el-header>
    <el-main class="elMain">
      <el-table v-loading="loading" :data="tableData" height="100%" stripe border size="mini">
        <el-table-column align="center" label="访问时间" prop="accessDate" width="140"></el-table-column>
        <el-table-column align="center" label="请求方式" prop="requestType" width="90"></el-table-column>
        <el-table-column align="center" label="请求接口" prop="accessInterface" width="280"></el-table-column>
        <el-table-column header-align="center" align="left" label="接口参数" width="90">
          <template slot-scope="scope" v-if="scope.row.interfaceParams">
            <el-popover v-if="scope.row.interfaceParams.length>10" placement="left-end" width="200" trigger="hover"
              :content="scope.row.interfaceParams">
              <span slot="reference" style="text-overflow: ellipsis;white-space:nowrap;overflow: hidden;">{{scope.row.interfaceParams}}</span>
            </el-popover>
            <span v-else>{{scope.row.interfaceParams}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="访问来源" prop="accessSource" width="263"></el-table-column>
        <el-table-column align="center" label="访问IP" prop="accessIp" width="130"></el-table-column>
        <el-table-column align="center" label="执行时长" prop="executeTime" width="90"></el-table-column>
      </el-table>
    </el-main>
    <el-footer>
      <div style="padding: 15px 0;text-align: right;">
        <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="formInline.pageNum"
          :page-sizes="pageSizes" :page-size="formInline.pageSize" layout="total, sizes, prev, pager, next, jumper"
          :total="pageTotal">
        </el-pagination>
      </div>
    </el-footer>

  </el-container>
</template>

<script>
  import {formatDate} from '@/utils/index';
  import {commonAPI} from '@/api/commonAPI';
  export default {
    data() {
      return {
        tableData: [],
        loading: false,
        formInline: {
          pageNum: 1,
          pageSize: 10,
          beginTime: new Date(new Date().getTime()-3600*1000*24*7),
          endTime: new Date()
        },
        pageSizes: [5, 10, 15, 20],
        pageTotal: 0
      }
    },
    created() {
      this.formInline.beginTime=formatDate(this.formInline.beginTime,true);
      this.formInline.endTime=formatDate(this.formInline.endTime,true);
      this.getData();
    },
    methods: {
      getData() {
        this.loading = true,
          commonAPI('sysLogList',this.formInline)
          .then(res => {
            this.loading = false;
            this.tableData = res.data.data.rows;
            this.pageTotal = res.data.data.total;
          })
      },
      dataFormat(val){
        console.log(val);
      },
      onSubmit() {//查询
        this.getData();
      },
      resetting() {//重置
        this.formInline.beginTime=formatDate(new Date(new Date().getTime()-3600*1000*24*7),true);
        this.formInline.endTime=formatDate(new Date(),true);
        this.getData();
      },
      handleSizeChange(val) {
        //console.log(`每页 ${val} 条`);
        this.formInline.pageSize = val;
        this.formInline.pageNum = 1;
        this.getData();
      },
      handleCurrentChange(val) {
        //console.log(`当前页: ${val}`);
        this.formInline.pageNum = val;
        this.getData();
      }
    }
  }
</script>

<style scoped>
  .dateStyle,.btn {
    margin-top: 18px;
  }

  .elMain {
    height: 418px;
  }
</style>
<style>
  .el-main {
    padding: 5px 10px;
  }

  .el-table th {
    height: 45px;
    font-size: 16px;
    font-family: 微软雅黑;
    font-weight: 500;
    color: darkblue;
  }

  body {
    margin: 0px;
  }
</style>
