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

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';
import { ProyectoService } from 'app/entities/proyecto/service/proyecto.service';

import { BitacoraUpdateComponent } from './bitacora-update.component';

describe('Bitacora Management Update Component', () => {
  let comp: BitacoraUpdateComponent;
  let fixture: ComponentFixture<BitacoraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bitacoraFormService: BitacoraFormService;
  let bitacoraService: BitacoraService;
  let userService: UserService;
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
    userService = TestBed.inject(UserService);
    proyectoService = TestBed.inject(ProyectoService);

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
      const user: IUser = { id: 28738 };
      bitacora.user = user;
      const proyecto: IProyecto = { id: 73132 };
      bitacora.proyecto = proyecto;

      activatedRoute.data = of({ bitacora });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
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
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
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
