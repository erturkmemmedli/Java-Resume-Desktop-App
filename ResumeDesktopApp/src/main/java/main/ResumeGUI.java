package main;

import dao.inter.*;
import entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;

public class ResumeGUI extends JFrame {
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
    private JTextField skillTextField;
    private JLabel skillLabel;
    private JLabel powerLabel;
    private JSlider powerSlider;
    private JComboBox skillComboBox;
    private JButton addButton;
    private JButton deleteButton;
    private JTable skillTable;
    private JScrollPane skillScrollPane;
    private JButton updateButton;
    private JTextField headerTextField;
    private JTextField endDateTextField;
    private JTextField beginDateTextField;
    private JButton experienceDeleteButton;
    private JButton experienceSaveButton;
    private JTextArea jobDescriptionTextArea;
    private JButton experienceAddButton;
    private JTable experienceTable;
    private JScrollPane experienceScrollPane;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private UserDaoInter userDao = Context.instanceUserDao();
    private CountryDaoInter countryDao = Context.instanceCountryDao();
    private SkillDaoInter skillDao = Context.instanceSkillDao();
    private UserSkillDaoInter userSkillDao = Context.instanceUserSkillDao();
    private EmploymentHistoryDaoInter employmentHistoryDao = Context.instanceEmploymentHistoryDao();
    private UserEmploymentHistoryDaoInter userEmploymentHistoryDao = Context.instanceUserEmploymentHistoryDao();
    private User loggedInUser;
    private List<UserSkill> userSkills;
    private List<EmploymentHistory> userExperiences;

    public ResumeGUI(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();

        loggedInUser = userDao.getById(1);
        fillWindow();
        fillUserComponents();

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // User info section
                String name = nameTextField.getText();
                String surname = surnameTextField.getText();
                loggedInUser.setName(name);
                loggedInUser.setSurname(surname);

                // Profile tab section
                String profileDescription = profileTextArea.getText();
                loggedInUser.setProfileDescription(profileDescription);

                // Details tab section
                String birthday = birthdayTextField.getText();
                String email = emailTextField.getText();
                String phone = phoneTextField.getText();
                String address = addressTextField.getText();
                Country country = (Country) birthplaceComboBox.getSelectedItem();
                Country nationality = (Country) nationalityComboBox.getSelectedItem();
                try {
                    Date bd = new Date(sdf.parse(birthday).getTime());
                    loggedInUser.setBirthday(bd);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                loggedInUser.setEmail(email);
                loggedInUser.setPhone(phone);
                loggedInUser.setAddress(address);
                loggedInUser.setCountry(country);
                loggedInUser.setNationality(nationality);

                // Update user
                userDao.updateUser(loggedInUser);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Skills tab section
                int index = skillTable.getSelectedRow();
                UserSkill skill = userSkills.get(index);
                userSkillDao.deleteSkillById(skill.getId());
                fillTable();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Skills tab section
                String skillName = skillTextField.getText();
                Skill skill = null;
                if (skillName != null && !skillName.trim().isEmpty()) {
                    skill = new Skill(0, skillName);
                    skillDao.insertSkill(skill);
                    fillSkillWindow();
                } else {
                    skill = (Skill) skillComboBox.getSelectedItem();
                }
                int power = powerSlider.getValue();
                UserSkill userSkill = new UserSkill(null, loggedInUser, skill, power);
                userSkillDao.insertSkillByUser(userSkill);
                fillTable();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Skills tab section
                int index = skillTable.getSelectedRow();
                if (index >= 0) {
                    UserSkill userSkill = userSkills.get(index);
                    String skillName = skillTextField.getText();
                    Skill skill = null;
                    if (skillName != null && !skillName.trim().isEmpty()) {
                        skill = new Skill(0, skillName);
                        skillDao.insertSkill(skill);
                        fillSkillWindow();
                    } else {
                        skill = (Skill) skillComboBox.getSelectedItem();
                    }
                    int power = powerSlider.getValue();
                    userSkill.setPower(power);
                    userSkill.setSkill(skill);
                    userSkillDao.updateUserSkill(userSkill);
                    fillTable();
                }
            }
        });

        experienceAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!headerTextField.getText().trim().isEmpty()) {
                    EmploymentHistory eh = new EmploymentHistory(0,
                            headerTextField.getText(), null, null,
                            jobDescriptionTextArea.getText(), loggedInUser);
                    try {
                        Date bd = new Date(sdf.parse(beginDateTextField.getText()).getTime());
                        eh.setBeginDate(bd);
                    } catch (ParseException ex) {
                        eh.setBeginDate(null);
                    }
                    try {
                        Date bd = new Date(sdf.parse(endDateTextField.getText()).getTime());
                        eh.setEndDate(bd);
                    } catch (ParseException ex) {
                        eh.setEndDate(null);
                    }
                    employmentHistoryDao.insertEmploymentHistory(eh);
                }
                fillTable();
            }
        });

        experienceDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = experienceTable.getSelectedRow();
                EmploymentHistory eh = userExperiences.get(index);
                employmentHistoryDao.deleteEmploymentHistory(eh.getId());
                fillTable();
            }
        });
        experienceSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = experienceTable.getSelectedRow();
                if (index >= 0) {
                    EmploymentHistory eh = userExperiences.get(index);
                    if (headerTextField.getText().trim().isEmpty()) {
                        eh = new EmploymentHistory(eh.getId(),
                                headerTextField.getText(), null, null,
                                jobDescriptionTextArea.getText(), loggedInUser);
                        try {
                            Date bd = new Date(sdf.parse(beginDateTextField.getText()).getTime());
                            eh.setBeginDate(bd);
                        } catch (ParseException ex) {
                            eh.setBeginDate(null);
                        }
                        try {
                            Date bd = new Date(sdf.parse(endDateTextField.getText()).getTime());
                            eh.setEndDate(bd);
                        } catch (ParseException ex) {
                            eh.setEndDate(null);
                        }
                        employmentHistoryDao.updateEmploymentHistory(eh);
                    }
                    fillTable();
                }
            }
        });
    }

    private void fillUserComponents() {
        // User info section
        nameTextField.setText(loggedInUser.getName());
        surnameTextField.setText(loggedInUser.getSurname());

        // Profile tab section
        profileTextArea.setText(loggedInUser.getProfileDescription());

        // Details tab section
        phoneTextField.setText(loggedInUser.getPhone());
        emailTextField.setText(loggedInUser.getEmail());
        addressTextField.setText(loggedInUser.getAddress());
        Date date = loggedInUser.getBirthday();
        String dateString = sdf.format(date);
        birthdayTextField.setText(dateString);
        birthplaceComboBox.setSelectedItem(loggedInUser.getCountry());
        nationalityComboBox.setSelectedItem(loggedInUser.getNationality());
    }

    private void fillWindow() {
        // Details tab section
        List<Country> countries = countryDao.getAllCountry();
        for (Country c : countries) {
            birthplaceComboBox.addItem(c);
        }
        List<Country> nationalities = countryDao.getAllNationality();
        for (Country n : nationalities) {
            nationalityComboBox.addItem(n);
        }

        // Skills tab section
        fillSkillWindow();
    }

    private void fillSkillWindow() {
        // Skills tab section
        List<Skill> skills = skillDao.getAll();
        skillComboBox.removeAllItems();
        for (Skill s : skills) {
            skillComboBox.addItem(s);
        }
        fillTable();
    }

    private void fillTable() {
        // Skills tab section
        userSkills = userSkillDao.getAllSkillByUserId(loggedInUser.getId());
        Vector<Vector> skillRows = new Vector<>();
        for (UserSkill us: userSkills) {
            Vector row = new Vector();
            row.add(us.getSkill());
            row.add(us.getPower());
            skillRows.add(row);
        }
        Vector<String> skillColumns = new Vector<>();
        skillColumns.add("Skill");
        skillColumns.add("Power");
        skillTable.setModel(new DefaultTableModel(skillRows, skillColumns));

        // Experience tab section
        Vector<Vector> experienceRows = new Vector<>();
        userExperiences = userEmploymentHistoryDao.getAllEmploymentHistoryByUserId(loggedInUser.getId());
        for (EmploymentHistory eh: userExperiences) {
            Vector row = new Vector();
            row.add(eh.getHeader());
            row.add(eh.getJobDescription());
            try {
                Date dt = eh.getBeginDate();
                row.add(sdf.format(dt));
            } catch (Exception ex) {
                row.add(null);
            }
            try {
                Date dt = eh.getEndDate();
                row.add(sdf.format(dt));
            } catch (Exception ex) {
                row.add(null);
            }
            experienceRows.add(row);
        }
        Vector experienceColumns = new Vector();
        experienceColumns.add("Header");
        experienceColumns.add("Job Description");
        experienceColumns.add("Begin Date");
        experienceColumns.add("End Date");
        experienceTable.setModel(new DefaultTableModel(experienceRows, experienceColumns));
    }

    public static void main(String[] args) {
        JFrame frame = new ResumeGUI("Resume GUI");
        frame.setVisible(true);
   }
}
