import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IIsAkisi, IsAkisi } from '../is-akisi.model';
import { IsAkisiService } from '../service/is-akisi.service';
import { IPersonel } from 'app/entities/personel/personel.model';
import { PersonelService } from 'app/entities/personel/service/personel.service';

@Component({
  selector: 'jhi-is-akisi-update',
  templateUrl: './is-akisi-update.component.html',
})
export class IsAkisiUpdateComponent implements OnInit {
  isSaving = false;

  personelsSharedCollection: IPersonel[] = [];

  editForm = this.fb.group({
    id: [],
    konu: [],
    aciklama: [],
    sonTarih: [],
    personel: [],
  });

  constructor(
    protected isAkisiService: IsAkisiService,
    protected personelService: PersonelService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ isAkisi }) => {
      this.updateForm(isAkisi);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const isAkisi = this.createFromForm();
    if (isAkisi.id !== undefined) {
      this.subscribeToSaveResponse(this.isAkisiService.update(isAkisi));
    } else {
      this.subscribeToSaveResponse(this.isAkisiService.create(isAkisi));
    }
  }

  trackPersonelById(index: number, item: IPersonel): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIsAkisi>>): void {
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

  protected updateForm(isAkisi: IIsAkisi): void {
    this.editForm.patchValue({
      id: isAkisi.id,
      konu: isAkisi.konu,
      aciklama: isAkisi.aciklama,
      sonTarih: isAkisi.sonTarih,
      personel: isAkisi.personel,
    });

    this.personelsSharedCollection = this.personelService.addPersonelToCollectionIfMissing(
      this.personelsSharedCollection,
      isAkisi.personel
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personelService
      .query()
      .pipe(map((res: HttpResponse<IPersonel[]>) => res.body ?? []))
      .pipe(
        map((personels: IPersonel[]) =>
          this.personelService.addPersonelToCollectionIfMissing(personels, this.editForm.get('personel')!.value)
        )
      )
      .subscribe((personels: IPersonel[]) => (this.personelsSharedCollection = personels));
  }

  protected createFromForm(): IIsAkisi {
    return {
      ...new IsAkisi(),
      id: this.editForm.get(['id'])!.value,
      konu: this.editForm.get(['konu'])!.value,
      aciklama: this.editForm.get(['aciklama'])!.value,
      sonTarih: this.editForm.get(['sonTarih'])!.value,
      personel: this.editForm.get(['personel'])!.value,
    };
  }
}
