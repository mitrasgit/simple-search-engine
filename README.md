__Simple Search Engine__

__How to use__  
Per default, the search engine will index all files in the db folder.  
To select a subset of the files, use the terminal and type for example:  
_select doc1.txt doc2.txt doc3.txt_

Search for a word in the index:  
_get fox_

Add a new document to the db folder and the index:  
_add filename.txt some example text_

Terminate the search engine:  
_exit_


__The project consists of...__  
_DB_  
Document 1: “the brown fox jumped over the brown dog”  
Document 2: “the lazy brown dog sat in the corner”  
Document 3: “the red fox bit the lazy dog”  
...

_MAIN_  
Interface with the user, uses a Lexer to tokenize the input an construct the relevant query

_LEXER_  
Tokenize a line of input according to the grammar:  
begin with ADD, SELECT, GET, EXIT  
FILENAME = filename.txt  
WORD = \w+(([\-\_]\w+)+)*  
PERIOD = [\.\!\?]

_QUERY_  
Object that holds the request from the user, allowed queries are:  
ADD FILENAME WORD+  
SELECT FILENAME+  
GET WORD  
EXIT

_INVERTED INDEX_  
Hashmap datastructure that maps a token to a list of documents
- Calulates idf  
- For each term, store a list of documents as a DocumentList

_DOCUMENTLIST_  
Interface for a list of documents associated with a term (word token)

_TFDOCUMENTLIST_  
A DocumentList that stores tf scores

_TFIDFDOCUMENTLIST_  
A DocumentList that stores tf-idf scores

_SEARCH ENGINE_  
Interface between Main, Queries, InvertedIndex and the data base (db folder)


_TF-IDF_  
TF: (term frequency) the number of times that term t occurs in document d  
	tf = for d; if d contains t; sum+=1

IDF: (inverse document frequency) inverse fraction of the documents that contain the term  
	idf = log( |N| / |N.contains(t)| )  
 	and when we need to avoid division by zero:  
	idf = log ( |N| / 1+|N.contains(t)| )


1. Man vill kunna lägga till nya dokument, och då behålla stora/små bokstäver, siffror, och tecken i databasen.
Därför används inte tokens för själva texten som ska sparas.  
MEN när man laddar dokumenten till index så behöver de vara kompatibla med Query-tokens, så innan indexering så
tokeniseras dokumenten!


