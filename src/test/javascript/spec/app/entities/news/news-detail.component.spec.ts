/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DiplomaTestModule } from '../../../test.module';
import { NewsDetailComponent } from '../../../../../../main/webapp/app/entities/news/news-detail.component';
import { NewsService } from '../../../../../../main/webapp/app/entities/news/news.service';
import { News } from '../../../../../../main/webapp/app/entities/news/news.model';

describe('Component Tests', () => {

    describe('News Management Detail Component', () => {
        let comp: NewsDetailComponent;
        let fixture: ComponentFixture<NewsDetailComponent>;
        let service: NewsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DiplomaTestModule],
                declarations: [NewsDetailComponent],
                providers: [
                    NewsService
                ]
            })
            .overrideTemplate(NewsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(NewsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NewsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new News(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.news).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
