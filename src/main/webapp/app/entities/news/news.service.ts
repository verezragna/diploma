import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { News } from './news.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<News>;

@Injectable()
export class NewsService {

    private resourceUrl =  SERVER_API_URL + 'api/news';

    constructor(private http: HttpClient) { }

    create(news: News): Observable<EntityResponseType> {
        const copy = this.convert(news);
        return this.http.post<News>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(news: News): Observable<EntityResponseType> {
        const copy = this.convert(news);
        return this.http.put<News>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<News>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<News[]>> {
        const options = createRequestOption(req);
        return this.http.get<News[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<News[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: News = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<News[]>): HttpResponse<News[]> {
        const jsonResponse: News[] = res.body;
        const body: News[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to News.
     */
    private convertItemFromServer(news: News): News {
        const copy: News = Object.assign({}, news);
        return copy;
    }

    /**
     * Convert a News to a JSON which can be sent to the server.
     */
    private convert(news: News): News {
        const copy: News = Object.assign({}, news);
        return copy;
    }
}
