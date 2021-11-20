import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRisk, Risk } from '../risk.model';
import { RiskService } from '../service/risk.service';
import { IVarlik } from 'app/entities/varlik/varlik.model';
import { VarlikService } from 'app/entities/varlik/service/varlik.service';
import { Onay } from 'app/entities/enumerations/onay.model';

@Component({
  selector: 'jhi-risk-update',
  templateUrl: './risk-update.component.html',
})
export class RiskUpdateComponent implements OnInit {
  isSaving = false;
  onayValues = Object.keys(Onay);

  varliksSharedCollection: IVarlik[] = [];

  editForm = this.fb.group({
    id: [],
    adi: [null, [Validators.required]],
    aciklama: [],
    onayDurumu: [],
    varliks: [],
  });

  constructor(
    protected riskService: RiskService,
    protected varlikService: VarlikService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ risk }) => {
      this.updateForm(risk);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const risk = this.createFromForm();
    if (risk.id !== undefined) {
      this.subscribeToSaveResponse(this.riskService.update(risk));
    } else {
      this.subscribeToSaveResponse(this.riskService.create(risk));
    }
  }

  trackVarlikById(index: number, item: IVarlik): number {
    return item.id!;
  }

  getSelectedVarlik(option: IVarlik, selectedVals?: IVarlik[]): IVarlik {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRisk>>): void {
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

  protected updateForm(risk: IRisk): void {
    this.editForm.patchValue({
      id: risk.id,
      adi: risk.adi,
      aciklama: risk.aciklama,
      onayDurumu: risk.onayDurumu,
      varliks: risk.varliks,
    });

    this.varliksSharedCollection = this.varlikService.addVarlikToCollectionIfMissing(this.varliksSharedCollection, ...(risk.varliks ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.varlikService
      .query()
      .pipe(map((res: HttpResponse<IVarlik[]>) => res.body ?? []))
      .pipe(
        map((varliks: IVarlik[]) =>
          this.varlikService.addVarlikToCollectionIfMissing(varliks, ...(this.editForm.get('varliks')!.value ?? []))
        )
      )
      .subscribe((varliks: IVarlik[]) => (this.varliksSharedCollection = varliks));
  }

  protected createFromForm(): IRisk {
    return {
      ...new Risk(),
      id: this.editForm.get(['id'])!.value,
      adi: this.editForm.get(['adi'])!.value,
      aciklama: this.editForm.get(['aciklama'])!.value,
      onayDurumu: this.editForm.get(['onayDurumu'])!.value,
      varliks: this.editForm.get(['varliks'])!.value,
    };
  }
}
