<template>
	<el-card class="swiper-container">
		<template #header>
			<div class="header">
				<el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd"
					>Add
				</el-button>
				<el-popconfirm title="Are you sure to delete this?" @confirm="handleDelete">
					<template #reference>
						<el-button type="danger" size="small" icon="el-icon-delete"
							>Batch delete</el-button
						>
					</template>
				</el-popconfirm>
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
			<el-table-column label="Carousel" width="200">
				<template #default="scope">
					<img
						style="width: 150px; height: 150px"
						:src="scope.row.carouselUrl"
						alt="Carousel"
					/>
				</template>
			</el-table-column>
			<el-table-column label="Jump link">
				<template #default="scope">
					<a target="_blank" :href="scope.row.redirectUrl">{{ scope.row.redirectUrl }}</a>
				</template>
			</el-table-column>
			<el-table-column prop="carouselRank" label="Sort Number" width="120"> </el-table-column>
			<el-table-column prop="createTime" label="Add Time" width="200"> </el-table-column>
			<el-table-column label="Operation" width="200">
				<template #default="scope">
					<a
						style="cursor: pointer; margin-right: 10px"
						@click="handleEdit(scope.row.carouselId)"
						>Modify
					</a>
					<el-popconfirm
						title="Are you sure to delete this?"
						@confirm="handleDeleteOne(scope.row.carouselId)"
					>
						<template #reference>
							<a style="cursor: pointer">Delete</a>
						</template>
					</el-popconfirm>
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
	<DialogAddSwiper ref="addGood" :reload="getCarousels" :type="type" />
</template>

<script>
import { onMounted, reactive, ref, toRefs } from 'vue'
import { ElMessage } from 'element-plus'
import DialogAddSwiper from '@/components/DialogAddSwiper.vue'
import axios from '@/utils/axios'
export default {
	name: 'Swiper',
	components: {
		DialogAddSwiper,
	},
	setup() {
		const multipleTable = ref(null)
		const addGood = ref(null)
		const state = reactive({
			loading: false,
			tableData: [],
			multipleSelection: [],
			total: 0,
			currentPage: 1,
			pageSize: 10,
			type: 'add',
		})
		onMounted(() => {
			getCarousels()
		})

		const getCarousels = () => {
			state.loading = true
			axios
				.get('/carousels', {
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
			state.type = 'add'
			addGood.value.open()
		}

		const handleEdit = (id) => {
			state.type = 'edit'
			addGood.value.open(id)
		}

		const handleSelectionChange = (val) => {
			state.multipleSelection = val
		}

		const handleDelete = () => {
			if (!state.multipleSelection.length) {
				ElMessage.error('Please select one to continue')
				return
			}
			axios
				.delete('/carousels', {
					data: {
						ids: state.multipleSelection.map((i) => i.carouselId),
					},
				})
				.then(() => {
					ElMessage.success('Delete successfully.')
					getCarousels()
				})
		}

		const handleDeleteOne = (id) => {
			axios
				.delete('/carousels', {
					data: {
						ids: [id],
					},
				})
				.then(() => {
					ElMessage.success('Delete successfully.')
					getCarousels()
				})
		}
		const changePage = (val) => {
			state.currentPage = val
			getCarousels()
		}
		return {
			...toRefs(state),
			multipleTable,
			handleSelectionChange,
			addGood,
			handleAdd,
			handleEdit,
			handleDelete,
			handleDeleteOne,
			getCarousels,
			changePage,
		}
	},
}
</script>

<style scoped>
.swiper-container {
	min-height: 100%;
}
.el-card.is-always-shadow {
	min-height: 100% !important;
}
</style>
