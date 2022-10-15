import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPartner, NewPartner } from '../partner.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPartner for edit and NewPartnerFormGroupInput for create.
 */
type PartnerFormGroupInput = IPartner | PartialWithRequiredKeyOf<NewPartner>;

type PartnerFormDefaults = Pick<NewPartner, 'id'>;

type PartnerFormGroupContent = {
  id: FormControl<IPartner['id'] | NewPartner['id']>;
  name: FormControl<IPartner['name']>;
  type: FormControl<IPartner['type']>;
  region: FormControl<IPartner['region']>;
  country: FormControl<IPartner['country']>;
  status: FormControl<IPartner['status']>;
  card: FormControl<IPartner['card']>;
  company: FormControl<IPartner['company']>;
};

export type PartnerFormGroup = FormGroup<PartnerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PartnerFormService {
  createPartnerFormGroup(partner: PartnerFormGroupInput = { id: null }): PartnerFormGroup {
    const partnerRawValue = {
      ...this.getFormDefaults(),
      ...partner,
    };
    return new FormGroup<PartnerFormGroupContent>({
      id: new FormControl(
        { value: partnerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(partnerRawValue.name),
      type: new FormControl(partnerRawValue.type),
      region: new FormControl(partnerRawValue.region),
      country: new FormControl(partnerRawValue.country),
      status: new FormControl(partnerRawValue.status),
      card: new FormControl(partnerRawValue.card),
      company: new FormControl(partnerRawValue.company),
    });
  }

  getPartner(form: PartnerFormGroup): IPartner | NewPartner {
    return form.getRawValue() as IPartner | NewPartner;
  }

  resetForm(form: PartnerFormGroup, partner: PartnerFormGroupInput): void {
    const partnerRawValue = { ...this.getFormDefaults(), ...partner };
    form.reset(
      {
        ...partnerRawValue,
        id: { value: partnerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PartnerFormDefaults {
    return {
      id: null,
    };
  }
}
