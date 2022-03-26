<template>
	<div class="img-verify">
		<canvas ref="verify" :width="width" :height="height" @click="handleDraw"></canvas>
	</div>
</template>

<script type="text/ecmascript-6">
import { reactive, onMounted, ref, toRefs } from 'vue'
export default {
  setup() {
    const verify = ref(null)
    const state = reactive({
      pool: 'ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890',
      width: 120,
      height: 40,
      imgCode: ''
    })
    onMounted(() => {
      state.imgCode = draw()
    })

    const handleDraw = () => {
      state.imgCode = draw()
    }

    const randomNum = (min, max) => {
      return parseInt(Math.random() * (max - min) + min)
    }
    const randomColor = (min, max) => {
      const r = randomNum(min, max)
      const g = randomNum(min, max)
      const b = randomNum(min, max)
      return `rgb(${r},${g},${b})`
    }

    const draw = () => {
      const ctx = verify.value.getContext('2d')
      ctx.fillStyle = randomColor(180, 230)
      ctx.fillRect(0, 0, state.width, state.height)
      let imgCode = ''
      for (let i = 0; i < 4; i++) {
        const text = state.pool[randomNum(0, state.pool.length)]
        imgCode += text
        const fontSize = randomNum(18, 40)
        const deg = randomNum(-30, 30)

        ctx.font = fontSize + 'px Simhei'
        ctx.textBaseline = 'top'
        ctx.fillStyle = randomColor(80, 150)

        ctx.save()
        ctx.translate(30 * i + 15, 15)
        ctx.rotate((deg * Math.PI) / 180)
        ctx.fillText(text, -15 + 5, -15)
        ctx.restore()
      }
      for (let i = 0; i < 5; i++) {
        ctx.beginPath()
        ctx.moveTo(randomNum(0, state.width), randomNum(0, state.height))
        ctx.lineTo(randomNum(0, state.width), randomNum(0, state.height))
        ctx.strokeStyle = randomColor(180, 230)
        ctx.closePath()
        ctx.stroke()
      }
      for (let i = 0; i < 40; i++) {
        ctx.beginPath()
        ctx.arc(randomNum(0, state.width), randomNum(0, state.height), 1, 0, 2 * Math.PI)
        ctx.closePath()
        ctx.fillStyle = randomColor(150, 200)
        ctx.fill()
      }
      return imgCode
    }

    return {
      ...toRefs(state),
      verify,
      handleDraw
    }
  }
}
</script>
<style type="text/css">
.img-verify canvas {
	cursor: pointer;
}
</style>
