import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

// Employee class
class Employee {
    private int id;
    private String name;
    private double basicSalary;
    private String department;

    // Default constructor
    Employee() {
        id = 0;
        name = "";
        basicSalary = 0;
        department = "";
    }

    // Parameterized constructor
    Employee(int id, String name, double basicSalary,
             String department) {
        this.id = id;
        this.name = name;
        this.basicSalary = basicSalary;
        this.department = department;
    }

    // Copy constructor
    Employee(Employee e) {
        this.id = e.id;
        this.name = e.name;
        this.basicSalary = e.basicSalary;
        this.department = e.department;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public String getDepartment() {
        return department;
    }

    // Allowance = 10%
    public double getAllowance() {
        return basicSalary * 0.10;
    }

    // Deduction = 5%
    public double getDeduction() {
        return basicSalary * 0.05;
    }

    // Calculate net salary
    public double getNetSalary() {
        return basicSalary + getAllowance()- getDeduction();
    }

    public String toString() {
        return "Employee ID: " + id
                + "\nName: " + name
                + "\nDepartment: " + department
                + "\nBasic Salary: " + basicSalary
                + "\nAllowance: " + getAllowance()
                + "\nDeduction: " + getDeduction()
                + "\nNet Salary: " + getNetSalary()
                + "\n-----------------------------";
    }
}

// Main class
public class EmployeePayrollSystem_new extends JFrame {

    JTextField idField, nameField, salaryField;
    JComboBox<String> departmentBox;
    JTextArea outputArea;

    ArrayList<Employee> employees =new ArrayList<Employee>();

    EmployeePayrollSystem_new() {

        setTitle("Employee Payroll System");
        setSize(800, 700);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Input panel
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBounds(40, 20, 700, 180);

        idField = new JTextField();
        nameField = new JTextField();
        salaryField = new JTextField();

        departmentBox = new JComboBox<String>(new String[]{"IT", "HR", "Sales", "Finance"});

        panel.add(new JLabel("Employee ID:"));
        panel.add(idField);

        panel.add(new JLabel("Employee Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Basic Salary:"));
        panel.add(salaryField);

        panel.add(new JLabel("Department:"));
        panel.add(departmentBox);

        add(panel);

        // Buttons
        JButton addButton = new JButton("Add Employee");
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");
        JButton viewButton = new JButton("View All");

        addButton.setBounds(40, 220, 160, 35);
        saveButton.setBounds(220, 220, 100, 35);
        loadButton.setBounds(340, 220, 100, 35);
        viewButton.setBounds(460, 220, 120, 35);

        add(addButton);
        add(saveButton);
        add(loadButton);
        add(viewButton);

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBounds(40, 280, 700, 340);

        add(scrollPane);

        // Add Employee button
        addButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText().trim();
                double salary = Double.parseDouble(salaryField.getText());
                String department = (String) departmentBox.getSelectedItem();

                if (id <= 0 || name.isEmpty() || salary <= 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Enter valid employee details!"
                    );
                    return;
                }

                // Check duplicate ID
                for (Employee emp : employees) {
                    if (emp.getId() == id) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Employee ID already exists!"
                        );
                        return;
                    }
                }

                Employee emp = new Employee(id, name, salary, department);

                employees.add(emp);

                JOptionPane.showMessageDialog(
                        this,
                        "Employee Added Successfully!\nNet Salary: "
                                + emp.getNetSalary()
                );

                idField.setText("");
                nameField.setText("");
                salaryField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Enter valid ID and salary!"
                );
            }
        });

        // Save button
        saveButton.addActionListener(e -> {
            try (BufferedWriter bw = new BufferedWriter(
                    new FileWriter("employees.txt"))) {

                for (Employee emp : employees) {
                    bw.write(emp.getId() + ","
                            + emp.getName() + ","
                            + emp.getBasicSalary() + ","
                            + emp.getDepartment());
                    bw.newLine();
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Records Saved Successfully!"
                );

            } catch (IOException ex)
			{
                JOptionPane.showMessageDialog(this,"Error saving records!");
            }
        });

        // Load button
        loadButton.addActionListener(e -> {
            employees.clear();

            try (BufferedReader br = new BufferedReader(
                    new FileReader("employees.txt"))) {

                String line;

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",", -1);

                    if (data.length != 4) {
                        continue;
                    }

                    Employee emp = new Employee(
                            Integer.parseInt(data[0]),
                            data[1],
                            Double.parseDouble(data[2]),
                            data[3]
                    );

                    employees.add(emp);
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Records Loaded Successfully!"
                );

            } 
			catch (FileNotFoundException ex)
			{
                JOptionPane.showMessageDialog( this,"File not found. Save records first!");
            } 
			catch (IOException | NumberFormatException ex) 
			{
                JOptionPane.showMessageDialog(this,"Error reading records!");
            }
        });

        // View All button
        viewButton.addActionListener(e -> {
            outputArea.setText("");

            for (Employee emp : employees) {
                outputArea.append(emp.toString() + "\n");
            }

            if (employees.isEmpty()) {
                outputArea.setText("No employee records found.");
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new EmployeePayrollSystem_new();
    }
}
