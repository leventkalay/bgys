import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVarlik, Varlik } from '../varlik.model';
import { VarlikService } from '../service/varlik.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IVarlikKategorisi } from 'app/entities/varlik-kategorisi/varlik-kategorisi.model';
import { VarlikKategorisiService } from 'app/entities/varlik-kategorisi/service/varlik-kategorisi.service';
import { ISurec } from 'app/entities/surec/surec.model';
import { SurecService } from 'app/entities/surec/service/surec.service';
import { IPersonel } from 'app/entities/personel/personel.model';
import { PersonelService } from 'app/entities/personel/service/personel.service';
import { Seviye } from 'app/entities/enumerations/seviye.model';
import { Siniflandirma } from 'app/entities/enumerations/siniflandirma.model';
import { Onay } from 'app/entities/enumerations/onay.model';
import { Durumu } from 'app/entities/enumerations/durumu.model';

@Component({
  selector: 'jhi-varlik-update',
  templateUrl: './varlik-update.component.html',
})
export class VarlikUpdateComponent implements OnInit {
  isSaving = false;
  seviyeValues = Object.keys(Seviye);
  siniflandirmaValues = Object.keys(Siniflandirma);
  onayValues = Object.keys(Onay);
  durumuValues = Object.keys(Durumu);

  varlikKategorisisSharedCollection: IVarlikKategorisi[] = [];
  surecsSharedCollection: ISurec[] = [];
  personelsSharedCollection: IPersonel[] = [];

  editForm = this.fb.group({
    id: [],
    adi: [null, [Validators.required]],
    yeri: [],
    aciklama: [],
    gizlilik: [null, [Validators.required]],
    butunluk: [null, [Validators.required]],
    erisebilirlik: [null, [Validators.required]],
    siniflandirma: [],
    onayDurumu: [],
    durumu: [],
    kategoriRiskleri: [],
    kategori: [],
    surec: [],
    ilkSahibi: [],
    ikinciSahibi: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected varlikService: VarlikService,
    protected varlikKategorisiService: VarlikKategorisiService,
    protected surecService: SurecService,
    protected personelService: PersonelService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ varlik }) => {
      this.updateForm(varlik);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('bgysApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const varlik = this.createFromForm();
    if (varlik.id !== undefined) {
      this.subscribeToSaveResponse(this.varlikService.update(varlik));
    } else {
      this.subscribeToSaveResponse(this.varlikService.create(varlik));
    }
  }

  trackVarlikKategorisiById(index: number, item: IVarlikKategorisi): number {
    return item.id!;
  }

  trackSurecById(index: number, item: ISurec): number {
    return item.id!;
  }

  trackPersonelById(index: number, item: IPersonel): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVarlik>>): void {
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

  protected updateForm(varlik: IVarlik): void {
    this.editForm.patchValue({
      id: varlik.id,
      adi: varlik.adi,
      yeri: varlik.yeri,
      aciklama: varlik.aciklama,
      gizlilik: varlik.gizlilik,
      butunluk: varlik.butunluk,
      erisebilirlik: varlik.erisebilirlik,
      siniflandirma: varlik.siniflandirma,
      onayDurumu: varlik.onayDurumu,
      durumu: varlik.durumu,
      kategoriRiskleri: varlik.kategoriRiskleri,
      kategori: varlik.kategori,
      surec: varlik.surec,
      ilkSahibi: varlik.ilkSahibi,
      ikinciSahibi: varlik.ikinciSahibi,
    });

    this.varlikKategorisisSharedCollection = this.varlikKategorisiService.addVarlikKategorisiToCollectionIfMissing(
      this.varlikKategorisisSharedCollection,
      varlik.kategori
    );
    this.surecsSharedCollection = this.surecService.addSurecToCollectionIfMissing(this.surecsSharedCollection, varlik.surec);
    this.personelsSharedCollection = this.personelService.addPersonelToCollectionIfMissing(
      this.personelsSharedCollection,
      varlik.ilkSahibi,
      varlik.ikinciSahibi
    );
  }

  protected loadRelationshipsOptions(): void {
    this.varlikKategorisiService
      .query()
      .pipe(map((res: HttpResponse<IVarlikKategorisi[]>) => res.body ?? []))
      .pipe(
        map((varlikKategorisis: IVarlikKategorisi[]) =>
          this.varlikKategorisiService.addVarlikKategorisiToCollectionIfMissing(varlikKategorisis, this.editForm.get('kategori')!.value)
        )
      )
      .subscribe((varlikKategorisis: IVarlikKategorisi[]) => (this.varlikKategorisisSharedCollection = varlikKategorisis));

    this.surecService
      .query()
      .pipe(map((res: HttpResponse<ISurec[]>) => res.body ?? []))
      .pipe(map((surecs: ISurec[]) => this.surecService.addSurecToCollectionIfMissing(surecs, this.editForm.get('surec')!.value)))
      .subscribe((surecs: ISurec[]) => (this.surecsSharedCollection = surecs));

    this.personelService
      .query()
      .pipe(map((res: HttpResponse<IPersonel[]>) => res.body ?? []))
      .pipe(
        map((personels: IPersonel[]) =>
          this.personelService.addPersonelToCollectionIfMissing(
            personels,
            this.editForm.get('ilkSahibi')!.value,
            this.editForm.get('ikinciSahibi')!.value
          )
        )
      )
      .subscribe((personels: IPersonel[]) => (this.personelsSharedCollection = personels));
  }

  protected createFromForm(): IVarlik {
    return {
      ...new Varlik(),
      id: this.editForm.get(['id'])!.value,
      adi: this.editForm.get(['adi'])!.value,
      yeri: this.editForm.get(['yeri'])!.value,
      aciklama: this.editForm.get(['aciklama'])!.value,
      gizlilik: this.editForm.get(['gizlilik'])!.value,
      butunluk: this.editForm.get(['butunluk'])!.value,
      erisebilirlik: this.editForm.get(['erisebilirlik'])!.value,
      siniflandirma: this.editForm.get(['siniflandirma'])!.value,
      onayDurumu: this.editForm.get(['onayDurumu'])!.value,
      durumu: this.editForm.get(['durumu'])!.value,
      kategoriRiskleri: this.editForm.get(['kategoriRiskleri'])!.value,
      kategori: this.editForm.get(['kategori'])!.value,
      surec: this.editForm.get(['surec'])!.value,
      ilkSahibi: this.editForm.get(['ilkSahibi'])!.value,
      ikinciSahibi: this.editForm.get(['ikinciSahibi'])!.value,
    };
  }
}
