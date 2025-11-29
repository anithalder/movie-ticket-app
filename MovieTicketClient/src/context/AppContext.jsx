/* eslint-disable no-unused-vars */
import { createContext, useContext, useEffect, useState } from "react";
import axios from "axios";
import { useLocation, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";

// Ensure this matches your backend URL
axios.defaults.baseURL = import.meta.env.VITE_BASE_URL;
axios.defaults.withCredentials = true; // CRITICAL: Enables sending the JSESSIONID cookie

export const AppContext = createContext();

export const AppProvider = ({ children }) => {
  const [isAdmin, setIsAdmin] = useState(false);
  const [shows, setShows] = useState([]);
  const [favoriteMovies, setFavoriteMovies] = useState([]);

  const [user, setUser] = useState(
    localStorage.getItem("user")
      ? JSON.parse(localStorage.getItem("user"))
      : null
  );

  const image_base_url = import.meta.env.VITE_TMDB_IMAGE_BASE_URL;

  const location = useLocation();
  const navigate = useNavigate();

  // --- LOGIN FUNCTION ---
  const login = async (username, password) => {
    try {
      // Backend expects JSON body: { username, password }
      // NOTE: Your Login.jsx asks for "email", but the backend expects "username".
      // We are sending the input value as 'username'.
      const { data } = await axios.post("/api/auth/login", {
        username,
        password,
      });

      // Backend returns { username: "...", roles: [...] } on success
      if (data.username) {
        const userData = { username: data.username, roles: data.roles };
        setUser(userData);
        localStorage.setItem("user", JSON.stringify(userData));

        // Check if admin
        if (data.roles.includes("ROLE_ADMIN")) {
          setIsAdmin(true);
        }

        toast.success("Login successful");
        navigate("/");
        return true;
      }
    } catch (error) {
      console.error(error);
      toast.error(error.response?.data?.message || "Invalid credentials");
      return false;
    }
  };

  // --- REGISTER FUNCTION ---
  const register = async (name, email, password) => {
    try {
      // Backend endpoint is /registernormaluser
      const { data } = await axios.post("/api/auth/registernormaluser", {
        username: name,
        email,
        password,
      });

      // Backend returns the created User object on success
      if (data && data.id) {
        toast.success("Account created! Please login.");
        navigate("/login");
        return true;
      }
    } catch (error) {
      console.error(error);
      toast.error("Registration failed. Username might depend taken.");
      return false;
    }
  };

  // --- LOGOUT FUNCTION ---
  const logout = async () => {
    try {
      await axios.post("/api/auth/logout");
      // Cleanup local state
      setUser(null);
      setIsAdmin(false);
      setFavoriteMovies([]);
      localStorage.removeItem("user");
      toast.success("Logged out successfully");
      navigate("/");
    } catch (error) {
      console.error(error);
      setUser(null);
      localStorage.removeItem("user");
      navigate("/");
    }
  };

  const fetchIsAdmin = async () => {
    // Optional: You can rely on the roles returned during login
    // or keep an endpoint to check status if needed.
    if (user && user.roles && user.roles.includes("ROLE_ADMIN")) {
      setIsAdmin(true);
    }
  };

  const fetchShows = async () => {
    try {
      // Backend endpoint match: /api/show/all
      const { data } = await axios.get("/api/show/all");
      // Backend returns List<Show>, not { success: true, shows: [...] }
      // So we set data directly if it's an array
      if (Array.isArray(data)) {
        setShows(data);
      }
    } catch (error) {
      console.error(error);
      toast.error("Failed to load shows");
    }
  };

  const fetchFavoriteMovies = async () => {
    // Placeholder: You need to implement favorites in backend User entity first
    // For now, we leave this empty or mock it to prevent errors
    setFavoriteMovies([]);
  };

  useEffect(() => {
    fetchShows();
  }, []);

  useEffect(() => {
    if (user) {
      fetchIsAdmin();
      fetchFavoriteMovies();
    }
  }, [user]);

  const value = {
    axios,
    fetchIsAdmin,
    user,
    setUser,
    login,
    register,
    logout,
    navigate,
    isAdmin,
    shows,
    favoriteMovies,
    fetchFavoriteMovies,
    image_base_url,
  };

  return <AppContext.Provider value={value}>{children}</AppContext.Provider>;
};

export const useAppContext = () => useContext(AppContext);
