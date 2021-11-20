import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBirim, Birim } from '../birim.model';
import { BirimService } from '../service/birim.service';

@Component({
  selector: 'jhi-birim-update',
  templateUrl: './birim-update.component.html',
})
export class BirimUpdateComponent implements OnInit {
  isSaving = false;

  birimsSharedCollection: IBirim[] = [];

  editForm = this.fb.group({
    id: [],
    adi: [null, [Validators.required]],
    soyadi: [null, [Validators.required]],
    ustBirim: [],
  });

  constructor(protected birimService: BirimService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ birim }) => {
      this.updateForm(birim);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const birim = this.createFromForm();
    if (birim.id !== undefined) {
      this.subscribeToSaveResponse(this.birimService.update(birim));
    } else {
      this.subscribeToSaveResponse(this.birimService.create(birim));
    }
  }

  trackBirimById(index: number, item: IBirim): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBirim>>): void {
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

  protected updateForm(birim: IBirim): void {
    this.editForm.patchValue({
      id: birim.id,
      adi: birim.adi,
      soyadi: birim.soyadi,
      ustBirim: birim.ustBirim,
    });

    this.birimsSharedCollection = this.birimService.addBirimToCollectionIfMissing(this.birimsSharedCollection, birim.ustBirim);
  }

  protected loadRelationshipsOptions(): void {
    this.birimService
      .query()
      .pipe(map((res: HttpResponse<IBirim[]>) => res.body ?? []))
      .pipe(map((birims: IBirim[]) => this.birimService.addBirimToCollectionIfMissing(birims, this.editForm.get('ustBirim')!.value)))
      .subscribe((birims: IBirim[]) => (this.birimsSharedCollection = birims));
  }

  protected createFromForm(): IBirim {
    return {
      ...new Birim(),
      id: this.editForm.get(['id'])!.value,
      adi: this.editForm.get(['adi'])!.value,
      soyadi: this.editForm.get(['soyadi'])!.value,
      ustBirim: this.editForm.get(['ustBirim'])!.value,
    };
  }
}
