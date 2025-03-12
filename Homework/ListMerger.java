package Homework;
import java.util.*;

class ListMerger {
    public static <T> List<T> mergeLists(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<>();
        int size1 = list1.size(), size2 = list2.size();
        int minSize = Math.min(size1, size2);
        for (int i = 0; i < minSize; i++) {
            result.add(list1.get(i));
            result.add(list2.get(i));
        }
        result.addAll(list1.subList(minSize, size1));
        result.addAll(list2.subList(minSize, size2));
        return result;
    }
}


class MapPrinter {
    public static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

class Task implements Comparable<TaskScheduler> {
    private String taskName;
    private int priority;
    private int duration;

    public Task(String taskName, int priority, int duration) {
        this.taskName = taskName;
        this.priority = priority;
        this.duration = duration;
    }

    public String getTaskName() { return taskName; }
    public int getPriority() { return priority; }
    public int getDuration() { return duration; }

    @Override
    public int compareTo(TaskScheduler other) {
        if (this.priority != other.priority) {
            return Integer.compare(other.priority, this.priority);
        }
        return Integer.compare(this.duration, other.duration);
    }

    @Override
    public String toString() {
        return "[Priority " + priority + "] " + taskName + " (Duration: " + duration + " mins)";
    }
}

class TaskScheduler {
    private PriorityQueue<TaskScheduler> priorityTasks = new PriorityQueue<>();
    private Queue<TaskScheduler> pendingTasks = new LinkedList<>();

    public void addTask(TaskScheduler task) {
        priorityTasks.offer(task);
        System.out.println("Task added: " + task);
    }

    public void processNextTask() {
        if (!priorityTasks.isEmpty()) {
            System.out.println("Processing Task: " + priorityTasks.poll());
        } else if (!pendingTasks.isEmpty()) {
            System.out.println("Processing Pending Task: " + pendingTasks.poll());
        } else {
            System.out.println("No tasks left.");
        }
    }

    public void delayTask(String taskName) {
        Iterator<TaskScheduler> iterator = priorityTasks.iterator();
        while (iterator.hasNext()) {
            TaskScheduler task = iterator.next();
            if (task.getTaskName().equals(taskName)) {
                iterator.remove();
                pendingTasks.offer(task);
                System.out.println("Delaying Task: " + taskName);
                return;
            }
        }
        System.out.println("Task not found: " + taskName);
    }

    public void printTasks() {
        System.out.println("Scheduled Tasks:");
        if (priorityTasks.isEmpty()) {
            System.out.println("(No priority tasks)");
        } else {
            int i = 1;
            for (TaskScheduler task : priorityTasks) {
                System.out.println(i++ + ". " + task);
            }
        }
        System.out.println("Pending Tasks (FIFO Order):");
        if (pendingTasks.isEmpty()) {
            System.out.println("(No pending tasks)");
        } else {
            int i = 1;
            for (TaskScheduler task : pendingTasks) {
                System.out.println(i++ + ". " + task);
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {

        List<Integer> list1 = Arrays.asList(1, 3, 5);
        List<Integer> list2 = Arrays.asList(2, 4, 6, 8, 10);
        System.out.println("Merged List: " + ListMerger.mergeLists(list1, list2));


        Map<String, Integer> map = new HashMap<>();
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);
        MapPrinter.printMap(map);
        TaskScheduler scheduler = new TaskScheduler();
        scheduler.addTask(new TaskScheduler("Code Review", 3, 20));
        scheduler.addTask(new TaskScheduler("System Update", 5, 45));
        scheduler.addTask(new TaskScheduler("Database Backup", 2, 30));
        scheduler.addTask(new TaskScheduler("Deploy New Feature", 5, 50));
        scheduler.addTask(new TaskScheduler("Bug Fixing", 4, 25));

        scheduler.printTasks();

        scheduler.processNextTask();
        scheduler.delayTask("Code Review");
        scheduler.printTasks();

        scheduler.delayTask("Database Backup");
        scheduler.printTasks();

        scheduler.processNextTask();
        scheduler.processNextTask();
        scheduler.processNextTask();
        scheduler.processNextTask();
        scheduler.processNextTask();
    }
}
