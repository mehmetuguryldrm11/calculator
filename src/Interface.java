import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

public class Interface extends JFrame {

    private JPanel interfaceP;
    private JTable userttable;


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Interface frame = new Interface();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public Interface() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 174, 862, 510);
        interfaceP = new JPanel();
        interfaceP.setBorder(new EmptyBorder(5, 5, 5, 5));
        interfaceP.setLayout(new BorderLayout(0, 0));
        setContentPane(interfaceP);


        JPanel anapanel = new JPanel();
        interfaceP.add(anapanel);

        anapanel.setBounds(0, 37, 862, 398);
        anapanel.setLayout(null);

        /**
         * Ust Panel
         */
        JPanel yupanel = new JPanel();
        yupanel.setBounds(0, 0, 862, 37);
        interfaceP.add(yupanel);
        yupanel.setLayout(null);
        yupanel.setBackground(Color.DARK_GRAY);

        JToggleButton maintglbtn = new JToggleButton("VIEW");
        maintglbtn.setBounds(780, 3, 73, 32);
        maintglbtn.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        maintglbtn.setForeground(Color.BLACK);
        yupanel.add(maintglbtn);

        JButton bttnHome = new JButton("HOME");

        bttnHome.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        bttnHome.setBounds(10, 3, 73, 32);
        bttnHome.setForeground(Color.BLACK);
        yupanel.add(bttnHome);

        /**
         * Alt Panel
         */

        JPanel downpanel = new JPanel();
        interfaceP.add(downpanel);
        downpanel.setBounds(0, 435, 862, 37);
        downpanel.setLayout(null);
        downpanel.setBackground(Color.DARK_GRAY);

        JButton btnnDeafen= new JButton("DEAFEN");
        btnnDeafen.setForeground(Color.BLACK);
        btnnDeafen.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        btnnDeafen.setBounds(0, 430, 90, 32);
        downpanel.add(btnnDeafen);

        JButton btnnMute = new JButton("MUTE");
        btnnMute.setForeground(Color.BLACK);
        btnnMute.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        btnnMute.setBounds(93, 430, 73, 32);
        downpanel.add(btnnMute);

        JButton btnnPermission = new JButton("PERMISSION");
        btnnPermission.setForeground(Color.BLACK);
        btnnPermission.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        btnnPermission.setBounds(169, 430, 117, 32);
        downpanel.add(btnnPermission);

        JButton bttnQuit = new JButton("QUIT");
        bttnQuit.setForeground(Color.BLACK);
        bttnQuit.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        bttnQuit.setBounds(751, 430, 73, 32);
        downpanel.add(bttnQuit);

        /**
         * Ara Panel
         */

        JPanel subPanel = new JPanel();
        subPanel.setBounds(0, 74, 814, 216);
        anapanel.add(subPanel);
        subPanel.setLayout(null);

        JLabel chnnlL = new JLabel("CHANNELS");
        chnnlL.setForeground(Color.BLACK);
        chnnlL.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        chnnlL.setHorizontalAlignment(SwingConstants.CENTER);
        chnnlL.setBounds(-20, -15, 193, 38);
        subPanel.add(chnnlL);

        JButton chnnlOne = new JButton("CHANNEL 1");
        chnnlOne.setForeground(Color.BLACK);
        chnnlOne.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        chnnlOne.setBounds(10, 26, 242, 40);
        subPanel.add(chnnlOne);

        JButton chnnlTwo = new JButton("CHANNEL 2");
        chnnlTwo.setForeground(Color.BLACK);
        chnnlTwo.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        chnnlTwo.setBounds(10, 76, 242, 40);
        subPanel.add(chnnlTwo);

        JButton chnnlThree = new JButton("CHANNEL 3");
        chnnlThree.setForeground(Color.BLACK);
        chnnlThree.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        chnnlThree.setBounds(10, 126, 242, 40);
        subPanel.add(chnnlThree);

        JButton chnnlFour = new JButton("CHANNEL 4");
        chnnlFour.setForeground(Color.BLACK);
        chnnlFour.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        chnnlFour.setBounds(10, 176, 242, 40);
        subPanel.add(chnnlFour);

        JButton usrB = new JButton("USERS");
        usrB.setForeground(Color.BLACK);
        usrB.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        usrB.setBounds(400, 10, 300, 200);
        subPanel.add(usrB);

        yupanel.setVisible(true);
        subPanel.setVisible(true);
        downpanel.setVisible(true);


        bttnHome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                anapanel.setVisible(true);
            }
        });
        chnnlTwo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                anapanel.setVisible(false);
            }
        });

        chnnlOne.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                anapanel.setVisible(false);
            }

        });
        chnnlThree.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                anapanel.setVisible(false);
            }
        });
        chnnlFour.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                anapanel.setVisible(false);
            }
        });
        bttnQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });


    }
}