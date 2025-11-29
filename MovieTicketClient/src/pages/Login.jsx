import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAppContext } from "../context/AppContext";
import { XIcon, ArrowRight } from "lucide-react";
import BlurCircle from "../components/BlurCircle";

const Login = () => {
  const navigate = useNavigate();
  const { login } = useAppContext();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    const success = await login(email, password);
    setIsSubmitting(false);
    if (success) {
      // Navigation is handled in AppContext, but safety check here
      navigate("/");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center px-4 relative overflow-hidden">
      {/* Background Elements */}
      <BlurCircle top="10%" left="10%" />
      <BlurCircle bottom="10%" right="10%" />

      <div className="relative w-full max-w-md bg-gray-900/80 backdrop-blur-md border border-gray-700/50 rounded-2xl shadow-2xl p-8">
        {/* Close Button (Navigates Home) */}
        <button
          onClick={() => navigate("/")}
          className="absolute top-4 right-4 text-gray-400 hover:text-white transition p-1 rounded-full hover:bg-white/10"
        >
          <XIcon className="w-5 h-5" />
        </button>

        <div className="mb-8 text-center">
          <h1 className="text-3xl font-bold mb-2">Welcome Back</h1>
          <p className="text-gray-400 text-sm">
            Please sign in to your account to continue
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label className="block text-sm font-medium text-gray-300 mb-1.5">
              Email Address
            </label>
            <input
              type="email"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full px-4 py-3 bg-black/40 border border-gray-700 rounded-lg focus:outline-none focus:border-primary text-white placeholder-gray-500 transition"
              placeholder="Enter your email"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-300 mb-1.5">
              Password
            </label>
            <input
              type="password"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full px-4 py-3 bg-black/40 border border-gray-700 rounded-lg focus:outline-none focus:border-primary text-white placeholder-gray-500 transition"
              placeholder="Enter your password"
            />
          </div>

          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full bg-primary hover:bg-primary-dull text-white font-medium py-3 rounded-lg transition duration-300 flex items-center justify-center gap-2 disabled:opacity-70 disabled:cursor-not-allowed"
          >
            {isSubmitting ? "Signing in..." : "Sign In"}
            {!isSubmitting && <ArrowRight className="w-4 h-4" />}
          </button>
        </form>

        <p className="mt-6 text-center text-sm text-gray-400">
          Don't have an account?{" "}
          <Link
            to="/register"
            className="text-primary hover:text-primary-dull font-medium transition"
          >
            Sign up
          </Link>
        </p>
      </div>
    </div>
  );
};

export default Login;