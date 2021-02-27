import urllib.request
from bs4 import BeautifulSoup
import csv


def find_imdb_rating(url):
    print(url)
    try:
        wiki_link = urllib.request.urlopen(url)
        wiki_html = wiki_link.read()
        wiki_link.close()
        wiki_soup = BeautifulSoup(wiki_html, 'html.parser')
    except:
        print("Trouble with wiki_link")
        return("Unknown","Unknown")


    try:
        
        a_imdb_link=wiki_soup.select("a[href*=imdb]")[-1] #find the final imdb link
        href_imdb_link=a_imdb_link['href'] #find the href tag for the real url
        print(href_imdb_link)
    except:
        print("Trouble with imdb_link inside wikipedia")
        return("Unknown","Unknown")



    try:
        imdb_link = urllib.request.urlopen(href_imdb_link)
        imdb_html = imdb_link.read()
        imdb_link.close()
        imdb_soup = BeautifulSoup(imdb_html, 'html.parser')
        

        stats = imdb_soup.find("div", {"class": "imdbRating"})
        if(stats==None):
            return("Unknown","Unknown")
        else:
            ratingValue=stats.text[2:5]
            ratingCount=stats.a.text
            print("collected rating")
            return(ratingValue,ratingCount)
    except:
        print("Trouble while colecting stats")
        return("Unknown","Unknown")

def writeFirstLine(row):
    print("writing first line to "+new_db)
    with open(new_db, 'a', newline='', encoding='utf-8') as csv_file:
        # Create a csv.writer object from the output file object
        csv_writer = csv.writer(csv_file, delimiter=',')
        row.append("Rating Value")
        row.append("Rating Count")
        csv_writer.writerow(row)
        print("done\n")


def writeCVS(row,ratingValue,ratingCount):
    print("now writing to "+new_db)
    with open(new_db, 'a', newline='', encoding='utf-8') as csv_file:
        # Create a csv.writer object from the output file object
        csv_writer = csv.writer(csv_file, delimiter=',')
        row.append(ratingValue)
        row.append(ratingCount)
        csv_writer.writerow(row)
        print("done\n")


def readCVS(start,limit):
    with open(prev_db, newline='', encoding='utf-8') as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        line_count = 0
        for row in csv_reader:
            print("processing item "+str(line_count))
            if start>line_count:
                print(line_count)
                line_count+=1
            else:
                if line_count==limit:
                    break
                else:    
                    if line_count == 0:
                        writeFirstLine(row)
                        line_count += 1
                    else:
                        (ratingValue,ratingCount)=find_imdb_rating(row[6])
                        writeCVS(row,ratingValue,ratingCount)
                        line_count += 1
        print("Processed "+str(line_count)+" lines.")

prev_db = 'wiki_movie_plots_deduped.csv'
# how many movies to check
start = 0
end = 10000
new_db = str(start)+'_'+str(end)+'.csv'

readCVS(start,end)

