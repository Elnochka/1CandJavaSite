
import { useNavigate } from "react-router-dom";
import { getJwtPayload } from "../utils/jwt";
import "./Navbar.css";

function Navbar() {
    const navigate = useNavigate();

    const token = localStorage.getItem("accessToken");

    if (!token) {
        return (
            <nav className="navbar">
                <div className="navbar-right">

                    <button
                        onClick={() => navigate("/login")}
                        className="navbar-button"
                    >
                        Вхід
                    </button>

                    <button
                        onClick={() => navigate("/register")}
                        className="navbar-button"
                    >
                        Реєстрація
                    </button>

                </div>
            </nav>
        );
    }

    let payload;

    try {
        payload = getJwtPayload(token);
    } catch (error) {
        console.error("Invalid JWT:", error);
        return null;
    }

    const handleLogout = () => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");

        navigate("/");
    };

    return (
        <nav className="navbar">

            <div className="navbar-right">                

                <span className="navbar-user">
                    {payload.sub}
                </span>

                <button
                    onClick={handleLogout}
                    className="navbar-button"
                >
                    Вийти
                </button>

            </div>

        </nav>
    );
}

export default Navbar;
