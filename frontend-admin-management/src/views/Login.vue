<template>
	<div class="login-body">
		<div class="login-container">
			<div class="head">
				<img
					class="logo"
					src="https://cdn1.iconfinder.com/data/icons/supermarket-cafe-and-stores/50/10-512.png"
				/>
				<div class="name">
					<div class="title">Mall</div>
					<div class="tips">Admin Management System</div>
				</div>
			</div>
			<el-form
				label-position="top"
				:rules="rules"
				:model="ruleForm"
				ref="loginForm"
				class="login-form"
			>
				<el-form-item label="Username" prop="username">
					<el-input
						type="text"
						v-model.trim="ruleForm.username"
						autocomplete="off"
					></el-input>
				</el-form-item>
				<el-form-item label="Password" prop="password">
					<el-input
						type="password"
						v-model.trim="ruleForm.password"
						autocomplete="off"
					></el-input>
				</el-form-item>
				<el-form-item>
					<div style="color: #333">
						Login means you are agreeing with <a>Term of Service</a>
					</div>
					<el-button style="width: 100%" type="primary" @click="submitForm"
						>Login</el-button
					>
					<el-checkbox v-model="checked" @change="!checked"
						>Auto Login Next Time</el-checkbox
					>
				</el-form-item>
			</el-form>
		</div>
	</div>
</template>

<script>
import axios from '@/utils/axios'
import md5 from 'js-md5'
import { reactive, ref, toRefs } from 'vue'
import { localSet } from '@/utils'
export default {
	name: 'Login',
	setup() {
		const loginForm = ref(null)
		const state = reactive({
			ruleForm: {
				username: '',
				password: '',
			},
			checked: true,
			rules: {
				username: [
					{ required: 'true', message: 'Username cannot be empty', trigger: 'blur' },
				],
				password: [
					{ required: 'true', message: 'Password cannot be empty', trigger: 'blur' },
				],
			},
		})
		const submitForm = async () => {
			loginForm.value.validate((valid) => {
				if (valid) {
					axios
						.post('/adminUser/login', {
							userName: state.ruleForm.username || '',
							passwordMd5: md5(state.ruleForm.password),
						})
						.then((res) => {
							localSet('token', res)
							window.location.href = '/'
						})
				} else {
					console.log('error submit!!')
					return false
				}
			})
		}
		const resetForm = () => {
			loginForm.value.resetFields()
		}
		return {
			...toRefs(state),
			loginForm,
			submitForm,
			resetForm,
		}
	},
}
</script>

<style scoped>
.login-body {
	display: flex;
	justify-content: center;
	align-items: center;
	width: 100%;
	background-color: #fff;
	/* background-image: linear-gradient(25deg, #077f7c, #3aa693, #5ecfaa, #7ffac2); */
}
.login-container {
	width: 600px;
	height: 500px;
	background-color: #fff;
	border-radius: 4px;
	box-shadow: 0px 21px 41px 0px rgba(0, 0, 0, 0.2);
}
.head {
	display: flex;
	justify-content: center;
	align-items: center;
	padding: 40px 0 20px 0;
}
.head img {
	width: 100px;
	height: 100px;
	margin-right: 20px;
}
.head .title {
	font-size: 28px;
	color: #1baeae;
	font-weight: bold;
}
.head .tips {
	font-size: 12px;
	color: #999;
}
.login-form {
	width: 70%;
	margin: 0 auto;
}
</style>
<style>
.el-form--label-top .el-form-item__label {
	padding: 0;
}
.login-form .el-form-item {
	margin-bottom: 12px;
}
</style>
