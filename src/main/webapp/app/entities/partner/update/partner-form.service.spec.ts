import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../partner.test-samples';

import { PartnerFormService } from './partner-form.service';

describe('Partner Form Service', () => {
  let service: PartnerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PartnerFormService);
  });

  describe('Service methods', () => {
    describe('createPartnerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPartnerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            region: expect.any(Object),
            country: expect.any(Object),
            status: expect.any(Object),
            card: expect.any(Object),
            company: expect.any(Object),
          })
        );
      });

      it('passing IPartner should create a new form with FormGroup', () => {
        const formGroup = service.createPartnerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            region: expect.any(Object),
            country: expect.any(Object),
            status: expect.any(Object),
            card: expect.any(Object),
            company: expect.any(Object),
          })
        );
      });
    });

    describe('getPartner', () => {
      it('should return NewPartner for default Partner initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPartnerFormGroup(sampleWithNewData);

        const partner = service.getPartner(formGroup) as any;

        expect(partner).toMatchObject(sampleWithNewData);
      });

      it('should return NewPartner for empty Partner initial value', () => {
        const formGroup = service.createPartnerFormGroup();

        const partner = service.getPartner(formGroup) as any;

        expect(partner).toMatchObject({});
      });

      it('should return IPartner', () => {
        const formGroup = service.createPartnerFormGroup(sampleWithRequiredData);

        const partner = service.getPartner(formGroup) as any;

        expect(partner).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPartner should not enable id FormControl', () => {
        const formGroup = service.createPartnerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPartner should disable id FormControl', () => {
        const formGroup = service.createPartnerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
