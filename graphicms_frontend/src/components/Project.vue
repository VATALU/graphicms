<template>
  <v-app>
    <div class="hide-overflow" style="position: relative;">
      <NavigateBar/>
      <div id="scrolling" class="scroll-y" overflow-y: auto>
        <v-content>
          <v-flex xs12 sm12>
            <v-container fluid grid-list-md>
              <v-layout row wrap>
                <v-flex
                  v-for="(project,index) in projects"
                  :key="project._id"
                  @click="routeProjectDashBoard(project._id)"
                  xs12
                  sm2
                >
                  <v-card>
                    <v-btn flat icon @click.stop="showDeleteDialog(index)" color="grey">
                      <v-icon>clear</v-icon>
                    </v-btn>

                    <v-card-title>
                      <v-spacer></v-spacer>
                      <v-avatar
                        v-bind:color="colors[index%colors.length]"
                        size="100"
                      >
                        <span class="white--text headline">{{project.name.substring(0,1)}}</span>
                      </v-avatar>
                      <v-spacer></v-spacer>
                    </v-card-title>
                    <v-card-title>
                      <v-spacer></v-spacer>
                      <h1 class="headline">{{project.name}}</h1>
                      <v-spacer></v-spacer>
                    </v-card-title>
                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-avatar icon v-if="project.auth.includes('read')">
                        <v-icon>remove_red_eye</v-icon>
                      </v-avatar>
                      <v-avatar icon v-if="project.auth.includes('write')">
                        <v-icon>create</v-icon>
                      </v-avatar>
                      <v-avatar v-if="project.auth.includes('super')">
                        <v-icon color="red">vpn_key</v-icon>
                      </v-avatar>
                      <v-spacer></v-spacer>
                    </v-card-actions>
                  </v-card>
                </v-flex>
                <v-flex xs12 sm2>
                  <v-card @click="dialog=true">
                    <v-avatar dark>
                      <v-icon dark>clear</v-icon>
                    </v-avatar>
                    <v-card-title>
                      <v-spacer></v-spacer>
                      <v-avatar color="grey" size="100">
                        <v-icon dark large>add</v-icon>
                      </v-avatar>
                      <v-spacer></v-spacer>
                    </v-card-title>
                    <v-card-title>
                      <v-spacer></v-spacer>
                      <h1 class="headline">Create Project</h1>
                      <v-spacer></v-spacer>
                    </v-card-title>
                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-avatar icon>
                        <v-icon dark>remove_red_eye</v-icon>
                      </v-avatar>
                      <v-avatar icon>
                        <v-icon dark>create</v-icon>
                      </v-avatar>
                      <v-avatar>
                        <v-icon dark>vpn_key</v-icon>
                      </v-avatar>
                      <v-spacer></v-spacer>
                    </v-card-actions>
                  </v-card>
                </v-flex>
              </v-layout>
            </v-container>
          </v-flex>

          <v-dialog v-model="dialog" width="500px">
            <v-card>
              <v-card-title class="headline grey lighten-2">Create Project</v-card-title>
              <v-divider></v-divider>
              <v-card-text>
                <v-container grid-list-md>
                  <v-layout wrap>
                    <v-flex v-for="(v,index) in values">
                      <v-flex xs12>
                        <v-text-field v-model="editProject[v]" v-bind:label="v"></v-text-field>
                      </v-flex>
                    </v-flex>
                  </v-layout>
                </v-container>
              </v-card-text>
              <v-divider></v-divider>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="red darken-1" flat @click="close">Cancel</v-btn>
                <v-btn color="blue darken-1" flat @click="create">Create</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>

          <v-dialog v-model="deleteDialog" width="500px">
            <v-card>
              <v-card-title class="headline grey lighten-2">Delete Project</v-card-title>
              <v-divider></v-divider>
              <v-card-text>
                Are you sure you want to delete the project
                <b>{{deleteIndex>-1?projects[deleteIndex].name:''}}</b>
                ?
                <br>This can not be reverted!
              </v-card-text>
              <v-divider></v-divider>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="red darken-1" flat @click="close">Cancel</v-btn>
                <v-btn
                  color="blue darken-1"
                  flat
                  @click="deleteProject(projects[deleteIndex]._id)"
                >Delete</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
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
    colors: ["#ca3e6b", "#fa8383", "#9dd3cc", "#ffe4b3"],
    projects: [],
    dialog: false,
    deleteDialog: false,
    deleteIndex: -1,
    values: ["name", "description"],
    editProject: {},
    defaultProject: {
      name: null,
      description: null
    }
  }),
  mounted: function() {
    this.getProjects(this.$route.params.userId);
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
    getProjects(userId) {
      axios.get("http://localhost:8081/api/projects/" + userId).then(res => {
        this.projects = res.data.projects;
      });
    },
    routeProjectDashBoard(project_id) {
      this.$router.push("/project/" + project_id + "/dashboard");
    },
    close() {
      this.editProject = this.defaultProject;
      this.dialog = false;
    },
    create() {
      console.log(this.editProject);
      axios
        .post("http://localhost:8081/api/user/" + this.userId + "/project", {
          project: this.editProject,
          auth: "readwritesuper"
        })
        .then(res => {
          if (res.data.success == true) {
            this.editProject._id = res.data.projectId;
            this.editProject.auth = "readwritesuper";
            this.projects.push(this.editProject);
            this.close();
          }
        });
    },
    deleteProject(projectId) {
      axios
        .delete(
          "http://localhost:8081/api/user/" +
            this.userId +
            "/project/" +
            projectId
        )
        .then(res => {
          if (res.data.success == true) {
            var index = this.deleteIndex;
            this.deleteIndex = -1;
            this.projects.splice(index, 1);
            this.deleteDialog = false;
          }
        });
    },
    showDeleteDialog(index) {
      this.deleteIndex = index;
      this.deleteDialog = true;
    },
    closeDeleteDialog() {
      this.deleteDialog = false;
      this.deleteIndex = -1;
    }
  }
};
</script>
<style>
</style>
