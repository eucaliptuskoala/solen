function PracticeSelectButton({ practice, selected, disabled, onClick, children }) {
  return (
    <button
      type="button"
      className={`flex items-center gap-2 px-3.5 py-2 border border-solen-border rounded-[8px] bg-solen-surface cursor-pointer transition-all duration-200 font-body text-sm text-solen-fg hover:border-solen-accent-dim hover:bg-solen-accent-subtle${selected ? " border-solen-accent bg-solen-accent-subtle" : ""}${disabled ? " opacity-40 cursor-not-allowed pointer-events-none" : ""}`}
      onClick={onClick}
      disabled={disabled}
    >
      <span className="font-medium">{practice.name}</span>
      {children}
    </button>
  );
}

export default PracticeSelectButton;
