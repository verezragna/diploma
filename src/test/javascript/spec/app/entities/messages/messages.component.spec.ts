/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DiplomaTestModule } from '../../../test.module';
import { MessagesComponent } from '../../../../../../main/webapp/app/entities/messages/messages.component';
import { MessagesService } from '../../../../../../main/webapp/app/entities/messages/messages.service';
import { Messages } from '../../../../../../main/webapp/app/entities/messages/messages.model';

describe('Component Tests', () => {

    describe('Messages Management Component', () => {
        let comp: MessagesComponent;
        let fixture: ComponentFixture<MessagesComponent>;
        let service: MessagesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DiplomaTestModule],
                declarations: [MessagesComponent],
                providers: [
                    MessagesService
                ]
            })
            .overrideTemplate(MessagesComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MessagesComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MessagesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Messages(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.messages[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
