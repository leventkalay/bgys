import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AksiyonDetailComponent } from './aksiyon-detail.component';

describe('Component Tests', () => {
  describe('Aksiyon Management Detail Component', () => {
    let comp: AksiyonDetailComponent;
    let fixture: ComponentFixture<AksiyonDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AksiyonDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ aksiyon: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AksiyonDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AksiyonDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load aksiyon on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.aksiyon).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
