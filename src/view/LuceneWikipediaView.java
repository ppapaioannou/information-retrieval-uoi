package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

import search.AdvancedSearcher;
import search.SimpleSearcher;

import javax.swing.JTextArea;

public class LuceneWikipediaView {
	
	private JFrame frame = new JFrame("WikipediaMovies");
	
	private JTextField searchField;
	
	String index = "index";
	
	private IndexReader reader;
	private IndexSearcher searcher;
	private EnglishAnalyzer analyzer;
	
	private JLabel numOfResultsLabel;
	
	private  String output="";
	
	
	public LuceneWikipediaView() {
		
		initiateIndexSearcher();
		
		try {
			UIManager.setLookAndFeel(
		            UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e) { }
		
		frame.getContentPane().setBackground(new Color(255, 204, 255));
		frame.getContentPane().setLayout(null);
		frame.setSize(new Dimension(700, 700));
		//frame.setForeground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setBackground(Color.WHITE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		setFrameLayout();
		
	}

	private void setFrameLayout() {
		setTitleLabel();
		setSearchField();
		setNumOfResultsLabel();
		setSearchHistory();
		//handleButtons();
		setButtons();
		
	}

	private void setTitleLabel() {
		JLabel TitleLabel = new JLabel("Wikipedia Movie Searcher");
		TitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		TitleLabel.setFont(new Font("Segoe UI Historic", Font.BOLD, 45));
		TitleLabel.setBounds(38, 50, 605, 72);
		frame.getContentPane().add(TitleLabel);
		
	}

	private void setSearchField() {
		searchField = new JTextField();
		searchField.setFont(new Font("Segoe UI Historic", Font.PLAIN, 25));
		searchField.setBounds(38, 162, 394, 39);
		frame.getContentPane().add(searchField);
		searchField.setColumns(10);
		
	}

	private void setNumOfResultsLabel() {
		numOfResultsLabel = new JLabel();
		numOfResultsLabel.setHorizontalAlignment(SwingConstants.LEFT);
		numOfResultsLabel.setFont(new Font("Segoe UI Historic", Font.BOLD, 18));
		numOfResultsLabel.setBounds(38, 198, 188, 28);
		frame.getContentPane().add(numOfResultsLabel);
		
	}

	private void setSearchHistory() {
		setSearchHistoryLabel();
		setSearchHistoryTextArea();
		
	}

	private void setSearchHistoryLabel() {
		JLabel searchHistoryLabel = new JLabel("Search History");
		searchHistoryLabel.setHorizontalAlignment(SwingConstants.LEFT);
		searchHistoryLabel.setFont(new Font("Segoe UI Historic", Font.PLAIN, 20));
		searchHistoryLabel.setBounds(38, 282, 147, 28);
		frame.getContentPane().add(searchHistoryLabel);
		
	}

	private void setSearchHistoryTextArea() {
		JTextArea textArea = new JTextArea();
		textArea.setBounds(38, 318, 605, 230);
		textArea.setFont(new Font("Segoe UI Historic", Font.PLAIN, 15));
		textArea.setBackground(new Color(204, 204, 204));
		textArea.setEditable(false);
		frame.getContentPane().add(textArea);
		
		//search history
		File file = new File("history.txt");
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String str;
			int newLine=0;
			while((str=br.readLine())!=null) {
				output+=str+", ";
				newLine++;
				if(newLine==4) {
					output+="\n";
					newLine=0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		textArea.append(output);
		
	}

	private void setButtons() {
		setSimpleSearchButton();
		setAdvancedSearchButton();
		
	}
	
	private void setSimpleSearchButton() {
		JButton simpleSearchButton = new JButton("Search");
		simpleSearchButton.setFocusPainted(false);
		simpleSearchButton.setBackground(new Color(221, 160, 221));
		simpleSearchButton.setFont(new Font("Segoe UI Historic", Font.PLAIN, 20));
		simpleSearchButton.setBounds(444, 162, 199, 39);
		
		simpleSearchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleSimpleSearchInput();				
			}
			
		});
		
		frame.getRootPane().setDefaultButton(simpleSearchButton);
		simpleSearchButton.requestFocus();
		
		frame.getContentPane().add(simpleSearchButton);
		
	}

	private void setAdvancedSearchButton() {
		JButton advancedSearchButton = new JButton("Advanced Search");
		advancedSearchButton.setFocusable(false);
		advancedSearchButton.setFont(new Font("Segoe UI Historic", Font.PLAIN, 20));
		advancedSearchButton.setFocusPainted(false);
		advancedSearchButton.setBackground(new Color(221, 160, 221));
		advancedSearchButton.setBounds(444, 601, 199, 39);
		
		advancedSearchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AdvancedSearcher advancedSearch = new AdvancedSearcher(frame, reader, searcher, analyzer);
				advancedSearch.handleAdvancedSearch();
				
			}
			
		});
		
		frame.getContentPane().add(advancedSearchButton);
		
	}

	

	private void handleSimpleSearchInput() {
		String searchInput = searchField.getText().trim();
		if(!searchInput.equals("")) {
			SimpleSearcher simpleSearch = new SimpleSearcher(searchInput, reader, searcher, analyzer);
			simpleSearch.pagingSearch();
			numOfResultsLabel.setText("Found "+simpleSearch.getTotalHits()+" results");
			
			ResultsManager resultManager = new ResultsManager(simpleSearch.getResultPages());
			
			resultManager.showResults();
			
		}
		
	}
	
	public void initiateIndexSearcher() {
		try {
			reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
			searcher = new IndexSearcher(reader);
			analyzer = new EnglishAnalyzer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

	public static void main(String[] args) {
		LuceneWikipediaView window = new LuceneWikipediaView();
		window.frame.setVisible(true);

	}
}
