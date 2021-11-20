import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITehditKategorisi } from '../tehdit-kategorisi.model';

@Component({
  selector: 'jhi-tehdit-kategorisi-detail',
  templateUrl: './tehdit-kategorisi-detail.component.html',
})
export class TehditKategorisiDetailComponent implements OnInit {
  tehditKategorisi: ITehditKategorisi | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tehditKategorisi }) => {
      this.tehditKategorisi = tehditKategorisi;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
