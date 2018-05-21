/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DiplomaTestModule } from '../../../test.module';
import { MessagesDetailComponent } from '../../../../../../main/webapp/app/entities/messages/messages-detail.component';
import { MessagesService } from '../../../../../../main/webapp/app/entities/messages/messages.service';
import { Messages } from '../../../../../../main/webapp/app/entities/messages/messages.model';

describe('Component Tests', () => {

    describe('Messages Management Detail Component', () => {
        let comp: MessagesDetailComponent;
        let fixture: ComponentFixture<MessagesDetailComponent>;
        let service: MessagesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DiplomaTestModule],
                declarations: [MessagesDetailComponent],
                providers: [
                    MessagesService
                ]
            })
            .overrideTemplate(MessagesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MessagesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MessagesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Messages(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.messages).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
