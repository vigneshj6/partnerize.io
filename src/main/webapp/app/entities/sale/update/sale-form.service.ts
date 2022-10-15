import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISale, NewSale } from '../sale.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISale for edit and NewSaleFormGroupInput for create.
 */
type SaleFormGroupInput = ISale | PartialWithRequiredKeyOf<NewSale>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISale | NewSale> = Omit<T, 'on'> & {
  on?: string | null;
};

type SaleFormRawValue = FormValueOf<ISale>;

type NewSaleFormRawValue = FormValueOf<NewSale>;

type SaleFormDefaults = Pick<NewSale, 'id' | 'on'>;

type SaleFormGroupContent = {
  id: FormControl<SaleFormRawValue['id'] | NewSale['id']>;
  saleId: FormControl<SaleFormRawValue['saleId']>;
  invoice: FormControl<SaleFormRawValue['invoice']>;
  totalAmount: FormControl<SaleFormRawValue['totalAmount']>;
  tax: FormControl<SaleFormRawValue['tax']>;
  couponCode: FormControl<SaleFormRawValue['couponCode']>;
  couponAmount: FormControl<SaleFormRawValue['couponAmount']>;
  on: FormControl<SaleFormRawValue['on']>;
  currency: FormControl<SaleFormRawValue['currency']>;
  customer: FormControl<SaleFormRawValue['customer']>;
  partner: FormControl<SaleFormRawValue['partner']>;
  company: FormControl<SaleFormRawValue['company']>;
};

export type SaleFormGroup = FormGroup<SaleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SaleFormService {
  createSaleFormGroup(sale: SaleFormGroupInput = { id: null }): SaleFormGroup {
    const saleRawValue = this.convertSaleToSaleRawValue({
      ...this.getFormDefaults(),
      ...sale,
    });
    return new FormGroup<SaleFormGroupContent>({
      id: new FormControl(
        { value: saleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      saleId: new FormControl(saleRawValue.saleId),
      invoice: new FormControl(saleRawValue.invoice),
      totalAmount: new FormControl(saleRawValue.totalAmount),
      tax: new FormControl(saleRawValue.tax),
      couponCode: new FormControl(saleRawValue.couponCode),
      couponAmount: new FormControl(saleRawValue.couponAmount),
      on: new FormControl(saleRawValue.on),
      currency: new FormControl(saleRawValue.currency),
      customer: new FormControl(saleRawValue.customer),
      partner: new FormControl(saleRawValue.partner),
      company: new FormControl(saleRawValue.company),
    });
  }

  getSale(form: SaleFormGroup): ISale | NewSale {
    return this.convertSaleRawValueToSale(form.getRawValue() as SaleFormRawValue | NewSaleFormRawValue);
  }

  resetForm(form: SaleFormGroup, sale: SaleFormGroupInput): void {
    const saleRawValue = this.convertSaleToSaleRawValue({ ...this.getFormDefaults(), ...sale });
    form.reset(
      {
        ...saleRawValue,
        id: { value: saleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SaleFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      on: currentTime,
    };
  }

  private convertSaleRawValueToSale(rawSale: SaleFormRawValue | NewSaleFormRawValue): ISale | NewSale {
    return {
      ...rawSale,
      on: dayjs(rawSale.on, DATE_TIME_FORMAT),
    };
  }

  private convertSaleToSaleRawValue(
    sale: ISale | (Partial<NewSale> & SaleFormDefaults)
  ): SaleFormRawValue | PartialWithRequiredKeyOf<NewSaleFormRawValue> {
    return {
      ...sale,
      on: sale.on ? sale.on.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
