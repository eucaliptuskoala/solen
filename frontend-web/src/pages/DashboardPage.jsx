import { useState, useEffect, useCallback } from "react";
import HabitAPI from "../apis/HabitAPI";
import CheckInAPI from "../apis/CheckInAPI";
import AuthHandler from "../apis/AuthHandler";
import PracticeCardList from "../components/practice/PracticeCardList";
import CreatePracticeModal from "../components/practice/CreatePracticeModal";
import DeleteConfirmationDialog from "../components/practice/DeleteConfirmationDialog";
import CheckInPopup from "../components/checkin/CheckInPopup";
import DailyCheckInForm from "../components/checkin/DailyCheckInForm";
import { formatDate } from "../utils/dates";
import useToast from "../hooks/useToast";

function getGreeting() {
  const h = new Date().getHours();
  if (h < 12) return "Good morning";
  if (h < 18) return "Good afternoon";
  return "Good evening";
}

function DashboardPage() {
  const [practices, setPractices] = useState([]);
  const { toast, showToast } = useToast();

  const [showCreateModal, setShowCreateModal] = useState(false);
  const [newName, setNewName] = useState("");
  const [newDesc, setNewDesc] = useState("");
  const [newCategoryId, setNewCategoryId] = useState(null);

  const [deleteTarget, setDeleteTarget] = useState(null);

  const [popupHabit, setPopupHabit] = useState(null);

  const todayStr = formatDate(new Date().toISOString());

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

  const saveCheckIn = async (data) => {
    await CheckInAPI.create(data);
    showToast("Check-in saved!");
    fetchPractices();
  };

  const handlePopupSave = async (data) => {
    await saveCheckIn(data);
    setPopupHabit(null);
  };

  const handleDailyCheckInSave = saveCheckIn;

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

  return (
    <main className="max-w-[1280px] mx-auto px-[var(--gutter)] py-[var(--space-xl)]">
      <div className="mb-[var(--space-xl)] animate-[fade-in_0.5s_ease_both]">
        <span className="font-mono text-xs tracking-[0.05em] uppercase text-solen-muted block mb-[var(--space-sm)]">
          {todayStr}
        </span>
        <h1 className="font-display text-[length:var(--fs-heading)] leading-[1.15] tracking-[-0.015em] font-normal">{getGreeting()}.</h1>
      </div>

      <DailyCheckInForm practices={practices} onSave={handleDailyCheckInSave} />

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
