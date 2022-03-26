<template>
	<el-dialog
		:title="type == 'add' ? 'Add Shopping Item' : 'Modify product'"
		v-model="visible"
		width="400px"
		@close="handleClose"
	>
		<el-form
			:model="ruleForm"
			:rules="rules"
			ref="formRef"
			label-width="100px"
			class="good-form"
		>
			<el-form-item label="Product name" prop="name">
				<el-input type="text" v-model="ruleForm.name"></el-input>
			</el-form-item>
			<el-form-item label="Jump link" prop="link">
				<el-input type="text" v-model="ruleForm.link"></el-input>
			</el-form-item>
			<el-form-item label="Product ID" prop="id">
				<el-input type="number" min="0" v-model="ruleForm.id"></el-input>
			</el-form-item>
			<el-form-item label="Sort Number" prop="sort">
				<el-input type="number" v-model="ruleForm.sort"></el-input>
			</el-form-item>
		</el-form>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="visible = false">Cancel</el-button>
				<el-button type="primary" @click="submitForm">OK</el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script>
import { reactive, ref, toRefs } from 'vue'
import axios from '@/utils/axios'
import { hasEmoji } from '@/utils/index'
import { ElMessage } from 'element-plus'
export default {
	name: 'DialogAddHotGood',
	props: {
		type: String,
		configType: Number,
		reload: Function,
	},
	setup(props) {
		const formRef = ref(null)
		const state = reactive({
			visible: false,
			ruleForm: {
				name: '',
				link: '',
				id: '',
				sort: '',
			},
			rules: {
				name: [{ required: 'true', message: 'Name cannot be empty', trigger: ['change'] }],
				id: [{ required: 'true', message: 'Id cannot be empty', trigger: ['change'] }],
				sort: [{ required: 'true', message: 'Sort cannot be empty', trigger: ['change'] }],
			},
			id: '',
		})
		const getDetail = (id) => {
			axios.get(`/indexConfigs/${id}`).then((res) => {
				state.ruleForm = {
					name: res.configName,
					id: res.goodsId,
					link: res.redirectUrl,
					sort: res.configRank,
				}
			})
		}
		const open = (id) => {
			state.visible = true
			if (id) {
				state.id = id
				getDetail(id)
			} else {
				state.ruleForm = {
					name: '',
					id: '',
					link: '',
					sort: '',
				}
			}
		}
		const close = () => {
			state.visible = false
		}
		const handleClose = () => {
			formRef.value.resetFields()
		}
		const submitForm = () => {
			formRef.value.validate((valid) => {
				if (valid) {
					if (hasEmoji(state.ruleForm.name) || hasEmoji(state.ruleForm.link)) {
						ElMessage.error('Emoji not supported.')
						return
					}
					if (state.ruleForm.name.length > 128) {
						ElMessage.error('Product name max to 128 chars')
						return
					}
					if (state.ruleForm.sort < 0 || state.ruleForm.sort > 200) {
						ElMessage.error('Sort Number must between 0 and 200')
						return
					}
					if (props.type == 'add') {
						axios
							.post('/indexConfigs', {
								configType: props.configType || 3,
								configName: state.ruleForm.name,
								redirectUrl: state.ruleForm.link,
								goodsId: state.ruleForm.id,
								configRank: state.ruleForm.sort,
							})
							.then(() => {
								ElMessage.success('Added successfully')
								state.visible = false
								if (props.reload) props.reload()
							})
					} else {
						axios
							.put('/indexConfigs', {
								configId: state.id,
								configType: props.configType || 3,
								configName: state.ruleForm.name,
								redirectUrl: state.ruleForm.link,
								goodsId: state.ruleForm.id,
								configRank: state.ruleForm.sort,
							})
							.then(() => {
								ElMessage.success('Modified successfully')
								state.visible = false
								if (props.reload) props.reload()
							})
					}
				}
			})
		}
		return {
			...toRefs(state),
			open,
			close,
			formRef,
			submitForm,
			handleClose,
		}
	},
}
</script>

<style scoped></style>
