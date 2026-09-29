
import * as XLSX from "xlsx";
import type { Order } from "../api/orders";

export function exportOrderToExcel(order: Order) {
    const rows = order.items.map((item) => ({
        "Номер замовлення": order.id,
        "Дата замовлення": new Date(order.createdAt).toLocaleString("uk-UA"),
        "ID клієнта": order.customerId,
        "1C код клієнта": order.customerCode,
        "1С назва клієнта": order.customerName,
        "Код товару": item.productCode,
        "Назва товару": item.productName,
        "Ціна": Number(item.price),
        "Кількість": Number(item.quantity),
        "Сума": Number(item.amount),
    }));

    const worksheet = XLSX.utils.json_to_sheet(rows);

    const workbook = XLSX.utils.book_new();

    XLSX.utils.book_append_sheet(
        workbook,
        worksheet,
        `Замовлення ${order.id}`
    );

    XLSX.writeFile(
        workbook,
        `Замовлення_${order.id}.xlsx`
    );
}