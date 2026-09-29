
import { useEffect, useState } from "react";
import { getAllOrders, getMyOrders, type Order } from "../api/orders";
import { getJwtPayload } from "../utils/jwt";
import "./Orders.css";
import { exportOrderToExcel } from "../utils/exportOrderToExcel";

function Orders() {

    const [orders, setOrders] = useState<Order[]>([]);
    const [selectedOrder, setSelectedOrder] =
        useState<Order | null>(null);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const loadOrders = async () => {
        try {
            setLoading(true);
            setError("");

            const token = localStorage.getItem("accessToken");

            if (!token) {
                throw new Error("Користувач не авторизований");
            }

            const payload = getJwtPayload(token);

            console.log("JWT payload:", payload);

            let result: Order[];

            if (payload.role === "ADMIN") {
                result = await getAllOrders();
            } else {
                result = await getMyOrders();
            }

            console.log("Завантажені замовлення:", result);

            setOrders(result);

        } catch (error) {
            console.error("Помилка завантаження замовлень:", error);

            setError("Не вдалося завантажити замовлення");

        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadOrders();
    }, []);

    if (error) {
        return (
            <div className="orders-page">
                <h1>Замовлення</h1>
                <p className="orders-error">
                    {error}
                </p>
            </div>
        );
    }

    return (
        <div className="orders-page">
            {/* 
            <h1 className="orders-title">
                Замовлення
            </h1> */}

            <div className="orders-header">
                <h1 className="orders-title">Замовлення</h1>

                <button
                    type="button"
                    className="details-button"
                    onClick={loadOrders}
                    disabled={loading}
                >
                    {loading ? "ЗАВАНТАЖЕННЯ..." : "ПЕРЕГЛЯНУТИ"}
                </button>
            </div>

            <div className="orders-table-container">

                <table className="orders-table">

                    <thead>
                        <tr>
                            <th>№</th>
                            <th>Номер замовлення</th>
                            <th>Дата</th>
                            <th>Клієнт код 1С</th>
                            <th>Клієнт 1С</th>
                            <th>Сума</th>
                            <th>Дії</th>
                        </tr>
                    </thead>

                    <tbody>

                        {orders.length === 0 ? (

                            <tr>
                                <td
                                    colSpan={6}
                                    className="orders-empty"
                                >
                                    Замовлень немає
                                </td>
                            </tr>

                        ) : (

                            orders.map((order, index) => (

                                <tr
                                    key={order.id}
                                    onClick={() =>
                                        setSelectedOrder(order)
                                    }
                                    className={
                                        selectedOrder?.id === order.id
                                            ? "selected-order"
                                            : ""
                                    }
                                >

                                    <td>
                                        {index + 1}
                                    </td>

                                    <td>
                                        № {order.id}
                                    </td>

                                    <td>
                                        {new Date(
                                            order.createdAt
                                        ).toLocaleString("uk-UA")}
                                    </td>

                                    <td>
                                        {order.customerCode}
                                    </td>

                                    <td>
                                        {order.customerName}
                                    </td>

                                    <td className="order-amount">
                                        {Number(
                                            order.totalAmount
                                        ).toFixed(2)}
                                    </td>

                                    <td>
                                        <button
                                            className="details-button"
                                            onClick={event => {
                                                event.stopPropagation();
                                                setSelectedOrder(order);
                                            }}
                                        >
                                            Переглянути
                                        </button>

                                        <button
                                            type="button"
                                            className="details-button"
                                            onClick={(event) => {
                                                event.stopPropagation();
                                                exportOrderToExcel(order);
                                            }}
                                        >
                                            XLS
                                        </button>
                                    </td>

                                </tr>

                            ))

                        )}

                    </tbody>

                </table>

            </div>


            {/* Деталі замовлення */}

            {selectedOrder && (

                <div className="order-details">

                    <div className="order-details-header">

                        <h2>
                            Замовлення №
                            {selectedOrder.id}
                        </h2>

                        <button
                            className="close-button"
                            onClick={() =>
                                setSelectedOrder(null)
                            }
                        >
                            ✕
                        </button>

                    </div>

                    <table className="order-items-table">

                        <thead>
                            <tr>
                                <th>№</th>
                                <th>Код</th>
                                <th className="product-name">
                                    Найменування
                                </th>
                                <th>Ціна</th>
                                <th>Кількість</th>
                                <th>Сума</th>
                            </tr>
                        </thead>

                        <tbody>

                            {selectedOrder.items.map(
                                (item, index) => (

                                    <tr key={item.id}>

                                        <td>
                                            {index + 1}
                                        </td>

                                        <td>
                                            {
                                                item.productCode
                                            }
                                        </td>

                                        <td className="product-name">
                                            {
                                                item.productName
                                            }
                                        </td>

                                        <td className="number">
                                            {Number(
                                                item.price
                                            ).toFixed(2)}
                                        </td>

                                        <td>
                                            {item.quantity}
                                        </td>

                                        <td className="number">
                                            {Number(
                                                item.amount
                                            ).toFixed(2)}
                                        </td>

                                    </tr>

                                )
                            )}

                        </tbody>

                        <tfoot>

                            <tr>

                                <td
                                    colSpan={5}
                                    className="details-total-label"
                                >
                                    Всього:
                                </td>

                                <td className="details-total">
                                    {Number(
                                        selectedOrder.totalAmount
                                    ).toFixed(2)}
                                </td>

                            </tr>

                        </tfoot>

                    </table>

                </div>

            )}

        </div>
    );
}

export default Orders;