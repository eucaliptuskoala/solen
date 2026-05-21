import { useState, useEffect } from "react";
import CategoryAPI from "../../apis/CategoryAPI";
import Button from "../ui/Button";
import Input from "../ui/Input";
import Textarea from "../ui/Textarea";
import Modal from "../ui/Modal";
import TreeView from "../ui/TreeView";

function CreatePracticeModal({ isOpen, onClose, onCreate, categoryId, setCategoryId, name, setName, description, setDescription }) {
  const [tree, setTree] = useState([]);

  useEffect(() => {
    if (isOpen) {
      CategoryAPI.getTree().then(setTree).catch((err) => console.error("Failed to load categories", err));
    }
  }, [isOpen]);

  const handleSubmit = (e) => {
    e.preventDefault();
    onCreate();
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose}>
        <h3 className="font-display text-[1.15rem] font-[400] mb-[var(--space-sm)]">New practice</h3>
        <p className="text-[0.9rem] text-solen-muted mb-[var(--space-lg)] leading-[1.6]">Choose a category and name your practice.</p>

        <form onSubmit={handleSubmit}>
          <div className="flex flex-col gap-1.5 mb-4">
            <label>Category</label>
            <div className="overflow-y-auto" style={{ maxHeight: "240px" }}>
              <TreeView nodes={tree} onSelect={(node) => setCategoryId(node.id)} />
            </div>
            {categoryId && <span className="font-body text-sm leading-relaxed text-solen-accent mt-1 block">Category selected</span>}
          </div>

          <div className="flex flex-col gap-1.5 mb-4">
            <label htmlFor="practiceName">Practice name</label>
            <Input id="practiceName" size="sm" placeholder="e.g. Morning stretch" value={name} onChange={(e) => setName(e.target.value)} required />
          </div>

          <div className="flex flex-col gap-1.5 mb-4">
            <label htmlFor="practiceDesc">Description (optional)</label>
            <Textarea id="practiceDesc" size="sm" placeholder="What does this practice involve?" value={description} onChange={(e) => setDescription(e.target.value)} rows={2} />
          </div>

          <div className="flex gap-2 justify-end">
            <Button variant="secondary" onClick={onClose}>Cancel</Button>
            <Button variant="primary" type="submit">Create</Button>
          </div>
        </form>
    </Modal>
  );
}

export default CreatePracticeModal;
