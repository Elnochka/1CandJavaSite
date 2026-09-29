
import { useState, useEffect } from "react";
import { createOrder } from "../api/orders";
import "./Customer.css";
import Orders from "./Orders";
import { getProducts, type Product } from "../api/products";


interface OrderItem {
    product: Product;
    quantity: number;
}

function Customer() {
    const [orderItems, setOrderItems] = useState<OrderItem[]>([]);
    const [selectedProductId, setSelectedProductId] =
        useState<number | "">("");
    const [quantity, setQuantity] = useState<number>(1);
    const [message, setMessage] = useState("");
    const [products, setProducts] = useState<Product[]>([]);
    const [loading, setLoading] = useState(false);
    const [, setError] = useState("");

    const loadProducts = async () => {
        try {
            setLoading(true);
            setError("");

            const result = await getProducts();

            setProducts(result);
        } catch (error) {
            console.error(error);
            setError("Не вдалося завантажити товари");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadProducts();
    }, []);

    const addProduct = () => {
        if (selectedProductId === "") {
            return;
        }

        const product = products.find(
            p => p.id === selectedProductId
        );

        if (!product || quantity <= 0) {
            return;
        }

        setOrderItems(prev => {
            const existingItem = prev.find(
                item => item.product.id === product.id
            );

            if (existingItem) {
                return prev.map(item =>
                    item.product.id === product.id
                        ? {
                            ...item,
                            quantity:
                                item.quantity + quantity,
                        }
                        : item
                );
            }

            return [
                ...prev,
                {
                    product,
                    quantity,
                },
            ];
        });

        setSelectedProductId("");
        setQuantity(1);
        setMessage("");
    };

    const removeProduct = (productId: number) => {
        setOrderItems(prev =>
            prev.filter(
                item => item.product.id !== productId
            )
        );
    };

    const changeQuantity = (
        productId: number,
        newQuantity: number
    ) => {
        if (newQuantity <= 0) {
            removeProduct(productId);
            return;
        }

        setOrderItems(prev =>
            prev.map(item =>
                item.product.id === productId
                    ? {
                        ...item,
                        quantity: newQuantity,
                    }
                    : item
            )
        );
    };

    const total = orderItems.reduce(
        (sum, item) =>
            sum + item.product.price * item.quantity,
        0
    );

    const handleCreateOrder = async () => {
        if (orderItems.length === 0) {
            setMessage("Додайте хоча б один товар");
            return;
        }

        try {
            const request = {
                items: orderItems.map(item => ({
                    productId: item.product.id,
                    productCode: item.product.productCode,
                    productName: item.product.productName,
                    price: item.product.price,
                    quantity: item.quantity,
                })),
            };

            const result = await createOrder(request);

            console.log("Created order:", result);

            setMessage(
                `Замовлення №${result.id} успішно створено`
            );

            setOrderItems([]);
        } catch (error) {
            console.error(error);

            setMessage(
                "Не вдалося створити замовлення"
            );
        }
    };

    return (
        <div className="customer-page">

            <div className="customer-content">

                <h1 className="customer-title">
                    Нове замовлення
                </h1>

                <div className="products-header">
                    
                    <button
                        type="button"
                        className="details-button"
                        onClick={loadProducts}
                        disabled={loading}
                    >
                        {loading ? "ЗАВАНТАЖЕННЯ..." : "ОНОВИТИ ТОВАРИ"}
                    </button>
                </div>

                <div className="add-panel">

                    <select
                        value={selectedProductId}
                        onChange={e =>
                            setSelectedProductId(
                                e.target.value === ""
                                    ? ""
                                    : Number(e.target.value)
                            )
                        }
                        className="product-select"
                    >
                        <option value="">
                            -- Виберіть товар --
                        </option>

                        {products.map(product => (
                            <option
                                key={product.id}
                                value={product.id}
                            >
                                {product.productCode} — {product.productName} — {product.price} грн
                            </option>
                        ))}
                    </select>

                    <input
                        type="number"
                        min="1"
                        value={quantity}
                        onChange={e =>
                            setQuantity(
                                Number(e.target.value)
                            )
                        }
                        className="quantity-input"
                    />

                    <button
                        onClick={addProduct}
                        className="add-button"
                    >
                        + Додати товар
                    </button>

                </div>

                <div className="order-table-container">

                    <table className="order-table">

                        <thead>
                            <tr>
                                <th>№</th>
                                <th>Код</th>
                                <th className="name-column">
                                    Найменування
                                </th>
                                <th>Ціна</th>
                                <th>Кількість</th>
                                <th>Сума</th>
                                <th></th>
                            </tr>
                        </thead>

                        <tbody>

                            {orderItems.length === 0 ? (

                                <tr>
                                    <td
                                        colSpan={7}
                                        className="empty-row"
                                    >
                                        Замовлення порожнє.
                                        Додайте товар.
                                    </td>
                                </tr>

                            ) : (

                                orderItems.map(
                                    (item, index) => (

                                        <tr
                                            key={item.product.id}
                                        >
                                            <td>
                                                {index + 1}
                                            </td>

                                            <td>
                                                {item.product.productCode}
                                            </td>

                                            <td className="name-column">
                                                {item.product.productName}
                                            </td>

                                            <td className="number-column">
                                                {item.product.price.toFixed(2)}
                                            </td>

                                            <td>
                                                <input
                                                    type="number"
                                                    min="1"
                                                    value={item.quantity}
                                                    onChange={e =>
                                                        changeQuantity(
                                                            item.product.id,
                                                            Number(
                                                                e.target.value
                                                            )
                                                        )
                                                    }
                                                    className="table-quantity"
                                                />
                                            </td>

                                            <td className="number-column">
                                                {(
                                                    item.product.price *
                                                    item.quantity
                                                ).toFixed(2)}
                                            </td>

                                            <td>
                                                <button
                                                    onClick={() =>
                                                        removeProduct(
                                                            item.product.id
                                                        )
                                                    }
                                                    className="delete-button"
                                                    title="Видалити"
                                                >
                                                    ✕
                                                </button>
                                            </td>
                                        </tr>
                                    )
                                )
                            )}

                        </tbody>

                        {orderItems.length > 0 && (
                            <tfoot>
                                <tr>
                                    <td
                                        colSpan={5}
                                        className="total-label"
                                    >
                                        Всього:
                                    </td>

                                    <td className="total-value">
                                        {total.toFixed(2)}
                                    </td>

                                    <td></td>
                                </tr>
                            </tfoot>
                        )}

                    </table>

                </div>

                <div className="order-bottom">

                    {message && (
                        <div className="order-message">
                            {message}
                        </div>
                    )}

                    <button
                        onClick={handleCreateOrder}
                        disabled={orderItems.length === 0}
                        className="create-order-button"
                    >
                        Створити замовлення
                    </button>

                </div>

            </div>

            <Orders />

        </div>
    );
}

export default Customer;
