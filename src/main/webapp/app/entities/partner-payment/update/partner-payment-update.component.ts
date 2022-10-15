import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PartnerPaymentFormService, PartnerPaymentFormGroup } from './partner-payment-form.service';
import { IPartnerPayment } from '../partner-payment.model';
import { PartnerPaymentService } from '../service/partner-payment.service';
import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { IPartner } from 'app/entities/partner/partner.model';
import { PartnerService } from 'app/entities/partner/service/partner.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';

@Component({
  selector: 'jhi-partner-payment-update',
  templateUrl: './partner-payment-update.component.html',
})
export class PartnerPaymentUpdateComponent implements OnInit {
  isSaving = false;
  partnerPayment: IPartnerPayment | null = null;
  paymentStatusValues = Object.keys(PaymentStatus);

  contractsSharedCollection: IContract[] = [];
  partnersSharedCollection: IPartner[] = [];
  companiesSharedCollection: ICompany[] = [];

  editForm: PartnerPaymentFormGroup = this.partnerPaymentFormService.createPartnerPaymentFormGroup();

  constructor(
    protected partnerPaymentService: PartnerPaymentService,
    protected partnerPaymentFormService: PartnerPaymentFormService,
    protected contractService: ContractService,
    protected partnerService: PartnerService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareContract = (o1: IContract | null, o2: IContract | null): boolean => this.contractService.compareContract(o1, o2);

  comparePartner = (o1: IPartner | null, o2: IPartner | null): boolean => this.partnerService.comparePartner(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partnerPayment }) => {
      this.partnerPayment = partnerPayment;
      if (partnerPayment) {
        this.updateForm(partnerPayment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const partnerPayment = this.partnerPaymentFormService.getPartnerPayment(this.editForm);
    if (partnerPayment.id !== null) {
      this.subscribeToSaveResponse(this.partnerPaymentService.update(partnerPayment));
    } else {
      this.subscribeToSaveResponse(this.partnerPaymentService.create(partnerPayment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPartnerPayment>>): void {
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

  protected updateForm(partnerPayment: IPartnerPayment): void {
    this.partnerPayment = partnerPayment;
    this.partnerPaymentFormService.resetForm(this.editForm, partnerPayment);

    this.contractsSharedCollection = this.contractService.addContractToCollectionIfMissing<IContract>(
      this.contractsSharedCollection,
      partnerPayment.contract
    );
    this.partnersSharedCollection = this.partnerService.addPartnerToCollectionIfMissing<IPartner>(
      this.partnersSharedCollection,
      partnerPayment.partner
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      partnerPayment.company
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contractService
      .query()
      .pipe(map((res: HttpResponse<IContract[]>) => res.body ?? []))
      .pipe(
        map((contracts: IContract[]) =>
          this.contractService.addContractToCollectionIfMissing<IContract>(contracts, this.partnerPayment?.contract)
        )
      )
      .subscribe((contracts: IContract[]) => (this.contractsSharedCollection = contracts));

    this.partnerService
      .query()
      .pipe(map((res: HttpResponse<IPartner[]>) => res.body ?? []))
      .pipe(
        map((partners: IPartner[]) => this.partnerService.addPartnerToCollectionIfMissing<IPartner>(partners, this.partnerPayment?.partner))
      )
      .subscribe((partners: IPartner[]) => (this.partnersSharedCollection = partners));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) =>
          this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.partnerPayment?.company)
        )
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }
}
