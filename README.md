# TWTR

TWTR ermöglicht dem Anwender ontologiegestützt nach Tweets zu suchen. Dazu werden Tweets aus einer Datenbank mit zusätzlichen Informationen hinterlegt und in einem RDF-Graphen gespeichert. Mittels einer Webapplikation können diese Informationen anschließend abgefragt werden.

## RDF Vokabular
...

### Beispiel
Aus einer CSV-Datei wird folgender Datensatz eingelesen:

```csv
29;f0b6cca0-66af-4499-abb7-71b0f41bd29c;807720856856109056;8426;17765;285;134;10;0.31981;0.32171;0.67829;"OD Hanz Moleman";118569851;"donate to my paypal #overdraft# Seattle";"I like my new Eastpak very much!"
```

#### XML-Struktur
Daraus wird mittels Jena und verschiedenen Taggern folgender RDF-Graph erzeugt:

```xml
<twtr:TwitterAccount rdf:about="http://example.org/118569851">
    <twtr:tweeted>
      <twtr:Tweet rdf:about="http://example.org/Tweet/807720856856109056">
        <twtr:contains>
          <twtr:ProperNoun>
            <twtr:isA rdf:resource="http://example.org/Organisation"/>
            <twtr:word>Eastpak</twtr:word>
          </twtr:ProperNoun>
        </twtr:contains>
        <twtr:mentions>
          <twtr:Object>
            <twtr:text>my new Eastpak</twtr:text>
          </twtr:Object>
        </twtr:mentions>
        <twtr:mentions>
          <twtr:Verb>
            <twtr:text>like</twtr:text>
          </twtr:Verb>
        </twtr:mentions>
        <twtr:mentions>
          <twtr:Subject>
            <twtr:text>I</twtr:text>
          </twtr:Subject>
        </twtr:mentions>
        <twtr:tweetText>I like my new Eastpak very much!</twtr:tweetText>
        <twtr:tweetID>807720856856109056</twtr:tweetID>
      </twtr:Tweet>
    </twtr:tweeted>
    <twtr:userDescription>donate to my paypal #overdraft# Seattle</twtr:userDescription>
    <twtr:followerCount rdf:datatype="http://www.w3.org/2001/XMLSchema#int"
    >285</twtr:followerCount>
    <twtr:userID>118569851</twtr:userID>
    <twtr:userName>OD Hanz Moleman</twtr:userName>
  </twtr:TwitterAccount>
```

### Beispielabfrage
Mittels der SPARQL-Anfragesprache lassen sich nun die Daten abfragen. 

In diesem Fall lassen wir uns alle Tweets mit einer Organisation als Proper Noun anzeigen.

```sparql
PREFIX  twtr: <http://www.example.com>

SELECT  ?author ?tweetText ?propNounText
WHERE
  { ?account  a                     twtr:TwitterAccount ;
              twtr:userName         ?author ;
              twtr:tweeted          ?tweet .
    ?tweet    twtr:tweetText        ?tweetText ;
              twtr:contains         ?propn .
    ?propn    a                     twtr:ProperNoun ;
              twtr:isA              twtr:Organisation ;
              twtr:word             ?propNounText
  }
```

### Rückgabe
```
| author            | tweetText                          | propNounText |
|-------------------|------------------------------------|--------------|
| "OD Hanz Moleman" | "I like my new Eastpak very much!" | "Eastpak"    |
```

### Zusätzliche Informationen
Die Tweets werden mit folgenden zusätzlichen Informationen hinterlegt:

#### Subjekt-Verb-Objekt Tagging

Derzeit sind für diesen Zweck zwei verschiedene Varianten von Taggern eingebaut, die noch evaluiert werden müssen:

* IBM Watson Natural Language Understanding Tagger
* Stanford NLP Tagger mit dem Gate-Twitter-Modell

Um den Watson Tagger zu nutzen, werden entsprechende IBM Watson Cloud Credentials mit aktivierter Natural Language Understanding API benötigt. Die Zugangsdaten müssen dann in den `bluemix.properties` hinterlegt werden.

#### Keywords
(Keyword-Extraction ...)

#### Kategorisierung von Eigennamen 
Derzeit wird dem Tweet das relevanteste Proper Noun hinterlegt. Dazu wird die IBM Watson Natural Language Understanding API abgefragt.

## Tasks:

- [ ] Auswahl und Evaluierung eines POS-Taggers (Goldstandard).
- [ ] Anwendung der Watson NLU Keyword-Extraction + Named Entity Recognition.
- [ ] Prototypische Implementierung einer RDF-Zielstruktur mit Jena + (Fuseki).
- [ ] Entwicklung eines Abfrageservices auf Basis von SPARQL und Angular.
- [ ] Überführung der Twitterdaten in den RDF-Triplestore anhand der gegebenen Zielstruktur.


## Hinweise zum Start der Umgebung (noch nicht nötig)

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

