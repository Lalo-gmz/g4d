import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBitacora } from '../bitacora.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../bitacora.test-samples';

import { BitacoraService, RestBitacora } from './bitacora.service';

const requireRestSample: RestBitacora = {
  ...sampleWithRequiredData,
  creado: sampleWithRequiredData.creado?.toJSON(),
};

describe('Bitacora Service', () => {
  let service: BitacoraService;
  let httpMock: HttpTestingController;
  let expectedResult: IBitacora | IBitacora[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BitacoraService);
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

    it('should create a Bitacora', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const bitacora = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bitacora).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Bitacora', () => {
      const bitacora = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bitacora).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Bitacora', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Bitacora', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Bitacora', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBitacoraToCollectionIfMissing', () => {
      it('should add a Bitacora to an empty array', () => {
        const bitacora: IBitacora = sampleWithRequiredData;
        expectedResult = service.addBitacoraToCollectionIfMissing([], bitacora);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bitacora);
      });

      it('should not add a Bitacora to an array that contains it', () => {
        const bitacora: IBitacora = sampleWithRequiredData;
        const bitacoraCollection: IBitacora[] = [
          {
            ...bitacora,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBitacoraToCollectionIfMissing(bitacoraCollection, bitacora);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Bitacora to an array that doesn't contain it", () => {
        const bitacora: IBitacora = sampleWithRequiredData;
        const bitacoraCollection: IBitacora[] = [sampleWithPartialData];
        expectedResult = service.addBitacoraToCollectionIfMissing(bitacoraCollection, bitacora);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bitacora);
      });

      it('should add only unique Bitacora to an array', () => {
        const bitacoraArray: IBitacora[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bitacoraCollection: IBitacora[] = [sampleWithRequiredData];
        expectedResult = service.addBitacoraToCollectionIfMissing(bitacoraCollection, ...bitacoraArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bitacora: IBitacora = sampleWithRequiredData;
        const bitacora2: IBitacora = sampleWithPartialData;
        expectedResult = service.addBitacoraToCollectionIfMissing([], bitacora, bitacora2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bitacora);
        expect(expectedResult).toContain(bitacora2);
      });

      it('should accept null and undefined values', () => {
        const bitacora: IBitacora = sampleWithRequiredData;
        expectedResult = service.addBitacoraToCollectionIfMissing([], null, bitacora, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bitacora);
      });

      it('should return initial array if no Bitacora is added', () => {
        const bitacoraCollection: IBitacora[] = [sampleWithRequiredData];
        expectedResult = service.addBitacoraToCollectionIfMissing(bitacoraCollection, undefined, null);
        expect(expectedResult).toEqual(bitacoraCollection);
      });
    });

    describe('compareBitacora', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBitacora(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBitacora(entity1, entity2);
        const compareResult2 = service.compareBitacora(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBitacora(entity1, entity2);
        const compareResult2 = service.compareBitacora(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBitacora(entity1, entity2);
        const compareResult2 = service.compareBitacora(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
