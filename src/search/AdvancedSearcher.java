package search;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;

import results.Page;
import view.ResultsManager;


public class AdvancedSearcher {
	
	private JFrame frame = new JFrame("Advanced Search");
	
	private JFrame parentFrame;
	
	private IndexReader reader;
	private IndexSearcher searcher;
	private EnglishAnalyzer analyzer;
	
	private Color initialColor = new Color(105, 105, 105);

	private JTextField titleTextField;

	private JTextField directorTextField;

	private JTextField originTextField;

	private JTextField genreTextField;

	private JTextField yearTextField;

	private JTextField castTextField;

	private JTextField ratingTextField;
	
	private JLabel numOfResultsLabel;
	
	private JRadioButton defaultRadioButton;
	private JRadioButton yearRadioButton;
	private JRadioButton ratingRadioButton;
	
	
	private int hitsPerPage = 10;
	private int start = 0;
	private int end = 0;
	private int numTotalHits;
	
	private String output="";
	
	private ArrayList<Page> results = new ArrayList<Page>();
	
	private String[] fields = {"releaseDate","title","origin","director","cast","genre","rating"};
	
	
	public AdvancedSearcher(JFrame parentFrame, IndexReader reader, IndexSearcher searcher, EnglishAnalyzer analyzer) {
		this.parentFrame=parentFrame;
		this.reader=reader;
		this.searcher=searcher;
		this.analyzer=analyzer;
		

		

	}


	public void handleAdvancedSearch() {
		setAdvancedSearchFrame();
		frame.setVisible(true);
		
	}


	


	private void setAdvancedSearchFrame() {
		try {
			UIManager.setLookAndFeel(
		            UIManager.getSystemLookAndFeelClassName());
	    }catch(Exception e) { }
		
		frame.getContentPane().setBackground(new Color(255, 204, 255));
		frame.getContentPane().setLayout(null);
		frame.setBounds(0, 0, 670, 550);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(parentFrame);
		frame.setResizable(false);
		
		setTitleField();
		setDirectorField();
		setOriginField();
		setGenreField();
		setYearField();
		setCastField();
		setRatingField();
		setSortingMethods();
		setNumOfResultsLabel();
		setSearchHistory();
		setAdvancedSearchButton();
		
	}


	


	


	


	


	private void setTitleField() {
		setTitleLabel();
		setTitleSearchField();
		
	}


	private void setTitleLabel() {
		JLabel startingLabel = new JLabel("Title: ");
		startingLabel.setFont(new Font("Segoe UI Historic", Font.PLAIN, 18));
		startingLabel.setBounds(12, 16, 50, 25);
		frame.getContentPane().add(startingLabel);
		
	}


	private void setTitleSearchField() {
		titleTextField = new JTextField("Movie Title");
		titleTextField.setHorizontalAlignment(JTextField.CENTER);
		
		titleTextField.setForeground(initialColor);
		titleTextField.setFont(new Font("Segoe UI Historic", Font.PLAIN, 15));
		titleTextField.setBounds(117, 17, 172, 25);
		titleTextField.setColumns(10);
		
		titleTextField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if(titleTextField.getText().equals("Movie Title") && titleTextField.getForeground()==initialColor) {
					titleTextField.setText("");
					titleTextField.setForeground(Color.black);
				}
				
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				
			}
		    
		});
		
		frame.getContentPane().add(titleTextField);
		
	}


	private void setDirectorField() {
		setDirectorLabel();
		setDirectorSearchField();
		
	}


	private void setDirectorLabel() {
		JLabel directedByLabel = new JLabel("Director:");
		directedByLabel.setFont(new Font("Segoe UI Historic", Font.PLAIN, 18));
		directedByLabel.setBounds(12, 56, 80, 25);
		frame.getContentPane().add(directedByLabel);
		
	}


	private void setDirectorSearchField() {
		directorTextField = new JTextField("Director");
		directorTextField.setHorizontalAlignment(SwingConstants.CENTER);
		directorTextField.setForeground(initialColor);
		directorTextField.setFont(new Font("Segoe UI Historic", Font.PLAIN, 15));
		directorTextField.setColumns(10);
		directorTextField.setBounds(117, 57, 172, 25);
		
		directorTextField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if(directorTextField.getText().equals("Director") && directorTextField.getForeground()==initialColor) {
					directorTextField.setText("");
					directorTextField.setForeground(Color.black);
				}
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				
			}
		    
		});
		
		frame.getContentPane().add(directorTextField);
		
	}


	private void setOriginField() {
		setOriginLabel();
		setOriginSearchField();
		
	}


	private void setOriginLabel() {
		JLabel originLabel = new JLabel("Origin:\r\n");
		originLabel.setFont(new Font("Segoe UI Historic", Font.PLAIN, 18));
		originLabel.setBounds(12, 94, 70, 25);
		frame.getContentPane().add(originLabel);
		
	}


	private void setOriginSearchField() {
		originTextField = new JTextField("Country of Origin");
		originTextField.setHorizontalAlignment(SwingConstants.CENTER);
		originTextField.setForeground(initialColor);
		originTextField.setFont(new Font("Segoe UI Historic", Font.PLAIN, 15));
		originTextField.setColumns(10);
		originTextField.setBounds(117, 95, 172, 25);
		
		originTextField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if(originTextField.getText().equals("Country of Origin") && originTextField.getForeground()==initialColor) {
					originTextField.setText("");
					originTextField.setForeground(Color.black);
				}
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				
			}
		    
		});
		
		frame.getContentPane().add(originTextField);
		
	}


	private void setGenreField() {
		setGenreLabel();
		setGenreSearchField();
		
	}


	private void setGenreLabel() {
		JLabel lblGenre = new JLabel("Genre:");
		lblGenre.setFont(new Font("Segoe UI Historic", Font.PLAIN, 18));
		lblGenre.setBounds(12, 128, 53, 25);
		frame.getContentPane().add(lblGenre);
		
	}


	private void setGenreSearchField() {
		genreTextField = new JTextField("Genre type");
		genreTextField.setHorizontalAlignment(SwingConstants.CENTER);
		genreTextField.setForeground(initialColor);
		genreTextField.setFont(new Font("Segoe UI Historic", Font.PLAIN, 15));
		genreTextField.setColumns(10);
		genreTextField.setBounds(117, 133, 172, 25);
		
		genreTextField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if(genreTextField.getText().equals("Genre type") && genreTextField.getForeground()==initialColor) {
					genreTextField.setText("");
					genreTextField.setForeground(Color.black);
				}
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				
			}
		    
		});
		
		frame.getContentPane().add(genreTextField);
		
	}


	private void setYearField() {
		setYearLabel();
		setYearSearchField();
		
	}


	private void setYearLabel() {
		JLabel theLabel = new JLabel("Year:");
		theLabel.setFont(new Font("Segoe UI Historic", Font.PLAIN, 18));
		theLabel.setBounds(350, 13, 41, 30);
		frame.getContentPane().add(theLabel);
		
	}


	private void setYearSearchField() {
		yearTextField = new JTextField("Year");
		yearTextField.setHorizontalAlignment(SwingConstants.CENTER);
		yearTextField.setForeground(initialColor);
		yearTextField.setFont(new Font("Segoe UI Historic", Font.PLAIN, 15));
		yearTextField.setColumns(10);
		yearTextField.setBounds(438, 17, 172, 25);
		
		yearTextField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if(yearTextField.getText().equals("Year") && yearTextField.getForeground()==initialColor) {
					yearTextField.setText("");
					yearTextField.setForeground(Color.black);
				}
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				
			}
		    
		});
		
		frame.getContentPane().add(yearTextField);
		
	}


	private void setCastField() {
		setCastLabel();
		setCastSearchField();
		
	}


	private void setCastLabel() {
		JLabel castLabel = new JLabel("Cast:");
		castLabel.setFont(new Font("Segoe UI Historic", Font.PLAIN, 18));
		castLabel.setBounds(350, 53, 41, 30);
		frame.getContentPane().add(castLabel);
		
	}


	private void setCastSearchField() {
		
		
		castTextField = new JTextField("Cast");
		castTextField.setHorizontalAlignment(SwingConstants.CENTER);
		castTextField.setForeground(initialColor);
		castTextField.setFont(new Font("Segoe UI Historic", Font.PLAIN, 15));
		castTextField.setColumns(10);
		castTextField.setBounds(438, 57, 172, 25);
		
		castTextField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if(castTextField.getText().equals("Cast") && castTextField.getForeground()==initialColor) {
					castTextField.setText("");
					castTextField.setForeground(Color.black);
				}
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				
			}
		    
		});
		
		frame.getContentPane().add(castTextField);
		
	}


	private void setRatingField() {
		setRatingLabel();
		setRatingSearchField();
		
	}


	private void setRatingLabel() {
		JLabel ratingLabel = new JLabel("Rating:");
		ratingLabel.setFont(new Font("Segoe UI Historic", Font.PLAIN, 18));
		ratingLabel.setBounds(350, 94, 60, 30);
		frame.getContentPane().add(ratingLabel);
		
	}


	private void setRatingSearchField() {
		
		
		ratingTextField = new JTextField("Rating out of 10");
		ratingTextField.setHorizontalAlignment(SwingConstants.CENTER);
		ratingTextField.setForeground(initialColor);
		ratingTextField.setFont(new Font("Segoe UI Historic", Font.PLAIN, 15));
		ratingTextField.setColumns(10);
		ratingTextField.setBounds(438, 98, 172, 26);
		
		ratingTextField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if(ratingTextField.getText().equals("Rating out of 10") && ratingTextField.getForeground()==initialColor) {
					ratingTextField.setText("");
					ratingTextField.setForeground(Color.black);
				}
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				//ratingTextField.setForeground(new Color(105, 105, 105));
				
			}
		    
		});
		
		frame.getContentPane().add(ratingTextField);
		
	}
	
	private void setSortingMethods() {
		setSortByLabel();
		
		setDefaultRadioButton();
		
		setYearRadioButton();
		
		setRatingRadioButton();
		
		ButtonGroup sortingButtonGroup = new ButtonGroup();
		
		sortingButtonGroup.add(defaultRadioButton);
		sortingButtonGroup.add(yearRadioButton);
		sortingButtonGroup.add(ratingRadioButton);
		
		
		
	}
	
	private void setSortByLabel() {
		JLabel lblSortBy = new JLabel("Sort by");
		lblSortBy.setFont(new Font("Segoe UI Historic", Font.PLAIN, 17));
		lblSortBy.setBounds(12, 168, 68, 30);
		frame.getContentPane().add(lblSortBy);
		
	}


	private void setDefaultRadioButton() {
		defaultRadioButton = new JRadioButton("Default");
		defaultRadioButton.setSelected(true);
		defaultRadioButton.setFont(new Font("Segoe UI Historic", Font.PLAIN, 14));
		defaultRadioButton.setFocusable(false);
		defaultRadioButton.setBorder(null);
		defaultRadioButton.setBackground(new Color(255, 204, 255));
		defaultRadioButton.setBounds(86, 173, 71, 25);
		frame.getContentPane().add(defaultRadioButton);
		
		
	}

	
	private void setYearRadioButton() {
		yearRadioButton = new JRadioButton("Year");
		yearRadioButton.setFocusable(false);
		yearRadioButton.setBackground(new Color(255, 204, 255));
		yearRadioButton.setBorder(null);
		yearRadioButton.setFont(new Font("Segoe UI Historic", Font.PLAIN, 14));
		yearRadioButton.setBounds(246, 173, 60, 25);
		frame.getContentPane().add(yearRadioButton);
		
	}


	private void setRatingRadioButton() {
		ratingRadioButton= new JRadioButton("Rating");
		ratingRadioButton.setFocusable(false);
		ratingRadioButton.setBorder(null);
		ratingRadioButton.setBackground(new Color(255, 204, 255));
		ratingRadioButton.setFont(new Font("Segoe UI Historic", Font.PLAIN, 14));
		ratingRadioButton.setBounds(169, 173, 68, 25);
		frame.getContentPane().add(ratingRadioButton);
		
	}
	
	private void setNumOfResultsLabel() {
		numOfResultsLabel = new JLabel("");
		numOfResultsLabel.setFont(new Font("Segoe UI Historic", Font.PLAIN, 18));
		numOfResultsLabel.setBounds(438, 205, 172, 25);
		frame.getContentPane().add(numOfResultsLabel);
		
	}
	
	private void setSearchHistory() {
		setSearchHistoryLabel();
		
		
		setSearchHistoryTextArea();
		
		
	}


	private void setSearchHistoryLabel() {
		JLabel searchHistoryLabel = new JLabel("Search History");
		searchHistoryLabel.setFont(new Font("Segoe UI Historic", Font.PLAIN, 18));
		//searchHistoryLabel.setBounds(438, 205, 172, 25);
		searchHistoryLabel.setBounds(12, 211, 147, 28);
		frame.getContentPane().add(searchHistoryLabel);
		
	}


	private void setSearchHistoryTextArea() {

		JTextArea textArea = new JTextArea();
		textArea.setBounds(12, 243, 618, 250);
		textArea.setFont(new Font("Segoe UI Historic", Font.PLAIN, 15));
		textArea.setBackground(new Color(204, 204, 204));
		textArea.setEditable(false);
		frame.getContentPane().add(textArea);
		
		//search history
		File file = new File("advhistory.txt");
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String str;
			while((str=br.readLine())!=null) {
				output+=str+"\n";
					
				
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		textArea.append(output);		
	}
	
	private void createSearchHistory(String line) {
		try {
            BufferedWriter out = new BufferedWriter( 
                   new FileWriter("advhistory.txt", true)); 
            out.write(line); 
            out.newLine();
            out.close(); 
        } 
        catch (Exception e) { 
            System.out.println("exception occoured" + e); 
        } 
	}


	private void setAdvancedSearchButton() {
		JButton advancedSearchButton = new JButton("Search");
		advancedSearchButton.setFont(new Font("Segoe UI Historic", Font.PLAIN, 20));
		advancedSearchButton.setBounds(396, 163, 241, 35);
		advancedSearchButton.setFocusPainted(false);
		
		advancedSearchButton.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e) {
						advancedSearch();
						ResultsManager resultsManager = new ResultsManager(results);
						numOfResultsLabel.setText("Found "+numTotalHits+" results");
						resultsManager.showResults();
						frame.dispose();
						
						
					}
			
				});
		
		frame.getRootPane().setDefaultButton(advancedSearchButton);
		advancedSearchButton.requestFocus();
		
		frame.getContentPane().add(advancedSearchButton);
		
		
		
	}
	
	private void advancedSearch() {
		handleInputs();
		
	}


	private void handleInputs() {
		
		ArrayList<String[]> inputs = new ArrayList<String[]>();
		String titleInput = titleTextField.getText().trim();
		String[] title = {"title",titleInput};
		inputs.add(title);
		
		String directorInput = directorTextField.getText().trim();
		String[] director = {"director",directorInput};
		inputs.add(director);
		
		String originInput = originTextField.getText().trim();
		String[] origin = {"origin",originInput};
		inputs.add(origin);
		
		String genreInput = genreTextField.getText().trim();
		String[] genre = {"genre",genreInput};
		inputs.add(genre);
		
		String yearInput = yearTextField.getText().trim();
		String[] year = {"releaseDate",yearInput};
		inputs.add(year);
		
		String castInput = castTextField.getText().trim();
		String[] cast = {"cast",castInput};
		inputs.add(cast);
		
		String ratingInput = ratingTextField.getText().trim();
		String[] rating = {"rating",ratingInput};
		inputs.add(rating);
		
		boolean[] fieldColors = checkFields();
		
		buildQuery(inputs,fieldColors);
		
	}


	private boolean[] checkFields() {
		boolean[] fieldColors = new boolean[7];
		if(titleTextField.getForeground()==Color.black) {
			fieldColors[0]=true;
		}
		if(directorTextField.getForeground()==Color.black) {
			fieldColors[1]=true;
		}
		if(originTextField.getForeground()==Color.black) {
			fieldColors[2]=true;
		}
		if(genreTextField.getForeground()==Color.black) {
			fieldColors[3]=true;
		}
		if(yearTextField.getForeground()==Color.black) {
			fieldColors[4]=true;
		}
		if(castTextField.getForeground()==Color.black) {
			fieldColors[5]=true;
		}
		if(ratingTextField.getForeground()==Color.black) {
			fieldColors[6]=true;
		}
		return fieldColors;
		
	}

	private void buildQuery(ArrayList<String[]> inputs, boolean[] fieldColors) {
		Builder queryBuilder = new BooleanQuery.Builder();
		for(int i=0; i<7; i++) {
			String[] input = inputs.get(i);
			if(!input[1].equals("") && fieldColors[i]) {
				
				try {
					Query query = new QueryParser(input[0], analyzer).parse(input[1]);
					
					BooleanClause bc = new BooleanClause(query,Occur.MUST);
					queryBuilder.add(bc);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
		}
		BooleanQuery boolQuery = queryBuilder.build();
		createSearchHistory(boolQuery.toString());
		
		String sortType = "";
		if(yearRadioButton.isSelected()) {
			sortType="releaseDate";
		}
		else if(ratingRadioButton.isSelected()) {
			sortType="rating";
		}
		else {
			sortType="default";
		}
		
		pagingSearch(boolQuery,sortType);
		
	}


	private void pagingSearch(BooleanQuery boolQuery, String sortType) {
		try {
			TopDocs results;
			if(sortType.equals("releaseDate")) {
				Sort sorted = new Sort(new SortField(sortType,Type.STRING,true),SortField.FIELD_SCORE);
				results = searcher.search(boolQuery, 5 * hitsPerPage, sorted);
			}
			else if(sortType.equals("rating")) {
				Sort sorted = new Sort(new SortField(sortType,Type.STRING,true),SortField.FIELD_SCORE);
				results = searcher.search(boolQuery, 5 * hitsPerPage, sorted);
			}
			else {
				Sort sorted = new Sort(SortField.FIELD_SCORE,new SortField("rating",Type.STRING,true)
						,new SortField("ratingCount",Type.STRING,true));
				results = searcher.search(boolQuery, 5 * hitsPerPage, sorted);
			}
			
			ScoreDoc[] hits = results.scoreDocs;
			numTotalHits = Math.toIntExact(results.totalHits.value);
			
			end = Math.min(numTotalHits, hitsPerPage);
			
			findPages(hits, boolQuery);
			
		} catch (Exception e) {
			System.out.println(" caught a " + e.getClass() +
					"\n with message: " + e.getMessage());
		}
		
	}


	private void findPages(ScoreDoc[] hits, BooleanQuery boolQuery) {
		

		//Uses HTML &lt;B&gt;&lt;/B&gt; tag to highlight the searched terms
        Formatter formatter = new SimpleHTMLFormatter();
         
        //It scores text fragments by the number of unique query terms found
        //Basically the matching score in layman terms
        QueryScorer scorer = new QueryScorer(boolQuery);
         
        //used to markup highlighted terms found in the best sections of a text
        Highlighter highlighter = new Highlighter(formatter, scorer);
         
        //It breaks text up into same-size texts but does not split up spans
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 10);

        highlighter.setTextFragmenter(fragmenter);
		
		int pages = numTotalHits/hitsPerPage+1;
		for(int i=0; i<=pages; i++) {
			Page page = new Page();
			end = Math.min(hits.length, start + hitsPerPage);
			for(int j=start; j<end;j++) {
				try {
					Document doc = searcher.doc(hits[j].doc);
					page.addResult(doc);
					for(String field : fields) {
						@SuppressWarnings("deprecation")
						TokenStream stream = TokenSources.getAnyTokenStream(reader, hits[j].doc, field, analyzer);
			            String[] frags = highlighter.getBestFragments(stream, doc.get(field), 10);
			            page.highlightResult(field,frags);
			            
					}
					
				} catch (Exception e) {
					System.out.println(" caught a " + e.getClass() +
							"\n with message: " + e.getMessage());
				}
			}
			if(start+hitsPerPage<numTotalHits) {
				start+=hitsPerPage;
			}
			if(pages>end/10+1) {
				try {
					hits = searcher.search(boolQuery, numTotalHits).scoreDocs;
				} catch (Exception e) {
					System.out.println(" caught a " + e.getClass() +
							"\n with message: " + e.getMessage());
				}
				end=hits.length;
			}
			results.add(page);
		}
		
	}
	
	public ArrayList<Page> getResults() {
		return results;
	}

}
