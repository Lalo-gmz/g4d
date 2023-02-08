import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UsuarioFormService } from './usuario-form.service';
import { UsuarioService } from '../service/usuario.service';
import { IUsuario } from '../usuario.model';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { FuncionalidadService } from 'app/entities/funcionalidad/service/funcionalidad.service';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';
import { ProyectoService } from 'app/entities/proyecto/service/proyecto.service';
import { IRol } from 'app/entities/rol/rol.model';
import { RolService } from 'app/entities/rol/service/rol.service';

import { UsuarioUpdateComponent } from './usuario-update.component';

describe('Usuario Management Update Component', () => {
  let comp: UsuarioUpdateComponent;
  let fixture: ComponentFixture<UsuarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let usuarioFormService: UsuarioFormService;
  let usuarioService: UsuarioService;
  let funcionalidadService: FuncionalidadService;
  let proyectoService: ProyectoService;
  let rolService: RolService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UsuarioUpdateComponent],
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
      .overrideTemplate(UsuarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UsuarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    usuarioFormService = TestBed.inject(UsuarioFormService);
    usuarioService = TestBed.inject(UsuarioService);
    funcionalidadService = TestBed.inject(FuncionalidadService);
    proyectoService = TestBed.inject(ProyectoService);
    rolService = TestBed.inject(RolService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Funcionalidad query and add missing value', () => {
      const usuario: IUsuario = { id: 456 };
      const funcionalidad: IFuncionalidad = { id: 1395 };
      usuario.funcionalidad = funcionalidad;

      const funcionalidadCollection: IFuncionalidad[] = [{ id: 41132 }];
      jest.spyOn(funcionalidadService, 'query').mockReturnValue(of(new HttpResponse({ body: funcionalidadCollection })));
      const additionalFuncionalidads = [funcionalidad];
      const expectedCollection: IFuncionalidad[] = [...additionalFuncionalidads, ...funcionalidadCollection];
      jest.spyOn(funcionalidadService, 'addFuncionalidadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      expect(funcionalidadService.query).toHaveBeenCalled();
      expect(funcionalidadService.addFuncionalidadToCollectionIfMissing).toHaveBeenCalledWith(
        funcionalidadCollection,
        ...additionalFuncionalidads.map(expect.objectContaining)
      );
      expect(comp.funcionalidadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Proyecto query and add missing value', () => {
      const usuario: IUsuario = { id: 456 };
      const proyecto: IProyecto = { id: 84435 };
      usuario.proyecto = proyecto;

      const proyectoCollection: IProyecto[] = [{ id: 66996 }];
      jest.spyOn(proyectoService, 'query').mockReturnValue(of(new HttpResponse({ body: proyectoCollection })));
      const additionalProyectos = [proyecto];
      const expectedCollection: IProyecto[] = [...additionalProyectos, ...proyectoCollection];
      jest.spyOn(proyectoService, 'addProyectoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      expect(proyectoService.query).toHaveBeenCalled();
      expect(proyectoService.addProyectoToCollectionIfMissing).toHaveBeenCalledWith(
        proyectoCollection,
        ...additionalProyectos.map(expect.objectContaining)
      );
      expect(comp.proyectosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Rol query and add missing value', () => {
      const usuario: IUsuario = { id: 456 };
      const rol: IRol = { id: 48518 };
      usuario.rol = rol;

      const rolCollection: IRol[] = [{ id: 64498 }];
      jest.spyOn(rolService, 'query').mockReturnValue(of(new HttpResponse({ body: rolCollection })));
      const additionalRols = [rol];
      const expectedCollection: IRol[] = [...additionalRols, ...rolCollection];
      jest.spyOn(rolService, 'addRolToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      expect(rolService.query).toHaveBeenCalled();
      expect(rolService.addRolToCollectionIfMissing).toHaveBeenCalledWith(rolCollection, ...additionalRols.map(expect.objectContaining));
      expect(comp.rolsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const usuario: IUsuario = { id: 456 };
      const funcionalidad: IFuncionalidad = { id: 92882 };
      usuario.funcionalidad = funcionalidad;
      const proyecto: IProyecto = { id: 35694 };
      usuario.proyecto = proyecto;
      const rol: IRol = { id: 76029 };
      usuario.rol = rol;

      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      expect(comp.funcionalidadsSharedCollection).toContain(funcionalidad);
      expect(comp.proyectosSharedCollection).toContain(proyecto);
      expect(comp.rolsSharedCollection).toContain(rol);
      expect(comp.usuario).toEqual(usuario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUsuario>>();
      const usuario = { id: 123 };
      jest.spyOn(usuarioFormService, 'getUsuario').mockReturnValue(usuario);
      jest.spyOn(usuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: usuario }));
      saveSubject.complete();

      // THEN
      expect(usuarioFormService.getUsuario).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(usuarioService.update).toHaveBeenCalledWith(expect.objectContaining(usuario));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUsuario>>();
      const usuario = { id: 123 };
      jest.spyOn(usuarioFormService, 'getUsuario').mockReturnValue({ id: null });
      jest.spyOn(usuarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuario: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: usuario }));
      saveSubject.complete();

      // THEN
      expect(usuarioFormService.getUsuario).toHaveBeenCalled();
      expect(usuarioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUsuario>>();
      const usuario = { id: 123 };
      jest.spyOn(usuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(usuarioService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFuncionalidad', () => {
      it('Should forward to funcionalidadService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(funcionalidadService, 'compareFuncionalidad');
        comp.compareFuncionalidad(entity, entity2);
        expect(funcionalidadService.compareFuncionalidad).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProyecto', () => {
      it('Should forward to proyectoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(proyectoService, 'compareProyecto');
        comp.compareProyecto(entity, entity2);
        expect(proyectoService.compareProyecto).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRol', () => {
      it('Should forward to rolService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(rolService, 'compareRol');
        comp.compareRol(entity, entity2);
        expect(rolService.compareRol).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
