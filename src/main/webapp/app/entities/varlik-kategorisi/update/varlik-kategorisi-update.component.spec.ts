jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VarlikKategorisiService } from '../service/varlik-kategorisi.service';
import { IVarlikKategorisi, VarlikKategorisi } from '../varlik-kategorisi.model';

import { VarlikKategorisiUpdateComponent } from './varlik-kategorisi-update.component';

describe('Component Tests', () => {
  describe('VarlikKategorisi Management Update Component', () => {
    let comp: VarlikKategorisiUpdateComponent;
    let fixture: ComponentFixture<VarlikKategorisiUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let varlikKategorisiService: VarlikKategorisiService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VarlikKategorisiUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VarlikKategorisiUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VarlikKategorisiUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      varlikKategorisiService = TestBed.inject(VarlikKategorisiService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const varlikKategorisi: IVarlikKategorisi = { id: 456 };

        activatedRoute.data = of({ varlikKategorisi });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(varlikKategorisi));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<VarlikKategorisi>>();
        const varlikKategorisi = { id: 123 };
        jest.spyOn(varlikKategorisiService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ varlikKategorisi });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: varlikKategorisi }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(varlikKategorisiService.update).toHaveBeenCalledWith(varlikKategorisi);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<VarlikKategorisi>>();
        const varlikKategorisi = new VarlikKategorisi();
        jest.spyOn(varlikKategorisiService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ varlikKategorisi });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: varlikKategorisi }));
        saveSubject.complete();

        // THEN
        expect(varlikKategorisiService.create).toHaveBeenCalledWith(varlikKategorisi);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<VarlikKategorisi>>();
        const varlikKategorisi = { id: 123 };
        jest.spyOn(varlikKategorisiService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ varlikKategorisi });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(varlikKategorisiService.update).toHaveBeenCalledWith(varlikKategorisi);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
