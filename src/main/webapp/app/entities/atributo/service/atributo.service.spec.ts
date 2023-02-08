import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAtributo } from '../atributo.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../atributo.test-samples';

import { AtributoService } from './atributo.service';

const requireRestSample: IAtributo = {
  ...sampleWithRequiredData,
};

describe('Atributo Service', () => {
  let service: AtributoService;
  let httpMock: HttpTestingController;
  let expectedResult: IAtributo | IAtributo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AtributoService);
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

    it('should create a Atributo', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const atributo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(atributo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Atributo', () => {
      const atributo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(atributo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Atributo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Atributo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Atributo', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAtributoToCollectionIfMissing', () => {
      it('should add a Atributo to an empty array', () => {
        const atributo: IAtributo = sampleWithRequiredData;
        expectedResult = service.addAtributoToCollectionIfMissing([], atributo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(atributo);
      });

      it('should not add a Atributo to an array that contains it', () => {
        const atributo: IAtributo = sampleWithRequiredData;
        const atributoCollection: IAtributo[] = [
          {
            ...atributo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAtributoToCollectionIfMissing(atributoCollection, atributo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Atributo to an array that doesn't contain it", () => {
        const atributo: IAtributo = sampleWithRequiredData;
        const atributoCollection: IAtributo[] = [sampleWithPartialData];
        expectedResult = service.addAtributoToCollectionIfMissing(atributoCollection, atributo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(atributo);
      });

      it('should add only unique Atributo to an array', () => {
        const atributoArray: IAtributo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const atributoCollection: IAtributo[] = [sampleWithRequiredData];
        expectedResult = service.addAtributoToCollectionIfMissing(atributoCollection, ...atributoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const atributo: IAtributo = sampleWithRequiredData;
        const atributo2: IAtributo = sampleWithPartialData;
        expectedResult = service.addAtributoToCollectionIfMissing([], atributo, atributo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(atributo);
        expect(expectedResult).toContain(atributo2);
      });

      it('should accept null and undefined values', () => {
        const atributo: IAtributo = sampleWithRequiredData;
        expectedResult = service.addAtributoToCollectionIfMissing([], null, atributo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(atributo);
      });

      it('should return initial array if no Atributo is added', () => {
        const atributoCollection: IAtributo[] = [sampleWithRequiredData];
        expectedResult = service.addAtributoToCollectionIfMissing(atributoCollection, undefined, null);
        expect(expectedResult).toEqual(atributoCollection);
      });
    });

    describe('compareAtributo', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAtributo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAtributo(entity1, entity2);
        const compareResult2 = service.compareAtributo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAtributo(entity1, entity2);
        const compareResult2 = service.compareAtributo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAtributo(entity1, entity2);
        const compareResult2 = service.compareAtributo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
