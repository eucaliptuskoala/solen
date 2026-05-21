import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import AuthAPI from "../apis/AuthAPI";
import AuthHandler from "../apis/AuthHandler";
import AuthSun from "../components/AuthSun";
import Button from "../components/ui/Button";
import Input from "../components/ui/Input";
import Label from "../components/ui/Label";

function SignInPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSignIn = (e) => {
    e.preventDefault();
    setError("");

    AuthAPI.signIn({ email, password })
      .then((data) => {
        AuthHandler.saveToken(data.token);
        navigate("/dashboard");
      })
      .catch((err) => {
        setError(err?.response?.data || "Something went wrong. Please try again.");
      });
  };

  return (
    <main className="max-w-[420px] mx-auto px-[var(--gutter)] pt-[var(--pt-hero)] animate-[fade-in_0.5s_ease_both]">
      <div className="text-center mb-16">
        <AuthSun />
        <h1 className="font-display text-[length:var(--fs-heading)] leading-[1.15] tracking-[-0.015em] font-[400] text-solen-fg mb-[var(--space-sm)]">Welcome back</h1>
        <p className="text-[0.9rem] text-solen-muted">Sign in to continue your practice.</p>
      </div>

      <form className="auth-form-card bg-solen-surface border border-solen-border rounded-[8px] p-[var(--space-xl)]" onSubmit={handleSignIn}>
        {error && (
          <div className="animate-[error-slide_0.3s_ease_both]" style={{ background: "oklch(55% 0.08 250 / 0.1)", color: "oklch(55% 0.08 250)", padding: "10px 14px", borderRadius: "8px", marginBottom: "16px", fontSize: "0.85rem" }}>
            {error}
          </div>
        )}

        <div className="mb-[var(--space-lg)] animate-[slide-up_0.4s_ease_both]" style={{ animationDelay: "0.1s" }}>
          <Label htmlFor="email">Email</Label>
          <Input type="email" id="email" placeholder="you@example.com" required value={email} onChange={(e) => setEmail(e.target.value)} />
        </div>
        <div className="mb-[var(--space-lg)] animate-[slide-up_0.4s_ease_both]" style={{ animationDelay: "0.2s" }}>
          <Label htmlFor="password">Password</Label>
          <Input type="password" id="password" placeholder="Your password" required value={password} onChange={(e) => setPassword(e.target.value)} />
          <a href="#" className="block text-right text-[0.85rem] mt-[var(--space-sm)] text-solen-muted no-underline hover:underline" onClick={(e) => { e.preventDefault(); alert("Password reset not implemented yet."); }}>Forgot password?</a>
        </div>
        <Button variant="primary" type="submit" className="w-full justify-center">Sign in</Button>
      </form>

      <div className="text-center mt-[var(--space-lg)] text-[0.9rem] text-solen-muted">
        Don't have an account? <Link to="/sign-up" className="text-solen-accent no-underline hover:underline">Create one</Link>
      </div>
    </main>
  );
}

export default SignInPage;
