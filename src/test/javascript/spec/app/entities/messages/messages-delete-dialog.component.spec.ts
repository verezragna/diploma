/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DiplomaTestModule } from '../../../test.module';
import { MessagesDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/messages/messages-delete-dialog.component';
import { MessagesService } from '../../../../../../main/webapp/app/entities/messages/messages.service';

describe('Component Tests', () => {

    describe('Messages Management Delete Component', () => {
        let comp: MessagesDeleteDialogComponent;
        let fixture: ComponentFixture<MessagesDeleteDialogComponent>;
        let service: MessagesService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DiplomaTestModule],
                declarations: [MessagesDeleteDialogComponent],
                providers: [
                    MessagesService
                ]
            })
            .overrideTemplate(MessagesDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MessagesDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MessagesService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
