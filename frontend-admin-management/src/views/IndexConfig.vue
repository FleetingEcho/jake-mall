<template>
	<el-card class="index-container">
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
			<el-table-column prop="configName" label="Product name"> </el-table-column>
			<el-table-column label="Jump link">
				<template #default="scope">
					<a target="_blank" :href="scope.row.redirectUrl">{{ scope.row.redirectUrl }}</a>
				</template>
			</el-table-column>
			<el-table-column prop="configRank" label="Sort Number" width="120"> </el-table-column>
			<el-table-column prop="goodsId" label="Product ID" width="200"> </el-table-column>
			<el-table-column prop="createTime" label="Add Time" width="200"> </el-table-column>
			<el-table-column label="Operation" width="200">
				<template #default="scope">
					<a
						style="cursor: pointer; margin-right: 10px"
						@click="handleEdit(scope.row.configId)"
						>Modify
					</a>
					<el-popconfirm
						title="Are you sure to delete this?"
						@confirm="handleDeleteOne(scope.row.configId)"
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
	<DialogAddGood ref="addGood" :reload="getIndexConfig" :type="type" :configType="configType" />
</template>

<script>
import { onMounted, onUnmounted, reactive, ref, toRefs } from 'vue'
import { ElMessage } from 'element-plus'
import DialogAddGood from '@/components/DialogAddGood.vue'
import { useRoute, useRouter } from 'vue-router'
import axios from '@/utils/axios'

const configTypeMap = {
	hot: 3,
	new: 4,
	recommend: 5,
}
export default {
	name: 'Hot',
	components: {
		DialogAddGood,
	},
	setup() {
		const router = useRouter()
		const route = useRoute()
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
			configType: 3,
		})

		const unwatch = router.beforeEach((to) => {
			if (['hot', 'new', 'recommend'].includes(to.name)) {
				state.configType = configTypeMap[to.name]
				state.currentPage = 1
				getIndexConfig()
			}
		})

		onMounted(() => {
			state.configType = configTypeMap[route.name]
			getIndexConfig()
		})
		onUnmounted(() => {
			unwatch()
		})

		const getIndexConfig = () => {
			state.loading = true
			axios
				.get('/indexConfigs', {
					params: {
						pageNumber: state.currentPage,
						pageSize: state.pageSize,
						configType: state.configType,
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
				.post('/indexConfigs/delete', {
					ids: state.multipleSelection.map((i) => i.configId),
				})
				.then(() => {
					ElMessage.success('Delete successfully.')
					getIndexConfig()
				})
		}

		const handleDeleteOne = (id) => {
			axios
				.post('/indexConfigs/delete', {
					ids: [id],
				})
				.then(() => {
					ElMessage.success('Delete successfully.')
					getIndexConfig()
				})
		}
		const changePage = (val) => {
			state.currentPage = val
			getIndexConfig()
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
			getIndexConfig,
			changePage,
		}
	},
}
</script>

<style scoped>
.index-container {
	min-height: 100%;
}
.el-card.is-always-shadow {
	min-height: 100% !important;
}
</style>
