import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAtributoFuncionalidad } from '../atributo-funcionalidad.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../atributo-funcionalidad.test-samples';

import { AtributoFuncionalidadService } from './atributo-funcionalidad.service';

const requireRestSample: IAtributoFuncionalidad = {
  ...sampleWithRequiredData,
};

describe('AtributoFuncionalidad Service', () => {
  let service: AtributoFuncionalidadService;
  let httpMock: HttpTestingController;
  let expectedResult: IAtributoFuncionalidad | IAtributoFuncionalidad[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AtributoFuncionalidadService);
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

    it('should create a AtributoFuncionalidad', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const atributoFuncionalidad = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(atributoFuncionalidad).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AtributoFuncionalidad', () => {
      const atributoFuncionalidad = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(atributoFuncionalidad).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AtributoFuncionalidad', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AtributoFuncionalidad', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AtributoFuncionalidad', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAtributoFuncionalidadToCollectionIfMissing', () => {
      it('should add a AtributoFuncionalidad to an empty array', () => {
        const atributoFuncionalidad: IAtributoFuncionalidad = sampleWithRequiredData;
        expectedResult = service.addAtributoFuncionalidadToCollectionIfMissing([], atributoFuncionalidad);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(atributoFuncionalidad);
      });

      it('should not add a AtributoFuncionalidad to an array that contains it', () => {
        const atributoFuncionalidad: IAtributoFuncionalidad = sampleWithRequiredData;
        const atributoFuncionalidadCollection: IAtributoFuncionalidad[] = [
          {
            ...atributoFuncionalidad,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAtributoFuncionalidadToCollectionIfMissing(atributoFuncionalidadCollection, atributoFuncionalidad);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AtributoFuncionalidad to an array that doesn't contain it", () => {
        const atributoFuncionalidad: IAtributoFuncionalidad = sampleWithRequiredData;
        const atributoFuncionalidadCollection: IAtributoFuncionalidad[] = [sampleWithPartialData];
        expectedResult = service.addAtributoFuncionalidadToCollectionIfMissing(atributoFuncionalidadCollection, atributoFuncionalidad);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(atributoFuncionalidad);
      });

      it('should add only unique AtributoFuncionalidad to an array', () => {
        const atributoFuncionalidadArray: IAtributoFuncionalidad[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const atributoFuncionalidadCollection: IAtributoFuncionalidad[] = [sampleWithRequiredData];
        expectedResult = service.addAtributoFuncionalidadToCollectionIfMissing(
          atributoFuncionalidadCollection,
          ...atributoFuncionalidadArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const atributoFuncionalidad: IAtributoFuncionalidad = sampleWithRequiredData;
        const atributoFuncionalidad2: IAtributoFuncionalidad = sampleWithPartialData;
        expectedResult = service.addAtributoFuncionalidadToCollectionIfMissing([], atributoFuncionalidad, atributoFuncionalidad2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(atributoFuncionalidad);
        expect(expectedResult).toContain(atributoFuncionalidad2);
      });

      it('should accept null and undefined values', () => {
        const atributoFuncionalidad: IAtributoFuncionalidad = sampleWithRequiredData;
        expectedResult = service.addAtributoFuncionalidadToCollectionIfMissing([], null, atributoFuncionalidad, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(atributoFuncionalidad);
      });

      it('should return initial array if no AtributoFuncionalidad is added', () => {
        const atributoFuncionalidadCollection: IAtributoFuncionalidad[] = [sampleWithRequiredData];
        expectedResult = service.addAtributoFuncionalidadToCollectionIfMissing(atributoFuncionalidadCollection, undefined, null);
        expect(expectedResult).toEqual(atributoFuncionalidadCollection);
      });
    });

    describe('compareAtributoFuncionalidad', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAtributoFuncionalidad(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAtributoFuncionalidad(entity1, entity2);
        const compareResult2 = service.compareAtributoFuncionalidad(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAtributoFuncionalidad(entity1, entity2);
        const compareResult2 = service.compareAtributoFuncionalidad(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAtributoFuncionalidad(entity1, entity2);
        const compareResult2 = service.compareAtributoFuncionalidad(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
