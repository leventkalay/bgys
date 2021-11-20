import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TehditKategorisiDetailComponent } from './tehdit-kategorisi-detail.component';

describe('Component Tests', () => {
  describe('TehditKategorisi Management Detail Component', () => {
    let comp: TehditKategorisiDetailComponent;
    let fixture: ComponentFixture<TehditKategorisiDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TehditKategorisiDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tehditKategorisi: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TehditKategorisiDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TehditKategorisiDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tehditKategorisi on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tehditKategorisi).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
