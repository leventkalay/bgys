jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VarlikService } from '../service/varlik.service';
import { IVarlik, Varlik } from '../varlik.model';
import { IVarlikKategorisi } from 'app/entities/varlik-kategorisi/varlik-kategorisi.model';
import { VarlikKategorisiService } from 'app/entities/varlik-kategorisi/service/varlik-kategorisi.service';
import { ISurec } from 'app/entities/surec/surec.model';
import { SurecService } from 'app/entities/surec/service/surec.service';
import { IPersonel } from 'app/entities/personel/personel.model';
import { PersonelService } from 'app/entities/personel/service/personel.service';

import { VarlikUpdateComponent } from './varlik-update.component';

describe('Component Tests', () => {
  describe('Varlik Management Update Component', () => {
    let comp: VarlikUpdateComponent;
    let fixture: ComponentFixture<VarlikUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let varlikService: VarlikService;
    let varlikKategorisiService: VarlikKategorisiService;
    let surecService: SurecService;
    let personelService: PersonelService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VarlikUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VarlikUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VarlikUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      varlikService = TestBed.inject(VarlikService);
      varlikKategorisiService = TestBed.inject(VarlikKategorisiService);
      surecService = TestBed.inject(SurecService);
      personelService = TestBed.inject(PersonelService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call VarlikKategorisi query and add missing value', () => {
        const varlik: IVarlik = { id: 456 };
        const kategori: IVarlikKategorisi = { id: 30394 };
        varlik.kategori = kategori;

        const varlikKategorisiCollection: IVarlikKategorisi[] = [{ id: 29067 }];
        jest.spyOn(varlikKategorisiService, 'query').mockReturnValue(of(new HttpResponse({ body: varlikKategorisiCollection })));
        const additionalVarlikKategorisis = [kategori];
        const expectedCollection: IVarlikKategorisi[] = [...additionalVarlikKategorisis, ...varlikKategorisiCollection];
        jest.spyOn(varlikKategorisiService, 'addVarlikKategorisiToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ varlik });
        comp.ngOnInit();

        expect(varlikKategorisiService.query).toHaveBeenCalled();
        expect(varlikKategorisiService.addVarlikKategorisiToCollectionIfMissing).toHaveBeenCalledWith(
          varlikKategorisiCollection,
          ...additionalVarlikKategorisis
        );
        expect(comp.varlikKategorisisSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Surec query and add missing value', () => {
        const varlik: IVarlik = { id: 456 };
        const surec: ISurec = { id: 643 };
        varlik.surec = surec;

        const surecCollection: ISurec[] = [{ id: 2606 }];
        jest.spyOn(surecService, 'query').mockReturnValue(of(new HttpResponse({ body: surecCollection })));
        const additionalSurecs = [surec];
        const expectedCollection: ISurec[] = [...additionalSurecs, ...surecCollection];
        jest.spyOn(surecService, 'addSurecToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ varlik });
        comp.ngOnInit();

        expect(surecService.query).toHaveBeenCalled();
        expect(surecService.addSurecToCollectionIfMissing).toHaveBeenCalledWith(surecCollection, ...additionalSurecs);
        expect(comp.surecsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Personel query and add missing value', () => {
        const varlik: IVarlik = { id: 456 };
        const ilkSahibi: IPersonel = { id: 5621 };
        varlik.ilkSahibi = ilkSahibi;
        const ikinciSahibi: IPersonel = { id: 94608 };
        varlik.ikinciSahibi = ikinciSahibi;

        const personelCollection: IPersonel[] = [{ id: 77295 }];
        jest.spyOn(personelService, 'query').mockReturnValue(of(new HttpResponse({ body: personelCollection })));
        const additionalPersonels = [ilkSahibi, ikinciSahibi];
        const expectedCollection: IPersonel[] = [...additionalPersonels, ...personelCollection];
        jest.spyOn(personelService, 'addPersonelToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ varlik });
        comp.ngOnInit();

        expect(personelService.query).toHaveBeenCalled();
        expect(personelService.addPersonelToCollectionIfMissing).toHaveBeenCalledWith(personelCollection, ...additionalPersonels);
        expect(comp.personelsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const varlik: IVarlik = { id: 456 };
        const kategori: IVarlikKategorisi = { id: 84450 };
        varlik.kategori = kategori;
        const surec: ISurec = { id: 5288 };
        varlik.surec = surec;
        const ilkSahibi: IPersonel = { id: 3212 };
        varlik.ilkSahibi = ilkSahibi;
        const ikinciSahibi: IPersonel = { id: 29009 };
        varlik.ikinciSahibi = ikinciSahibi;

        activatedRoute.data = of({ varlik });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(varlik));
        expect(comp.varlikKategorisisSharedCollection).toContain(kategori);
        expect(comp.surecsSharedCollection).toContain(surec);
        expect(comp.personelsSharedCollection).toContain(ilkSahibi);
        expect(comp.personelsSharedCollection).toContain(ikinciSahibi);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Varlik>>();
        const varlik = { id: 123 };
        jest.spyOn(varlikService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ varlik });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: varlik }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(varlikService.update).toHaveBeenCalledWith(varlik);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Varlik>>();
        const varlik = new Varlik();
        jest.spyOn(varlikService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ varlik });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: varlik }));
        saveSubject.complete();

        // THEN
        expect(varlikService.create).toHaveBeenCalledWith(varlik);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Varlik>>();
        const varlik = { id: 123 };
        jest.spyOn(varlikService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ varlik });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(varlikService.update).toHaveBeenCalledWith(varlik);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackVarlikKategorisiById', () => {
        it('Should return tracked VarlikKategorisi primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVarlikKategorisiById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackSurecById', () => {
        it('Should return tracked Surec primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSurecById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

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
