import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITehdit } from '../tehdit.model';

@Component({
  selector: 'jhi-tehdit-detail',
  templateUrl: './tehdit-detail.component.html',
})
export class TehditDetailComponent implements OnInit {
  tehdit: ITehdit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tehdit }) => {
      this.tehdit = tehdit;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
