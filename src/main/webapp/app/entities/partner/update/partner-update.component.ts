import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PartnerFormService, PartnerFormGroup } from './partner-form.service';
import { IPartner } from '../partner.model';
import { PartnerService } from '../service/partner.service';
import { ICard } from 'app/entities/card/card.model';
import { CardService } from 'app/entities/card/service/card.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';

@Component({
  selector: 'jhi-partner-update',
  templateUrl: './partner-update.component.html',
})
export class PartnerUpdateComponent implements OnInit {
  isSaving = false;
  partner: IPartner | null = null;

  cardsCollection: ICard[] = [];
  companiesSharedCollection: ICompany[] = [];

  editForm: PartnerFormGroup = this.partnerFormService.createPartnerFormGroup();

  constructor(
    protected partnerService: PartnerService,
    protected partnerFormService: PartnerFormService,
    protected cardService: CardService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCard = (o1: ICard | null, o2: ICard | null): boolean => this.cardService.compareCard(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partner }) => {
      this.partner = partner;
      if (partner) {
        this.updateForm(partner);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const partner = this.partnerFormService.getPartner(this.editForm);
    if (partner.id !== null) {
      this.subscribeToSaveResponse(this.partnerService.update(partner));
    } else {
      this.subscribeToSaveResponse(this.partnerService.create(partner));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPartner>>): void {
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

  protected updateForm(partner: IPartner): void {
    this.partner = partner;
    this.partnerFormService.resetForm(this.editForm, partner);

    this.cardsCollection = this.cardService.addCardToCollectionIfMissing<ICard>(this.cardsCollection, partner.card);
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      partner.company
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cardService
      .query({ filter: 'partner-is-null' })
      .pipe(map((res: HttpResponse<ICard[]>) => res.body ?? []))
      .pipe(map((cards: ICard[]) => this.cardService.addCardToCollectionIfMissing<ICard>(cards, this.partner?.card)))
      .subscribe((cards: ICard[]) => (this.cardsCollection = cards));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.partner?.company)))
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }
}
