<template>
  <v-app>
    <LeftBar/>
    <v-content>
      <v-toolbar app>
        <v-spacer></v-spacer>
        <v-btn v-if="isAuthed" flat @click="fieldDialog=true">
          <v-icon left>flip_to_back</v-icon>
          <div class="subheading">ADD FIELD</div>
        </v-btn>

        <v-dialog v-if="isAuthed" v-model="modelDialog" width="30%">
          <v-btn slot="activator" flat>
            <v-icon left>add_circle</v-icon>
            <div class="subheading">Create Model</div>
          </v-btn>

          <v-card>
            <v-progress-linear
              :active="isLoading"
              class="ma-0"
              color="pink "
              height="4"
              indeterminate
            ></v-progress-linear>
            <v-card-title class="headline lighten-2" primary-title>Create Your Model</v-card-title>
            <v-flex xs10 offset-xs1>
              <v-alert
                id="alert"
                :value="errorCreateModelAlert"
                icon="warning"
                outline
                type="error"
              >
                <v-flex text-xs-center>
                  Create
                  <b
                    class="subheading"
                  >{{models.length>0?models[models.length-1].name:''}} Success</b>
                </v-flex>
              </v-alert>
            </v-flex>
            <v-card-text>
              <v-form ref="form" v-model="valid" lazy-validation>
                <v-text-field
                  v-model="modelName"
                  :counter="10"
                  :rules="modelNameRules"
                  label="Display Name"
                  required
                ></v-text-field>

                <v-text-field
                  v-model="modelDescription"
                  :rules="modelDescriptionRules"
                  label="Description"
                  required
                ></v-text-field>
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="primary" :disabled="!valid" flat @click="createModel()">create</v-btn>
                  <v-btn color="error" flat @click="modelDialog = false">cancel</v-btn>
                </v-card-actions>
              </v-form>
            </v-card-text>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </v-content>

    <v-container>
      <draggable v-model="models" :options="{animation:700}">
        <div id="model" v-for="(model,index) in models" :key="model._id">
          <v-flex d-flex xs10 sm12 offset-xs1>
            <v-hover>
              <v-card slot-scope="{ hover }" :class="`elevation-${hover ? 12 : 2}`">
                <v-card-title>
                  <div id="title" class="title">{{model.name}}</div>
                  <div id="description" class="text--primary">#{{model.description}}</div>
                  <v-spacer></v-spacer>

                  <v-tooltip bottom v-if="isAuthed">
                    <v-btn
                      slot="activator"
                      icon
                      small
                      color="error"
                      @click="showDeleteDialog(index)"
                    >
                      <v-icon>remove</v-icon>
                    </v-btn>
                    <span>Delete Model</span>
                  </v-tooltip>
                </v-card-title>
                <draggable
                  v-model="model.fields"
                  :options="{group:{name:'field',pull:'false'},animation:150,sort:false}"
                  @add="onAddField"
                  id="field"
                >
                  <div v-for="(field,fieldIndex) in model.fields" :key="field.name">
                    <v-divider></v-divider>
                    <v-list-tile avatar>
                      <v-list-tile-avatar>
                        <v-icon>{{field.icon}}</v-icon>
                      </v-list-tile-avatar>
                      <v-list-tile-content>
                        <v-list-tile-title>{{ field.name }}</v-list-tile-title>
                        <v-chip disable="true" color="primary" outline small>{{ field.type }}</v-chip>
                      </v-list-tile-content>
                      <!-- <v-tooltip top>
                        <v-btn slot="activator" icon small ripple color="primary">
                          <v-icon>create</v-icon>
                        </v-btn>
                        <span>Edit Field</span>
                      </v-tooltip>-->
                      <v-tooltip top v-if="isAuthed">
                        <v-btn
                          slot="activator"
                          icon
                          small
                          ripple
                          color="error"
                          @click="showDeleteFieldDialog(index,fieldIndex)"
                        >
                          <v-icon>clear</v-icon>
                        </v-btn>
                        <span>Delete Field</span>
                      </v-tooltip>
                    </v-list-tile>
                  </div>
                </draggable>
              </v-card>
            </v-hover>
          </v-flex>
        </div>
      </draggable>

      <v-dialog v-model="deleteModelDialog" width="30%">
        <!-- <v-btn slot="activator" hidden>add</v-btn> -->
        <v-card>
          <v-card-title class="headline grey lighten-2" primary-title>Delete Model</v-card-title>

          <v-card-text>
            Are you sure you want to delete the model
            <b>{{deleteModelIndex<0?'':models[deleteModelIndex].name}}</b>
            ?
            <br>This can not be reverted!
          </v-card-text>
          <v-divider></v-divider>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" flat @click="deleteModelDialog = false">Cancel</v-btn>
            <v-btn
              color="error"
              flat
              @click="deleteModel()"
            >Delete {{deleteModelIndex<0?'':models[deleteModelIndex].name}}</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <v-dialog v-model="fieldDialog" hide-overlay width="15%">
        <v-card>
          <draggable
            v-model="fields"
            :options="{sort:false,group: {name:'field',pull: 'clone',put: false}, animation:100 }"
            @start="onStart"
          >
            <div v-for="element in fields" :key="element.name">
              <v-list-tile>
                <v-list-tile-action>
                  <v-icon>{{element.icon}}</v-icon>
                </v-list-tile-action>
                <v-list-tile-content>
                  <v-list-tile-title v-text="element.name"></v-list-tile-title>
                </v-list-tile-content>
              </v-list-tile>
            </div>
          </draggable>
        </v-card>
      </v-dialog>

      <v-dialog v-model="deleteFieldDialog" width="30%">
        <v-progress-linear :active="isLoading" class="ma-0" color="pink " height="4" indeterminate></v-progress-linear>
        <v-btn hidden slot="activator" icon small ripple color="error">
          <v-icon>clear</v-icon>
        </v-btn>
        <v-card>
          <v-card-title class="headline grey lighten-2" primary-title>Delete Field</v-card-title>
          <v-card-text>
            Are you sure you want to delete the field
            <b>{{deleteFieldIndex<0||deleteModelIndex<0?'':models[deleteModelIndex].fields[deleteFieldIndex].name}}</b>
            ?
            <br>This can not be reverted!
          </v-card-text>
          <v-divider></v-divider>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" flat @click="deleteFieldDialog = false">Cancel</v-btn>
            <v-btn
              color="error"
              flat
              @click="deleteField"
            >Delete {{deleteFieldIndex<0||deleteModelIndex<0?'':models[deleteModelIndex].fields[deleteFieldIndex].name}}</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <v-dialog v-model="addFieldDialog" width="30%">
        <v-card>
          <v-progress-linear
            :active="isLoading"
            class="ma-0"
            color="pink "
            height="4"
            indeterminate
          ></v-progress-linear>
          <v-card-title class="headline" primary-title>Add Your Field</v-card-title>
          <v-flex xs10 offset-xs1>
            <v-alert id="alert" :value="errorCreateFieldAlert" icon="warning" outline type="error">
              <v-flex text-xs-center>
                Add
                <b class="subheading">{{models.length>0?models[models.length-1].name:''}} Success</b>
              </v-flex>
            </v-alert>
          </v-flex>
          <v-card-text>
            <v-form ref="form" v-model="validField" lazy-validation>
              <v-text-field
                v-model="fieldName"
                :counter="20"
                :rules="fieldNameRules"
                label="Display Name"
                required
              ></v-text-field>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" :disabled="!validField" flat @click="addField">add</v-btn>
                <v-btn color="error" flat @click="cancelAddField">cancel</v-btn>
              </v-card-actions>
            </v-form>
          </v-card-text>
        </v-card>
      </v-dialog>
    </v-container>
  </v-app>
</template>

<script>
import { mapState } from "vuex";
import LeftBar from "./LeftBar.vue";
import axios from "axios";
import draggable from "vuedraggable";
export default {
  components: {
    LeftBar,
    draggable
  },
  data() {
    return {
      models: [],
      deleteModelIndex: -1,
      deleteFieldIndex: -1,
      errorCreateModelAlert: false,
      errorCreateFieldAlert: false,
      modelDialog: false,
      fieldDialog: false,
      addFieldDialog: false,
      deleteFieldDialog: false,
      deleteModelDialog: false,
      isLoading: false,
      valid: false,
      validField: false,
      fields: [
        { name: "String", type: "String", icon: "text_fields" },
        { name: "Int", type: "Int", icon: "format_list_numbered" },
        { name: "Float", type: "Float", icon: "waves" },
        { name: "Boolean", type: "Boolean", icon: "toggle_on" }
      ],
      defaultFields: [
         { name: "String", type: "String", icon: "text_fields" },
        { name: "Int", type: "Int", icon: "format_list_numbered" },
        { name: "Float", type: "Float", icon: "waves" },
        { name: "Boolean", type: "Boolean", icon: "toggle_on" }
      ],
      modelName: "",
      modelNameRules: [
        v => !!v || "Name is required",
        v => (v && /^[A-Z]+$/.test(v[0])) || "The first letter must be upper"
      ],
      modelDescription: "",
      modelDescriptionRules: [v => !!v || "Description is required"],
      oldIndex: -1,
      newIndex: -1,
      parentIndex: -1,
      fieldName: "",
      isAuthed: false,
      fieldNameRules: [
        v => !!v || "Name is required",
        v => (v && /^[A-Z]+$/.test(v[0])) || "The first letter must be upper",
        v=>(v&&this.isLowwer(v))||"The rest letters must be lowwer"
      ]
      // fieldDescription: "",
      // fieldDescriptionRules: [v => !!v || "Description is required"]
    };
  },
  created: function() {
    this.getModels();
    this.getAuth();
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
    getModels() {
      axios
        .get(
          "http://localhost:8081/api/project/" +
            this.$route.params.projectId +
            "/models"
        )
        .then(res => {
          if (res.data.success == true) {
            var models = res.data.models;
            for (var i = 0; i < models.length; i++) {
              var fields = models[i].fields;
              if (fields != null)
                for (var j = 0; j < fields.length; j++) {
                  var field = fields[j];
                  switch (field.type) {
                    case "String":
                      field["icon"] = "text_fields";
                      break;
                    case "Int":
                      field["icon"] = "format_list_numbered";
                      break;
                    case "Float":
                      field["icon"] = "waves";
                      break;
                    case "Boolean":
                      field["icon"] = "toggle_on";
                      break;
                    default:
                      break;
                  }
                }
            }
            this.models = models;
          }
          this.$store.commit("addCallTime");
        });
    },
    createModel() {
      var json = {
        name: this.modelName,
        description: this.modelDescription
      };
      this.modelDialog = false;
      this.isLoading = true;
      axios
        .post(
          "http://localhost:8081/api/project/" +
            this.$route.params.projectId +
            "/model",
          json
        )
        .then(res => {
          if (res.data.success == true) {
            json._id = res.data._id;
            this.models.push(json);
            this.modelName = "";
            this.modelDescription = "";
            this.isLoading = false;
            this.$store.commit("addModel", json);
            this.$store.commit("addCallTime");
          } else {
            this.errorCreateModelAlert = true;
          }
        })
        .catch(er => {
          this.isLoading = false;
          this.errorCreateModelAlert = true;
        });
    },
    showDeleteDialog(index) {
      this.deleteModelIndex = index;
      this.deleteModelDialog = true;
    },
    deleteModel() {
      this.deleteModelDialog = false;
      this.isLoading = true;
      axios
        .delete(
          "http://localhost:8081/api/project/" +
            this.$route.params.projectId +
            "/model/" +
            this.models[this.deleteModelIndex]._id
        )
        .then(res => {
          console.log(res.data);
          if (res.data.success == true) {
            this.$store.commit("deleteModel",this.deleteModelIndex);
            this.models.splice(this.deleteModelIndex, 1);
            this.deleteModelIndex = -1;
            this.isLoading = false;
            this.$store.commit("addCallTime");
          }
        })
        .catch(er => {
          this.isLoading = false;
        });
    },
    onAddField(evt) {
      this.oldIndex = evt.oldIndex;
      this.newIndex = evt.newIndex;
      var i = 0;
      var element = evt.to.parentElement.parentElement.parentElement;
      while (element.previousElementSibling != null) {
        i++;
        element = element.previousElementSibling;
      }
      this.parentIndex = i;
      this.fieldDialog = false;
      this.addFieldDialog = true;
    },
    addField() {
      var fieldType = this.fields[this.oldIndex].type;
      var json = {
        name: this.fieldName,
        type: fieldType
      };
      axios
        .put(
          "http://localhost:8081/api/project/" +
            this.$route.params.projectId +
            "/model/" +
            this.models[this.parentIndex]._id,
          json
        )
        .then(res => {
          if (res.data.success == true) {
            if (this.models[this.parentIndex].fields != undefined) {
              this.models[this.parentIndex].fields[
                this.newIndex
              ].name = this.fieldName;
            } else {
              json["icon"] = this.fields[this.oldIndex].icon;
              this.models[this.parentIndex].fields = [json];
            }
            this.fields = this.defaultFields;
            this.fieldName = null;
            this.parentIndex = -1;
            this.oldIndex = -1;
            this.newIndex = -1;
            this.addFieldDialog = false;
            this.$store.commit("addCallTime");
          }
        });
    },
    cancelAddField() {
      if (this.models[this.parentIndex].fields != undefined)
        this.models[this.parentIndex].fields.splice(this.newIndex, 1);
      this.fieldName = null;
      this.parentIndex = -1;
      this.oldIndex = -1;
      this.newIndex = -1;
      this.addFieldDialog = false;
      this.fieldDialog = true;
    },
    showDeleteFieldDialog(index, fieldIndex) {
      this.deleteModelIndex = index;
      this.deleteFieldIndex = fieldIndex;
      this.deleteFieldDialog = true;
    },
    deleteField() {
      axios
        .delete(
          "http://localhost:8081/api/project/" +
            this.$route.params.projectId +
            "/model/" +
            this.models[this.deleteModelIndex]._id +
            "/field/" +
            this.models[this.deleteModelIndex].fields[this.deleteFieldIndex]
              .name
        )
        .then(res => {
          if (res.data.success == true) {
            this.models[this.deleteModelIndex].fields.splice(
              this.deleteFieldIndex,
              1
            );
            this.deleteModelIndex = -1;
            this.deleteFieldIndex = -1;
            this.deleteFieldDialog = false;

            this.$store.commit("addCallTime");
          }
        });
    },
    onStart() {
      this.fieldDialog = false;
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
        });
    },
    isLowwer(v) {
      var is =true;
      for(var i=1;i<v.length;i++){
        if(!/^[a-z]+$/.test(v[i])){
          is=false;
          break;
        }
      }
      return is;
    }
  }
};
</script>

<style>
#description {
  padding-left: 1%;
  width: 30%;
}
#model {
  padding-top: 1%;
}
#type {
  margin: 1px;
}
#field {
  min-height: 10%;
}
</style>

