import { createRouter, createWebHistory } from "vue-router";
import { useAuthStore } from "../api/stores/authStore";
import Items from "../components/items/Items.vue";
import AuctionRoom from "../components/rooms/AuctionRoom.vue";

const routes = [
  {
    path: "/",
    name: "Items",
    component: Items,
  },
  {
    path: "/rooms/:id",
    name: "AuctionRoom",
    component: AuctionRoom,
    props: true,
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("../components/auth/LoginForm.vue"),
    meta: { requiresAuth: false },
  },
  {
    path: "/register",
    name: "Register",
    component: () => import("../components/auth/RegisterForm.vue"),
    meta: { requiresAuth: false },
  },
  {
    path: "/admin",
    name: "Admin",
    component: () => import("../components/admin/AdminDashboard.vue"),
    meta: { requiresAuth: true, requiredRole: "ADMIN" },
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

// Navigation guard
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();

  // Check if the token is valid
  authStore.checkToken();

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    // Redirect to login if authentication is required but user is not authenticated
    next({ name: "Login", query: { redirect: to.fullPath } });
  } else if (to.meta.requiredRole && !authStore.hasRole(to.meta.requiredRole)) {
    // Redirect to home if specific role is required but user doesn't have it
    next({ name: "Home" });
  } else {
    next();
  }
});

export default router;
