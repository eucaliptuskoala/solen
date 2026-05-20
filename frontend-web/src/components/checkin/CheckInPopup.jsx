import { useState, useEffect } from "react";
import MoodPicker from "../practice/MoodPicker";
import ToggleSwitch from "../ui/ToggleSwitch";

function CheckInPopup({ isOpen, habitId, habitName, habits, onSave, onClose }) {
  const [mood, setMood] = useState(null);
  const [content, setContent] = useState("");
  const [selectedHabitId, setSelectedHabitId] = useState(null);
  const [saving, setSaving] = useState(false);
  const [success, setSuccess] = useState(false);
  const [isPublic, setIsPublic] = useState(false);

  useEffect(() => {
    return () => {
      setMood(null);
      setContent("");
      setSelectedHabitId(null);
      setSaving(false);
      setSuccess(false);
      setIsPublic(false);
    };
  }, [isOpen]);

  if (!isOpen) return null;

  const effectiveHabitId = habitId || selectedHabitId;

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!mood || !effectiveHabitId) return;
    setSaving(true);
    await onSave({ habitId: effectiveHabitId, mood, content, public: isPublic });
    setSuccess(true);
    setTimeout(() => {
      onClose();
      setSuccess(false);
      setSaving(false);
      setMood(null);
      setContent("");
      setSelectedHabitId(null);
    }, 600);
  };

  if (success) {
    return (
      <div className="fixed inset-0 bg-black/20 flex items-center justify-center z-200">
        <div className="bg-solen-surface border border-solen-border rounded-[8px] p-[var(--space-xl)] max-w-[400px] w-[90%] shadow-[0_16px_48px_rgb(0_0_0_/_0.08)] text-center">
          <div className="popup-success-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round">
              <path d="M20 6L9 17l-5-5" />
            </svg>
          </div>
          <p className="font-body text-[length:var(--fs-body)] leading-relaxed text-solen-accent mt-[var(--space-md)]">
            Check-in saved!
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="fixed inset-0 bg-black/20 flex items-center justify-center z-200" onClick={onClose}>
      <div className="bg-solen-surface border border-solen-border rounded-[8px] p-[var(--space-xl)] max-w-[500px] w-[90%] shadow-[0_16px_48px_rgb(0_0_0_/_0.08)]" onClick={(e) => e.stopPropagation()}>
        <h3 className="font-display text-[1.15rem] font-[400] mb-[var(--space-sm)]">{habitName ? `Check in: ${habitName}` : "New check-in"}</h3>
        <p className="font-body text-[length:var(--fs-body)] leading-relaxed text-solen-muted mb-[var(--space-lg)]">How are you feeling?</p>

        <form onSubmit={handleSubmit}>
          <div className="mb-[var(--space-lg)]">
            <label className="block font-mono text-[0.7rem] tracking-[0.08em] uppercase text-solen-muted mb-[var(--space-sm)]">Mood</label>
            <MoodPicker value={mood} onChange={setMood} />
          </div>

          {!habitId && habits && (
          <div className="mb-[var(--space-lg)]">
              <label className="block font-mono text-[0.7rem] tracking-[0.08em] uppercase text-solen-muted mb-[var(--space-sm)]">Practice</label>
              {habits.filter(h => !h.checkedInToday).length === 0 ? (
                <p className="font-body text-sm leading-relaxed text-solen-muted">
                  No practices to check in today.
                </p>
              ) : (
                <div className="flex flex-wrap gap-[var(--space-sm)]">
                  {habits.filter(h => !h.checkedInToday).map(h => (
                    <button
                      key={h.id}
                      type="button"
                      className={`flex items-center gap-2 px-3.5 py-2 border border-solen-border rounded-[8px] bg-solen-surface cursor-pointer transition-all duration-200 font-body text-sm text-solen-fg hover:border-solen-accent-dim hover:bg-solen-accent-subtle${selectedHabitId === h.id ? " border-solen-accent bg-solen-accent-subtle" : ""}`}
                      onClick={() => setSelectedHabitId(h.id)}
                    >
                      <span className="font-medium">{h.name}</span>
                    </button>
                  ))}
                </div>
              )}
            </div>
          )}

          <div className="mb-[var(--space-lg)]">
            <label className="block font-mono text-[0.7rem] tracking-[0.08em] uppercase text-solen-muted mb-[var(--space-sm)]" htmlFor="popupContent">Reflection (optional)</label>
            <textarea
              id="popupContent"
              className="w-full px-4 py-3 border border-solen-border rounded-[8px] bg-solen-surface text-[1rem] text-solen-fg outline-none transition-[border-color] duration-200 focus:border-solen-accent focus:shadow-[0_0_0_3px_oklch(68%_0.16_75_/_0.1)] placeholder:text-solen-muted/60 resize-y min-h-[100px] font-body"
              rows={4}
              value={content}
              onChange={(e) => setContent(e.target.value)}
              placeholder="What's on your mind?"
            />
          </div>

          <div className="mb-[var(--space-lg)]">
            <ToggleSwitch
              id="popupIsPublic"
              checked={isPublic}
              onChange={setIsPublic}
              label="Share on Inspire"
            />
          </div>

          <div className="flex gap-[var(--space-sm)] justify-end">
            <button type="button" className="inline-flex items-center gap-2 px-7 py-3 rounded-[8px] text-[0.95rem] font-medium text-solen-fg no-underline hover:bg-solen-surface-soft transition-all duration-200 cursor-pointer font-body bg-transparent border-none" onClick={onClose}>Cancel</button>
            <button type="submit" className="inline-flex items-center gap-2 px-7 py-3 rounded-[8px] text-[0.95rem] font-medium text-[var(--color-solen-surface)] bg-solen-accent border border-solen-accent no-underline hover:bg-solen-accent-glow hover:border-solen-accent-glow hover:shadow-[0_0_24px_oklch(78%_0.18_80_/_0.25)] transition-all duration-200 cursor-pointer font-body disabled:opacity-40 disabled:cursor-not-allowed disabled:hover:bg-solen-accent disabled:hover:border-solen-accent disabled:hover:shadow-none" disabled={!mood || !effectiveHabitId || saving}>
              {saving ? "Saving..." : "Save check-in"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default CheckInPopup;
