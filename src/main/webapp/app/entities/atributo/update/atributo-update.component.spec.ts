import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AtributoFormService } from './atributo-form.service';
import { AtributoService } from '../service/atributo.service';
import { IAtributo } from '../atributo.model';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { FuncionalidadService } from 'app/entities/funcionalidad/service/funcionalidad.service';

import { AtributoUpdateComponent } from './atributo-update.component';

describe('Atributo Management Update Component', () => {
  let comp: AtributoUpdateComponent;
  let fixture: ComponentFixture<AtributoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let atributoFormService: AtributoFormService;
  let atributoService: AtributoService;
  let funcionalidadService: FuncionalidadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AtributoUpdateComponent],
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
      .overrideTemplate(AtributoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AtributoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    atributoFormService = TestBed.inject(AtributoFormService);
    atributoService = TestBed.inject(AtributoService);
    funcionalidadService = TestBed.inject(FuncionalidadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Funcionalidad query and add missing value', () => {
      const atributo: IAtributo = { id: 456 };
      const funcionalidad: IFuncionalidad = { id: 89582 };
      atributo.funcionalidad = funcionalidad;

      const funcionalidadCollection: IFuncionalidad[] = [{ id: 88061 }];
      jest.spyOn(funcionalidadService, 'query').mockReturnValue(of(new HttpResponse({ body: funcionalidadCollection })));
      const additionalFuncionalidads = [funcionalidad];
      const expectedCollection: IFuncionalidad[] = [...additionalFuncionalidads, ...funcionalidadCollection];
      jest.spyOn(funcionalidadService, 'addFuncionalidadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ atributo });
      comp.ngOnInit();

      expect(funcionalidadService.query).toHaveBeenCalled();
      expect(funcionalidadService.addFuncionalidadToCollectionIfMissing).toHaveBeenCalledWith(
        funcionalidadCollection,
        ...additionalFuncionalidads.map(expect.objectContaining)
      );
      expect(comp.funcionalidadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const atributo: IAtributo = { id: 456 };
      const funcionalidad: IFuncionalidad = { id: 71759 };
      atributo.funcionalidad = funcionalidad;

      activatedRoute.data = of({ atributo });
      comp.ngOnInit();

      expect(comp.funcionalidadsSharedCollection).toContain(funcionalidad);
      expect(comp.atributo).toEqual(atributo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAtributo>>();
      const atributo = { id: 123 };
      jest.spyOn(atributoFormService, 'getAtributo').mockReturnValue(atributo);
      jest.spyOn(atributoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ atributo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: atributo }));
      saveSubject.complete();

      // THEN
      expect(atributoFormService.getAtributo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(atributoService.update).toHaveBeenCalledWith(expect.objectContaining(atributo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAtributo>>();
      const atributo = { id: 123 };
      jest.spyOn(atributoFormService, 'getAtributo').mockReturnValue({ id: null });
      jest.spyOn(atributoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ atributo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: atributo }));
      saveSubject.complete();

      // THEN
      expect(atributoFormService.getAtributo).toHaveBeenCalled();
      expect(atributoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAtributo>>();
      const atributo = { id: 123 };
      jest.spyOn(atributoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ atributo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(atributoService.update).toHaveBeenCalled();
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
  });
});
