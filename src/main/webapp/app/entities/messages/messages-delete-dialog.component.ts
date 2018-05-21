import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Messages } from './messages.model';
import { MessagesPopupService } from './messages-popup.service';
import { MessagesService } from './messages.service';

@Component({
    selector: 'jhi-messages-delete-dialog',
    templateUrl: './messages-delete-dialog.component.html'
})
export class MessagesDeleteDialogComponent {

    messages: Messages;

    constructor(
        private messagesService: MessagesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.messagesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'messagesListModification',
                content: 'Deleted an messages'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-messages-delete-popup',
    template: ''
})
export class MessagesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private messagesPopupService: MessagesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.messagesPopupService
                .open(MessagesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
