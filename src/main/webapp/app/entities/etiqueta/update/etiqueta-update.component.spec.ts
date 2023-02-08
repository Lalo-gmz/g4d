import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EtiquetaFormService } from './etiqueta-form.service';
import { EtiquetaService } from '../service/etiqueta.service';
import { IEtiqueta } from '../etiqueta.model';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { FuncionalidadService } from 'app/entities/funcionalidad/service/funcionalidad.service';

import { EtiquetaUpdateComponent } from './etiqueta-update.component';

describe('Etiqueta Management Update Component', () => {
  let comp: EtiquetaUpdateComponent;
  let fixture: ComponentFixture<EtiquetaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let etiquetaFormService: EtiquetaFormService;
  let etiquetaService: EtiquetaService;
  let funcionalidadService: FuncionalidadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EtiquetaUpdateComponent],
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
      .overrideTemplate(EtiquetaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EtiquetaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    etiquetaFormService = TestBed.inject(EtiquetaFormService);
    etiquetaService = TestBed.inject(EtiquetaService);
    funcionalidadService = TestBed.inject(FuncionalidadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Funcionalidad query and add missing value', () => {
      const etiqueta: IEtiqueta = { id: 456 };
      const funcionalidad: IFuncionalidad = { id: 4465 };
      etiqueta.funcionalidad = funcionalidad;

      const funcionalidadCollection: IFuncionalidad[] = [{ id: 78960 }];
      jest.spyOn(funcionalidadService, 'query').mockReturnValue(of(new HttpResponse({ body: funcionalidadCollection })));
      const additionalFuncionalidads = [funcionalidad];
      const expectedCollection: IFuncionalidad[] = [...additionalFuncionalidads, ...funcionalidadCollection];
      jest.spyOn(funcionalidadService, 'addFuncionalidadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ etiqueta });
      comp.ngOnInit();

      expect(funcionalidadService.query).toHaveBeenCalled();
      expect(funcionalidadService.addFuncionalidadToCollectionIfMissing).toHaveBeenCalledWith(
        funcionalidadCollection,
        ...additionalFuncionalidads.map(expect.objectContaining)
      );
      expect(comp.funcionalidadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const etiqueta: IEtiqueta = { id: 456 };
      const funcionalidad: IFuncionalidad = { id: 98933 };
      etiqueta.funcionalidad = funcionalidad;

      activatedRoute.data = of({ etiqueta });
      comp.ngOnInit();

      expect(comp.funcionalidadsSharedCollection).toContain(funcionalidad);
      expect(comp.etiqueta).toEqual(etiqueta);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEtiqueta>>();
      const etiqueta = { id: 123 };
      jest.spyOn(etiquetaFormService, 'getEtiqueta').mockReturnValue(etiqueta);
      jest.spyOn(etiquetaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ etiqueta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: etiqueta }));
      saveSubject.complete();

      // THEN
      expect(etiquetaFormService.getEtiqueta).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(etiquetaService.update).toHaveBeenCalledWith(expect.objectContaining(etiqueta));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEtiqueta>>();
      const etiqueta = { id: 123 };
      jest.spyOn(etiquetaFormService, 'getEtiqueta').mockReturnValue({ id: null });
      jest.spyOn(etiquetaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ etiqueta: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: etiqueta }));
      saveSubject.complete();

      // THEN
      expect(etiquetaFormService.getEtiqueta).toHaveBeenCalled();
      expect(etiquetaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEtiqueta>>();
      const etiqueta = { id: 123 };
      jest.spyOn(etiquetaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ etiqueta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(etiquetaService.update).toHaveBeenCalled();
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
