<template>
	<el-card class="order-container">
		<div class="data">
			<el-card class="data-item" shadow="hover">
				<template #header>
					<div class="card-header">
						<span>Order status</span>
					</div>
				</template>
				<div>
					{{ data.orderStatusString }}
				</div>
			</el-card>
			<el-card class="data-item" shadow="hover">
				<template #header>
					<div class="card-header">
						<span>CreateTime</span>
					</div>
				</template>
				<div>
					{{ data.createTime }}
				</div>
			</el-card>
			<el-card class="data-item" shadow="hover">
				<template #header>
					<div class="card-header">
						<span>Order number</span>
					</div>
				</template>
				<div>
					{{ data.orderNo }}
				</div>
			</el-card>
		</div>
		<el-table :data="tableData" tooltip-effect="dark" style="width: 100%">
			<el-table-column label="Product Image">
				<template #default="scope">
					<img
						style="width: 100px"
						:key="scope.row.goodsId"
						:src="$filters.prefix(scope.row.goodsCoverImg)"
						alt="Product cover image"
					/>
				</template>
			</el-table-column>
			<el-table-column prop="goodsId" label="Product ID"> </el-table-column>
			<el-table-column prop="goodsName" label="Product Name"></el-table-column>
			<el-table-column prop="goodsCount" label="Product count"> </el-table-column>
			<el-table-column prop="sellingPrice" label="price"> </el-table-column>
		</el-table>
	</el-card>
</template>

<script>
import { onMounted, reactive, toRefs } from 'vue'
import { useRoute } from 'vue-router'
import axios from '@/utils/axios'
export default {
	name: 'OrderDetail',
	setup() {
		const route = useRoute()
		const { id } = route.query
		const state = reactive({
			data: {},
			tableData: [],
		})
		onMounted(() => {
			axios.get(`/orders/${id}`).then((res) => {
				console.log(res)
				state.data = res
				state.tableData = res.newBeeMallOrderItemVOS
			})
		})

		return {
			...toRefs(state),
		}
	},
}
</script>

<style scoped>
.data {
	display: flex;
	margin-bottom: 50px;
}
.data .data-item {
	flex: 1;
	margin: 0 10px;
}
.el-table {
	border: 1px solid #ebeef5;
	border-bottom: none;
}
.has-gutter th {
	border-right: 1px solid #ebeef5;
}

.has-gutter th:last-child {
	border-right: none;
}
.el-table__row td {
	border-right: 1px solid #ebeef5;
}
.el-table__row td:last-child {
	border-right: none;
}
</style>
