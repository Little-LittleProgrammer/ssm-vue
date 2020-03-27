<template>
  <el-dialog
    :visible.sync="isShow"
    :title="title"
    center
  >
    <el-dialog
        width="30%"
        :visible.sync="innerVisible"
        center
        :show-close = false
        append-to-body
        :close-on-click-modal= false
        :close-on-press-escape = false>
        <span style="margin:30px">{{message}}</span>
      </el-dialog>
    <!-- excel文件选择框 -->
    <upload-excel-component :on-success="handleSuccess" :before-upload="beforeUpload"/>
     <!-- excel文件预览框 -->
    <el-table :data="tableData" border highlight-current-row style="width: 100%;margin-top:20px;">
      <el-table-column v-for="item of tableHeader" :prop="item" :label="item" :key="item"/>
    </el-table>
    <el-button v-show="showFlag" size="mini" type="primary" style="float:right" @click="upload">确认上传</el-button>
  </el-dialog>
</template>

<script>
import UploadExcelComponent from '@/components/UploadExcel/index.vue'
import {commonAPI} from '@/api/commonAPI'
export default {
  name: 'UploadExcel',
  props: {
    option: Object
  },
  computed: {
    isShow: {
      // 修改用户弹窗是否显示
      get: function() {
        return this.option.isShow;
      },
      set: function(newValue) {
        this.option.isShow = newValue;
      }
    }
  },
  components: { UploadExcelComponent },
  data() {
    return {
      tableData: [],
      innerVisible:false,
      showFlag:false,
      tableDataForUpload:[],
      tableHeader: [],
      title:'请选择要导入数据库的表格',
      message:'正在上传，请稍后'
    }
  },
  methods: {
    // 文件读取前执行
    beforeUpload(file) {
      // 取文件大小，限制文件大小超过1mb
      const isLt1M = file.size / 1024 / 1024 < 1
      if (isLt1M) {
        return true
      }
      this.$message({
        message: '上传的Excel文件不能大于1mb.',
        type: 'warning'
      })
      return false
    },
    // 文件读取后执行
    handleSuccess({ results, header }) {
      console.log(results)
      this.showFlag = true
      this.tableData = results
      this.tableDataForUpload = results
      this.tableHeader = header
    },
    upload(){
      this.innerVisible=true
      var jsonKey =[]
      var list = this.tableDataForUpload
      for(var jsonVal in list[0]){
        jsonKey.push(jsonVal);
      }
      //旧key到新key的映射
      var oldkey = {
        [jsonKey[0]]: "bookName",
        [jsonKey[1]]: "author",
        [jsonKey[2]]: "inventory",
        [jsonKey[3]]: "price"
      };
      for(var i = 0;i < list.length;i++){
        var obj = this.tableDataForUpload[i];
        for(var key in obj){
          var newKey = oldkey[key];
          if(newKey){
            obj[newKey] = obj[key];
            delete obj[key];
          }
        }
      }
      commonAPI('batchAddBook',{bookList:this.tableDataForUpload}).then(res=>{
        if(res.data.status = '200'){
          this.message = '上传成功'
          setTimeout(()=>{
            this.innerVisible = false
            this.option.isShow = false
            this.tableData = []
            this.tableDataForUpload = []
            this.tableHeader = []
          },1000)
        }
      })
    }
  }
}
</script>
