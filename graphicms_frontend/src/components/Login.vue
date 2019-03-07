<template>
  <v-app>
    <div class="hide-overflow" style="position: relative;">
      <NavigateBar/>
      <div id="scrolling" class="scroll-y" overflow-y: auto>
        <v-content>
          <v-container>
            <v-layout>
              <v-flex d-flex xs12 sm6>
                <v-card color="grey lighten-5" flat>
                  <v-card-text></v-card-text>
                </v-card>
              </v-flex>
            </v-layout>
            <v-layout>
              <v-flex d-flex xs12 sm6>
                <v-card color="grey lighten-5" flat>
                  <v-card-text></v-card-text>
                </v-card>
              </v-flex>
            </v-layout>
            <v-layout align-center justify-center>
              <v-flex xs10 sm4>
                <v-card>
                  <v-progress-linear
                    :active="isUpdating"
                    class="ma-0"
                    color="pink "
                    height="4"
                    indeterminate
                  ></v-progress-linear>
                  <v-card-title>
                    <v-spacer></v-spacer>
                    <v-icon x-large color="grey darken-4">graphic_eq</v-icon>
                    <h3>GraphiCMS</h3>
                    <v-spacer></v-spacer>
                  </v-card-title>
                  <v-tabs fixed-tabs slider-color="pink">
                    <v-tab>LOG IN</v-tab>
                    <v-tab-item>
                      <v-form ref="login" v-model="valid" lazy-validation>
                        <v-card-text>
                          <v-text-field
                            v-model="nameLogin"
                            :disabled="isUpdating"
                            label="USERNAME"
                            :rules="nameRules"
                            hint="Less than 10 characters"
                            required
                          ></v-text-field>
                        </v-card-text>

                        <v-card-text>
                          <v-text-field
                            v-model="passwordLogin"
                            :append-icon="showLogin ? 'visibility_off' : 'visibility'"
                            :rules="passwordRules"
                            :type="showLogin ? 'text' : 'password'"
                            name="input-10-1"
                            label="PASSWORD"
                            hint="At least 8 characters"
                            :disabled="isUpdating"
                            @click:append="showLogin = !showLogin"
                            required
                          ></v-text-field>
                        </v-card-text>
                        <v-card-title v-show="!correctLogin">
                          <v-spacer></v-spacer>
                          <h3 class="red--text font-weight-light">{{errorLogin}}</h3>
                          <v-spacer></v-spacer>
                        </v-card-title>
                        <v-card-actions>
                          <v-spacer></v-spacer>
                          <v-btn :disabled="isUpdating" @click="clearLogin">clear</v-btn>
                          <v-btn :disabled="!valid" :loading="isUpdating" v-on:click="login">LOG IN</v-btn>
                          <v-spacer></v-spacer>
                        </v-card-actions>
                      </v-form>
                    </v-tab-item>

                    <v-tab>SIGN UP</v-tab>
                    <v-tab-item>
                      <v-form ref="signup" v-model="valid" lazy-validation>
                        <v-card-text>
                          <v-text-field
                            v-model="nameSignUp"
                            :disabled="isUpdating"
                            label="USERNAME"
                            :rules="nameRules"
                            hint="Less than 10 characters"
                            required
                          ></v-text-field>
                        </v-card-text>
                        <v-card-text>
                          <v-text-field
                            v-model="email"
                            :disabled="isUpdating"
                            label="EMAIL"
                            :rules="emailRules"
                            required
                          ></v-text-field>
                        </v-card-text>
                        <v-card-text>
                          <v-text-field
                            v-model="passwordSignUp"
                            :append-icon="showSignUp ? 'visibility_off' : 'visibility'"
                            :rules="passwordRules"
                            :type="showSignUp ? 'text' : 'password'"
                            name="input-10-1"
                            label="PASSWORD"
                            hint="At least 8 characters"
                            :disabled="isUpdating"
                            @click:append="showSignUp = !showSignUp"
                            required
                          ></v-text-field>
                        </v-card-text>
                        <v-card-title v-show="!correctSignUp">
                          <v-spacer></v-spacer>
                          <h3 class="red--text font-weight-light">{{errorSignUp}}</h3>
                          <v-spacer></v-spacer>
                        </v-card-title>
                        <v-card-actions>
                          <v-spacer></v-spacer>
                          <v-btn :disabled="isUpdating" @click="clearSignUp">clear</v-btn>
                          <v-btn :disabled="!valid" :loading="isUpdating" @click="signUp">SIGN UP</v-btn>
                          <v-spacer></v-spacer>
                        </v-card-actions>
                      </v-form>
                    </v-tab-item>
                  </v-tabs>
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
import NavigateBar from "./NavigateBar.vue";
import axios from "axios";
export default {
  components: {
    NavigateBar
  },
  data() {
    return {
      valid: true,
      isUpdating: false,
      showLogin: false,
      showSignUp: false,
      correctLogin: true,
      correctSignUp: true,
      passwordLogin: "",
      nameLogin: "",
      nameSignUp: "",
      passwordSignUp: "",
      email: "",
      errorLogin: "",
      errorSignUp: "",
      nameRules: [
        v => !!v || "Name is required",
        v => (v && v.length <= 18) || "Name must be less than 18 characters"
      ],
      passwordRules: [
        value => !!value || "Password is required.",
        v => v.length >= 8 || "Password must be more than 8 characters"
      ],
      emailRules: [
        v => !!v || "E-mail is required",
        v => /.+@.+/.test(v) || "E-mail mustn't be valid"
      ]
    };
  },

  methods: {
    login() {
      if (this.$refs.login.validate()) {
        this.isUpdating = true;
        axios
          .post("http://localhost:8081/api/login", {
            username: this.nameLogin,
            password: this.passwordLogin
          })
          .then(response => {
            if (response.data.success) {
              const token = response.data.data.token;
              localStorage.setItem("token",token);
              this.$store.commit('setUserId', response.data.data.id);
              localStorage.setItem('userId',response.data.data.id);
              this.$store.commit('login');
              localStorage.setItem('isLogined',true);
              this.$router.push("/projects/"+response.data.data.id);
            } else {
              this.isUpdating = false;
              this.nameLogin = "";
              this.passwordLogin = "";
              this.errorLogin = response.data.error;
              this.correctLogin = false;
            }
          })
          .catch(() => {
            this.isUpdating = false;
            this.errorLogin = "Network Error";
          });
      }
    },
    signUp() {
      if (this.$refs.signup.validate()) {
        this.isUpdating = true;
        axios
          .post("http://localhost:8081/api/signup", {
            username: this.nameSignUp,
            password: this.passwordSignUp,
            email: this.email
          })
          .then(response => {
            if (response.data.success) {
              const token = response.data.data.token;
              localStorage.setItem("token",token);
              this.$store.commit('setUserId', response.data.data.id);
              localStorage.setItem('userId',response.data.data.id);
              this.$store.commit('login');
              localStorage.setItem('isLogined',true);
              console.log(response.data)
              this.$router.push("/projects/"+response.data.data.id);
            } else {
              this.isUpdating = false;
              this.nameSignUp = "";
              this.passwordSignUp = "";
              this.errorSignUp = response.data.error;
              this.correctSignUp = false;
            }
          })
          .catch(() => {
            this.isUpdating = false;
            this.errorSignUp = "Network Error";
          });
      }
    },
    clearLogin() {
      (this.nameLogin = ""), (this.passwordLogin = "");
    },
    clearSignUp() {
      (this.nameSignUp = ""), (this.passwordSignUp = ""), (this.email = "");
    }
  }
};
</script>

<style>
</style>
