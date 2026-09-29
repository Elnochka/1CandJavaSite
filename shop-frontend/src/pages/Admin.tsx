
import { useState } from "react";
import Orders from "./Orders";
import Users from "./Users";
import "./Admin.css";

function Admin() {
    const [activeTab, setActiveTab] = useState("orders");

    return (
        <div className="admin">
            <div className="tabs">
                <button
                    className={`tab ${activeTab === "orders" ? "active" : ""}`}
                    onClick={() => setActiveTab("orders")}
                >
                    Замовлення
                </button>

                <button
                    className={`tab ${activeTab === "users" ? "active" : ""}`}
                    onClick={() => setActiveTab("users")}
                >
                    Користувачі
                </button>
            </div>

            <div className="tab-content">
                {activeTab === "orders" && <Orders />}
                {activeTab === "users" && <Users />}
            </div>
        </div>
    );

}

export default Admin;
