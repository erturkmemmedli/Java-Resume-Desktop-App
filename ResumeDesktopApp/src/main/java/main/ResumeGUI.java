package main;

import dao.inter.UserDaoInter;
import entitiy.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ResumeGUI extends JFrame {
    private UserDaoInter userDao = Context.instanceUserDao();
    User loggedInUser;
    private JPanel mainPanel;
    private JTextField nameTextField;
    private JTextField surnameTextField;
    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JPanel userInfoPanel;
    private JTabbedPane userInfoTabbedPane;
    private JPanel profilePanel;
    private JPanel detailsPanel;
    private JPanel skillsPanel;
    private JPanel experiencePanel;
    private JTextArea profileTextArea;
    private JButton saveButton;
    private JTextField birthdayTextField;
    private JTextField addressTextField;
    private JTextField emailTextField;
    private JTextField phoneTextField;
    private JLabel addressLabel;
    private JLabel phoneLabel;
    private JLabel emailPanel;
    private JLabel birthdayLabel;
    private JLabel birthplaceLabel;
    private JLabel nationalityLabel;
    private JComboBox birthplaceComboBox;
    private JComboBox nationalityComboBox;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public ResumeGUI(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        loggedInUser = userDao.getById(1);
        fillUserComponents();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                String surname = surnameTextField.getText();
                String profileDescription = profileTextArea.getText();
                String birthday = birthdayTextField.getText();
                String email = emailTextField.getText();
                String phone = phoneTextField.getText();
                String address = addressTextField.getText();
                loggedInUser.setName(name);
                loggedInUser.setSurname(surname);
                loggedInUser.setProfileDescription(profileDescription);
                try {
                    Date bd = new Date(sdf.parse(birthday).getTime());
                    loggedInUser.setBirthday(bd);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                loggedInUser.setEmail(email);
                loggedInUser.setPhone(phone);
                loggedInUser.setAddress(address);
                userDao.updateUser(loggedInUser);
            }
        });
    }

    private void fillUserComponents() {
        nameTextField.setText(loggedInUser.getName());
        surnameTextField.setText(loggedInUser.getSurname());
        profileTextArea.setText(loggedInUser.getProfileDescription());
        phoneTextField.setText(loggedInUser.getPhone());
        emailTextField.setText(loggedInUser.getEmail());
        addressTextField.setText(loggedInUser.getAddress());
        Date date = loggedInUser.getBirthday();
        String dateString = sdf.format(date);
        birthdayTextField.setText(dateString);
    }

    public static void main(String[] args) {
        JFrame frame = new ResumeGUI("Resume GUI");
        frame.setVisible(true);
   }
}
