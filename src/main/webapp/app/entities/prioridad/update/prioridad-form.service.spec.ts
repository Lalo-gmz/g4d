import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../prioridad.test-samples';

import { PrioridadFormService } from './prioridad-form.service';

describe('Prioridad Form Service', () => {
  let service: PrioridadFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PrioridadFormService);
  });

  describe('Service methods', () => {
    describe('createPrioridadFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPrioridadFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            prioridadNumerica: expect.any(Object),
          })
        );
      });

      it('passing IPrioridad should create a new form with FormGroup', () => {
        const formGroup = service.createPrioridadFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            prioridadNumerica: expect.any(Object),
          })
        );
      });
    });

    describe('getPrioridad', () => {
      it('should return NewPrioridad for default Prioridad initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPrioridadFormGroup(sampleWithNewData);

        const prioridad = service.getPrioridad(formGroup) as any;

        expect(prioridad).toMatchObject(sampleWithNewData);
      });

      it('should return NewPrioridad for empty Prioridad initial value', () => {
        const formGroup = service.createPrioridadFormGroup();

        const prioridad = service.getPrioridad(formGroup) as any;

        expect(prioridad).toMatchObject({});
      });

      it('should return IPrioridad', () => {
        const formGroup = service.createPrioridadFormGroup(sampleWithRequiredData);

        const prioridad = service.getPrioridad(formGroup) as any;

        expect(prioridad).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPrioridad should not enable id FormControl', () => {
        const formGroup = service.createPrioridadFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPrioridad should disable id FormControl', () => {
        const formGroup = service.createPrioridadFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
