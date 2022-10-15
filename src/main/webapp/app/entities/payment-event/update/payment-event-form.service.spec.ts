import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../payment-event.test-samples';

import { PaymentEventFormService } from './payment-event-form.service';

describe('PaymentEvent Form Service', () => {
  let service: PaymentEventFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaymentEventFormService);
  });

  describe('Service methods', () => {
    describe('createPaymentEventFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaymentEventFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            status: expect.any(Object),
            reason: expect.any(Object),
            on: expect.any(Object),
            totalAmount: expect.any(Object),
            invoice: expect.any(Object),
            partnerPayment: expect.any(Object),
          })
        );
      });

      it('passing IPaymentEvent should create a new form with FormGroup', () => {
        const formGroup = service.createPaymentEventFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            status: expect.any(Object),
            reason: expect.any(Object),
            on: expect.any(Object),
            totalAmount: expect.any(Object),
            invoice: expect.any(Object),
            partnerPayment: expect.any(Object),
          })
        );
      });
    });

    describe('getPaymentEvent', () => {
      it('should return NewPaymentEvent for default PaymentEvent initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPaymentEventFormGroup(sampleWithNewData);

        const paymentEvent = service.getPaymentEvent(formGroup) as any;

        expect(paymentEvent).toMatchObject(sampleWithNewData);
      });

      it('should return NewPaymentEvent for empty PaymentEvent initial value', () => {
        const formGroup = service.createPaymentEventFormGroup();

        const paymentEvent = service.getPaymentEvent(formGroup) as any;

        expect(paymentEvent).toMatchObject({});
      });

      it('should return IPaymentEvent', () => {
        const formGroup = service.createPaymentEventFormGroup(sampleWithRequiredData);

        const paymentEvent = service.getPaymentEvent(formGroup) as any;

        expect(paymentEvent).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPaymentEvent should not enable id FormControl', () => {
        const formGroup = service.createPaymentEventFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPaymentEvent should disable id FormControl', () => {
        const formGroup = service.createPaymentEventFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
