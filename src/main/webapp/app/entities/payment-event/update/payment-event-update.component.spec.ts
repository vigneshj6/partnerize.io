import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PaymentEventFormService } from './payment-event-form.service';
import { PaymentEventService } from '../service/payment-event.service';
import { IPaymentEvent } from '../payment-event.model';
import { IPartnerPayment } from 'app/entities/partner-payment/partner-payment.model';
import { PartnerPaymentService } from 'app/entities/partner-payment/service/partner-payment.service';

import { PaymentEventUpdateComponent } from './payment-event-update.component';

describe('PaymentEvent Management Update Component', () => {
  let comp: PaymentEventUpdateComponent;
  let fixture: ComponentFixture<PaymentEventUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paymentEventFormService: PaymentEventFormService;
  let paymentEventService: PaymentEventService;
  let partnerPaymentService: PartnerPaymentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PaymentEventUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PaymentEventUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaymentEventUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paymentEventFormService = TestBed.inject(PaymentEventFormService);
    paymentEventService = TestBed.inject(PaymentEventService);
    partnerPaymentService = TestBed.inject(PartnerPaymentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PartnerPayment query and add missing value', () => {
      const paymentEvent: IPaymentEvent = { id: 456 };
      const partnerPayment: IPartnerPayment = { id: 10349 };
      paymentEvent.partnerPayment = partnerPayment;

      const partnerPaymentCollection: IPartnerPayment[] = [{ id: 41260 }];
      jest.spyOn(partnerPaymentService, 'query').mockReturnValue(of(new HttpResponse({ body: partnerPaymentCollection })));
      const additionalPartnerPayments = [partnerPayment];
      const expectedCollection: IPartnerPayment[] = [...additionalPartnerPayments, ...partnerPaymentCollection];
      jest.spyOn(partnerPaymentService, 'addPartnerPaymentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paymentEvent });
      comp.ngOnInit();

      expect(partnerPaymentService.query).toHaveBeenCalled();
      expect(partnerPaymentService.addPartnerPaymentToCollectionIfMissing).toHaveBeenCalledWith(
        partnerPaymentCollection,
        ...additionalPartnerPayments.map(expect.objectContaining)
      );
      expect(comp.partnerPaymentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const paymentEvent: IPaymentEvent = { id: 456 };
      const partnerPayment: IPartnerPayment = { id: 46093 };
      paymentEvent.partnerPayment = partnerPayment;

      activatedRoute.data = of({ paymentEvent });
      comp.ngOnInit();

      expect(comp.partnerPaymentsSharedCollection).toContain(partnerPayment);
      expect(comp.paymentEvent).toEqual(paymentEvent);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaymentEvent>>();
      const paymentEvent = { id: 123 };
      jest.spyOn(paymentEventFormService, 'getPaymentEvent').mockReturnValue(paymentEvent);
      jest.spyOn(paymentEventService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentEvent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paymentEvent }));
      saveSubject.complete();

      // THEN
      expect(paymentEventFormService.getPaymentEvent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paymentEventService.update).toHaveBeenCalledWith(expect.objectContaining(paymentEvent));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaymentEvent>>();
      const paymentEvent = { id: 123 };
      jest.spyOn(paymentEventFormService, 'getPaymentEvent').mockReturnValue({ id: null });
      jest.spyOn(paymentEventService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentEvent: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paymentEvent }));
      saveSubject.complete();

      // THEN
      expect(paymentEventFormService.getPaymentEvent).toHaveBeenCalled();
      expect(paymentEventService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaymentEvent>>();
      const paymentEvent = { id: 123 };
      jest.spyOn(paymentEventService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentEvent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paymentEventService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePartnerPayment', () => {
      it('Should forward to partnerPaymentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(partnerPaymentService, 'comparePartnerPayment');
        comp.comparePartnerPayment(entity, entity2);
        expect(partnerPaymentService.comparePartnerPayment).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
