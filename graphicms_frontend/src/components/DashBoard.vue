<template>
  <v-app>
    <LeftBar/>
    <v-container>
      <v-flex xs12 offset-xs1>
        <v-stepper non-linear>
          <v-stepper-header>
            <v-stepper-step editable step="1">Define a Model</v-stepper-step>

            <v-divider></v-divider>

            <v-stepper-step editable step="2">Creating Content</v-stepper-step>

            <v-divider></v-divider>

            <v-stepper-step step="3" editable>Building your Application</v-stepper-step>
          </v-stepper-header>
        </v-stepper>
      </v-flex>
      <v-flex xs12 offset-xs1>
        <v-container fluid grid-list-sm>
          <v-layout row wrap>
            <v-flex d-flex xs12 md4>
              <v-card elevation="12">
                <v-card-text class="text-xs-center">Content Storage Size</v-card-text>
                <v-card-text class="text-xs-center headline">
                  {{totalSize}}
                  <strong>B</strong>
                </v-card-text>
                <v-sheet class="v-sheet--offset mx-auto" color="cyan">
                  <v-list light>
                    <v-list-tile v-for="s in sizes">
                      <v-list-tile-sub-title>{{s.name}}</v-list-tile-sub-title>
                      <v-list-tile-avatar>{{s.size}}B</v-list-tile-avatar>
                    </v-list-tile>
                  </v-list>
                </v-sheet>
              </v-card>
            </v-flex>
            <v-flex d-flex xs12 md4>
              <v-card color="teal accent-3" dark elevation="12">
                <v-card-text class="text-xs-center">API Call Frequency</v-card-text>
                <v-card-text class="text-xs-center headline">{{callTimes}} Times Today</v-card-text>
                <v-sheet class="v-sheet--offset mx-auto" color="teal accent-3">
                  <v-sparkline
                    :labels="labels"
                    :value="apiValues"
                    color="white"
                    line-width="3"
                    auto-draw
                    smooth
                    height="120"
                    padding="16"
                  ></v-sparkline>
                </v-sheet>
              </v-card>
            </v-flex>
            <v-flex xs12 md4>
              <v-card dark color="purple lighten-1" elevation="12">
                <v-card-text class="text-xs-center headline">Content Owner
                  <v-dialog v-model="dialog" max-width="800px">
                    <v-card>
                      <v-card-title class="headline grey lighten-2">
                        <span>{{ formTitle }}</span>
                      </v-card-title>
                      <v-layout row align-start justify-center>
                        <v-flex d-flex xs12 md5 v-if="editIndex==-1">
                          <v-card flat>
                            <v-text-field
                              hide-details
                              prepend-icon="search"
                              v-model="input"
                              single-line
                              @keyup.enter="searchUsers"
                            ></v-text-field>
                            <v-list>
                              <v-list-tile
                                v-for="user in users"
                                @click="setUserInfo(user.name,user.avatar)"
                              >
                                <v-list-tile-avatar>
                                  <v-img :src="user.avatar"></v-img>
                                </v-list-tile-avatar>
                                <v-list-tile-title>{{user.name}}</v-list-tile-title>
                              </v-list-tile>
                            </v-list>
                          </v-card>
                        </v-flex>
                        <v-flex d-flex xs12 md5>
                          <v-card flat>
                            <v-card-title>
                              <v-spacer></v-spacer>
                              <v-list-tile>
                                <v-list-tile-avatar>
                                  <v-img :src="user.avatar"></v-img>
                                </v-list-tile-avatar>
                                <v-list-tile-title>{{user.name}}</v-list-tile-title>
                              </v-list-tile>
                              <v-spacer></v-spacer>
                            </v-card-title>
                            <v-card-actions>
                              <v-spacer></v-spacer>
                              <v-switch v-model="read" label="Read"></v-switch>
                              <v-switch v-model="write" label="Write"></v-switch>
                              <v-switch v-model="admin" label="Admin"></v-switch>
                              <v-spacer></v-spacer>
                            </v-card-actions>
                            <v-divider></v-divider>
                            <v-card-actions>
                              <v-spacer></v-spacer>
                              <v-btn color="red darken-1" flat @click="close">Cancel</v-btn>
                              <v-btn color="blue darken-1" flat @click="save">Save</v-btn>
                              <v-spacer></v-spacer>
                            </v-card-actions>
                          </v-card>
                        </v-flex>
                      </v-layout>
                    </v-card>
                  </v-dialog>
                </v-card-text>

                <v-list light>
                  <v-list-tile v-for="owner in owners" :key="owner.name">
                    <v-list-tile-avatar @click="routeToPersonalInformation(owner._id)">
                      <v-img :src="owner.avatar"></v-img>
                    </v-list-tile-avatar>
                    <v-list-tile-title>{{owner.name}}</v-list-tile-title>
                    <v-list-tile-action>
                      <v-list-tile-avatar>
                        <v-icon v-show="judgeAuth(owner,'read')">remove_red_eye</v-icon>
                        <v-icon v-show="judgeAuth(owner,'write')">create</v-icon>
                        <v-icon dark color="pink" v-show="judgeAuth(owner,'super')">vpn_key</v-icon>
                      </v-list-tile-avatar>
                    </v-list-tile-action>
                  </v-list-tile>
                </v-list>
                <v-card-text>
                  <v-btn
                    v-if="isSuper"
                    slot="activator"
                    fab
                    right
                    dark
                    color="pink"
                    small
                    @click="dialog=true"
                    absolute
                  >
                    <v-icon dark>add</v-icon>
                  </v-btn>
                </v-card-text>
              </v-card>
            </v-flex>
          </v-layout>
          <!-- <v-flex>
            <v-timeline dense align-top>
              <v-timeline-item
                v-bind:color="colors[Math.floor(Math.random() * colors.length)]"
                small
                v-for="log in logs"
              >
                <v-layout row justify-start align-center>
                  <v-flex md2>
                    <strong>{{log.time}}</strong>
                  </v-flex>
                  <v-flex md1>
                    <v-avatar>
                      <v-img :src="getAvatar(log.name)"></v-img>
                    </v-avatar>
                  </v-flex>
                  <v-flex>
                    <v-list-tile-title>{{log.operation}}</v-list-tile-title>
                  </v-flex>
                </v-layout>
              </v-timeline-item>
            </v-timeline>
          </v-flex>-->
        </v-container>
      </v-flex>
    </v-container>
  </v-app>
</template>

<script>
import { mapState } from "vuex";
import axios from "axios";
import LeftBar from "./LeftBar.vue";
export default {
  components: {
    LeftBar
  },
  data: () => ({
    colors: ["#ca3e6b", "#fa8383", "#9dd3cc", "#ffe4b3"],
    labels: ["2/14", "2/15", "2/16", "2/17", "2/18", "2/19", "2/20", "2/21"],
    dbValues: [0, 5, 7, 12, 14, 15, 23, 30],
    apiValues: [10, 15, 27, 12, 14, 25, 21, 45],
    defaultApiValues: [0, 0, 0, 0, 0, 0, 0, 0],
    totalSize: 0,
    sizes: [],
    owners: [],
    input: "",
    editOwner: {},
    editOwnerDefault: {},
    editIndex: -1,
    isSuper: false,
    dialog: false,
    logs: [
      {
        name: "Smith Noah",
        operation: "Update one Doctor",
        time: "2019/2/25 13:08"
      },
      {
        name: "John Leider",
        operation: "Insert one Doctor",
        time: "2019/2/25 13:07"
      },
      {
        name: "John Leider",
        operation: "Create the model: Doctor",
        time: "2019/2/25 13:06"
      }
    ],
    read: false,
    write: false,
    admin: false,
    users: [],
    user: {}
  }),
  computed: {
    formTitle() {
      return this.editIndex === -1 ? "Add Owner" : "Edit Authority";
    },
    callTimes() {
      return this.$store.state.callTimes;
    },
    ...mapState({
      userId: function(state) {
        let localUserId = localStorage.getItem("userId");
        if (state.userId == "" && localUserId) {
          this.$store.commit("setUserId", localUserId);
        }
        return state.userId;
      }
    })
  },
  created: function() {
    this.getUsers();
    this.getAuth();
  },
  mounted: function () {
    this.getSizes();
  },
  methods: {
    getAvatar(name) {
      for (var i = 0; i < this.owners.length; i++) {
        if (this.owners[i].name == name) return this.owners[i].avatar;
      }
    },
    getUsers() {
      axios
        .get(
          "http://localhost:8081/api/project/" +
            this.$route.params.projectId +
            "/users"
        )
        .then(res => {
          if (res.data.success == true) {
            this.owners = res.data.data;
          }
        });
    },
    getAuth() {
      axios
        .get("http://localhost:8081/api/projects/" + this.userId)
        .then(res => {
          var projects = res.data.projects;
          for (var i = 0; i < projects.length; i++) {
            if (this.$route.params.projectId == projects[i]._id) {
              this.isSuper = res.data.projects[i].auth.includes("super");
              break;
            }
          }
        });
    },
    routeToPersonalInformation(id) {
      this.$router.push("/personalInformation/" + id);
    },
    close() {
      this.user = {};
      this.dialog = false;
    },
    save() {
      var authority = "";
      if (this.read === true) {
        authority += "read";
      }
      if (this.write === true) {
        authority += "write";
      }
      if (this.admin === true) {
        authority += "super";
      }
      axios
        .post(
          "http://localhost:8081/api/user/" +
            this.user.name +
            "/project/" +
            this.$route.params.projectId +
            "/auth",
          {
            auth: authority
          }
        )
        .then(res => {
          if (res.data.success == true) {
            console.log(this.owners);
            var p = [{ auth: authority, _id: this.$route.params.projectId }];
            this.user.projects = p;
            this.owners.push(this.user);
            this.close();
          }
        });
    },
    judgeAuth(owner, str) {
      var projects = owner.projects;
      for (var i = 0; i < projects.length; i++) {
        if (projects[i]._id == this.$route.params.projectId) {
          return projects[i].auth.includes(str);
        }
      }
    },
    searchUsers() {
      axios
        .get("http://localhost:8081/api/user/fulltext/" + this.input)
        .then(res => {
          if (res.data.success == true) {
            this.users = res.data.data;
          }
        });
    },
    setUserInfo(name, avatar) {
      this.user = {
        name: name,
        avatar: avatar
      };
    },
    getSizes() {
      var models = this.$store.state.models;
      for (var i = 0; i < models.length; i++) {
        let id = models[i]._id;
        let name = models[i].name;
        axios
          .get("http://localhost:8081/api/model/" + id + "/size")
          .then(res => {
            if (res.data.success == true) {
              if (res.data.size != 0)
                this.sizes.push({
                  name: name,
                  size: res.data.size
                });
              this.totalSize += res.data.size;
            }
          });
      }
    }
  }
};
</script>

