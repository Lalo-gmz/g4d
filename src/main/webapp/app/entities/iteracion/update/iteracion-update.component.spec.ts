import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IteracionFormService } from './iteracion-form.service';
import { IteracionService } from '../service/iteracion.service';
import { IIteracion } from '../iteracion.model';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';
import { ProyectoService } from 'app/entities/proyecto/service/proyecto.service';

import { IteracionUpdateComponent } from './iteracion-update.component';

describe('Iteracion Management Update Component', () => {
  let comp: IteracionUpdateComponent;
  let fixture: ComponentFixture<IteracionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let iteracionFormService: IteracionFormService;
  let iteracionService: IteracionService;
  let proyectoService: ProyectoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IteracionUpdateComponent],
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
      .overrideTemplate(IteracionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IteracionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    iteracionFormService = TestBed.inject(IteracionFormService);
    iteracionService = TestBed.inject(IteracionService);
    proyectoService = TestBed.inject(ProyectoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Proyecto query and add missing value', () => {
      const iteracion: IIteracion = { id: 456 };
      const proyecto: IProyecto = { id: 62074 };
      iteracion.proyecto = proyecto;

      const proyectoCollection: IProyecto[] = [{ id: 99865 }];
      jest.spyOn(proyectoService, 'query').mockReturnValue(of(new HttpResponse({ body: proyectoCollection })));
      const additionalProyectos = [proyecto];
      const expectedCollection: IProyecto[] = [...additionalProyectos, ...proyectoCollection];
      jest.spyOn(proyectoService, 'addProyectoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ iteracion });
      comp.ngOnInit();

      expect(proyectoService.query).toHaveBeenCalled();
      expect(proyectoService.addProyectoToCollectionIfMissing).toHaveBeenCalledWith(
        proyectoCollection,
        ...additionalProyectos.map(expect.objectContaining)
      );
      expect(comp.proyectosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const iteracion: IIteracion = { id: 456 };
      const proyecto: IProyecto = { id: 68747 };
      iteracion.proyecto = proyecto;

      activatedRoute.data = of({ iteracion });
      comp.ngOnInit();

      expect(comp.proyectosSharedCollection).toContain(proyecto);
      expect(comp.iteracion).toEqual(iteracion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIteracion>>();
      const iteracion = { id: 123 };
      jest.spyOn(iteracionFormService, 'getIteracion').mockReturnValue(iteracion);
      jest.spyOn(iteracionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ iteracion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: iteracion }));
      saveSubject.complete();

      // THEN
      expect(iteracionFormService.getIteracion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(iteracionService.update).toHaveBeenCalledWith(expect.objectContaining(iteracion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIteracion>>();
      const iteracion = { id: 123 };
      jest.spyOn(iteracionFormService, 'getIteracion').mockReturnValue({ id: null });
      jest.spyOn(iteracionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ iteracion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: iteracion }));
      saveSubject.complete();

      // THEN
      expect(iteracionFormService.getIteracion).toHaveBeenCalled();
      expect(iteracionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIteracion>>();
      const iteracion = { id: 123 };
      jest.spyOn(iteracionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ iteracion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(iteracionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProyecto', () => {
      it('Should forward to proyectoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(proyectoService, 'compareProyecto');
        comp.compareProyecto(entity, entity2);
        expect(proyectoService.compareProyecto).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
