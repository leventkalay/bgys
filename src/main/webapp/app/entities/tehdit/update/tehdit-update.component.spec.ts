jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TehditService } from '../service/tehdit.service';
import { ITehdit, Tehdit } from '../tehdit.model';
import { ITehditKategorisi } from 'app/entities/tehdit-kategorisi/tehdit-kategorisi.model';
import { TehditKategorisiService } from 'app/entities/tehdit-kategorisi/service/tehdit-kategorisi.service';

import { TehditUpdateComponent } from './tehdit-update.component';

describe('Component Tests', () => {
  describe('Tehdit Management Update Component', () => {
    let comp: TehditUpdateComponent;
    let fixture: ComponentFixture<TehditUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tehditService: TehditService;
    let tehditKategorisiService: TehditKategorisiService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TehditUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TehditUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TehditUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tehditService = TestBed.inject(TehditService);
      tehditKategorisiService = TestBed.inject(TehditKategorisiService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call TehditKategorisi query and add missing value', () => {
        const tehdit: ITehdit = { id: 456 };
        const kategori: ITehditKategorisi = { id: 56936 };
        tehdit.kategori = kategori;

        const tehditKategorisiCollection: ITehditKategorisi[] = [{ id: 59992 }];
        jest.spyOn(tehditKategorisiService, 'query').mockReturnValue(of(new HttpResponse({ body: tehditKategorisiCollection })));
        const additionalTehditKategorisis = [kategori];
        const expectedCollection: ITehditKategorisi[] = [...additionalTehditKategorisis, ...tehditKategorisiCollection];
        jest.spyOn(tehditKategorisiService, 'addTehditKategorisiToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ tehdit });
        comp.ngOnInit();

        expect(tehditKategorisiService.query).toHaveBeenCalled();
        expect(tehditKategorisiService.addTehditKategorisiToCollectionIfMissing).toHaveBeenCalledWith(
          tehditKategorisiCollection,
          ...additionalTehditKategorisis
        );
        expect(comp.tehditKategorisisSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const tehdit: ITehdit = { id: 456 };
        const kategori: ITehditKategorisi = { id: 54004 };
        tehdit.kategori = kategori;

        activatedRoute.data = of({ tehdit });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tehdit));
        expect(comp.tehditKategorisisSharedCollection).toContain(kategori);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tehdit>>();
        const tehdit = { id: 123 };
        jest.spyOn(tehditService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tehdit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tehdit }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tehditService.update).toHaveBeenCalledWith(tehdit);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tehdit>>();
        const tehdit = new Tehdit();
        jest.spyOn(tehditService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tehdit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tehdit }));
        saveSubject.complete();

        // THEN
        expect(tehditService.create).toHaveBeenCalledWith(tehdit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tehdit>>();
        const tehdit = { id: 123 };
        jest.spyOn(tehditService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tehdit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tehditService.update).toHaveBeenCalledWith(tehdit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTehditKategorisiById', () => {
        it('Should return tracked TehditKategorisi primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTehditKategorisiById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
