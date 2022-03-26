<template>
	<el-card class="good-container">
		<template #header>
			<div class="header">
				<el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd"
					>Add Product
				</el-button>
			</div>
		</template>
		<el-table
			v-loading="loading"
			ref="multipleTable"
			:data="tableData"
			tooltip-effect="dark"
			style="width: 100%"
			@selection-change="handleSelectionChange"
		>
			<el-table-column type="selection" width="55"> </el-table-column>
			<el-table-column prop="goodsId" label="Product ID"> </el-table-column>
			<el-table-column prop="goodsName" label="Product name"> </el-table-column>
			<el-table-column prop="goodsIntro" label="Product INTRODUCTION "> </el-table-column>
			<el-table-column label="Product Image" width="150px">
				<template #default="scope">
					<img
						style="width: 100px; height: 100px"
						:key="scope.row.goodsId"
						:src="$filters.prefix(scope.row.goodsCoverImg)"
						alt="Product cover image"
					/>
				</template>
			</el-table-column>
			<el-table-column prop="stockNum" label="Product Inventory"> </el-table-column>
			<el-table-column prop="sellingPrice" label="Product price"> </el-table-column>
			<el-table-column label="Shelf status">
				<template #default="scope">
					<span style="color: green" v-if="scope.row.goodsSellStatus == 0">Selling</span>
					<span style="color: red" v-else>Off shelf</span>
				</template>
			</el-table-column>

			<el-table-column label="Operation" width="300">
				<template #default="scope">
					<a
						style="cursor: pointer; margin-right: 10px"
						@click="handleEdit(scope.row.goodsId)"
						>Modify
					</a>
					<a
						style="cursor: pointer; margin-right: 10px"
						v-if="scope.row.goodsSellStatus == 0"
						@click="handleStatus(scope.row.goodsId, 1)"
						>Off shelf</a
					>
					<a
						style="cursor: pointer; margin-right: 10px"
						v-else
						@click="handleStatus(scope.row.goodsId, 0)"
						>On shelf</a
					>
				</template>
			</el-table-column>
		</el-table>
		<el-pagination
			background
			layout="prev, pager, next"
			:total="total"
			:page-size="pageSize"
			:current-page="currentPage"
			@current-change="changePage"
		/>
	</el-card>
</template>

<script>
import { onMounted, reactive, ref, toRefs } from 'vue'
import axios from '@/utils/axios'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
export default {
	name: 'Good',
	setup() {
		const multipleTable = ref(null)
		const router = useRouter()
		const state = reactive({
			loading: false,
			tableData: [],
			multipleSelection: [],
			total: 0,
			currentPage: 1,
			pageSize: 10,
		})
		onMounted(() => {
			getGoodList()
		})

		const getGoodList = () => {
			state.loading = true
			axios
				.get('/goods/list', {
					params: {
						pageNumber: state.currentPage,
						pageSize: state.pageSize,
					},
				})
				.then((res) => {
					state.tableData = res.list
					state.total = res.totalCount
					state.currentPage = res.currPage
					state.loading = false
				})
		}
		const handleAdd = () => {
			router.push({ path: '/add' })
		}
		const handleEdit = (id) => {
			router.push({ path: '/add', query: { id } })
		}

		const handleSelectionChange = (val) => {
			state.multipleSelection = val
		}
		const changePage = (val) => {
			state.currentPage = val
			getGoodList()
		}
		const handleStatus = (id, status) => {
			axios
				.put(`/goods/status/${status}`, {
					ids: id ? [id] : [],
				})
				.then(() => {
					ElMessage.success('Modified successfully')
					getGoodList()
				})
		}
		return {
			...toRefs(state),
			multipleTable,
			handleSelectionChange,
			handleAdd,
			handleEdit,
			getGoodList,
			changePage,
			handleStatus,
		}
	},
}
</script>

<style scoped>
.good-container {
	min-height: 100%;
}
.el-card.is-always-shadow {
	min-height: 100% !important;
}
</style>
