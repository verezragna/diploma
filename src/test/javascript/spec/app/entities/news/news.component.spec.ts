/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DiplomaTestModule } from '../../../test.module';
import { NewsComponent } from '../../../../../../main/webapp/app/entities/news/news.component';
import { NewsService } from '../../../../../../main/webapp/app/entities/news/news.service';
import { News } from '../../../../../../main/webapp/app/entities/news/news.model';

describe('Component Tests', () => {

    describe('News Management Component', () => {
        let comp: NewsComponent;
        let fixture: ComponentFixture<NewsComponent>;
        let service: NewsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DiplomaTestModule],
                declarations: [NewsComponent],
                providers: [
                    NewsService
                ]
            })
            .overrideTemplate(NewsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(NewsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NewsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new News(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.news[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
