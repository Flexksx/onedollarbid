import { defineStore } from "pinia";
import UserService, { UserCreationPayload } from "../services/UserService";
import { User } from "../models/User";

interface UserState {
  users: User[];
  currentUser: User | null;
  loading: boolean;
  error: string | null;
}

export const useUserStore = defineStore("user", {
  state: (): UserState => ({
    users: [],
    currentUser: null,
    loading: false,
    error: null,
  }),
  actions: {
    async fetchAllUsers() {
      this.loading = true;
      this.error = null;
      try {
        this.users = await UserService.getAllUsers();
      } catch (e: any) {
        this.error = e.message || "Failed to fetch users";
      } finally {
        this.loading = false;
      }
    },
    async fetchUserById(id: number) {
      this.loading = true;
      this.error = null;
      try {
        this.currentUser = await UserService.getUserById(id);
      } catch (e: any) {
        this.error = e.message || `Failed to fetch user ${id}`;
        this.currentUser = null;
      } finally {
        this.loading = false;
      }
    },
    async createUser(userData: UserCreationPayload) {
      this.loading = true;
      this.error = null;
      try {
        const newUser = await UserService.createUser(userData);
        this.users.push(newUser);
        this.currentUser = newUser; // Optionally set as current user
      } catch (e: any) {
        this.error = e.message || "Failed to create user";
      } finally {
        this.loading = false;
      }
    },
    async updateUser(id: number, userData: Partial<User>) {
      this.loading = true;
      this.error = null;
      try {
        const updatedUser = await UserService.updateUser(id, userData);
        const index = this.users.findIndex((u) => u.id === id);
        if (index !== -1) {
          this.users[index] = updatedUser;
        }
        if (this.currentUser?.id === id) {
          this.currentUser = updatedUser;
        }
      } catch (e: any) {
        this.error = e.message || `Failed to update user ${id}`;
      } finally {
        this.loading = false;
      }
    },
    async deleteUser(id: number) {
      this.loading = true;
      this.error = null;
      try {
        await UserService.deleteUser(id);
        this.users = this.users.filter((u) => u.id !== id);
        if (this.currentUser?.id === id) {
          this.currentUser = null;
        }
      } catch (e: any) {
        this.error = e.message || `Failed to delete user ${id}`;
      } finally {
        this.loading = false;
      }
    },
    async fetchUserByUsername(username: string) {
      this.loading = true;
      this.error = null;
      try {
        this.currentUser = await UserService.findByUsername(username);
      } catch (e: any) {
        this.error = e.message || `Failed to fetch user ${username}`;
        this.currentUser = null;
      } finally {
        this.loading = false;
      }
    },
  },
});
