<template>
  <div class="verification-code">
    <canvas id="canvas"
            width="80"
            height="46"
            @click="drawPic"></canvas>
  </div>
</template>

<script>
export default {
  name: "verification-code",
  data() {
    return {};
  },
  mounted() {
    this.drawPic();
  },
  methods: {
    randomNum(min, max) {
      return Math.floor(Math.random() * (max - min) + min);
    },
    randomColor(min, max) {
      var r = this.randomNum(min, max);
      var g = this.randomNum(min, max);
      var b = this.randomNum(min, max);
      return "rgb(" + r + "," + g + "," + b + ")";
    },
    drawPic() {
      var canvas = document.getElementById("canvas");
      var width = canvas.width;
      var height = canvas.height;
      var ctx = canvas.getContext("2d");
      ctx.textBaseline = "bottom";

      /**绘制背景色**/
      ctx.fillStyle = this.randomColor(10, 30); //颜色若太深可能导致看不清
      // ctx.fillStyle = "rgba(0,0,0,0.2)";
      ctx.fillRect(0, 0, width, height);
      /**绘制文字**/
      var finallStr = "";
      var str = "1234567890";
      for (var i = 0; i < 4; i++) {
        var txt = str[this.randomNum(0, str.length)];
        ctx.fillStyle = this.randomColor(180, 240); //随机生成字体颜色
        ctx.font = this.randomNum(25, 30) + "px SimHei"; //随机生成字体大小
        var x = 8 + i * 18;
        var y = this.randomNum(40, 45);
        var deg = this.randomNum(-15, 15);
        //修改坐标原点和旋转角度
        ctx.translate(x, y);
        ctx.rotate(deg * Math.PI / 160);
        ctx.fillText(txt, 0, 0);
        //恢复坐标原点和旋转角度
        ctx.rotate(-deg * Math.PI / 160);
        ctx.translate(-x, -y);
        // console.log("str", txt);
        finallStr += txt;
      }
      this.$emit('rendom', finallStr)

      /**绘制干扰线**/
      // for (var i = 0; i <3; i++) {
      //   ctx.strokeStyle = this.randomColor(40, 180);
      //   ctx.beginPath();
      //   ctx.moveTo(this.randomNum(0, width), this.randomNum(0, height));
      //   ctx.lineTo(this.randomNum(0, width), this.randomNum(0, height));
      //   ctx.stroke();
      // }

      /**绘制干扰点**/
      for (var i = 0; i < 30; i++) {
        ctx.fillStyle = this.randomColor(0, 255);
        ctx.beginPath();
        ctx.arc(
          this.randomNum(0, width),
          this.randomNum(0, height),
          1,
          0,
          2 * Math.PI
        );
        ctx.fill();
      }
    }
  }
};
</script>

<style scoped>
#canvas{
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 4px;
}
</style>
