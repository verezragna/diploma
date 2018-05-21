import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DiplomaSharedModule } from '../../shared';
import { DiplomaAdminModule } from '../../admin/admin.module';
import {
    MessagesService,
    MessagesPopupService,
    MessagesComponent,
    MessagesDetailComponent,
    MessagesDialogComponent,
    MessagesPopupComponent,
    MessagesDeletePopupComponent,
    MessagesDeleteDialogComponent,
    messagesRoute,
    messagesPopupRoute,
    MessagesResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...messagesRoute,
    ...messagesPopupRoute,
];

@NgModule({
    imports: [
        DiplomaSharedModule,
        DiplomaAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MessagesComponent,
        MessagesDetailComponent,
        MessagesDialogComponent,
        MessagesDeleteDialogComponent,
        MessagesPopupComponent,
        MessagesDeletePopupComponent,
    ],
    entryComponents: [
        MessagesComponent,
        MessagesDialogComponent,
        MessagesPopupComponent,
        MessagesDeleteDialogComponent,
        MessagesDeletePopupComponent,
    ],
    providers: [
        MessagesService,
        MessagesPopupService,
        MessagesResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DiplomaMessagesModule {}
