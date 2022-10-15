import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPaymentEvent, NewPaymentEvent } from '../payment-event.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaymentEvent for edit and NewPaymentEventFormGroupInput for create.
 */
type PaymentEventFormGroupInput = IPaymentEvent | PartialWithRequiredKeyOf<NewPaymentEvent>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPaymentEvent | NewPaymentEvent> = Omit<T, 'on'> & {
  on?: string | null;
};

type PaymentEventFormRawValue = FormValueOf<IPaymentEvent>;

type NewPaymentEventFormRawValue = FormValueOf<NewPaymentEvent>;

type PaymentEventFormDefaults = Pick<NewPaymentEvent, 'id' | 'on'>;

type PaymentEventFormGroupContent = {
  id: FormControl<PaymentEventFormRawValue['id'] | NewPaymentEvent['id']>;
  status: FormControl<PaymentEventFormRawValue['status']>;
  reason: FormControl<PaymentEventFormRawValue['reason']>;
  on: FormControl<PaymentEventFormRawValue['on']>;
  totalAmount: FormControl<PaymentEventFormRawValue['totalAmount']>;
  invoice: FormControl<PaymentEventFormRawValue['invoice']>;
  partnerPayment: FormControl<PaymentEventFormRawValue['partnerPayment']>;
};

export type PaymentEventFormGroup = FormGroup<PaymentEventFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaymentEventFormService {
  createPaymentEventFormGroup(paymentEvent: PaymentEventFormGroupInput = { id: null }): PaymentEventFormGroup {
    const paymentEventRawValue = this.convertPaymentEventToPaymentEventRawValue({
      ...this.getFormDefaults(),
      ...paymentEvent,
    });
    return new FormGroup<PaymentEventFormGroupContent>({
      id: new FormControl(
        { value: paymentEventRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      status: new FormControl(paymentEventRawValue.status),
      reason: new FormControl(paymentEventRawValue.reason),
      on: new FormControl(paymentEventRawValue.on),
      totalAmount: new FormControl(paymentEventRawValue.totalAmount),
      invoice: new FormControl(paymentEventRawValue.invoice),
      partnerPayment: new FormControl(paymentEventRawValue.partnerPayment),
    });
  }

  getPaymentEvent(form: PaymentEventFormGroup): IPaymentEvent | NewPaymentEvent {
    return this.convertPaymentEventRawValueToPaymentEvent(form.getRawValue() as PaymentEventFormRawValue | NewPaymentEventFormRawValue);
  }

  resetForm(form: PaymentEventFormGroup, paymentEvent: PaymentEventFormGroupInput): void {
    const paymentEventRawValue = this.convertPaymentEventToPaymentEventRawValue({ ...this.getFormDefaults(), ...paymentEvent });
    form.reset(
      {
        ...paymentEventRawValue,
        id: { value: paymentEventRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PaymentEventFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      on: currentTime,
    };
  }

  private convertPaymentEventRawValueToPaymentEvent(
    rawPaymentEvent: PaymentEventFormRawValue | NewPaymentEventFormRawValue
  ): IPaymentEvent | NewPaymentEvent {
    return {
      ...rawPaymentEvent,
      on: dayjs(rawPaymentEvent.on, DATE_TIME_FORMAT),
    };
  }

  private convertPaymentEventToPaymentEventRawValue(
    paymentEvent: IPaymentEvent | (Partial<NewPaymentEvent> & PaymentEventFormDefaults)
  ): PaymentEventFormRawValue | PartialWithRequiredKeyOf<NewPaymentEventFormRawValue> {
    return {
      ...paymentEvent,
      on: paymentEvent.on ? paymentEvent.on.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
