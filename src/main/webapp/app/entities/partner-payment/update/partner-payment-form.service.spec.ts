import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../partner-payment.test-samples';

import { PartnerPaymentFormService } from './partner-payment-form.service';

describe('PartnerPayment Form Service', () => {
  let service: PartnerPaymentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PartnerPaymentFormService);
  });

  describe('Service methods', () => {
    describe('createPartnerPaymentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPartnerPaymentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            invoice: expect.any(Object),
            totalAmount: expect.any(Object),
            tax: expect.any(Object),
            on: expect.any(Object),
            status: expect.any(Object),
            reason: expect.any(Object),
            contract: expect.any(Object),
            partner: expect.any(Object),
            company: expect.any(Object),
          })
        );
      });

      it('passing IPartnerPayment should create a new form with FormGroup', () => {
        const formGroup = service.createPartnerPaymentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            invoice: expect.any(Object),
            totalAmount: expect.any(Object),
            tax: expect.any(Object),
            on: expect.any(Object),
            status: expect.any(Object),
            reason: expect.any(Object),
            contract: expect.any(Object),
            partner: expect.any(Object),
            company: expect.any(Object),
          })
        );
      });
    });

    describe('getPartnerPayment', () => {
      it('should return NewPartnerPayment for default PartnerPayment initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPartnerPaymentFormGroup(sampleWithNewData);

        const partnerPayment = service.getPartnerPayment(formGroup) as any;

        expect(partnerPayment).toMatchObject(sampleWithNewData);
      });

      it('should return NewPartnerPayment for empty PartnerPayment initial value', () => {
        const formGroup = service.createPartnerPaymentFormGroup();

        const partnerPayment = service.getPartnerPayment(formGroup) as any;

        expect(partnerPayment).toMatchObject({});
      });

      it('should return IPartnerPayment', () => {
        const formGroup = service.createPartnerPaymentFormGroup(sampleWithRequiredData);

        const partnerPayment = service.getPartnerPayment(formGroup) as any;

        expect(partnerPayment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPartnerPayment should not enable id FormControl', () => {
        const formGroup = service.createPartnerPaymentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPartnerPayment should disable id FormControl', () => {
        const formGroup = service.createPartnerPaymentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
