import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { DiplomaMessagesModule } from './messages/messages.module';
import { DiplomaTasksModule } from './tasks/tasks.module';
import { DiplomaNewsModule } from './news/news.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        DiplomaMessagesModule,
        DiplomaTasksModule,
        DiplomaNewsModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DiplomaEntityModule {}
