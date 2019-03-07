<template>
  <v-toolbar app dark scroll-off-screen scroll-target="#scrolling">
    <v-toolbar-side-icon>
      <v-icon>graphic_eq</v-icon>
    </v-toolbar-side-icon>
    <v-toolbar-title>
      <span class="font-weight-thin">GRAPHICMS</span>
    </v-toolbar-title>
    <v-spacer></v-spacer>
    <v-toolbar-items>
      <v-btn icon v-on:click="routeHomepage">
        <v-icon>home</v-icon>
      </v-btn>
      <v-btn v-if="logined" icon v-on:click="routeProjects">
        <v-icon>apps</v-icon>
      </v-btn>
      <v-btn icon v-on:click="routeLogin">
        <v-icon>account_circle</v-icon>
      </v-btn>
    </v-toolbar-items>
  </v-toolbar>
</template>
<script>
  import {mapState} from 'vuex';
export default {
  computed:mapState({
    logined: function(state) {
      let localIsLogined = localStorage.getItem('isLogined');
      if(localIsLogined) {
        this.$store.commit('login');
      }
      return state.isLogined;
    },
    userId: function(state) {
      let localUserId = localStorage.getItem('userId');
      if(state.userId==""&&localUserId) {
        this.$store.commit('setUserId',localUserId);
      }
      return state.userId
    }
  }),
  methods: {
    routeLogin: function() {
      if (this.$store.state.isLogined == true) {
        this.$router.push("/personalInformation/" + this.userId);
      } else {
        this.$router.push("/login");
      }
    },
    routeHomepage: function() {
      this.$router.push("/");
    },
    routeProjects() {
      this.$router.push("/projects/" + this.userId);
    }
  }
};
</script>