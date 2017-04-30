# TWTR

TWTR ermöglicht dem Anwender ontologiegestützt nach Tweets zu suchen. Dazu werden Tweets aus einer Datenbank mit zusätzlichen Informationen hinterlegt und in einem RDF-Graphen gespeichert. Mittels einer Webapplikation können diese Informationen anschließend abgefragt werden.

## RDF Vokabular
<img src="http://svgshare.com/i/1SU.svg">

## Beispiel
Aus einer CSV-Datei wird folgender Datensatz eingelesen:

```csv
781937602936963072;17158660;3096;15625;2415;2425;120;Virtually Everywhere;Jett Ray;17158660;Learning Leader, Soul Gardener, Conscious Wanderer. http://JettJournal.com ~  We are stronger, together. Peace/Love/Coffee ✌ ❤ ☕;RT @24k: #IoT Gets Social: How Our Connected Lives Will Change #SocialMedia https://t.co/LaToDF8BNa MT @socialmedia2day https://t.co/Mudz0v…
```

#### XML-Struktur
Daraus wird mittels Jena und verschiedenen Taggern folgender RDF-Graph erzeugt:

```xml
<twtr:Person rdf:about="http://example.org/17158660">
    <twtr:tweeted>
      <twtr:Tweet rdf:about="http://example.org/Tweet/781937602936963072">
        <twtr:contains>
          <twtr:CommonNoun>
            <twtr:word>Lives</twtr:word>
          </twtr:CommonNoun>
        </twtr:contains>
        <twtr:contains>
          <twtr:ProperNoun>
            <twtr:word>socialmedia2day</twtr:word>
          </twtr:ProperNoun>
        </twtr:contains>
        <twtr:contains>
          <twtr:ProperNoun>
            <twtr:word>MT</twtr:word>
          </twtr:ProperNoun>
        </twtr:contains>
        <twtr:contains>
          <twtr:Adjective>
            <twtr:word>Connected</twtr:word>
          </twtr:Adjective>
        </twtr:contains>
        <twtr:contains>
          <twtr:Triplet>
            <twtr:has>
              <twtr:Object>
                <twtr:word>How Our Connected Lives</twtr:word>
              </twtr:Object>
            </twtr:has>
            <twtr:has>
              <twtr:Verb>
                <twtr:word>Will Change</twtr:word>
              </twtr:Verb>
            </twtr:has>
            <twtr:has>
              <twtr:Subject>
                <twtr:word>SocialMedia</twtr:word>
              </twtr:Subject>
            </twtr:has>
          </twtr:Triplet>
        </twtr:contains>
        <twtr:contains>
          <twtr:Triplet>
            <twtr:has>
              <twtr:ProperNoun>
                <twtr:word>Social</twtr:word>
                <rdf:type rdf:resource="http://example.org/Object"/>
              </twtr:ProperNoun>
            </twtr:has>
            <twtr:has>
              <twtr:Verb>
                <twtr:word>Gets</twtr:word>
              </twtr:Verb>
            </twtr:has>
            <twtr:has>
              <twtr:ProperNoun>
                <twtr:word>IoT</twtr:word>
                <rdf:type rdf:resource="http://example.org/Subject"/>
              </twtr:ProperNoun>
            </twtr:has>
          </twtr:Triplet>
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
```

#### Beispielabfrage
Mittels der SPARQL-Anfragesprache lassen sich nun die Daten abfragen. 

In diesem Fall lassen wir uns alle Tweets anzeigen, die ein Subjekt beinhalten, das gleichzeitig ein Proper Noun ist.

```sparql
PREFIX  twtr: <http://www.example.com>

SELECT  ?author ?tweetText ?subjWord
WHERE
  { ?account  a                     twtr:TwitterAccount ;
              twtr:userName         ?author ;
              twtr:tweeted          ?tweet .
    ?tweet    twtr:tweetText        ?tweetText ;
              twtr:contains         ?triplet .
    ?triplet  a                     twtr:Triplet ;
              twtr:has              ?subj .
    ?subj     a                     twtr:Subject ;
              a                     twtr:ProperNoun ;
              twtr:word             ?subjWord
  }
```

#### Rückgabe
```
| author     | tweetText                                                                                                                                      | subjWord |
|------------|------------------------------------------------------------------------------------------------------------------------------------------------|----------|
| "Jett Ray" | "RT @24k: #IoT Gets Social: How Our Connected Lives Will Change #SocialMedia https://t.co/LaToDF8BNa MT @socialmedia2day https://t.co/Mudz0v…" | "IoT"    |
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

