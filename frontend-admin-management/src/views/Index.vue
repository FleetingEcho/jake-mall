<template>
	<el-card class="introduce">
		<div class="order">
			<el-card class="order-item">
				<template #header>
					<div class="card-header">
						<span>Number of orders today</span>
					</div>
				</template>
				<div class="item">1888</div>
			</el-card>
			<el-card class="order-item">
				<template #header>
					<div class="card-header">
						<span>Today's Daily Activity</span>
					</div>
				</template>
				<div class="item">36271</div>
			</el-card>
			<el-card class="order-item">
				<template #header>
					<div class="card-header">
						<span>Conversion rate</span>
					</div>
				</template>
				<div class="item">30%</div>
			</el-card>
		</div>
		<div id="zoom"></div>
	</el-card>
</template>

<script>
import { onMounted, onUnmounted } from 'vue'

let myChart = null
export default {
	name: 'Index',
	setup() {
		onMounted(() => {
			if (window.echarts) {
				myChart = window.echarts.init(document.getElementById('zoom'))

				const option = {
					title: {
						text: 'System Line Chart',
					},
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'cross',
							label: {
								backgroundColor: '#6a7985',
							},
						},
					},
					legend: {
						data: [
							'Add Register ',
							'Paid users',
							'Active users',
							'Number of orders',
							'Total revenue for the day',
						],
					},
					toolbox: {
						feature: {
							saveAsImage: {},
						},
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true,
					},
					xAxis: [
						{
							type: 'category',
							boundaryGap: false,
							data: [
								'2022-03-11',
								'2022-03-12',
								'2022-03-13',
								'2022-03-14',
								'2022-03-15',
								'2022-03-16',
								'2022-03-17',
							],
						},
					],
					yAxis: [
						{
							type: 'value',
						},
					],
					series: [
						{
							name: 'Add Register ',
							type: 'line',
							stack: 'Total amount',
							areaStyle: {},
							emphasis: {
								focus: 'series',
							},
							data: [120, 132, 101, 134, 90, 230, 210],
						},
						{
							name: 'Paid users',
							type: 'line',
							stack: 'Total amount',
							areaStyle: {},
							emphasis: {
								focus: 'series',
							},
							data: [220, 182, 191, 234, 290, 330, 310],
						},
						{
							name: 'Active users',
							type: 'line',
							stack: 'Total amount',
							areaStyle: {},
							emphasis: {
								focus: 'series',
							},
							data: [150, 232, 201, 154, 190, 330, 410],
						},
						{
							name: 'Number of orders',
							type: 'line',
							stack: 'Total amount',
							areaStyle: {},
							emphasis: {
								focus: 'series',
							},
							data: [320, 332, 301, 334, 390, 330, 320],
						},
						{
							name: 'Total revenue for the day',
							type: 'line',
							stack: 'Total amount',
							label: {
								show: true,
								position: 'top',
							},
							areaStyle: {},
							emphasis: {
								focus: 'series',
							},
							data: [820, 932, 901, 934, 1290, 1330, 1320],
						},
					],
				}

				myChart.setOption(option)
			}
		})
		onUnmounted(() => {
			myChart.dispose()
		})
	},
}
</script>

<style scoped>
.introduce .order {
	display: flex;
	margin-bottom: 50px;
}
.introduce .order .order-item {
	flex: 1;
	margin-right: 20px;
}
.introduce .order .order-item:last-child {
	margin-right: 0;
}
#zoom {
	min-height: 300px;
}
</style>
