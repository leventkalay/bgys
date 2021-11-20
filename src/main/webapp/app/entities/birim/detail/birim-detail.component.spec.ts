import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BirimDetailComponent } from './birim-detail.component';

describe('Component Tests', () => {
  describe('Birim Management Detail Component', () => {
    let comp: BirimDetailComponent;
    let fixture: ComponentFixture<BirimDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BirimDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ birim: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BirimDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BirimDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load birim on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.birim).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
