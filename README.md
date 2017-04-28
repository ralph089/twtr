# TWTR

TWTR ermöglicht dem Anwender ontologiegestützt nach Tweets zu suchen. Dazu werden Tweets aus einer Datenbank mit zusätzlichen Informationen hinterlegt und in einem RDF-Graphen gespeichert. Mittels einer Webapplikation können diese Informationen anschließend abgefragt werden.

## RDF Vokabular
<img src="http://svgshare.com/i/1Ts.svg">

## Beispiel
Aus einer CSV-Datei wird folgender Datensatz eingelesen:

```csv
781935565872693248;14968996;3253;11130;2445;1864;235;Massachusetts;Michael Corey;14968996;★Columnist★Entrepreneur★Microsoft MVP★VMware vExpert★Author★Oracle Ace★Speaker★Virtualization Nut★Blog: http://michaelcorey.com/;Microsoft Ignite: How algorithms, the cloud, IoT and data are changing computing https://t.co/klbOZM6aCm
```

#### XML-Struktur
Daraus wird mittels Jena und verschiedenen Taggern folgender RDF-Graph erzeugt:

```xml
<twtr:TwitterAccount rdf:about="http://example.org/14968996">
    <twtr:tweeted>
      <twtr:Tweet rdf:about="http://example.org/Tweet/781935565872693248">
        <twtr:contains>
          <twtr:ProperNoun>
            <twtr:word>data</twtr:word>
          </twtr:ProperNoun>
        </twtr:contains>
        <twtr:contains>
          <twtr:ProperNoun>
            <twtr:word>cloud</twtr:word>
          </twtr:ProperNoun>
        </twtr:contains>
        <twtr:contains>
          <twtr:ProperNoun>
            <twtr:word>IoT</twtr:word>
          </twtr:ProperNoun>
        </twtr:contains>
        <twtr:contains>
          <twtr:ProperNoun>
            <twtr:word>Ignite</twtr:word>
          </twtr:ProperNoun>
        </twtr:contains>
        <twtr:contains>
          <twtr:ProperNoun>
            <twtr:isEntity>
              <twtr:Organisation/>
            </twtr:isEntity>
            <twtr:word>Microsoft</twtr:word>
          </twtr:ProperNoun>
        </twtr:contains>
        <twtr:contains>
          <twtr:Triplet>
            <twtr:has>
              <twtr:Object>
                <twtr:is rdf:resource="http://example.org/CommonNoun"/>
                <twtr:word>computing</twtr:word>
              </twtr:Object>
            </twtr:has>
            <twtr:has>
              <twtr:Verb>
                <twtr:word>changing</twtr:word>
              </twtr:Verb>
            </twtr:has>
            <twtr:has>
              <twtr:Subject>
                <twtr:is rdf:resource="http://example.org/CommonNoun"/>
                <twtr:word>algorithms</twtr:word>
              </twtr:Subject>
            </twtr:has>
          </twtr:Triplet>
        </twtr:contains>
        <twtr:tweetText>Microsoft Ignite: How algorithms, the cloud, IoT and data are changing computing https://t.co/klbOZM6aCm</twtr:tweetText>
        <twtr:tweetID>781935565872693248</twtr:tweetID>
      </twtr:Tweet>
    </twtr:tweeted>
    <twtr:isEntity>
      <twtr:Person/>
    </twtr:isEntity>
    <twtr:userDescription>★Columnist★Entrepreneur★Microsoft MVP★VMware vExpert★Author★Oracle Ace★Speaker★Virtualization Nut★Blog: http://michaelcorey.com/</twtr:userDescription>
    <twtr:followerCount rdf:datatype="http://www.w3.org/2001/XMLSchema#int"
    >2445</twtr:followerCount>
    <twtr:userID>14968996</twtr:userID>
    <twtr:userName>Michael Corey</twtr:userName>
  </twtr:TwitterAccount>
```

#### Beispielabfrage
Mittels der SPARQL-Anfragesprache lassen sich nun die Daten abfragen. 

In diesem Fall lassen wir uns alle Tweets anzeigen, die ein vollständiges Triplet beinhalten.

```sparql
PREFIX  twtr: <http://www.example.com>

SELECT  ?author ?tweetText ?subjWord ?verbWord ?objWord
WHERE
  { ?account  a                     twtr:TwitterAccount ;
              twtr:userName         ?author ;
              twtr:tweeted          ?tweet .
    ?tweet    twtr:tweetText        ?tweetText ;
              twtr:contains         ?triplet .
    ?triplet  a                     twtr:Triplet ;
              twtr:has              ?subj ;
              twtr:has              ?verb ;
              twtr:has              ?obj .
    ?subj     a                     twtr:Subject ;
              twtr:word             ?subjWord .
    ?verb     a                     twtr:Verb ;
              twtr:word             ?verbWord .
    ?obj      a                     twtr:Object ;
              twtr:word             ?objWord
  }
```

#### Rückgabe
```
| author          | tweetText                                                                                                  | subjWord     | verbWord   | objWord     |
|-----------------|------------------------------------------------------------------------------------------------------------|--------------|------------|-------------|
| "Michael Corey" | "Microsoft Ignite: How algorithms, the cloud, IoT and data are changing computing https://t.co/klbOZM6aCm" | "algorithms" | "changing" | "computing" |
```

## Zusätzliche Informationen
Die Tweets werden mit folgenden zusätzlichen Informationen hinterlegt:

#### Subjekt-Verb-Objekt Tagging

Derzeit sind für diesen Zweck zwei verschiedene Varianten von Taggern eingebaut, die noch evaluiert werden müssen:

* IBM Watson Natural Language Understanding Tagger
* Stanford NLP Tagger mit dem Gate-Twitter-Modell

Um den Watson Tagger zu nutzen, werden entsprechende IBM Watson Cloud Credentials mit aktivierter Natural Language Understanding API benötigt. Die Zugangsdaten müssen dann in den `bluemix.properties` hinterlegt werden.

#### POS-Tagging

Neben dem Subjekt-Verb-Objekt Tagging wird außerdem noch die Wortart ermittelt. Derzeit werden folgende Wortarten hinterlegt:

* Common Nouns
* Proper Nouns
* Adjektive

Dazu wird derzeit Stanford NLP verwendet.

#### Kategorisierung von Eigennamen 
Bei der Kategorisierung von Eigennamen wird der Autor und die Proper Nouns einer der folgenden drei Kategorien zugeordnet:

* Person
* Organization
* Place

Die Kategorisierung von Eigennamen erfolgt derzeit über die Stanford Named Entity Recognition (NER) API. Zusätzlich soll noch eine Anbindung an die Watson NLU API erfolgen.

#### Keywords
(Keyword-Extraction ...)

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

