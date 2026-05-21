import { useState, useEffect } from "react";
import MoodPicker from "../practice/MoodPicker";
import ToggleSwitch from "../ui/ToggleSwitch";
import Button from "../ui/Button";
import Textarea from "../ui/Textarea";
import Modal from "../ui/Modal";
import Label from "../ui/Label";
import PracticeSelectButton from "../ui/PracticeSelectButton";

function CheckInPopup({ isOpen, habitId, habitName, availablePractices, onSave, onClose }) {
  const [mood, setMood] = useState(null);
  const [content, setContent] = useState("");
  const [pickedHabitId, setPickedHabitId] = useState(null);
  const [saving, setSaving] = useState(false);
  const [success, setSuccess] = useState(false);
  const [isPublic, setIsPublic] = useState(false);

  useEffect(() => {
    return () => {
      setMood(null);
      setContent("");
      setPickedHabitId(null);
      setSaving(false);
      setSuccess(false);
      setIsPublic(false);
    };
  }, [isOpen]);

  if (!isOpen) return null;

  const effectiveHabitId = habitId || pickedHabitId;

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
      setPickedHabitId(null);
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
    <Modal isOpen={isOpen} onClose={onClose}>
      <h3 className="font-display text-[1.15rem] font-[400] mb-[var(--space-sm)]">{habitName ? `Check in: ${habitName}` : "New check-in"}</h3>
      <p className="font-body text-[length:var(--fs-body)] leading-relaxed text-solen-muted mb-[var(--space-lg)]">How are you feeling?</p>

      <form onSubmit={handleSubmit}>
        <div className="mb-[var(--space-lg)]">
          <Label>Mood</Label>
          <MoodPicker value={mood} onChange={setMood} />
        </div>

        {!habitId && availablePractices && (
        <div className="mb-[var(--space-lg)]">
            <Label>Practice</Label>
              {availablePractices.filter(h => !h.checkedInToday).length === 0 ? (
              <p className="font-body text-sm leading-relaxed text-solen-muted">
                No practices to check in today.
              </p>
            ) : (
              <div className="flex flex-wrap gap-[var(--space-sm)]">
                {availablePractices.filter(h => !h.checkedInToday).map(h => (
                  <PracticeSelectButton key={h.id} practice={h} selected={pickedHabitId === h.id} onClick={() => setPickedHabitId(h.id)} />
                ))}
              </div>
            )}
          </div>
        )}

        <div className="mb-[var(--space-lg)]">
          <Label htmlFor="popupContent">Reflection (optional)</Label>
          <Textarea
            id="popupContent"
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
          <Button variant="secondary" onClick={onClose}>Cancel</Button>
          <Button variant="primary" type="submit" disabled={!mood || !effectiveHabitId || saving}>
            {saving ? "Saving..." : "Save check-in"}
          </Button>
        </div>
      </form>
    </Modal>
  );
}

export default CheckInPopup;
