import sys
import gc
# Enable automatic garbage collection
gc.enable()

class Node:
    def __init__(self, name):
        self.name = name
        self.link = None

    def __del__(self):
        print(f"{self.name} is being destroyed")

# -----------------------------
# Step 1: Create Nodes
# -----------------------------
A = Node("Node A")
B = Node("Node B")
# -----------------------------
# Step 2: Create a Cycle
# A -> B and B -> A
# -----------------------------
A.link = B
B.link = A
# -----------------------------
# Step 3: Check Reference Counts
# -----------------------------
print("Reference Counts Before Deletion:")
print("A:", sys.getrefcount(A))
print("B:", sys.getrefcount(B))
# Keep object IDs for investigation later
a_id = id(A)
b_id = id(B)
# -----------------------------
# Step 4: Delete External References
# -----------------------------
del A
del B
print("\nDeleted A and B variables")
# -----------------------------
# Step 5: Investigation
# Objects still exist because
# they reference each other
# -----------------------------
print("\nChecking objects still tracked by GC:")
found = False
for obj in gc.get_objects():
 if id(obj) == a_id or id(obj) == b_id:
  print(f"Object still exists in memory: {obj.name}")
found = True

if not found:
 print("Objects not found")
# -----------------------------
# Step 6: Force Garbage Collection
# -----------------------------
print("\nRunning Garbage Collector...")
collected = gc.collect()
print("Unreachable objects collected:", collected)
# -----------------------------
# Step 7: Verify Cleanup
# -----------------------------
print("\nChecking again after gc.collect():")
found = False
for obj in gc.get_objects():
 if id(obj) == a_id or id(obj) == b_id:
  print(f"Still exists: {obj.name}")
found = True
if not found:
 print("Cycle cleaned successfully")