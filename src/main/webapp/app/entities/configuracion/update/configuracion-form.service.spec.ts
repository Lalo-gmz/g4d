import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../configuracion.test-samples';

import { ConfiguracionFormService } from './configuracion-form.service';

describe('Configuracion Form Service', () => {
  let service: ConfiguracionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConfiguracionFormService);
  });

  describe('Service methods', () => {
    describe('createConfiguracionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createConfiguracionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            clave: expect.any(Object),
            valor: expect.any(Object),
            proyecto: expect.any(Object),
          })
        );
      });

      it('passing IConfiguracion should create a new form with FormGroup', () => {
        const formGroup = service.createConfiguracionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            clave: expect.any(Object),
            valor: expect.any(Object),
            proyecto: expect.any(Object),
          })
        );
      });
    });

    describe('getConfiguracion', () => {
      it('should return NewConfiguracion for default Configuracion initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createConfiguracionFormGroup(sampleWithNewData);

        const configuracion = service.getConfiguracion(formGroup) as any;

        expect(configuracion).toMatchObject(sampleWithNewData);
      });

      it('should return NewConfiguracion for empty Configuracion initial value', () => {
        const formGroup = service.createConfiguracionFormGroup();

        const configuracion = service.getConfiguracion(formGroup) as any;

        expect(configuracion).toMatchObject({});
      });

      it('should return IConfiguracion', () => {
        const formGroup = service.createConfiguracionFormGroup(sampleWithRequiredData);

        const configuracion = service.getConfiguracion(formGroup) as any;

        expect(configuracion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConfiguracion should not enable id FormControl', () => {
        const formGroup = service.createConfiguracionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConfiguracion should disable id FormControl', () => {
        const formGroup = service.createConfiguracionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
