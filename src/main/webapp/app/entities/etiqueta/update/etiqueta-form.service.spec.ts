import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../etiqueta.test-samples';

import { EtiquetaFormService } from './etiqueta-form.service';

describe('Etiqueta Form Service', () => {
  let service: EtiquetaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EtiquetaFormService);
  });

  describe('Service methods', () => {
    describe('createEtiquetaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEtiquetaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            color: expect.any(Object),
            funcionalidad: expect.any(Object),
          })
        );
      });

      it('passing IEtiqueta should create a new form with FormGroup', () => {
        const formGroup = service.createEtiquetaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            color: expect.any(Object),
            funcionalidad: expect.any(Object),
          })
        );
      });
    });

    describe('getEtiqueta', () => {
      it('should return NewEtiqueta for default Etiqueta initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEtiquetaFormGroup(sampleWithNewData);

        const etiqueta = service.getEtiqueta(formGroup) as any;

        expect(etiqueta).toMatchObject(sampleWithNewData);
      });

      it('should return NewEtiqueta for empty Etiqueta initial value', () => {
        const formGroup = service.createEtiquetaFormGroup();

        const etiqueta = service.getEtiqueta(formGroup) as any;

        expect(etiqueta).toMatchObject({});
      });

      it('should return IEtiqueta', () => {
        const formGroup = service.createEtiquetaFormGroup(sampleWithRequiredData);

        const etiqueta = service.getEtiqueta(formGroup) as any;

        expect(etiqueta).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEtiqueta should not enable id FormControl', () => {
        const formGroup = service.createEtiquetaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEtiqueta should disable id FormControl', () => {
        const formGroup = service.createEtiquetaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
