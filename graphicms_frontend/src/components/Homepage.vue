<template>
  <v-app>
    <div class="hide-overflow" style="position: relative;">
      <NavigateBar/>
      <div id="scrolling" class="scroll-y" overflow-y: auto>
        <v-content>
          <v-container fluid grid-list-sm>
            <v-timeline dense>
              <v-timeline-item color="cyan">
                <v-layout justify-center align-center row>
                  <v-flex offset-xs1 xs6>
                    <v-img :src="howto_build" aspect-ratio="1" width="80%"></v-img>
                  </v-flex>
                  <v-flex column justify-start xs6>
                    <v-flex>
                      <h2 class="display-1">Define your API</h2>
                    </v-flex>
                    <v-flex>
                      <h2 class=".headline">Within minutes, define everything</h2>
                      <h2 class=".headline">about your GraphQL API - structures,</h2>
                      <h2 class=".headline">relations, powerful permissions.</h2>
                    </v-flex>
                  </v-flex>
                </v-layout>
              </v-timeline-item>
              <v-timeline-item color="green">
                <v-layout justify-center align-center row>
                  <v-flex offset-xs1 xs6>
                    <v-img :src="howto_connect" aspect-ratio="1" width="80%"></v-img>
                  </v-flex>
                  <v-flex column justify-start xs6>
                    <v-flex>
                      <h2 class="display-1">Manage your Content</h2>
                    </v-flex>
                    <v-flex>
                      <h2 class=".headline">We give your content creators the tools</h2>
                      <h2 class=".headline">they're used to - text editors, asset</h2>
                      <h2 class=".headline">workflows, advanced roles.</h2>
                      <h2 class=".headline">No pull requests required.</h2>
                    </v-flex>
                  </v-flex>
                </v-layout>
              </v-timeline-item>
              <v-timeline-item color="pink">
                <v-layout justify-center align-center row>
                  <v-flex offset-xs1 xs6>
                    <v-img :src="howto_manage" aspect-ratio="1" width="80%"></v-img>
                  </v-flex>
                  <v-flex column justify-start xs6>
                    <v-flex>
                      <h2 class="display-1">Connect your Platforms</h2>
                    </v-flex>
                    <v-flex>
                      <h2 class=".headline">Use our GraphQL API to start feeding </h2>
                      <h2 class=".headline">content into any platform of your choice </h2>
                    </v-flex>
                  </v-flex>
                </v-layout>
              </v-timeline-item>
            </v-timeline>
          </v-container>
        </v-content>
      </div>
    </div>
  </v-app>
</template>

<script>
import NavigateBar from "./NavigateBar.vue";
export default {
  components: {
    NavigateBar
  },
  data: () => ({
    howto_build: "./howto-build.webp",
    howto_connect: "./howto-connect.webp",
    howto_manage: "./howto-manage.webp"
  })
};
</script>

<style>
</style>
