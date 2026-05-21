function Input({ size = "md", className = "", ...props }) {
  const sizes = {
    md: "px-4 py-3 text-[1rem] focus:border-solen-accent focus:shadow-[0_0_0_3px_oklch(68%_0.16_75_/_0.1)]",
    sm: "px-3 py-2 text-sm focus:border-solen-accent-dim",
  };

  return (
    <input
      className={`w-full border border-solen-border rounded-[8px] bg-solen-surface text-solen-fg outline-none transition-[border-color] duration-200 placeholder:text-solen-muted/60 font-body ${sizes[size]} ${className}`}
      {...props}
    />
  );
}

export default Input;
