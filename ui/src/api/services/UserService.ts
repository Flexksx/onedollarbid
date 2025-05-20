import axiosInstance from "../axios/axios";
import { User } from "../models/User";

export interface UserCreationPayload {
  username: string;
}

const UserService = {
  getAllUsers: async (): Promise<User[]> => {
    const response = await axiosInstance.get<User[]>("/users");
    return response.data;
  },

  getUserById: async (id: number): Promise<User> => {
    const response = await axiosInstance.get<User>(`/users/${id}`);
    return response.data;
  },

  createUser: async (userData: UserCreationPayload): Promise<User> => {
    const response = await axiosInstance.post<User>("/users", userData);
    return response.data;
  },

  updateUser: async (id: number, userData: Partial<User>): Promise<User> => {
    const response = await axiosInstance.put<User>(`/users/${id}`, userData);
    return response.data;
  },

  deleteUser: async (id: number): Promise<void> => {
    await axiosInstance.delete(`/users/${id}`);
  },

  findByUsername: async (username: string): Promise<User | null> => {
    try {
      const response = await axiosInstance.get<User>(
        `/users/username/${username}`
      );
      return response.data;
    } catch (error: any) {
      if (error.response && error.response.status === 404) {
        return null;
      }
      throw error;
    }
  },
};

export default UserService;
