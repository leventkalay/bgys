jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TehditKategorisiService } from '../service/tehdit-kategorisi.service';
import { ITehditKategorisi, TehditKategorisi } from '../tehdit-kategorisi.model';

import { TehditKategorisiUpdateComponent } from './tehdit-kategorisi-update.component';

describe('Component Tests', () => {
  describe('TehditKategorisi Management Update Component', () => {
    let comp: TehditKategorisiUpdateComponent;
    let fixture: ComponentFixture<TehditKategorisiUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tehditKategorisiService: TehditKategorisiService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TehditKategorisiUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TehditKategorisiUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TehditKategorisiUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tehditKategorisiService = TestBed.inject(TehditKategorisiService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const tehditKategorisi: ITehditKategorisi = { id: 456 };

        activatedRoute.data = of({ tehditKategorisi });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tehditKategorisi));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TehditKategorisi>>();
        const tehditKategorisi = { id: 123 };
        jest.spyOn(tehditKategorisiService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tehditKategorisi });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tehditKategorisi }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tehditKategorisiService.update).toHaveBeenCalledWith(tehditKategorisi);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TehditKategorisi>>();
        const tehditKategorisi = new TehditKategorisi();
        jest.spyOn(tehditKategorisiService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tehditKategorisi });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tehditKategorisi }));
        saveSubject.complete();

        // THEN
        expect(tehditKategorisiService.create).toHaveBeenCalledWith(tehditKategorisi);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TehditKategorisi>>();
        const tehditKategorisi = { id: 123 };
        jest.spyOn(tehditKategorisiService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tehditKategorisi });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tehditKategorisiService.update).toHaveBeenCalledWith(tehditKategorisi);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
