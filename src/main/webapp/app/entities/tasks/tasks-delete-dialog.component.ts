import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Tasks } from './tasks.model';
import { TasksPopupService } from './tasks-popup.service';
import { TasksService } from './tasks.service';

@Component({
    selector: 'jhi-tasks-delete-dialog',
    templateUrl: './tasks-delete-dialog.component.html'
})
export class TasksDeleteDialogComponent {

    tasks: Tasks;

    constructor(
        private tasksService: TasksService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.tasksService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'tasksListModification',
                content: 'Deleted an tasks'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-tasks-delete-popup',
    template: ''
})
export class TasksDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tasksPopupService: TasksPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.tasksPopupService
                .open(TasksDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
