import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PartnerPaymentFormService } from './partner-payment-form.service';
import { PartnerPaymentService } from '../service/partner-payment.service';
import { IPartnerPayment } from '../partner-payment.model';
import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { IPartner } from 'app/entities/partner/partner.model';
import { PartnerService } from 'app/entities/partner/service/partner.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';

import { PartnerPaymentUpdateComponent } from './partner-payment-update.component';

describe('PartnerPayment Management Update Component', () => {
  let comp: PartnerPaymentUpdateComponent;
  let fixture: ComponentFixture<PartnerPaymentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let partnerPaymentFormService: PartnerPaymentFormService;
  let partnerPaymentService: PartnerPaymentService;
  let contractService: ContractService;
  let partnerService: PartnerService;
  let companyService: CompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PartnerPaymentUpdateComponent],
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
      .overrideTemplate(PartnerPaymentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PartnerPaymentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partnerPaymentFormService = TestBed.inject(PartnerPaymentFormService);
    partnerPaymentService = TestBed.inject(PartnerPaymentService);
    contractService = TestBed.inject(ContractService);
    partnerService = TestBed.inject(PartnerService);
    companyService = TestBed.inject(CompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Contract query and add missing value', () => {
      const partnerPayment: IPartnerPayment = { id: 456 };
      const contract: IContract = { id: 13207 };
      partnerPayment.contract = contract;

      const contractCollection: IContract[] = [{ id: 97823 }];
      jest.spyOn(contractService, 'query').mockReturnValue(of(new HttpResponse({ body: contractCollection })));
      const additionalContracts = [contract];
      const expectedCollection: IContract[] = [...additionalContracts, ...contractCollection];
      jest.spyOn(contractService, 'addContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partnerPayment });
      comp.ngOnInit();

      expect(contractService.query).toHaveBeenCalled();
      expect(contractService.addContractToCollectionIfMissing).toHaveBeenCalledWith(
        contractCollection,
        ...additionalContracts.map(expect.objectContaining)
      );
      expect(comp.contractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Partner query and add missing value', () => {
      const partnerPayment: IPartnerPayment = { id: 456 };
      const partner: IPartner = { id: 3023 };
      partnerPayment.partner = partner;

      const partnerCollection: IPartner[] = [{ id: 54029 }];
      jest.spyOn(partnerService, 'query').mockReturnValue(of(new HttpResponse({ body: partnerCollection })));
      const additionalPartners = [partner];
      const expectedCollection: IPartner[] = [...additionalPartners, ...partnerCollection];
      jest.spyOn(partnerService, 'addPartnerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partnerPayment });
      comp.ngOnInit();

      expect(partnerService.query).toHaveBeenCalled();
      expect(partnerService.addPartnerToCollectionIfMissing).toHaveBeenCalledWith(
        partnerCollection,
        ...additionalPartners.map(expect.objectContaining)
      );
      expect(comp.partnersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const partnerPayment: IPartnerPayment = { id: 456 };
      const company: ICompany = { id: 26542 };
      partnerPayment.company = company;

      const companyCollection: ICompany[] = [{ id: 85490 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partnerPayment });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining)
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const partnerPayment: IPartnerPayment = { id: 456 };
      const contract: IContract = { id: 75609 };
      partnerPayment.contract = contract;
      const partner: IPartner = { id: 77837 };
      partnerPayment.partner = partner;
      const company: ICompany = { id: 49544 };
      partnerPayment.company = company;

      activatedRoute.data = of({ partnerPayment });
      comp.ngOnInit();

      expect(comp.contractsSharedCollection).toContain(contract);
      expect(comp.partnersSharedCollection).toContain(partner);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.partnerPayment).toEqual(partnerPayment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPartnerPayment>>();
      const partnerPayment = { id: 123 };
      jest.spyOn(partnerPaymentFormService, 'getPartnerPayment').mockReturnValue(partnerPayment);
      jest.spyOn(partnerPaymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partnerPayment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: partnerPayment }));
      saveSubject.complete();

      // THEN
      expect(partnerPaymentFormService.getPartnerPayment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partnerPaymentService.update).toHaveBeenCalledWith(expect.objectContaining(partnerPayment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPartnerPayment>>();
      const partnerPayment = { id: 123 };
      jest.spyOn(partnerPaymentFormService, 'getPartnerPayment').mockReturnValue({ id: null });
      jest.spyOn(partnerPaymentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partnerPayment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: partnerPayment }));
      saveSubject.complete();

      // THEN
      expect(partnerPaymentFormService.getPartnerPayment).toHaveBeenCalled();
      expect(partnerPaymentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPartnerPayment>>();
      const partnerPayment = { id: 123 };
      jest.spyOn(partnerPaymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partnerPayment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partnerPaymentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContract', () => {
      it('Should forward to contractService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(contractService, 'compareContract');
        comp.compareContract(entity, entity2);
        expect(contractService.compareContract).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePartner', () => {
      it('Should forward to partnerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(partnerService, 'comparePartner');
        comp.comparePartner(entity, entity2);
        expect(partnerService.comparePartner).toHaveBeenCalledWith(entity, entity2);
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
