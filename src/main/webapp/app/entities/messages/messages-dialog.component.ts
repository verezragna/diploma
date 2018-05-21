import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Messages } from './messages.model';
import { MessagesPopupService } from './messages-popup.service';
import { MessagesService } from './messages.service';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-messages-dialog',
    templateUrl: './messages-dialog.component.html'
})
export class MessagesDialogComponent implements OnInit {

    messages: Messages;
    isSaving: boolean;

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private messagesService: MessagesService,
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
        if (this.messages.id !== undefined) {
            this.subscribeToSaveResponse(
                this.messagesService.update(this.messages));
        } else {
            this.subscribeToSaveResponse(
                this.messagesService.create(this.messages));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Messages>>) {
        result.subscribe((res: HttpResponse<Messages>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Messages) {
        this.eventManager.broadcast({ name: 'messagesListModification', content: 'OK'});
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

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-messages-popup',
    template: ''
})
export class MessagesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private messagesPopupService: MessagesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.messagesPopupService
                    .open(MessagesDialogComponent as Component, params['id']);
            } else {
                this.messagesPopupService
                    .open(MessagesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
