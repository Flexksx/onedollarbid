import { defineStore } from "pinia";
import AuthService from "../services/AuthService";
import { AuthCredentials, RegisterRequest, TokenRequest } from "../models/Auth";
import { User } from "../models/User";
import { useUserStore } from "./userStore";

interface AuthState {
  token: string | null;
  loading: boolean;
  error: string | null;
  roles: string[];
  isAuthenticated: boolean;
}

export const useAuthStore = defineStore("auth", {
  state: (): AuthState => ({
    token: localStorage.getItem("token"),
    loading: false,
    error: null,
    roles: [],
    isAuthenticated: !!localStorage.getItem("token"),
  }),

  getters: {
    hasRole: (state) => (role: string) => {
      return state.roles.includes(role);
    },
  },

  actions: {
    async login(credentials: AuthCredentials) {
      this.loading = true;
      this.error = null;
      try {
        const response = await AuthService.login(credentials);
        this.setToken(response.token);
        const userStore = useUserStore();
        await userStore.fetchUserByUsername(credentials.username);
        return true;
      } catch (e: any) {
        this.error =
          e.response?.data || "Login failed. Please check your credentials.";
        return false;
      } finally {
        this.loading = false;
      }
    },

    async register(registerData: RegisterRequest) {
      this.loading = true;
      this.error = null;
      try {
        await AuthService.register(registerData);
        return true;
      } catch (e: any) {
        this.error = e.response?.data || "Registration failed.";
        return false;
      } finally {
        this.loading = false;
      }
    },

    async generateToken(tokenRequest: TokenRequest) {
      this.loading = true;
      this.error = null;
      try {
        const response = await AuthService.generateToken(tokenRequest);
        this.setToken(response.token);
        return true;
      } catch (e: any) {
        this.error = e.response?.data || "Token generation failed.";
        return false;
      } finally {
        this.loading = false;
      }
    },

    setToken(token: string) {
      this.token = token;
      this.isAuthenticated = true;
      localStorage.setItem("token", token);

      // Extract roles from token
      this.roles = AuthService.getRolesFromToken(token);
    },

    logout() {
      this.token = null;
      this.roles = [];
      this.isAuthenticated = false;
      localStorage.removeItem("token");

      // Optional: Clear user store data
      const userStore = useUserStore();
      userStore.currentUser = null;
    },

    checkToken() {
      if (!this.token) {
        this.isAuthenticated = false;
        return false;
      }

      const isExpired = AuthService.isTokenExpired(this.token);
      if (isExpired) {
        this.logout();
        this.error = "Your session has expired. Please login again.";
        return false;
      }

      this.isAuthenticated = true;
      this.roles = AuthService.getRolesFromToken(this.token);
      return true;
    },

    init() {
      // Called on app startup to check token validity
      if (this.token) {
        this.checkToken();
      }
    },
  },
});
