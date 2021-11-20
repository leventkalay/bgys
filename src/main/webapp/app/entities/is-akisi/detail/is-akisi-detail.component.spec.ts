import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IsAkisiDetailComponent } from './is-akisi-detail.component';

describe('Component Tests', () => {
  describe('IsAkisi Management Detail Component', () => {
    let comp: IsAkisiDetailComponent;
    let fixture: ComponentFixture<IsAkisiDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [IsAkisiDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ isAkisi: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(IsAkisiDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IsAkisiDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load isAkisi on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.isAkisi).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
