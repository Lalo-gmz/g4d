import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFuncionalidad } from '../funcionalidad.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../funcionalidad.test-samples';

import { FuncionalidadService, RestFuncionalidad } from './funcionalidad.service';

const requireRestSample: RestFuncionalidad = {
  ...sampleWithRequiredData,
  creado: sampleWithRequiredData.creado?.toJSON(),
  modificado: sampleWithRequiredData.modificado?.toJSON(),
};

describe('Funcionalidad Service', () => {
  let service: FuncionalidadService;
  let httpMock: HttpTestingController;
  let expectedResult: IFuncionalidad | IFuncionalidad[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FuncionalidadService);
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

    it('should create a Funcionalidad', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const funcionalidad = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(funcionalidad).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Funcionalidad', () => {
      const funcionalidad = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(funcionalidad).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Funcionalidad', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Funcionalidad', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Funcionalidad', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFuncionalidadToCollectionIfMissing', () => {
      it('should add a Funcionalidad to an empty array', () => {
        const funcionalidad: IFuncionalidad = sampleWithRequiredData;
        expectedResult = service.addFuncionalidadToCollectionIfMissing([], funcionalidad);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(funcionalidad);
      });

      it('should not add a Funcionalidad to an array that contains it', () => {
        const funcionalidad: IFuncionalidad = sampleWithRequiredData;
        const funcionalidadCollection: IFuncionalidad[] = [
          {
            ...funcionalidad,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFuncionalidadToCollectionIfMissing(funcionalidadCollection, funcionalidad);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Funcionalidad to an array that doesn't contain it", () => {
        const funcionalidad: IFuncionalidad = sampleWithRequiredData;
        const funcionalidadCollection: IFuncionalidad[] = [sampleWithPartialData];
        expectedResult = service.addFuncionalidadToCollectionIfMissing(funcionalidadCollection, funcionalidad);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(funcionalidad);
      });

      it('should add only unique Funcionalidad to an array', () => {
        const funcionalidadArray: IFuncionalidad[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const funcionalidadCollection: IFuncionalidad[] = [sampleWithRequiredData];
        expectedResult = service.addFuncionalidadToCollectionIfMissing(funcionalidadCollection, ...funcionalidadArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const funcionalidad: IFuncionalidad = sampleWithRequiredData;
        const funcionalidad2: IFuncionalidad = sampleWithPartialData;
        expectedResult = service.addFuncionalidadToCollectionIfMissing([], funcionalidad, funcionalidad2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(funcionalidad);
        expect(expectedResult).toContain(funcionalidad2);
      });

      it('should accept null and undefined values', () => {
        const funcionalidad: IFuncionalidad = sampleWithRequiredData;
        expectedResult = service.addFuncionalidadToCollectionIfMissing([], null, funcionalidad, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(funcionalidad);
      });

      it('should return initial array if no Funcionalidad is added', () => {
        const funcionalidadCollection: IFuncionalidad[] = [sampleWithRequiredData];
        expectedResult = service.addFuncionalidadToCollectionIfMissing(funcionalidadCollection, undefined, null);
        expect(expectedResult).toEqual(funcionalidadCollection);
      });
    });

    describe('compareFuncionalidad', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFuncionalidad(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFuncionalidad(entity1, entity2);
        const compareResult2 = service.compareFuncionalidad(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFuncionalidad(entity1, entity2);
        const compareResult2 = service.compareFuncionalidad(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFuncionalidad(entity1, entity2);
        const compareResult2 = service.compareFuncionalidad(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
