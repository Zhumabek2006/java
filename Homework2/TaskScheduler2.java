package Homework2;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        List<Integer> list1 = Arrays.asList(1, 3, 5);
        List<Integer> list2 = Arrays.asList(2, 4, 6);
        System.out.println("Merged List: " + mergeLists(list1, list2));

        Map<String, Integer> sampleMap = new HashMap<>();
        sampleMap.put("Alice", 25);
        sampleMap.put("Bob", 30);
        printMap(sampleMap);

        TaskScheduler scheduler = new TaskScheduler();
        scheduler.addTask(new Task("Code Review", 3, 20));
        scheduler.addTask(new Task("System Update", 5, 45));
        scheduler.addTask(new Task("Database Backup", 2, 30));
        scheduler.addTask(new Task("Deploy New Feature", 5, 50));
        scheduler.addTask(new Task("Bug Fixing", 4, 25));

        scheduler.processTasks();
    }

    public static <T> List<T> mergeLists(List<T> list1, List<T> list2) {
        List<T> mergedList = new ArrayList<>();
        int size = Math.max(list1.size(), list2.size());
        for (int i = 0; i < size; i++) {
            if (i < list1.size()) mergedList.add(list1.get(i));
            if (i < list2.size()) mergedList.add(list2.get(i));
        }
        return mergedList;
    }

    public static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

class Task implements Comparable<Task> {
    String taskName;
    int priority;
    int duration;

    public Task(String taskName, int priority, int duration) {
        this.taskName = taskName;
        this.priority = priority;
        this.duration = duration;
    }

    @Override
    public int compareTo(Task other) {
        int priorityCompare = Integer.compare(other.priority, this.priority);
        return (priorityCompare != 0) ? priorityCompare : Integer.compare(this.duration, other.duration);
    }

    @Override
    public String toString() {
        return "[Priority " + priority + "] " + taskName + " (Duration: " + duration + " mins)";
    }
}

class TaskScheduler {
    private PriorityQueue<Task> taskQueue = new PriorityQueue<>();
    private Queue<Task> pendingTasks = new LinkedList<>();

    public void addTask(Task task) {
        taskQueue.offer(task);
    }

    public void processTasks() {
        System.out.println("Tasks Added:");
        printQueue(taskQueue);

        while (!taskQueue.isEmpty() || !pendingTasks.isEmpty()) {
            if (!taskQueue.isEmpty()) {
                Task task = taskQueue.poll();
                System.out.println("Processing Task: " + task);
            } else if (!pendingTasks.isEmpty()) {
                Task task = pendingTasks.poll();
                System.out.println("Processing Pending Task: " + task);
            }
            printState();
        }
    }

    public void moveToPending(Task task) {
        System.out.println("Delaying Task: " + task.taskName);
        pendingTasks.offer(task);
    }

    private void printState() {
        System.out.println("\nScheduled Tasks (sorted by priority):");
        printQueue(taskQueue);
        System.out.println("Pending Tasks (FIFO Order):");
        printQueue(pendingTasks);
        System.out.println();
    }

    private void printQueue(Collection<Task> queue) {
        if (queue.isEmpty()) {
            System.out.println("(No tasks)");
        } else {
            int index = 1;
            for (Task task : queue) {
                System.out.println(index++ + ". " + task);
            }
        }
    }
}
