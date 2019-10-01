
package io.opthema.com.egitim.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import io.opthema.com.egitim.Parse;
import io.opthema.com.egitim.Starter;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class Gui {

	public String gonderilen;
	private JFrame frame;
	public JTextField textField;
	public JList list;
	String[] dizi;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frame.setVisible(true);
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	
	
	public Gui() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(200, 200, 654, 412);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("OK");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Starter starter = new Starter();
				try {
					String text = textField.getText().toString().trim();
					String[] s= starter.startDownload(text.replace(" ", "+"));
					dizi=s;
					list.setListData(dizi);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		
		btnNewButton.setBounds(234, 10, 119, 69);
		frame.getContentPane().add(btnNewButton);
		
		
		
		list= new JList();
		list.setBounds(96, 116, 128, 105);
		list.setVisible(true);
		list.getBorder();
		frame.getContentPane().add(list);
		
		list.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        JList list2 = (JList)evt.getSource();
		        if (evt.getClickCount() == 2) {
		            Parse prs = new Parse();
		            try {
						prs.LinkeGit(list2.getSelectedIndex());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        } 
		    }
		});
		
		
		textField = new JTextField();
		textField.setBounds(105, 24, 119, 42);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(332, 132, 160, 58);
		frame.getContentPane().add(lblNewLabel);
		
		
	}
}