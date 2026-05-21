import { useState } from "react";
import MoodPicker from "../practice/MoodPicker";
import ToggleSwitch from "../ui/ToggleSwitch";
import Button from "../ui/Button";
import Textarea from "../ui/Textarea";
import PracticeSelectButton from "../ui/PracticeSelectButton";
import Card from "../ui/Card";

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
    <Card className="mb-[var(--space-xl)] animate-[fade-in_0.5s_ease_0.1s_both]">
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
              <PracticeSelectButton key={p.id} practice={p} selected={selectedHabitId === p.id} disabled={p.checkedInToday} onClick={() => !p.checkedInToday && setSelectedHabitId(p.id)}>
                <span className="inline-flex items-center gap-1 font-mono text-xs text-solen-accent">
                  <svg viewBox="0 0 16 16" width="12" height="12">
                    <circle cx="8" cy="8" r="6" fill="oklch(68% 0.16 75 / 0.2)" />
                    <circle cx="8" cy="8" r="3" fill="var(--color-solen-accent)" />
                  </svg>
                  {p.streak}d
                </span>
              </PracticeSelectButton>
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
    </Card>
  );
}

export default DailyCheckInForm;
