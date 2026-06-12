import { useState } from "react";

const COLORS = {
  bg: "#F7F6F3",
  surface: "#FFFFFF",
  ink: "#1A1A1A",
  muted: "#8A8A8A",
  accent: "#4F46E5",
  accentLight: "#EEF2FF",
  border: "#E5E5E5",
  done: "#D1FAE5",
  doneText: "#065F46",
};

const styles = {
  page: {
    minHeight: "100vh",
    background: COLORS.bg,
    display: "flex",
    alignItems: "flex-start",
    justifyContent: "center",
    padding: "60px 16px",
    fontFamily: "'Inter', 'Helvetica Neue', Arial, sans-serif",
  },
  card: {
    background: COLORS.surface,
    borderRadius: "16px",
    boxShadow: "0 1px 3px rgba(0,0,0,0.08), 0 8px 32px rgba(0,0,0,0.06)",
    width: "100%",
    maxWidth: "480px",
    overflow: "hidden",
  },
  header: {
    padding: "32px 32px 24px",
    borderBottom: `1px solid ${COLORS.border}`,
  },
  eyebrow: {
    fontSize: "11px",
    fontWeight: 700,
    letterSpacing: "0.1em",
    textTransform: "uppercase",
    color: COLORS.accent,
    marginBottom: "8px",
  },
  title: {
    fontSize: "26px",
    fontWeight: 800,
    color: COLORS.ink,
    margin: 0,
    lineHeight: 1.2,
  },
  subtitle: {
    fontSize: "13px",
    color: COLORS.muted,
    marginTop: "6px",
  },
  inputArea: {
    padding: "24px 32px",
    borderBottom: `1px solid ${COLORS.border}`,
    display: "flex",
    gap: "10px",
  },
  input: {
    flex: 1,
    padding: "10px 14px",
    fontSize: "14px",
    border: `1.5px solid ${COLORS.border}`,
    borderRadius: "8px",
    outline: "none",
    color: COLORS.ink,
    background: COLORS.bg,
    transition: "border-color 0.15s",
    fontFamily: "inherit",
  },
  addBtn: {
    padding: "10px 18px",
    background: COLORS.accent,
    color: "#fff",
    border: "none",
    borderRadius: "8px",
    fontSize: "14px",
    fontWeight: 600,
    cursor: "pointer",
    whiteSpace: "nowrap",
    transition: "opacity 0.15s, transform 0.1s",
    fontFamily: "inherit",
  },
  listArea: {
    padding: "8px 0 16px",
  },
  empty: {
    textAlign: "center",
    padding: "40px 32px",
    color: COLORS.muted,
    fontSize: "14px",
  },
  emptyIcon: {
    fontSize: "32px",
    marginBottom: "10px",
  },
  item: {
    display: "flex",
    alignItems: "center",
    gap: "12px",
    padding: "10px 32px",
    transition: "background 0.1s",
    cursor: "pointer",
  },
  checkbox: {
    width: "18px",
    height: "18px",
    borderRadius: "5px",
    border: `2px solid ${COLORS.border}`,
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    flexShrink: 0,
    transition: "background 0.15s, border-color 0.15s",
    cursor: "pointer",
  },
  checkboxDone: {
    background: COLORS.accent,
    borderColor: COLORS.accent,
  },
  check: {
    color: "#fff",
    fontSize: "11px",
    fontWeight: 900,
  },
  itemText: {
    fontSize: "14px",
    color: COLORS.ink,
    flex: 1,
    lineHeight: 1.5,
  },
  itemTextDone: {
    textDecoration: "line-through",
    color: COLORS.muted,
  },
  deleteBtn: {
    background: "none",
    border: "none",
    color: COLORS.muted,
    cursor: "pointer",
    fontSize: "16px",
    padding: "2px 4px",
    borderRadius: "4px",
    lineHeight: 1,
    opacity: 0,
    transition: "opacity 0.15s, color 0.15s",
  },
  footer: {
    padding: "12px 32px",
    borderTop: `1px solid ${COLORS.border}`,
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  },
  footerText: {
    fontSize: "12px",
    color: COLORS.muted,
  },
  clearBtn: {
    fontSize: "12px",
    color: COLORS.accent,
    background: "none",
    border: "none",
    cursor: "pointer",
    padding: 0,
    fontFamily: "inherit",
    fontWeight: 600,
  },
};

export default function TodoList() {
  const [todos, setTodos] = useState([
    { id: 1, text: "Read the docs", done: true },
    { id: 2, text: "Build something with React", done: false },
  ]);
  const [input, setInput] = useState("");
  const [hoveredId, setHoveredId] = useState(null);

  const addTodo = () => {
    const trimmed = input.trim();
    if (!trimmed) return;
    setTodos((prev) => [
      ...prev,
      { id: Date.now(), text: trimmed, done: false },
    ]);
    setInput("");
  };

  const toggleTodo = (id) => {
    setTodos((prev) =>
      prev.map((t) => (t.id === id ? { ...t, done: !t.done } : t))
    );
  };

  const deleteTodo = (id) => {
    setTodos((prev) => prev.filter((t) => t.id !== id));
  };

  const clearDone = () => {
    setTodos((prev) => prev.filter((t) => !t.done));
  };

  const doneCount = todos.filter((t) => t.done).length;

  return (
    <div style={styles.page}>
      <div style={styles.card}>
        {/* Header */}
        <div style={styles.header}>
          <div style={styles.eyebrow}>My Workspace</div>
          <h1 style={styles.title}>To-do list</h1>
          <div style={styles.subtitle}>
            {doneCount} of {todos.length} tasks complete
          </div>
        </div>

        {/* Input */}
        <div style={styles.inputArea}>
          <input
            style={styles.input}
            type="text"
            placeholder="Add a new task…"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && addTodo()}
            onFocus={(e) =>
              (e.target.style.borderColor = COLORS.accent)
            }
            onBlur={(e) =>
              (e.target.style.borderColor = COLORS.border)
            }
          />
          <button
            style={styles.addBtn}
            onClick={addTodo}
            onMouseEnter={(e) => (e.target.style.opacity = "0.85")}
            onMouseLeave={(e) => (e.target.style.opacity = "1")}
          >
            Add
          </button>
        </div>

        {/* List */}
        <div style={styles.listArea}>
          {todos.length === 0 ? (
            <div style={styles.empty}>
              <div style={styles.emptyIcon}>✓</div>
              <div>All clear — add a task above to get started.</div>
            </div>
          ) : (
            todos.map((todo) => (
              <div
                key={todo.id}
                style={{
                  ...styles.item,
                  background:
                    hoveredId === todo.id ? COLORS.bg : "transparent",
                }}
                onMouseEnter={() => setHoveredId(todo.id)}
                onMouseLeave={() => setHoveredId(null)}
                onClick={() => toggleTodo(todo.id)}
              >
                {/* Checkbox */}
                <div
                  style={{
                    ...styles.checkbox,
                    ...(todo.done ? styles.checkboxDone : {}),
                  }}
                >
                  {todo.done && <span style={styles.check}>✓</span>}
                </div>

                {/* Text */}
                <span
                  style={{
                    ...styles.itemText,
                    ...(todo.done ? styles.itemTextDone : {}),
                  }}
                >
                  {todo.text}
                </span>

                {/* Delete */}
                <button
                  style={{
                    ...styles.deleteBtn,
                    opacity: hoveredId === todo.id ? 1 : 0,
                  }}
                  onClick={(e) => {
                    e.stopPropagation();
                    deleteTodo(todo.id);
                  }}
                  title="Remove task"
                >
                  ×
                </button>
              </div>
            ))
          )}
        </div>

        {/* Footer */}
        {doneCount > 0 && (
          <div style={styles.footer}>
            <span style={styles.footerText}>
              {doneCount} task{doneCount !== 1 ? "s" : ""} done
            </span>
            <button style={styles.clearBtn} onClick={clearDone}>
              Clear completed
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
