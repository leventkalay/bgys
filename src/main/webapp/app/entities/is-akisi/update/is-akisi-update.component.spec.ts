jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { IsAkisiService } from '../service/is-akisi.service';
import { IIsAkisi, IsAkisi } from '../is-akisi.model';
import { IPersonel } from 'app/entities/personel/personel.model';
import { PersonelService } from 'app/entities/personel/service/personel.service';

import { IsAkisiUpdateComponent } from './is-akisi-update.component';

describe('Component Tests', () => {
  describe('IsAkisi Management Update Component', () => {
    let comp: IsAkisiUpdateComponent;
    let fixture: ComponentFixture<IsAkisiUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let isAkisiService: IsAkisiService;
    let personelService: PersonelService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [IsAkisiUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(IsAkisiUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IsAkisiUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      isAkisiService = TestBed.inject(IsAkisiService);
      personelService = TestBed.inject(PersonelService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Personel query and add missing value', () => {
        const isAkisi: IIsAkisi = { id: 456 };
        const personel: IPersonel = { id: 24482 };
        isAkisi.personel = personel;

        const personelCollection: IPersonel[] = [{ id: 31727 }];
        jest.spyOn(personelService, 'query').mockReturnValue(of(new HttpResponse({ body: personelCollection })));
        const additionalPersonels = [personel];
        const expectedCollection: IPersonel[] = [...additionalPersonels, ...personelCollection];
        jest.spyOn(personelService, 'addPersonelToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ isAkisi });
        comp.ngOnInit();

        expect(personelService.query).toHaveBeenCalled();
        expect(personelService.addPersonelToCollectionIfMissing).toHaveBeenCalledWith(personelCollection, ...additionalPersonels);
        expect(comp.personelsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const isAkisi: IIsAkisi = { id: 456 };
        const personel: IPersonel = { id: 38041 };
        isAkisi.personel = personel;

        activatedRoute.data = of({ isAkisi });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(isAkisi));
        expect(comp.personelsSharedCollection).toContain(personel);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<IsAkisi>>();
        const isAkisi = { id: 123 };
        jest.spyOn(isAkisiService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ isAkisi });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: isAkisi }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(isAkisiService.update).toHaveBeenCalledWith(isAkisi);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<IsAkisi>>();
        const isAkisi = new IsAkisi();
        jest.spyOn(isAkisiService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ isAkisi });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: isAkisi }));
        saveSubject.complete();

        // THEN
        expect(isAkisiService.create).toHaveBeenCalledWith(isAkisi);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<IsAkisi>>();
        const isAkisi = { id: 123 };
        jest.spyOn(isAkisiService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ isAkisi });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(isAkisiService.update).toHaveBeenCalledWith(isAkisi);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPersonelById', () => {
        it('Should return tracked Personel primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPersonelById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
