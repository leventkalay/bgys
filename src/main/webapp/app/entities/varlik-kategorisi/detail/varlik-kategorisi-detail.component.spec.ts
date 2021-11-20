import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VarlikKategorisiDetailComponent } from './varlik-kategorisi-detail.component';

describe('Component Tests', () => {
  describe('VarlikKategorisi Management Detail Component', () => {
    let comp: VarlikKategorisiDetailComponent;
    let fixture: ComponentFixture<VarlikKategorisiDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [VarlikKategorisiDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ varlikKategorisi: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(VarlikKategorisiDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VarlikKategorisiDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load varlikKategorisi on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.varlikKategorisi).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
