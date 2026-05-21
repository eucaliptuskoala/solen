import { useState } from "react";
import MoodPicker from "../practice/MoodPicker";
import ToggleSwitch from "../ui/ToggleSwitch";
import Button from "../ui/Button";
import Textarea from "../ui/Textarea";

function DailyCheckInForm({ practices, onSave }) {
  const [hidden, setHidden] = useState(false);
  const [mood, setMood] = useState(null);
  const [reflection, setReflection] = useState("");
  const [selectedHabitId, setSelectedHabitId] = useState(null);
  const [isPublic, setIsPublic] = useState(false);

  const handleSave = async () => {
    if (!mood || !selectedHabitId) return;
    await onSave({ mood, content: reflection, habitId: selectedHabitId, public: isPublic });
    setMood(null);
    setReflection("");
    setSelectedHabitId(null);
  };

  if (hidden) return null;

  return (
    <div className="bg-solen-surface border border-solen-border rounded-[8px] p-[var(--space-lg)] mb-[var(--space-xl)] animate-[fade-in_0.5s_ease_0.1s_both]">
      <h2 className="font-display text-[1.3rem] font-[400] mb-[var(--space-lg)]">How are you feeling today?</h2>

      <div className="mb-4">
        <MoodPicker value={mood} onChange={setMood} />
      </div>

      <div className="mb-4">
        <span className="font-body text-sm leading-relaxed text-solen-muted mb-[var(--space-sm)]">Which practice are you checking in for?</span>
        {practices.length === 0 ? (
          <p className="font-body text-sm leading-relaxed text-solen-muted">
            Create a practice first to check in.
          </p>
        ) : (
          <div className="flex flex-wrap gap-2">
            {practices.map((p) => (
              <button
                key={p.id}
                type="button"
                className={`flex items-center gap-2 px-3.5 py-2 border border-solen-border rounded-[8px] bg-solen-surface cursor-pointer transition-all duration-200 font-body text-sm text-solen-fg hover:border-solen-accent-dim hover:bg-solen-accent-subtle${selectedHabitId === p.id ? " border-solen-accent bg-solen-accent-subtle" : ""}${p.checkedInToday ? " opacity-40 cursor-not-allowed pointer-events-none" : ""}`}
                onClick={() => !p.checkedInToday && setSelectedHabitId(p.id)}
                disabled={p.checkedInToday}
              >
                <span className="font-medium">{p.name}</span>
                <span className="inline-flex items-center gap-1 font-mono text-xs text-solen-accent">
                  <svg viewBox="0 0 16 16" width="12" height="12">
                    <circle cx="8" cy="8" r="6" fill="oklch(68% 0.16 75 / 0.2)" />
                    <circle cx="8" cy="8" r="3" fill="var(--color-solen-accent)" />
                  </svg>
                  {p.streak}d
                </span>
              </button>
            ))}
          </div>
        )}
      </div>

      <Textarea
        className="bg-solen-bg min-h-[60px] mb-[var(--space-md)]"
        placeholder="What's on your mind today? (optional)"
        rows={2}
        value={reflection}
        onChange={(e) => setReflection(e.target.value)}
      />

      <div className="mb-[var(--space-md)]">
        <ToggleSwitch
          id="inlineIsPublic"
          checked={isPublic}
          onChange={setIsPublic}
          label="Share on Inspire"
        />
      </div>

      <div className="flex items-center gap-4 mt-4">
        <Button variant="primary" onClick={handleSave} disabled={!mood || !selectedHabitId}>
          Save check-in
        </Button>
        <Button variant="secondary" onClick={() => setHidden(true)}>
          Skip for now
        </Button>
      </div>
    </div>
  );
}

export default DailyCheckInForm;
