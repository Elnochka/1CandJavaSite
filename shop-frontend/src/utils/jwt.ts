
export interface JwtPayload {
    sub: string;
    role: "ADMIN" | "CUSTOMER";
    exp: number;
    iat: number;
}

export function getJwtPayload(token: string): JwtPayload {
    const payload = token.split(".")[1];

    const decodedPayload = atob(
        payload.replace(/-/g, "+").replace(/_/g, "/")
    );

    return JSON.parse(decodedPayload);
}