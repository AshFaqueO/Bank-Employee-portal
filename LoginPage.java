import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel dateTimeLabel;
    private JLabel titleLabel;

    private static final String FILE_PATH = "login_data.txt"; 
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    public LoginPage() {
        // Frame settings
        setTitle("Bank of Imagination - Login");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Panel for the login form
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(new Color(34, 49, 63));
        loginPanel.setBounds(0, 0, 900, 600);
        add(loginPanel);

        // Title label
        titleLabel = new JLabel("Bank of Imagination");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 36));
        titleLabel.setForeground(new Color(255, 182, 193));
        titleLabel.setBounds(150, 30, 600, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginPanel.add(titleLabel);

        // Date and Time Label
        dateTimeLabel = new JLabel();
        dateTimeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dateTimeLabel.setForeground(new Color(255, 182, 193));
        dateTimeLabel.setBounds(130, 90, 600, 40);
        dateTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginPanel.add(dateTimeLabel);
        updateTime();

        // Bank of Imagination
        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon("B:\\Bank Employee Portal\\resources\\logo.gif"); // Update with your logo's file name and path
        logoLabel.setIcon(logoIcon);
        logoLabel.setBounds(650, 50, 200, 180); // Adjust size and position as needed
        loginPanel.add(logoLabel);

        

        // Username Label and Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        usernameLabel.setForeground(new Color(255, 182, 193));
        usernameLabel.setBounds(150, 230, 120, 30);
        loginPanel.add(usernameLabel);

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 20));
        usernameField.setBounds(300, 230, 300, 40);
        usernameField.setBackground(new Color(236, 240, 241));
        usernameField.setForeground(Color.BLACK);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 182, 193), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        loginPanel.add(usernameField);

        // Password Label and Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        passwordLabel.setForeground(new Color(255, 182, 193));
        passwordLabel.setBounds(150, 290, 120, 30);
        loginPanel.add(passwordLabel);

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordField.setBounds(300, 290, 300, 40);
        passwordField.setBackground(new Color(236, 240, 241));
        passwordField.setForeground(Color.BLACK);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 182, 193), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        loginPanel.add(passwordField);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 20));
        loginButton.setBackground(new Color(46, 204, 113));
        loginButton.setForeground(Color.WHITE);
        loginButton.setOpaque(true);
        loginButton.setBounds(350, 370, 200, 50);
        loginButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        loginPanel.add(loginButton);

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 20));
        registerButton.setBackground(new Color(52, 152, 219));
        registerButton.setForeground(Color.WHITE);
        registerButton.setOpaque(true);
        registerButton.setBounds(350, 440, 200, 50);
        registerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        loginPanel.add(registerButton);

        // Login button's action listener
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                    JOptionPane.showMessageDialog(null, "Admin login successful!");
                    Dashboard dashboard = new Dashboard(true); //for admin
                    dashboard.setVisible(true);
                    dispose();
                } else {
                    boolean loginSuccessful = checkLoginFromFile(username, password);

                    if (loginSuccessful) {
                        JOptionPane.showMessageDialog(null, "Login successful! Welcome " + username + "!");
                        Dashboard dashboard = new Dashboard(false); // for employee
                        dashboard.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                        usernameField.setText("");
                        passwordField.setText("");
                    }
                }
            }
        });

        // Register button's action listener
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                saveCredentialsToFile(username, password);
                JOptionPane.showMessageDialog(null, "Registered successfully!");
                usernameField.setText("");
                passwordField.setText("");
            }
        });

        // Timer to update time every second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        });
        timer.start();
    }

    // Update date and time display
    private void updateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a, EEEE, dd-MMM-yyyy");
        Date currentDate = new Date();
        dateTimeLabel.setText(formatter.format(currentDate));
    }

    // Checking username and password from the file
    private static boolean checkLoginFromFile(String username, String password) {
        try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] credentials = line.split(",");
                if (credentials.length == 2) {
                    String storedUsername = credentials[0].trim();
                    String storedPassword = credentials[1].trim();
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }
        return false;
    }

    // Saving new usernames and passwords to the file
    private static void saveCredentialsToFile(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error: Could not write to file.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginPage loginPage = new LoginPage();
                loginPage.setVisible(true);
            }
        });
    }
}
