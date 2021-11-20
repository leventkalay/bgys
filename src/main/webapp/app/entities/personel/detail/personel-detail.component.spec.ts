import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonelDetailComponent } from './personel-detail.component';

describe('Component Tests', () => {
  describe('Personel Management Detail Component', () => {
    let comp: PersonelDetailComponent;
    let fixture: ComponentFixture<PersonelDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PersonelDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ personel: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PersonelDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PersonelDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load personel on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.personel).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
