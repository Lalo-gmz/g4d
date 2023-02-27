import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../captura.test-samples';

import { CapturaFormService } from './captura-form.service';

describe('Captura Form Service', () => {
  let service: CapturaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CapturaFormService);
  });

  describe('Service methods', () => {
    describe('createCapturaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCapturaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            funcionalidades: expect.any(Object),
            estatus: expect.any(Object),
            fecha: expect.any(Object),
            iteracion: expect.any(Object),
          })
        );
      });

      it('passing ICaptura should create a new form with FormGroup', () => {
        const formGroup = service.createCapturaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            funcionalidades: expect.any(Object),
            estatus: expect.any(Object),
            fecha: expect.any(Object),
            iteracion: expect.any(Object),
          })
        );
      });
    });

    describe('getCaptura', () => {
      it('should return NewCaptura for default Captura initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCapturaFormGroup(sampleWithNewData);

        const captura = service.getCaptura(formGroup) as any;

        expect(captura).toMatchObject(sampleWithNewData);
      });

      it('should return NewCaptura for empty Captura initial value', () => {
        const formGroup = service.createCapturaFormGroup();

        const captura = service.getCaptura(formGroup) as any;

        expect(captura).toMatchObject({});
      });

      it('should return ICaptura', () => {
        const formGroup = service.createCapturaFormGroup(sampleWithRequiredData);

        const captura = service.getCaptura(formGroup) as any;

        expect(captura).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICaptura should not enable id FormControl', () => {
        const formGroup = service.createCapturaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCaptura should disable id FormControl', () => {
        const formGroup = service.createCapturaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
