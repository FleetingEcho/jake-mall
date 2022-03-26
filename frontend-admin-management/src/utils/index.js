export function localGet(key) {
	const value = window.localStorage.getItem(key)
	try {
		return JSON.parse(window.localStorage.getItem(key))
	} catch (error) {
		return value
	}
}

export function localSet(key, value) {
	window.localStorage.setItem(key, JSON.stringify(value))
}

export function localRemove(key) {
	window.localStorage.removeItem(key)
}

export function hasEmoji(str = '') {
	const reg =
		/[^\u0020-\u007E\u00A0-\u00BE\u2E80-\uA4CF\uF900-\uFAFF\uFE30-\uFE4F\uFF00-\uFFEF\u0080-\u009F\u2000-\u201f\u2026\u2022\u20ac\r\n]/g
	return str.match(reg) && str.match(reg).length
}

export const uploadImgServer = 'http://localhost:28019/manage-api/v1/upload/file'
export const uploadImgsServer = 'http://localhost:28019/manage-api/v1/upload/files'

export const pathMap = {
	login: 'Login',
	introduce: 'System Description',
	dashboard: 'Broad market data',
	add: 'Add Shopping Item',
	swiper: 'Carousel Config',
	hot: 'Popular goods config',
	new: 'New Product online config',
	recommend: 'Recommend for you',
	category: 'Category management',
	level2: 'Secondary management of categories',
	level3: 'Category management at three levels',
	good: 'Product Management',
	guest: 'VIP Management',
	order: 'Order Management',
	order_detail: 'Order Detail',
	account: 'Modify Account',
}
