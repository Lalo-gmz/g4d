import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../bitacora.test-samples';

import { BitacoraFormService } from './bitacora-form.service';

describe('Bitacora Form Service', () => {
  let service: BitacoraFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BitacoraFormService);
  });

  describe('Service methods', () => {
    describe('createBitacoraFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBitacoraFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tabla: expect.any(Object),
            accion: expect.any(Object),
            creado: expect.any(Object),
            user: expect.any(Object),
            proyecto: expect.any(Object),
          })
        );
      });

      it('passing IBitacora should create a new form with FormGroup', () => {
        const formGroup = service.createBitacoraFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tabla: expect.any(Object),
            accion: expect.any(Object),
            creado: expect.any(Object),
            user: expect.any(Object),
            proyecto: expect.any(Object),
          })
        );
      });
    });

    describe('getBitacora', () => {
      it('should return NewBitacora for default Bitacora initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBitacoraFormGroup(sampleWithNewData);

        const bitacora = service.getBitacora(formGroup) as any;

        expect(bitacora).toMatchObject(sampleWithNewData);
      });

      it('should return NewBitacora for empty Bitacora initial value', () => {
        const formGroup = service.createBitacoraFormGroup();

        const bitacora = service.getBitacora(formGroup) as any;

        expect(bitacora).toMatchObject({});
      });

      it('should return IBitacora', () => {
        const formGroup = service.createBitacoraFormGroup(sampleWithRequiredData);

        const bitacora = service.getBitacora(formGroup) as any;

        expect(bitacora).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBitacora should not enable id FormControl', () => {
        const formGroup = service.createBitacoraFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBitacora should disable id FormControl', () => {
        const formGroup = service.createBitacoraFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
