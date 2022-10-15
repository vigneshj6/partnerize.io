import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PartnerFormService } from './partner-form.service';
import { PartnerService } from '../service/partner.service';
import { IPartner } from '../partner.model';
import { ICard } from 'app/entities/card/card.model';
import { CardService } from 'app/entities/card/service/card.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';

import { PartnerUpdateComponent } from './partner-update.component';

describe('Partner Management Update Component', () => {
  let comp: PartnerUpdateComponent;
  let fixture: ComponentFixture<PartnerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let partnerFormService: PartnerFormService;
  let partnerService: PartnerService;
  let cardService: CardService;
  let companyService: CompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PartnerUpdateComponent],
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
      .overrideTemplate(PartnerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PartnerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partnerFormService = TestBed.inject(PartnerFormService);
    partnerService = TestBed.inject(PartnerService);
    cardService = TestBed.inject(CardService);
    companyService = TestBed.inject(CompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call card query and add missing value', () => {
      const partner: IPartner = { id: 456 };
      const card: ICard = { id: 56085 };
      partner.card = card;

      const cardCollection: ICard[] = [{ id: 57974 }];
      jest.spyOn(cardService, 'query').mockReturnValue(of(new HttpResponse({ body: cardCollection })));
      const expectedCollection: ICard[] = [card, ...cardCollection];
      jest.spyOn(cardService, 'addCardToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partner });
      comp.ngOnInit();

      expect(cardService.query).toHaveBeenCalled();
      expect(cardService.addCardToCollectionIfMissing).toHaveBeenCalledWith(cardCollection, card);
      expect(comp.cardsCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const partner: IPartner = { id: 456 };
      const company: ICompany = { id: 77279 };
      partner.company = company;

      const companyCollection: ICompany[] = [{ id: 44719 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partner });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining)
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const partner: IPartner = { id: 456 };
      const card: ICard = { id: 44381 };
      partner.card = card;
      const company: ICompany = { id: 14958 };
      partner.company = company;

      activatedRoute.data = of({ partner });
      comp.ngOnInit();

      expect(comp.cardsCollection).toContain(card);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.partner).toEqual(partner);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPartner>>();
      const partner = { id: 123 };
      jest.spyOn(partnerFormService, 'getPartner').mockReturnValue(partner);
      jest.spyOn(partnerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partner });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: partner }));
      saveSubject.complete();

      // THEN
      expect(partnerFormService.getPartner).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partnerService.update).toHaveBeenCalledWith(expect.objectContaining(partner));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPartner>>();
      const partner = { id: 123 };
      jest.spyOn(partnerFormService, 'getPartner').mockReturnValue({ id: null });
      jest.spyOn(partnerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partner: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: partner }));
      saveSubject.complete();

      // THEN
      expect(partnerFormService.getPartner).toHaveBeenCalled();
      expect(partnerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPartner>>();
      const partner = { id: 123 };
      jest.spyOn(partnerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partner });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partnerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCard', () => {
      it('Should forward to cardService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cardService, 'compareCard');
        comp.compareCard(entity, entity2);
        expect(cardService.compareCard).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCompany', () => {
      it('Should forward to companyService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(companyService, 'compareCompany');
        comp.compareCompany(entity, entity2);
        expect(companyService.compareCompany).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
