import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAksiyon } from '../aksiyon.model';

@Component({
  selector: 'jhi-aksiyon-detail',
  templateUrl: './aksiyon-detail.component.html',
})
export class AksiyonDetailComponent implements OnInit {
  aksiyon: IAksiyon | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aksiyon }) => {
      this.aksiyon = aksiyon;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
