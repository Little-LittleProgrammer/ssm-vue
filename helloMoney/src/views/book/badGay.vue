<template>
  <el-card style="margin-top:20px">
    <el-table v-loading="loading" :data="tableData" stripe border size="mini">
       <el-table-column align="center" label="用户名" prop="userName"></el-table-column>
        <el-table-column align="center" label="姓名" prop="uName"></el-table-column>
        <el-table-column align="center" label="征信分" prop="score"></el-table-column>
    </el-table>
  </el-card>
</template>

<script>
import {commonAPI} from '@/api/commonAPI'
export default {
   data(){
    return {
      tableData:[],
      tableDataDome:[],
      loading:false,
      pageTotal:''
    }
  },
  created(){
    this.getData()
  },
  methods:{
    getData(){
      this.loading = true;
      commonAPI('queryUserList',{pageNum:1,pageSize:10})
      .then(res => {
        this.loading = false;
        this.tableDataDome = res.data.data.rows
        this.pageTotal=res.data.data.total;
        for(var i=0;i<this.tableDataDome.length;i++){
          if(this.tableDataDome[i].score<25){
            this.tableData.push(this.tableDataDome[i])
          }
        }
        console.log(this.tableData)
      })
    }
  }
}
</script>

<style>

</style>
