<template>
	<div class="seting-box">
		<s-header :name="'Manage account'"></s-header>
		<div class="input-item">
			<van-field v-model="nickName" label="Nickname" />
			<van-field v-model="introduceSign" label="Signature" />
			<van-field v-model="password" type="password" label="Password" />
		</div>
		<van-button round class="save-btn" color="#1baeae" type="primary" @click="save" block
			>Save</van-button
		>
		<van-button
			round
			class="save-btn"
			color="#1baeae"
			type="primary"
			@click="handleLogout"
			block
			>Logout</van-button
		>
	</div>
</template>

<script>
import { reactive, onMounted, toRefs } from 'vue'
import md5 from 'js-md5'
import sHeader from '@/components/SimpleHeader'
import { getUserInfo, EditUserInfo, logout } from '@/service/user'
import { setLocal } from '@/common/js/utils'
import { Toast } from 'vant'
export default {
	components: {
		sHeader,
	},
	setup() {
		const state = reactive({
			nickName: '',
			introduceSign: '',
			password: '',
		})

		onMounted(async () => {
			const { data } = await getUserInfo()
			state.nickName = data.nickName
			state.introduceSign = data.introduceSign
		})

		const save = async () => {
			const params = {
				introduceSign: state.introduceSign,
				nickName: state.nickName,
			}
			if (state.password) {
				params.passwordMd5 = md5(state.password)
			}
			await EditUserInfo(params)
			Toast.success('Saved successfully')
		}

		const handleLogout = async () => {
			const { resultCode } = await logout()
			if (resultCode == 200) {
				setLocal('token', '')
				window.location.href = '/home'
			}
		}

		return {
			...toRefs(state),
			save,
			handleLogout,
		}
	},
}
</script>

<style lang="less" scoped>
.seting-box {
	.save-btn {
		width: 80%;
		margin: 20px auto;
	}
}
</style>
