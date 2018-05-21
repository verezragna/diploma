import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Tasks } from './tasks.model';
import { TasksPopupService } from './tasks-popup.service';
import { TasksService } from './tasks.service';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-tasks-dialog',
    templateUrl: './tasks-dialog.component.html'
})
export class TasksDialogComponent implements OnInit {

    tasks: Tasks;
    isSaving: boolean;

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private tasksService: TasksService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.tasks.id !== undefined) {
            this.subscribeToSaveResponse(
                this.tasksService.update(this.tasks));
        } else {
            this.subscribeToSaveResponse(
                this.tasksService.create(this.tasks));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Tasks>>) {
        result.subscribe((res: HttpResponse<Tasks>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Tasks) {
        this.eventManager.broadcast({ name: 'tasksListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-tasks-popup',
    template: ''
})
export class TasksPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tasksPopupService: TasksPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.tasksPopupService
                    .open(TasksDialogComponent as Component, params['id']);
            } else {
                this.tasksPopupService
                    .open(TasksDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
