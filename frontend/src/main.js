import App from './App'
import IconFont from './components/IconFont/IconFont.vue'
import { startHeartbeat } from './utils/keep-alive.js'

// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
Vue.config.productionTip = false

// 全局注册IconFont组件
Vue.component('IconFont', IconFont)

// 启动心跳，防止Render免费tier休眠
startHeartbeat()

App.mpType = 'app'
const app = new Vue({
  ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)

  // 启动心跳，防止Render免费tier休眠
  startHeartbeat()

  return {
    app
  }
}
// #endif