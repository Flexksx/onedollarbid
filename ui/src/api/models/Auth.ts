export interface AuthCredentials {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  role?: string;
}

export interface TokenRequest {
  username: string;
  role: string;
}

export interface AuthResponse {
  token: string;
}

export interface JwtPayload {
  sub: string; // subject (username)
  roles: string[];
  exp: number; // expiration timestamp
  iat: number; // issued at timestamp
}
