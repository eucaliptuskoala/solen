import { useState, useEffect, useCallback } from "react";
import CheckInAPI from "../apis/CheckInAPI";
import HabitAPI from "../apis/HabitAPI";
import CheckInTimeline from "../components/checkin/CheckInTimeline";
import EditCheckInModal from "../components/checkin/EditCheckInModal";
import CheckInPopup from "../components/checkin/CheckInPopup";
import useToast from "../hooks/useToast";

function CheckInsPage() {
  const [entries, setEntries] = useState([]);
  const [practices, setPractices] = useState([]);
  const [editEntry, setEditEntry] = useState(null);
  const [showEdit, setShowEdit] = useState(false);
  const [showNew, setShowNew] = useState(false);
  const { toast, showToast } = useToast();

  const fetchEntries = useCallback(() => {
    CheckInAPI.getAll()
      .then(setEntries)
      .catch((err) => console.error("Failed to fetch entries", err));
  }, []);

  const fetchPractices = useCallback(() => {
    HabitAPI.getHabitsByUser()
      .then(setPractices)
      .catch((err) => console.error("Failed to fetch practices", err));
  }, []);

  useEffect(() => { fetchEntries(); }, [fetchEntries]);
  useEffect(() => { fetchPractices(); }, [fetchPractices]);

  const handleEdit = (entry) => {
    setEditEntry(entry);
    setShowEdit(true);
  };

  const handleSave = (id, request) => {
    CheckInAPI.update(id, request)
      .then((updated) => {
        setEntries((prev) => prev.map((e) => (e.id === id ? updated : e)));
        setShowEdit(false);
        setEditEntry(null);
      })
      .catch((err) => console.error("Failed to update check-in", err));
  };

  const handleDelete = (entry) => {
    if (!window.confirm(`Delete this check-in?`)) return;
    CheckInAPI.delete(entry.id)
      .then(() => {
        setEntries((prev) => prev.filter((e) => e.id !== entry.id));
      })
      .catch((err) => console.error("Failed to delete check-in", err));
  };

  const handleNewCheckIn = async ({ habitId, mood, content, public: isPublic }) => {
    await CheckInAPI.create({ habitId, mood, content, public: isPublic });
    setShowNew(false);
    showToast("Check-in saved!");
    fetchEntries();
  };

  return (
    <main className="max-w-[1280px] mx-auto px-[var(--gutter)] py-[var(--space-xl)]">
      <CheckInTimeline entries={entries} onEdit={handleEdit} onDelete={handleDelete} onCreate={() => setShowNew(true)} />

      <EditCheckInModal
        isOpen={showEdit}
        entry={editEntry}
        onSave={handleSave}
        onClose={() => { setShowEdit(false); setEditEntry(null); }}
      />

      <CheckInPopup
        isOpen={showNew}
        habitId={null}
        habitName={null}
        availablePractices={practices}
        onSave={handleNewCheckIn}
        onClose={() => setShowNew(false)}
      />

      {toast && <div className="toast">{toast}</div>}
    </main>
  );
}

export default CheckInsPage;
