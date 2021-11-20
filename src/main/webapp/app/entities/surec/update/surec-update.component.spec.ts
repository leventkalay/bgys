jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SurecService } from '../service/surec.service';
import { ISurec, Surec } from '../surec.model';

import { SurecUpdateComponent } from './surec-update.component';

describe('Component Tests', () => {
  describe('Surec Management Update Component', () => {
    let comp: SurecUpdateComponent;
    let fixture: ComponentFixture<SurecUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let surecService: SurecService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SurecUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SurecUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SurecUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      surecService = TestBed.inject(SurecService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const surec: ISurec = { id: 456 };

        activatedRoute.data = of({ surec });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(surec));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Surec>>();
        const surec = { id: 123 };
        jest.spyOn(surecService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ surec });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: surec }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(surecService.update).toHaveBeenCalledWith(surec);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Surec>>();
        const surec = new Surec();
        jest.spyOn(surecService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ surec });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: surec }));
        saveSubject.complete();

        // THEN
        expect(surecService.create).toHaveBeenCalledWith(surec);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Surec>>();
        const surec = { id: 123 };
        jest.spyOn(surecService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ surec });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(surecService.update).toHaveBeenCalledWith(surec);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
