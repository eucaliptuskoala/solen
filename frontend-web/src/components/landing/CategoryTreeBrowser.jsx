import { useState, useEffect } from "react";
import CategoryAPI from "../../apis/CategoryAPI";
import Button from "../ui/Button";
import TreeView from "../ui/TreeView";

function collectLeaves(nodes, map) {
  for (const n of nodes) {
    if (n.children && n.children.length > 0) {
      collectLeaves(n.children, map);
    } else {
      map.set(n.id, n.name);
    }
  }
}

function CategoryTreeBrowser({ onSelect }) {
  const [tree, setTree] = useState([]);
  const [leafNames, setLeafNames] = useState(new Map());
  const [selectedIds, setSelectedIds] = useState(new Set());

  useEffect(() => {
    CategoryAPI.getTree().then((data) => {
      setTree(data);
      const map = new Map();
      collectLeaves(data, map);
      setLeafNames(map);
    }).catch((err) => console.error("Failed to fetch category tree", err));
  }, []);

  const handleToggle = (id) => {
    setSelectedIds((prev) => {
      const next = new Set(prev);
      if (next.has(id)) next.delete(id);
      else next.add(id);
      return next;
    });
  };

  const handleFinish = () => {
    const selected = [...selectedIds].map((id) => ({
      id,
      name: leafNames.get(id) || "Practice",
    }));
    onSelect(selected);
  };

  return (
    <div>
      <div className="bg-solen-surface border border-solen-border rounded-[8px] p-[var(--space-md)] max-h-[420px] overflow-y-auto">
        <TreeView nodes={tree} selectedIds={selectedIds} onToggle={handleToggle} defaultOpenDepth={1} showIcons />
      </div>
      <div className="flex items-center justify-between mt-[var(--space-md)]">
        <span className="font-mono text-[0.75rem] text-solen-muted">{selectedIds.size} practices selected</span>
      </div>
      <div className="flex items-center justify-between mt-[var(--space-xl)] pt-[var(--space-lg)] border-t border-solen-border">
        <Button variant="secondary" className="text-solen-muted" onClick={() => onSelect([])}>Skip</Button>
        <Button variant="primary" onClick={handleFinish}>Finish</Button>
      </div>
    </div>
  );
}

export default CategoryTreeBrowser;
