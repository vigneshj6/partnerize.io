import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PartnerReveneFormService } from './partner-revene-form.service';
import { PartnerReveneService } from '../service/partner-revene.service';
import { IPartnerRevene } from '../partner-revene.model';

import { PartnerReveneUpdateComponent } from './partner-revene-update.component';

describe('PartnerRevene Management Update Component', () => {
  let comp: PartnerReveneUpdateComponent;
  let fixture: ComponentFixture<PartnerReveneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let partnerReveneFormService: PartnerReveneFormService;
  let partnerReveneService: PartnerReveneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PartnerReveneUpdateComponent],
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
      .overrideTemplate(PartnerReveneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PartnerReveneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partnerReveneFormService = TestBed.inject(PartnerReveneFormService);
    partnerReveneService = TestBed.inject(PartnerReveneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const partnerRevene: IPartnerRevene = { id: 456 };

      activatedRoute.data = of({ partnerRevene });
      comp.ngOnInit();

      expect(comp.partnerRevene).toEqual(partnerRevene);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPartnerRevene>>();
      const partnerRevene = { id: 123 };
      jest.spyOn(partnerReveneFormService, 'getPartnerRevene').mockReturnValue(partnerRevene);
      jest.spyOn(partnerReveneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partnerRevene });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: partnerRevene }));
      saveSubject.complete();

      // THEN
      expect(partnerReveneFormService.getPartnerRevene).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partnerReveneService.update).toHaveBeenCalledWith(expect.objectContaining(partnerRevene));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPartnerRevene>>();
      const partnerRevene = { id: 123 };
      jest.spyOn(partnerReveneFormService, 'getPartnerRevene').mockReturnValue({ id: null });
      jest.spyOn(partnerReveneService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partnerRevene: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: partnerRevene }));
      saveSubject.complete();

      // THEN
      expect(partnerReveneFormService.getPartnerRevene).toHaveBeenCalled();
      expect(partnerReveneService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPartnerRevene>>();
      const partnerRevene = { id: 123 };
      jest.spyOn(partnerReveneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partnerRevene });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partnerReveneService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
