import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PrioridadFormService } from './prioridad-form.service';
import { PrioridadService } from '../service/prioridad.service';
import { IPrioridad } from '../prioridad.model';

import { PrioridadUpdateComponent } from './prioridad-update.component';

describe('Prioridad Management Update Component', () => {
  let comp: PrioridadUpdateComponent;
  let fixture: ComponentFixture<PrioridadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let prioridadFormService: PrioridadFormService;
  let prioridadService: PrioridadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PrioridadUpdateComponent],
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
      .overrideTemplate(PrioridadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrioridadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prioridadFormService = TestBed.inject(PrioridadFormService);
    prioridadService = TestBed.inject(PrioridadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const prioridad: IPrioridad = { id: 456 };

      activatedRoute.data = of({ prioridad });
      comp.ngOnInit();

      expect(comp.prioridad).toEqual(prioridad);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrioridad>>();
      const prioridad = { id: 123 };
      jest.spyOn(prioridadFormService, 'getPrioridad').mockReturnValue(prioridad);
      jest.spyOn(prioridadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prioridad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prioridad }));
      saveSubject.complete();

      // THEN
      expect(prioridadFormService.getPrioridad).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(prioridadService.update).toHaveBeenCalledWith(expect.objectContaining(prioridad));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrioridad>>();
      const prioridad = { id: 123 };
      jest.spyOn(prioridadFormService, 'getPrioridad').mockReturnValue({ id: null });
      jest.spyOn(prioridadService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prioridad: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prioridad }));
      saveSubject.complete();

      // THEN
      expect(prioridadFormService.getPrioridad).toHaveBeenCalled();
      expect(prioridadService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrioridad>>();
      const prioridad = { id: 123 };
      jest.spyOn(prioridadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prioridad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(prioridadService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
