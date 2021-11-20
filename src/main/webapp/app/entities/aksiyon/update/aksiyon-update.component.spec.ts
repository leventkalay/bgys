jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AksiyonService } from '../service/aksiyon.service';
import { IAksiyon, Aksiyon } from '../aksiyon.model';

import { AksiyonUpdateComponent } from './aksiyon-update.component';

describe('Component Tests', () => {
  describe('Aksiyon Management Update Component', () => {
    let comp: AksiyonUpdateComponent;
    let fixture: ComponentFixture<AksiyonUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let aksiyonService: AksiyonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AksiyonUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AksiyonUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AksiyonUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      aksiyonService = TestBed.inject(AksiyonService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const aksiyon: IAksiyon = { id: 456 };

        activatedRoute.data = of({ aksiyon });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(aksiyon));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Aksiyon>>();
        const aksiyon = { id: 123 };
        jest.spyOn(aksiyonService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ aksiyon });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: aksiyon }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(aksiyonService.update).toHaveBeenCalledWith(aksiyon);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Aksiyon>>();
        const aksiyon = new Aksiyon();
        jest.spyOn(aksiyonService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ aksiyon });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: aksiyon }));
        saveSubject.complete();

        // THEN
        expect(aksiyonService.create).toHaveBeenCalledWith(aksiyon);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Aksiyon>>();
        const aksiyon = { id: 123 };
        jest.spyOn(aksiyonService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ aksiyon });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(aksiyonService.update).toHaveBeenCalledWith(aksiyon);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
