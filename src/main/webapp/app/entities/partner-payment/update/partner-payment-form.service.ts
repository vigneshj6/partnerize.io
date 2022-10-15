import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPartnerPayment, NewPartnerPayment } from '../partner-payment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPartnerPayment for edit and NewPartnerPaymentFormGroupInput for create.
 */
type PartnerPaymentFormGroupInput = IPartnerPayment | PartialWithRequiredKeyOf<NewPartnerPayment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPartnerPayment | NewPartnerPayment> = Omit<T, 'on'> & {
  on?: string | null;
};

type PartnerPaymentFormRawValue = FormValueOf<IPartnerPayment>;

type NewPartnerPaymentFormRawValue = FormValueOf<NewPartnerPayment>;

type PartnerPaymentFormDefaults = Pick<NewPartnerPayment, 'id' | 'on'>;

type PartnerPaymentFormGroupContent = {
  id: FormControl<PartnerPaymentFormRawValue['id'] | NewPartnerPayment['id']>;
  invoice: FormControl<PartnerPaymentFormRawValue['invoice']>;
  totalAmount: FormControl<PartnerPaymentFormRawValue['totalAmount']>;
  tax: FormControl<PartnerPaymentFormRawValue['tax']>;
  on: FormControl<PartnerPaymentFormRawValue['on']>;
  status: FormControl<PartnerPaymentFormRawValue['status']>;
  reason: FormControl<PartnerPaymentFormRawValue['reason']>;
  contract: FormControl<PartnerPaymentFormRawValue['contract']>;
  partner: FormControl<PartnerPaymentFormRawValue['partner']>;
  company: FormControl<PartnerPaymentFormRawValue['company']>;
};

export type PartnerPaymentFormGroup = FormGroup<PartnerPaymentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PartnerPaymentFormService {
  createPartnerPaymentFormGroup(partnerPayment: PartnerPaymentFormGroupInput = { id: null }): PartnerPaymentFormGroup {
    const partnerPaymentRawValue = this.convertPartnerPaymentToPartnerPaymentRawValue({
      ...this.getFormDefaults(),
      ...partnerPayment,
    });
    return new FormGroup<PartnerPaymentFormGroupContent>({
      id: new FormControl(
        { value: partnerPaymentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      invoice: new FormControl(partnerPaymentRawValue.invoice),
      totalAmount: new FormControl(partnerPaymentRawValue.totalAmount),
      tax: new FormControl(partnerPaymentRawValue.tax),
      on: new FormControl(partnerPaymentRawValue.on),
      status: new FormControl(partnerPaymentRawValue.status),
      reason: new FormControl(partnerPaymentRawValue.reason),
      contract: new FormControl(partnerPaymentRawValue.contract),
      partner: new FormControl(partnerPaymentRawValue.partner),
      company: new FormControl(partnerPaymentRawValue.company),
    });
  }

  getPartnerPayment(form: PartnerPaymentFormGroup): IPartnerPayment | NewPartnerPayment {
    return this.convertPartnerPaymentRawValueToPartnerPayment(
      form.getRawValue() as PartnerPaymentFormRawValue | NewPartnerPaymentFormRawValue
    );
  }

  resetForm(form: PartnerPaymentFormGroup, partnerPayment: PartnerPaymentFormGroupInput): void {
    const partnerPaymentRawValue = this.convertPartnerPaymentToPartnerPaymentRawValue({ ...this.getFormDefaults(), ...partnerPayment });
    form.reset(
      {
        ...partnerPaymentRawValue,
        id: { value: partnerPaymentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PartnerPaymentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      on: currentTime,
    };
  }

  private convertPartnerPaymentRawValueToPartnerPayment(
    rawPartnerPayment: PartnerPaymentFormRawValue | NewPartnerPaymentFormRawValue
  ): IPartnerPayment | NewPartnerPayment {
    return {
      ...rawPartnerPayment,
      on: dayjs(rawPartnerPayment.on, DATE_TIME_FORMAT),
    };
  }

  private convertPartnerPaymentToPartnerPaymentRawValue(
    partnerPayment: IPartnerPayment | (Partial<NewPartnerPayment> & PartnerPaymentFormDefaults)
  ): PartnerPaymentFormRawValue | PartialWithRequiredKeyOf<NewPartnerPaymentFormRawValue> {
    return {
      ...partnerPayment,
      on: partnerPayment.on ? partnerPayment.on.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
