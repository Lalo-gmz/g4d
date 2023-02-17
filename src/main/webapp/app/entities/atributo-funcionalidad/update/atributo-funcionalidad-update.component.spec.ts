import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AtributoFuncionalidadFormService } from './atributo-funcionalidad-form.service';
import { AtributoFuncionalidadService } from '../service/atributo-funcionalidad.service';
import { IAtributoFuncionalidad } from '../atributo-funcionalidad.model';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { FuncionalidadService } from 'app/entities/funcionalidad/service/funcionalidad.service';
import { IAtributo } from 'app/entities/atributo/atributo.model';
import { AtributoService } from 'app/entities/atributo/service/atributo.service';

import { AtributoFuncionalidadUpdateComponent } from './atributo-funcionalidad-update.component';

describe('AtributoFuncionalidad Management Update Component', () => {
  let comp: AtributoFuncionalidadUpdateComponent;
  let fixture: ComponentFixture<AtributoFuncionalidadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let atributoFuncionalidadFormService: AtributoFuncionalidadFormService;
  let atributoFuncionalidadService: AtributoFuncionalidadService;
  let funcionalidadService: FuncionalidadService;
  let atributoService: AtributoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AtributoFuncionalidadUpdateComponent],
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
      .overrideTemplate(AtributoFuncionalidadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AtributoFuncionalidadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    atributoFuncionalidadFormService = TestBed.inject(AtributoFuncionalidadFormService);
    atributoFuncionalidadService = TestBed.inject(AtributoFuncionalidadService);
    funcionalidadService = TestBed.inject(FuncionalidadService);
    atributoService = TestBed.inject(AtributoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Funcionalidad query and add missing value', () => {
      const atributoFuncionalidad: IAtributoFuncionalidad = { id: 456 };
      const funcionalidad: IFuncionalidad = { id: 34943 };
      atributoFuncionalidad.funcionalidad = funcionalidad;

      const funcionalidadCollection: IFuncionalidad[] = [{ id: 36230 }];
      jest.spyOn(funcionalidadService, 'query').mockReturnValue(of(new HttpResponse({ body: funcionalidadCollection })));
      const additionalFuncionalidads = [funcionalidad];
      const expectedCollection: IFuncionalidad[] = [...additionalFuncionalidads, ...funcionalidadCollection];
      jest.spyOn(funcionalidadService, 'addFuncionalidadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ atributoFuncionalidad });
      comp.ngOnInit();

      expect(funcionalidadService.query).toHaveBeenCalled();
      expect(funcionalidadService.addFuncionalidadToCollectionIfMissing).toHaveBeenCalledWith(
        funcionalidadCollection,
        ...additionalFuncionalidads.map(expect.objectContaining)
      );
      expect(comp.funcionalidadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Atributo query and add missing value', () => {
      const atributoFuncionalidad: IAtributoFuncionalidad = { id: 456 };
      const atributo: IAtributo = { id: 51540 };
      atributoFuncionalidad.atributo = atributo;

      const atributoCollection: IAtributo[] = [{ id: 17477 }];
      jest.spyOn(atributoService, 'query').mockReturnValue(of(new HttpResponse({ body: atributoCollection })));
      const additionalAtributos = [atributo];
      const expectedCollection: IAtributo[] = [...additionalAtributos, ...atributoCollection];
      jest.spyOn(atributoService, 'addAtributoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ atributoFuncionalidad });
      comp.ngOnInit();

      expect(atributoService.query).toHaveBeenCalled();
      expect(atributoService.addAtributoToCollectionIfMissing).toHaveBeenCalledWith(
        atributoCollection,
        ...additionalAtributos.map(expect.objectContaining)
      );
      expect(comp.atributosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const atributoFuncionalidad: IAtributoFuncionalidad = { id: 456 };
      const funcionalidad: IFuncionalidad = { id: 89048 };
      atributoFuncionalidad.funcionalidad = funcionalidad;
      const atributo: IAtributo = { id: 60570 };
      atributoFuncionalidad.atributo = atributo;

      activatedRoute.data = of({ atributoFuncionalidad });
      comp.ngOnInit();

      expect(comp.funcionalidadsSharedCollection).toContain(funcionalidad);
      expect(comp.atributosSharedCollection).toContain(atributo);
      expect(comp.atributoFuncionalidad).toEqual(atributoFuncionalidad);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAtributoFuncionalidad>>();
      const atributoFuncionalidad = { id: 123 };
      jest.spyOn(atributoFuncionalidadFormService, 'getAtributoFuncionalidad').mockReturnValue(atributoFuncionalidad);
      jest.spyOn(atributoFuncionalidadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ atributoFuncionalidad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: atributoFuncionalidad }));
      saveSubject.complete();

      // THEN
      expect(atributoFuncionalidadFormService.getAtributoFuncionalidad).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(atributoFuncionalidadService.update).toHaveBeenCalledWith(expect.objectContaining(atributoFuncionalidad));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAtributoFuncionalidad>>();
      const atributoFuncionalidad = { id: 123 };
      jest.spyOn(atributoFuncionalidadFormService, 'getAtributoFuncionalidad').mockReturnValue({ id: null });
      jest.spyOn(atributoFuncionalidadService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ atributoFuncionalidad: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: atributoFuncionalidad }));
      saveSubject.complete();

      // THEN
      expect(atributoFuncionalidadFormService.getAtributoFuncionalidad).toHaveBeenCalled();
      expect(atributoFuncionalidadService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAtributoFuncionalidad>>();
      const atributoFuncionalidad = { id: 123 };
      jest.spyOn(atributoFuncionalidadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ atributoFuncionalidad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(atributoFuncionalidadService.update).toHaveBeenCalled();
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

    describe('compareAtributo', () => {
      it('Should forward to atributoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(atributoService, 'compareAtributo');
        comp.compareAtributo(entity, entity2);
        expect(atributoService.compareAtributo).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
