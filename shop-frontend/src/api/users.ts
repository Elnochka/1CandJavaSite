
export interface User {
    id: number;
    code1C: string;
    name1C: string;
    email: string;
    role: string;
}

const API_URL = "http://localhost:8088/api/users";

function getHeaders() {
    const token = localStorage.getItem("accessToken");

    return {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
    };
}

export async function getUsers(): Promise<User[]> {
    const response = await fetch(API_URL, {
        method: "GET",
        headers: getHeaders(),
    });

    if (!response.ok) {
        throw new Error("Не вдалося завантажити користувачів");
    }

    return response.json();
}

export async function updateUser(
    id: number,
    user: Omit<User, "id">
): Promise<User> {
    const response = await fetch(`${API_URL}/${id}`, {
        method: "PUT",
        headers: getHeaders(),
        body: JSON.stringify(user),
    });

    if (!response.ok) {
        throw new Error("Не вдалося оновити користувача");
    }

    return response.json();
}