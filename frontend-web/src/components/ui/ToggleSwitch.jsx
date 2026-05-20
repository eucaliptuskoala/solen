function ToggleSwitch({ checked, onChange, id, label }) {
  return (
    <div className="flex items-center justify-between">
      {label && (
        <label htmlFor={id} className="font-body text-[0.9rem] text-solen-fg cursor-pointer select-none">
          {label}
        </label>
      )}
      <button
        id={id}
        type="button"
        role="switch"
        aria-checked={checked}
        onClick={() => onChange(!checked)}
        className={`relative inline-flex h-[26px] w-[48px] shrink-0 cursor-pointer items-center rounded-full border transition-colors duration-200 focus:outline-none ${
          checked
            ? "border-solen-accent bg-solen-accent"
            : "border-solen-border bg-solen-surface-soft"
        }`}
      >
        <span
          className={`inline-block h-[20px] w-[20px] rounded-full bg-white shadow-sm transition-transform duration-200 ${
            checked ? "translate-x-[25px]" : "translate-x-[2px]"
          }`}
        />
      </button>
    </div>
  );
}

export default ToggleSwitch;
