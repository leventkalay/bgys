import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TehditDetailComponent } from './tehdit-detail.component';

describe('Component Tests', () => {
  describe('Tehdit Management Detail Component', () => {
    let comp: TehditDetailComponent;
    let fixture: ComponentFixture<TehditDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TehditDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tehdit: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TehditDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TehditDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tehdit on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tehdit).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
