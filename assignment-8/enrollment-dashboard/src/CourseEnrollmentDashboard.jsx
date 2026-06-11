import { useState } from "react";

// ─── Initial seed data ───────────────────────────────────────────────────────
const initialStudents = new Map([
  [1, { id: 1, name: "Alice",   enrolledCourses: new Set(["Math", "Physics", "Chemistry"]), gpa: 3.8 }],
  [2, { id: 2, name: "Bob",     enrolledCourses: new Set(["Math", "Biology", "English"]),   gpa: 3.5 }],
  [3, { id: 3, name: "Charlie", enrolledCourses: new Set(["Physics", "Chemistry", "CS"]),   gpa: 3.9 }],
  [4, { id: 4, name: "Diana",   enrolledCourses: new Set(["Math", "CS", "English"]),        gpa: 3.2 }],
]);

// ─── Complexity note (displayed in UI) ───────────────────────────────────────
// Filter by course: O(N * C) where N = students, C = avg courses per student
// Set.has() is O(1), so overall O(N)

let nextId = 5;

export default function CourseEnrollmentDashboard() {
  // ── State ──────────────────────────────────────────────────────────────────
  const [studentMap, setStudentMap] = useState(initialStudents);
  const [filterCourse, setFilterCourse] = useState("");
  const [form, setForm]               = useState({ name: "", courses: "", gpa: "" });
  const [error, setError]             = useState("");

  // ── Derived data ───────────────────────────────────────────────────────────
  // Convert Map → array, sort by GPA descending
  const allStudents = [...studentMap.values()].sort((a, b) => b.gpa - a.gpa);

  // All unique courses via reduce + Set
  const allCourses = [...allStudents.reduce((acc, s) => {
    [...s.enrolledCourses].forEach(c => acc.add(c));
    return acc;
  }, new Set())].sort();

  // Filter students by selected course — O(N)
  const displayedStudents = filterCourse
    ? allStudents.filter(s => s.enrolledCourses.has(filterCourse))
    : allStudents;

  // ── Add student ────────────────────────────────────────────────────────────
  function handleAdd() {
    const name    = form.name.trim();
    const gpa     = parseFloat(form.gpa);
    const courses = form.courses.split(",").map(c => c.trim()).filter(Boolean);

    if (!name)                        return setError("Name is required.");
    if (isNaN(gpa) || gpa < 0 || gpa > 4) return setError("GPA must be between 0 and 4.");
    if (courses.length === 0)         return setError("Enter at least one course.");

    const newStudent = {
      id: nextId,
      name,
      enrolledCourses: new Set(courses),
      gpa,
    };

    // Spread operator — no direct mutation
    setStudentMap(prev => new Map([...prev, [nextId, newStudent]]));
    nextId++;
    setForm({ name: "", courses: "", gpa: "" });
    setError("");
  }

  // ── Remove student ─────────────────────────────────────────────────────────
  function handleRemove(id) {
    setStudentMap(prev => {
      const next = new Map([...prev]);
      next.delete(id);
      return next;
    });
  }

  // ── GPA badge colour ───────────────────────────────────────────────────────
  function gpaBadgeStyle(gpa) {
    if (gpa >= 3.7) return { background: "#d1fae5", color: "#065f46" };
    if (gpa >= 3.0) return { background: "#fef9c3", color: "#713f12" };
    return              { background: "#fee2e2", color: "#991b1b" };
  }

  // ── Render ─────────────────────────────────────────────────────────────────
  return (
    <div style={styles.page}>
      {/* ── Header ── */}
      <header style={styles.header}>
        <div style={styles.headerInner}>
          <span style={styles.logoMark}>⬡</span>
          <div>
            <h1 style={styles.title}>Enrollment Dashboard</h1>
            <p style={styles.subtitle}>Course & student management</p>
          </div>
        </div>
        <div style={styles.statsRow}>
          <Stat label="Students"       value={studentMap.size} />
          <Stat label="Unique Courses" value={allCourses.length} />
          <Stat label="Avg GPA"        value={
            studentMap.size
              ? (allStudents.reduce((s, x) => s + x.gpa, 0) / studentMap.size).toFixed(2)
              : "—"
          } />
        </div>
      </header>

      <main style={styles.main}>
        {/* ── Add Student ── */}
        <section style={styles.card}>
          <h2 style={styles.cardTitle}>Add Student</h2>
          <div style={styles.formGrid}>
            <input
              style={styles.input}
              placeholder="Full name"
              value={form.name}
              onChange={e => setForm({ ...form, name: e.target.value })}
            />
            <input
              style={styles.input}
              placeholder="GPA (0 – 4)"
              type="number"
              min="0" max="4" step="0.1"
              value={form.gpa}
              onChange={e => setForm({ ...form, gpa: e.target.value })}
            />
            <input
              style={{ ...styles.input, gridColumn: "1 / -1" }}
              placeholder="Courses (comma-separated): Math, Physics, CS"
              value={form.courses}
              onChange={e => setForm({ ...form, courses: e.target.value })}
            />
          </div>
          {error && <p style={styles.error}>{error}</p>}
          <button style={styles.btnPrimary} onClick={handleAdd}>+ Add Student</button>
        </section>

        {/* ── Unique Courses ── */}
        <section style={styles.card}>
          <h2 style={styles.cardTitle}>All Unique Courses</h2>
          <div style={styles.tagCloud}>
            {allCourses.map(c => (
              <button
                key={c}
                style={{
                  ...styles.courseTag,
                  ...(filterCourse === c ? styles.courseTagActive : {}),
                }}
                onClick={() => setFilterCourse(prev => prev === c ? "" : c)}
              >
                {c}
              </button>
            ))}
            {allCourses.length === 0 && <p style={styles.empty}>No courses yet.</p>}
          </div>
          {filterCourse && (
            <div style={styles.filterBanner}>
              Showing students enrolled in <strong>{filterCourse}</strong>
              <button style={styles.clearBtn} onClick={() => setFilterCourse("")}>✕ Clear</button>
            </div>
          )}
        </section>

        {/* ── Student List ── */}
        <section style={styles.card}>
          <div style={styles.cardTitleRow}>
            <h2 style={styles.cardTitle}>
              Students
              <span style={styles.countBadge}>{displayedStudents.length}</span>
            </h2>
            <span style={styles.sortNote}>↓ sorted by GPA</span>
          </div>

          {displayedStudents.length === 0 && (
            <p style={styles.empty}>No students match the current filter.</p>
          )}

          <div style={styles.studentGrid}>
            {displayedStudents.map(student => (
              <div key={student.id} style={styles.studentCard}>
                <div style={styles.studentTop}>
                  <div style={styles.avatar}>{student.name[0]}</div>
                  <div>
                    <p style={styles.studentName}>{student.name}</p>
                    <p style={styles.studentId}>ID #{student.id}</p>
                  </div>
                  <span style={{ ...styles.gpaBadge, ...gpaBadgeStyle(student.gpa) }}>
                    {student.gpa.toFixed(1)}
                  </span>
                </div>
                {/* Convert Set → array before rendering */}
                <div style={styles.courseList}>
                  {[...student.enrolledCourses].map(c => (
                    <span key={c} style={styles.coursePill}>{c}</span>
                  ))}
                </div>
                <button
                  style={styles.removeBtn}
                  onClick={() => handleRemove(student.id)}
                >
                  Remove
                </button>
              </div>
            ))}
          </div>
        </section>

        {/* ── Complexity Note ── */}
        <section style={styles.complexityBox}>
          <h3 style={styles.complexityTitle}>⏱ Complexity Analysis</h3>
          <div style={styles.complexityGrid}>
            <ComplexityRow
              op="Filter students by course"
              time="O(N)"
              space="O(N)"
              note="Set.has() is O(1); we visit each of N students once"
            />
            <ComplexityRow
              op="Sort students by GPA"
              time="O(N log N)"
              space="O(N)"
              note="JavaScript's Array.sort uses Timsort"
            />
            <ComplexityRow
              op="Collect unique courses"
              time="O(N × C)"
              space="O(U)"
              note="C = avg courses/student, U = unique courses"
            />
            <ComplexityRow
              op="Add student"
              time="O(1)"
              space="O(C)"
              note="Map.set is O(1) amortized"
            />
            <ComplexityRow
              op="Remove student by ID"
              time="O(1)"
              space="O(1)"
              note="Map.delete is O(1)"
            />
          </div>
        </section>
      </main>
    </div>
  );
}

// ─── Sub-components ───────────────────────────────────────────────────────────
function Stat({ label, value }) {
  return (
    <div style={styles.stat}>
      <span style={styles.statValue}>{value}</span>
      <span style={styles.statLabel}>{label}</span>
    </div>
  );
}

function ComplexityRow({ op, time, space, note }) {
  return (
    <div style={styles.complexityRow}>
      <span style={styles.cxOp}>{op}</span>
      <span style={styles.cxBadge}>{time}</span>
      <span style={styles.cxBadgeSpace}>{space}</span>
      <span style={styles.cxNote}>{note}</span>
    </div>
  );
}

// ─── Styles ───────────────────────────────────────────────────────────────────
const styles = {
  page: {
    minHeight: "100vh",
    background: "#0f1117",
    fontFamily: "'Inter', 'Segoe UI', sans-serif",
    color: "#e2e8f0",
  },
  header: {
    background: "linear-gradient(135deg, #1a1f2e 0%, #0f1117 100%)",
    borderBottom: "1px solid #2d3748",
    padding: "28px 32px 20px",
  },
  headerInner: {
    display: "flex",
    alignItems: "center",
    gap: 16,
    marginBottom: 20,
  },
  logoMark: {
    fontSize: 36,
    color: "#818cf8",
    lineHeight: 1,
  },
  title: {
    margin: 0,
    fontSize: 26,
    fontWeight: 700,
    color: "#f1f5f9",
    letterSpacing: "-0.5px",
  },
  subtitle: {
    margin: "2px 0 0",
    fontSize: 13,
    color: "#64748b",
  },
  statsRow: {
    display: "flex",
    gap: 32,
  },
  stat: {
    display: "flex",
    flexDirection: "column",
  },
  statValue: {
    fontSize: 28,
    fontWeight: 700,
    color: "#818cf8",
    lineHeight: 1,
  },
  statLabel: {
    fontSize: 11,
    color: "#64748b",
    textTransform: "uppercase",
    letterSpacing: "0.08em",
    marginTop: 2,
  },
  main: {
    maxWidth: 900,
    margin: "0 auto",
    padding: "28px 20px",
    display: "flex",
    flexDirection: "column",
    gap: 20,
  },
  card: {
    background: "#1a1f2e",
    border: "1px solid #2d3748",
    borderRadius: 12,
    padding: "22px 24px",
  },
  cardTitle: {
    margin: "0 0 16px",
    fontSize: 15,
    fontWeight: 600,
    color: "#cbd5e1",
    textTransform: "uppercase",
    letterSpacing: "0.06em",
    display: "flex",
    alignItems: "center",
    gap: 10,
  },
  cardTitleRow: {
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    marginBottom: 16,
  },
  countBadge: {
    background: "#312e81",
    color: "#a5b4fc",
    borderRadius: 999,
    fontSize: 11,
    fontWeight: 700,
    padding: "2px 8px",
    marginLeft: 8,
  },
  sortNote: {
    fontSize: 12,
    color: "#475569",
  },
  formGrid: {
    display: "grid",
    gridTemplateColumns: "1fr 1fr",
    gap: 10,
    marginBottom: 12,
  },
  input: {
    background: "#0f1117",
    border: "1px solid #334155",
    borderRadius: 8,
    color: "#e2e8f0",
    fontSize: 14,
    padding: "10px 12px",
    outline: "none",
    width: "100%",
    boxSizing: "border-box",
  },
  btnPrimary: {
    background: "#4f46e5",
    color: "#fff",
    border: "none",
    borderRadius: 8,
    padding: "10px 20px",
    fontSize: 14,
    fontWeight: 600,
    cursor: "pointer",
  },
  error: {
    color: "#f87171",
    fontSize: 13,
    margin: "0 0 10px",
  },
  tagCloud: {
    display: "flex",
    flexWrap: "wrap",
    gap: 8,
  },
  courseTag: {
    background: "#1e293b",
    border: "1px solid #334155",
    color: "#94a3b8",
    borderRadius: 999,
    padding: "5px 14px",
    fontSize: 13,
    cursor: "pointer",
    transition: "all 0.15s",
  },
  courseTagActive: {
    background: "#312e81",
    border: "1px solid #6366f1",
    color: "#a5b4fc",
  },
  filterBanner: {
    marginTop: 14,
    background: "#1e2a4a",
    border: "1px solid #3b4f8a",
    borderRadius: 8,
    padding: "8px 14px",
    fontSize: 13,
    color: "#93c5fd",
    display: "flex",
    alignItems: "center",
    gap: 10,
  },
  clearBtn: {
    background: "none",
    border: "none",
    color: "#60a5fa",
    cursor: "pointer",
    fontSize: 13,
    marginLeft: "auto",
    padding: 0,
  },
  empty: {
    color: "#475569",
    fontSize: 13,
    margin: 0,
  },
  studentGrid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fill, minmax(260px, 1fr))",
    gap: 14,
  },
  studentCard: {
    background: "#0f1117",
    border: "1px solid #2d3748",
    borderRadius: 10,
    padding: "16px",
    display: "flex",
    flexDirection: "column",
    gap: 12,
  },
  studentTop: {
    display: "flex",
    alignItems: "center",
    gap: 12,
  },
  avatar: {
    width: 40,
    height: 40,
    borderRadius: "50%",
    background: "linear-gradient(135deg, #6366f1, #8b5cf6)",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    fontWeight: 700,
    fontSize: 17,
    color: "#fff",
    flexShrink: 0,
  },
  studentName: {
    margin: 0,
    fontWeight: 600,
    fontSize: 15,
    color: "#f1f5f9",
  },
  studentId: {
    margin: "2px 0 0",
    fontSize: 12,
    color: "#475569",
  },
  gpaBadge: {
    marginLeft: "auto",
    borderRadius: 8,
    padding: "3px 9px",
    fontSize: 13,
    fontWeight: 700,
  },
  courseList: {
    display: "flex",
    flexWrap: "wrap",
    gap: 6,
  },
  coursePill: {
    background: "#1e293b",
    color: "#7dd3fc",
    borderRadius: 6,
    fontSize: 11,
    padding: "3px 8px",
    border: "1px solid #1e3a5f",
  },
  removeBtn: {
    background: "none",
    border: "1px solid #3f3f3f",
    color: "#ef4444",
    borderRadius: 7,
    padding: "6px",
    fontSize: 12,
    cursor: "pointer",
    marginTop: "auto",
  },
  complexityBox: {
    background: "#0d1117",
    border: "1px solid #1e3a2e",
    borderRadius: 12,
    padding: "20px 24px",
  },
  complexityTitle: {
    margin: "0 0 14px",
    fontSize: 14,
    fontWeight: 600,
    color: "#34d399",
    letterSpacing: "0.04em",
  },
  complexityGrid: {
    display: "flex",
    flexDirection: "column",
    gap: 10,
  },
  complexityRow: {
    display: "grid",
    gridTemplateColumns: "220px 90px 90px 1fr",
    alignItems: "center",
    gap: 10,
    fontSize: 13,
  },
  cxOp: {
    color: "#cbd5e1",
    fontWeight: 500,
  },
  cxBadge: {
    background: "#14532d",
    color: "#4ade80",
    borderRadius: 6,
    padding: "2px 8px",
    fontSize: 12,
    fontWeight: 700,
    textAlign: "center",
  },
  cxBadgeSpace: {
    background: "#1e3a5f",
    color: "#60a5fa",
    borderRadius: 6,
    padding: "2px 8px",
    fontSize: 12,
    fontWeight: 700,
    textAlign: "center",
  },
  cxNote: {
    color: "#475569",
    fontSize: 12,
  },
};
