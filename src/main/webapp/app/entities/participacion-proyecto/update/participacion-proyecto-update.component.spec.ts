import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ParticipacionProyectoFormService } from './participacion-proyecto-form.service';
import { ParticipacionProyectoService } from '../service/participacion-proyecto.service';
import { IParticipacionProyecto } from '../participacion-proyecto.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';
import { ProyectoService } from 'app/entities/proyecto/service/proyecto.service';
import { IRol } from 'app/entities/rol/rol.model';
import { RolService } from 'app/entities/rol/service/rol.service';

import { ParticipacionProyectoUpdateComponent } from './participacion-proyecto-update.component';

describe('ParticipacionProyecto Management Update Component', () => {
  let comp: ParticipacionProyectoUpdateComponent;
  let fixture: ComponentFixture<ParticipacionProyectoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let participacionProyectoFormService: ParticipacionProyectoFormService;
  let participacionProyectoService: ParticipacionProyectoService;
  let userService: UserService;
  let proyectoService: ProyectoService;
  let rolService: RolService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ParticipacionProyectoUpdateComponent],
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
      .overrideTemplate(ParticipacionProyectoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParticipacionProyectoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    participacionProyectoFormService = TestBed.inject(ParticipacionProyectoFormService);
    participacionProyectoService = TestBed.inject(ParticipacionProyectoService);
    userService = TestBed.inject(UserService);
    proyectoService = TestBed.inject(ProyectoService);
    rolService = TestBed.inject(RolService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const participacionProyecto: IParticipacionProyecto = { id: 456 };
      const user: IUser = { id: 4009 };
      participacionProyecto.user = user;

      const userCollection: IUser[] = [{ id: 82105 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ participacionProyecto });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Proyecto query and add missing value', () => {
      const participacionProyecto: IParticipacionProyecto = { id: 456 };
      const proyecto: IProyecto = { id: 31805 };
      participacionProyecto.proyecto = proyecto;

      const proyectoCollection: IProyecto[] = [{ id: 27466 }];
      jest.spyOn(proyectoService, 'query').mockReturnValue(of(new HttpResponse({ body: proyectoCollection })));
      const additionalProyectos = [proyecto];
      const expectedCollection: IProyecto[] = [...additionalProyectos, ...proyectoCollection];
      jest.spyOn(proyectoService, 'addProyectoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ participacionProyecto });
      comp.ngOnInit();

      expect(proyectoService.query).toHaveBeenCalled();
      expect(proyectoService.addProyectoToCollectionIfMissing).toHaveBeenCalledWith(
        proyectoCollection,
        ...additionalProyectos.map(expect.objectContaining)
      );
      expect(comp.proyectosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Rol query and add missing value', () => {
      const participacionProyecto: IParticipacionProyecto = { id: 456 };
      const rol: IRol = { id: 15383 };
      participacionProyecto.rol = rol;

      const rolCollection: IRol[] = [{ id: 98234 }];
      jest.spyOn(rolService, 'query').mockReturnValue(of(new HttpResponse({ body: rolCollection })));
      const additionalRols = [rol];
      const expectedCollection: IRol[] = [...additionalRols, ...rolCollection];
      jest.spyOn(rolService, 'addRolToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ participacionProyecto });
      comp.ngOnInit();

      expect(rolService.query).toHaveBeenCalled();
      expect(rolService.addRolToCollectionIfMissing).toHaveBeenCalledWith(rolCollection, ...additionalRols.map(expect.objectContaining));
      expect(comp.rolsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const participacionProyecto: IParticipacionProyecto = { id: 456 };
      const user: IUser = { id: 1071 };
      participacionProyecto.user = user;
      const proyecto: IProyecto = { id: 83369 };
      participacionProyecto.proyecto = proyecto;
      const rol: IRol = { id: 31925 };
      participacionProyecto.rol = rol;

      activatedRoute.data = of({ participacionProyecto });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.proyectosSharedCollection).toContain(proyecto);
      expect(comp.rolsSharedCollection).toContain(rol);
      expect(comp.participacionProyecto).toEqual(participacionProyecto);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParticipacionProyecto>>();
      const participacionProyecto = { id: 123 };
      jest.spyOn(participacionProyectoFormService, 'getParticipacionProyecto').mockReturnValue(participacionProyecto);
      jest.spyOn(participacionProyectoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ participacionProyecto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: participacionProyecto }));
      saveSubject.complete();

      // THEN
      expect(participacionProyectoFormService.getParticipacionProyecto).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(participacionProyectoService.update).toHaveBeenCalledWith(expect.objectContaining(participacionProyecto));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParticipacionProyecto>>();
      const participacionProyecto = { id: 123 };
      jest.spyOn(participacionProyectoFormService, 'getParticipacionProyecto').mockReturnValue({ id: null });
      jest.spyOn(participacionProyectoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ participacionProyecto: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: participacionProyecto }));
      saveSubject.complete();

      // THEN
      expect(participacionProyectoFormService.getParticipacionProyecto).toHaveBeenCalled();
      expect(participacionProyectoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParticipacionProyecto>>();
      const participacionProyecto = { id: 123 };
      jest.spyOn(participacionProyectoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ participacionProyecto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(participacionProyectoService.update).toHaveBeenCalled();
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
