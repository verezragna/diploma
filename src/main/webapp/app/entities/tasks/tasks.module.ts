import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DiplomaSharedModule } from '../../shared';
import { DiplomaAdminModule } from '../../admin/admin.module';
import {
    TasksService,
    TasksPopupService,
    TasksComponent,
    TasksDetailComponent,
    TasksDialogComponent,
    TasksPopupComponent,
    TasksDeletePopupComponent,
    TasksDeleteDialogComponent,
    tasksRoute,
    tasksPopupRoute,
    TasksResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...tasksRoute,
    ...tasksPopupRoute,
];

@NgModule({
    imports: [
        DiplomaSharedModule,
        DiplomaAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TasksComponent,
        TasksDetailComponent,
        TasksDialogComponent,
        TasksDeleteDialogComponent,
        TasksPopupComponent,
        TasksDeletePopupComponent,
    ],
    entryComponents: [
        TasksComponent,
        TasksDialogComponent,
        TasksPopupComponent,
        TasksDeleteDialogComponent,
        TasksDeletePopupComponent,
    ],
    providers: [
        TasksService,
        TasksPopupService,
        TasksResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DiplomaTasksModule {}
