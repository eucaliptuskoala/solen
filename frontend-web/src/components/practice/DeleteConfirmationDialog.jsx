import Button from "../ui/Button";
import Modal from "../ui/Modal";

function DeleteConfirmationDialog({ isOpen, practiceName, onCancel, onConfirm }) {
  return (
    <Modal isOpen={isOpen} onClose={onCancel} maxWidth="400px">
        <h3 className="font-display text-[1.15rem] font-[400] mb-[var(--space-sm)]">Delete practice?</h3>
        <p className="text-[0.9rem] text-solen-muted mb-[var(--space-lg)] leading-[1.6]">
          Are you sure you want to delete "{practiceName}"? This will remove the practice
          and all its check-ins. This can't be undone.
        </p>
        <div className="flex gap-[var(--space-sm)] justify-end">
          <Button variant="secondary" onClick={onCancel}>Cancel</Button>
          <Button variant="danger" onClick={onConfirm}>Delete</Button>
        </div>
    </Modal>
  );
}

export default DeleteConfirmationDialog;
