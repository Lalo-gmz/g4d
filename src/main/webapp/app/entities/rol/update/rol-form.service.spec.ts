import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../rol.test-samples';

import { RolFormService } from './rol-form.service';

describe('Rol Form Service', () => {
  let service: RolFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RolFormService);
  });

  describe('Service methods', () => {
    describe('createRolFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRolFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
          })
        );
      });

      it('passing IRol should create a new form with FormGroup', () => {
        const formGroup = service.createRolFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
          })
        );
      });
    });

    describe('getRol', () => {
      it('should return NewRol for default Rol initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRolFormGroup(sampleWithNewData);

        const rol = service.getRol(formGroup) as any;

        expect(rol).toMatchObject(sampleWithNewData);
      });

      it('should return NewRol for empty Rol initial value', () => {
        const formGroup = service.createRolFormGroup();

        const rol = service.getRol(formGroup) as any;

        expect(rol).toMatchObject({});
      });

      it('should return IRol', () => {
        const formGroup = service.createRolFormGroup(sampleWithRequiredData);

        const rol = service.getRol(formGroup) as any;

        expect(rol).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRol should not enable id FormControl', () => {
        const formGroup = service.createRolFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRol should disable id FormControl', () => {
        const formGroup = service.createRolFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
