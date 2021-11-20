import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITehdit, Tehdit } from '../tehdit.model';
import { TehditService } from '../service/tehdit.service';
import { ITehditKategorisi } from 'app/entities/tehdit-kategorisi/tehdit-kategorisi.model';
import { TehditKategorisiService } from 'app/entities/tehdit-kategorisi/service/tehdit-kategorisi.service';
import { Onay } from 'app/entities/enumerations/onay.model';

@Component({
  selector: 'jhi-tehdit-update',
  templateUrl: './tehdit-update.component.html',
})
export class TehditUpdateComponent implements OnInit {
  isSaving = false;
  onayValues = Object.keys(Onay);

  tehditKategorisisSharedCollection: ITehditKategorisi[] = [];

  editForm = this.fb.group({
    id: [],
    adi: [null, [Validators.required]],
    aciklama: [],
    onayDurumu: [],
    kategori: [],
  });

  constructor(
    protected tehditService: TehditService,
    protected tehditKategorisiService: TehditKategorisiService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tehdit }) => {
      this.updateForm(tehdit);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tehdit = this.createFromForm();
    if (tehdit.id !== undefined) {
      this.subscribeToSaveResponse(this.tehditService.update(tehdit));
    } else {
      this.subscribeToSaveResponse(this.tehditService.create(tehdit));
    }
  }

  trackTehditKategorisiById(index: number, item: ITehditKategorisi): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITehdit>>): void {
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

  protected updateForm(tehdit: ITehdit): void {
    this.editForm.patchValue({
      id: tehdit.id,
      adi: tehdit.adi,
      aciklama: tehdit.aciklama,
      onayDurumu: tehdit.onayDurumu,
      kategori: tehdit.kategori,
    });

    this.tehditKategorisisSharedCollection = this.tehditKategorisiService.addTehditKategorisiToCollectionIfMissing(
      this.tehditKategorisisSharedCollection,
      tehdit.kategori
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tehditKategorisiService
      .query()
      .pipe(map((res: HttpResponse<ITehditKategorisi[]>) => res.body ?? []))
      .pipe(
        map((tehditKategorisis: ITehditKategorisi[]) =>
          this.tehditKategorisiService.addTehditKategorisiToCollectionIfMissing(tehditKategorisis, this.editForm.get('kategori')!.value)
        )
      )
      .subscribe((tehditKategorisis: ITehditKategorisi[]) => (this.tehditKategorisisSharedCollection = tehditKategorisis));
  }

  protected createFromForm(): ITehdit {
    return {
      ...new Tehdit(),
      id: this.editForm.get(['id'])!.value,
      adi: this.editForm.get(['adi'])!.value,
      aciklama: this.editForm.get(['aciklama'])!.value,
      onayDurumu: this.editForm.get(['onayDurumu'])!.value,
      kategori: this.editForm.get(['kategori'])!.value,
    };
  }
}
