package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import results.Page;
import results.Result;

public class ResultsManager {
	
	private ArrayList<Page> resultPages;
	
	private JFrame frame;
	
	private int currentPage = 0;

	public ResultsManager(ArrayList<Page> resultPages) {
		this.resultPages=resultPages;
	}

	public void showResults() {
		ResultsFrame();
	}
	
	private void ResultsFrame() {
		frame = new JFrame("Page "+String.valueOf(currentPage+1));
		frame.setLayout(new BorderLayout());
		
		JPanel panel = createPanel();
		
		panel.setBackground(new Color(221, 160, 221));
		
		JScrollPane scrollBar = new JScrollPane(panel);
		
		scrollBar.getVerticalScrollBar().setUnitIncrement(16);
		
		frame.add(BorderLayout.CENTER, scrollBar);
	    frame.setSize(800, 600);
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	}

	private JPanel createPanel() {
		JPanel panel = new JPanel();
		GridLayout gl = new GridLayout(21,2,10,1);
		panel.setLayout(gl);

		fillPage(resultPages.get(currentPage),panel);
		
		
		JPanel buttomPanel = new JPanel();
		buttomPanel.setBackground(new Color(221, 160, 221));
		buttomPanel.setLayout(new GridLayout(1,3,10,1));
		buttomPanel.setBounds(21, 1, 10, 10);
		
		panel.add(buttomPanel);
		
		JButton prevPage = new JButton("<-");
		prevPage.setFont(new Font("Segoe UI Historic", Font.BOLD, 20));
		prevPage.setFocusable(false);
		
		prevPage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentPage>0) {
					currentPage-=1;
					createPanel();
					frame.dispose();
					ResultsFrame();
				}
				
			}
			
		});
		
		buttomPanel.add(prevPage);

		
		JButton nextPage = new JButton("->");
		nextPage.setFocusable(false);
		nextPage.setFont(new Font("Segoe UI Historic", Font.BOLD, 20));
		
		nextPage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentPage+1<resultPages.size()-1) {
					currentPage+=1;
					createPanel();
					frame.dispose();
					ResultsFrame();
				}
				
			}
			
		});
		
		buttomPanel.add(nextPage);

		
		JLabel num = new JLabel(String.valueOf(currentPage+1)+"-"+String.valueOf(resultPages.size()-1));
		num.setFont(new Font("Segoe UI Historic", Font.BOLD, 20));	
		buttomPanel.add(num);

		
		
		return panel;
	}

	private void fillPage(Page page, JPanel panel) {
		int y=0;
		for(Result res : page.getResults()) {

			JLabel hyperlink = new JLabel("<html>"+res.getHypelinkText()+"</html>");
			hyperlink.setFont(new Font("Segoe UI Historic", Font.PLAIN, 20));
			hyperlink.setBorder(null);
			hyperlink.setBounds(1, y++, 20, 20);
			hyperlink.setForeground(Color.BLUE.darker());
			hyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			
			hyperlink.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
				   	try {
				   		Desktop.getDesktop().browse(new URI(res.getURL()));	             
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			
			panel.add(hyperlink);
			
			
			JLabel resultInfo = new JLabel("<html>"+res.getResultInfo()+"</html>");
			resultInfo.setFont(new Font("Segoe UI Historic", Font.PLAIN, 14));
			resultInfo.setBorder(null);
			resultInfo.setBounds(1, y++, 20, 20);
			panel.add(resultInfo);
		}
		
	}

}
