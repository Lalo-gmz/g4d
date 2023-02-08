import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPermiso } from '../permiso.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../permiso.test-samples';

import { PermisoService } from './permiso.service';

const requireRestSample: IPermiso = {
  ...sampleWithRequiredData,
};

describe('Permiso Service', () => {
  let service: PermisoService;
  let httpMock: HttpTestingController;
  let expectedResult: IPermiso | IPermiso[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PermisoService);
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

    it('should create a Permiso', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const permiso = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(permiso).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Permiso', () => {
      const permiso = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(permiso).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Permiso', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Permiso', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Permiso', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPermisoToCollectionIfMissing', () => {
      it('should add a Permiso to an empty array', () => {
        const permiso: IPermiso = sampleWithRequiredData;
        expectedResult = service.addPermisoToCollectionIfMissing([], permiso);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(permiso);
      });

      it('should not add a Permiso to an array that contains it', () => {
        const permiso: IPermiso = sampleWithRequiredData;
        const permisoCollection: IPermiso[] = [
          {
            ...permiso,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPermisoToCollectionIfMissing(permisoCollection, permiso);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Permiso to an array that doesn't contain it", () => {
        const permiso: IPermiso = sampleWithRequiredData;
        const permisoCollection: IPermiso[] = [sampleWithPartialData];
        expectedResult = service.addPermisoToCollectionIfMissing(permisoCollection, permiso);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(permiso);
      });

      it('should add only unique Permiso to an array', () => {
        const permisoArray: IPermiso[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const permisoCollection: IPermiso[] = [sampleWithRequiredData];
        expectedResult = service.addPermisoToCollectionIfMissing(permisoCollection, ...permisoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const permiso: IPermiso = sampleWithRequiredData;
        const permiso2: IPermiso = sampleWithPartialData;
        expectedResult = service.addPermisoToCollectionIfMissing([], permiso, permiso2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(permiso);
        expect(expectedResult).toContain(permiso2);
      });

      it('should accept null and undefined values', () => {
        const permiso: IPermiso = sampleWithRequiredData;
        expectedResult = service.addPermisoToCollectionIfMissing([], null, permiso, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(permiso);
      });

      it('should return initial array if no Permiso is added', () => {
        const permisoCollection: IPermiso[] = [sampleWithRequiredData];
        expectedResult = service.addPermisoToCollectionIfMissing(permisoCollection, undefined, null);
        expect(expectedResult).toEqual(permisoCollection);
      });
    });

    describe('comparePermiso', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePermiso(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePermiso(entity1, entity2);
        const compareResult2 = service.comparePermiso(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePermiso(entity1, entity2);
        const compareResult2 = service.comparePermiso(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePermiso(entity1, entity2);
        const compareResult2 = service.comparePermiso(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
