import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import UserAPI from "../apis/UserAPI";
import AuthAPI from "../apis/AuthAPI";
import AuthHandler from "../apis/AuthHandler";
import HabitAPI from "../apis/HabitAPI";
import CategoryTreeBrowser from "../components/landing/CategoryTreeBrowser";
import AuthSun from "../components/AuthSun";
import Button from "../components/ui/Button";
import Input from "../components/ui/Input";

function SignUpPage() {
  const [step, setStep] = useState(1);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleStep1 = async (e) => {
    e.preventDefault();
    setError("");

    if (!name || !email || password.length < 8) {
      setError("Please fill in all fields with a valid password (8+ characters).");
      return;
    }

    try {
      await UserAPI.createUser({ name, email, password, isAdmin: false });
      const data = await AuthAPI.signIn({ email, password });
      AuthHandler.saveToken(data.token);
      setStep(2);
    } catch (err) {
      const msg = err?.response?.data;
      setError(typeof msg === "string" ? msg : JSON.stringify(msg) || "Something went wrong. Please try again.");
    }
  };

  const handleFinish = (selected) => {
    if (selected.length > 0) {
      Promise.all(
        selected.map(({ id: categoryId, name }) =>
          HabitAPI.createHabit({ name, description: "", userId: AuthHandler.getUserId(), categoryId })
        )
      ).catch((err) => console.error("Failed to create habits", err)).finally(() => navigate("/dashboard"));
    } else {
      navigate("/dashboard");
    }
  };

  return (
    <main className="max-w-[560px] mx-auto px-[var(--gutter)] pt-[var(--space-2xl)] animate-[fade-in_0.5s_ease_both]">
      <div className={`${step === 1 ? "block" : "hidden"}`} id="step1">
        <div className="text-center mb-16">
          <AuthSun />
          <h1 className="font-display text-[length:var(--fs-heading)] leading-[1.15] tracking-[-0.015em] font-[400] text-solen-fg mb-[var(--space-sm)]">Join Solen</h1>
          <p className="text-[0.9rem] text-solen-muted">Create your account to begin your practice.</p>
        </div>

        <form className="auth-form-card bg-solen-surface border border-solen-border rounded-[8px] p-[var(--space-xl)]" onSubmit={handleStep1}>
          {error && (
            <div className="animate-[error-slide_0.3s_ease_both]" style={{ background: "oklch(55% 0.08 250 / 0.1)", color: "oklch(55% 0.08 250)", padding: "10px 14px", borderRadius: "8px", marginBottom: "16px", fontSize: "0.85rem" }}>
              {error}
            </div>
          )}

          <div className="mb-[var(--space-lg)] animate-[slide-up_0.4s_ease_both]" style={{ animationDelay: "0.1s" }}>
            <label htmlFor="name" className="block font-mono text-[0.7rem] tracking-[0.08em] uppercase text-solen-muted mb-[var(--space-sm)]">Name</label>
            <Input type="text" id="name" placeholder="Your name" required value={name} onChange={(e) => setName(e.target.value)} />
          </div>
          <div className="mb-[var(--space-lg)] animate-[slide-up_0.4s_ease_both]" style={{ animationDelay: "0.2s" }}>
            <label htmlFor="email" className="block font-mono text-[0.7rem] tracking-[0.08em] uppercase text-solen-muted mb-[var(--space-sm)]">Email</label>
            <Input type="email" id="email" placeholder="you@example.com" required value={email} onChange={(e) => setEmail(e.target.value)} />
          </div>
          <div className="mb-[var(--space-lg)] animate-[slide-up_0.4s_ease_both]" style={{ animationDelay: "0.3s" }}>
            <label htmlFor="password" className="block font-mono text-[0.7rem] tracking-[0.08em] uppercase text-solen-muted mb-[var(--space-sm)]">Password</label>
            <Input type="password" id="password" placeholder="Choose a password" required minLength={8} value={password} onChange={(e) => setPassword(e.target.value)} />
          </div>
          <Button variant="primary" type="submit" className="w-full justify-center">Continue</Button>
        </form>

        <div className="text-center mt-[var(--space-lg)] text-[0.9rem] text-solen-muted">
          By continuing, you agree to Solen's terms and privacy policy.
        </div>
      </div>

      <div className={`${step === 2 ? "block" : "hidden"}`} id="step2">
        <div className="text-center mb-16">
          <span className="block font-mono text-[0.75rem] tracking-[0.05em] uppercase text-solen-muted mb-[var(--space-sm)]">Step 2 of 2</span>
          <h2 className="font-display text-[length:var(--fs-heading)] leading-[1.15] tracking-[-0.015em] font-[400] text-solen-fg mb-[var(--space-sm)]">Pick your practices</h2>
          <p className="text-[0.9rem] text-solen-muted">Browse categories and select the practices you'd like to track. You can always change these later.</p>
        </div>

        <CategoryTreeBrowser onSelect={handleFinish} />
      </div>
    </main>
  );
}

export default SignUpPage;
