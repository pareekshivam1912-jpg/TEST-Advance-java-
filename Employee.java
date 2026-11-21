public class Employee {
    int id;
    String name;
    String department;
    double salary;

    public Employee(int id, String name, String department, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public void display() {
        System.out.println("ID: " + id + ", Name: " + name + ", Dept: " + department + ", Salary: " + salary);
    }

    public static void main(String[] args) {
        // list of 15 employees
        Employee[] employees = new Employee[15];

        employees[0] = new Employee(101, "Amit", "IT", 50000);
        employees[1] = new Employee(102, "Rahul", "HR", 40000);
        employees[2] = new Employee(103, "Priya", "IT", 70000);
        employees[3] = new Employee(104, "Neha", "Finance", 60000);
        employees[4] = new Employee(105, "Karan", "IT", 70000);
        employees[5] = new Employee(101, "Amit Duplicate", "IT", 50000);
        employees[6] = new Employee(106, "Simran", "Sales", 45000);
        employees[7] = new Employee(107, "Mohit", "Sales", 55000);
        employees[8] = new Employee(103, "Priya Copy", "IT", 70000);
        employees[9] = new Employee(108, "Anjali", "HR", 40000);
        employees[10] = new Employee(109, "Vikas", "Finance", 85000);
        employees[11] = new Employee(110, "Suresh", "IT", 90000);
        employees[12] = new Employee(102, "Rahul Duplicate", "HR", 40000);
        employees[13] = new Employee(111, "Ritu", "Sales", 60000);
        employees[14] = new Employee(112, "Deepak", "IT", 85000);

        System.out.println("All employee records:");
        for (int i = 0; i < employees.length; i++) {
            employees[i].display();
        }

        // find duplicate employees based on id
        System.out.println("\nDuplicate records:");
        for (int i = 0; i < employees.length; i++) {
            boolean isDuplicate = false;
            for (int j = 0; j < i; j++) {
                if (employees[i].id == employees[j].id) {
                    isDuplicate = true;
                    break;
                }
            }
            if (isDuplicate) {
                System.out.println("Duplicate ID " + employees[i].id + ", Name: " + employees[i].name);
            }
        }

        // remove duplicates while keeping insertion order
        System.out.println("\nUnique records:");
        Employee[] uniqueEmployees = new Employee[15];
        int uniqueCount = 0;

        for (int i = 0; i < employees.length; i++) {
            boolean alreadyAdded = false;
            for (int j = 0; j < uniqueCount; j++) {
                if (employees[i].id == uniqueEmployees[j].id) {
                    alreadyAdded = true;
                    break;
                }
            }
            if (!alreadyAdded) {
                uniqueEmployees[uniqueCount] = employees[i];
                uniqueCount++;
            }
        }

        for (int i = 0; i < uniqueCount; i++) {
            uniqueEmployees[i].display();
        }
        System.out.println("Total unique records: " + uniqueCount);

        // find second highest distinct salary
        System.out.println("\nSecond highest salary:");
        double highest = 0;
        double secondHighest = 0;

        for (int i = 0; i < uniqueCount; i++) {
            double currentSalary = uniqueEmployees[i].salary;
            if (currentSalary > highest) {
                secondHighest = highest;
                highest = currentSalary;
            } else if (currentSalary < highest && currentSalary > secondHighest) {
                secondHighest = currentSalary;
            }
        }

        System.out.println("Second highest salary is: " + secondHighest);

        // display employees having the same salary
        System.out.println("\nEmployees having same salary:");
        boolean[] alreadyChecked = new boolean[uniqueCount];

        for (int i = 0; i < uniqueCount; i++) {
            if (alreadyChecked[i]) {
                continue;
            }

            int count = 1;
            for (int j = i + 1; j < uniqueCount; j++) {
                if (uniqueEmployees[i].salary == uniqueEmployees[j].salary) {
                    count++;
                }
            }

            if (count > 1) {
                System.out.println("Salary " + uniqueEmployees[i].salary + " is shared by:");
                for (int j = i; j < uniqueCount; j++) {
                    if (uniqueEmployees[i].salary == uniqueEmployees[j].salary) {
                        System.out.println("  " + uniqueEmployees[j].name + " from " + uniqueEmployees[j].department);
                        alreadyChecked[j] = true;
                    }
                }
            }
        }
    }
}
