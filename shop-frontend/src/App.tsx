
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Admin from "./pages/Admin";
import Customer from "./pages/Customer";
import ProtectedRoute from "./components/ProtectedRoute";
import RoleRoute from "./components/RoleRoute";
import Navbar from "./components/Navbar";
import Welcome from "./pages/Welcome";

function App() {
    return (
        <BrowserRouter>
            <Navbar />

            <Routes>
                <Route path="/" element={<Welcome />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />

                <Route element={<ProtectedRoute />}>
                    <Route element={<RoleRoute role="ADMIN" />}>
                        <Route path="/admin" element={<Admin />} />
                    </Route>

                    <Route element={<RoleRoute role="CUSTOMER" />}>
                        <Route path="/customer" element={<Customer />} />
                    </Route>
                </Route>

                <Route path="*" element={<Welcome />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;

