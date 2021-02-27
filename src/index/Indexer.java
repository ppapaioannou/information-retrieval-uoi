package index;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import com.opencsv.CSVReader;

public class Indexer {
	
	
	public static void main(String[] args) {
		
		String indexPath = "index";
		
		String docsPath = "complete.csv";
		
		boolean create = true;
		
		Path docDir = Paths.get(docsPath);
		if (!Files.isReadable(docDir)) {
			System.out.println("Document directory '" +docDir.toAbsolutePath()+ "' does not exist or is not readable, please check the path");
			System.exit(1);
		}
		
		try {
			System.out.println("Indexing to directory '" + indexPath + "'...");
			
			Directory dir = FSDirectory.open(Paths.get(indexPath));
		    Analyzer analyzer = new EnglishAnalyzer(); //built in stemmer
		    IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		    
		    if (create) {
		    	// Create a new index in the directory, removing any
		    	// previously indexed documents:
		    	iwc.setOpenMode(OpenMode.CREATE);
		    }
		    else {
		    	// Add new documents to an existing index:
		    	iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		    }
		    
		    IndexWriter writer = new IndexWriter(dir, iwc);
		    indexDocs(writer, docDir);

		    
		    writer.close();
		}
		catch (IOException e) {
			System.out.println(" caught a " + e.getClass() +
					"\n with message: " + e.getMessage());
		}
	    
	}

	private static void indexDocs(IndexWriter writer, Path docDir) {
		int line_counter = 0;

        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(docDir.toString()));
            String[] nextLine;
            reader.readNext();
			while ((nextLine = reader.readNext()) != null)  { 

				String releaseDate = nextLine[0];
				String title = nextLine[1];				
				String origin = nextLine[2];
				String director = nextLine[3];
				String cast = nextLine[4];
				String genre =  nextLine[5];
				String url = nextLine[6];
				String rating = nextLine[8];
				if(rating.equals("Unknown")) {
					rating="-1";
				}
				String ratingCount = nextLine[9];
					
				Document doc = new Document();
				
				doc.add(new StringField("releaseDate", releaseDate, Field.Store.YES));
				doc.add(new SortedDocValuesField("releaseDate", new BytesRef(releaseDate)));
				doc.add(new TextField("title", title, Field.Store.YES));
				doc.add(new TextField("origin", origin, Field.Store.YES));
				doc.add(new TextField("director", director, Field.Store.YES));
				doc.add(new TextField("cast", cast, Field.Store.YES));
				doc.add(new TextField("genre", genre, Field.Store.YES));
				doc.add(new StoredField("url",url));//store gia na anoigei to link
				doc.add(new StringField("rating",rating,Field.Store.YES));//store for katataksi
				doc.add(new SortedDocValuesField("rating", new BytesRef(rating)));
				doc.add(new StoredField("ratingCount",ratingCount));//store for katataksi
				doc.add(new SortedDocValuesField("ratingCount", new BytesRef(ratingCount)));
				
				writer.addDocument(doc);

				line_counter+=1;
			}
			System.out.println("Processed "+line_counter+1+" lines.");
        } 
        catch (IOException e) {
        	System.out.println(" caught a " + e.getClass() +
					"\n with message: " + e.getMessage());
        }
        
	}
	
}
