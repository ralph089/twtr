# TWTR

TWTR ermöglicht dem Anwender ontologiegestützt nach Tweets zu suchen. Dazu werden Tweets aus einer Datenbank mit zusätzlichen Informationen hinterlegt und in einem RDF-Graphen gespeichert. Mittels einer Webapplikation können diese Informationen anschließend abgefragt werden.

## RDF Vokabular
<img src="http://svgshare.com/i/1s0.svg">

## Quickstart
In der Kommandozeile folgendes eingeben, um das Projekt zu bauen.
```
mvn package
```
Anschließend kann die erzeugte .jar-Datei ausgeführt werden.

## Beispiel
Aus einer CSV-Datei wird folgender Datensatz eingelesen:

```csv
81935964658798592;213339721;2362;253796;10240;4012;11585;Online;IT Knowingness;213339721;Latest news and trends in IT and Computer Science;Donald Trump is President of America! Make America Great Again
```

#### XML-Struktur
Daraus wird mittels Jena und verschiedenen Taggern folgender RDFS-Graph erzeugt:

```xml
<rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:twtr="http://example.org/"
    xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema#">
  <twtr:TwitterAccount rdf:about="http://example.org/213339721">
    <twtr:tweeted>
      <twtr:Tweet rdf:about="http://example.org/Tweet/81935964658798592">
        <twtr:hasKeyword>
          <rdf:Bag>
            <rdf:li>Donald Trump</rdf:li>
            <rdf:li>President</rdf:li>
            <rdf:li>America</rdf:li>
          </rdf:Bag>
        </twtr:hasKeyword>
        <twtr:contains>
          <twtr:POS rdf:about="http://example.org/Tweet/81935964658798592/POS/">
            <rdfs:subClassOf rdf:resource="http://example.org/Tweet/81935964658798592/POS/"/>
          </twtr:POS>
        </twtr:contains>
        <twtr:tweetText>Donald Trump is President of America! Make America Great Again</twtr:tweetText>
        <twtr:tweetID>81935964658798592</twtr:tweetID>
      </twtr:Tweet>
    </twtr:tweeted>
    <twtr:userDescription>Latest news and trends in IT and Computer Science</twtr:userDescription>
    <twtr:followerCount rdf:datatype="http://www.w3.org/2001/XMLSchema#int"
    >10240</twtr:followerCount>
    <twtr:userID>213339721</twtr:userID>
    <twtr:userName>IT Knowingness</twtr:userName>
  </twtr:TwitterAccount>
  <twtr:Triplet rdf:nodeID="A0">
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/81935964658798592/POS/"/>
    <rdfs:subClassOf rdf:nodeID="A0"/>
  </twtr:Triplet>
  <twtr:ProperNoun rdf:nodeID="A1">
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/81935964658798592/POS/"/>
    <twtr:word>Great</twtr:word>
    <rdfs:subClassOf rdf:nodeID="A1"/>
  </twtr:ProperNoun>
  <twtr:Entity rdf:nodeID="A2">
    <twtr:entityName>Location</twtr:entityName>
    <rdf:type rdf:resource="http://example.org/ProperNoun"/>
    <rdfs:subClassOf rdf:nodeID="A0"/>
    <twtr:word>President of America</twtr:word>
    <rdf:type rdf:resource="http://example.org/Object"/>
    <rdfs:subClassOf rdf:nodeID="A2"/>
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/81935964658798592/POS/"/>
  </twtr:Entity>
  <twtr:Verb rdf:nodeID="A3">
    <rdfs:subClassOf rdf:nodeID="A0"/>
    <twtr:word>be</twtr:word>
    <rdfs:subClassOf rdf:nodeID="A3"/>
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/81935964658798592/POS/"/>
  </twtr:Verb>
  <twtr:Entity rdf:nodeID="A4">
    <twtr:entityName>Person</twtr:entityName>
    <rdf:type rdf:resource="http://example.org/ProperNoun"/>
    <rdfs:subClassOf rdf:nodeID="A0"/>
    <twtr:word>Donald Trump</twtr:word>
    <rdf:type rdf:resource="http://example.org/Subject"/>
    <rdfs:subClassOf rdf:nodeID="A4"/>
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/81935964658798592/POS/"/>
  </twtr:Entity>
  <twtr:Entity rdf:nodeID="A5">
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/81935964658798592/POS/"/>
    <twtr:entityName>Location</twtr:entityName>
    <twtr:word>America</twtr:word>
    <rdf:type rdf:resource="http://example.org/ProperNoun"/>
    <rdfs:subClassOf rdf:nodeID="A5"/>
  </twtr:Entity>
</rdf:RDF>
```

#### Beispielabfrage
Mittels der SPARQL-Anfragesprache lassen sich nun die Daten abfragen. 

In diesem Fall lassen wir uns alle Proper Nouns anzeigen, die auch gleichzeitig Subjekt sind. 

Durch einen RDFS-Reasoner kann Jena schlussfolgern, dass auch Triplets Teil der Part-Of-Speech (POS)-Klasse sind (siehe Graph).
 Dadurch werden auch Proper Nouns gefunden, die nicht unmittelbar in der Part-Of-Speech Klasse sind, sondern sich in einer Subklasse (Triplets) befinden.

```sparql
PREFIX  rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX  : <http://example.org/>

SELECT  ?tweetText ?properNoun
WHERE
  { ?tweet    :contains         ?pos ;
    	 	  :tweetText 		?tweetText .
    ?element  rdfs:subClassOf   ?pos ;
              a                 :ProperNoun ;
              a                 :Subject ;
              :word             ?properNoun
  }
```

#### Rückgabe
```
| tweetText                                                      | properNoun            |
|----------------------------------------------------------------|-----------------------|
| Donald Trump is President of America! Make America Great Again | "Donald Trump"        |
```

## Zusätzliche Informationen
Die Tweets werden mit folgenden zusätzlichen Informationen hinterlegt:

#### Triplet (Subjekt-Verb-Objekt)-Tagging

Derzeit sind für diesen Zweck drei verschiedene Varianten von Taggern eingebaut.

* IBM Watson Natural Language Understanding Tagger
* Stanford OpenIE Tagger
* Stanford Core Tagger (zur Zeit nur im StanfordTripletTagger Branch nutzbar)

Um den Watson Tagger zu nutzen, werden entsprechende IBM Watson Cloud Credentials mit aktivierter Natural Language Understanding API benötigt. Die Zugangsdaten müssen dann in den `bluemix.properties` hinterlegt werden.

Bei einer ersten Evaluierung erzielte der Watson Natural Language Understanding Tagger die besten Ergebnisse.

#### Part-of-Speech (POS)-Tagging

Neben dem Triplet-Tagging wird außerdem noch die Wortart ermittelt. Derzeit werden folgende Wortarten hinterlegt:

* Common Nouns
* Proper Nouns
* Adjektive

Dazu wird derzeit Stanford NLP mit dem Gate-Twitter-Modell verwendet.

#### Kategorisierung von Eigennamen (Named Entity Recognition)
Bei der Kategorisierung von Eigennamen wird der Autor einer der folgenden drei Kategorien zugeordnet:

* Person
* Organization
* Place

Die Proper Nouns werden einer der folgenden Kategorien zugeordnet: 

https://www.ibm.com/watson/developercloud/doc/natural-language-understanding/entity-types.html

Die Kategorisierung von Eigennamen erfolgt derzeit über die Stanford Named Entity Recognition (NER) API für Autoren und über die Watson NLU API für Proper Nouns.

#### Stichwörter (Keywords)
Die Keywords werden über die Watson Natural Language Understanding API abgerufen.


## Tasks:

- [x] Auswahl und Evaluierung eines Subjekt-Verb-Objekt-Taggers (Goldstandard).
- [x] Anwendung der Stanford Named Entity Recognition.
- [x] Anwendung der Watson Named Entity Recognition.
- [x] Anwendung der Watson Keyword Extraction.
- [x] Prototypische Implementierung einer RDF-Zielstruktur mit Jena + (Fuseki).
- [X] Überführung der Twitterdaten in den RDF-Triplestore anhand der gegebenen Zielstruktur.
- [ ] Entwicklung eines Abfrageservices auf Basis von SPARQL und Angular.

## Hinweise zum Start der Umgebung

Zur persistenten Speicherung der RDF-Graphen und zur Abfrage der Daten über HTTP wird Apache Jena Fuseki eingesetzt. Die Webapplikation zur Abfrage der Daten läuft auf einem nginx Webserver.

### Starten des Servers mittels Docker

Um den Server zu starten, wird Docker benötigt. Anschließend erfolgt der Start des Servers durch Eingabe der folgenden Kommandos in ein Kommandozeilenfenster:
```bash
cd twtr
docker-compose up
```
Anschließend startet der vorkonfigurierte Apache Jena Fuseki und ein Webserver.


* **Fuseki:** Über http://localhost:3030/ kann auf das interne Fuseki Webinterface zugegriffen werden (user: admin - pw: admin).
* **Webapplikation:** Über http://localhost/ kann auf die fertige Angular Webapplikation zugegriffen werden. Intern werden die Daten aus dem Verzeichnis angular-app/dist an den Docker-Container weitergeleitet. Für Entwicklungszwecke empfiehlt es sich, einen eigenen NPM-Webserver zu starten. Mehr Infos zur Entwicklung der Angular Webapplikation finden sich [hier](angular-app/README.md).

