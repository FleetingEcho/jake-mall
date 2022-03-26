<template>
	<el-dialog
		:title="type == 'add' ? 'Add Carousel' : 'Modify Carousel'"
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
			<el-form-item label="Image" prop="url">
				<el-upload
					class="avatar-uploader"
					:action="uploadImgServer"
					accept="jpg,jpeg,png"
					:headers="{
						token: token,
					}"
					:show-file-list="false"
					:before-upload="handleBeforeUpload"
					:on-success="handleUrlSuccess"
				>
					<img
						style="width: 200px; height: 100px; border: 1px solid #e9e9e9"
						v-if="ruleForm.url"
						:src="ruleForm.url"
						class="avatar"
					/>
					<i v-else class="el-icon-plus avatar-uploader-icon"></i>
				</el-upload>
			</el-form-item>
			<el-form-item label="Jump link" prop="link">
				<el-input type="text" v-model="ruleForm.link"></el-input>
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
import { localGet, uploadImgServer, hasEmoji } from '@/utils'
import { ElMessage } from 'element-plus'

export default {
	name: 'DialogAddSwiper',
	props: {
		type: String,
		reload: Function,
	},
	setup(props) {
		const formRef = ref(null)
		const state = reactive({
			uploadImgServer,
			token: localGet('token') || '',
			visible: false,
			ruleForm: {
				url: '',
				link: '',
				sort: '',
			},
			rules: {
				url: [{ required: 'true', message: 'Image cannot be empty', trigger: ['change'] }],
				sort: [{ required: 'true', message: 'Sort cannot be empty', trigger: ['change'] }],
			},
			id: '',
		})
		// 获取详情
		const getDetail = (id) => {
			axios.get(`/carousels/${id}`).then((res) => {
				state.ruleForm = {
					url: res.carouselUrl,
					link: res.redirectUrl,
					sort: res.carouselRank,
				}
			})
		}
		const handleBeforeUpload = (file) => {
			const sufix = file.name.split('.')[1] || ''
			if (!['jpg', 'jpeg', 'png'].includes(sufix)) {
				ElMessage.error('Please upload jpg、jpeg、png  formatted image')
				return false
			}
		}
		const handleUrlSuccess = (val) => {
			state.ruleForm.url = val.data || ''
		}
		const open = (id) => {
			state.visible = true
			if (id) {
				state.id = id
				getDetail(id)
			} else {
				state.ruleForm = {
					url: '',
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
			console.log(formRef.value.validate)
			formRef.value.validate((valid) => {
				if (valid) {
					if (hasEmoji(state.ruleForm.link)) {
						ElMessage.error('Emoji not supported.')
						return
					}
					if (props.type == 'add') {
						axios
							.post('/carousels', {
								carouselUrl: state.ruleForm.url,
								redirectUrl: state.ruleForm.link,
								carouselRank: state.ruleForm.sort,
							})
							.then(() => {
								ElMessage.success('Added successfully')
								state.visible = false
								if (props.reload) props.reload()
							})
					} else {
						axios
							.put('/carousels', {
								carouselId: state.id,
								carouselUrl: state.ruleForm.url,
								redirectUrl: state.ruleForm.link,
								carouselRank: state.ruleForm.sort,
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
			handleBeforeUpload,
			handleUrlSuccess,
			submitForm,
			handleClose,
		}
	},
}
</script>

<style scoped>
.avatar-uploader {
	width: 100px;
	height: 100px;
	color: #ddd;
	font-size: 30px;
}
.avatar-uploader-icon {
	display: block;
	width: 100%;
	height: 100%;
	border: 1px solid #e9e9e9;
	padding: 32px 17px;
}
</style>
