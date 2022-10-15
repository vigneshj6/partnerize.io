import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CustomerFormService, CustomerFormGroup } from './customer-form.service';
import { ICustomer } from '../customer.model';
import { CustomerService } from '../service/customer.service';
import { IPartner } from 'app/entities/partner/partner.model';
import { PartnerService } from 'app/entities/partner/service/partner.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';

@Component({
  selector: 'jhi-customer-update',
  templateUrl: './customer-update.component.html',
})
export class CustomerUpdateComponent implements OnInit {
  isSaving = false;
  customer: ICustomer | null = null;

  partnersSharedCollection: IPartner[] = [];
  companiesSharedCollection: ICompany[] = [];

  editForm: CustomerFormGroup = this.customerFormService.createCustomerFormGroup();

  constructor(
    protected customerService: CustomerService,
    protected customerFormService: CustomerFormService,
    protected partnerService: PartnerService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePartner = (o1: IPartner | null, o2: IPartner | null): boolean => this.partnerService.comparePartner(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customer }) => {
      this.customer = customer;
      if (customer) {
        this.updateForm(customer);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customer = this.customerFormService.getCustomer(this.editForm);
    if (customer.id !== null) {
      this.subscribeToSaveResponse(this.customerService.update(customer));
    } else {
      this.subscribeToSaveResponse(this.customerService.create(customer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomer>>): void {
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

  protected updateForm(customer: ICustomer): void {
    this.customer = customer;
    this.customerFormService.resetForm(this.editForm, customer);

    this.partnersSharedCollection = this.partnerService.addPartnerToCollectionIfMissing<IPartner>(
      this.partnersSharedCollection,
      customer.partner
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      customer.company
    );
  }

  protected loadRelationshipsOptions(): void {
    this.partnerService
      .query()
      .pipe(map((res: HttpResponse<IPartner[]>) => res.body ?? []))
      .pipe(map((partners: IPartner[]) => this.partnerService.addPartnerToCollectionIfMissing<IPartner>(partners, this.customer?.partner)))
      .subscribe((partners: IPartner[]) => (this.partnersSharedCollection = partners));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.customer?.company))
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }
}
