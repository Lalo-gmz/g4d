import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEstatusFuncionalidad } from '../estatus-funcionalidad.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../estatus-funcionalidad.test-samples';

import { EstatusFuncionalidadService } from './estatus-funcionalidad.service';

const requireRestSample: IEstatusFuncionalidad = {
  ...sampleWithRequiredData,
};

describe('EstatusFuncionalidad Service', () => {
  let service: EstatusFuncionalidadService;
  let httpMock: HttpTestingController;
  let expectedResult: IEstatusFuncionalidad | IEstatusFuncionalidad[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EstatusFuncionalidadService);
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

    it('should create a EstatusFuncionalidad', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const estatusFuncionalidad = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(estatusFuncionalidad).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EstatusFuncionalidad', () => {
      const estatusFuncionalidad = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(estatusFuncionalidad).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EstatusFuncionalidad', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EstatusFuncionalidad', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EstatusFuncionalidad', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEstatusFuncionalidadToCollectionIfMissing', () => {
      it('should add a EstatusFuncionalidad to an empty array', () => {
        const estatusFuncionalidad: IEstatusFuncionalidad = sampleWithRequiredData;
        expectedResult = service.addEstatusFuncionalidadToCollectionIfMissing([], estatusFuncionalidad);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(estatusFuncionalidad);
      });

      it('should not add a EstatusFuncionalidad to an array that contains it', () => {
        const estatusFuncionalidad: IEstatusFuncionalidad = sampleWithRequiredData;
        const estatusFuncionalidadCollection: IEstatusFuncionalidad[] = [
          {
            ...estatusFuncionalidad,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEstatusFuncionalidadToCollectionIfMissing(estatusFuncionalidadCollection, estatusFuncionalidad);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EstatusFuncionalidad to an array that doesn't contain it", () => {
        const estatusFuncionalidad: IEstatusFuncionalidad = sampleWithRequiredData;
        const estatusFuncionalidadCollection: IEstatusFuncionalidad[] = [sampleWithPartialData];
        expectedResult = service.addEstatusFuncionalidadToCollectionIfMissing(estatusFuncionalidadCollection, estatusFuncionalidad);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(estatusFuncionalidad);
      });

      it('should add only unique EstatusFuncionalidad to an array', () => {
        const estatusFuncionalidadArray: IEstatusFuncionalidad[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const estatusFuncionalidadCollection: IEstatusFuncionalidad[] = [sampleWithRequiredData];
        expectedResult = service.addEstatusFuncionalidadToCollectionIfMissing(estatusFuncionalidadCollection, ...estatusFuncionalidadArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const estatusFuncionalidad: IEstatusFuncionalidad = sampleWithRequiredData;
        const estatusFuncionalidad2: IEstatusFuncionalidad = sampleWithPartialData;
        expectedResult = service.addEstatusFuncionalidadToCollectionIfMissing([], estatusFuncionalidad, estatusFuncionalidad2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(estatusFuncionalidad);
        expect(expectedResult).toContain(estatusFuncionalidad2);
      });

      it('should accept null and undefined values', () => {
        const estatusFuncionalidad: IEstatusFuncionalidad = sampleWithRequiredData;
        expectedResult = service.addEstatusFuncionalidadToCollectionIfMissing([], null, estatusFuncionalidad, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(estatusFuncionalidad);
      });

      it('should return initial array if no EstatusFuncionalidad is added', () => {
        const estatusFuncionalidadCollection: IEstatusFuncionalidad[] = [sampleWithRequiredData];
        expectedResult = service.addEstatusFuncionalidadToCollectionIfMissing(estatusFuncionalidadCollection, undefined, null);
        expect(expectedResult).toEqual(estatusFuncionalidadCollection);
      });
    });

    describe('compareEstatusFuncionalidad', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEstatusFuncionalidad(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEstatusFuncionalidad(entity1, entity2);
        const compareResult2 = service.compareEstatusFuncionalidad(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEstatusFuncionalidad(entity1, entity2);
        const compareResult2 = service.compareEstatusFuncionalidad(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEstatusFuncionalidad(entity1, entity2);
        const compareResult2 = service.compareEstatusFuncionalidad(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
