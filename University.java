import java.util.ArrayList;
import java.util.Scanner;

public class University {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();
        ArrayList<Course> courses = new ArrayList<>();
        String userInput;

        while (true) {
            System.out.println("\nUniversity Management System");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Add Course");
            System.out.println("4. Register Student to Course");
            System.out.println("5. View All Courses");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            userInput = scanner.nextLine();

            switch (userInput) {
                case "1":
                    // Add Student
                    System.out.print("Enter first name: ");
                    String firstName = scanner.nextLine();

                    System.out.print("Enter last name: ");
                    String lastName = scanner.nextLine();

                    System.out.print("Enter student ID: ");
                    String studentID = scanner.nextLine();

                    System.out.print("Enter major: ");
                    String major = scanner.nextLine();

                    System.out.print("Enter year: ");
                    String year = scanner.nextLine();

                    Student newStudent = new Student();
                    newStudent.addStudent(firstName, lastName, studentID, major, year);
                    students.add(newStudent);

                    System.out.println("Student added successfully!");
                    break;

                case "2":
                    // View All Students
                    if (students.isEmpty()) {
                        System.out.println("No students found.");
                    } else {
                        System.out.println("List of Students:");
                        for (int i = 0; i < students.size(); i++) {
                            Student student = students.get(i);
                            System.out.println((i + 1) + ". " + student.getFirstName() + " " + student.getLastName() +
                                    " - ID: " + student.getStudentID() +
                                    " - Major: " + student.getMajor() +
                                    " - Year: " + student.getYear());
                        }
                    }
                    break;

                case "3":
                    // Add Course
                    System.out.print("Enter course name: ");
                    String courseName = scanner.nextLine();

                    System.out.print("Enter professor name: ");
                    String professorName = scanner.nextLine();

                    System.out.print("Enter course ID: ");
                    String courseID = scanner.nextLine();

                    System.out.print("Enter course credit: ");
                    int credit = Integer.parseInt(scanner.nextLine());

                    Course newCourse = new Course();
                    newCourse.addCourse(courseName, professorName, courseID, credit);
                    courses.add(newCourse);

                    System.out.println("Course added successfully!");
                    break;

                case "4":
                    // Register Student to Course
                    if (students.isEmpty() || courses.isEmpty()) {
                        System.out.println("You must add both students and courses first.");
                    } else {
                        System.out.println("List of Students:");
                        for (int i = 0; i < students.size(); i++) {
                            System.out.println((i + 1) + ". " + students.get(i).getFirstName() + " " + students.get(i).getLastName());
                        }

                        System.out.print("Enter student number to register: ");
                        int studentIndex = Integer.parseInt(scanner.nextLine()) - 1;

                        if (studentIndex < 0 || studentIndex >= students.size()) {
                            System.out.println("Invalid student selection.");
                            break;
                        }

                        Student selectedStudent = students.get(studentIndex);

                        System.out.println("List of Courses:");
                        for (int i = 0; i < courses.size(); i++) {
                            System.out.println((i + 1) + ". " + courses.get(i).getCourseName() + " - Credits: " + courses.get(i).getCredit());
                        }

                        System.out.print("Enter course number to register: ");
                        int courseIndex = Integer.parseInt(scanner.nextLine()) - 1;

                        if (courseIndex < 0 || courseIndex >= courses.size()) {
                            System.out.println("Invalid course selection.");
                            break;
                        }

                        Course selectedCourse = courses.get(courseIndex);

                        try {
                            selectedStudent.addCourse(selectedCourse);
                            System.out.println("Student registered to the course successfully!");
                        } catch (Exception e) {
                            System.out.println("Could not register student: " + e.getMessage());
                        }
                    }
                    break;

                case "5":
                    // View All Courses
                    if (courses.isEmpty()) {
                        System.out.println("No courses found.");
                    } else {
                        System.out.println("List of Courses:");
                        for (int i = 0; i < courses.size(); i++) {
                            Course course = courses.get(i);
                            System.out.println((i + 1) + ". " + course.getCourseName() +
                                    " - Professor: " + course.getProfessorName() +
                                    " - ID: " + course.getCourseID() +
                                    " - Credits: " + course.getCredit());
                        }
                    }
                    break;

                case "6":
                    // Exit
                    System.out.println("Exiting the program. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
