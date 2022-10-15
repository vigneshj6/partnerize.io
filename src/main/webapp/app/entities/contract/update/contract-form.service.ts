import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IContract, NewContract } from '../contract.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContract for edit and NewContractFormGroupInput for create.
 */
type ContractFormGroupInput = IContract | PartialWithRequiredKeyOf<NewContract>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IContract | NewContract> = Omit<T, 'startAt' | 'endAt'> & {
  startAt?: string | null;
  endAt?: string | null;
};

type ContractFormRawValue = FormValueOf<IContract>;

type NewContractFormRawValue = FormValueOf<NewContract>;

type ContractFormDefaults = Pick<NewContract, 'id' | 'startAt' | 'endAt'>;

type ContractFormGroupContent = {
  id: FormControl<ContractFormRawValue['id'] | NewContract['id']>;
  name: FormControl<ContractFormRawValue['name']>;
  type: FormControl<ContractFormRawValue['type']>;
  startAt: FormControl<ContractFormRawValue['startAt']>;
  endAt: FormControl<ContractFormRawValue['endAt']>;
  billingCycle: FormControl<ContractFormRawValue['billingCycle']>;
  fixedRate: FormControl<ContractFormRawValue['fixedRate']>;
  commisionPercent: FormControl<ContractFormRawValue['commisionPercent']>;
  commisionRate: FormControl<ContractFormRawValue['commisionRate']>;
  currency: FormControl<ContractFormRawValue['currency']>;
  partner: FormControl<ContractFormRawValue['partner']>;
  company: FormControl<ContractFormRawValue['company']>;
};

export type ContractFormGroup = FormGroup<ContractFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContractFormService {
  createContractFormGroup(contract: ContractFormGroupInput = { id: null }): ContractFormGroup {
    const contractRawValue = this.convertContractToContractRawValue({
      ...this.getFormDefaults(),
      ...contract,
    });
    return new FormGroup<ContractFormGroupContent>({
      id: new FormControl(
        { value: contractRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(contractRawValue.name),
      type: new FormControl(contractRawValue.type),
      startAt: new FormControl(contractRawValue.startAt),
      endAt: new FormControl(contractRawValue.endAt),
      billingCycle: new FormControl(contractRawValue.billingCycle),
      fixedRate: new FormControl(contractRawValue.fixedRate),
      commisionPercent: new FormControl(contractRawValue.commisionPercent),
      commisionRate: new FormControl(contractRawValue.commisionRate),
      currency: new FormControl(contractRawValue.currency),
      partner: new FormControl(contractRawValue.partner),
      company: new FormControl(contractRawValue.company),
    });
  }

  getContract(form: ContractFormGroup): IContract | NewContract {
    return this.convertContractRawValueToContract(form.getRawValue() as ContractFormRawValue | NewContractFormRawValue);
  }

  resetForm(form: ContractFormGroup, contract: ContractFormGroupInput): void {
    const contractRawValue = this.convertContractToContractRawValue({ ...this.getFormDefaults(), ...contract });
    form.reset(
      {
        ...contractRawValue,
        id: { value: contractRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ContractFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startAt: currentTime,
      endAt: currentTime,
    };
  }

  private convertContractRawValueToContract(rawContract: ContractFormRawValue | NewContractFormRawValue): IContract | NewContract {
    return {
      ...rawContract,
      startAt: dayjs(rawContract.startAt, DATE_TIME_FORMAT),
      endAt: dayjs(rawContract.endAt, DATE_TIME_FORMAT),
    };
  }

  private convertContractToContractRawValue(
    contract: IContract | (Partial<NewContract> & ContractFormDefaults)
  ): ContractFormRawValue | PartialWithRequiredKeyOf<NewContractFormRawValue> {
    return {
      ...contract,
      startAt: contract.startAt ? contract.startAt.format(DATE_TIME_FORMAT) : undefined,
      endAt: contract.endAt ? contract.endAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
