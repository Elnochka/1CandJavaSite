


const API_URL = "http://localhost:8088/api/orders";

export interface CreateOrderItemRequest {
    productId: number;
    quantity: number;
}

export interface CreateOrderRequest {
    items: CreateOrderItemRequest[];
}

export interface OrderItem {
    id: number;
    quantity: number;
    price: number;
    amount: number;
    productCode: string;
    productName: string;

    // product: {
    //     id: number;
    //     productCode: string;
    //     productName: string;
    // };
}

export interface Order {
    id: number;
    customerId: number;
    customerCode: string;
    customerName: string;
    totalAmount: number;
    createdAt: string;
    items: OrderItem[];
}


export async function createOrder(
    data: CreateOrderRequest
) {
    const token = localStorage.getItem("accessToken");

    const response = await fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(data),
    });

    if (!response.ok) {
        throw new Error("Помилка створення замовлення");
    }

    return response.json();
}

async function getOrders(url: string): Promise<Order[]> {

    const token = localStorage.getItem("accessToken");

    const response = await fetch(url, {
        method: "GET",
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    if (!response.ok) {
        throw new Error("Не вдалося отримати замовлення");
    }

    return response.json();
}

export async function getMyOrders(): Promise<Order[]> {
    return getOrders(`${API_URL}/my`);
}

export async function getAllOrders(): Promise<Order[]> {
    return getOrders(`${API_URL}/all`);
}