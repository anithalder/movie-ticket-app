import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import axios from "axios";
import toast from "react-hot-toast";

const SignIn = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const { data } = await axios.post("/api/auth/login", { email, password });
      if (data.success) {
        login(data.user, data.token);
        navigate("/");
        toast.success("Signed in successfully");
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      console.error(error);
      toast.error("Something went wrong");
    }
  };

  return (
    <div className="min-h-screen flex justify-center items-center">
      <form onSubmit={handleSubmit} className="card">
        <h1 className="text-2xl font-bold">Sign In</h1>
        <div className="form-control">
          <label htmlFor="email">Email</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>
        <div className="form-control">
          <label htmlFor="password">Password</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <button type="submit" className="btn btn-primary">
          Sign In
        </button>
        <p>
          Don't have an account?{" "}
          <Link to="/sign-up" className="link">
            Sign Up
          </Link>
        </p>
      </form>
    </div>
  );
};

export default SignIn;
