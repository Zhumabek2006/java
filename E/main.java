package E;

import java.util.ArrayList;

interface Payable {
    double getPaymentAmount();
}

abstract class Employee implements Payable {
    protected String name;
    protected int id;
    protected double baseSalary;

    public Employee(String name, int id, double baseSalary) {
        this.name = name;
        this.id = id;
        this.baseSalary = baseSalary;
    }

    public abstract double calculateSalary();

    public void displayEmployeeInfo() {
        System.out.println("Employee ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Salary: $" + calculateSalary());
    }
}

class FullTimeEmployee extends Employee {
    public FullTimeEmployee(String name, int id, double baseSalary) {
        super(name, id, baseSalary);
    }

    @Override
    public double calculateSalary() {
        return baseSalary * 1.2; // 20% extra for benefits
    }

    @Override
    public double getPaymentAmount() {
        return calculateSalary();
    }
}

class ContractEmployee extends Employee {
    private double hourlyRate;
    private int hoursWorked;

    public ContractEmployee(String name, int id, double hourlyRate, int hoursWorked) {
        super(name, id, 0);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    @Override
    public double calculateSalary() {
        return hourlyRate * hoursWorked;
    }

    @Override
    public double getPaymentAmount() {
        return calculateSalary();
    }
}
public class main {
    public static void main(String[] args) {
        ArrayList<Employee> employees = new ArrayList<Employee>();

        employees.add(new FullTimeEmployee("Alice", 101, 5000)) ;
        employees.add(new ContractEmployee("Bob", 102, 50, 160));
        for (Employee employee : employees) {
            employee.displayEmployeeInfo();
            System.out.println();
        }
    }
}

