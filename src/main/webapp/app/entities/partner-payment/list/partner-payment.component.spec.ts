import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PartnerPaymentService } from '../service/partner-payment.service';

import { PartnerPaymentComponent } from './partner-payment.component';

describe('PartnerPayment Management Component', () => {
  let comp: PartnerPaymentComponent;
  let fixture: ComponentFixture<PartnerPaymentComponent>;
  let service: PartnerPaymentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'partner-payment', component: PartnerPaymentComponent }]), HttpClientTestingModule],
      declarations: [PartnerPaymentComponent],
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
      .overrideTemplate(PartnerPaymentComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PartnerPaymentComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PartnerPaymentService);

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
    expect(comp.partnerPayments?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to partnerPaymentService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getPartnerPaymentIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getPartnerPaymentIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
