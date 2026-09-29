
import { Navigate, Outlet } from "react-router-dom";
import { getJwtPayload } from "../utils/jwt";

interface RoleRouteProps {
    role: "ADMIN" | "CUSTOMER";
}

function RoleRoute({ role }: RoleRouteProps) {
    const token = localStorage.getItem("accessToken");

    if (!token) {
        return <Navigate to="/" replace />;
    }

    try {
        const payload = getJwtPayload(token);

        if (payload.role !== role) {
            if (payload.role === "ADMIN") {
                return <Navigate to="/admin" replace />;
            }

            return <Navigate to="/customer" replace />;
        }

        return <Outlet />;
    } catch (error) {
        console.error("Invalid JWT:", error);
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");

        return <Navigate to="/" replace />;
    }
}

export default RoleRoute;