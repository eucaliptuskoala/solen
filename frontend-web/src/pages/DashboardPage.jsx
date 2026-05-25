import { useState, useEffect, useCallback } from "react";
import PracticeAPI from "../apis/PracticeAPI";
import CheckInAPI from "../apis/CheckInAPI";
import AuthHandler from "../apis/AuthHandler";
import PracticeCardList from "../components/practice/PracticeCardList";
import CreatePracticeModal from "../components/practice/CreatePracticeModal";
import DeleteConfirmationDialog from "../components/practice/DeleteConfirmationDialog";
import CheckInPopup from "../components/checkin/CheckInPopup";
import DailyCheckInForm from "../components/checkin/DailyCheckInForm";
import PageHeader from "../components/ui/PageHeader";
import { today, formatDate } from "../utils/dates";
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

  const [popupPractice, setPopupPractice] = useState(null);

  const todayStr = formatDate(today());

  const fetchPractices = useCallback(() => {
    PracticeAPI.getPracticesByUser()
      .then(setPractices)
      .catch((err) => console.error("Failed to fetch practices", err));
  }, []);

  useEffect(() => { fetchPractices(); }, [fetchPractices]);

  const handleDone = (practiceId) => {
    const practice = practices.find((p) => p.id === practiceId);
    setPopupPractice(practice || { id: practiceId, name: "Practice" });
  };

  const saveCheckIn = async (data) => {
    try {
      await CheckInAPI.create(data);
      showToast("Check-in saved!");
      fetchPractices();
    } catch {
      showToast("Failed to save check-in.");
    }
  };

  const handlePopupSave = async (data) => {
    await saveCheckIn(data);
    setPopupPractice(null);
  };

  const handleDailyCheckInSave = saveCheckIn;

  const handleCreate = () => {
    const userId = AuthHandler.getUserId();
    if (!userId) return;
    PracticeAPI.createPractice({
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
      .catch((err) => console.error("Failed to create practice", err));
  };

  const handleDeleteTarget = (practice) => setDeleteTarget(practice);

  const handleDeleteConfirm = () => {
    if (!deleteTarget) return;
    PracticeAPI.deletePractice(deleteTarget.id)
      .then(() => {
        setDeleteTarget(null);
        fetchPractices();
      })
      .catch((err) => console.error("Failed to delete practice", err));
  };

  return (
    <main className="max-w-[1280px] mx-auto px-[var(--gutter)] py-[var(--space-xl)]">
      <PageHeader eyebrow={todayStr} title={`${getGreeting()}.`} />

      <DailyCheckInForm practices={practices} onSave={handleDailyCheckInSave} />

      <div className="border-t border-solen-border mb-[var(--space-lg)]" />

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
        isOpen={!!popupPractice}
        practiceId={popupPractice?.id}
        practiceName={popupPractice?.name}
        onSave={handlePopupSave}
        onClose={() => setPopupPractice(null)}
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
