import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPartnerRevene } from '../partner-revene.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../partner-revene.test-samples';

import { PartnerReveneService } from './partner-revene.service';

const requireRestSample: IPartnerRevene = {
  ...sampleWithRequiredData,
};

describe('PartnerRevene Service', () => {
  let service: PartnerReveneService;
  let httpMock: HttpTestingController;
  let expectedResult: IPartnerRevene | IPartnerRevene[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PartnerReveneService);
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

    it('should create a PartnerRevene', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const partnerRevene = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(partnerRevene).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PartnerRevene', () => {
      const partnerRevene = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(partnerRevene).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PartnerRevene', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PartnerRevene', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PartnerRevene', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPartnerReveneToCollectionIfMissing', () => {
      it('should add a PartnerRevene to an empty array', () => {
        const partnerRevene: IPartnerRevene = sampleWithRequiredData;
        expectedResult = service.addPartnerReveneToCollectionIfMissing([], partnerRevene);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(partnerRevene);
      });

      it('should not add a PartnerRevene to an array that contains it', () => {
        const partnerRevene: IPartnerRevene = sampleWithRequiredData;
        const partnerReveneCollection: IPartnerRevene[] = [
          {
            ...partnerRevene,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPartnerReveneToCollectionIfMissing(partnerReveneCollection, partnerRevene);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PartnerRevene to an array that doesn't contain it", () => {
        const partnerRevene: IPartnerRevene = sampleWithRequiredData;
        const partnerReveneCollection: IPartnerRevene[] = [sampleWithPartialData];
        expectedResult = service.addPartnerReveneToCollectionIfMissing(partnerReveneCollection, partnerRevene);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partnerRevene);
      });

      it('should add only unique PartnerRevene to an array', () => {
        const partnerReveneArray: IPartnerRevene[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const partnerReveneCollection: IPartnerRevene[] = [sampleWithRequiredData];
        expectedResult = service.addPartnerReveneToCollectionIfMissing(partnerReveneCollection, ...partnerReveneArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partnerRevene: IPartnerRevene = sampleWithRequiredData;
        const partnerRevene2: IPartnerRevene = sampleWithPartialData;
        expectedResult = service.addPartnerReveneToCollectionIfMissing([], partnerRevene, partnerRevene2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partnerRevene);
        expect(expectedResult).toContain(partnerRevene2);
      });

      it('should accept null and undefined values', () => {
        const partnerRevene: IPartnerRevene = sampleWithRequiredData;
        expectedResult = service.addPartnerReveneToCollectionIfMissing([], null, partnerRevene, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(partnerRevene);
      });

      it('should return initial array if no PartnerRevene is added', () => {
        const partnerReveneCollection: IPartnerRevene[] = [sampleWithRequiredData];
        expectedResult = service.addPartnerReveneToCollectionIfMissing(partnerReveneCollection, undefined, null);
        expect(expectedResult).toEqual(partnerReveneCollection);
      });
    });

    describe('comparePartnerRevene', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePartnerRevene(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePartnerRevene(entity1, entity2);
        const compareResult2 = service.comparePartnerRevene(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePartnerRevene(entity1, entity2);
        const compareResult2 = service.comparePartnerRevene(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePartnerRevene(entity1, entity2);
        const compareResult2 = service.comparePartnerRevene(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
