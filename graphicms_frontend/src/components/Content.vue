<template>
  <v-app>
    <LeftBar/>
    <v-container>
      <v-flex offset-xs1 xs12>
        <v-toolbar flat color="white">
          <v-toolbar-title>{{model.name}}</v-toolbar-title>
          <v-divider class="mx-2" inset vertical></v-divider>
          <v-spacer></v-spacer>
          <v-dialog v-model="dialog" max-width="500px" v-if="isAuthed">
            <v-btn slot="activator" color="primary" dark class="mb-2">New Item</v-btn>
            <v-card>
              <v-card-title>
                <span class="headline">{{ formTitle }}</span>
              </v-card-title>

              <v-card-text>
                <v-container grid-list-md>
                  <v-layout wrap>
                    <v-flex v-for="(v,index) in values">
                      <v-flex xs12>
                        <v-text-field v-model="editedItems[v]" v-bind:label="v"></v-text-field>
                      </v-flex>
                    </v-flex>
                  </v-layout>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="red darken-1" flat @click="close">Cancel</v-btn>
                <v-btn color="blue darken-1" flat @click="save">Save</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-toolbar>
        <v-data-table :headers="headers" :items="data" class="elevation-1">
          <template   v-slot:items="props">
            <td v-for="(v,index) in values">{{props.item[v]}}</td>
            <td v-if="isAuthed" class="justify-center align-center">
              <v-icon v-if="isAuthed" small class="mr-2" @click="editItem(props.item)">edit</v-icon>
              <v-icon v-if="isAuthed" small @click="deleteItem(props.item)">delete</v-icon>
            </td>
          </template>
        </v-data-table>
      </v-flex>
    </v-container>
  </v-app>
</template>

<script>
import { mapState } from "vuex";
import LeftBar from "./LeftBar.vue";
import axios from "axios";
export default {
  components: {
    LeftBar
  },
  data: () => ({
    isAuthed: false,
    headers: [],
    data: [],
    values: [],
    model: {},
    dialog: false,
    editedItems: {},
    editedIndex: -1,
    defaultItems: {}
  }),
  created: function() {
    this.getAuth();
    this.getData();
    this.getHeaders();
  },
  watch: {
    $route(to, from) {
      this.headers = [];
      this.data = [];
      this.values = [];
      this.model = {};
      this.dialog = false;
      this.editedItems = {};
      this.editedIndex = -1;
      this.defaultItems = {};
      this.getData();
      this.getHeaders();
    }
  },
  computed: {
    formTitle() {
      return this.editedIndex === -1 ? "New Item" : "Edit Item";
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
  methods: {
    getHeaders() {
      axios
        .get(
          "http://localhost:8081/api/project/" +
            this.$route.params.projectId +
            "/model/" +
            this.$route.params.modelId
        )
        .then(res => {
          if (res.data.success == true) {
            var model = res.data.model;
            this.model = model;
            var fields = model.fields;
            for (var i = 0; i < fields.length; i++) {
              this.headers.push({
                text: fields[i].name+"("+fields[i].type+")",
                value: fields[i].name
              });
              this.values.push(fields[i].name);
              this.editedItems[fields[i].name] = "";
            }
            console.log(this.isAuthed);
            if (this.isAuthed) {
              this.headers.push({
                text: "Action",
                value: "action"
              });
            }
            this.defaultItems = this.editedItems;
          }
        });
    },
    getData() {
      axios
        .get(
          "http://localhost:8081/api/project/" +
            this.$route.params.projectId +
            "/model/" +
            this.$route.params.modelId +
            "/content"
        )
        .then(res => {
          if (res.data.success == true) {
            this.data = res.data.data;
            this.$store.commit("addCallTime");
          }
        });
    },
    close() {
      this.editedItems = {};
      this.editedIndex = -1;
      this.dialog = false;
    },
    save() {
      var graphql = "mutation{" + this.model.name + "(";
      for (var key in this.editedItems) {
        var fields = this.model.fields;
        var isString = false;
        for (var j = 0; j < fields.length; j++) {
          if (key == "_id") {
            isString = true;
            break;
          }
          if (fields[j].name == key)
            if (fields[j].type == "String" || fields[j].type == "Date") {
              isString = true;
              break;
            }
        }
        if (isString)
          graphql +=
            " " +
            this.toLowerCaseFirstOne(key) +
            ':"' +
            this.editedItems[key] +
            '"';
        else {
          graphql +=
            " " + this.toLowerCaseFirstOne(key) + ":" + this.editedItems[key];
        }
      }
      graphql += ")}";
      axios
        .post("http://localhost:8081/api/graphql", {
          modelId: this.$route.params.modelId,
          query: graphql
        })
        .then(res => {
          if (this.editedIndex != -1) {
            Object.assign(this.data[this.editedIndex], this.editedItems);
          } else {
            if (JSON.stringify(res.data).indexOf("success") != -1) {
              this.data.push(this.editedItems);
            }
          }
          this.$store.commit("addCallTime");
          this.close();
        })
        .catch(er => {
          console.log(er);
        });
    },
    editItem(item) {
      console.log(item);
      console.log(this.data.indexOf(item));
      this.editedIndex = this.data.indexOf(item);
      console.log(this.editedIndex);
      this.editedItems = item;
      this.dialog = true;
    },

    deleteItem(item) {
      const index = this.data.indexOf(item);
      console.log(item._id)
      if (confirm("Are you sure you want to delete this item?")) {
        axios
          .delete(
            "http://localhost:8081/api/model/" +
              this.$route.params.modelId +
              "/itemId/" +
              item._id
          )
          .then(res => {
            if (res.data.success == true) {
              this.data.splice(index, 1);

              this.$store.commit("addCallTime");
            }
          });
      }
    },
    toUpperCaseFirstOne(str) {
      var stringArr = str.split("");
      for (var i = 0; i < stringArr.length; i++) {
        stringArr[i] = stringArr[i][0].toUpperCase() + stringArr[i].slice(1);
      }
      return stringArr.join("");
    },
    toLowerCaseFirstOne(str) {
      var stringArr = str.split("");
      for (var i = 0; i < stringArr.length; i++) {
        stringArr[i] = stringArr[i][0].toLowerCase() + stringArr[i].slice(1);
      }
      return stringArr.join("");
    },
    getAuth() {
      axios
        .get("http://localhost:8081/api/projects/" + this.userId)
        .then(res => {
          var projects = res.data.projects;
          for (var i = 0; i < projects.length; i++) {
            if (this.$route.params.projectId == projects[i]._id) {
              this.isAuthed = res.data.projects[i].auth.includes("write");
              break;
            }
          }

          this.$store.commit("addCallTime");
        });
    }
  }
};
</script>

