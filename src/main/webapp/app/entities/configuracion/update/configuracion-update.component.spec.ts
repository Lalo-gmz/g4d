import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ConfiguracionFormService } from './configuracion-form.service';
import { ConfiguracionService } from '../service/configuracion.service';
import { IConfiguracion } from '../configuracion.model';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';
import { ProyectoService } from 'app/entities/proyecto/service/proyecto.service';

import { ConfiguracionUpdateComponent } from './configuracion-update.component';

describe('Configuracion Management Update Component', () => {
  let comp: ConfiguracionUpdateComponent;
  let fixture: ComponentFixture<ConfiguracionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let configuracionFormService: ConfiguracionFormService;
  let configuracionService: ConfiguracionService;
  let proyectoService: ProyectoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ConfiguracionUpdateComponent],
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
      .overrideTemplate(ConfiguracionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConfiguracionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    configuracionFormService = TestBed.inject(ConfiguracionFormService);
    configuracionService = TestBed.inject(ConfiguracionService);
    proyectoService = TestBed.inject(ProyectoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Proyecto query and add missing value', () => {
      const configuracion: IConfiguracion = { id: 456 };
      const proyecto: IProyecto = { id: 12040 };
      configuracion.proyecto = proyecto;

      const proyectoCollection: IProyecto[] = [{ id: 38020 }];
      jest.spyOn(proyectoService, 'query').mockReturnValue(of(new HttpResponse({ body: proyectoCollection })));
      const additionalProyectos = [proyecto];
      const expectedCollection: IProyecto[] = [...additionalProyectos, ...proyectoCollection];
      jest.spyOn(proyectoService, 'addProyectoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ configuracion });
      comp.ngOnInit();

      expect(proyectoService.query).toHaveBeenCalled();
      expect(proyectoService.addProyectoToCollectionIfMissing).toHaveBeenCalledWith(
        proyectoCollection,
        ...additionalProyectos.map(expect.objectContaining)
      );
      expect(comp.proyectosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const configuracion: IConfiguracion = { id: 456 };
      const proyecto: IProyecto = { id: 38589 };
      configuracion.proyecto = proyecto;

      activatedRoute.data = of({ configuracion });
      comp.ngOnInit();

      expect(comp.proyectosSharedCollection).toContain(proyecto);
      expect(comp.configuracion).toEqual(configuracion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConfiguracion>>();
      const configuracion = { id: 123 };
      jest.spyOn(configuracionFormService, 'getConfiguracion').mockReturnValue(configuracion);
      jest.spyOn(configuracionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ configuracion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: configuracion }));
      saveSubject.complete();

      // THEN
      expect(configuracionFormService.getConfiguracion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(configuracionService.update).toHaveBeenCalledWith(expect.objectContaining(configuracion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConfiguracion>>();
      const configuracion = { id: 123 };
      jest.spyOn(configuracionFormService, 'getConfiguracion').mockReturnValue({ id: null });
      jest.spyOn(configuracionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ configuracion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: configuracion }));
      saveSubject.complete();

      // THEN
      expect(configuracionFormService.getConfiguracion).toHaveBeenCalled();
      expect(configuracionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConfiguracion>>();
      const configuracion = { id: 123 };
      jest.spyOn(configuracionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ configuracion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(configuracionService.update).toHaveBeenCalled();
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
