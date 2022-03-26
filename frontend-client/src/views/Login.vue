<template>
	<div class="login">
		<s-header :name="type == 'login' ? 'Login' : 'Sign Up'" :back="'/home'"></s-header>
		<img
			class="logo"
			src="https://cdn.shopify.com/s/files/1/0151/0741/products/63767e36be69e2204043233365c0b89f_1024x1024.jpeg?v=1578641816"
			alt="logo"
		/>
		<div v-if="type == 'login'" class="login-body login">
			<van-form @submit="onSubmit">
				<van-field
					v-model="username"
					name="username"
					label="userName"
					placeholder="userName"
					:rules="[{ required: true, message: 'Please fill in user-name.' }]"
				/>
				<van-field
					v-model="password"
					type="password"
					name="password"
					label="Password"
					placeholder="Password"
					:rules="[{ required: true, message: 'Please fill in password.' }]"
				/>
				<van-field
					center
					clearable
					label="Verification code"
					placeholder="Please fill in verification code"
					v-model="verify"
				>
					<template #button>
						<vue-img-verify ref="verifyRef" />
					</template>
				</van-field>
				<div style="margin: 16px;">
					<div class="link-register" @click="toggle('register')">Register</div>
					<van-button round block color="#1baeae" native-type="submit">Login</van-button>
				</div>
			</van-form>
		</div>
		<div v-else class="login-body register">
			<van-form @submit="onSubmit">
				<van-field
					v-model="username1"
					name="username1"
					label="userName"
					placeholder="userName"
					:rules="[{ required: true, message: 'Please fill in user-name.' }]"
				/>
				<van-field
					v-model="password1"
					type="password"
					name="password1"
					label="Password"
					placeholder="Password"
					:rules="[{ required: true, message: 'Please fill in password.' }]"
				/>
				<van-field
					center
					clearable
					label="Verification code"
					placeholder="Please fill in verification code"
					v-model="verify"
				>
					<template #button>
						<vue-img-verify ref="verifyRef" />
					</template>
				</van-field>
				<div style="margin: 16px;">
					<div class="link-login" @click="toggle('login')">Already has accounts</div>
					<van-button round block color="#1baeae" native-type="submit"
						>Sign up</van-button
					>
				</div>
			</van-form>
		</div>
	</div>
</template>

<script>
import { reactive, ref, toRefs } from 'vue'
import sHeader from '@/components/SimpleHeader'
import vueImgVerify from '@/components/VueImageVerify'
import { login, register } from '@/service/user'
import { setLocal } from '@/common/js/utils'
import md5 from 'js-md5'
import { Toast } from 'vant'
export default {
	setup() {
		const verifyRef = ref(null)
		const state = reactive({
			username: '',
			password: '',
			username1: '',
			password1: '',
			type: 'login',
			imgCode: '',
			verify: '',
		})

		const toggle = (v) => {
			state.type = v
			state.verify = ''
		}

		const onSubmit = async (values) => {
			console.log('verifyRef.value.imgCode', verifyRef.value.imgCode)
			state.imgCode = verifyRef.value.imgCode || ''
			if (state.verify.toLowerCase() != state.imgCode.toLowerCase()) {
				Toast.fail('Wrong code')
				return
			}
			if (state.type == 'login') {
				const { data } = await login({
					loginName: values.username,
					passwordMd5: md5(values.password),
				})
				setLocal('token', data)
				window.location.href = '/'
			} else {
				await register({
					loginName: values.username1,
					password: values.password1,
				})
				Toast.success('Successfully registered.')
				state.type = 'login'
				state.verify = ''
			}
		}

		return {
			...toRefs(state),
			toggle,
			onSubmit,
			verifyRef,
		}
	},
	components: {
		sHeader,
		vueImgVerify,
	},
}
</script>

<style lang="less">
.login {
	.logo {
		width: 120px;
		height: 120px;
		display: block;
		margin: 80px auto 20px;
	}
	.login-body {
		padding: 0 20px;
	}
	.login {
		.link-register {
			font-size: 14px;
			margin-bottom: 20px;
			color: #1989fa;
			display: inline-block;
		}
	}
	.register {
		.link-login {
			font-size: 14px;
			margin-bottom: 20px;
			color: #1989fa;
			display: inline-block;
		}
	}
	.verify-bar-area {
		margin-top: 24px;
		.verify-left-bar {
			border-color: #1baeae;
		}
		.verify-move-block {
			background-color: #1baeae;
			color: #fff;
		}
	}
	.verify {
		> div {
			width: 100%;
		}
		display: flex;
		justify-content: center;
		.cerify-code-panel {
			margin-top: 16px;
		}
		.verify-code {
			width: 40% !important;
			float: left !important;
		}
		.verify-code-area {
			float: left !important;
			width: 54% !important;
			margin-left: 14px !important;
			.varify-input-code {
				width: 90px;
				height: 38px !important;
				border: 1px solid #e9e9e9;
				padding-left: 10px;
				font-size: 16px;
			}
			.verify-change-area {
				line-height: 44px;
			}
		}
	}
}
</style>
