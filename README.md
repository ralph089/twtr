# TWTR

TWTR ermöglicht dem Anwender ontologiegestützt nach Tweets zu suchen. Dazu werden Tweets aus einer Datenbank mit zusätzlichen Informationen hinterlegt und in einem RDF-Graphen gespeichert. Mittels einer Webapplikation können diese Informationen anschließend abgefragt werden.

## RDF Vokabular
<img src="http://svgshare.com/i/1UR.svg">

## Beispiel
Aus einer CSV-Datei wird folgender Datensatz eingelesen:

```csv
781937602936963072;17158660;3096;15625;2415;2425;120;Virtually Everywhere;Jett Ray;17158660;Learning Leader, Soul Gardener, Conscious Wanderer. http://JettJournal.com ~  We are stronger, together. Peace/Love/Coffee ✌ ❤ ☕;RT @24k: #IoT Gets Social: How Our Connected Lives Will Change #SocialMedia https://t.co/LaToDF8BNa MT @socialmedia2day https://t.co/Mudz0v…
```

#### XML-Struktur
Daraus wird mittels Jena und verschiedenen Taggern folgender RDFS-Graph erzeugt:

```xml
<rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:twtr="http://example.org/"
    xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema#">
  <twtr:Person rdf:about="http://example.org/17158660">
    <twtr:tweeted>
      <twtr:Tweet rdf:about="http://example.org/Tweet/781937602936963072">
        <twtr:contains>
          <twtr:POS rdf:about="http://example.org/Tweet/781937602936963072/POS/">
            <rdfs:subClassOf rdf:resource="http://example.org/Tweet/781937602936963072/POS/"/>
          </twtr:POS>
        </twtr:contains>
        <twtr:tweetText>RT @24k: #IoT Gets Social: How Our Connected Lives Will Change #SocialMedia https://t.co/LaToDF8BNa MT @socialmedia2day https://t.co/Mudz0v…</twtr:tweetText>
        <twtr:tweetID>781937602936963072</twtr:tweetID>
      </twtr:Tweet>
    </twtr:tweeted>
    <twtr:userDescription>Learning Leader, Soul Gardener, Conscious Wanderer. http://JettJournal.com ~  We are stronger, together. Peace/Love/Coffee ✌ ❤ ☕</twtr:userDescription>
    <twtr:followerCount rdf:datatype="http://www.w3.org/2001/XMLSchema#int"
    >2415</twtr:followerCount>
    <twtr:userID>17158660</twtr:userID>
    <twtr:userName>Jett Ray</twtr:userName>
    <rdf:type rdf:resource="http://example.org/TwitterAccount"/>
  </twtr:Person>
  <twtr:Triplet rdf:nodeID="A0">
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/781937602936963072/POS/"/>
    <rdfs:subClassOf rdf:nodeID="A0"/>
  </twtr:Triplet>
  <twtr:Triplet rdf:nodeID="A1">
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/781937602936963072/POS/"/>
    <rdfs:subClassOf rdf:nodeID="A1"/>
  </twtr:Triplet>
  <twtr:Triplet rdf:nodeID="A2">
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/781937602936963072/POS/"/>
    <rdfs:subClassOf rdf:nodeID="A2"/>
  </twtr:Triplet>
  <twtr:Subject rdf:nodeID="A3">
    <rdfs:subClassOf rdf:nodeID="A0"/>
    <twtr:word>Our Connected Lives</twtr:word>
    <rdfs:subClassOf rdf:nodeID="A3"/>
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/781937602936963072/POS/"/>
  </twtr:Subject>
  <twtr:Object rdf:nodeID="A4">
    <rdfs:subClassOf rdf:nodeID="A1"/>
    <twtr:word>SocialMedia MT socialmedia2day</twtr:word>
    <rdfs:subClassOf rdf:nodeID="A4"/>
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/781937602936963072/POS/"/>
  </twtr:Object>
  <twtr:Verb rdf:nodeID="A5">
    <rdfs:subClassOf rdf:nodeID="A1"/>
    <twtr:word>Will Change</twtr:word>
    <rdfs:subClassOf rdf:nodeID="A5"/>
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/781937602936963072/POS/"/>
  </twtr:Verb>
  <twtr:Subject rdf:nodeID="A6">
    <rdfs:subClassOf rdf:nodeID="A1"/>
    <twtr:word>Our Lives</twtr:word>
    <rdfs:subClassOf rdf:nodeID="A6"/>
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/781937602936963072/POS/"/>
  </twtr:Subject>
  <twtr:ProperNoun rdf:nodeID="A7">
    <rdfs:subClassOf rdf:nodeID="A2"/>
    <twtr:word>Social</twtr:word>
    <rdf:type rdf:resource="http://example.org/Object"/>
    <rdfs:subClassOf rdf:nodeID="A7"/>
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/781937602936963072/POS/"/>
  </twtr:ProperNoun>
  <twtr:Verb rdf:nodeID="A8">
    <rdfs:subClassOf rdf:nodeID="A2"/>
    <twtr:word>Gets</twtr:word>
    <rdfs:subClassOf rdf:nodeID="A8"/>
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/781937602936963072/POS/"/>
  </twtr:Verb>
  <twtr:ProperNoun rdf:nodeID="A9">
    <rdfs:subClassOf rdf:nodeID="A2"/>
    <twtr:word>IoT</twtr:word>
    <rdf:type rdf:resource="http://example.org/Subject"/>
    <rdfs:subClassOf rdf:nodeID="A9"/>
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/781937602936963072/POS/"/>
  </twtr:ProperNoun>
  <twtr:CommonNoun rdf:nodeID="A10">
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/781937602936963072/POS/"/>
    <twtr:word>Lives</twtr:word>
    <rdfs:subClassOf rdf:nodeID="A10"/>
  </twtr:CommonNoun>
  <twtr:ProperNoun rdf:nodeID="A11">
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/781937602936963072/POS/"/>
    <twtr:word>socialmedia2day</twtr:word>
    <rdfs:subClassOf rdf:nodeID="A11"/>
  </twtr:ProperNoun>
  <twtr:ProperNoun rdf:nodeID="A12">
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/781937602936963072/POS/"/>
    <twtr:word>MT</twtr:word>
    <rdfs:subClassOf rdf:nodeID="A12"/>
  </twtr:ProperNoun>
  <twtr:ProperNoun rdf:nodeID="A13">
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/781937602936963072/POS/"/>
    <twtr:word>SocialMedia</twtr:word>
    <rdfs:subClassOf rdf:nodeID="A13"/>
  </twtr:ProperNoun>
  <twtr:Adjective rdf:nodeID="A14">
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/781937602936963072/POS/"/>
    <twtr:word>Connected</twtr:word>
    <rdfs:subClassOf rdf:nodeID="A14"/>
  </twtr:Adjective>
  <twtr:Object rdf:nodeID="A15">
    <rdfs:subClassOf rdf:nodeID="A0"/>
    <twtr:word>SocialMedia MT socialmedia2day</twtr:word>
    <rdfs:subClassOf rdf:nodeID="A15"/>
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/781937602936963072/POS/"/>
  </twtr:Object>
  <twtr:Verb rdf:nodeID="A16">
    <rdfs:subClassOf rdf:nodeID="A0"/>
    <twtr:word>Will Change</twtr:word>
    <rdfs:subClassOf rdf:nodeID="A16"/>
    <rdfs:subClassOf rdf:resource="http://example.org/Tweet/781937602936963072/POS/"/>
  </twtr:Verb>
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

SELECT  ?properNoun
WHERE
  { ?tweet    :contains         ?pos .
    ?element  rdfs:subClassOf   ?pos ;
              a                 :ProperNoun ;
              a                 :Subject ;
              :word             ?properNoun
  }
```

#### Rückgabe
```
| properNoun        |
|-------------------|
| "IoT"             |
```

## Zusätzliche Informationen
Die Tweets werden mit folgenden zusätzlichen Informationen hinterlegt:

#### Subjekt-Verb-Objekt Tagging

Derzeit sind für diesen Zweck drei verschiedene Varianten von Taggern eingebaut, die noch evaluiert werden müssen:

* IBM Watson Natural Language Understanding Tagger
* Stanford OpenIE Tagger
* Stanford Core Tagger (zur Zeit nur im StanfordTripletTagger Branch nutzbar)

Um den Watson Tagger zu nutzen, werden entsprechende IBM Watson Cloud Credentials mit aktivierter Natural Language Understanding API benötigt. Die Zugangsdaten müssen dann in den `bluemix.properties` hinterlegt werden.

#### POS-Tagging

Neben dem Subjekt-Verb-Objekt Tagging wird außerdem noch die Wortart ermittelt. Derzeit werden folgende Wortarten hinterlegt:

* Common Nouns
* Proper Nouns
* Adjektive

Dazu wird derzeit Stanford NLP mit dem Gate-Twitter-Modell verwendet.

#### Kategorisierung von Eigennamen 
Bei der Kategorisierung von Eigennamen wird der Autor und die Proper Nouns einer der folgenden drei Kategorien zugeordnet:

* Person
* Organization
* Place

Die Kategorisierung von Eigennamen erfolgt derzeit über die Stanford Named Entity Recognition (NER) API. Zusätzlich soll noch eine Anbindung an die Watson NLU API erfolgen.

#### Keywords
(Keyword-Extraction ...)

## Tasks:

- [ ] Auswahl und Evaluierung eines Subjekt-Verb-Objekt-Taggers (Goldstandard).
- [x] Anwendung der Stanford Named Entity Recognition.
- [ ] Anwendung der Watson NLU Keyword-Extraction + Named Entity Recognition.
- [x] Prototypische Implementierung einer RDF-Zielstruktur mit Jena + (Fuseki).
- [ ] Entwicklung eines Abfrageservices auf Basis von SPARQL und Angular.
- [ ] Überführung der Twitterdaten in den RDF-Triplestore anhand der gegebenen Zielstruktur.


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

