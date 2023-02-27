import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CapturaFormService } from './captura-form.service';
import { CapturaService } from '../service/captura.service';
import { ICaptura } from '../captura.model';
import { IIteracion } from 'app/entities/iteracion/iteracion.model';
import { IteracionService } from 'app/entities/iteracion/service/iteracion.service';

import { CapturaUpdateComponent } from './captura-update.component';

describe('Captura Management Update Component', () => {
  let comp: CapturaUpdateComponent;
  let fixture: ComponentFixture<CapturaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let capturaFormService: CapturaFormService;
  let capturaService: CapturaService;
  let iteracionService: IteracionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CapturaUpdateComponent],
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
      .overrideTemplate(CapturaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CapturaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    capturaFormService = TestBed.inject(CapturaFormService);
    capturaService = TestBed.inject(CapturaService);
    iteracionService = TestBed.inject(IteracionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Iteracion query and add missing value', () => {
      const captura: ICaptura = { id: 456 };
      const iteracion: IIteracion = { id: 61185 };
      captura.iteracion = iteracion;

      const iteracionCollection: IIteracion[] = [{ id: 38914 }];
      jest.spyOn(iteracionService, 'query').mockReturnValue(of(new HttpResponse({ body: iteracionCollection })));
      const additionalIteracions = [iteracion];
      const expectedCollection: IIteracion[] = [...additionalIteracions, ...iteracionCollection];
      jest.spyOn(iteracionService, 'addIteracionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ captura });
      comp.ngOnInit();

      expect(iteracionService.query).toHaveBeenCalled();
      expect(iteracionService.addIteracionToCollectionIfMissing).toHaveBeenCalledWith(
        iteracionCollection,
        ...additionalIteracions.map(expect.objectContaining)
      );
      expect(comp.iteracionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const captura: ICaptura = { id: 456 };
      const iteracion: IIteracion = { id: 89171 };
      captura.iteracion = iteracion;

      activatedRoute.data = of({ captura });
      comp.ngOnInit();

      expect(comp.iteracionsSharedCollection).toContain(iteracion);
      expect(comp.captura).toEqual(captura);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICaptura>>();
      const captura = { id: 123 };
      jest.spyOn(capturaFormService, 'getCaptura').mockReturnValue(captura);
      jest.spyOn(capturaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ captura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: captura }));
      saveSubject.complete();

      // THEN
      expect(capturaFormService.getCaptura).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(capturaService.update).toHaveBeenCalledWith(expect.objectContaining(captura));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICaptura>>();
      const captura = { id: 123 };
      jest.spyOn(capturaFormService, 'getCaptura').mockReturnValue({ id: null });
      jest.spyOn(capturaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ captura: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: captura }));
      saveSubject.complete();

      // THEN
      expect(capturaFormService.getCaptura).toHaveBeenCalled();
      expect(capturaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICaptura>>();
      const captura = { id: 123 };
      jest.spyOn(capturaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ captura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(capturaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareIteracion', () => {
      it('Should forward to iteracionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(iteracionService, 'compareIteracion');
        comp.compareIteracion(entity, entity2);
        expect(iteracionService.compareIteracion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
