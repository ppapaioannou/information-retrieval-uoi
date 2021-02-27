package results;

import java.util.ArrayList;

import org.apache.lucene.document.Document;


public class Page {
	
	private ArrayList<Result> results = new ArrayList<Result>();
	
	public void addResult(Document doc) {
		Result res = new Result(doc);
		
		results.add(res);
	}
	
	public void highlightResult(String field, String[] frags) {
		Result res = results.get(results.size()-1);
		res.update(field, frags);
		
	}
	
	public ArrayList<Result> getResults() {
		return results;
	}

	

}
