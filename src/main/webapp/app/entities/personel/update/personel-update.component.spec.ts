jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PersonelService } from '../service/personel.service';
import { IPersonel, Personel } from '../personel.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IBirim } from 'app/entities/birim/birim.model';
import { BirimService } from 'app/entities/birim/service/birim.service';

import { PersonelUpdateComponent } from './personel-update.component';

describe('Component Tests', () => {
  describe('Personel Management Update Component', () => {
    let comp: PersonelUpdateComponent;
    let fixture: ComponentFixture<PersonelUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let personelService: PersonelService;
    let userService: UserService;
    let birimService: BirimService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonelUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PersonelUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonelUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      personelService = TestBed.inject(PersonelService);
      userService = TestBed.inject(UserService);
      birimService = TestBed.inject(BirimService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const personel: IPersonel = { id: 456 };
        const internalUser: IUser = { id: 77661 };
        personel.internalUser = internalUser;

        const userCollection: IUser[] = [{ id: 69664 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [internalUser];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ personel });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Birim query and add missing value', () => {
        const personel: IPersonel = { id: 456 };
        const birim: IBirim = { id: 61220 };
        personel.birim = birim;

        const birimCollection: IBirim[] = [{ id: 79850 }];
        jest.spyOn(birimService, 'query').mockReturnValue(of(new HttpResponse({ body: birimCollection })));
        const additionalBirims = [birim];
        const expectedCollection: IBirim[] = [...additionalBirims, ...birimCollection];
        jest.spyOn(birimService, 'addBirimToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ personel });
        comp.ngOnInit();

        expect(birimService.query).toHaveBeenCalled();
        expect(birimService.addBirimToCollectionIfMissing).toHaveBeenCalledWith(birimCollection, ...additionalBirims);
        expect(comp.birimsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const personel: IPersonel = { id: 456 };
        const internalUser: IUser = { id: 95974 };
        personel.internalUser = internalUser;
        const birim: IBirim = { id: 48442 };
        personel.birim = birim;

        activatedRoute.data = of({ personel });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(personel));
        expect(comp.usersSharedCollection).toContain(internalUser);
        expect(comp.birimsSharedCollection).toContain(birim);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Personel>>();
        const personel = { id: 123 };
        jest.spyOn(personelService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ personel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personel }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(personelService.update).toHaveBeenCalledWith(personel);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Personel>>();
        const personel = new Personel();
        jest.spyOn(personelService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ personel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personel }));
        saveSubject.complete();

        // THEN
        expect(personelService.create).toHaveBeenCalledWith(personel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Personel>>();
        const personel = { id: 123 };
        jest.spyOn(personelService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ personel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(personelService.update).toHaveBeenCalledWith(personel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

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
