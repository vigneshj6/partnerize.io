import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PartnerPaymentDetailComponent } from './partner-payment-detail.component';

describe('PartnerPayment Management Detail Component', () => {
  let comp: PartnerPaymentDetailComponent;
  let fixture: ComponentFixture<PartnerPaymentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PartnerPaymentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ partnerPayment: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PartnerPaymentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PartnerPaymentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load partnerPayment on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.partnerPayment).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
