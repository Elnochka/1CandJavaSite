const API_URL = "http://localhost:8088/api/auth";

export interface LoginRequest {
    email: string;
    password: string;
}

export interface RegisterRequest {
    email: string;
    password: string;
    code1C: string;
    name1C: string;
}

export interface AuthResponse {
    access: string;
    refresh: string;
}

export async function login(
    data: LoginRequest
): Promise<AuthResponse> {

    const response = await fetch(`${API_URL}/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });

    if (!response.ok) {
        throw new Error("Невірний email або пароль");
    }

    return response.json();
}

export async function register(
    data: RegisterRequest
): Promise<AuthResponse> {

    const response = await fetch(`${API_URL}/register`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });

    if (!response.ok) {
        throw new Error("Помилка реєстрації");
    }

    return response.json();
}