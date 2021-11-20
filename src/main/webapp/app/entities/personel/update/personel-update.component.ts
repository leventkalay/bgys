import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPersonel, Personel } from '../personel.model';
import { PersonelService } from '../service/personel.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IBirim } from 'app/entities/birim/birim.model';
import { BirimService } from 'app/entities/birim/service/birim.service';

@Component({
  selector: 'jhi-personel-update',
  templateUrl: './personel-update.component.html',
})
export class PersonelUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  birimsSharedCollection: IBirim[] = [];

  editForm = this.fb.group({
    id: [],
    adi: [null, [Validators.required]],
    soyadi: [null, [Validators.required]],
    internalUser: [],
    birim: [],
  });

  constructor(
    protected personelService: PersonelService,
    protected userService: UserService,
    protected birimService: BirimService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personel }) => {
      this.updateForm(personel);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personel = this.createFromForm();
    if (personel.id !== undefined) {
      this.subscribeToSaveResponse(this.personelService.update(personel));
    } else {
      this.subscribeToSaveResponse(this.personelService.create(personel));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackBirimById(index: number, item: IBirim): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonel>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(personel: IPersonel): void {
    this.editForm.patchValue({
      id: personel.id,
      adi: personel.adi,
      soyadi: personel.soyadi,
      internalUser: personel.internalUser,
      birim: personel.birim,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, personel.internalUser);
    this.birimsSharedCollection = this.birimService.addBirimToCollectionIfMissing(this.birimsSharedCollection, personel.birim);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('internalUser')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.birimService
      .query()
      .pipe(map((res: HttpResponse<IBirim[]>) => res.body ?? []))
      .pipe(map((birims: IBirim[]) => this.birimService.addBirimToCollectionIfMissing(birims, this.editForm.get('birim')!.value)))
      .subscribe((birims: IBirim[]) => (this.birimsSharedCollection = birims));
  }

  protected createFromForm(): IPersonel {
    return {
      ...new Personel(),
      id: this.editForm.get(['id'])!.value,
      adi: this.editForm.get(['adi'])!.value,
      soyadi: this.editForm.get(['soyadi'])!.value,
      internalUser: this.editForm.get(['internalUser'])!.value,
      birim: this.editForm.get(['birim'])!.value,
    };
  }
}
