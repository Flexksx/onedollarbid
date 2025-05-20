import axiosInstance from "../axios/axios";
import {
  AuthCredentials,
  AuthResponse,
  JwtPayload,
  RegisterRequest,
  TokenRequest,
} from "../models/Auth";

const AuthService = {
  login: async (credentials: AuthCredentials): Promise<AuthResponse> => {
    const response = await axiosInstance.post<AuthResponse>(
      "/auth/login",
      credentials
    );
    return response.data;
  },

  register: async (registerData: RegisterRequest): Promise<any> => {
    const response = await axiosInstance.post<any>(
      "/auth/register",
      registerData
    );
    return response.data;
  },

  generateToken: async (tokenRequest: TokenRequest): Promise<AuthResponse> => {
    const response = await axiosInstance.post<AuthResponse>(
      "/auth/token",
      tokenRequest
    );
    return response.data;
  },

  parseJwt: (token: string): JwtPayload | null => {
    try {
      const base64Url = token.split(".")[1];
      const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split("")
          .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
          .join("")
      );
      return JSON.parse(jsonPayload);
    } catch (error) {
      console.error("Failed to parse JWT token", error);
      return null;
    }
  },

  isTokenExpired: (token: string): boolean => {
    const payload = AuthService.parseJwt(token);
    if (!payload) return true;

    const currentTime = Math.floor(Date.now() / 1000);
    return payload.exp < currentTime;
  },

  getRolesFromToken: (token: string): string[] => {
    const payload = AuthService.parseJwt(token);
    return payload?.roles || [];
  },
};

export default AuthService;
