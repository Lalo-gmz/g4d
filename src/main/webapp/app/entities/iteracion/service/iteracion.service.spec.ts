import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IIteracion } from '../iteracion.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../iteracion.test-samples';

import { IteracionService, RestIteracion } from './iteracion.service';

const requireRestSample: RestIteracion = {
  ...sampleWithRequiredData,
  inicio: sampleWithRequiredData.inicio?.format(DATE_FORMAT),
  fin: sampleWithRequiredData.fin?.format(DATE_FORMAT),
};

describe('Iteracion Service', () => {
  let service: IteracionService;
  let httpMock: HttpTestingController;
  let expectedResult: IIteracion | IIteracion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IteracionService);
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

    it('should create a Iteracion', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const iteracion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(iteracion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Iteracion', () => {
      const iteracion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(iteracion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Iteracion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Iteracion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Iteracion', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIteracionToCollectionIfMissing', () => {
      it('should add a Iteracion to an empty array', () => {
        const iteracion: IIteracion = sampleWithRequiredData;
        expectedResult = service.addIteracionToCollectionIfMissing([], iteracion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(iteracion);
      });

      it('should not add a Iteracion to an array that contains it', () => {
        const iteracion: IIteracion = sampleWithRequiredData;
        const iteracionCollection: IIteracion[] = [
          {
            ...iteracion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIteracionToCollectionIfMissing(iteracionCollection, iteracion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Iteracion to an array that doesn't contain it", () => {
        const iteracion: IIteracion = sampleWithRequiredData;
        const iteracionCollection: IIteracion[] = [sampleWithPartialData];
        expectedResult = service.addIteracionToCollectionIfMissing(iteracionCollection, iteracion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(iteracion);
      });

      it('should add only unique Iteracion to an array', () => {
        const iteracionArray: IIteracion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const iteracionCollection: IIteracion[] = [sampleWithRequiredData];
        expectedResult = service.addIteracionToCollectionIfMissing(iteracionCollection, ...iteracionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const iteracion: IIteracion = sampleWithRequiredData;
        const iteracion2: IIteracion = sampleWithPartialData;
        expectedResult = service.addIteracionToCollectionIfMissing([], iteracion, iteracion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(iteracion);
        expect(expectedResult).toContain(iteracion2);
      });

      it('should accept null and undefined values', () => {
        const iteracion: IIteracion = sampleWithRequiredData;
        expectedResult = service.addIteracionToCollectionIfMissing([], null, iteracion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(iteracion);
      });

      it('should return initial array if no Iteracion is added', () => {
        const iteracionCollection: IIteracion[] = [sampleWithRequiredData];
        expectedResult = service.addIteracionToCollectionIfMissing(iteracionCollection, undefined, null);
        expect(expectedResult).toEqual(iteracionCollection);
      });
    });

    describe('compareIteracion', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIteracion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIteracion(entity1, entity2);
        const compareResult2 = service.compareIteracion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIteracion(entity1, entity2);
        const compareResult2 = service.compareIteracion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIteracion(entity1, entity2);
        const compareResult2 = service.compareIteracion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
