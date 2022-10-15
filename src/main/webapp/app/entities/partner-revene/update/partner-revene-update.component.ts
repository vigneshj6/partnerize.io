import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PartnerReveneFormService, PartnerReveneFormGroup } from './partner-revene-form.service';
import { IPartnerRevene } from '../partner-revene.model';
import { PartnerReveneService } from '../service/partner-revene.service';

@Component({
  selector: 'jhi-partner-revene-update',
  templateUrl: './partner-revene-update.component.html',
})
export class PartnerReveneUpdateComponent implements OnInit {
  isSaving = false;
  partnerRevene: IPartnerRevene | null = null;

  editForm: PartnerReveneFormGroup = this.partnerReveneFormService.createPartnerReveneFormGroup();

  constructor(
    protected partnerReveneService: PartnerReveneService,
    protected partnerReveneFormService: PartnerReveneFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partnerRevene }) => {
      this.partnerRevene = partnerRevene;
      if (partnerRevene) {
        this.updateForm(partnerRevene);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const partnerRevene = this.partnerReveneFormService.getPartnerRevene(this.editForm);
    if (partnerRevene.id !== null) {
      this.subscribeToSaveResponse(this.partnerReveneService.update(partnerRevene));
    } else {
      this.subscribeToSaveResponse(this.partnerReveneService.create(partnerRevene));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPartnerRevene>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
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

  protected updateForm(partnerRevene: IPartnerRevene): void {
    this.partnerRevene = partnerRevene;
    this.partnerReveneFormService.resetForm(this.editForm, partnerRevene);
  }
}
