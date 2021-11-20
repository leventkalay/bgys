import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBirim } from '../birim.model';

@Component({
  selector: 'jhi-birim-detail',
  templateUrl: './birim-detail.component.html',
})
export class BirimDetailComponent implements OnInit {
  birim: IBirim | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ birim }) => {
      this.birim = birim;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
