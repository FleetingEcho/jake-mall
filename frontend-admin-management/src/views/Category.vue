<template>
	<el-card class="category-container">
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
			<el-table-column prop="categoryName" label="Category name"> </el-table-column>
			<el-table-column prop="categoryRank" label="Sort Number" width="120"> </el-table-column>
			<el-table-column prop="createTime" label="Add Time" width="200"> </el-table-column>
			<el-table-column label="Operation" width="300">
				<template #default="scope">
					<a
						style="cursor: pointer; margin-right: 10px"
						@click="handleEdit(scope.row.categoryId)"
						>Modify
					</a>
					<a style="cursor: pointer; margin-right: 10px" @click="handleNext(scope.row)"
						>Lower level Category
					</a>
					<el-popconfirm
						title="Are you sure to delete this?"
						@confirm="handleDeleteOne(scope.row.categoryId)"
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
	<DialogAddCategory
		ref="addGood"
		:reload="getCategory"
		:type="type"
		:level="level"
		:parent_id="parent_id"
	/>
</template>

<script>
import { onMounted, onUnmounted, reactive, ref, toRefs } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import DialogAddCategory from '@/components/DialogAddCategory.vue'
import axios from '@/utils/axios'
export default {
	name: 'Category',
	components: {
		DialogAddCategory,
	},
	setup() {
		const multipleTable = ref(null)
		const addGood = ref(null)
		const router = useRouter()
		const route = useRoute()
		const state = reactive({
			loading: false,
			tableData: [],
			multipleSelection: [],
			total: 0,
			currentPage: 1,
			pageSize: 10,
			type: 'add',
			level: 1,
			parent_id: 0,
		})
		onMounted(() => {
			getCategory()
		})
		onUnmounted(() => {
			unwatch()
		})
		const unwatch = router.afterEach((to) => {
			if (['category', 'level2', 'level3'].includes(to.name)) {
				getCategory()
			}
		})
		const getCategory = () => {
			const { level = 1, parent_id = 0 } = route.query
			state.loading = true
			axios
				.get('/categories', {
					params: {
						pageNumber: state.currentPage,
						pageSize: state.pageSize,
						categoryLevel: level,
						parentId: parent_id,
					},
				})
				.then((res) => {
					state.tableData = res.list
					state.total = res.totalCount
					state.currentPage = res.currPage
					state.loading = false
					state.level = level
					state.parentId = parent_id
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
				.delete('/categories', {
					data: {
						ids: state.multipleSelection.map((i) => i.categoryId),
					},
				})
				.then(() => {
					ElMessage.success('Delete successfully.')
					getCategory()
				})
		}
		const handleDeleteOne = (id) => {
			axios
				.delete('/categories', {
					data: {
						ids: [id],
					},
				})
				.then(() => {
					ElMessage.success('Delete successfully.')
					getCategory()
				})
		}
		const changePage = (val) => {
			state.currentPage = val
			getCategory()
		}
		const handleNext = (item) => {
			const levelNumber = item.categoryLevel + 1
			if (levelNumber == 4) {
				ElMessage.error('No next level')
				return
			}
			router.push({
				name: `level${levelNumber}`,
				query: {
					level: levelNumber,
					parent_id: item.categoryId,
				},
			})
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
			getCategory,
			changePage,
			handleNext,
		}
	},
}
</script>

<style scoped>
.category-container {
	min-height: 100%;
}
.el-card.is-always-shadow {
	min-height: 100% !important;
}
</style>
