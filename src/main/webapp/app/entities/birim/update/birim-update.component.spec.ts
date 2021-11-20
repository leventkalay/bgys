jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BirimService } from '../service/birim.service';
import { IBirim, Birim } from '../birim.model';

import { BirimUpdateComponent } from './birim-update.component';

describe('Component Tests', () => {
  describe('Birim Management Update Component', () => {
    let comp: BirimUpdateComponent;
    let fixture: ComponentFixture<BirimUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let birimService: BirimService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BirimUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BirimUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BirimUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      birimService = TestBed.inject(BirimService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Birim query and add missing value', () => {
        const birim: IBirim = { id: 456 };
        const ustBirim: IBirim = { id: 41803 };
        birim.ustBirim = ustBirim;

        const birimCollection: IBirim[] = [{ id: 44110 }];
        jest.spyOn(birimService, 'query').mockReturnValue(of(new HttpResponse({ body: birimCollection })));
        const additionalBirims = [ustBirim];
        const expectedCollection: IBirim[] = [...additionalBirims, ...birimCollection];
        jest.spyOn(birimService, 'addBirimToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ birim });
        comp.ngOnInit();

        expect(birimService.query).toHaveBeenCalled();
        expect(birimService.addBirimToCollectionIfMissing).toHaveBeenCalledWith(birimCollection, ...additionalBirims);
        expect(comp.birimsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const birim: IBirim = { id: 456 };
        const ustBirim: IBirim = { id: 38026 };
        birim.ustBirim = ustBirim;

        activatedRoute.data = of({ birim });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(birim));
        expect(comp.birimsSharedCollection).toContain(ustBirim);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Birim>>();
        const birim = { id: 123 };
        jest.spyOn(birimService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ birim });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: birim }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(birimService.update).toHaveBeenCalledWith(birim);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Birim>>();
        const birim = new Birim();
        jest.spyOn(birimService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ birim });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: birim }));
        saveSubject.complete();

        // THEN
        expect(birimService.create).toHaveBeenCalledWith(birim);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Birim>>();
        const birim = { id: 123 };
        jest.spyOn(birimService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ birim });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(birimService.update).toHaveBeenCalledWith(birim);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackBirimById', () => {
        it('Should return tracked Birim primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBirimById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
