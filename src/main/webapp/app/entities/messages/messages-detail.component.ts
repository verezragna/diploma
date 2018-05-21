import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Messages } from './messages.model';
import { MessagesService } from './messages.service';

@Component({
    selector: 'jhi-messages-detail',
    templateUrl: './messages-detail.component.html'
})
export class MessagesDetailComponent implements OnInit, OnDestroy {

    messages: Messages;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private messagesService: MessagesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMessages();
    }

    load(id) {
        this.messagesService.find(id)
            .subscribe((messagesResponse: HttpResponse<Messages>) => {
                this.messages = messagesResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMessages() {
        this.eventSubscriber = this.eventManager.subscribe(
            'messagesListModification',
            (response) => this.load(this.messages.id)
        );
    }
}
