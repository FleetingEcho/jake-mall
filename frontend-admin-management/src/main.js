import { createApp } from 'vue'
import {
	ElButton,
	ElContainer,
	ElAside,
	ElHeader,
	ElMain,
	ElFooter,
	ElMenu,
	ElSubmenu,
	ElMenuItemGroup,
	ElMenuItem,
	ElForm,
	ElFormItem,
	ElInput,
	ElPopover,
	ElTag,
	ElCard,
	ElTable,
	ElTableColumn,
	ElPagination,
	ElDialog,
	ElPopconfirm,
	ElUpload,
	ElLoading,
	ElSelect,
	ElOption,
	ElRadioGroup,
	ElRadio,
	ElCascader,
	ElCheckbox,
	ElInputNumber,
} from 'element-plus'
import * as Sentry from '@sentry/browser'
import { Integrations } from '@sentry/tracing'
import App from './App.vue'
import router from './router/index'

const orderStatus = {
	0: 'To be paid',
	1: 'Paid',
	2: 'Allocation complete',
	3: 'Successful exit from storage',
	4: 'Successful transaction',
	'-1': 'Manual closure',
	'-2': 'Timeout closure',
	'-3': 'Merchant closure',
}

const app = createApp(App)
// filter
app.config.globalProperties.$filters = {
	orderMap(status) {
		return orderStatus[status] || 'Unknown status'
	},
	prefix(url) {
		if (url && url.startsWith('http')) {
			return url
		} else {
			url = `http://localhost:28019${url}`
			return url
		}
	},
	resetImgUrl(imgObj, imgSrc, maxErrorNum) {
		if (maxErrorNum > 0) {
			imgObj.onerror = function () {
				resetImgUrl(imgObj, imgSrc, maxErrorNum - 1)
			}
			setTimeout(function () {
				imgObj.src = imgSrc
			}, 500)
		} else {
			imgObj.onerror = null
			imgObj.src = imgSrc
		}
	},
}

app.use(router)

app.use(ElButton)
	.use(ElContainer)
	.use(ElAside)
	.use(ElHeader)
	.use(ElMain)
	.use(ElFooter)
	.use(ElMenu)
	.use(ElSubmenu)
	.use(ElMenuItemGroup)
	.use(ElMenuItem)
	.use(ElForm)
	.use(ElFormItem)
	.use(ElInput)
	.use(ElPopover)
	.use(ElTag)
	.use(ElCard)
	.use(ElTable)
	.use(ElTableColumn)
	.use(ElPagination)
	.use(ElDialog)
	.use(ElPopconfirm)
	.use(ElUpload)
	.use(ElLoading)
	.use(ElSelect)
	.use(ElOption)
	.use(ElRadioGroup)
	.use(ElRadio)
	.use(ElCascader)
	.use(ElCheckbox)
	.use(ElInputNumber)

Sentry.init({
	dsn: 'https://f866b695d21d467ba523f1adf14e0a24@o584908.ingest.sentry.io/5737358',
	integrations: [new Integrations.BrowserTracing()],

	tracesSampleRate: 1.0,
})

app.mount('#app')
