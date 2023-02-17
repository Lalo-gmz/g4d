import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../funcionalidad.test-samples';

import { FuncionalidadFormService } from './funcionalidad-form.service';

describe('Funcionalidad Form Service', () => {
  let service: FuncionalidadFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FuncionalidadFormService);
  });

  describe('Service methods', () => {
    describe('createFuncionalidadFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFuncionalidadFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            urlGitLab: expect.any(Object),
            fechaEntrega: expect.any(Object),
            creado: expect.any(Object),
            modificado: expect.any(Object),
            users: expect.any(Object),
            estatusFuncionalidad: expect.any(Object),
            iteracion: expect.any(Object),
            prioridad: expect.any(Object),
          })
        );
      });

      it('passing IFuncionalidad should create a new form with FormGroup', () => {
        const formGroup = service.createFuncionalidadFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            urlGitLab: expect.any(Object),
            fechaEntrega: expect.any(Object),
            creado: expect.any(Object),
            modificado: expect.any(Object),
            users: expect.any(Object),
            estatusFuncionalidad: expect.any(Object),
            iteracion: expect.any(Object),
            prioridad: expect.any(Object),
          })
        );
      });
    });

    describe('getFuncionalidad', () => {
      it('should return NewFuncionalidad for default Funcionalidad initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFuncionalidadFormGroup(sampleWithNewData);

        const funcionalidad = service.getFuncionalidad(formGroup) as any;

        expect(funcionalidad).toMatchObject(sampleWithNewData);
      });

      it('should return NewFuncionalidad for empty Funcionalidad initial value', () => {
        const formGroup = service.createFuncionalidadFormGroup();

        const funcionalidad = service.getFuncionalidad(formGroup) as any;

        expect(funcionalidad).toMatchObject({});
      });

      it('should return IFuncionalidad', () => {
        const formGroup = service.createFuncionalidadFormGroup(sampleWithRequiredData);

        const funcionalidad = service.getFuncionalidad(formGroup) as any;

        expect(funcionalidad).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFuncionalidad should not enable id FormControl', () => {
        const formGroup = service.createFuncionalidadFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFuncionalidad should disable id FormControl', () => {
        const formGroup = service.createFuncionalidadFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
