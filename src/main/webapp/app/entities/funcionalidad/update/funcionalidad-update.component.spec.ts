import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FuncionalidadFormService } from './funcionalidad-form.service';
import { FuncionalidadService } from '../service/funcionalidad.service';
import { IFuncionalidad } from '../funcionalidad.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEstatusFuncionalidad } from 'app/entities/estatus-funcionalidad/estatus-funcionalidad.model';
import { EstatusFuncionalidadService } from 'app/entities/estatus-funcionalidad/service/estatus-funcionalidad.service';
import { IIteracion } from 'app/entities/iteracion/iteracion.model';
import { IteracionService } from 'app/entities/iteracion/service/iteracion.service';
import { IPrioridad } from 'app/entities/prioridad/prioridad.model';
import { PrioridadService } from 'app/entities/prioridad/service/prioridad.service';

import { FuncionalidadUpdateComponent } from './funcionalidad-update.component';

describe('Funcionalidad Management Update Component', () => {
  let comp: FuncionalidadUpdateComponent;
  let fixture: ComponentFixture<FuncionalidadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let funcionalidadFormService: FuncionalidadFormService;
  let funcionalidadService: FuncionalidadService;
  let userService: UserService;
  let estatusFuncionalidadService: EstatusFuncionalidadService;
  let iteracionService: IteracionService;
  let prioridadService: PrioridadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FuncionalidadUpdateComponent],
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
      .overrideTemplate(FuncionalidadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FuncionalidadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    funcionalidadFormService = TestBed.inject(FuncionalidadFormService);
    funcionalidadService = TestBed.inject(FuncionalidadService);
    userService = TestBed.inject(UserService);
    estatusFuncionalidadService = TestBed.inject(EstatusFuncionalidadService);
    iteracionService = TestBed.inject(IteracionService);
    prioridadService = TestBed.inject(PrioridadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const funcionalidad: IFuncionalidad = { id: 456 };
      const users: IUser[] = [{ id: 26964 }];
      funcionalidad.users = users;

      const userCollection: IUser[] = [{ id: 52411 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [...users];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ funcionalidad });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call EstatusFuncionalidad query and add missing value', () => {
      const funcionalidad: IFuncionalidad = { id: 456 };
      const estatusFuncionalidad: IEstatusFuncionalidad = { id: 57521 };
      funcionalidad.estatusFuncionalidad = estatusFuncionalidad;

      const estatusFuncionalidadCollection: IEstatusFuncionalidad[] = [{ id: 93445 }];
      jest.spyOn(estatusFuncionalidadService, 'query').mockReturnValue(of(new HttpResponse({ body: estatusFuncionalidadCollection })));
      const additionalEstatusFuncionalidads = [estatusFuncionalidad];
      const expectedCollection: IEstatusFuncionalidad[] = [...additionalEstatusFuncionalidads, ...estatusFuncionalidadCollection];
      jest.spyOn(estatusFuncionalidadService, 'addEstatusFuncionalidadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ funcionalidad });
      comp.ngOnInit();

      expect(estatusFuncionalidadService.query).toHaveBeenCalled();
      expect(estatusFuncionalidadService.addEstatusFuncionalidadToCollectionIfMissing).toHaveBeenCalledWith(
        estatusFuncionalidadCollection,
        ...additionalEstatusFuncionalidads.map(expect.objectContaining)
      );
      expect(comp.estatusFuncionalidadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Iteracion query and add missing value', () => {
      const funcionalidad: IFuncionalidad = { id: 456 };
      const iteracion: IIteracion = { id: 67949 };
      funcionalidad.iteracion = iteracion;

      const iteracionCollection: IIteracion[] = [{ id: 15328 }];
      jest.spyOn(iteracionService, 'query').mockReturnValue(of(new HttpResponse({ body: iteracionCollection })));
      const additionalIteracions = [iteracion];
      const expectedCollection: IIteracion[] = [...additionalIteracions, ...iteracionCollection];
      jest.spyOn(iteracionService, 'addIteracionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ funcionalidad });
      comp.ngOnInit();

      expect(iteracionService.query).toHaveBeenCalled();
      expect(iteracionService.addIteracionToCollectionIfMissing).toHaveBeenCalledWith(
        iteracionCollection,
        ...additionalIteracions.map(expect.objectContaining)
      );
      expect(comp.iteracionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Prioridad query and add missing value', () => {
      const funcionalidad: IFuncionalidad = { id: 456 };
      const prioridad: IPrioridad = { id: 59684 };
      funcionalidad.prioridad = prioridad;

      const prioridadCollection: IPrioridad[] = [{ id: 28322 }];
      jest.spyOn(prioridadService, 'query').mockReturnValue(of(new HttpResponse({ body: prioridadCollection })));
      const additionalPrioridads = [prioridad];
      const expectedCollection: IPrioridad[] = [...additionalPrioridads, ...prioridadCollection];
      jest.spyOn(prioridadService, 'addPrioridadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ funcionalidad });
      comp.ngOnInit();

      expect(prioridadService.query).toHaveBeenCalled();
      expect(prioridadService.addPrioridadToCollectionIfMissing).toHaveBeenCalledWith(
        prioridadCollection,
        ...additionalPrioridads.map(expect.objectContaining)
      );
      expect(comp.prioridadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const funcionalidad: IFuncionalidad = { id: 456 };
      const user: IUser = { id: 5477 };
      funcionalidad.users = [user];
      const estatusFuncionalidad: IEstatusFuncionalidad = { id: 47684 };
      funcionalidad.estatusFuncionalidad = estatusFuncionalidad;
      const iteracion: IIteracion = { id: 24899 };
      funcionalidad.iteracion = iteracion;
      const prioridad: IPrioridad = { id: 36124 };
      funcionalidad.prioridad = prioridad;

      activatedRoute.data = of({ funcionalidad });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.estatusFuncionalidadsSharedCollection).toContain(estatusFuncionalidad);
      expect(comp.iteracionsSharedCollection).toContain(iteracion);
      expect(comp.prioridadsSharedCollection).toContain(prioridad);
      expect(comp.funcionalidad).toEqual(funcionalidad);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFuncionalidad>>();
      const funcionalidad = { id: 123 };
      jest.spyOn(funcionalidadFormService, 'getFuncionalidad').mockReturnValue(funcionalidad);
      jest.spyOn(funcionalidadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ funcionalidad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: funcionalidad }));
      saveSubject.complete();

      // THEN
      expect(funcionalidadFormService.getFuncionalidad).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(funcionalidadService.update).toHaveBeenCalledWith(expect.objectContaining(funcionalidad));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFuncionalidad>>();
      const funcionalidad = { id: 123 };
      jest.spyOn(funcionalidadFormService, 'getFuncionalidad').mockReturnValue({ id: null });
      jest.spyOn(funcionalidadService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ funcionalidad: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: funcionalidad }));
      saveSubject.complete();

      // THEN
      expect(funcionalidadFormService.getFuncionalidad).toHaveBeenCalled();
      expect(funcionalidadService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFuncionalidad>>();
      const funcionalidad = { id: 123 };
      jest.spyOn(funcionalidadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ funcionalidad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(funcionalidadService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEstatusFuncionalidad', () => {
      it('Should forward to estatusFuncionalidadService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(estatusFuncionalidadService, 'compareEstatusFuncionalidad');
        comp.compareEstatusFuncionalidad(entity, entity2);
        expect(estatusFuncionalidadService.compareEstatusFuncionalidad).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareIteracion', () => {
      it('Should forward to iteracionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(iteracionService, 'compareIteracion');
        comp.compareIteracion(entity, entity2);
        expect(iteracionService.compareIteracion).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePrioridad', () => {
      it('Should forward to prioridadService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(prioridadService, 'comparePrioridad');
        comp.comparePrioridad(entity, entity2);
        expect(prioridadService.comparePrioridad).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
