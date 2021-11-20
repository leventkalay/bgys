jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RiskService } from '../service/risk.service';
import { IRisk, Risk } from '../risk.model';
import { IVarlik } from 'app/entities/varlik/varlik.model';
import { VarlikService } from 'app/entities/varlik/service/varlik.service';

import { RiskUpdateComponent } from './risk-update.component';

describe('Component Tests', () => {
  describe('Risk Management Update Component', () => {
    let comp: RiskUpdateComponent;
    let fixture: ComponentFixture<RiskUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let riskService: RiskService;
    let varlikService: VarlikService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RiskUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RiskUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RiskUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      riskService = TestBed.inject(RiskService);
      varlikService = TestBed.inject(VarlikService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Varlik query and add missing value', () => {
        const risk: IRisk = { id: 456 };
        const varliks: IVarlik[] = [{ id: 51830 }];
        risk.varliks = varliks;

        const varlikCollection: IVarlik[] = [{ id: 63940 }];
        jest.spyOn(varlikService, 'query').mockReturnValue(of(new HttpResponse({ body: varlikCollection })));
        const additionalVarliks = [...varliks];
        const expectedCollection: IVarlik[] = [...additionalVarliks, ...varlikCollection];
        jest.spyOn(varlikService, 'addVarlikToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ risk });
        comp.ngOnInit();

        expect(varlikService.query).toHaveBeenCalled();
        expect(varlikService.addVarlikToCollectionIfMissing).toHaveBeenCalledWith(varlikCollection, ...additionalVarliks);
        expect(comp.varliksSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const risk: IRisk = { id: 456 };
        const varliks: IVarlik = { id: 37210 };
        risk.varliks = [varliks];

        activatedRoute.data = of({ risk });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(risk));
        expect(comp.varliksSharedCollection).toContain(varliks);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Risk>>();
        const risk = { id: 123 };
        jest.spyOn(riskService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ risk });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: risk }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(riskService.update).toHaveBeenCalledWith(risk);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Risk>>();
        const risk = new Risk();
        jest.spyOn(riskService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ risk });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: risk }));
        saveSubject.complete();

        // THEN
        expect(riskService.create).toHaveBeenCalledWith(risk);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Risk>>();
        const risk = { id: 123 };
        jest.spyOn(riskService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ risk });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(riskService.update).toHaveBeenCalledWith(risk);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackVarlikById', () => {
        it('Should return tracked Varlik primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVarlikById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedVarlik', () => {
        it('Should return option if no Varlik is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedVarlik(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Varlik for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedVarlik(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Varlik is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedVarlik(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
