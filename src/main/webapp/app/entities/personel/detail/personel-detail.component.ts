import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonel } from '../personel.model';

@Component({
  selector: 'jhi-personel-detail',
  templateUrl: './personel-detail.component.html',
})
export class PersonelDetailComponent implements OnInit {
  personel: IPersonel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personel }) => {
      this.personel = personel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
