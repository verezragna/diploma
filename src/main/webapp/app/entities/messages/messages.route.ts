import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { MessagesComponent } from './messages.component';
import { MessagesDetailComponent } from './messages-detail.component';
import { MessagesPopupComponent } from './messages-dialog.component';
import { MessagesDeletePopupComponent } from './messages-delete-dialog.component';

@Injectable()
export class MessagesResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const messagesRoute: Routes = [
    {
        path: 'messages',
        component: MessagesComponent,
        resolve: {
            'pagingParams': MessagesResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Messages'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'messages/:id',
        component: MessagesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Messages'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const messagesPopupRoute: Routes = [
    {
        path: 'messages-new',
        component: MessagesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Messages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'messages/:id/edit',
        component: MessagesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Messages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'messages/:id/delete',
        component: MessagesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Messages'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
