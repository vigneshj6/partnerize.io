import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CompanyFormService, CompanyFormGroup } from './company-form.service';
import { ICompany } from '../company.model';
import { CompanyService } from '../service/company.service';
import { ICard } from 'app/entities/card/card.model';
import { CardService } from 'app/entities/card/service/card.service';

@Component({
  selector: 'jhi-company-update',
  templateUrl: './company-update.component.html',
})
export class CompanyUpdateComponent implements OnInit {
  isSaving = false;
  company: ICompany | null = null;

  cardsCollection: ICard[] = [];

  editForm: CompanyFormGroup = this.companyFormService.createCompanyFormGroup();

  constructor(
    protected companyService: CompanyService,
    protected companyFormService: CompanyFormService,
    protected cardService: CardService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCard = (o1: ICard | null, o2: ICard | null): boolean => this.cardService.compareCard(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ company }) => {
      this.company = company;
      if (company) {
        this.updateForm(company);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const company = this.companyFormService.getCompany(this.editForm);
    if (company.id !== null) {
      this.subscribeToSaveResponse(this.companyService.update(company));
    } else {
      this.subscribeToSaveResponse(this.companyService.create(company));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompany>>): void {
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

  protected updateForm(company: ICompany): void {
    this.company = company;
    this.companyFormService.resetForm(this.editForm, company);

    this.cardsCollection = this.cardService.addCardToCollectionIfMissing<ICard>(this.cardsCollection, company.card);
  }

  protected loadRelationshipsOptions(): void {
    this.cardService
      .query({ filter: 'company-is-null' })
      .pipe(map((res: HttpResponse<ICard[]>) => res.body ?? []))
      .pipe(map((cards: ICard[]) => this.cardService.addCardToCollectionIfMissing<ICard>(cards, this.company?.card)))
      .subscribe((cards: ICard[]) => (this.cardsCollection = cards));
  }
}
