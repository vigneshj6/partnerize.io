import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaymentEventDetailComponent } from './payment-event-detail.component';

describe('PaymentEvent Management Detail Component', () => {
  let comp: PaymentEventDetailComponent;
  let fixture: ComponentFixture<PaymentEventDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaymentEventDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ paymentEvent: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PaymentEventDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PaymentEventDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load paymentEvent on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.paymentEvent).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
