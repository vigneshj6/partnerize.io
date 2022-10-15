import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPartnerRevene, NewPartnerRevene } from '../partner-revene.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPartnerRevene for edit and NewPartnerReveneFormGroupInput for create.
 */
type PartnerReveneFormGroupInput = IPartnerRevene | PartialWithRequiredKeyOf<NewPartnerRevene>;

type PartnerReveneFormDefaults = Pick<NewPartnerRevene, 'id'>;

type PartnerReveneFormGroupContent = {
  id: FormControl<IPartnerRevene['id'] | NewPartnerRevene['id']>;
};

export type PartnerReveneFormGroup = FormGroup<PartnerReveneFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PartnerReveneFormService {
  createPartnerReveneFormGroup(partnerRevene: PartnerReveneFormGroupInput = { id: null }): PartnerReveneFormGroup {
    const partnerReveneRawValue = {
      ...this.getFormDefaults(),
      ...partnerRevene,
    };
    return new FormGroup<PartnerReveneFormGroupContent>({
      id: new FormControl(
        { value: partnerReveneRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getPartnerRevene(form: PartnerReveneFormGroup): IPartnerRevene | NewPartnerRevene {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as IPartnerRevene | NewPartnerRevene;
  }

  resetForm(form: PartnerReveneFormGroup, partnerRevene: PartnerReveneFormGroupInput): void {
    const partnerReveneRawValue = { ...this.getFormDefaults(), ...partnerRevene };
    form.reset(
      {
        ...partnerReveneRawValue,
        id: { value: partnerReveneRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PartnerReveneFormDefaults {
    return {
      id: null,
    };
  }
}
