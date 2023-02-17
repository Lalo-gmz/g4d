import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../estatus-funcionalidad.test-samples';

import { EstatusFuncionalidadFormService } from './estatus-funcionalidad-form.service';

describe('EstatusFuncionalidad Form Service', () => {
  let service: EstatusFuncionalidadFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EstatusFuncionalidadFormService);
  });

  describe('Service methods', () => {
    describe('createEstatusFuncionalidadFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEstatusFuncionalidadFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
          })
        );
      });

      it('passing IEstatusFuncionalidad should create a new form with FormGroup', () => {
        const formGroup = service.createEstatusFuncionalidadFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
          })
        );
      });
    });

    describe('getEstatusFuncionalidad', () => {
      it('should return NewEstatusFuncionalidad for default EstatusFuncionalidad initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEstatusFuncionalidadFormGroup(sampleWithNewData);

        const estatusFuncionalidad = service.getEstatusFuncionalidad(formGroup) as any;

        expect(estatusFuncionalidad).toMatchObject(sampleWithNewData);
      });

      it('should return NewEstatusFuncionalidad for empty EstatusFuncionalidad initial value', () => {
        const formGroup = service.createEstatusFuncionalidadFormGroup();

        const estatusFuncionalidad = service.getEstatusFuncionalidad(formGroup) as any;

        expect(estatusFuncionalidad).toMatchObject({});
      });

      it('should return IEstatusFuncionalidad', () => {
        const formGroup = service.createEstatusFuncionalidadFormGroup(sampleWithRequiredData);

        const estatusFuncionalidad = service.getEstatusFuncionalidad(formGroup) as any;

        expect(estatusFuncionalidad).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEstatusFuncionalidad should not enable id FormControl', () => {
        const formGroup = service.createEstatusFuncionalidadFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEstatusFuncionalidad should disable id FormControl', () => {
        const formGroup = service.createEstatusFuncionalidadFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
