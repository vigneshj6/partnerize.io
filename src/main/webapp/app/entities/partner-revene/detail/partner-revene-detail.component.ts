import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPartnerRevene } from '../partner-revene.model';

@Component({
  selector: 'jhi-partner-revene-detail',
  templateUrl: './partner-revene-detail.component.html',
})
export class PartnerReveneDetailComponent implements OnInit {
  partnerRevene: IPartnerRevene | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partnerRevene }) => {
      this.partnerRevene = partnerRevene;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
