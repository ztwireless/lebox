/**
 * Created by pengguanfa on 2017/10/25.
 * 扩展api配置
 */
window.LetoConf = {
	__leto_firstCanvas: true,
	extApi: [
		{
			name: 'testApi',
			fn: function(params) {
				this.showToast(params)
			},
			params: {
				title: ''
			}
		},
		{
			name: 'openPage',
			params: {
				name: '',
				param: {}
			}
		},
		{
			name: 'getCookie',
			params: {
				host: ''
			}
		},
		{
			name: 'openLink',
			params: {
				url: ''
			}
		},
		{
			name: 'aldSendEvent',
			replace: false,
			fn: function() {}
		},
		{
			name: 'reportAnalytics',
			replace: false,
			fn: function() {}
		},
		{
			name: 'isMeizuSync',
			replace: false,
			fn: function() {
				if(window.__letoSys && window.__letoSys.brand && typeof (window.__letoSys.brand) == 'string') {
					return window.__letoSys.brand.toLowerCase() == 'meizu'
				}
				return false
			}
		},
		{
			name: 'ext_compareVersion',
			replace: true,
			fn: function(v1, v2) {
				v1 = v1 || ''
				v2 = v2 || ''
				let list1 = v1.split('.')
				let list2 = v2.split('.')
				let size = Math.min(list1.length, list2.length)
				for(let i = 0; i < size; i++) {
					let i1 = Number(list1[i])
					let i2 = Number(list2[i])
					if(i1 > i2) {
						return 1
					} else if(i1 < i2) {
						return -1
					}
				}
				if(list1.length > list2.length) {
					for(let i = list2.length; i < list1.length; i++) {
						let i1 = Number(list1[i])
						if(i1 > 0) {
							return 1
						}
					}
				} else if(list1.length < list2.length) {
					for(let i = list1.length; i < list2.length; i++) {
						let i2 = Number(list2[i])
						if(i2 > 0) {
							return -1
						}
					}
				}
				return 0
			}
		},
		{
			name: 'ext_createCanvas',
			replace: true,
			fn: function() {
				if(window.LetoConf.__leto_firstCanvas) {
					window.LetoConf.__leto_firstCanvas = false
					return window.canvas
				}
				return window.NativeGlobal.createElement.call(window.NativeGlobal.document, 'canvas')
			}
		},
		{
			name: 'triggerEventToNative',
			replace: false,
			fn: function(ev, params = {}) {
				if(window.ViewJSBridge) {
					window.ViewJSBridge.publish(ev, params)
				} else if(window.ServiceJSBridge) {
					window.ServiceJSBridge.publish(ev, params)
				}
			}
		}
	]
}
window.LetoConf && window.LetoConf.extApi && (window.LetoExtApiConf = window.LetoConf.extApi)
