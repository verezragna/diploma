import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Messages } from './messages.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Messages>;

@Injectable()
export class MessagesService {

    private resourceUrl =  SERVER_API_URL + 'api/messages';

    constructor(private http: HttpClient) { }

    create(messages: Messages): Observable<EntityResponseType> {
        const copy = this.convert(messages);
        return this.http.post<Messages>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(messages: Messages): Observable<EntityResponseType> {
        const copy = this.convert(messages);
        return this.http.put<Messages>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Messages>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Messages[]>> {
        const options = createRequestOption(req);
        return this.http.get<Messages[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Messages[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Messages = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Messages[]>): HttpResponse<Messages[]> {
        const jsonResponse: Messages[] = res.body;
        const body: Messages[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Messages.
     */
    private convertItemFromServer(messages: Messages): Messages {
        const copy: Messages = Object.assign({}, messages);
        return copy;
    }

    /**
     * Convert a Messages to a JSON which can be sent to the server.
     */
    private convert(messages: Messages): Messages {
        const copy: Messages = Object.assign({}, messages);
        return copy;
    }
}
