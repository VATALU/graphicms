<template>
  <v-app>
    <div class="hide-overflow" style="position: relative;">
      <NavigateBar/>
      <div id="scrolling" class="scroll-y" overflow-y: auto>
        <v-content>
          <v-container>
            <v-layout align-center>
              <v-flex xs3 offset-xs4>
                <v-card>
                  <!-- <v-card-title>
                    <v-avatar>
                      <v-img src="https://randomuser.me/api/portraits/men/85.jpg"></v-img>
                    </v-avatar>
                    <h2 class="headline font-weight-light">{{user.name}}</h2>
                    <v-spacer></v-spacer>
                  </v-card-title>-->
                  <v-card-actions>
                    <v-list-tile>
                      <v-list-tile-avatar>
                        <v-img :src="user.avatar"></v-img>
                      </v-list-tile-avatar>
                      <v-list-tile-content>
                        <v-list-tile-title>{{user.name}}</v-list-tile-title>
                      </v-list-tile-content>
                    </v-list-tile>
                  </v-card-actions>
                  <v-spacer></v-spacer>
                  <v-card-actions>
                    <v-list-tile>
                      <v-list-tile-avatar>
                        <v-icon>email</v-icon>
                      </v-list-tile-avatar>
                      <v-list-tile-content>
                        <v-list-tile-title>{{user.email}}</v-list-tile-title>
                      </v-list-tile-content>
                    </v-list-tile>
                  </v-card-actions>
                  <v-spacer></v-spacer>
                  <v-divider></v-divider>
                  <v-card-actions v-if="isSelf">
                    <v-spacer></v-spacer>
                    <v-btn @click="signout">SIGN OUT</v-btn>
                    <v-spacer></v-spacer>
                  </v-card-actions>
                </v-card>
              </v-flex>
            </v-layout>
          </v-container>
        </v-content>
      </div>
    </div>
  </v-app>
</template>
<script>
import { mapState } from "vuex";
import NavigateBar from "./NavigateBar.vue";
import axios from "axios";
export default {
  components: {
    NavigateBar
  },
  data: () => ({
    user: {},
    isSelf:false,
  }),
  created: function() {
    this.getUser();
  },
  computed: mapState({
    userId: function(state) {
      let localUserId = localStorage.getItem("userId");
      if (state.userId == "" && localUserId) {
        this.$store.commit("setUserId", localUserId);
      }
      return state.userId;
    }
  }),
  methods: {
    getUser() {
      axios
        .get("http://localhost:8081/api/user/" + this.$route.params.userId)
        .then(res => {
          if (res.data.success == true) {
            this.user = res.data.data;
            if(this.user._id==this.userId)
              this.isSelf=true;
          }
        });
    },
    signout() {
      this.$store.commit("logout");
      this.$store.commit("clearUserId");
       this.$store.commit("clearModels");
       this.$store.commit("clearCallTime");
      localStorage.removeItem("token");
      localStorage.removeItem("userId");
      localStorage.removeItem("isLogined");
      this.$router.push("/login");
    },
  }
};
</script>

