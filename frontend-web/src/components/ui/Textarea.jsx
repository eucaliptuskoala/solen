function Textarea({ size = "md", className = "", ...props }) {
  const sizes = {
    md: "px-4 py-3 text-[1rem] min-h-[100px] focus:border-solen-accent focus:shadow-[0_0_0_3px_oklch(68%_0.16_75_/_0.1)]",
    sm: "px-3 py-2 text-sm focus:border-solen-accent-dim",
  };

  return (
    <textarea
      className={`w-full border border-solen-border rounded-[8px] bg-solen-surface text-solen-fg outline-none transition-[border-color] duration-200 placeholder:text-solen-muted/60 resize-y font-body ${sizes[size]} ${className}`}
      {...props}
    />
  );
}

export default Textarea;
