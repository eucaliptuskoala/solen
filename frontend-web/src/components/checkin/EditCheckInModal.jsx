import { useState, useEffect } from "react";
import MoodPicker from "../practice/MoodPicker";
import ToggleSwitch from "../ui/ToggleSwitch";
import Button from "../ui/Button";
import Textarea from "../ui/Textarea";
import Modal from "../ui/Modal";

function EditCheckInModal({ isOpen, entry, onSave, onClose }) {
  const [content, setContent] = useState("");
  const [mood, setMood] = useState(null);
  const [isPublic, setIsPublic] = useState(false);

  useEffect(() => {
    if (entry && isOpen) {
      setContent(entry.content || "");
      setMood(entry.mood || null);
      setIsPublic(entry.isPublic || false);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isOpen]);

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave(entry.id, { content, mood, public: isPublic });
  };

  return (
    <Modal isOpen={isOpen && !!entry} onClose={onClose}>
      <h3 className="font-display text-[1.15rem] font-[400] mb-[var(--space-sm)]">Edit check-in</h3>
      <p className="font-body text-[length:var(--fs-body)] leading-relaxed text-solen-muted mb-[var(--space-lg)]">Update your mood or reflection.</p>

      <form onSubmit={handleSubmit}>
        <div className="mb-[var(--space-lg)]">
          <label className="block font-mono text-[0.7rem] tracking-[0.08em] uppercase text-solen-muted mb-[var(--space-sm)]">Mood</label>
          <MoodPicker value={mood} onChange={setMood} />
        </div>

        <div className="mb-[var(--space-lg)]">
          <label className="block font-mono text-[0.7rem] tracking-[0.08em] uppercase text-solen-muted mb-[var(--space-sm)]" htmlFor="editContent">Reflection</label>
          <Textarea
            id="editContent"
            rows={4}
            value={content}
            onChange={(e) => setContent(e.target.value)}
          />
        </div>

        <div className="mb-[var(--space-lg)]">
          <ToggleSwitch
            id="editPublic"
            checked={isPublic}
            onChange={setIsPublic}
            label="Make this check-in public on Inspire"
          />
        </div>

        <div className="flex gap-[var(--space-sm)] justify-end">
          <Button variant="secondary" onClick={onClose}>Cancel</Button>
          <Button variant="primary" type="submit">Save</Button>
        </div>
      </form>
    </Modal>
  );
}

export default EditCheckInModal;
