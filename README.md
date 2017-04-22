# TWTR

TWTR ermöglicht dem Anwender ontologiegestützt nach Tweets zu suchen. Dazu werden Tweets mit zusätzlichen Informationen hinterlegt und in einem RDF-Graphen gespeichert. Mittels einer Webapplikation können diese Informationen anschließend abgefragt werden.

## RDF Vokabular
...

### Beispielausgabe
```xml
<rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:twtr="http://example.org/"
    xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema#">
  <twtr:TwitterAccount rdf:about="http://example.org/MyrtleNewsam">
    <twtr:tweeted>
      <twtr:Tweet rdf:about="http://example.org/Tweet/8,10E+17">
        <twtr:mentions>
          <twtr:TweetSubject rdf:about="http://example.org/It">
            <twtr:StressingOut>
              <twtr:TweetObject rdf:about="http://example.org/Me"/>
            </twtr:StressingOut>
          </twtr:TweetSubject>
        </twtr:mentions>
        <twtr:tweetText>Trying to play Battlefield 4 and the bastard keep killing me, it's stressing me out. Giving up.</twtr:tweetText>
        <twtr:tweetID>8,10E+17</twtr:tweetID>
      </twtr:Tweet>
    </twtr:tweeted>
    <twtr:userDescription>I am a gospel songwriter.  Currently recording a gospel CD.  I write children and adult stories. Member of BMI, CCSA and Writers Unite.</twtr:userDescription>
    <twtr:followerCount rdf:datatype="http://www.w3.org/2001/XMLSchema#int"
    >274</twtr:followerCount>
    <twtr:userID>722982356</twtr:userID>
    <twtr:userName>Myrtle Newsam</twtr:userName>
  </twtr:TwitterAccount>
</rdf:RDF>
```

### Zusätzliche Informationen
Die Tweets werden mit folgenden zusätzlichen Informationen hinterlegt:

#### POS-Tagging (Subjekt-Verb-Objekt)

Derzeit sind zwei verschiedene Varianten von Taggern eingebaut, die noch evaluiert werden müssen:

* IBM Watson Natural Language Understanding Tagger
* Stanford NLP Tagger mit dem Gate-Twitter-Modell

Um den Watson Tagger zu nutzen, werden entsprechende IBM Watson Cloud Credentials mit aktivierter Natural Language Understanding API benötigt. Die Zugangsdaten müssen dann in den `bluemix.properties` hinterlegt werden.

#### Keywords
(Keyword-Extraction ...)

#### Kategorisierung von Eigennamen 
(Named-entity recognition ...)

## Tasks:

- [ ] Auswahl und Evaluierung eines POS-Taggers (Goldstandard).
- [ ] Anwendung der Watson NLU Keyword-Extraction + Named Entity Recognition.
- [ ] Prototypische Implementierung einer RDF-Zielstruktur mit Jena + (Fuseki).
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

