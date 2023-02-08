import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../iteracion.test-samples';

import { IteracionFormService } from './iteracion-form.service';

describe('Iteracion Form Service', () => {
  let service: IteracionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IteracionFormService);
  });

  describe('Service methods', () => {
    describe('createIteracionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIteracionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            inicio: expect.any(Object),
            fin: expect.any(Object),
            proyecto: expect.any(Object),
          })
        );
      });

      it('passing IIteracion should create a new form with FormGroup', () => {
        const formGroup = service.createIteracionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            inicio: expect.any(Object),
            fin: expect.any(Object),
            proyecto: expect.any(Object),
          })
        );
      });
    });

    describe('getIteracion', () => {
      it('should return NewIteracion for default Iteracion initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createIteracionFormGroup(sampleWithNewData);

        const iteracion = service.getIteracion(formGroup) as any;

        expect(iteracion).toMatchObject(sampleWithNewData);
      });

      it('should return NewIteracion for empty Iteracion initial value', () => {
        const formGroup = service.createIteracionFormGroup();

        const iteracion = service.getIteracion(formGroup) as any;

        expect(iteracion).toMatchObject({});
      });

      it('should return IIteracion', () => {
        const formGroup = service.createIteracionFormGroup(sampleWithRequiredData);

        const iteracion = service.getIteracion(formGroup) as any;

        expect(iteracion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIteracion should not enable id FormControl', () => {
        const formGroup = service.createIteracionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIteracion should disable id FormControl', () => {
        const formGroup = service.createIteracionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
