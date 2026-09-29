
import { useNavigate } from "react-router-dom";
import "./Welcome.css";

export default function Welcome() {
  const navigate = useNavigate();

  return (
    <div className="welcome-page">
      <div className="welcome-content">
        <div className="welcome-text">
          <h1>Вітаємо!</h1>

          <p>
            Раді бачити вас у нашому магазині!
          </p>

          <span>
            Щоб продовжити, увійдіть у свій обліковий запис
            або створіть новий.
          </span>
        </div>

        <div className="welcome-actions">
          <button
            type="button"
            onClick={() => navigate("/login")}
          >
            Увійти
          </button>

          <button
            type="button"
            className="secondary-button"
            onClick={() => navigate("/register")}
          >
            Зареєструватися
          </button>
        </div>
      </div>
    </div>
  );
}