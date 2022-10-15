import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPartnerPayment } from '../partner-payment.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../partner-payment.test-samples';

import { PartnerPaymentService, RestPartnerPayment } from './partner-payment.service';

const requireRestSample: RestPartnerPayment = {
  ...sampleWithRequiredData,
  on: sampleWithRequiredData.on?.toJSON(),
};

describe('PartnerPayment Service', () => {
  let service: PartnerPaymentService;
  let httpMock: HttpTestingController;
  let expectedResult: IPartnerPayment | IPartnerPayment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PartnerPaymentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PartnerPayment', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const partnerPayment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(partnerPayment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PartnerPayment', () => {
      const partnerPayment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(partnerPayment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PartnerPayment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PartnerPayment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PartnerPayment', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPartnerPaymentToCollectionIfMissing', () => {
      it('should add a PartnerPayment to an empty array', () => {
        const partnerPayment: IPartnerPayment = sampleWithRequiredData;
        expectedResult = service.addPartnerPaymentToCollectionIfMissing([], partnerPayment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(partnerPayment);
      });

      it('should not add a PartnerPayment to an array that contains it', () => {
        const partnerPayment: IPartnerPayment = sampleWithRequiredData;
        const partnerPaymentCollection: IPartnerPayment[] = [
          {
            ...partnerPayment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPartnerPaymentToCollectionIfMissing(partnerPaymentCollection, partnerPayment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PartnerPayment to an array that doesn't contain it", () => {
        const partnerPayment: IPartnerPayment = sampleWithRequiredData;
        const partnerPaymentCollection: IPartnerPayment[] = [sampleWithPartialData];
        expectedResult = service.addPartnerPaymentToCollectionIfMissing(partnerPaymentCollection, partnerPayment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partnerPayment);
      });

      it('should add only unique PartnerPayment to an array', () => {
        const partnerPaymentArray: IPartnerPayment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const partnerPaymentCollection: IPartnerPayment[] = [sampleWithRequiredData];
        expectedResult = service.addPartnerPaymentToCollectionIfMissing(partnerPaymentCollection, ...partnerPaymentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partnerPayment: IPartnerPayment = sampleWithRequiredData;
        const partnerPayment2: IPartnerPayment = sampleWithPartialData;
        expectedResult = service.addPartnerPaymentToCollectionIfMissing([], partnerPayment, partnerPayment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partnerPayment);
        expect(expectedResult).toContain(partnerPayment2);
      });

      it('should accept null and undefined values', () => {
        const partnerPayment: IPartnerPayment = sampleWithRequiredData;
        expectedResult = service.addPartnerPaymentToCollectionIfMissing([], null, partnerPayment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(partnerPayment);
      });

      it('should return initial array if no PartnerPayment is added', () => {
        const partnerPaymentCollection: IPartnerPayment[] = [sampleWithRequiredData];
        expectedResult = service.addPartnerPaymentToCollectionIfMissing(partnerPaymentCollection, undefined, null);
        expect(expectedResult).toEqual(partnerPaymentCollection);
      });
    });

    describe('comparePartnerPayment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePartnerPayment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePartnerPayment(entity1, entity2);
        const compareResult2 = service.comparePartnerPayment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePartnerPayment(entity1, entity2);
        const compareResult2 = service.comparePartnerPayment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePartnerPayment(entity1, entity2);
        const compareResult2 = service.comparePartnerPayment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
