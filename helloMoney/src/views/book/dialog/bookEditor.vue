<template>
  <el-dialog
    :visible.sync="isShow"
    :title="title"
    center
    @close = "closeDialog"
    @open = "openDialog"
  >
    <el-card>
      <el-form ref="addData" :model="addData" :rules="rules" label-width="100px" label-position="left">
        <el-row :gutter="30">
          <el-col :span="12">
            <el-form-item prop="bookName" label="书名:">
              <el-input v-model="addData.bookName" placeholder="书名" :disabled="isDisabled" @blur="checkBookName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="author" label="作者">
              <el-input v-model="addData.author"  placeholder="作者"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="30">
          <el-col :span="12">
            <el-form-item prop="inventory" label="库存">
              <el-input v-model="addData.inventory" placeholder="库存" ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="price" label="单价">
              <el-input v-model="addData.price"  placeholder="单价"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
    <div style="text-align:right;margin:20px 0">
        <el-button size="mini" @click="closeDialog">取消</el-button>
        <el-button type="primary" size="mini" @click="addBook">确认</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {commonAPI} from '@/api/commonAPI'
export default {
  props:{
    option:Object
  },
  data(){
    const isNumber = (rule, value, callback) => {
      if (!Number(value)) {
        callback(new Error('只能为数字'))
      }else{
        callback()
      }
    }
    return{
      addData:{
        bookName:'',
        author:'',
        inventory:Number,
        price:Number
      },
      bookName:[],
      title:'',
      flag:false,
      formQuery:{
        bookName:'',
        author:'',
        pageNum:1,
        pageSize:10
      },
      isDisabled:false,
      rules:{
        bookName: [{required:true,message:'请输入书名',trigger:'blur'}],
        inventory:[{required:true,message:'请输入库存数',trigger:'blur'},
                  {trigger:'blur',validator: isNumber}],
        price:[{required:true,message:'请输入单价',trigger:'blur'},
              {trigger:'blur',validator: isNumber}],
      }
    }
  },
  computed:{
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
    openDialog(){
      this.title =this.option.title
      this.isDisabled = this.option.isDisabled
      this.addData = JSON.parse(JSON.stringify(this.option.tableData))
    },
    checkBookName(){
      commonAPI('queryBookList',this.formQuery)
      .then(res => {
        for(var i=0;i<res.data.data.rows.length;i++){
          if(this.addData.bookName === res.data.data.rows[i].bookName){
            this.$message.error('书名已存在');
            this.flag = true;
            break
          }
        }
      })
    },
    closeDialog(){
      this.$refs['addData'].resetFields();
      this.option.isShow = false;
      this.flag = false
      this.$emit('refreshTable');
    },
    addBook(){
      if(this.flag === false){
        this.$refs.addData.validate(valid => {
          if (valid) {
            if(this.isDisabled === false){
              commonAPI('addBook',this.addData)
              .then(res => {
                this.option.isShow = false
                if(res.data.data ==='OK'){
                  this.$message({
                    showClose: true,
                    message: '添加成功',
                    type: 'success'
                  });
                } else {
                  this.$message.error('添加失败，请重试');
                }
              })
            }else{
              commonAPI('editBook',this.addData)
              .then(res => {
                this.option.isShow = false
                if(res.data.data ==='OK'){
                  this.$message({
                    showClose: true,
                    message: '修改成功',
                    type: 'success'
                  });
                } else {
                  this.$message.error('修改失败，请重试');
                }
              })
            }
          }
        })
      }
    }
  }
}
</script>

<style>

</style>
