import { useState, useEffect, useRef } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import AuthHandler from "../../apis/AuthHandler";

const navItems = [
  {
    label: "Dashboard",
    path: "/dashboard",
    icon: (
      <svg className="w-[18px] h-[18px] shrink-0 opacity-60" viewBox="0 0 18 18">
        <rect x="2" y="2" width="6" height="6" rx="1.5" fill="currentColor" />
        <rect x="10" y="2" width="6" height="6" rx="1.5" fill="currentColor" />
        <rect x="2" y="10" width="6" height="6" rx="1.5" fill="currentColor" />
        <rect x="10" y="10" width="6" height="6" rx="1.5" fill="currentColor" />
      </svg>
    ),
  },
  {
    label: "Check-Ins",
    path: "/checkins",
    icon: (
      <svg className="w-[18px] h-[18px] shrink-0 opacity-60" viewBox="0 0 18 18">
        <path d="M3 9l4 4 8-8" stroke="currentColor" strokeWidth="1.5" fill="none" strokeLinecap="round" />
      </svg>
    ),
  },
  {
    label: "Progress",
    path: "/progress",
    icon: (
      <svg className="w-[18px] h-[18px] shrink-0 opacity-60" viewBox="0 0 18 18">
        <rect x="2" y="8" width="3" height="8" rx="1" fill="currentColor" />
        <rect x="7.5" y="4" width="3" height="12" rx="1" fill="currentColor" />
        <rect x="13" y="2" width="3" height="14" rx="1" fill="currentColor" />
      </svg>
    ),
  },
  {
    label: "Inspire",
    path: "/inspire",
    icon: (
      <svg className="w-[18px] h-[18px] shrink-0 opacity-60" viewBox="0 0 18 18">
        <circle cx="9" cy="9" r="7" stroke="currentColor" strokeWidth="1.5" fill="none" />
        <circle cx="9" cy="9" r="2" fill="currentColor" />
      </svg>
    ),
  },
];

function NavBar() {
  const [isOpen, setIsOpen] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();
  const menuRef = useRef(null);
  const triggerRef = useRef(null);

  const token = AuthHandler.tokenExists();
  const userName = AuthHandler.getName() || "User";
  const avatarLetter = userName.charAt(0).toUpperCase();

  useEffect(() => {
    const handleClick = (e) => {
      if (
        menuRef.current &&
        triggerRef.current &&
        !menuRef.current.contains(e.target) &&
        !triggerRef.current.contains(e.target)
      ) {
        setIsOpen(false);
      }
    };
    document.addEventListener("click", handleClick);
    return () => document.removeEventListener("click", handleClick);
  }, []);

  const handleSignOut = () => {
    AuthHandler.clearToken();
    navigate("/");
  };

  const isLanding = location.pathname === "/";

  return (
    <nav className="flex items-center justify-between px-[var(--gutter)] h-16 max-w-[1280px] mx-auto relative">
      <Link to={token ? "/dashboard" : "/"} className="flex items-center gap-[10px] font-display text-[1.35rem] tracking-[-0.01em] text-solen-fg no-underline">
        <span className="w-7 h-7 rounded-full bg-solen-accent relative shrink-0">
            <span className="absolute -inset-[6px] rounded-full border border-solen-accent/30" />
          </span>
        Solen
      </Link>

      {isLanding && (
        <div className="flex items-center gap-4">
          <Link to="/sign-in" className="inline-flex items-center gap-2 px-[18px] py-2 rounded-[8px] text-[0.85rem] font-medium text-solen-muted no-underline hover:bg-solen-surface-soft hover:text-solen-fg transition-all duration-200">
            Sign in
          </Link>
          <Link to="/sign-up" className="inline-flex items-center gap-2 px-[18px] py-2 rounded-[8px] text-[0.85rem] font-medium text-[var(--color-solen-surface)] bg-solen-accent border border-solen-accent no-underline hover:bg-solen-accent-glow hover:border-solen-accent-glow hover:shadow-[0_0_24px_oklch(78%_0.18_80_/_0.25)] transition-all duration-200">
            Get started
          </Link>
        </div>
      )}

      {location.pathname === "/sign-up" && (
        <div className="flex items-center gap-2">
          <span className="text-[0.875rem] leading-[1.5] text-solen-muted">Already have an account?</span>
          <Link to="/sign-in" className="inline-flex items-center gap-2 px-[18px] py-2 rounded-[8px] text-[0.85rem] font-medium text-solen-muted no-underline hover:bg-solen-surface-soft hover:text-solen-fg transition-all duration-200">
            Sign in
          </Link>
        </div>
      )}

      {token && (
        <div className="relative">
          <button
            ref={triggerRef}
            className="flex items-center gap-3 px-4 py-2 rounded-[8px] border border-solen-border bg-solen-surface cursor-pointer transition-all duration-200 font-body text-sm text-solen-fg hover:bg-solen-surface-soft"
            onClick={() => setIsOpen((p) => !p)}
          >
            <span className="w-7 h-7 rounded-full bg-solen-accent-dim flex items-center justify-center text-[var(--color-solen-surface)] font-mono text-xs font-semibold">{avatarLetter}</span>
            <span>{userName}</span>
            <svg
              className={`w-3.5 h-3.5 transition-transform duration-300${isOpen ? " rotate-180" : ""}`}
              viewBox="0 0 14 14"
            >
              <path d="M3 5l4 4 4-4" stroke="currentColor" strokeWidth="1.5" fill="none" />
            </svg>
          </button>

          <div ref={menuRef} className={`nav-dropdown-menu${isOpen ? " open" : ""}`}>
            <div className="px-[18px] pt-2 pb-1 font-mono text-[0.65rem] tracking-[0.08em] uppercase text-solen-muted opacity-60">Navigate</div>
            {navItems.map((item) => (
              <Link
                key={item.path}
                to={item.path}
                className={`flex items-center gap-3 px-[18px] py-3 text-solen-fg text-sm transition-colors duration-150 cursor-pointer no-underline border-none w-full text-left bg-transparent${location.pathname === item.path ? " active bg-solen-accent-subtle text-solen-accent" : ""}`}
                onClick={() => setIsOpen(false)}
              >
                {item.icon}
                {item.label}
              </Link>
            ))}
            <div className="h-px bg-solen-border my-1" />
            <button className="flex items-center gap-3 px-[18px] py-3 text-solen-fg text-sm transition-colors duration-150 cursor-pointer no-underline border-none w-full text-left bg-transparent" onClick={handleSignOut}>
              Sign out
            </button>
          </div>
        </div>
      )}
    </nav>
  );
}

export default NavBar;
