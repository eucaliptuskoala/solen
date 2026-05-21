function Button({ variant = "primary", size = "md", children, disabled, onClick, type = "button", className = "", ...props }) {
  const base = "inline-flex items-center gap-2 rounded-[8px] font-medium transition-all duration-200 cursor-pointer font-body no-underline";

  const sizes = {
    md: "px-7 py-3 text-[0.95rem]",
    sm: "px-6 py-2.5 text-[0.9rem]",
  };

  const variants = {
    primary:
      "text-[var(--color-solen-surface)] bg-solen-accent border border-solen-accent " +
      "hover:bg-solen-accent-glow hover:border-solen-accent-glow hover:shadow-[0_0_24px_oklch(78%_0.18_80_/_0.25)] " +
      "disabled:opacity-40 disabled:cursor-not-allowed disabled:pointer-events-none",
    secondary:
      "text-solen-fg bg-transparent border-none hover:bg-solen-surface-soft",
    ghost:
      "bg-transparent border-none text-[0.8rem] text-solen-muted px-2 py-1 rounded-[4px] hover:bg-solen-surface-soft hover:text-solen-fg",
    danger:
      "text-white bg-solen-danger border border-solen-danger hover:bg-solen-danger-hover",
  };

  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`${base} ${sizes[size]} ${variants[variant]} ${className}`}
      {...props}
    >
      {children}
    </button>
  );
}

export default Button;
