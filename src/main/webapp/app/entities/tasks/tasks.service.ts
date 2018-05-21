import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Tasks } from './tasks.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Tasks>;

@Injectable()
export class TasksService {

    private resourceUrl =  SERVER_API_URL + 'api/tasks';

    constructor(private http: HttpClient) { }

    create(tasks: Tasks): Observable<EntityResponseType> {
        const copy = this.convert(tasks);
        return this.http.post<Tasks>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(tasks: Tasks): Observable<EntityResponseType> {
        const copy = this.convert(tasks);
        return this.http.put<Tasks>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Tasks>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Tasks[]>> {
        const options = createRequestOption(req);
        return this.http.get<Tasks[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Tasks[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Tasks = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Tasks[]>): HttpResponse<Tasks[]> {
        const jsonResponse: Tasks[] = res.body;
        const body: Tasks[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Tasks.
     */
    private convertItemFromServer(tasks: Tasks): Tasks {
        const copy: Tasks = Object.assign({}, tasks);
        return copy;
    }

    /**
     * Convert a Tasks to a JSON which can be sent to the server.
     */
    private convert(tasks: Tasks): Tasks {
        const copy: Tasks = Object.assign({}, tasks);
        return copy;
    }
}
