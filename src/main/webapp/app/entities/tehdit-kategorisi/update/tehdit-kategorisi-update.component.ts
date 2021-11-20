import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITehditKategorisi, TehditKategorisi } from '../tehdit-kategorisi.model';
import { TehditKategorisiService } from '../service/tehdit-kategorisi.service';

@Component({
  selector: 'jhi-tehdit-kategorisi-update',
  templateUrl: './tehdit-kategorisi-update.component.html',
})
export class TehditKategorisiUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    adi: [],
    aciklama: [],
  });

  constructor(
    protected tehditKategorisiService: TehditKategorisiService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tehditKategorisi }) => {
      this.updateForm(tehditKategorisi);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tehditKategorisi = this.createFromForm();
    if (tehditKategorisi.id !== undefined) {
      this.subscribeToSaveResponse(this.tehditKategorisiService.update(tehditKategorisi));
    } else {
      this.subscribeToSaveResponse(this.tehditKategorisiService.create(tehditKategorisi));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITehditKategorisi>>): void {
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

  protected updateForm(tehditKategorisi: ITehditKategorisi): void {
    this.editForm.patchValue({
      id: tehditKategorisi.id,
      adi: tehditKategorisi.adi,
      aciklama: tehditKategorisi.aciklama,
    });
  }

  protected createFromForm(): ITehditKategorisi {
    return {
      ...new TehditKategorisi(),
      id: this.editForm.get(['id'])!.value,
      adi: this.editForm.get(['adi'])!.value,
      aciklama: this.editForm.get(['aciklama'])!.value,
    };
  }
}
