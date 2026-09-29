
import { useEffect, useState } from "react";
import { getUsers, updateUser, type User } from "../api/users";
import "./Users.css";

function Users() {
    const [users, setUsers] = useState<User[]>([]);
    const [editingUser, setEditingUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const loadUsers = async () => {
        try {
            setLoading(true);
            setError("");

            const result = await getUsers();

            setUsers(result);
        } catch (error) {
            console.error(error);
            setError("Не вдалося завантажити користувачів");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadUsers();
    }, []);

    const handleSave = async () => {
        if (!editingUser) {
            return;
        }

        try {
            const updatedUser = await updateUser(
                editingUser.id,
                {
                    code1C: editingUser.code1C,
                    name1C: editingUser.name1C,
                    email: editingUser.email,
                    role: editingUser.role,
                }
            );

            setUsers((currentUsers) =>
                currentUsers.map((user) =>
                    user.id === updatedUser.id
                        ? updatedUser
                        : user
                )
            );

            setEditingUser(null);
        } catch (error) {
            console.error(error);
            setError("Не вдалося зберегти користувача");
        }
    };

    if (loading) {
        return <p>Завантаження користувачів...</p>;
    }

    return (
        <div className="users-page">
            <h1>Користувачі</h1>

            {error && <p className="users-error">{error}</p>}

            <table className="users-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Код 1С</th>
                        <th>Назва 1С</th>
                        <th>Email</th>
                        <th>Роль</th>
                        <th>Дії</th>
                    </tr>
                </thead>

                <tbody>
                    {users.map((user) => (
                        <tr key={user.id}>
                            <td>{user.id}</td>
                            <td>{user.code1C}</td>
                            <td>{user.name1C}</td>
                            <td>{user.email}</td>
                            <td>{user.role}</td>
                            <td>
                                <button
                                    type="button"
                                    onClick={() => setEditingUser({ ...user })}
                                >
                                    Редагувати
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {editingUser && (
                <div className="user-edit-form">
                    <h2>Редагування користувача</h2>
                    
                    <input
                        
                        value={editingUser.code1C}
                        onChange={(event) =>
                            setEditingUser({
                                ...editingUser,
                                code1C: event.target.value,
                            })
                        }
                        placeholder="Код 1С"
                    />

                    <input
                        value={editingUser.name1C}
                        onChange={(event) =>
                            setEditingUser({
                                ...editingUser,
                                name1C: event.target.value,
                            })
                        }
                        placeholder="Назва 1С"
                    />

                    <input
                        value={editingUser.email}
                        onChange={(event) =>
                            setEditingUser({
                                ...editingUser,
                                email: event.target.value,
                            })
                        }
                        placeholder="Email"
                    />

                    <select
                        value={editingUser.role}
                        onChange={(event) =>
                            setEditingUser({
                                ...editingUser,
                                role: event.target.value,
                            })
                        }
                    >
                        <option value="CUSTOMER">CUSTOMER</option>
                        <option value="ADMIN">ADMIN</option>
                    </select>

                    <button type="button" onClick={handleSave}>
                        Зберегти
                    </button>

                    <button
                        type="button"
                        onClick={() => setEditingUser(null)}
                    >
                        Скасувати
                    </button>
                </div>
            )}
        </div>
    );
}

export default Users;