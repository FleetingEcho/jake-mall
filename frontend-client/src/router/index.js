import { createRouter, createWebHashHistory } from 'vue-router'

const router = createRouter({
	history: createWebHashHistory(),
	routes: [
		{
			path: '/',
			redirect: '/home',
		},
		{
			path: '/home',
			name: 'home',
			component: () => import('@/views/Home.vue'),
			meta: {
				index: 1,
			},
		},
		{
			path: '/login',
			name: 'login',
			component: () => import('@/views/Login.vue'),
			meta: {
				index: 1,
			},
		},
		{
			path: '/about',
			name: 'about',
			component: () => import('@/views/About.vue'),
			meta: {
				index: 2,
			},
		},
		{
			path: '/category',
			name: 'category',
			component: () => import('@/views/Category.vue'),
			meta: {
				index: 1,
			},
		},
		{
			path: '/product-list',
			name: 'product-list',
			component: () => import('@/views/ProductList.vue'),
			meta: {
				index: 2,
			},
		},
		{
			path: '/product/:id',
			name: 'product',
			component: () => import('@/views/ProductDetail.vue'),
			meta: {
				index: 3,
			},
		},
		{
			path: '/cart',
			name: 'cart',
			component: () => import('@/views/Cart.vue'),
			meta: {
				index: 1,
			},
		},
		{
			path: '/create-order',
			name: 'create-order',
			component: () => import('@/views/CreateOrder.vue'),
			meta: {
				index: 2,
			},
		},
		{
			path: '/order',
			name: 'order',
			component: () => import('@/views/Order.vue'),
			meta: {
				index: 2,
			},
		},
		{
			path: '/order-detail',
			name: 'order-detail',
			component: () => import('@/views/OrderDetail.vue'),
			meta: {
				index: 3,
			},
		},
		{
			path: '/user',
			name: 'user',
			component: () => import('@/views/User.vue'),
			meta: {
				index: 1,
			},
		},
		{
			path: '/setting',
			name: 'setting',
			component: () => import('@/views/Setting.vue'),
			meta: {
				index: 2,
			},
		},
		{
			path: '/address',
			name: 'address',
			component: () => import('@/views/Address.vue'),
			meta: {
				index: 2,
			},
		},
		{
			path: '/address-edit',
			name: 'address-edit',
			component: () => import('@/views/AddressEdit.vue'),
			meta: {
				index: 3,
			},
		},
	],
})

export default router
