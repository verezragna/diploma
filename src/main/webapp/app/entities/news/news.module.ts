import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DiplomaSharedModule } from '../../shared';
import { DiplomaAdminModule } from '../../admin/admin.module';
import {
    NewsService,
    NewsPopupService,
    NewsComponent,
    NewsDetailComponent,
    NewsDialogComponent,
    NewsPopupComponent,
    NewsDeletePopupComponent,
    NewsDeleteDialogComponent,
    newsRoute,
    newsPopupRoute,
    NewsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...newsRoute,
    ...newsPopupRoute,
];

@NgModule({
    imports: [
        DiplomaSharedModule,
        DiplomaAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        NewsComponent,
        NewsDetailComponent,
        NewsDialogComponent,
        NewsDeleteDialogComponent,
        NewsPopupComponent,
        NewsDeletePopupComponent,
    ],
    entryComponents: [
        NewsComponent,
        NewsDialogComponent,
        NewsPopupComponent,
        NewsDeleteDialogComponent,
        NewsDeletePopupComponent,
    ],
    providers: [
        NewsService,
        NewsPopupService,
        NewsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DiplomaNewsModule {}
