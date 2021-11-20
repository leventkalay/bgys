import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIsAkisi } from '../is-akisi.model';

@Component({
  selector: 'jhi-is-akisi-detail',
  templateUrl: './is-akisi-detail.component.html',
})
export class IsAkisiDetailComponent implements OnInit {
  isAkisi: IIsAkisi | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ isAkisi }) => {
      this.isAkisi = isAkisi;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
