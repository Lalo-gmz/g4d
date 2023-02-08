import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EstatusFuncionalidadFormService } from './estatus-funcionalidad-form.service';
import { EstatusFuncionalidadService } from '../service/estatus-funcionalidad.service';
import { IEstatusFuncionalidad } from '../estatus-funcionalidad.model';

import { EstatusFuncionalidadUpdateComponent } from './estatus-funcionalidad-update.component';

describe('EstatusFuncionalidad Management Update Component', () => {
  let comp: EstatusFuncionalidadUpdateComponent;
  let fixture: ComponentFixture<EstatusFuncionalidadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let estatusFuncionalidadFormService: EstatusFuncionalidadFormService;
  let estatusFuncionalidadService: EstatusFuncionalidadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EstatusFuncionalidadUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EstatusFuncionalidadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EstatusFuncionalidadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    estatusFuncionalidadFormService = TestBed.inject(EstatusFuncionalidadFormService);
    estatusFuncionalidadService = TestBed.inject(EstatusFuncionalidadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const estatusFuncionalidad: IEstatusFuncionalidad = { id: 456 };

      activatedRoute.data = of({ estatusFuncionalidad });
      comp.ngOnInit();

      expect(comp.estatusFuncionalidad).toEqual(estatusFuncionalidad);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEstatusFuncionalidad>>();
      const estatusFuncionalidad = { id: 123 };
      jest.spyOn(estatusFuncionalidadFormService, 'getEstatusFuncionalidad').mockReturnValue(estatusFuncionalidad);
      jest.spyOn(estatusFuncionalidadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ estatusFuncionalidad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: estatusFuncionalidad }));
      saveSubject.complete();

      // THEN
      expect(estatusFuncionalidadFormService.getEstatusFuncionalidad).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(estatusFuncionalidadService.update).toHaveBeenCalledWith(expect.objectContaining(estatusFuncionalidad));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEstatusFuncionalidad>>();
      const estatusFuncionalidad = { id: 123 };
      jest.spyOn(estatusFuncionalidadFormService, 'getEstatusFuncionalidad').mockReturnValue({ id: null });
      jest.spyOn(estatusFuncionalidadService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ estatusFuncionalidad: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: estatusFuncionalidad }));
      saveSubject.complete();

      // THEN
      expect(estatusFuncionalidadFormService.getEstatusFuncionalidad).toHaveBeenCalled();
      expect(estatusFuncionalidadService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEstatusFuncionalidad>>();
      const estatusFuncionalidad = { id: 123 };
      jest.spyOn(estatusFuncionalidadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ estatusFuncionalidad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(estatusFuncionalidadService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
