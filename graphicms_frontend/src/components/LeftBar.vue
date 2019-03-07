<template>
  <v-navigation-drawer v-model="drawer" :mini-variant="mini" absolute>
    <v-card flat height="95%">
      <v-list class="pa-1">
        <v-list-tile v-if="mini" @click.stop="mini = !mini">
          <v-list-tile-action>
            <v-icon>chevron_right</v-icon>
          </v-list-tile-action>
        </v-list-tile>

        <v-list-tile avatar tag="div">
          <v-list-tile-avatar>
            <v-img :src="user.avatar" @click="routeToPersonalInformation"></v-img>
          </v-list-tile-avatar>
          <v-list-tile-content>
            <v-list-tile-title>{{user.name}}</v-list-tile-title>
          </v-list-tile-content>

          <v-list-tile-action>
            <v-btn icon @click.stop="mini = !mini">
              <v-icon>chevron_left</v-icon>
            </v-btn>
          </v-list-tile-action>
        </v-list-tile>
      </v-list>

      <v-list class="pt-0" dense>
        <v-divider light></v-divider>

        <v-list-tile @click="routeTo('dashboard')">
          <v-list-tile-action>
            <v-icon>dashboard</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>Dashboard</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>

        <v-list-tile @click="routeTo('schema')">
          <v-list-tile-action>
            <v-icon>category</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>Model</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>

        <v-list-group no-action>
          <v-list-tile slot="activator">
            <v-list-tile-action>
              <v-icon>view_list</v-icon>
            </v-list-tile-action>
            <v-list-tile-content>Content</v-list-tile-content>
          </v-list-tile>

          <v-list-tile v-for="model in models" :key="model._id" @click="routeToContent(model._id)">
            <v-list-tile-title>{{model.name}}</v-list-tile-title>
          </v-list-tile>
        </v-list-group>
        <v-list-tile>
          <v-list-tile-title></v-list-tile-title>
        </v-list-tile>
      </v-list>
      <v-card-text>
        <v-btn color="pink" dark absolute bottom small fab @click="routeProject">
          <v-icon>power_settings_new</v-icon>
        </v-btn>
      </v-card-text>
    </v-card>
  </v-navigation-drawer>
</template>

<script>
import { mapState } from "vuex";
import axios from "axios";
export default {
  data() {
    return {
      drawer: true,
      mini: true,
      right: null,
      // models: [],
      user: ""
    };
  },
  computed:{ 
    models(){
      return this.$store.state.models
    },
    ...mapState({
    logined: function(state) {
      let localIsLogined = localStorage.getItem("isLogined");
      if (localIsLogined) {
        this.$store.commit("login");
      }
      return state.isLogined;
    },
    userId: function(state) {
      let localUserId = localStorage.getItem("userId");
      if (state.userId == "" && localUserId) {
        this.$store.commit("setUserId", localUserId);
      }
      return state.userId;
    }
  })},
  created: function() {
    this.getAvatar();
  },
  mounted: function() {
    this.getModels();
  },
  methods: {
    routeTo(name) {
      this.$router.push(
        "/project/" + this.$route.params.projectId + "/" + name
      );
    },
    routeToContent(modelId) {
      this.$router.push(
        "/project/" + this.$route.params.projectId + "/content/" + modelId
      );
    },
    routeProject() {
      this.$router.push("/projects/" + this.userId);
    },
    getModels() {
      axios
        .get(
          "http://localhost:8081/api/project/" +
            this.$route.params.projectId +
            "/models"
        )
        .then(res => {
          if (res.data.success == true) {
            // this.models = res.data.models;
            this.$store.commit("setModels",res.data.models);
          }
        });
    },
    getAvatar() {
      axios.get("http://localhost:8081/api/user/" + this.userId).then(res => {
        if (res.data.success == true) {
          var user = res.data.data;
          user.avatar = "./" + user.avatar;
          this.user = user;
        }
      });
    },
    routeToPersonalInformation() {
      this.$router.push("/personalInformation/" + this.userId);
    }
  }
};
</script>