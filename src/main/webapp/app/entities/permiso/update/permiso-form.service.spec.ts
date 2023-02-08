import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../permiso.test-samples';

import { PermisoFormService } from './permiso-form.service';

describe('Permiso Form Service', () => {
  let service: PermisoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PermisoFormService);
  });

  describe('Service methods', () => {
    describe('createPermisoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPermisoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            rol: expect.any(Object),
          })
        );
      });

      it('passing IPermiso should create a new form with FormGroup', () => {
        const formGroup = service.createPermisoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            rol: expect.any(Object),
          })
        );
      });
    });

    describe('getPermiso', () => {
      it('should return NewPermiso for default Permiso initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPermisoFormGroup(sampleWithNewData);

        const permiso = service.getPermiso(formGroup) as any;

        expect(permiso).toMatchObject(sampleWithNewData);
      });

      it('should return NewPermiso for empty Permiso initial value', () => {
        const formGroup = service.createPermisoFormGroup();

        const permiso = service.getPermiso(formGroup) as any;

        expect(permiso).toMatchObject({});
      });

      it('should return IPermiso', () => {
        const formGroup = service.createPermisoFormGroup(sampleWithRequiredData);

        const permiso = service.getPermiso(formGroup) as any;

        expect(permiso).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPermiso should not enable id FormControl', () => {
        const formGroup = service.createPermisoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPermiso should disable id FormControl', () => {
        const formGroup = service.createPermisoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
