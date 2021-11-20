import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IVarlikKategorisi, VarlikKategorisi } from '../varlik-kategorisi.model';
import { VarlikKategorisiService } from '../service/varlik-kategorisi.service';

@Component({
  selector: 'jhi-varlik-kategorisi-update',
  templateUrl: './varlik-kategorisi-update.component.html',
})
export class VarlikKategorisiUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    adi: [],
    aciklama: [],
  });

  constructor(
    protected varlikKategorisiService: VarlikKategorisiService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ varlikKategorisi }) => {
      this.updateForm(varlikKategorisi);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const varlikKategorisi = this.createFromForm();
    if (varlikKategorisi.id !== undefined) {
      this.subscribeToSaveResponse(this.varlikKategorisiService.update(varlikKategorisi));
    } else {
      this.subscribeToSaveResponse(this.varlikKategorisiService.create(varlikKategorisi));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVarlikKategorisi>>): void {
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

  protected updateForm(varlikKategorisi: IVarlikKategorisi): void {
    this.editForm.patchValue({
      id: varlikKategorisi.id,
      adi: varlikKategorisi.adi,
      aciklama: varlikKategorisi.aciklama,
    });
  }

  protected createFromForm(): IVarlikKategorisi {
    return {
      ...new VarlikKategorisi(),
      id: this.editForm.get(['id'])!.value,
      adi: this.editForm.get(['adi'])!.value,
      aciklama: this.editForm.get(['aciklama'])!.value,
    };
  }
}
