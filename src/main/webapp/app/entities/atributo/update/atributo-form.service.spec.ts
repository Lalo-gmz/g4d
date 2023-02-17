import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../atributo.test-samples';

import { AtributoFormService } from './atributo-form.service';

describe('Atributo Form Service', () => {
  let service: AtributoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AtributoFormService);
  });

  describe('Service methods', () => {
    describe('createAtributoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAtributoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
          })
        );
      });

      it('passing IAtributo should create a new form with FormGroup', () => {
        const formGroup = service.createAtributoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
          })
        );
      });
    });

    describe('getAtributo', () => {
      it('should return NewAtributo for default Atributo initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAtributoFormGroup(sampleWithNewData);

        const atributo = service.getAtributo(formGroup) as any;

        expect(atributo).toMatchObject(sampleWithNewData);
      });

      it('should return NewAtributo for empty Atributo initial value', () => {
        const formGroup = service.createAtributoFormGroup();

        const atributo = service.getAtributo(formGroup) as any;

        expect(atributo).toMatchObject({});
      });

      it('should return IAtributo', () => {
        const formGroup = service.createAtributoFormGroup(sampleWithRequiredData);

        const atributo = service.getAtributo(formGroup) as any;

        expect(atributo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAtributo should not enable id FormControl', () => {
        const formGroup = service.createAtributoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAtributo should disable id FormControl', () => {
        const formGroup = service.createAtributoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
