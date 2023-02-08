import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BitacoraFormService } from './bitacora-form.service';
import { BitacoraService } from '../service/bitacora.service';
import { IBitacora } from '../bitacora.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';
import { ProyectoService } from 'app/entities/proyecto/service/proyecto.service';

import { BitacoraUpdateComponent } from './bitacora-update.component';

describe('Bitacora Management Update Component', () => {
  let comp: BitacoraUpdateComponent;
  let fixture: ComponentFixture<BitacoraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bitacoraFormService: BitacoraFormService;
  let bitacoraService: BitacoraService;
  let usuarioService: UsuarioService;
  let proyectoService: ProyectoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BitacoraUpdateComponent],
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
      .overrideTemplate(BitacoraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BitacoraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bitacoraFormService = TestBed.inject(BitacoraFormService);
    bitacoraService = TestBed.inject(BitacoraService);
    usuarioService = TestBed.inject(UsuarioService);
    proyectoService = TestBed.inject(ProyectoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Usuario query and add missing value', () => {
      const bitacora: IBitacora = { id: 456 };
      const usuario: IUsuario = { id: 67674 };
      bitacora.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 89254 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bitacora });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(
        usuarioCollection,
        ...additionalUsuarios.map(expect.objectContaining)
      );
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Proyecto query and add missing value', () => {
      const bitacora: IBitacora = { id: 456 };
      const proyecto: IProyecto = { id: 30077 };
      bitacora.proyecto = proyecto;

      const proyectoCollection: IProyecto[] = [{ id: 23657 }];
      jest.spyOn(proyectoService, 'query').mockReturnValue(of(new HttpResponse({ body: proyectoCollection })));
      const additionalProyectos = [proyecto];
      const expectedCollection: IProyecto[] = [...additionalProyectos, ...proyectoCollection];
      jest.spyOn(proyectoService, 'addProyectoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bitacora });
      comp.ngOnInit();

      expect(proyectoService.query).toHaveBeenCalled();
      expect(proyectoService.addProyectoToCollectionIfMissing).toHaveBeenCalledWith(
        proyectoCollection,
        ...additionalProyectos.map(expect.objectContaining)
      );
      expect(comp.proyectosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bitacora: IBitacora = { id: 456 };
      const usuario: IUsuario = { id: 2216 };
      bitacora.usuario = usuario;
      const proyecto: IProyecto = { id: 73132 };
      bitacora.proyecto = proyecto;

      activatedRoute.data = of({ bitacora });
      comp.ngOnInit();

      expect(comp.usuariosSharedCollection).toContain(usuario);
      expect(comp.proyectosSharedCollection).toContain(proyecto);
      expect(comp.bitacora).toEqual(bitacora);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBitacora>>();
      const bitacora = { id: 123 };
      jest.spyOn(bitacoraFormService, 'getBitacora').mockReturnValue(bitacora);
      jest.spyOn(bitacoraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bitacora });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bitacora }));
      saveSubject.complete();

      // THEN
      expect(bitacoraFormService.getBitacora).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bitacoraService.update).toHaveBeenCalledWith(expect.objectContaining(bitacora));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBitacora>>();
      const bitacora = { id: 123 };
      jest.spyOn(bitacoraFormService, 'getBitacora').mockReturnValue({ id: null });
      jest.spyOn(bitacoraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bitacora: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bitacora }));
      saveSubject.complete();

      // THEN
      expect(bitacoraFormService.getBitacora).toHaveBeenCalled();
      expect(bitacoraService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBitacora>>();
      const bitacora = { id: 123 };
      jest.spyOn(bitacoraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bitacora });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bitacoraService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUsuario', () => {
      it('Should forward to usuarioService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(usuarioService, 'compareUsuario');
        comp.compareUsuario(entity, entity2);
        expect(usuarioService.compareUsuario).toHaveBeenCalledWith(entity, entity2);
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
  });
});
