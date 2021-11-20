import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISurec, Surec } from '../surec.model';
import { SurecService } from '../service/surec.service';

@Component({
  selector: 'jhi-surec-update',
  templateUrl: './surec-update.component.html',
})
export class SurecUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    adi: [],
    aciklama: [],
  });

  constructor(protected surecService: SurecService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ surec }) => {
      this.updateForm(surec);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const surec = this.createFromForm();
    if (surec.id !== undefined) {
      this.subscribeToSaveResponse(this.surecService.update(surec));
    } else {
      this.subscribeToSaveResponse(this.surecService.create(surec));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISurec>>): void {
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

  protected updateForm(surec: ISurec): void {
    this.editForm.patchValue({
      id: surec.id,
      adi: surec.adi,
      aciklama: surec.aciklama,
    });
  }

  protected createFromForm(): ISurec {
    return {
      ...new Surec(),
      id: this.editForm.get(['id'])!.value,
      adi: this.editForm.get(['adi'])!.value,
      aciklama: this.editForm.get(['aciklama'])!.value,
    };
  }
}
