import Vue from 'vue'
import './plugins/vuetify'
import App from './App.vue'
import Vuetify from 'vuetify/lib';
import Login from './components/Login.vue'
import VueRouter from 'vue-router';
import Homepage from './components/Homepage.vue'
import Project from './components/Project.vue'
import DashBoard from './components/DashBoard.vue'
import Schema from './components/Schema.vue'
import Content from './components/Content.vue'
import PersonalInformation from './components/PersonalInformation.vue'
import store from './store.js'

Vue.config.productionTip = false
Vue.use(Vuetify)
Vue.use(VueRouter)

const routes = [{
    path: '/',
    component: Homepage
  },
  {
    path: '/login',
    component: Login
  },
  {
    path: '/projects/:userId',
    component: Project
  },
  {
    path: '/project/:projectId/dashboard',
    component: DashBoard
  },
  {
    path: '/project/:projectId/schema',
    component: Schema
  },
  {
    path: '/project/:projectId/content/:modelId',
    component: Content
  },
  {
    path:'/personalInformation/:userId',
    component: PersonalInformation
  }
]

const router = new VueRouter({
  routes
})

new Vue({
  el: '#app',
  store,
  router,
  render: h => h(App),
}).$mount('#app')
