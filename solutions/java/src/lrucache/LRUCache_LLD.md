# LRU Cache - Low Level Design (LLD)

This document serves as a comprehensive interview preparation guide for the **Least Recently Used (LRU) Cache** Low-Level Design (LLD). It is structured to mirror an actual Microsoft SDE-2 interview flow, moving from problem definition to data structure selection, implementation details, and design principles.

---

## 1. Problem Statement & Clarifying Requirements

**Interviewer:** "Design a data structure that follows the constraints of a Least Recently Used (LRU) cache."

**Candidate (You):** "Before we jump into the design, let me clarify a few requirements to ensure we are on the same page:
1.  **Fixed Capacity:** The cache will be initialized with a positive integer capacity. Once this capacity is reached, adding a new item should evict the least recently used item.
2.  **Core Operations:** We need to support `get(key)` and `put(key, value)` operations.
3.  **Time Complexity:** Both operations should ideally work in **O(1)** time complexity.
4.  **Thread Safety:** Should the cache support concurrent access from multiple threads? (Our implementation will support basic thread safety)."

---

## 2. High-Level Approach & Data Structure Selection

**Candidate:** "To achieve **O(1)** time complexity for both `get` and `put` operations, we need a combination of two data structures:

1.  **HashMap (Map):** This will map the `key` to the actual node containing the value. This gives us the O(1) lookup time.
2.  **Doubly Linked List (DLL):** This will maintain the 'recency' of access.
    *   The **Most Recently Used (MRU)** items will be kept near the **head** (front) of the list.
    *   The **Least Recently Used (LRU)** items will naturally fall to the **tail** (end) of the list.
    *   We use a Doubly Linked List because removing an arbitrary node (which we do when an item is accessed and moved to the front) takes O(1) time if we have a pointer to it. A singly linked list would require O(N) to find the previous node.

**Data Node Structure:**
Each node in our DLL must contain both the `key` and the `value`. Why the key? When the cache is full, we must evict the node at the tail of the DLL. To successfully remove this node's reference from the HashMap, we need to know its key."

---

## 3. Core Logic & Flow Diagrams

### `get(key)` Operation

When we get an item by its key:
1.  If the key is not in the map, return `null`.
2.  If the key exists, fetch the node from the map.
3.  Because it was just accessed, move this node to the front of the DLL (making it the most recently used).
4.  Return the node's value.

```mermaid
graph TD
    A[Start: get(key)] --> B{Is key in HashMap?}
    B -- No --> C[Return null]
    B -- Yes --> D[Fetch Node from HashMap]
    D --> E[Move Node to Front of DLL]
    E --> F[Return Node.value]
```

### `put(key, value)` Operation

When we insert or update an item:
1.  If the key already exists: Update the node's value and move it to the front of the DLL.
2.  If the key doesn't exist:
    *   Check if the cache is at full capacity.
    *   If full, remove the least recently used node from the tail of the DLL and delete its key from the HashMap.
    *   Create a new Node, add it to the front of the DLL, and put it in the HashMap.

```mermaid
graph TD
    A[Start: put(key, value)] --> B{Is key in HashMap?}
    B -- Yes --> C[Update Node's value]
    C --> D[Move Node to Front of DLL]
    B -- No --> E{Is Cache Full?}
    E -- Yes --> F[Remove Node at DLL Tail]
    F --> G[Remove Node.key from HashMap]
    G --> H
    E -- No --> H[Create New Node]
    H --> I[Add Node to Front of DLL]
    I --> J[Add Key and Node to HashMap]
```

---

## 4. Object-Oriented Design & Principles

**Candidate:** "Let me walk you through how I've structured the code using standard Object-Oriented principles."

### Design Principles Applied:

1.  **Separation of Concerns (Single Responsibility Principle):**
    *   The `Node` class strictly acts as a data container.
    *   The `DoublyLinkedList` class solely manages list operations (`addFirst`, `remove`, `moveToFront`, `removeLast`). It doesn't know about cache eviction policies or capacities.
    *   The `LRUCache` acts as the orchestrator. It manages the `HashMap` and the capacity logic, delegating list manipulations to the `DoublyLinkedList`.
2.  **Encapsulation:**
    *   The internal workings (`head`, `tail`, `HashMap`, `DoublyLinkedList`) are kept `private`. The user only interacts with `get()`, `put()`, and `remove()`.
    *   We use dummy `head` and `tail` nodes in the DLL. This elegant trick eliminates edge cases (like checking for null heads or tails) when adding or removing nodes.

---

## 5. Implementation Deep Dive (Code Walkthrough)

*Interview Tip: You don't need to write all the code on a whiteboard unless asked, but you should be able to explain the critical components confidently.*

**The Generic Node:**
Supports generics `<K, V>` for flexibility.
```java
class Node<K, V> {
    K key;
    V value;
    Node<K, V> prev, next;
    // Constructor...
}
```

**The Doubly Linked List:**
Handles structural changes seamlessly. Note the dummy head/tail usage.
```java
class DoublyLinkedList<K, V> {
    private final Node<K, V> head;
    private final Node<K, V> tail;
    
    // ... initialize dummy head and tail linking to each other ...

    public void moveToFront(Node<K, V> node) {
        remove(node);     // Detach from current position
        addFirst(node);   // Attach right after dummy head
    }
    // ... addFirst, remove, removeLast ...
}
```

**The LRU Cache Orchestrator:**
Utilizes `synchronized` methods to ensure basic thread safety. In a high-throughput production environment, we might discuss using `ConcurrentHashMap` and more granular locking mechanisms (like `ReentrantReadWriteLock`), but method-level synchronization is a solid, correct starting point.

```java
public class LRUCache<K, V> {
    private final int capacity;
    private final Map<K, Node<K, V>> map;
    private final DoublyLinkedList<K, V> dll;

    // ... Constructor ...

    public synchronized V get(K key) { ... }
    public synchronized void put(K key, V value) { ... }
}
```

---

## 6. Time and Space Complexity

To wrap up the technical discussion:
*   **Time Complexity:** 
    *   `get(key)`: **O(1)**. HashMap lookup is O(1) and moving a node in our DLL is O(1).
    *   `put(key, value)`: **O(1)**. HashMap insertion/deletion is O(1), and DLL boundary operations are O(1).
*   **Space Complexity:** **O(C)** where `C` is the capacity of the cache. We store at most `C` elements in both the HashMap and the Doubly Linked List.

---

## Summary for the Interviewer
"By isolating the linked list logic from the cache orchestration logic, the code is highly readable and testable. The combination of HashMap and a Doubly Linked List allows us to strictly meet the O(1) time complexity constraints while safely handling capacity eviction."
