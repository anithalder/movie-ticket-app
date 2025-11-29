import { useAppContext } from "../context/AppContext";
import { assets } from "../assets/assets";
import BlurCircle from "../components/BlurCircle";
import { LogOut, User } from "lucide-react";

const Profile = () => {
  const { user, logout } = useAppContext();

  return (
    <div className="min-h-[80vh] flex items-center justify-center px-6 md:px-16 lg:px-40 pt-30 pb-20 relative overflow-hidden">
      <BlurCircle top="10%" left="20%" />
      <BlurCircle bottom="10%" right="20%" />

      <div className="bg-gray-900/80 backdrop-blur-md border border-gray-700/50 rounded-2xl p-8 w-full max-w-2xl shadow-2xl">
        <div className="flex flex-col md:flex-row items-center gap-8">
          {/* Profile Image */}
          <div className="relative">
            <img
              src={assets.profile}
              alt="Profile"
              className="w-32 h-32 rounded-full object-cover border-4 border-primary/50 shadow-lg"
            />
            <div className="absolute bottom-2 right-2 bg-gray-800 p-1.5 rounded-full border border-gray-600">
              <User className="w-4 h-4 text-gray-300" />
            </div>
          </div>

          {/* User Details */}
          <div className="flex-1 text-center md:text-left space-y-4">
            <div>
              <h2 className="text-gray-400 text-sm uppercase tracking-wide font-semibold mb-1">
                Account Email
              </h2>
              <p className="text-2xl md:text-3xl font-bold text-white">
                {user?.email || "guest@example.com"}
              </p>
            </div>

            <div>
              <h2 className="text-gray-400 text-sm uppercase tracking-wide font-semibold mb-1">
                Username
              </h2>
              <p className="text-xl text-gray-200">
                {user?.username || user?.email?.split("@")[0] || "Guest User"}
              </p>
            </div>
          </div>
        </div>

        <hr className="border-gray-700 my-8" />

        {/* Actions */}
        <div className="flex flex-col sm:flex-row justify-end gap-4">
          <button
            onClick={logout}
            className="flex items-center justify-center gap-2 px-6 py-3 bg-red-500/10 hover:bg-red-500/20 text-red-500 border border-red-500/50 rounded-lg transition-all duration-300 font-medium cursor-pointer"
          >
            <LogOut className="w-5 h-5" />
            Sign Out
          </button>
        </div>
      </div>
    </div>
  );
};

export default Profile;