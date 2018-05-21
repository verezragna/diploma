import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { News } from './news.model';
import { NewsPopupService } from './news-popup.service';
import { NewsService } from './news.service';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-news-dialog',
    templateUrl: './news-dialog.component.html'
})
export class NewsDialogComponent implements OnInit {

    news: News;
    isSaving: boolean;

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private newsService: NewsService,
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
        if (this.news.id !== undefined) {
            this.subscribeToSaveResponse(
                this.newsService.update(this.news));
        } else {
            this.subscribeToSaveResponse(
                this.newsService.create(this.news));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<News>>) {
        result.subscribe((res: HttpResponse<News>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: News) {
        this.eventManager.broadcast({ name: 'newsListModification', content: 'OK'});
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
    selector: 'jhi-news-popup',
    template: ''
})
export class NewsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private newsPopupService: NewsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.newsPopupService
                    .open(NewsDialogComponent as Component, params['id']);
            } else {
                this.newsPopupService
                    .open(NewsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
