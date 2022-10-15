import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PaymentEventService } from '../service/payment-event.service';

import { PaymentEventComponent } from './payment-event.component';

describe('PaymentEvent Management Component', () => {
  let comp: PaymentEventComponent;
  let fixture: ComponentFixture<PaymentEventComponent>;
  let service: PaymentEventService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'payment-event', component: PaymentEventComponent }]), HttpClientTestingModule],
      declarations: [PaymentEventComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(PaymentEventComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaymentEventComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PaymentEventService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.paymentEvents?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to paymentEventService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getPaymentEventIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getPaymentEventIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
