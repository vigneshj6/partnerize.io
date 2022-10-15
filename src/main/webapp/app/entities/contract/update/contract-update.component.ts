import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ContractFormService, ContractFormGroup } from './contract-form.service';
import { IContract } from '../contract.model';
import { ContractService } from '../service/contract.service';
import { ICurrency } from 'app/entities/currency/currency.model';
import { CurrencyService } from 'app/entities/currency/service/currency.service';
import { IPartner } from 'app/entities/partner/partner.model';
import { PartnerService } from 'app/entities/partner/service/partner.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { ContractType } from 'app/entities/enumerations/contract-type.model';
import { BillingCycle } from 'app/entities/enumerations/billing-cycle.model';

@Component({
  selector: 'jhi-contract-update',
  templateUrl: './contract-update.component.html',
})
export class ContractUpdateComponent implements OnInit {
  isSaving = false;
  contract: IContract | null = null;
  contractTypeValues = Object.keys(ContractType);
  billingCycleValues = Object.keys(BillingCycle);

  currenciesSharedCollection: ICurrency[] = [];
  partnersSharedCollection: IPartner[] = [];
  companiesSharedCollection: ICompany[] = [];

  editForm: ContractFormGroup = this.contractFormService.createContractFormGroup();

  constructor(
    protected contractService: ContractService,
    protected contractFormService: ContractFormService,
    protected currencyService: CurrencyService,
    protected partnerService: PartnerService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCurrency = (o1: ICurrency | null, o2: ICurrency | null): boolean => this.currencyService.compareCurrency(o1, o2);

  comparePartner = (o1: IPartner | null, o2: IPartner | null): boolean => this.partnerService.comparePartner(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contract }) => {
      this.contract = contract;
      if (contract) {
        this.updateForm(contract);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contract = this.contractFormService.getContract(this.editForm);
    if (contract.id !== null) {
      this.subscribeToSaveResponse(this.contractService.update(contract));
    } else {
      this.subscribeToSaveResponse(this.contractService.create(contract));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContract>>): void {
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

  protected updateForm(contract: IContract): void {
    this.contract = contract;
    this.contractFormService.resetForm(this.editForm, contract);

    this.currenciesSharedCollection = this.currencyService.addCurrencyToCollectionIfMissing<ICurrency>(
      this.currenciesSharedCollection,
      contract.currency
    );
    this.partnersSharedCollection = this.partnerService.addPartnerToCollectionIfMissing<IPartner>(
      this.partnersSharedCollection,
      contract.partner
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      contract.company
    );
  }

  protected loadRelationshipsOptions(): void {
    this.currencyService
      .query()
      .pipe(map((res: HttpResponse<ICurrency[]>) => res.body ?? []))
      .pipe(
        map((currencies: ICurrency[]) =>
          this.currencyService.addCurrencyToCollectionIfMissing<ICurrency>(currencies, this.contract?.currency)
        )
      )
      .subscribe((currencies: ICurrency[]) => (this.currenciesSharedCollection = currencies));

    this.partnerService
      .query()
      .pipe(map((res: HttpResponse<IPartner[]>) => res.body ?? []))
      .pipe(map((partners: IPartner[]) => this.partnerService.addPartnerToCollectionIfMissing<IPartner>(partners, this.contract?.partner)))
      .subscribe((partners: IPartner[]) => (this.partnersSharedCollection = partners));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.contract?.company))
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }
}
