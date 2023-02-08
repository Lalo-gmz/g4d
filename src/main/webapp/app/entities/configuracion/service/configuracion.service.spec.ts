import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConfiguracion } from '../configuracion.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../configuracion.test-samples';

import { ConfiguracionService } from './configuracion.service';

const requireRestSample: IConfiguracion = {
  ...sampleWithRequiredData,
};

describe('Configuracion Service', () => {
  let service: ConfiguracionService;
  let httpMock: HttpTestingController;
  let expectedResult: IConfiguracion | IConfiguracion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConfiguracionService);
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

    it('should create a Configuracion', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const configuracion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(configuracion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Configuracion', () => {
      const configuracion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(configuracion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Configuracion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Configuracion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Configuracion', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addConfiguracionToCollectionIfMissing', () => {
      it('should add a Configuracion to an empty array', () => {
        const configuracion: IConfiguracion = sampleWithRequiredData;
        expectedResult = service.addConfiguracionToCollectionIfMissing([], configuracion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(configuracion);
      });

      it('should not add a Configuracion to an array that contains it', () => {
        const configuracion: IConfiguracion = sampleWithRequiredData;
        const configuracionCollection: IConfiguracion[] = [
          {
            ...configuracion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addConfiguracionToCollectionIfMissing(configuracionCollection, configuracion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Configuracion to an array that doesn't contain it", () => {
        const configuracion: IConfiguracion = sampleWithRequiredData;
        const configuracionCollection: IConfiguracion[] = [sampleWithPartialData];
        expectedResult = service.addConfiguracionToCollectionIfMissing(configuracionCollection, configuracion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(configuracion);
      });

      it('should add only unique Configuracion to an array', () => {
        const configuracionArray: IConfiguracion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const configuracionCollection: IConfiguracion[] = [sampleWithRequiredData];
        expectedResult = service.addConfiguracionToCollectionIfMissing(configuracionCollection, ...configuracionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const configuracion: IConfiguracion = sampleWithRequiredData;
        const configuracion2: IConfiguracion = sampleWithPartialData;
        expectedResult = service.addConfiguracionToCollectionIfMissing([], configuracion, configuracion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(configuracion);
        expect(expectedResult).toContain(configuracion2);
      });

      it('should accept null and undefined values', () => {
        const configuracion: IConfiguracion = sampleWithRequiredData;
        expectedResult = service.addConfiguracionToCollectionIfMissing([], null, configuracion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(configuracion);
      });

      it('should return initial array if no Configuracion is added', () => {
        const configuracionCollection: IConfiguracion[] = [sampleWithRequiredData];
        expectedResult = service.addConfiguracionToCollectionIfMissing(configuracionCollection, undefined, null);
        expect(expectedResult).toEqual(configuracionCollection);
      });
    });

    describe('compareConfiguracion', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareConfiguracion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareConfiguracion(entity1, entity2);
        const compareResult2 = service.compareConfiguracion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareConfiguracion(entity1, entity2);
        const compareResult2 = service.compareConfiguracion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareConfiguracion(entity1, entity2);
        const compareResult2 = service.compareConfiguracion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
