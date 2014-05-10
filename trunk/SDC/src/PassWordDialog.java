import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * 
 */

/**
 * @author richardr
 *
 */
public class PassWordDialog extends JDialog {

	
	private final JLabel jlblUsername = new JLabel("Username");
    private final JLabel jlblPassword = new JLabel("Password");

    private final JTextField jtfUsername = new JTextField(15);
    private final JPasswordField jpfPassword = new JPasswordField();

    private final JButton jbtOk = new JButton("Login");
    private final JButton jbtCancel = new JButton("Cancel");

    private final JTextArea jlblStatus = new JTextArea(20,20);

    public PassWordDialog() {
        this(null, true);
    }

    public PassWordDialog(final JFrame parent, boolean modal) {
        super(parent, modal);
        
        this.setTitle("ANU-RSES Seismic Data Centre v1.7");
        

        JPanel p3 = new JPanel(new GridLayout(2, 1));
        p3.add(jlblUsername);
        p3.add(jlblPassword);

        JPanel p4 = new JPanel(new GridLayout(2, 1));
        p4.add(jtfUsername);
        p4.add(jpfPassword);

        JPanel p1 = new JPanel();
        p1.setBorder(new EmptyBorder(10, 10, 10, 10) );
        p1.add(p3);
        p1.add(p4);

        JPanel p2 = new JPanel();
        p2.setBorder(new EmptyBorder(10, 10, 10, 10) );
        p2.add(jbtOk);
        p2.add(jbtCancel);
        
        JPanel p6 = new JPanel();
        p6.setBorder(new EmptyBorder(10, 10, 0, 10) );
        p6.add(jlblStatus);
        jlblStatus.setForeground(Color.RED);
        jlblStatus.setBackground(getBackground());
        jlblStatus.setEditable(false);
        jlblStatus.setWrapStyleWord(true);
        jlblStatus.setLineWrap(true);
        

        JPanel p5 = new JPanel(new GridLayout(3, 1));
        p5.add(p1,BorderLayout.NORTH);
        p5.add(p6, BorderLayout.CENTER);
        p5.add(p2, BorderLayout.SOUTH);
        

        //setLayout(new GridLayout(2, 1));
        //add(p1, BorderLayout.CENTER);
        //add(p5, BorderLayout.SOUTH);
        add(p5);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {  
            @Override
            public void windowClosing(WindowEvent e) {  
                System.exit(0);  
            }  
        });


        jbtOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ("test".equals(jpfPassword.getText())
                        && "test".equals(jtfUsername.getText())) {
                    parent.setVisible(true);
                    setVisible(false);
                } else {
                    jlblStatus.setText("Invalid username or password\nPlease contact Email:xxx@anu.edu.au");
                }
            }
        });
        jbtCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                parent.dispose();
                System.exit(0);
            }
        });
        
        
    }

}
