import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../participacion-proyecto.test-samples';

import { ParticipacionProyectoFormService } from './participacion-proyecto-form.service';

describe('ParticipacionProyecto Form Service', () => {
  let service: ParticipacionProyectoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ParticipacionProyectoFormService);
  });

  describe('Service methods', () => {
    describe('createParticipacionProyectoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createParticipacionProyectoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            user: expect.any(Object),
            proyecto: expect.any(Object),
            rol: expect.any(Object),
          })
        );
      });

      it('passing IParticipacionProyecto should create a new form with FormGroup', () => {
        const formGroup = service.createParticipacionProyectoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            user: expect.any(Object),
            proyecto: expect.any(Object),
            rol: expect.any(Object),
          })
        );
      });
    });

    describe('getParticipacionProyecto', () => {
      it('should return NewParticipacionProyecto for default ParticipacionProyecto initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createParticipacionProyectoFormGroup(sampleWithNewData);

        const participacionProyecto = service.getParticipacionProyecto(formGroup) as any;

        expect(participacionProyecto).toMatchObject(sampleWithNewData);
      });

      it('should return NewParticipacionProyecto for empty ParticipacionProyecto initial value', () => {
        const formGroup = service.createParticipacionProyectoFormGroup();

        const participacionProyecto = service.getParticipacionProyecto(formGroup) as any;

        expect(participacionProyecto).toMatchObject({});
      });

      it('should return IParticipacionProyecto', () => {
        const formGroup = service.createParticipacionProyectoFormGroup(sampleWithRequiredData);

        const participacionProyecto = service.getParticipacionProyecto(formGroup) as any;

        expect(participacionProyecto).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IParticipacionProyecto should not enable id FormControl', () => {
        const formGroup = service.createParticipacionProyectoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewParticipacionProyecto should disable id FormControl', () => {
        const formGroup = service.createParticipacionProyectoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
