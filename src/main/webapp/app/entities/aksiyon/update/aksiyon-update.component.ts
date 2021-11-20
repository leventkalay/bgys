import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAksiyon, Aksiyon } from '../aksiyon.model';
import { AksiyonService } from '../service/aksiyon.service';
import { Onay } from 'app/entities/enumerations/onay.model';

@Component({
  selector: 'jhi-aksiyon-update',
  templateUrl: './aksiyon-update.component.html',
})
export class AksiyonUpdateComponent implements OnInit {
  isSaving = false;
  onayValues = Object.keys(Onay);

  editForm = this.fb.group({
    id: [],
    adi: [null, [Validators.required]],
    aciklama: [],
    onayDurumu: [],
  });

  constructor(protected aksiyonService: AksiyonService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aksiyon }) => {
      this.updateForm(aksiyon);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aksiyon = this.createFromForm();
    if (aksiyon.id !== undefined) {
      this.subscribeToSaveResponse(this.aksiyonService.update(aksiyon));
    } else {
      this.subscribeToSaveResponse(this.aksiyonService.create(aksiyon));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAksiyon>>): void {
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

  protected updateForm(aksiyon: IAksiyon): void {
    this.editForm.patchValue({
      id: aksiyon.id,
      adi: aksiyon.adi,
      aciklama: aksiyon.aciklama,
      onayDurumu: aksiyon.onayDurumu,
    });
  }

  protected createFromForm(): IAksiyon {
    return {
      ...new Aksiyon(),
      id: this.editForm.get(['id'])!.value,
      adi: this.editForm.get(['adi'])!.value,
      aciklama: this.editForm.get(['aciklama'])!.value,
      onayDurumu: this.editForm.get(['onayDurumu'])!.value,
    };
  }
}
