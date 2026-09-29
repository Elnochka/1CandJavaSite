

import { useState } from "react";
import { register } from "../api/auth";
import { useNavigate } from "react-router-dom";
import "./Auth.css";

function Register() {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [code1C, setCode1C] = useState("");
    const [name1C, setName1C] = useState("");

    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleRegister = async (
        event: React.SubmitEvent<HTMLFormElement>
    ) => {
        event.preventDefault();

        setError("");
        setSuccess("");

        if (password !== confirmPassword) {
            setError("Паролі не співпадають");
            return;
        }

        setLoading(true);

        try {
            await register({
                email,
                password,
                code1C: code1C,
                name1C: name1C
            });

            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");

            setSuccess("Користувача успішно створено");

            console.log("Успішна реєстрація");
            navigate("/");

        } catch (error) {
            console.error(error);
            setError("Не вдалося зареєструвати користувача");
        } finally {
            setLoading(false);
        }
    };


    return (
        <div className="auth-page">
            <div className="auth-card">

                <h1>Реєстрація</h1>

                <form
                    className="auth-form"
                    onSubmit={handleRegister}
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

                    <div className="form-group">
                        <label htmlFor="confirmPassword">
                            Повторіть пароль
                        </label>

                        <input
                            id="confirmPassword"
                            type="password"
                            value={confirmPassword}
                            onChange={(event) =>
                                setConfirmPassword(event.target.value)
                            }
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="code1C">
                            Код 1С
                        </label>

                        <input
                            id="code1C"
                            type="text"
                            value={code1C}
                            onChange={(event) =>
                                setCode1C(event.target.value)
                            }
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="name1C">
                            Назва 1С
                        </label>

                        <input
                            id="name1C"
                            type="text"
                            value={name1C}
                            onChange={(event) =>
                                setName1C(event.target.value)
                            }
                            required
                        />
                    </div>

                    {error && (
                        <p className="auth-error">
                            {error}
                        </p>
                    )}

                    {success && (
                        <p className="auth-success">
                            {success}
                        </p>
                    )}

                    <button
                        className="auth-submit-button"
                        type="submit"
                        disabled={loading}
                    >
                        {loading
                            ? "Реєстрація..."
                            : "Зареєструватися"
                        }
                    </button>

                </form>

            </div>
        </div>
    );

}

export default Register;

