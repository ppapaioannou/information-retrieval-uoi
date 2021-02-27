package search;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;

import results.Page;

public class SimpleSearcher {

	private String searchInput;
	
	private IndexReader reader;
	private IndexSearcher searcher;
	private EnglishAnalyzer analyzer;

	private String[] fields = {"releaseDate","title","origin","director","cast","genre","rating"};
	
	private int hitsPerPage = 10;
	
	private int start = 0;
	private int end = 0;
	
	private int numTotalHits;
	
	private ArrayList<Page> results = new ArrayList<Page>();

	public SimpleSearcher(String searchInput, IndexReader reader, IndexSearcher searcher, EnglishAnalyzer analyzer) {
		this.searchInput=searchInput;
		this.reader=reader;
		this.searcher=searcher;
		this.analyzer=analyzer;
	}

	public void pagingSearch() {

		MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"title", "releaseDate", "origin","cast","director", "rating"}, analyzer);
		
		try {
			Query query = parser.parse(searchInput);
			
			createSearchHistory(searchInput);
			
			Sort sorted = new Sort(SortField.FIELD_SCORE,new SortField("rating",Type.STRING,true)
					,new SortField("ratingCount",Type.STRING,true));
			
			TopDocs results = searcher.search(query, 5 * hitsPerPage, sorted);

			ScoreDoc[] hits = results.scoreDocs;
			
			numTotalHits = Math.toIntExact(results.totalHits.value);
			
			end = Math.min(numTotalHits, hitsPerPage);
			
			currentPage(hits,query);		
			
		} catch (Exception e) {
			System.out.println(" caught a " + e.getClass() +
					"\n with message: " + e.getMessage());
		}
		
	}

	public void createSearchHistory(String line) {
		try {
            BufferedWriter out = new BufferedWriter( 
                   new FileWriter("history.txt", true)); 
            out.write(line);
            out.newLine();
            out.close(); 
		} catch (Exception e) {
			System.out.println(" caught a " + e.getClass() +
					"\n with message: " + e.getMessage());
		}
	}

	private void currentPage(ScoreDoc[] hits, Query query) {
		
		//Uses HTML &lt;B&gt;&lt;/B&gt; tag to highlight the searched terms
        Formatter formatter = new SimpleHTMLFormatter();
         
        //It scores text fragments by the number of unique query terms found
        //Basically the matching score in layman terms
        QueryScorer scorer = new QueryScorer(query);
         
        //used to markup highlighted terms found in the best sections of a text
        Highlighter highlighter = new Highlighter(formatter, scorer);
         
        //It breaks text up into same-size texts but does not split up spans
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 10);
         
        highlighter.setTextFragmenter(fragmenter);
		
		int pages = numTotalHits/10+1;
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
					hits = searcher.search(query, numTotalHits).scoreDocs;
				} catch (Exception e) {
					System.out.println(" caught a " + e.getClass() +
							"\n with message: " + e.getMessage());
				}
				end=hits.length;
			}
			results.add(page);
		}
		
	}

	public String getTotalHits() {
		return String.valueOf(numTotalHits);
	}

	public ArrayList<Page> getResultPages() {
		return results;
	}

}
