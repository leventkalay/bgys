import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SurecDetailComponent } from './surec-detail.component';

describe('Component Tests', () => {
  describe('Surec Management Detail Component', () => {
    let comp: SurecDetailComponent;
    let fixture: ComponentFixture<SurecDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SurecDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ surec: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SurecDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SurecDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load surec on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.surec).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
