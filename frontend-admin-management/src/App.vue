<template>
	<div class="layout">
		<el-container v-if="state.showMenu" class="container">
			<el-aside class="aside">
				<div class="head">
					<div>
						<img
							src="https://cdn1.iconfinder.com/data/icons/supermarket-cafe-and-stores/50/10-512.png"
							alt="logo"
							style="height: 30px"
						/>
						<span>Shopping Mall</span>
					</div>
				</div>
				<div class="line" />
				<el-menu
					:default-openeds="state.defaultOpen"
					background-color="#222832"
					text-color="#fff"
					:router="true"
					:default-active="state.currentPath"
				>
					<el-submenu index="1">
						<template #title>
							<span>Dashboard</span>
						</template>
						<el-menu-item-group>
							<el-menu-item index="/introduce"
								><i class="el-icon-data-line" />System Introduction</el-menu-item
							>
							<el-menu-item index="/dashboard"
								><i class="el-icon-odometer" />Dashboard</el-menu-item
							>
							<el-menu-item index="/add"
								><i class="el-icon-plus" />Add Shopping Item</el-menu-item
							>
						</el-menu-item-group>
					</el-submenu>
					<el-submenu index="2">
						<template #title>
							<span>HomePage Config</span>
						</template>
						<el-menu-item-group>
							<el-menu-item index="/swiper"
								><i class="el-icon-picture" />Carousel Config</el-menu-item
							>
							<el-menu-item index="/hot"
								><i class="el-icon-star-on" />Popular Goods Config</el-menu-item
							>
							<el-menu-item index="/new"
								><i class="el-icon-sell" />New Product Online Config</el-menu-item
							>
							<el-menu-item index="/recommend"
								><i class="el-icon-thumb" />Recommend For You</el-menu-item
							>
						</el-menu-item-group>
					</el-submenu>
					<el-submenu index="3">
						<template #title>
							<span>Module Management</span>
						</template>
						<el-menu-item-group>
							<el-menu-item index="/category"
								><i class="el-icon-menu" />Category management</el-menu-item
							>
							<el-menu-item index="/good"
								><i class="el-icon-s-goods" />Product Management</el-menu-item
							>
							<el-menu-item index="/guest"
								><i class="el-icon-user-solid" />VIP Management</el-menu-item
							>
							<el-menu-item index="/order"
								><i class="el-icon-s-order" />Order Management</el-menu-item
							>
						</el-menu-item-group>
					</el-submenu>
					<el-submenu index="4">
						<template #title>
							<span>System Management</span>
						</template>
						<el-menu-item-group>
							<el-menu-item index="/account"
								><i class="el-icon-lock" />Update Password</el-menu-item
							>
						</el-menu-item-group>
					</el-submenu>
				</el-menu>
			</el-aside>
			<el-container class="content">
				<Header />
				<div class="main">
					<router-view />
				</div>
				<Footer />
			</el-container>
		</el-container>
		<el-container v-else class="container">
			<router-view />
		</el-container>
	</div>
</template>

<script>
import { onUnmounted, reactive } from 'vue'
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import { useRouter } from 'vue-router'
import { pathMap, localGet } from '@/utils'
export default {
	name: 'App',
	components: {
		Header,
		Footer,
	},
	setup() {
		console.log('App')
		const noMenu = ['/login']
		const router = useRouter()
		const state = reactive({
			defaultOpen: ['1', '2', '3', '4'],
			showMenu: true,
			currentPath: '/dashboard',
			count: {
				number: 1,
			},
		})
		// Listening for browser native fallback events
		if (window.history && window.history.pushState) {
			history.pushState(null, null, document.URL)
			window.addEventListener(
				'popstate',
				() => {
					if (!localGet('token')) {
						state.showMenu = false
					}
				},
				false
			)
		}
		const unwatch = router.beforeEach((to, from, next) => {
			if (to.path == '/login') {
				// If the path is /login then it will execute normally
				next()
			} else {
				// If not /login, determine if there is a token
				if (!localGet('token')) {
					next({ path: '/login' })
				} else {
					next()
				}
			}
			state.showMenu = !noMenu.includes(to.path)
			state.currentPath = to.path
			document.title = pathMap[to.name]
		})

		onUnmounted(() => {
			unwatch()
		})

		return {
			state,
		}
	},
}
</script>

<style scoped>
.layout {
	min-height: 100vh;
	background-color: #ffffff;
}
.container {
	height: 100vh;
}
.aside {
	width: 300px !important;
	background-color: #222832;
	overflow: hidden;
	overflow-y: auto;
	-ms-overflow-style: none;
	overflow: -moz-scrollbars-none;
}
.aside::-webkit-scrollbar {
	display: none;
}
.head {
	display: flex;
	align-items: center;
	justify-content: center;
	height: 50px;
}
.head > div {
	display: flex;
	align-items: center;
}

.head img {
	width: 50px;
	height: 50px;
	margin-right: 10px;
}
.head span {
	font-size: 20px;
	color: #ffffff;
}
.line {
	border-top: 1px solid hsla(0, 0%, 100%, 0.05);
	border-bottom: 1px solid rgba(0, 0, 0, 0.2);
}
.content {
	display: flex;
	flex-direction: column;
	max-height: 100vh;
	overflow: hidden;
}
.main {
	height: calc(100vh - 100px);
	overflow: auto;
	padding: 10px;
}
</style>
<style>
body {
	padding: 0;
	margin: 0;
	box-sizing: border-box;
}
.el-menu {
	border-right: none !important;
}
.el-submenu {
	border-top: 1px solid hsla(0, 0%, 100%, 0.05);
	border-bottom: 1px solid rgba(0, 0, 0, 0.2);
}
.el-submenu:first-child {
	border-top: none;
}
.el-submenu [class^='el-icon-'] {
	vertical-align: -1px !important;
}
a {
	color: #409eff;
	text-decoration: none;
}
.el-pagination {
	text-align: center;
	margin-top: 20px;
}
.el-popper__arrow {
	display: none;
}
</style>
