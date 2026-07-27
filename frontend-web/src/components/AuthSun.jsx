function AuthSun({ size = 50, className = "" }) {
  return (
    <div
      className={`relative mx-auto mb-[var(--space-lg)] ${className}`}
      style={{ width: size, height: size }}
    >
      {/* Orbit ring — always present, revealed when sun shrinks */}
      <div
        className="absolute inset-0 rounded-full border border-solen-accent/30"
      />

      {/* Sun circle — shrinks on hover to reveal the orbit */}
      <div
        className="absolute inset-0 rounded-full bg-solen-accent transition-transform duration-500 ease-[cubic-bezier(0.34,1.56,0.64,1)] hover:scale-[0.72]"
        style={{
          boxShadow: `0 0 ${Math.round(size * 1.1)}px oklch(68% 0.16 75 / 0.35)`,
        }}
      >
      </div>
    </div>
  );
}

export default AuthSun