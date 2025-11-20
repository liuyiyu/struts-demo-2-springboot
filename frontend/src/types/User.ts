export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
}

export interface CreateUserRequest {
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
}

export interface UpdateUserRequest {
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
}

export interface ValidationError {
  timestamp: string;
  status: number;
  error: string;
  errors: Record<string, string>;
}

export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
}
