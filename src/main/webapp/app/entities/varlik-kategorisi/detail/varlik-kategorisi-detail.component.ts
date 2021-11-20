import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVarlikKategorisi } from '../varlik-kategorisi.model';

@Component({
  selector: 'jhi-varlik-kategorisi-detail',
  templateUrl: './varlik-kategorisi-detail.component.html',
})
export class VarlikKategorisiDetailComponent implements OnInit {
  varlikKategorisi: IVarlikKategorisi | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ varlikKategorisi }) => {
      this.varlikKategorisi = varlikKategorisi;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
