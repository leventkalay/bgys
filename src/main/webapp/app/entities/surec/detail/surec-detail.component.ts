import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISurec } from '../surec.model';

@Component({
  selector: 'jhi-surec-detail',
  templateUrl: './surec-detail.component.html',
})
export class SurecDetailComponent implements OnInit {
  surec: ISurec | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ surec }) => {
      this.surec = surec;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
