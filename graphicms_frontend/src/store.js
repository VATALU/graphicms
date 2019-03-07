import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        isLogined: false,
        userId: "",
        models: [],
        callTimes: 0
    },
    mutations: {
        login(state) {
            state.isLogined = true;
        },
        logout(state) {
            state.isLogined = false;
        },
        setUserId(state, userId) {
            state.userId = userId;
        },
        clearUserId(state) {
            state.userId = "";
        },
        addModel(state, model) {
            state.models.push(model);
        },
        setModels(state, models) {
            state.models = models;
        },
        deleteModel(state, i) {
            state.models.slice(i, 1);
        },
        clearModels(state) {
            state.models = [];
        },
        addCallTime(state) {
            state.callTimes++;
        },
        setCallTime(state, num) {
            state.callTimes = nums;
        },
        clearCallTime(state) {
            state.callTimes = 0;
        }
    },
    actions: {

    }
})