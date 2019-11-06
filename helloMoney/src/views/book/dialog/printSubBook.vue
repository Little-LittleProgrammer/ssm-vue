<template>
   <el-dialog
    :visible.sync="isShow"
    title="打印已借阅书籍"
    center
    @close ="closeDialog"
    @open = "openDialog"
  >
    <el-card style="width:100%" >
      <el-button v-print="'#printMe'"><i class="el-icon-printer"></i>&nbsp;打印</el-button><br><br>
      <div id ='printMe' style="margin:auto">
        <el-table v-loading="loading" :data="tableSubData" stripe border size="mini">
          <el-table-column align="center" label="编号" prop="bId" :sortable="true" :sort-method="sortSubBook"></el-table-column>
          <el-table-column align="center" label="书名" prop="bookName"></el-table-column>
          <el-table-column align="center" label="作者" prop="author"></el-table-column>
          <el-table-column align="center" label="单价" prop="price"></el-table-column>
        </el-table>
      </div>
    </el-card>
  </el-dialog>
</template>

<script>
import {commonAPI} from '@/api/commonAPI'
export default {
  props: {
    option: Object
  },
  data(){
    return {
      tableData:[],
      tableSubData:[],
      loading:false,
    }
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
  methods:{
    getData(){
      this.loading =true
      commonAPI('querySub',{uId:this.$store.state.uId,bookType:1})
      .then(res =>{
        if(res.data.data.rows.length === 0){
          this.loading =false
        }
        for(var i=0;i<res.data.data.rows.length;i++){
          this.tableData.push(res.data.data.rows[i])
          var j = 0
          commonAPI('queryBookList',{bId:res.data.data.rows[i].bId,pageNum:1,pageSize: 10})
          .then(res1 =>{
            Object.assign(this.tableData[j], res1.data.data.rows[0]);
            j++
            if( j === i){
              this.loading =false
              this.tableSubData =this.tableData
              this.total =this.tableSubData.length
              this.pageSize =5
            }
          })
        }
      })
    },
    openDialog(){
      // this.tableData = JSON.parse(JSON.stringify(this.option.tableData))
      this.getData()
    },
    sortSubBook(a,b){
      return a.bookType-b.bookType
    },
    closeDialog(){
      this.tableData=[]
      this.tableSubData =[]
      this.option.isShow = false;
    }
  }
}
</script>

<style>

</style>
