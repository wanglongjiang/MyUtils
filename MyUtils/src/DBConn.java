import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class DBConn {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new DBConnFrame().setVisible(true);
	}
}

class DBConnFrame extends JFrame {
	private static final long serialVersionUID = -7057024474845891568L;

	private JTextField urlField = new JTextField("jdbc:oracle:thin:@127.0.0.1:1521:orcl");
	private JTextField usernameField = new JTextField("");
	private JTextField passwordField = new JTextField("");
	private JTextArea sqlArea = new JTextArea();
	private JLabel statusLabel = new JLabel("等待测试");
	private JTable resultTable = new JTable();

	// 构造方法
	DBConnFrame() throws HeadlessException {
		super("测试数据库连接");
		setSize(1024, 768);
		setLocationByPlatform(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setupComponents();
	}

	// 初始化控件
	private void setupComponents() {
		setupBorder();

		JPanel topPanel = new JPanel(new BorderLayout(5, 0));
		this.add(topPanel, BorderLayout.NORTH);
		setupTopPanel(topPanel);

		JPanel centerPanel = new JPanel(new BorderLayout());
		this.add(centerPanel, BorderLayout.CENTER);
		setupCenterPanel(centerPanel);
	}

	// 给窗体面板加上边框
	private void setupBorder() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setContentPane(panel);
	}

	// 文本域面板
	private void setupCenterPanel(JPanel panel) {
		sqlArea.setFont(new Font(Font.DIALOG_INPUT, 0, 12));
		sqlArea.setText("select sysdate from dual");
		panel.add(new JScrollPane(sqlArea), BorderLayout.NORTH);
		panel.add(new JLabel("运行结果："), BorderLayout.WEST);
		panel.add(new JScrollPane(resultTable), BorderLayout.CENTER);
		statusLabel.setFont(new Font(Font.DIALOG_INPUT, 0, 12));
		panel.add(statusLabel, BorderLayout.SOUTH);
	}

	// 上方面板
	private void setupTopPanel(JPanel panel) {
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(3, 1));
		panel1.add(new JLabel("URL:"));
		panel1.add(new JLabel("用户名:"));
		panel1.add(new JLabel("密码:"));
		panel.add(panel1, BorderLayout.WEST);

		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(3, 1));
		urlField.setFont(new Font(Font.DIALOG_INPUT, 0, 12));
		panel2.add(urlField);
		usernameField.setFont(new Font(Font.DIALOG_INPUT, 0, 12));
		panel2.add(usernameField);
		passwordField.setFont(new Font(Font.DIALOG_INPUT, 0, 12));
		panel2.add(passwordField);
		panel.add(panel2, BorderLayout.CENTER);

		JButton button = new JButton("测试");
		panel.add(button, BorderLayout.EAST);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Connection conn = null;
				try {
					conn = DriverManager.getConnection(urlField.getText(), usernameField.getText(), passwordField.getText());
					statusLabel.setText("连接成功!!!!");
					if (sqlArea.getText() != null && !"".equals(sqlArea.getText())) {
						Statement stmt = conn.createStatement();
						ResultSet resultSet = stmt.executeQuery(sqlArea.getText());
						ResultSetMetaData metaData = resultSet.getMetaData();
						DefaultTableModel tableModel = new DefaultTableModel();
						for (int i = 1; i <= metaData.getColumnCount(); i++) {
							tableModel.addColumn(metaData.getColumnLabel(i));
						}
						while (resultSet.next()) {
							Object[] rowData = new Object[metaData.getColumnCount()];
							for (int i = 1; i <= metaData.getColumnCount(); i++) {
								rowData[i - 1] = resultSet.getString(i);
							}
							tableModel.addRow(rowData);
						}
						resultTable.setModel(tableModel);
						resultTable.updateUI();
					}
				} catch (SQLException e1) {
					statusLabel.setText(e1.toString());
					e1.printStackTrace();
				} finally {
					try {
						if (conn != null)
							conn.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}
}