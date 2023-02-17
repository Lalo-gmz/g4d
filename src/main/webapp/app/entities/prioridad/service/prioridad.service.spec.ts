import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPrioridad } from '../prioridad.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../prioridad.test-samples';

import { PrioridadService } from './prioridad.service';

const requireRestSample: IPrioridad = {
  ...sampleWithRequiredData,
};

describe('Prioridad Service', () => {
  let service: PrioridadService;
  let httpMock: HttpTestingController;
  let expectedResult: IPrioridad | IPrioridad[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PrioridadService);
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

    it('should create a Prioridad', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const prioridad = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(prioridad).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Prioridad', () => {
      const prioridad = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(prioridad).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Prioridad', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Prioridad', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Prioridad', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPrioridadToCollectionIfMissing', () => {
      it('should add a Prioridad to an empty array', () => {
        const prioridad: IPrioridad = sampleWithRequiredData;
        expectedResult = service.addPrioridadToCollectionIfMissing([], prioridad);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prioridad);
      });

      it('should not add a Prioridad to an array that contains it', () => {
        const prioridad: IPrioridad = sampleWithRequiredData;
        const prioridadCollection: IPrioridad[] = [
          {
            ...prioridad,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPrioridadToCollectionIfMissing(prioridadCollection, prioridad);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Prioridad to an array that doesn't contain it", () => {
        const prioridad: IPrioridad = sampleWithRequiredData;
        const prioridadCollection: IPrioridad[] = [sampleWithPartialData];
        expectedResult = service.addPrioridadToCollectionIfMissing(prioridadCollection, prioridad);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prioridad);
      });

      it('should add only unique Prioridad to an array', () => {
        const prioridadArray: IPrioridad[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const prioridadCollection: IPrioridad[] = [sampleWithRequiredData];
        expectedResult = service.addPrioridadToCollectionIfMissing(prioridadCollection, ...prioridadArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prioridad: IPrioridad = sampleWithRequiredData;
        const prioridad2: IPrioridad = sampleWithPartialData;
        expectedResult = service.addPrioridadToCollectionIfMissing([], prioridad, prioridad2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prioridad);
        expect(expectedResult).toContain(prioridad2);
      });

      it('should accept null and undefined values', () => {
        const prioridad: IPrioridad = sampleWithRequiredData;
        expectedResult = service.addPrioridadToCollectionIfMissing([], null, prioridad, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prioridad);
      });

      it('should return initial array if no Prioridad is added', () => {
        const prioridadCollection: IPrioridad[] = [sampleWithRequiredData];
        expectedResult = service.addPrioridadToCollectionIfMissing(prioridadCollection, undefined, null);
        expect(expectedResult).toEqual(prioridadCollection);
      });
    });

    describe('comparePrioridad', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePrioridad(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePrioridad(entity1, entity2);
        const compareResult2 = service.comparePrioridad(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePrioridad(entity1, entity2);
        const compareResult2 = service.comparePrioridad(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePrioridad(entity1, entity2);
        const compareResult2 = service.comparePrioridad(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
