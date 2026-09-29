


import { useState } from "react";
import { login } from "../api/auth";
import { useNavigate } from "react-router-dom";
import { getJwtPayload } from "../utils/jwt";
import "./Auth.css";

function Login() {
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const handleLogin = async (
        event: React.SubmitEvent<HTMLFormElement>
    ) => {
        event.preventDefault();

        setError("");
        setLoading(true);

        try {
            const response = await login({ email, password });

            localStorage.setItem("accessToken", response.access);
            localStorage.setItem("refreshToken", response.refresh);

            const payload = getJwtPayload(response.access);

            if (payload.role === "ADMIN") {
                navigate("/admin");
            } else {
                navigate("/customer");
            }
        } catch (error) {
            console.error(error);
            setError("Невірний email або пароль");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-page">
            <div className="auth-card">

                <h1>Вхід</h1>

                <form
                    className="auth-form"
                    onSubmit={handleLogin}
                >

                    <div className="form-group">
                        <label htmlFor="email">
                            Email
                        </label>

                        <input
                            id="email"
                            type="email"
                            value={email}
                            onChange={(event) =>
                                setEmail(event.target.value)
                            }
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">
                            Пароль
                        </label>

                        <input
                            id="password"
                            type="password"
                            value={password}
                            onChange={(event) =>
                                setPassword(event.target.value)
                            }
                            required
                        />
                    </div>

                    {error && (
                        <p className="auth-error">
                            {error}
                        </p>
                    )}

                    <button
                        className="auth-submit-button"
                        type="submit"
                        disabled={loading}
                    >
                        {loading
                            ? "Вхід..."
                            : "Увійти"
                        }
                    </button>

                </form>

            </div>
        </div>
    );
    
}

export default Login;
