<template>
	<div class="order-detail-box">
		<s-header :name="'订单详情'" @callback="close"></s-header>
		<div class="order-status">
			<div class="status-item">
				<label>Order status:</label>
				<span>{{ detail.orderStatusString }}</span>
			</div>
			<div class="status-item">
				<label>Order No.:</label>
				<span>{{ detail.orderNo }}</span>
			</div>
			<div class="status-item">
				<label>Order Time:</label>
				<span>{{ detail.createTime }}</span>
			</div>
			<van-button
				v-if="detail.orderStatus == 3"
				style="margin-bottom: 10px"
				color="#1baeae"
				block
				@click="handleConfirmOrder(detail.orderNo)"
				>Confirm received</van-button
			>
			<van-button
				v-if="detail.orderStatus == 0"
				style="margin-bottom: 10px"
				color="#1baeae"
				block
				@click="showPayFn"
				>Pay</van-button
			>
			<van-button
				v-if="!(detail.orderStatus < 0 || detail.orderStatus == 4)"
				block
				@click="handleCancelOrder(detail.orderNo)"
				>Cancel order</van-button
			>
		</div>
		<div class="order-price">
			<div class="price-item">
				<label>The amount of goods:</label>
				<span>¥ {{ detail.totalPrice }}</span>
			</div>
			<div class="price-item">
				<label>Delivery type:</label>
				<span>Ordinary express</span>
			</div>
		</div>
		<van-card
			v-for="item in detail.newBeeMallOrderItemVOS"
			:key="item.goodsId"
			style="background: #fff"
			:num="item.goodsCount"
			:price="item.sellingPrice"
			desc="Free shipping"
			:title="item.goodsName"
			:thumb="$filters.prefix(item.goodsCoverImg)"
		/>
		<van-popup v-model:show="showPay" position="bottom" :style="{ height: '24%' }">
			<div :style="{ width: '90%', margin: '0 auto', padding: '20px 0' }">
				<van-button
					:style="{ marginBottom: '10px' }"
					color="#1989fa"
					block
					@click="handlePayOrder(detail.orderNo, 1)"
					>Alipay</van-button
				>
				<van-button color="#4fc08d" block @click="handlePayOrder(detail.orderNo, 2)"
					>Weixin Pay</van-button
				>
			</div>
		</van-popup>
	</div>
</template>

<script>
import { reactive, toRefs, onMounted } from 'vue'
import sHeader from '@/components/SimpleHeader'
import { getOrderDetail, cancelOrder, confirmOrder, payOrder } from '@/service/order'
import { Dialog, Toast } from 'vant'
import { useRoute } from 'vue-router'
export default {
	name: 'OrderDetail',
	components: {
		sHeader,
	},
	setup() {
		const route = useRoute()
		const state = reactive({
			detail: {},
			showPay: false,
		})

		onMounted(() => {
			init()
		})

		const init = async () => {
			Toast.loading({
				message: 'Loading...',
				forbidClick: true,
			})
			const { id } = route.query
			const { data } = await getOrderDetail(id)
			state.detail = data
			Toast.clear()
		}

		const handleCancelOrder = (id) => {
			Dialog.confirm({
				title: 'Confirm cancel this order?',
			})
				.then(() => {
					cancelOrder(id).then((res) => {
						if (res.resultCode == 200) {
							Toast('Deleted successfully')
							init()
						}
					})
				})
				.catch(() => {})
		}

		const handleConfirmOrder = (id) => {
			Dialog.confirm({
				title: 'Are you sure to confirm this order?',
			})
				.then(() => {
					confirmOrder(id).then((res) => {
						if (res.resultCode == 200) {
							Toast('确认成功')
							init()
						}
					})
				})
				.catch(() => {})
		}

		const showPayFn = () => {
			state.showPay = true
		}

		const handlePayOrder = async (id, type) => {
			Toast.loading
			await payOrder({ orderNo: id, payType: type })
			state.showPay = false
			init()
		}

		const close = () => {
			Dialog.close()
		}

		return {
			...toRefs(state),
			handleCancelOrder,
			handleConfirmOrder,
			showPayFn,
			handlePayOrder,
			close,
		}
	},
}
</script>

<style lang="less" scoped>
.order-detail-box {
	background: #f7f7f7;
	.order-status {
		background: #fff;
		padding: 20px;
		font-size: 15px;
		.status-item {
			margin-bottom: 10px;
			label {
				color: #999;
			}
			span {
			}
		}
	}
	.order-price {
		background: #fff;
		margin: 20px 0;
		padding: 20px;
		font-size: 15px;
		.price-item {
			margin-bottom: 10px;
			label {
				color: #999;
			}
			span {
			}
		}
	}
	.van-card {
		margin-top: 0;
	}
	.van-card__content {
		display: flex;
		flex-direction: column;
		justify-content: center;
	}
}
</style>
