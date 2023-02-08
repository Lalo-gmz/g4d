import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ComentarioFormService } from './comentario-form.service';
import { ComentarioService } from '../service/comentario.service';
import { IComentario } from '../comentario.model';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { FuncionalidadService } from 'app/entities/funcionalidad/service/funcionalidad.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

import { ComentarioUpdateComponent } from './comentario-update.component';

describe('Comentario Management Update Component', () => {
  let comp: ComentarioUpdateComponent;
  let fixture: ComponentFixture<ComentarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let comentarioFormService: ComentarioFormService;
  let comentarioService: ComentarioService;
  let funcionalidadService: FuncionalidadService;
  let usuarioService: UsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ComentarioUpdateComponent],
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
      .overrideTemplate(ComentarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ComentarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    comentarioFormService = TestBed.inject(ComentarioFormService);
    comentarioService = TestBed.inject(ComentarioService);
    funcionalidadService = TestBed.inject(FuncionalidadService);
    usuarioService = TestBed.inject(UsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Funcionalidad query and add missing value', () => {
      const comentario: IComentario = { id: 456 };
      const funcionalidad: IFuncionalidad = { id: 91458 };
      comentario.funcionalidad = funcionalidad;

      const funcionalidadCollection: IFuncionalidad[] = [{ id: 66956 }];
      jest.spyOn(funcionalidadService, 'query').mockReturnValue(of(new HttpResponse({ body: funcionalidadCollection })));
      const additionalFuncionalidads = [funcionalidad];
      const expectedCollection: IFuncionalidad[] = [...additionalFuncionalidads, ...funcionalidadCollection];
      jest.spyOn(funcionalidadService, 'addFuncionalidadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ comentario });
      comp.ngOnInit();

      expect(funcionalidadService.query).toHaveBeenCalled();
      expect(funcionalidadService.addFuncionalidadToCollectionIfMissing).toHaveBeenCalledWith(
        funcionalidadCollection,
        ...additionalFuncionalidads.map(expect.objectContaining)
      );
      expect(comp.funcionalidadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Usuario query and add missing value', () => {
      const comentario: IComentario = { id: 456 };
      const usuario: IUsuario = { id: 51644 };
      comentario.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 11467 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ comentario });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(
        usuarioCollection,
        ...additionalUsuarios.map(expect.objectContaining)
      );
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const comentario: IComentario = { id: 456 };
      const funcionalidad: IFuncionalidad = { id: 9848 };
      comentario.funcionalidad = funcionalidad;
      const usuario: IUsuario = { id: 87143 };
      comentario.usuario = usuario;

      activatedRoute.data = of({ comentario });
      comp.ngOnInit();

      expect(comp.funcionalidadsSharedCollection).toContain(funcionalidad);
      expect(comp.usuariosSharedCollection).toContain(usuario);
      expect(comp.comentario).toEqual(comentario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IComentario>>();
      const comentario = { id: 123 };
      jest.spyOn(comentarioFormService, 'getComentario').mockReturnValue(comentario);
      jest.spyOn(comentarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comentario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: comentario }));
      saveSubject.complete();

      // THEN
      expect(comentarioFormService.getComentario).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(comentarioService.update).toHaveBeenCalledWith(expect.objectContaining(comentario));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IComentario>>();
      const comentario = { id: 123 };
      jest.spyOn(comentarioFormService, 'getComentario').mockReturnValue({ id: null });
      jest.spyOn(comentarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comentario: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: comentario }));
      saveSubject.complete();

      // THEN
      expect(comentarioFormService.getComentario).toHaveBeenCalled();
      expect(comentarioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IComentario>>();
      const comentario = { id: 123 };
      jest.spyOn(comentarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comentario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(comentarioService.update).toHaveBeenCalled();
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

    describe('compareUsuario', () => {
      it('Should forward to usuarioService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(usuarioService, 'compareUsuario');
        comp.compareUsuario(entity, entity2);
        expect(usuarioService.compareUsuario).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
