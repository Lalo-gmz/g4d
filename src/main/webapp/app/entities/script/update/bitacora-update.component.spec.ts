import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BitacoraFormService } from './script-form.service';
import { BitacoraService } from '../service/script.service';
import { IBitacora } from '../script.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { FuncionalidadService } from 'app/entities/funcionalidad/service/funcionalidad.service';

import { BitacoraUpdateComponent } from './script-update.component';

describe('Bitacora Management Update Component', () => {
  let comp: BitacoraUpdateComponent;
  let fixture: ComponentFixture<BitacoraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bitacoraFormService: BitacoraFormService;
  let bitacoraService: BitacoraService;
  let userService: UserService;
  let funcionalidadService: FuncionalidadService;

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
    userService = TestBed.inject(UserService);
    funcionalidadService = TestBed.inject(FuncionalidadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const bitacora: IBitacora = { id: 456 };
      const user: IUser = { id: 79838 };
      bitacora.user = user;

      const userCollection: IUser[] = [{ id: 33701 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bitacora });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Funcionalidad query and add missing value', () => {
      const bitacora: IBitacora = { id: 456 };
      const funcionalidad: IFuncionalidad = { id: 32970 };
      bitacora.funcionalidad = funcionalidad;

      const funcionalidadCollection: IFuncionalidad[] = [{ id: 19974 }];
      jest.spyOn(funcionalidadService, 'query').mockReturnValue(of(new HttpResponse({ body: funcionalidadCollection })));
      const additionalFuncionalidads = [funcionalidad];
      const expectedCollection: IFuncionalidad[] = [...additionalFuncionalidads, ...funcionalidadCollection];
      jest.spyOn(funcionalidadService, 'addFuncionalidadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bitacora });
      comp.ngOnInit();

      expect(funcionalidadService.query).toHaveBeenCalled();
      expect(funcionalidadService.addFuncionalidadToCollectionIfMissing).toHaveBeenCalledWith(
        funcionalidadCollection,
        ...additionalFuncionalidads.map(expect.objectContaining)
      );
      expect(comp.funcionalidadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bitacora: IBitacora = { id: 456 };
      const user: IUser = { id: 28738 };
      bitacora.user = user;
      const funcionalidad: IFuncionalidad = { id: 98459 };
      bitacora.funcionalidad = funcionalidad;

      activatedRoute.data = of({ bitacora });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.funcionalidadsSharedCollection).toContain(funcionalidad);
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
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFuncionalidad', () => {
      it('Should forward to funcionalidadService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(funcionalidadService, 'compareFuncionalidad');
        comp.compareFuncionalidad(entity, entity2);
        expect(funcionalidadService.compareFuncionalidad).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
