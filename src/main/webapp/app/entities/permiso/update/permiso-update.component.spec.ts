import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PermisoFormService } from './permiso-form.service';
import { PermisoService } from '../service/permiso.service';
import { IPermiso } from '../permiso.model';
import { IRol } from 'app/entities/rol/rol.model';
import { RolService } from 'app/entities/rol/service/rol.service';

import { PermisoUpdateComponent } from './permiso-update.component';

describe('Permiso Management Update Component', () => {
  let comp: PermisoUpdateComponent;
  let fixture: ComponentFixture<PermisoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let permisoFormService: PermisoFormService;
  let permisoService: PermisoService;
  let rolService: RolService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PermisoUpdateComponent],
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
      .overrideTemplate(PermisoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PermisoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    permisoFormService = TestBed.inject(PermisoFormService);
    permisoService = TestBed.inject(PermisoService);
    rolService = TestBed.inject(RolService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Rol query and add missing value', () => {
      const permiso: IPermiso = { id: 456 };
      const rol: IRol = { id: 66600 };
      permiso.rol = rol;

      const rolCollection: IRol[] = [{ id: 58940 }];
      jest.spyOn(rolService, 'query').mockReturnValue(of(new HttpResponse({ body: rolCollection })));
      const additionalRols = [rol];
      const expectedCollection: IRol[] = [...additionalRols, ...rolCollection];
      jest.spyOn(rolService, 'addRolToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ permiso });
      comp.ngOnInit();

      expect(rolService.query).toHaveBeenCalled();
      expect(rolService.addRolToCollectionIfMissing).toHaveBeenCalledWith(rolCollection, ...additionalRols.map(expect.objectContaining));
      expect(comp.rolsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const permiso: IPermiso = { id: 456 };
      const rol: IRol = { id: 48314 };
      permiso.rol = rol;

      activatedRoute.data = of({ permiso });
      comp.ngOnInit();

      expect(comp.rolsSharedCollection).toContain(rol);
      expect(comp.permiso).toEqual(permiso);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPermiso>>();
      const permiso = { id: 123 };
      jest.spyOn(permisoFormService, 'getPermiso').mockReturnValue(permiso);
      jest.spyOn(permisoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permiso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: permiso }));
      saveSubject.complete();

      // THEN
      expect(permisoFormService.getPermiso).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(permisoService.update).toHaveBeenCalledWith(expect.objectContaining(permiso));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPermiso>>();
      const permiso = { id: 123 };
      jest.spyOn(permisoFormService, 'getPermiso').mockReturnValue({ id: null });
      jest.spyOn(permisoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permiso: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: permiso }));
      saveSubject.complete();

      // THEN
      expect(permisoFormService.getPermiso).toHaveBeenCalled();
      expect(permisoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPermiso>>();
      const permiso = { id: 123 };
      jest.spyOn(permisoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permiso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(permisoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
