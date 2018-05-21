import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Tasks } from './tasks.model';
import { TasksService } from './tasks.service';

@Component({
    selector: 'jhi-tasks-detail',
    templateUrl: './tasks-detail.component.html'
})
export class TasksDetailComponent implements OnInit, OnDestroy {

    tasks: Tasks;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private tasksService: TasksService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTasks();
    }

    load(id) {
        this.tasksService.find(id)
            .subscribe((tasksResponse: HttpResponse<Tasks>) => {
                this.tasks = tasksResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTasks() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tasksListModification',
            (response) => this.load(this.tasks.id)
        );
    }
}
