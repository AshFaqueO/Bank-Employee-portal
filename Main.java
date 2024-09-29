import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // login page
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginPage loginPage = new LoginPage();
                loginPage.setVisible(true);
            }
        });
    }
}
