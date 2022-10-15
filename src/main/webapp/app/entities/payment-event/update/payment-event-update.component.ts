import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PaymentEventFormService, PaymentEventFormGroup } from './payment-event-form.service';
import { IPaymentEvent } from '../payment-event.model';
import { PaymentEventService } from '../service/payment-event.service';
import { IPartnerPayment } from 'app/entities/partner-payment/partner-payment.model';
import { PartnerPaymentService } from 'app/entities/partner-payment/service/partner-payment.service';
import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';

@Component({
  selector: 'jhi-payment-event-update',
  templateUrl: './payment-event-update.component.html',
})
export class PaymentEventUpdateComponent implements OnInit {
  isSaving = false;
  paymentEvent: IPaymentEvent | null = null;
  paymentStatusValues = Object.keys(PaymentStatus);

  partnerPaymentsSharedCollection: IPartnerPayment[] = [];

  editForm: PaymentEventFormGroup = this.paymentEventFormService.createPaymentEventFormGroup();

  constructor(
    protected paymentEventService: PaymentEventService,
    protected paymentEventFormService: PaymentEventFormService,
    protected partnerPaymentService: PartnerPaymentService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePartnerPayment = (o1: IPartnerPayment | null, o2: IPartnerPayment | null): boolean =>
    this.partnerPaymentService.comparePartnerPayment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentEvent }) => {
      this.paymentEvent = paymentEvent;
      if (paymentEvent) {
        this.updateForm(paymentEvent);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paymentEvent = this.paymentEventFormService.getPaymentEvent(this.editForm);
    if (paymentEvent.id !== null) {
      this.subscribeToSaveResponse(this.paymentEventService.update(paymentEvent));
    } else {
      this.subscribeToSaveResponse(this.paymentEventService.create(paymentEvent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentEvent>>): void {
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

  protected updateForm(paymentEvent: IPaymentEvent): void {
    this.paymentEvent = paymentEvent;
    this.paymentEventFormService.resetForm(this.editForm, paymentEvent);

    this.partnerPaymentsSharedCollection = this.partnerPaymentService.addPartnerPaymentToCollectionIfMissing<IPartnerPayment>(
      this.partnerPaymentsSharedCollection,
      paymentEvent.partnerPayment
    );
  }

  protected loadRelationshipsOptions(): void {
    this.partnerPaymentService
      .query()
      .pipe(map((res: HttpResponse<IPartnerPayment[]>) => res.body ?? []))
      .pipe(
        map((partnerPayments: IPartnerPayment[]) =>
          this.partnerPaymentService.addPartnerPaymentToCollectionIfMissing<IPartnerPayment>(
            partnerPayments,
            this.paymentEvent?.partnerPayment
          )
        )
      )
      .subscribe((partnerPayments: IPartnerPayment[]) => (this.partnerPaymentsSharedCollection = partnerPayments));
  }
}
