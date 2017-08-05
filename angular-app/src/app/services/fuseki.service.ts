import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Headers, RequestOptions } from '@angular/http';
import { Complete } from '../fuseki';
import { FusekiResult } from '../entity';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';

@Injectable()
export class FusekiService {

    constructor(private http: Http) {
        console.log('FusekiService Initialized');
    }

    /*
    Sends a SPQARL Query to Fuseki Server
    */
    sendSparqlQuery(query: string, serverUrl:string): Promise<Complete[]> {
        let headers = new Headers({ 'Accept': 'application/sparql-results+json,*/*;q=0.9' });
        headers.append('X-Requested-With', 'XMLHttpRequest');
        headers.append('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
        let options = new RequestOptions({ headers: headers });

        return this.http.post(serverUrl, 'query=' + query,
            options).toPromise().then(this.extractData).catch(this.handleError);

    }

    getEntitys(type: string, serverUrl:string): Promise<FusekiResult> {
        let headers = new Headers({ 'Accept': 'application/sparql-results+json,*/*;q=0.9' });
        headers.append('X-Requested-With', 'XMLHttpRequest');
        headers.append('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
        let options = new RequestOptions({ headers: headers });
        let query: string;
        query = `PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
                 PREFIX twtr: <http://example.org/> 
                 SELECT DISTINCT ?entityName
                 WHERE { ?accout a               twtr:` + type + `;
                                 twtr:entityName ?entityName.
                 }`;
        return this.http.post(serverUrl, 'query=' + query,
            options).toPromise().then(this.extractData).catch(this.handleError);

    }

    private extractData(res: Response) {
        let body = res.json();
        return body;
    }

    private handleError(error: any): Promise<any> {
        console.log("LOL");
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }
}
