import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../atributo-funcionalidad.test-samples';

import { AtributoFuncionalidadFormService } from './atributo-funcionalidad-form.service';

describe('AtributoFuncionalidad Form Service', () => {
  let service: AtributoFuncionalidadFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AtributoFuncionalidadFormService);
  });

  describe('Service methods', () => {
    describe('createAtributoFuncionalidadFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAtributoFuncionalidadFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            marcado: expect.any(Object),
            valor: expect.any(Object),
            funcionalidad: expect.any(Object),
            atributo: expect.any(Object),
          })
        );
      });

      it('passing IAtributoFuncionalidad should create a new form with FormGroup', () => {
        const formGroup = service.createAtributoFuncionalidadFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            marcado: expect.any(Object),
            valor: expect.any(Object),
            funcionalidad: expect.any(Object),
            atributo: expect.any(Object),
          })
        );
      });
    });

    describe('getAtributoFuncionalidad', () => {
      it('should return NewAtributoFuncionalidad for default AtributoFuncionalidad initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAtributoFuncionalidadFormGroup(sampleWithNewData);

        const atributoFuncionalidad = service.getAtributoFuncionalidad(formGroup) as any;

        expect(atributoFuncionalidad).toMatchObject(sampleWithNewData);
      });

      it('should return NewAtributoFuncionalidad for empty AtributoFuncionalidad initial value', () => {
        const formGroup = service.createAtributoFuncionalidadFormGroup();

        const atributoFuncionalidad = service.getAtributoFuncionalidad(formGroup) as any;

        expect(atributoFuncionalidad).toMatchObject({});
      });

      it('should return IAtributoFuncionalidad', () => {
        const formGroup = service.createAtributoFuncionalidadFormGroup(sampleWithRequiredData);

        const atributoFuncionalidad = service.getAtributoFuncionalidad(formGroup) as any;

        expect(atributoFuncionalidad).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAtributoFuncionalidad should not enable id FormControl', () => {
        const formGroup = service.createAtributoFuncionalidadFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAtributoFuncionalidad should disable id FormControl', () => {
        const formGroup = service.createAtributoFuncionalidadFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
