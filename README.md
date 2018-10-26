# Easy Content to Metadata

Why this Alfresco extension? It's goal is to give a non developer the ability to fill metadata fields with data extracted from preselected areas on documents. He can indicate for a specific document type where to find metadata and to which field it is linked.
For example: your invoices have the same information (let's say total amount) 
* always in a certain area
* correspond to a specific regex 

then this extension can extract the amount and feed it to the invoiceAmount field.

## How to use it
So how does it work:
* After being installed, login to Alfresco and go to the "Metadata Extraction Information" site
* Choose the "Data lists" menu, then "Extractor Mapping Datalist" and click on " New Item"
* In the modal view select first the type of document you want to extract the data
* Then select the property of the document type to whom the data will be extracted.
* Choose which way the metadata will be extracted.
* Indicate coordinates or the regex to indicate where to find the metadata.

## How to get it
If you want to use it directly, you can download the release. 
Otherwise clone this repo, do your modifications and 
run with `mvn clean install -DskipTests=true alfresco:run` or `./run.sh` and verify that it works.

 
## Few things to notice

 * Works for the moment only on text pdfs
 * Since it's developed with SDK 3.0 it should be compatible from 4.2 to 5.2
  
## Leftovers
 
   * I think a video explaining the usage would be interesting
   * Check out the project page and add a point if needed:
   https://github.com/sgirardin/easyContent2Metadata/projects/1
  
 
