import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../partner-revene.test-samples';

import { PartnerReveneFormService } from './partner-revene-form.service';

describe('PartnerRevene Form Service', () => {
  let service: PartnerReveneFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PartnerReveneFormService);
  });

  describe('Service methods', () => {
    describe('createPartnerReveneFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPartnerReveneFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing IPartnerRevene should create a new form with FormGroup', () => {
        const formGroup = service.createPartnerReveneFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getPartnerRevene', () => {
      it('should return NewPartnerRevene for default PartnerRevene initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPartnerReveneFormGroup(sampleWithNewData);

        const partnerRevene = service.getPartnerRevene(formGroup) as any;

        expect(partnerRevene).toMatchObject(sampleWithNewData);
      });

      it('should return NewPartnerRevene for empty PartnerRevene initial value', () => {
        const formGroup = service.createPartnerReveneFormGroup();

        const partnerRevene = service.getPartnerRevene(formGroup) as any;

        expect(partnerRevene).toMatchObject({});
      });

      it('should return IPartnerRevene', () => {
        const formGroup = service.createPartnerReveneFormGroup(sampleWithRequiredData);

        const partnerRevene = service.getPartnerRevene(formGroup) as any;

        expect(partnerRevene).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPartnerRevene should not enable id FormControl', () => {
        const formGroup = service.createPartnerReveneFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPartnerRevene should disable id FormControl', () => {
        const formGroup = service.createPartnerReveneFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
