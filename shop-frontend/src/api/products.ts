

export interface Product {
    id: number;
    productCode: string;
    productName: string;
    price: number;
    stock: number;
}

const API_URL = "http://localhost:8088/api/products";

export async function getProducts(): Promise<Product[]> {
    const token = localStorage.getItem("accessToken");

    const response = await fetch(API_URL, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    if (!response.ok) {
        throw new Error("Не вдалося завантажити товари");
    }

    return response.json();
}

// export interface Product {
//     id: number;
//     code: string;
//     name: string;
//     price: number;
// }

// const API_URL = "http://localhost:8088/api/products";

// export async function getProducts(): Promise<Product[]> {
//     const token = localStorage.getItem("accessToken");

//     const response = await fetch(API_URL, {
//         method: "GET",
//         headers: {
//             Authorization: `Bearer ${token}`,
//         },
//     });

//     if (!response.ok) {
//         throw new Error("Не вдалося завантажити товари");
//     }

//     return response.json();
// }