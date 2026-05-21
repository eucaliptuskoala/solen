import { useState } from "react";

function TreeNode({ node, selectedIds, onToggle, onSelect, depth, defaultOpenDepth, showIcons }) {
  const [open, setOpen] = useState(depth < defaultOpenDepth);
  const hasChildren = node.children && node.children.length > 0;

  if (hasChildren) {
    return (
      <li>
        <div
          className="flex items-center gap-[var(--space-sm)] px-3 py-2 rounded-[8px] cursor-pointer select-none hover:bg-solen-surface-soft transition-[background] duration-150"
          onClick={() => setOpen((p) => !p)}
        >
          <span className={`w-5 h-5 flex items-center justify-center transition-transform duration-200 shrink-0${open ? " rotate-90" : ""}`}>
            <svg width="12" height="12" viewBox="0 0 12 12"><path d="M4 2l4 4-4 4" stroke="currentColor" strokeWidth="1.5" fill="none" /></svg>
          </span>
          {showIcons && (
            <span className="w-5 h-5 rounded-sm flex items-center justify-center font-mono text-[0.6rem] text-solen-muted shrink-0">{node.icon || "\u{1F4C1}"}</span>
          )}
          <span>{node.name}</span>
        </div>
        {hasChildren && (
          <ul className={`pl-7 ${open ? "block" : "hidden"}`}>
            {node.children.map((child) => (
              <TreeNode key={child.id} node={child} selectedIds={selectedIds} onToggle={onToggle} onSelect={onSelect} depth={depth + 1} defaultOpenDepth={defaultOpenDepth} showIcons={showIcons} />
            ))}
          </ul>
        )}
      </li>
    );
  }

  if (onSelect) {
    return (
      <li>
        <div
          className="flex items-center gap-2 px-3 py-1.5 rounded-[8px] cursor-pointer transition-colors duration-150 text-sm hover:bg-solen-surface-soft"
          onClick={() => onSelect(node)}
        >
          {node.name}
        </div>
      </li>
    );
  }

  const isSelected = selectedIds && selectedIds.has(node.id);
  return (
    <li>
      <div
        className={`flex items-center gap-[var(--space-sm)] pl-10 pr-3 py-[6px] rounded-[8px] cursor-pointer text-[0.9rem] hover:bg-solen-surface-soft transition-[background] duration-150${isSelected ? " bg-solen-accent-subtle text-solen-accent" : ""}`}
        onClick={() => onToggle(node.id)}
      >
        {node.name}
      </div>
    </li>
  );
}

function TreeView({ nodes, selectedIds, onToggle, onSelect, defaultOpenDepth = 0, showIcons = false }) {
  return (
    <ul className="list-none">
      {nodes.map((node) => (
        <TreeNode key={node.id} node={node} selectedIds={selectedIds} onToggle={onToggle} onSelect={onSelect} depth={0} defaultOpenDepth={defaultOpenDepth} showIcons={showIcons} />
      ))}
    </ul>
  );
}

export default TreeView;
