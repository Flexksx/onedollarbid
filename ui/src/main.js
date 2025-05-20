import { createApp } from "vue";
import App from "./App.vue";
import router from "./router/index";
import { createPinia } from "pinia"; // Import createPinia

const app = createApp(App);
const pinia = createPinia(); // Create Pinia instance

app.use(router);
app.use(pinia); // Use Pinia

app.mount("#app");
