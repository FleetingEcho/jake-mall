<template>
	<el-card class="account-container">
		<el-form
			:model="nameForm"
			:rules="rules"
			ref="nameRef"
			label-width="80px"
			label-position="right"
			class="demo-ruleForm"
		>
			<el-form-item label="Login name: " prop="loginName">
				<el-input style="width: 200px" v-model="nameForm.loginName"></el-input>
			</el-form-item>
			<el-form-item label="Nickname" prop="nickName">
				<el-input style="width: 200px" v-model="nameForm.nickName"></el-input>
			</el-form-item>
			<el-form-item>
				<el-button type="danger" @click="submitName">Modify </el-button>
			</el-form-item>
		</el-form>
	</el-card>
	<el-card class="account-container">
		<el-form
			:model="passForm"
			:rules="rules"
			ref="passRef"
			label-width="80px"
			label-position="right"
			class="demo-ruleForm"
		>
			<el-form-item label="Old password" prop="oldpass">
				<el-input style="width: 200px" v-model="passForm.oldpass"></el-input>
			</el-form-item>
			<el-form-item label="New password " prop="newpass">
				<el-input style="width: 200px" v-model="passForm.newpass"></el-input>
			</el-form-item>
			<el-form-item>
				<el-button type="danger" @click="submitPass">Modify </el-button>
			</el-form-item>
		</el-form>
	</el-card>
</template>

<script>
import { onMounted, reactive, ref, toRefs } from 'vue'
import axios from '@/utils/axios'
import { ElMessage } from 'element-plus'
import md5 from 'js-md5'
export default {
	name: 'Account',
	setup() {
		const nameRef = ref(null)
		const passRef = ref(null)
		const state = reactive({
			user: null,
			nameForm: {
				loginName: '',
				nickName: '',
			},
			passForm: {
				oldpass: '',
				newpass: '',
			},
			rules: {
				loginName: [
					{
						required: 'true',
						message: 'Login name cannot be empty',
						trigger: ['change'],
					},
				],
				nickName: [
					{ required: 'true', message: 'Nickname cannot be empty', trigger: ['change'] },
				],
				oldpass: [
					{
						required: 'true',
						message: 'Old password cannot be empty',
						trigger: ['change'],
					},
				],
				newpass: [
					{
						required: 'true',
						message: 'New password  cannot be empty',
						trigger: ['change'],
					},
				],
			},
		})
		onMounted(() => {
			axios.get('/adminUser/profile').then((res) => {
				state.user = res
				state.nameForm.loginName = res.loginUserName
				state.nameForm.nickName = res.nickName
			})
		})
		const submitName = () => {
			nameRef.value.validate((vaild) => {
				if (vaild) {
					axios
						.put('/adminUser/name', {
							loginUserName: state.nameForm.loginName,
							nickName: state.nameForm.nickName,
						})
						.then(() => {
							ElMessage.success('Modified successfully')
							window.location.reload()
						})
				}
			})
		}
		const submitPass = () => {
			passRef.value.validate((vaild) => {
				if (vaild) {
					axios
						.put('/adminUser/password', {
							originalPassword: md5(state.passForm.oldpass),
							newPassword: md5(state.passForm.newpass),
						})
						.then(() => {
							ElMessage.success('Modified successfully')
							window.location.reload()
						})
				}
			})
		}
		return {
			...toRefs(state),
			nameRef,
			passRef,
			submitName,
			submitPass,
		}
	},
}
</script>

<style>
.account-container {
	margin-bottom: 20px;
}
</style>
