import java.util.*;
import java.util.stream.*;

class Student {
    private int id;
    private String name;
    private List<String> courses;
    private Map<String, Integer> scores;

    public Student(int id, String name, List<String> courses, Map<String, Integer> scores) {
        this.id = id;
        this.name = name;
        this.courses = courses;
        this.scores = scores;
    }

    public double getAverageScore() {
        return scores.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    public List<String> getCourses() {
        return courses;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name +
                "', avg=" + String.format("%.2f", getAverageScore()) + "}";
    }
}

public class StudentPerformanceAnalyzer {

    // Sort by average score descending, return top N
    public static List<Student> getTopNStudents(List<Student> students, int n) {
        if (students == null || students.isEmpty() || n <= 0)
            return new ArrayList<>();

        return students.stream()
                .sorted(Comparator.comparingDouble(Student::getAverageScore).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    // Average score per course using Streams
    public static Map<String, Double> getAverageScorePerCourse(List<Student> students) {
        if (students == null || students.isEmpty())
            return new HashMap<>();

        Map<String, List<Integer>> scoresPerCourse = students.stream()
                .flatMap(student -> student.getCourses().stream()
                        .map(course -> new AbstractMap.SimpleEntry<>(
                                course, student.getScores().getOrDefault(course, 0))))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        return scoresPerCourse.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .mapToInt(Integer::intValue)
                                .average()
                                .orElse(0.0)));
    }

    // All unique courses using HashSet
    public static Set<String> getAllUniqueCourses(List<Student> students) {
        if (students == null || students.isEmpty())
            return new HashSet<>();

        return students.stream()
                .flatMap(student -> student.getCourses().stream())
                .collect(Collectors.toCollection(HashSet::new));
    }

    private static List<Student> createSampleStudents() {
        List<Student> students = new ArrayList<>();

        students.add(new Student(1, "Alice",
                Arrays.asList("Math", "Physics", "Chemistry"),
                new HashMap<>(Map.of("Math", 85, "Physics", 90, "Chemistry", 78))));

        students.add(new Student(2, "Bob",
                Arrays.asList("Math", "Physics", "Biology"),
                new HashMap<>(Map.of("Math", 92, "Physics", 88, "Biology", 95))));

        students.add(new Student(3, "Charlie",
                Arrays.asList("Math", "Chemistry", "Biology"),
                new HashMap<>(Map.of("Math", 78, "Chemistry", 82, "Biology", 79))));

        students.add(new Student(4, "Diana",
                Arrays.asList("Physics", "Chemistry", "Biology"),
                new HashMap<>(Map.of("Physics", 85, "Chemistry", 88, "Biology", 84))));

        return students;
    }

    public static void main(String[] args) {
        List<Student> students = createSampleStudents();

        System.out.println("Top 3 Students:");
        getTopNStudents(students, 3).forEach(System.out::println);

        System.out.println("\nAverage Scores per Course:");
        getAverageScorePerCourse(students)
                .forEach((course, avg) -> System.out.printf("%s: %.2f%n", course, avg));

        System.out.println("\nAll Unique Courses:");
        getAllUniqueCourses(students).forEach(System.out::println);
    }
}