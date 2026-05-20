import { useState, useEffect, useCallback } from "react";
import HabitAPI from "../apis/HabitAPI";
import CheckInAPI from "../apis/CheckInAPI";
import AuthHandler from "../apis/AuthHandler";
import MoodPicker from "../components/practice/MoodPicker";
import PracticeCardList from "../components/practice/PracticeCardList";
import CreatePracticeModal from "../components/practice/CreatePracticeModal";
import DeleteConfirmationDialog from "../components/practice/DeleteConfirmationDialog";
import CheckInPopup from "../components/checkin/CheckInPopup";
import ToggleSwitch from "../components/ui/ToggleSwitch";

const dayNames = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

function formatDate(d) {
  const date = new Date(d);
  return `${dayNames[date.getDay()]} ${date.getDate()} ${monthNames[date.getMonth()]} ${date.getFullYear()}`;
}

function getGreeting() {
  const h = new Date().getHours();
  if (h < 12) return "Good morning";
  if (h < 18) return "Good afternoon";
  return "Good evening";
}

function DashboardPage() {
  const [practices, setPractices] = useState([]);
  const [mood, setMood] = useState(null);
  const [reflection, setReflection] = useState("");
  const [checkedInHidden, setCheckedInHidden] = useState(false);
  const [selectedHabitId, setSelectedHabitId] = useState(null);
  const [isPublic, setIsPublic] = useState(false);
  const [toast, setToast] = useState(null);

  const [showCreateModal, setShowCreateModal] = useState(false);
  const [newName, setNewName] = useState("");
  const [newDesc, setNewDesc] = useState("");
  const [newCategoryId, setNewCategoryId] = useState(null);

  const [deleteTarget, setDeleteTarget] = useState(null);

  const [popupHabit, setPopupHabit] = useState(null);

  const todayStr = formatDate(new Date());

  const showToast = (msg) => {
    setToast(msg);
    setTimeout(() => setToast(null), 2500);
  };

  const fetchPractices = useCallback(() => {
    HabitAPI.getHabitsByUser()
      .then(setPractices)
      .catch((err) => console.error("Failed to fetch practices", err));
  }, []);

  useEffect(() => { fetchPractices(); }, [fetchPractices]);

  const handleDone = (habitId) => {
    const habit = practices.find((p) => p.id === habitId);
    setPopupHabit(habit || { id: habitId, name: "Practice" });
  };

  const handlePopupSave = async ({ habitId, mood, content, public: isPublic }) => {
    await CheckInAPI.create({ habitId, mood, content, public: isPublic });
    setPopupHabit(null);
    setMood(null);
    setReflection("");
    setSelectedHabitId(null);
    showToast("Check-in saved!");
    fetchPractices();
  };

  const handleCreate = () => {
    const userId = AuthHandler.getUserId();
    if (!userId) return;
    HabitAPI.createHabit({
      name: newName,
      description: newDesc,
      userId,
      categoryId: newCategoryId,
    })
      .then(() => {
        setShowCreateModal(false);
        setNewName("");
        setNewDesc("");
        setNewCategoryId(null);
        fetchPractices();
      })
      .catch((err) => console.error("Failed to create habit", err));
  };

  const handleDeleteTarget = (practice) => setDeleteTarget(practice);

  const handleDeleteConfirm = () => {
    if (!deleteTarget) return;
    HabitAPI.deleteHabit(deleteTarget.id)
      .then(() => {
        setDeleteTarget(null);
        fetchPractices();
      })
      .catch((err) => console.error("Failed to delete habit", err));
  };

  const handleSaveCheckIn = () => {
    if (!mood || !selectedHabitId) return;
    const checkin = { mood, content: reflection, habitId: selectedHabitId, public: isPublic };
    CheckInAPI.create(checkin)
      .then(() => {
        setMood(null);
        setReflection("");
        setSelectedHabitId(null);
        showToast("Check-in saved!");
        fetchPractices();
      })
      .catch((err) => console.error("Failed to save check-in", err));
  };

  return (
    <main className="max-w-[1280px] mx-auto px-[var(--gutter)] py-[var(--space-xl)]">
      <div className="mb-[var(--space-xl)] animate-[fade-in_0.5s_ease_both]">
        <span className="font-mono text-xs tracking-[0.05em] uppercase text-solen-muted block mb-[var(--space-sm)]">
          {todayStr}
        </span>
        <h1 className="font-display text-[length:var(--fs-heading)] leading-[1.15] tracking-[-0.015em] font-normal">{getGreeting()}.</h1>
      </div>

      {!checkedInHidden && (
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

          <textarea
            className="w-full px-4 py-3 border border-solen-border rounded-[8px] bg-solen-bg text-[0.95rem] text-solen-fg outline-none resize-vertical transition-[border-color] duration-200 font-body min-h-[60px] focus:border-solen-accent mb-[var(--space-md)]"
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
            <button className="inline-flex items-center gap-2 px-7 py-3 rounded-[8px] text-[0.95rem] font-medium text-[var(--color-solen-surface)] bg-solen-accent border border-solen-accent no-underline hover:bg-solen-accent-glow hover:border-solen-accent-glow hover:shadow-[0_0_24px_oklch(78%_0.18_80_/_0.25)] transition-all duration-200 cursor-pointer font-body disabled:opacity-40 disabled:cursor-not-allowed disabled:pointer-events-none" onClick={handleSaveCheckIn} disabled={!mood || !selectedHabitId}>
              Save check-in
            </button>
            <button className="inline-flex items-center gap-2 px-7 py-3 rounded-[8px] text-[0.95rem] font-medium text-solen-fg no-underline hover:bg-solen-surface-soft transition-all duration-200 cursor-pointer font-body bg-transparent border-none" onClick={() => setCheckedInHidden(true)}>
              Skip for now
            </button>
          </div>
        </div>
      )}

      <div className="flex items-center justify-between mb-[var(--space-lg)] animate-[fade-in_0.5s_ease_0.2s_both]">
        <h2 className="font-display text-[1.3rem] font-normal">Your practices</h2>
        <button className="inline-flex items-center gap-2 px-[18px] py-2 rounded-[8px] text-[0.85rem] font-medium text-solen-muted no-underline hover:bg-solen-surface-soft hover:text-solen-fg transition-all duration-200 cursor-pointer font-body bg-transparent border-none" onClick={() => setShowCreateModal(true)}>
          + New practice
        </button>
      </div>

      <div className="animate-[fade-in_0.5s_ease_0.3s_both]">
        <PracticeCardList
          practices={practices}
          onDone={handleDone}
          onDelete={handleDeleteTarget}
        />
      </div>

      <CreatePracticeModal
        isOpen={showCreateModal}
        onClose={() => setShowCreateModal(false)}
        onCreate={handleCreate}
        categoryId={newCategoryId}
        setCategoryId={setNewCategoryId}
        name={newName}
        setName={setNewName}
        description={newDesc}
        setDescription={setNewDesc}
      />

      <CheckInPopup
        isOpen={!!popupHabit}
        habitId={popupHabit?.id}
        habitName={popupHabit?.name}
        onSave={handlePopupSave}
        onClose={() => setPopupHabit(null)}
      />

      <DeleteConfirmationDialog
        isOpen={!!deleteTarget}
        practiceName={deleteTarget?.name || ""}
        onCancel={() => setDeleteTarget(null)}
        onConfirm={handleDeleteConfirm}
      />

      {toast && <div className="toast">{toast}</div>}
    </main>
  );
}

export default DashboardPage;
