//  R E G U M A T E  // 

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;
import java.awt.Shape;
import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.text.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.TimeZone;
import javax.swing.border.LineBorder;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.AbstractBorder;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Vector;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;

public class input extends JFrame {

    public JTextField meetingIdField;
    public JFormattedTextField meetTimeField;
    public JTextField totalMeetTimeField;
    public JPasswordField passcodeField;
    Connection con;
    public JCheckBox showHideCheckBox;

    // Variables for roundness of panel edges
    public int topLeft = 20;
    public int topRight = 20;
    public int bottomLeft = 20;
    public int bottomRight = 20;

    public input() {
        setTitle("Regumate - Zoom Meeting Automation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(0, 33, 43));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("REGUMATE");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 34));
        titleLabel.setForeground(Color.CYAN);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        meetingIdField = new JTextField(16);
        totalMeetTimeField = new JTextField(16);
        totalMeetTimeField.setText("In minutes");
        totalMeetTimeField.setForeground(Color.GRAY);
        totalMeetTimeField.setFont(new Font("Arial", Font.ITALIC, 12));

        meetingIdField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFont(meetingIdField);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFont(meetingIdField);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFont(meetingIdField);
            }

            private void updateFont(JTextField textField) {
                Font defaultFont = new Font("Arial", Font.BOLD, 12);
                String text = textField.getText();
                if (text.isEmpty()) {
                    textField.setFont(defaultFont);
                }
            }
        });

        totalMeetTimeField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (totalMeetTimeField.getText().equals("In minutes")) {
                    totalMeetTimeField.setText("");
                    totalMeetTimeField.setForeground(Color.BLACK);
                    totalMeetTimeField.setFont(new Font("Arial", Font.PLAIN, 12));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (totalMeetTimeField.getText().isEmpty()) {
                    totalMeetTimeField.setText("In minutes");
                    totalMeetTimeField.setForeground(Color.GRAY);
                    totalMeetTimeField.setFont(new Font("Arial", Font.ITALIC, 12));
                }
            }
        });

        passcodeField = new JPasswordField(16);
        passcodeField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (passcodeField.getPassword().length == 0) {
                    passcodeField.setEchoChar((char) 0);
                    passcodeField.setForeground(Color.BLACK);
                    passcodeField.setFont(new Font("Arial", Font.PLAIN, 12));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passcodeField.getPassword().length == 0) {
                    passcodeField.setEchoChar((char) 0);
                    passcodeField.setForeground(Color.GRAY);
                    passcodeField.setFont(new Font("Arial", Font.PLAIN, 12));
                }
            }
        });

        try {
            MaskFormatter time = new MaskFormatter("##:##");
            time.setPlaceholderCharacter('_');
            meetTimeField = new JFormattedTextField(time);
            meetTimeField.setColumns(16);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        JLabel meetingIdLabel = new JLabel("Meeting ID:");
        JLabel passcodeLabel = new JLabel("Passcode:");
        JLabel meetTimeLabel = new JLabel("Meeting Time (24hr format):");
        JLabel totalMeetTimeLabel = new JLabel("Total Meeting Duration:");

        Font labelFont = new Font("Times New Roman", Font.BOLD, 16);
        meetingIdLabel.setFont(labelFont);
        passcodeLabel.setFont(labelFont);
        meetTimeLabel.setFont(labelFont);
        totalMeetTimeLabel.setFont(labelFont);
        meetingIdLabel.setForeground(Color.WHITE);
        passcodeLabel.setForeground(Color.WHITE);
        meetTimeLabel.setForeground(Color.WHITE);
        totalMeetTimeLabel.setForeground(Color.WHITE);

        JButton submitButton = new JButton("Submit");
        JButton resetButton = new JButton("Reset");

        submitButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        resetButton.setFont(new Font("Times New Roman", Font.BOLD, 16));

        submitButton.addActionListener(e -> saveMeetingData());
        resetButton.addActionListener(e -> {
            meetingIdField.setText("");
            meetTimeField.setText("");
            totalMeetTimeField.setText("In minutes");
            totalMeetTimeField.setForeground(Color.GRAY);
            passcodeField.setText("");
        });

        showHideCheckBox = new JCheckBox("Show");
        showHideCheckBox.setForeground(Color.WHITE);
        showHideCheckBox.setOpaque(false);
        showHideCheckBox.setFocusPainted(false);
        showHideCheckBox.addActionListener(e -> {
            if (showHideCheckBox.isSelected()) {
                passcodeField.setEchoChar((char) 0);
            } else {
                passcodeField.setEchoChar('\u2022');
            }
        });

        JPanel inputCard = new PanelRound();
        inputCard.setBackground(new Color(0, 90, 111));
        inputCard.setOpaque(true);
        inputCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        inputCard.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputCard.add(meetingIdLabel, gbc);

        gbc.gridx = 1;
        inputCard.add(meetingIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputCard.add(passcodeLabel, gbc);

        JPanel passPanel = new JPanel(new BorderLayout(8, 0));
        passPanel.setOpaque(false);
        passPanel.add(passcodeField, BorderLayout.CENTER);
        passPanel.add(showHideCheckBox, BorderLayout.EAST);
        gbc.gridx = 1;
        inputCard.add(passPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputCard.add(meetTimeLabel, gbc);

        gbc.gridx = 1;
        inputCard.add(meetTimeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputCard.add(totalMeetTimeLabel, gbc);

        gbc.gridx = 1;
        inputCard.add(totalMeetTimeField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(resetButton);
        buttonPanel.add(submitButton);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        inputCard.add(buttonPanel, gbc);

        JLabel infoLabel = new JLabel("<html><body style='width:350px; color:#D3D3D3;'>Schedule Zoom meetings in the database and Regumate will automatically join at the right time. Use 24-hour time format like 13:30.</body></html>");
        infoLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(0, 33, 43));
        infoPanel.add(infoLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints c2 = new GridBagConstraints();
        c2.insets = new Insets(10, 10, 10, 10);
        c2.fill = GridBagConstraints.BOTH;
        c2.weightx = 0.6;
        c2.weighty = 1.0;
        centerPanel.add(inputCard, c2);

        c2.gridx = 1;
        c2.weightx = 0.4;
        centerPanel.add(infoPanel, c2);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
        pack();
        setSize(950, 560);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // DATABASE CONNECTION , LOADING OF DATA IN DATABASE AND RESTRICTIONS
    public void saveMeetingData() {
        String meetingId = meetingIdField.getText();
        String meetTime = meetTimeField.getText();
        String totalMeetTime = totalMeetTimeField.getText();
        String passcode = new String(passcodeField.getPassword());

        // Check if any of the fields are empty
        if (meetingId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Meeting ID is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (meetTime.isEmpty() || meetTime.equals("__:__")) {
            JOptionPane.showMessageDialog(this, "Meeting time is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (totalMeetTime.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Total Meeting Time is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (passcode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Passcode is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Parse meetTime to hours and minutes
        String[] meetTimeParts = meetTime.split(":");
        int meetTimeHours = Integer.parseInt(meetTimeParts[0]);
        int meetTimeMinutes = Integer.parseInt(meetTimeParts[1]);

        // Calculate total minutes of meetTime
        int totalMeetTimeMinutes = meetTimeHours * 60 + meetTimeMinutes;

        // Check if the meeting time exceeds 24 hours
        if (totalMeetTimeMinutes > 24 * 60) { // Convert 24 hours to minutes
            JOptionPane.showMessageDialog(this, "Meeting time cannot exceed 24 hours.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (meetTimeHours >= 24) {
            JOptionPane.showMessageDialog(this, "Please Enter valid Hour.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (meetTimeMinutes >= 60) {
            JOptionPane.showMessageDialog(this, "Please Enter valid minutes.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Convert totalMeetTime to an integer using type casting
        int totalMeetTimeInt = 0;
        try {
            totalMeetTimeInt = Integer.parseInt(totalMeetTime);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid total meeting time. Please enter a valid integer.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Database connection
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/regumate", "reguuser", "regupass");
            System.out.println("DATABASE CONNECTED SUCCESSFULLY...");

            String sql = "INSERT INTO meetings (meeting_id, meeting_time, total_meeting, passcode) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                preparedStatement.setString(1, meetingId);
                preparedStatement.setString(2, meetTime);
                preparedStatement.setInt(3, totalMeetTimeInt);
                preparedStatement.setString(4, passcode);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Meeting data saved successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save meeting data.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error or SQL error.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // creation of new JFrame where the table resides
    public JTable table;

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Meeting Scheduler");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel rootPanel = new JPanel(new BorderLayout(10, 10));
        rootPanel.setBackground(new Color(0, 33, 43));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setOpaque(false);

        ImageIcon homeIcon = new ImageIcon("Backend_Files/img/icon.jpg");
        JButton homeButton = new JButton(homeIcon);
        homeButton.setBorderPainted(false);
        homeButton.setContentAreaFilled(false);
        homeButton.setFocusPainted(false);
        homeButton.addActionListener(e -> {
            frame.dispose();
            for (Frame f : Frame.getFrames()) {
                if (f instanceof input) {
                    f.setVisible(true);
                    break;
                }
            }
        });

        JLabel hyperlinkLabel = new JLabel("Home");
        hyperlinkLabel.setForeground(Color.WHITE);
        hyperlinkLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        hyperlinkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        hyperlinkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                for (Frame f : Frame.getFrames()) {
                    if (f instanceof input) {
                        f.setVisible(true);
                        break;
                    }
                }
            }
        });

        topPanel.add(homeButton);
        topPanel.add(hyperlinkLabel);
        rootPanel.add(topPanel, BorderLayout.NORTH);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.add(createScheduledMeetingsPanel(), BorderLayout.CENTER);
        rootPanel.add(contentWrapper, BorderLayout.CENTER);

        frame.add(rootPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JPanel createScheduledMeetingsPanel() {
        PanelRound panel2 = new PanelRound() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                int width = getWidth();
                int height = getHeight();
                Graphics2D graphics = (Graphics2D) g.create();
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setColor(getBackground());
                graphics.fillRoundRect(0, 0, width - 1, height - 1, 10, 10);
                graphics.setColor(Color.CYAN);
                graphics.setStroke(new BasicStroke(2));
                RoundRectangle2D rect = new RoundRectangle2D.Double(1, 1, width - 3, height - 3, 10, 10);
                graphics.draw(rect);
                graphics.dispose();
            }
        };
        ;
        panel2.setLayout(null);
        panel2.setBackground(new Color(0, 90, 111, 255));
        panel2.setBounds(500, 200, 900, 650);

        // Create a table to display the scheduled meetings
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Serial no.");
        columnNames.add("Meeting ID");
        columnNames.add("Meeting Time");
        columnNames.add("Total Meeting Duration");
        columnNames.add("Passcode");

        Vector<Vector<Object>> data = new Vector<>();
        refreshData(data);

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 880, 605);
        panel2.add(scrollPane);

        // set the column size
        TableColumnModel columnModel = table.getColumnModel();
        TableColumn serialColumn = columnModel.getColumn(0);
        serialColumn.setMinWidth(65);
        serialColumn.setMaxWidth(65);

        TableColumn meetingIDColumn = columnModel.getColumn(1);
        meetingIDColumn.setMinWidth(220);
        meetingIDColumn.setMaxWidth(220);

        TableColumn meetingTimeColumn = columnModel.getColumn(2);
        meetingTimeColumn.setMinWidth(190);
        meetingTimeColumn.setMaxWidth(190);

        TableColumn TotalmeetingColumn = columnModel.getColumn(3);
        TotalmeetingColumn.setMinWidth(190);
        TotalmeetingColumn.setMaxWidth(190);

        TableColumn passcodeColumn = columnModel.getColumn(4);
        passcodeColumn.setMinWidth(210);
        passcodeColumn.setMaxWidth(210);

        // Set the table's column fixed
        table.getTableHeader().setReorderingAllowed(false);

        // Create a refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData(data);
                tableModel.fireTableDataChanged();
            }
        });
        refreshButton.setBounds(790, 618, 100, 25);
        refreshButton.setFont(new Font("Times new Roman", Font.BOLD, 14));
        refreshButton.setForeground(Color.BLACK);
        Color transparentColor = new Color(255, 255, 255, 100);
        refreshButton.setBackground(transparentColor);
        panel2.add(refreshButton);

        // Create a delete button
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {

                    String meetingTimeToDelete = (String) table.getValueAt(selectedRow, 2);
                    // Assuming meeting time is in the second column
                    try {
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/regumate", "reguuser",
                                "regupass");
                        PreparedStatement deleteStatement = con
                                .prepareStatement("DELETE FROM meetings WHERE meeting_time = ?"); // change made
                        deleteStatement.setString(1, meetingTimeToDelete);
                        int rowsDeleted = deleteStatement.executeUpdate();
                        if (rowsDeleted > 0) {
                            JOptionPane.showMessageDialog(null, "Meeting deleted successfully.");

                            refreshButton.doClick();

                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to delete meeting.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Database error.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.setBounds(690, 618, 90, 25);
        deleteButton.setFont(new Font("Times new Roman", Font.BOLD, 14));
        deleteButton.setForeground(Color.BLACK);
        deleteButton.setBackground(transparentColor);

        panel2.add(deleteButton);

        return panel2;
    }

    public void refreshData(Vector<Vector<Object>> data) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/regumate", "reguuser",
                    "regupass");
            PreparedStatement pst = con.prepareStatement("select * from meetings");
            ResultSet rs = pst.executeQuery();
            data.clear();

            int serialNumber = 1; // changes made

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(serialNumber++); // adding serial number to the row // changes made by Anand on April 5 at 07:54
                row.add(rs.getString("meeting_id"));
                row.add(rs.getString("meeting_time"));
                row.add(rs.getInt("total_meeting"));
                row.add(rs.getString("passcode"));
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // MAIN FUNCTION
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new input());

    }

    static class PanelRound extends JPanel {

        public int getRoundTopRight() {
            return roundTopRight;
        }

        public void setRoundTopRight(int roundTopRight) {
            this.roundTopRight = roundTopRight;
            repaint();
        }

        public int getRoundTopLeft() {
            return roundTopLeft;
        }

        public void setRoundTopLeft(int roundTopRight) {
            this.roundTopLeft = roundTopLeft;
            repaint();
        }

        public int getRoundBottomRight() {
            return roundBottomRight;
        }

        public void setRoundBottomRight(int roundTopRight) {
            this.roundBottomRight = roundBottomRight;
            repaint();
        }

        public int getRoundBottomLeft() {
            return roundBottomLeft;
        }

        public void setRoundBottomLeft(int roundTopRight) {
            this.roundBottomLeft = roundBottomLeft;
            repaint();
        }

        public int roundTopLeft = 200;
        public int roundTopRight = 200;
        public int roundBottomLeft = 200;
        public int roundBottomRight = 200;

        public PanelRound() {

            setOpaque(false);

        }

        @Override
        public void paintComponent(Graphics grphcs) {
            Graphics2D g2 = (Graphics2D) grphcs.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            Area area = new Area(createRoundTopLeft());
            if (roundTopRight > 0) {
                area.intersect(new Area(createRoundTopRight()));

            }
            if (roundBottomLeft > 0) {
                area.intersect(new Area(createRoundBottomLeft()));
            }
            if (roundBottomRight > 0) {
                area.intersect(new Area(createRoundBottomRight()));
            }
            g2.fill(area);
            g2.dispose();
            super.paintComponent(grphcs);

        }

        public Shape createRoundTopLeft() {
            int width = getWidth();
            int height = getHeight();
            int roundX = Math.min(width, roundTopLeft);
            int roundY = Math.min(height, roundTopLeft);
            Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, roundX, roundY));
            area.add(new Area(new Rectangle2D.Double(roundX / 2, 0, width - roundX / 2, height)));
            area.add(new Area(new Rectangle2D.Double(0, roundY / 2, width, height - roundY / 2)));
            return area;
        }

        public Shape createRoundTopRight() {
            int width = getWidth();
            int height = getHeight();
            int roundX = Math.min(width, roundTopRight);
            int roundY = Math.min(height, roundTopRight);
            Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, roundX, roundY));
            area.add(new Area(new Rectangle2D.Double(0, 0, width - roundX / 2, height)));
            area.add(new Area(new Rectangle2D.Double(0, roundY / 2, width, height - roundY / 2)));
            return area;
        }

        public Shape createRoundBottomLeft() {
            int width = getWidth();
            int height = getHeight();
            int roundX = Math.min(width, roundBottomLeft);
            int roundY = Math.min(height, roundBottomLeft);
            Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, roundX, roundY));
            area.add(new Area(new Rectangle2D.Double(roundX / 2, 0, width - roundX / 2, height)));
            area.add(new Area(new Rectangle2D.Double(0, 0, width, height - roundY / 2)));
            return area;
        }

        public Shape createRoundBottomRight() {
            int width = getWidth();
            int height = getHeight();
            int roundX = Math.min(width, roundBottomRight);
            int roundY = Math.min(height, roundBottomRight);
            Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, roundX, roundY));
            area.add(new Area(new Rectangle2D.Double(0, 0, width - roundX / 2, height)));
            area.add(new Area(new Rectangle2D.Double(0, 0, width, height - roundY / 2)));
            return area;
        }
    }

}

// R E G U M A T E //
