import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PartnerReveneDetailComponent } from './partner-revene-detail.component';

describe('PartnerRevene Management Detail Component', () => {
  let comp: PartnerReveneDetailComponent;
  let fixture: ComponentFixture<PartnerReveneDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PartnerReveneDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ partnerRevene: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PartnerReveneDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PartnerReveneDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load partnerRevene on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.partnerRevene).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
