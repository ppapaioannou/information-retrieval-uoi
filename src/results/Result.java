package results;

import org.apache.lucene.document.Document;

public class Result {
	
	private String releaseDate;
	private String title;			
	private String origin;
	private String director;
	private String cast;
	private String genre;
	private String url;
	private String rating;
	private String ratingCount;

	
	public Result(Document doc) {
		releaseDate=doc.get("releaseDate");
		title=doc.get("title");
		origin=doc.get("origin");
		director=doc.get("director");
		cast=doc.get("cast");
		genre=doc.get("genre");
		url=doc.get("url");
		rating=doc.get("rating");	
		ratingCount=doc.get("ratingCount");
	}
	
	public String getURL() {
		return url;
	}
	
	public String getHypelinkText() {
		return title+" ("+releaseDate+")";
	}
	
	public String getResultInfo() {		 
		return "Origin: "+origin+", Director/s: "+director+" <br/>Genre: "+genre+" <br/>Cast: "+cast
				+" <br/>Imdb rating: "+rating+"/10 from "+ratingCount+" votes";
	}
	
	

	public void update(String field,String frags[]) {
		String update = "";
		for(String x : frags) {
			update+=x;
		}
		
		if(!update.equals("")) {
			if(field.equals("releaseDate")) {
				releaseDate=update;
			}
			else if(field.equals("title")) {
				title=update;
			}
			else if(field.equals("director")) {
				director=update;
			}
			else if(field.equals("cast")) {
				cast=update;
			}
			else if(field.equals("genre")) {
				genre=update;
			}
			
			else if(field.equals("origin")) {
				origin=update;
			}
			
			else if(field.equals("rating")) {
				rating=update;
			}
		}
		
	}
	

}
