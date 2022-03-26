<template>
	<div class="order-box">
		<s-header :name="'My orders'" :back="'/user'"></s-header>
		<van-tabs
			@click="onChangeTab"
			:color="'#1baeae'"
			:title-active-color="'#1baeae'"
			class="order-tab"
			v-model="status"
		>
			<van-tab title="All" name=""></van-tab>
			<van-tab title="Pending payment" name="0"></van-tab>
			<van-tab title="To be confirmed" name="1"></van-tab>
			<van-tab title="To be delivered" name="2"></van-tab>
			<van-tab title="Delivering" name="3"></van-tab>
			<van-tab title="Order completed" name="4"></van-tab>
		</van-tabs>
		<div class="content">
			<van-pull-refresh v-model="refreshing" @refresh="onRefresh" class="order-list-refresh">
				<van-list
					v-model:loading="loading"
					:finished="finished"
					finished-text="No more products"
					@load="onLoad"
					@offset="10"
				>
					<div
						v-for="(item, index) in list"
						:key="index"
						class="order-item-box"
						@click="goTo(item.orderNo)"
					>
						<div class="order-item-header">
							<span>Order Time:{{ item.createTime }}</span>
							<span>{{ item.orderStatusString }}</span>
						</div>
						<van-card
							v-for="one in item.newBeeMallOrderItemVOS"
							:key="one.orderId"
							:num="one.goodsCount"
							:price="one.sellingPrice"
							desc="Free Delivery"
							:title="one.goodsName"
							:thumb="$filters.prefix(one.goodsCoverImg)"
						/>
					</div>
				</van-list>
			</van-pull-refresh>
		</div>
	</div>
</template>

<script>
import { reactive, toRefs } from 'vue'
import sHeader from '@/components/SimpleHeader'
import { getOrderList } from '@/service/order'
import { useRouter } from 'vue-router'

export default {
	name: 'Order',
	components: {
		sHeader,
	},
	setup() {
		const router = useRouter()
		const state = reactive({
			status: '',
			loading: false,
			finished: false,
			refreshing: false,
			list: [],
			page: 1,
			totalPage: 0,
		})

		const loadData = async () => {
			const {
				data,
				data: { list },
			} = await getOrderList({ pageNumber: state.page, status: state.status })
			state.list = state.list.concat(list)
			state.totalPage = data.totalPage
			state.loading = false
			if (state.page >= data.totalPage) state.finished = true
		}

		const onChangeTab = (name) => {
			state.status = name
			onRefresh()
		}

		const goTo = (id) => {
			router.push({ path: '/order-detail', query: { id } })
		}

		const goBack = () => {
			router.go(-1)
		}

		const onLoad = () => {
			if (!state.refreshing && state.page < state.totalPage) {
				console.log(state.page)
				console.log(state.totalPage)
				state.page = state.page + 1
			}
			if (state.refreshing) {
				state.list = []
				state.refreshing = false
			}
			loadData()
		}

		const onRefresh = () => {
			state.refreshing = true
			state.finished = false
			state.loading = true
			state.page = 1
			onLoad()
		}

		return {
			...toRefs(state),
			onChangeTab,
			goTo,
			goBack,
			onLoad,
			onRefresh,
		}
	},
}
</script>

<style lang="less" scoped>
@import '../common/style/mixin';
.order-box {
	.order-header {
		position: fixed;
		top: 0;
		left: 0;
		z-index: 10000;
		.fj();
		.wh(100%, 44px);
		line-height: 44px;
		padding: 0 10px;
		.boxSizing();
		color: #252525;
		background: #fff;
		border-bottom: 1px solid #dcdcdc;
		.order-name {
			font-size: 14px;
		}
	}
	.order-tab {
		position: fixed;
		left: 0;
		z-index: 1000;
		width: 100%;
		border-bottom: 1px solid #e9e9e9;
	}
	.skeleton {
		margin-top: 60px;
	}
	.content {
		height: calc(~'(100vh - 70px)');
		overflow: hidden;
		overflow-y: scroll;
		margin-top: 34px;
	}
	.order-list-refresh {
		.van-card__content {
			display: flex;
			flex-direction: column;
			justify-content: center;
		}
		.van-pull-refresh__head {
			background: #f9f9f9;
		}
		.order-item-box {
			margin: 20px 10px;
			background-color: #fff;
			.order-item-header {
				padding: 10px 20px 0 20px;
				display: flex;
				justify-content: space-between;
			}
			.van-card {
				background-color: #fff;
				margin-top: 0;
			}
		}
	}
}
</style>
